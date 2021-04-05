package org.github.myibu.json;

import org.github.myibu.json.decoder.JSONDecoder;
import org.github.myibu.json.util.TypeValue;

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
}
