package org.github.myibu.json.util;

public class KeyValue {

    public final static KeyValue KEY_VALUE_BORDER = new KeyValue("KEY", "VALUE", true);

    String key;
    Object value;
    boolean isBorder;

    public KeyValue(String key, Object value, boolean isBorder) {
        this.key = key;
        this.value = value;
        this.isBorder = isBorder;
    }

    public KeyValue(String key, Object value) {
        this(key, value, false);
    }

    public String key() {
        return key;
    }

    public Object value() {
        return value;
    }

    public void value(Object value) {
        this.value = value;
    }

    public boolean isBorder() {
        return isBorder;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", isBorder=" + isBorder +
                '}';
    }
}
