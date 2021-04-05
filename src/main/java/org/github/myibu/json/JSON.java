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

    public static <T> T parse(String text, Class<T> clazz) {
        //todo
        return null;
    }

    public static String toJSONString(Object bean) {
        JSONEncoder encoder = new JSONEncoder(bean);
        return encoder.encode();
    }

}
