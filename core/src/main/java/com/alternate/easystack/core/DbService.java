package com.alternate.easystack.core;

public interface DbService {

    TxItem get(String key);

    void save(Transaction transaction);

    void save(TxItem item);
}
