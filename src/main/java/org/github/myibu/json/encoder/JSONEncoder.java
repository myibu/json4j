package org.github.myibu.json.encoder;

import org.github.myibu.json.*;
import org.github.myibu.json.util.TypeValue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.github.myibu.json.JSON.EMPTY_CHARS;

public class JSONEncoder {
    Object target;

    public JSONEncoder(Object target) {
        if (null == target) {
            throw new JSONException("input object can not be empty");
        }
        this.target = target;
    }

    public boolean isJSONPrimary(Object target) {
        if (target == null) {
            return true;
        }
        return target.getClass() == short.class || target.getClass() == Short.class
                || target.getClass() == int.class || target.getClass() == Integer.class
                || target.getClass() == long.class || target.getClass() == Long.class
                || target.getClass() == float.class || target.getClass() == Float.class
                || target.getClass() == double.class || target.getClass() == Double.class
                || target.getClass() == boolean.class || target.getClass() == Boolean.class
                || target.getClass() == String.class;
    }

    public String encode() {
        return doEncode(target);
    }

    private String doEncode(Object target) {
        if (isJSONPrimary(target)) {
            if (null == target) {
                return "null";
            } else if (target.getClass() == boolean.class) {
                return (boolean)target ? "true" : "false";
            } else if (target.getClass() == String.class) {
                return String.format("\"%s\"", target);
            } else {
                return  target + "";
            }
        } else if (target instanceof List) {
            String res = "[";
            if (((List)target).size() < 2) {
                for (Object listItem: (List)target) {
                    res = res + doEncode(listItem);
                }
            } else {
                for (Object listItem: (List)target) {
                    res = res + doEncode(listItem) + ",";
                }
                res = res.substring(0, res.length()-1);
            }
            return res + "]";
        } else if (target instanceof Map) {
            String res = "{";
            if (((Map)target).size() < 2) {
                for(Map.Entry<String, Object> entry: ((Map<String, Object>)target).entrySet()) {
                    res = res + String.format("\"%s\":%s", entry.getKey(), doEncode(entry.getValue()));
                }
            } else {
                for(Map.Entry<String, Object> entry: ((Map<String, Object>)target).entrySet()) {
                    res = res + String.format("\"%s\":%s,", entry.getKey(), doEncode(entry.getValue()));
                }
                res = res.substring(0, res.length()-1);
            }
            return res + "}";
        } else {
            String res = "{";
            Field[] fields = target.getClass().getDeclaredFields();
            if (fields.length < 2) {
                for (Field field: fields) {
                    field.setAccessible(true);
                    try {
                        res = res + String.format("\"%s\":%s", field.getName(), doEncode(field.get(target)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (Field field: fields) {
                    field.setAccessible(true);
                    try {
                        res = res + String.format("\"%s\":%s,", field.getName(), doEncode(field.get(target)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                res = res.substring(0, res.length()-1);
            }
            return res + "}";
        }
    }
}
