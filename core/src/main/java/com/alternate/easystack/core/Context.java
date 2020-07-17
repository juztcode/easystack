package com.alternate.easystack.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Context {
    private final DbService dbService;
    private final Map<String, TxItem> cache = new HashMap<>();
    private Transaction transaction = null;

    public Context(DbService dbService) {
        this.dbService = dbService;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        TxItem txItem = null;

        if (transaction != null) {
            txItem = transaction.getTxItem(key);
        }

        if (txItem == null) {
            txItem = cache.get(key);
        }

        if (txItem == null) {
            txItem = dbService.get(key);

            if (txItem != null) {
                cache.put(key, txItem);
            }
        }

        T t = (txItem != null) ? (T) txItem.getPayload() : null;
        return Optional.ofNullable(t);
    }

    public void beginTx() {
        if (transaction != null) {
            throw new RuntimeException("Has pending tx");
        }

        transaction = new Transaction();
    }

    public void save(String key, Object item) {
        TxItem txItem = new TxItem(key, item, getVersion(key) + 1);

        if (transaction != null) {
            transaction.addToTx(txItem);
        } else {
            dbService.save(txItem);

            System.out.println("TxItem committed: " + txItem);
            cache.put(key, txItem);
        }
    }

    public void commitTx() {
        dbService.save(transaction);
        transaction.getTxItems().forEach(i -> {

            System.out.println("TxItem committed: " + i);
            cache.put(i.getKey(), i);
        });

        transaction = null;
    }

    public void rollbackTx() {
        transaction = null;
    }

    private long getVersion(String key) {
        TxItem txItem = cache.get(key);

        if (txItem == null) {
            txItem = dbService.get(key);
        }

        return txItem != null ? txItem.getVersion() : 0L;
    }
}
