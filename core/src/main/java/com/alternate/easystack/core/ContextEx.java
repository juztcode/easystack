package com.alternate.easystack.core;

public class ContextEx extends Context {
    public ContextEx(DbService dbService) {
        super(dbService);
    }

    public void commitTx() {
        if (!transaction.values().isEmpty()) {
            System.out.println("Commit context transaction: " + transaction);
            dbService.save(transaction.values());
        } else {
            System.out.println("Transaction is empty");
        }
    }
}
