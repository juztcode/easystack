package com.alternate.easystack.core;

public class ContextEx extends Context {
    public ContextEx(DbService dbService) {
        super(dbService);
    }

    public void commitTx() {
        dbService.save(transaction);
    }
}
