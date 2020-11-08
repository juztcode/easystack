package com.alternate.easystack.core.impl;

import com.alternate.easystack.common.utils.GSONCodec;
import com.alternate.easystack.core.DbService;
import com.alternate.easystack.core.TxItem;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Expected;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Put;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItem;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItemsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DynamoDbService implements DbService {

    private final static Logger logger = LoggerFactory.getLogger(DynamoDbService.class);

    private final AmazonDynamoDB db;
    private final Table table;

    public DynamoDbService(String tableName) {
        db = createDb();
        table = new DynamoDB(db).getTable(tableName);
    }

    @Override
    public TxItem get(String key) {
        GetItemSpec getItemSpec = createGetItemSpec(key);
        Item item = table.getItem(getItemSpec);

        if (item != null) {
            return toTxItem(item);
        } else {
            return null;
        }
    }

//    @Override
//    public Stream<TxItem> getAll(String type) {
//        QuerySpec querySpec = createQuerySpec(type);
//        Index typeIndex = table.getIndex("typeIndex");
//        ItemCollection<QueryOutcome> query = typeIndex.query(querySpec);
//        Spliterator<Item> spliterator = Spliterators.spliteratorUnknownSize(query.iterator(), Spliterator.ORDERED);
//        return StreamSupport.stream(spliterator, false).map(DynamoDbService::toTxItem);
//    }

    @Override
    public void save(Collection<TxItem> txItems) {
        TransactWriteItemsRequest txRequest = createTxWriteItemRequest(table.getTableName(), txItems);
        db.transactWriteItems(txRequest);
    }

    @Override
    public void save(TxItem txItem) {
        PutItemSpec putItemSpec = createPutItemSpec(txItem);
        table.putItem(putItemSpec);
    }

    public static void createTable(String tableName) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition("key", ScalarAttributeType.S));

        List<KeySchemaElement> ks = new ArrayList<>();
        ks.add(new KeySchemaElement("key", KeyType.HASH));

//        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();
//        indexKeySchema.add(new KeySchemaElement().withAttributeName("type").withKeyType(KeyType.HASH));  //Partition key
//        indexKeySchema.add(new KeySchemaElement().withAttributeName("key").withKeyType(KeyType.RANGE));  //Sort key
//
//        Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
//
//        LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex()
//                .withIndexName("typeIndex").withKeySchema(indexKeySchema).withProjection(projection);
//
//        ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<>();
//        localSecondaryIndexes.add(localSecondaryIndex);

        CreateTableRequest request = new CreateTableRequest()
                .withBillingMode(BillingMode.PAY_PER_REQUEST)
                .withTableName(tableName)
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(ks);
//                .withLocalSecondaryIndexes(localSecondaryIndexes);

        AmazonDynamoDB db = createDb();

        try {
            db.createTable(request);
        } catch (ResourceInUseException e) {
            logger.warn("table already available");
        }
    }

    private static TxItem toTxItem(Item item) {
        String key = item.getString("key");
        long version = Long.parseLong(item.getString("version"));
        String type = item.getString("type");
        String data = item.getString("data");

        Object payload = GSONCodec.decode(type, data);
        return new TxItem(key, payload, version);
    }

    private static AmazonDynamoDB createDb() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
                .build();
    }

    private static GetItemSpec createGetItemSpec(String key) {
        return new GetItemSpec()
                .withPrimaryKey("key", key);
    }

    private static PutItemSpec createPutItemSpec(TxItem item) {
        Item dbItem = new Item()
                .with("key", item.getKey())
                .with("version", String.valueOf(item.getVersion()))
                .with("type", item.getPayload().getClass().getName())
                .with("data", GSONCodec.encode(item.getPayload()));

        PutItemSpec putItemSpec = new PutItemSpec()
                .withItem(dbItem);

        if (item.getVersion() == 1L) {
            putItemSpec.withExpected(new Expected("key").notExist());
        } else {
            putItemSpec.withExpected(new Expected("version").eq(String.valueOf(item.getVersion() - 1)));
        }

        return putItemSpec;
    }

//    private static QuerySpec createQuerySpec(String type) {
//        return new QuerySpec()
//                .withKeyConditionExpression("type = :type")
//                .withValueMap(new ValueMap()
//                        .withString(":type", type)
//                );
//    }

    private static TransactWriteItemsRequest createTxWriteItemRequest(String tableName, Collection<TxItem> txItems) {
        List<TransactWriteItem> requestItems = new LinkedList<>();

        txItems.forEach(i -> {
            Map<String, AttributeValue> dbItem = new HashMap<>();
            dbItem.put("key", new AttributeValue(i.getKey()));
            dbItem.put("type", new AttributeValue(i.getPayload().getClass().getName()));
            dbItem.put("version", new AttributeValue(String.valueOf(i.getVersion())));
            dbItem.put("data", new AttributeValue(GSONCodec.encode(i.getPayload())));


            Put put = new Put()
                    .withItem(dbItem)
                    .withTableName(tableName);

            if (i.getVersion() == 1L) {
                put
                        .withConditionExpression("attribute_not_exists(#field)")
                        .withExpressionAttributeNames(Collections.singletonMap("#field", "key"));
            } else {
                put
                        .withConditionExpression(String.format("%s = :value", "version"))
                        .withExpressionAttributeValues(Collections.singletonMap(":value", new AttributeValue(String.valueOf(i.getVersion() - 1))));
            }

            requestItems.add(new TransactWriteItem().withPut(put));
        });

        return new TransactWriteItemsRequest()
                .withTransactItems(requestItems)
                .withReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);
    }
}
