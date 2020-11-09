package io.github.juztcode.easystack.core;

public class TxItem {
    private final String key;
    private final Object payload;
    private final long version;

    public TxItem(String key, Object payload, long version) {
        this.key = key;
        this.payload = payload;
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public Object getPayload() {
        return payload;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "TxItem{" +
                "key='" + key + '\'' +
                ", payload=" + payload +
                ", version=" + version +
                '}';
    }
}
