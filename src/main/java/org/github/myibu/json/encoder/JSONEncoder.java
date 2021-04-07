package org.github.myibu.json.encoder;

import org.github.myibu.json.*;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystem;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author myibu
 * @since 1.0
 */
public class JSONEncoder {
    Object target;

    public JSONEncoder(Object target) {
        if (null == target) {
            throw new JSONException("input object can not be empty");
        }
        this.target = target;
    }

    private boolean isJSONPrimary(Object target) {
        if (target == null) {
            return true;
        }
        return target.getClass() == Short.class
                || target.getClass() == Integer.class
                || target.getClass() == Long.class
                || target.getClass() == Float.class
                || target.getClass() == Double.class
                || target.getClass() == Boolean.class
                || target.getClass() == BigInteger.class
                || target.getClass() == BigDecimal.class
                || target.getClass() == String.class;
    }

    public String encode() {
        return doEncode(target);
    }

    public String formatEncode(int level) {
        if (level < 0) {
            level = 0;
        }
        return doFormatEncode(target, level);
    }

    private String doEncode(Object target) {
        if (isJSONPrimary(target)) {
            if (null == target) {
                return "null";
            } else if (target.getClass() == Boolean.class) {
                return (boolean)target ? "true" : "false";
            } else if (target.getClass() == String.class) {
                return String.format("\"%s\"", target);
            } else {
                return  target + "";
            }
        } else if (target instanceof List) {
            StringBuilder res = new StringBuilder("[");
            Iterator<?> iterator = ((List<?>)target).iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                res.append(doEncode(item));

                if (iterator.hasNext()) {
                    res.append(",");
                }
            }
            return res.append("]").toString();
        } else if (target instanceof Map) {
            StringBuilder res = new StringBuilder("{");
            Iterator<? extends Map.Entry<?, ?>> iterator = ((Map<?, ?>)target).entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<?, ?> item = iterator.next();
                res.append(String.format("\"%s\":%s", item.getKey().toString(), doEncode(item.getValue())));

                if (iterator.hasNext()) {
                    res.append(",");
                }
            }
            return res.append("}").toString();
        } else {
            StringBuilder res = new StringBuilder("{");
            Field[] fields = target.getClass().getDeclaredFields();
            Iterator<Field> iterator = Arrays.asList(fields).iterator();
            while (iterator.hasNext()) {
                Field item = iterator.next();
                item.setAccessible(true);
                try {
                    res.append(String.format("\"%s\":%s", item.getName(), doEncode(item.get(target))));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (iterator.hasNext()) {
                    res.append(",");
                }
            }
            return res.append("}").toString();

        }
    }

    private String doFormatEncode(Object target, int level) {
        if (isJSONPrimary(target)) {
            if (null == target) {
                return "null";
            } else if (target.getClass() == Boolean.class) {
                return (boolean)target ? "true" : "false";
            } else if (target.getClass() == String.class) {
                return String.format("\"%s\"", target);
            } else {
                return  target + "";
            }
        } else if (target instanceof List) {
            StringBuilder res = new StringBuilder("[").append(System.lineSeparator());
            Iterator<?> iterator = ((List<?>)target).iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();

                for (int i = 0; i < level+1; i++) {
                    res.append("    ");
                }
                res.append(doFormatEncode(item, level+1));

                if (iterator.hasNext()) {
                    res.append(",").append(System.lineSeparator());
                }
            }
            res.append(System.lineSeparator());
            for (int i = 0; i < level; i++) {
                res.append("    ");
            }
            return res.append("]").toString();
        } else if (target instanceof Map) {
            StringBuilder res = new StringBuilder("{").append(System.lineSeparator());
            Iterator<? extends Map.Entry<?, ?>> iterator = ((Map<?, ?>)target).entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<?, ?> item = iterator.next();

                for (int i = 0; i < level+1; i++) {
                    res.append("    ");
                }
                res.append(String.format("\"%s\": %s", item.getKey().toString(), doFormatEncode(item.getValue(), level+1)));

                if (iterator.hasNext()) {
                    res.append(",").append(System.lineSeparator());
                }
            }
            res.append(System.lineSeparator());
            for (int i = 0; i < level; i++) {
                res.append("    ");
            }
            return res.append("}").toString();
        } else {
            StringBuilder res = new StringBuilder("{").append(System.lineSeparator());
            Field[] fields = target.getClass().getDeclaredFields();
            Iterator<Field> iterator = Arrays.asList(fields).iterator();
            while (iterator.hasNext()) {
                Field item = iterator.next();

                for (int i = 0; i < level+1; i++) {
                    res.append("    ");
                }
                item.setAccessible(true);
                try {
                    res.append(String.format("\"%s\": %s", item.getName(), doFormatEncode(item.get(target), level+1)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (iterator.hasNext()) {
                    res.append(",").append(System.lineSeparator());
                }
            }
            res.append(System.lineSeparator());
            for (int i = 0; i < level; i++) {
                res.append("    ");
            }
            return res.append("}").toString();

        }
    }
}
