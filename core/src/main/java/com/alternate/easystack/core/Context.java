package com.alternate.easystack.core;

import com.alternate.easystack.common.utils.GSONCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Context {
    protected final DbService dbService;
    protected final Map<String, TxItem> cache = new HashMap<>();
    protected final Transaction transaction = new Transaction();

    public Context(DbService dbService) {
        this.dbService = dbService;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        TxItem txItem = transaction.getTxItem(key);

        if (txItem == null) {
            txItem = cache.get(key);
        }

        if (txItem == null) {
            txItem = dbService.get(key);

            if (txItem != null) {
                System.out.println("Add to context cache: " + txItem);
                cache.put(key, txItem);
            }
        }

        T t = (txItem != null) ? (T) txItem.getPayload() : null;
        return Optional.ofNullable(GSONCodec.clone(t));
    }

    public void save(String key, Object item) {
        TxItem txItem = new TxItem(key, GSONCodec.clone(item), getVersion(key) + 1);

        System.out.println("Add to context transaction: " + txItem);
        transaction.addToTx(txItem);
    }

    private long getVersion(String key) {
        TxItem txItem = cache.get(key);

        if (txItem == null) {
            txItem = dbService.get(key);
        }

        return txItem != null ? txItem.getVersion() : 0L;
    }
}
