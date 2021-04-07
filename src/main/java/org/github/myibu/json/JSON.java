package org.github.myibu.json;


import org.github.myibu.json.decoder.JSONDecoder;
import org.github.myibu.json.encoder.JSONEncoder;
import org.github.myibu.json.util.TypeValue;

import java.util.Arrays;
import java.util.List;

/**
 * @author myibu
 * @since 1.0
 */
public class JSON {
    public static final List<Character> EMPTY_CHARS = Arrays.asList(' ', '\t', '\r', '\n');

    public static Object parse(String text) {
        JSONDecoder decoder = new JSONDecoder(text);
        TypeValue<Class<? extends JSON>> typeValue = decoder.decode();
        return typeValue.value();
    }

    public static String toJSONString(Object bean) {
        JSONEncoder encoder = new JSONEncoder(bean);
        return encoder.encode();
    }

    public static String format(Object bean) {
        return format(bean, 0);
    }
    public static String format(Object bean, int level) {
        JSONEncoder encoder = new JSONEncoder(bean);
        return encoder.formatEncode(level);
    }
}
