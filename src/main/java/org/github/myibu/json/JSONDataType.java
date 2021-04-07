package org.github.myibu.json;

import java.util.function.Function;

import static org.github.myibu.json.JSONSeparator.QUOTATION;

/**
 * @author myibu
 * @since 1.0
 */
public enum JSONDataType {
    /* string type */
    STRING(input -> {
        if (input.length() < 2) {
            return false;
        }
        char[] chars = input.toCharArray();
        if (chars[0] != QUOTATION || chars[chars.length-1] != QUOTATION) {
            return false;
        }
        input = input.substring(1, input.length()-1);
        int pos = 0, foundPos = -1;
        while ((foundPos = input.indexOf("\"", pos)) != -1) {
            if (foundPos == 0) {
                return false;
            }
            int backslashIndex = foundPos-1, backslashNum = 0;
            while (backslashIndex >= 0) {
                if (chars[backslashIndex] != '\\') {
                    break;
                }
                backslashNum++;
                backslashIndex--;
            }
            if ((backslashNum & 1) == 0) {
                return false;
            }
        }
        return true;
    }),

    /* number type */
    NUMBER(input -> {
        int pointNum = 0;
        for (char ch : input.toCharArray()) {
            if ('.' == ch) {
                pointNum++;
            } else if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return pointNum < 2;
    }),

    /* true type */
    BOOL_TRUE("true"::equals),

    /* false type */
    BOOL_FALSE("false"::equals),

    /* null type */
    NULL("null"::equals),

    /* empty type */
    EMPTY(""::equals),

    /* illegal type */
    ILLEGAL(input -> false);

    Function<String, Boolean> func;

    public boolean matches(String input) {
        if (null == input) {
            return false;
        }
        input = input.strip();
        return func.apply(input);
    }

    JSONDataType(Function<String, Boolean> func) {
        this.func = func;
    }
}
