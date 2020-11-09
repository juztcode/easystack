package io.github.juztcode.easystack.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextEx extends Context {
    private static final Logger logger = LoggerFactory.getLogger(ContextEx.class);

    public ContextEx(DbService dbService, Logger contextLogger) {
        super(dbService, contextLogger);
    }

    public void commitTx() {
        if (!transaction.values().isEmpty()) {
            logger.info("Commit context transaction: " + transaction);
            dbService.save(transaction.values());
        } else {
            logger.info("Transaction is empty");
        }
    }
}
