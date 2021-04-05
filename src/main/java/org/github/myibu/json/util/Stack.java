package org.github.myibu.json.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Stack<T> {
    List<T> elements;
    T top;

    public Stack() {
        this.elements = new ArrayList<>();
        this.top = null;
    }
    public void push(T element) {
        this.elements.add(element);
        this.top = element;
    }

    public T pop() {
        if (elements.size() == 0) {
            throw new StackEmptyException();
        }
        T value = elements.remove(elements.size() -1);
        top = elements.size() > 0? elements.get(elements.size()-1): null;
        return value;
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public List<T> popUtilBorder(T border){
        List<T> list = new ArrayList<>();
        T value;
        while (!elements.isEmpty() && !top.equals(border)) {
            list.add(0, pop());
        }
        return list;
    }

    public T top() {
        return top;
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        Iterator<T> it = elements.iterator();
        if (! it.hasNext())
            return "<>";

        StringBuilder sb = new StringBuilder();
        sb.append('<');
        for (;;) {
            T e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append('>').toString();
            sb.append(',').append(' ');
        }
    }
}
