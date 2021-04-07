package org.github.myibu.json;

import org.github.myibu.json.decoder.JSONDecoder;
import org.github.myibu.json.util.TypeValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author myibu
 * @since 1.0
 */
public class JSONArray extends JSON implements List<Object> {
    private final List<Object> list;

    static final int DEFAULT_CAPACITY = 10;

    public JSONArray() {
        this.list = new ArrayList<>(DEFAULT_CAPACITY);
    }

    public JSONArray(List<Object> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        return list.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<?> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Object get(int index) {
        return list.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        list.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public static JSONArray toJSONArray(String text) {
        JSONDecoder decoder = new JSONDecoder(text);
        TypeValue<Class<? extends JSON>> typeValue = decoder.decode();
        if (typeValue.type() != JSONArray.class) {
            throw new JSONException(String.format("can not constructor a json object, the text you input may be %s type",
                    typeValue.type() == JSONObject.class ? "object" : "primary"));
        }
        return (JSONArray) typeValue.value();
    }

    @Override
    public String toString() {
        return toJSONString(this);
    }

    public String getString(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public Byte getByte(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).byteValue();
        }
        throw new JSONException(String.format("can not cast %s to byte", value));
    }

    public Short getShort(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).shortValue();
        }
        throw new JSONException(String.format("can not cast %s to short", value));
    }

    public Integer getInteger(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).intValue();
        }
        throw new JSONException(String.format("can not cast %s to integer", value));
    }

    public Long getLong(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return ((BigInteger)value).longValue();
        }
        throw new JSONException(String.format("can not cast %s to long", value));
    }

    public Float getFloat(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).floatValue();
        }
        throw new JSONException(String.format("can not cast %s to float", value));
    }

    public Double getDouble(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).doubleValue();
        }
        throw new JSONException(String.format("can not cast %s to double", value));
    }

    public BigDecimal getBigDecimal(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        }
        throw new JSONException(String.format("can not cast %s to BigDecimal", value));
    }

    public BigInteger getBigInteger(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return (BigInteger)value;
        }
        throw new JSONException(String.format("can not cast %s to BigInteger", value));
    }

    public JSONObject getJSONObject(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof JSONObject) {
            return (JSONObject)value;
        }
        throw new JSONException(String.format("can not cast %s to JSONObject", value));
    }

    public JSONArray getJSONArray(int index) {
        Object value = get(index);

        if (value == null) {
            return null;
        }

        if (value instanceof JSONArray) {
            return (JSONArray)value;
        }
        throw new JSONException(String.format("can not cast %s to JSONArray", value));
    }
}
