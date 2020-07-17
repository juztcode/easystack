package com.alternate.easystack.core;

import com.alternate.easystack.common.utils.GSONCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Context {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    protected final DbService dbService;
    protected final Logger contextLogger;

    protected final Map<String, TxItem> cache = new HashMap<>();
    protected final Map<String, TxItem> transaction = new HashMap<>();

    public Context(DbService dbService, Logger contextLogger) {
        this.dbService = dbService;
        this.contextLogger = contextLogger;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Class<?> type, String id) {
        String key = String.join(":", type.getName(), id);
        TxItem txItem = transaction.get(key);

        if (txItem == null) {
            txItem = cache.get(key);
        }

        if (txItem == null) {
            txItem = dbService.get(key);

            if (txItem != null) {
                logger.debug("Add to context cache: " + txItem);
                cache.put(key, txItem);
            }
        }

        T t = (txItem != null) ? (T) txItem.getPayload() : null;
        return Optional.ofNullable(GSONCodec.clone(t));
    }

    public void save(String id, Object item) {
        String key = String.join(":", item.getClass().getName(), id);
        TxItem txItem = new TxItem(key, GSONCodec.clone(item), getVersion(key) + 1);

        logger.debug("Add to context transaction: " + txItem);
        transaction.put(key, txItem);
    }

    public Logger logger() {
        return contextLogger;
    }

    private long getVersion(String key) {
        TxItem txItem = cache.get(key);

        if (txItem == null) {
            txItem = dbService.get(key);
        }

        return txItem != null ? txItem.getVersion() : 0L;
    }
}
