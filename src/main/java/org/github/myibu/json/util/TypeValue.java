package org.github.myibu.json.util;

public class TypeValue <T>{
    T type;
    Object value;

    public TypeValue(T type, Object value) {
        this.type = type;
        this.value = value;
    }
    public T type() {
        return type;
    }
    public Object value() {
        return value;
    }

    @Override
    public String toString() {
        return "TypeValue{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}
