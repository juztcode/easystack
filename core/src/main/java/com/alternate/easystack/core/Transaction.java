package com.alternate.easystack.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Transaction {

    private final Map<String, TxItem> txItems = new HashMap<>();

    public void addToTx(TxItem txItem) {
        txItems.put(txItem.getKey(), txItem);
    }

    public TxItem getTxItem(String key) {
        return txItems.get(key);
    }

    public Collection<TxItem> getTxItems() {
        return txItems.values();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "txItems=" + txItems +
                '}';
    }
}
