package com.alternate.easystack.core;

import java.util.Collection;

public interface DbService {

    TxItem get(String key);

    void save(Collection<TxItem> txItems);

    void save(TxItem txItem);
}
