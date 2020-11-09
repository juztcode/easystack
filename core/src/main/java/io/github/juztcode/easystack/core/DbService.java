package io.github.juztcode.easystack.core;

import java.util.Collection;

public interface DbService {

    TxItem get(String key);

//    Stream<TxItem> getAll(String type);

    void save(Collection<TxItem> txItems);

    void save(TxItem txItem);
}
