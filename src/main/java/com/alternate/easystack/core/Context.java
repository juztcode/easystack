package com.alternate.easystack.core;

import com.alternate.easystack.services.DbService;
import com.alternate.easystack.services.Transaction;
import com.alternate.easystack.services.TxItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Context {
    private final DbService dbService;

    private final Map<String, Object> cache = new HashMap<>();
    private final Map<String, Long> dbVersion = new HashMap<>();

    private Transaction transaction = null;

    public Context(DbService dbService) {
        this.dbService = dbService;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        T t = null;

        if (transaction != null) {
            TxItem txItem = transaction.getTxItem(key);

            if (txItem != null) {
                t = (T) txItem.getPayload();
            }
        }

        if (t == null) {
            t = (T) cache.get(key);
        }

        if (t == null) {
            TxItem txItem = dbService.get(key);

            if (txItem != null) {
                t = (T) txItem.getPayload();
                cache.put(key, t);
                dbVersion.put(key, txItem.getVersion());
            }
        }

        return Optional.ofNullable(t);
    }

    public void beginTx() {
        if (transaction != null) {
            throw new RuntimeException("Has pending tx");
        }

        transaction = new Transaction();
    }

    public void save(String key, Object item) {
        TxItem txItem = new TxItem(key, item, getDbVersion(key) + 1);

        if (transaction != null) {
            transaction.addToTx(txItem);
        } else {
            dbService.save(txItem);

            System.out.println("TxItem committed: " + txItem);
            cache.put(key, txItem.getPayload());
            dbVersion.put(key, txItem.getVersion());
        }
    }

    public void commitTx() {
        dbService.save(transaction);
        transaction.getTxItems().forEach(i -> {

            System.out.println("TxItem committed: " + i);
            cache.put(i.getKey(), i.getPayload());
            dbVersion.put(i.getKey(), i.getVersion());
        });

        transaction = null;
    }

    public void rollbackTx() {
        transaction = null;
    }

    private long getDbVersion(String key) {
        long version = dbVersion.getOrDefault(key, 0L);

        if (version == 0L) {
            TxItem txItem = dbService.get(key);

            if (txItem != null) {
                version = txItem.getVersion();
            }
        }

        return version;
    }
}
