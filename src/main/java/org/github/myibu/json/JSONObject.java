package org.github.myibu.json;

import org.github.myibu.json.decoder.JSONDecoder;
import org.github.myibu.json.util.TypeValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author myibu
 * @since 1.0
 */
public class JSONObject extends JSON implements Map<String, Object> {
    private final Map<String, Object> map;

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    public JSONObject() {
        this.map = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
    }

    public JSONObject(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsValue(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    public static JSONObject toJSONObject(String text) {
        JSONDecoder decoder = new JSONDecoder(text);
        TypeValue<Class<? extends JSON>> typeValue = decoder.decode();
        if (typeValue.type() != JSONObject.class) {
            throw new JSONException(String.format("can not constructor a json object, the text you input may be %s type",
                    typeValue.type() == JSONArray.class ? "array" : "primary"));
        }
        return (JSONObject) typeValue.value();
    }

}
