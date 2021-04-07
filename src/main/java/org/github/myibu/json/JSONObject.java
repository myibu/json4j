package org.github.myibu.json;

import org.github.myibu.json.decoder.JSONDecoder;
import org.github.myibu.json.util.TypeValue;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    @Override
    public String toString() {
        return toJSONString(this);
    }

    public String getString(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public Byte getByte(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).byteValue();
        }
        throw new JSONException(String.format("can not cast %s to byte", value));
    }

    public Short getShort(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).shortValue();
        }
        throw new JSONException(String.format("can not cast %s to short", value));
    }

    public Integer getInteger(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).intValue();
        }
        throw new JSONException(String.format("can not cast %s to integer", value));
    }

    public Long getLong(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).longValue();
        }
        throw new JSONException(String.format("can not cast %s to long", value));
    }

    public Float getFloat(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).floatValue();
        }
        throw new JSONException(String.format("can not cast %s to float", value));
    }

    public Double getDouble(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).doubleValue();
        }
        throw new JSONException(String.format("can not cast %s to double", value));
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        }
        throw new JSONException(String.format("can not cast %s to BigDecimal", value));
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return (BigInteger)value;
        }
        throw new JSONException(String.format("can not cast %s to BigInteger", value));
    }

    public JSONObject getJSONObject(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof JSONObject) {
            return (JSONObject)value;
        }
        throw new JSONException(String.format("can not cast %s to JSONObject", value));
    }

    public JSONArray getJSONArray(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof JSONArray) {
            return (JSONArray)value;
        }
        throw new JSONException(String.format("can not cast %s to JSONArray", value));
    }
}
