package org.github.myibu.json.decoder;

import org.github.myibu.json.*;
import org.github.myibu.json.util.KeyValue;
import org.github.myibu.json.util.Stack;
import org.github.myibu.json.util.TypeValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.github.myibu.json.JSON.EMPTY_CHARS;
import static org.github.myibu.json.JSONSeparator.*;
/**
 * @author myibu
 * @since 1.0
 */
public class JSONDecoder {
    String text;

    public JSONDecoder(String text) {
        if (null == text || "".equals(text.strip())) {
            throw new JSONException("input string can not be empty");
        }
        this.text = text;
    }

    public TypeValue<Class<? extends JSON>> decode() {
        Stack<TypeValue<Class<? extends JSON>>> classStack = new Stack<>();
        Stack<KeyValue> keyValueStack = new Stack<>();
        Stack<Character> stack = new Stack<>();
        char[] chars = text.toCharArray();
        int index = 0;
        while (index <= chars.length - 1) {
            char ch = chars[index];
            if (ch == LEFT_BRACKETS) {
                // remove character in [' ', '\t', '\r', '\n']
                while (!stack.isEmpty() && EMPTY_CHARS.contains(stack.top())) {
                    stack.pop();
                }
                stack.push(ch);
                classStack.push(new TypeValue<>(JSONArray.class, new JSONArray()));
            } else if (ch == LEFT_BRACE) {
                // remove character in [' ', '\t', '\r', '\n']
                while (!stack.isEmpty() && EMPTY_CHARS.contains(stack.top())) {
                    stack.pop();
                }
                stack.push(ch);
                classStack.push(new TypeValue<>(JSONObject.class, new JSONObject()));
                keyValueStack.push(KeyValue.KEY_VALUE_BORDER);
            } else if (ch == COMMA) {
                // remove character in [' ', '\t', '\r', '\n']
                while (!stack.isEmpty() && EMPTY_CHARS.contains(stack.top())) {
                    stack.pop();
                }
                String stackStr;
                if (classStack.top().type() == JSONArray.class) {
                    List<Character> list = stack.popUtilBorder('[');
                    char[] stackChars = new char[list.size()];
                    int stackCharIndex = list.size() - 1;
                    while (stackCharIndex >= 0) {
                        stackChars[stackCharIndex] = list.get(stackCharIndex);
                        stackCharIndex--;
                    }
                    stackStr = new String(stackChars);
                    TypeValue<JSONDataType> stackObj = parse(stackStr);
                    if (stackObj.type() == JSONDataType.EMPTY) {
                        if (((JSONArray) classStack.top().value()).size() == 0) {
                            throw new JSONException(String.format("Missing element after \",\" delimiter: column %d", index));
                        } else {
                            index++; //index++; continue;
                        }
                    } else if (stackObj.type() != JSONDataType.ILLEGAL) {
                        ((JSONArray) classStack.top().value()).add(stackObj.value());
                    } else {
                        throw new JSONException(String.format("Illegal json character sequence \"%s\": column %d", stackObj.value(), index));
                    }
                } else if (classStack.top().type() == JSONObject.class) {
                    List<Character> list = stack.popUtilBorder('{');
                    char[] stackChars = new char[list.size()];
                    int stackCharIndex = list.size() - 1;
                    while (stackCharIndex >= 0) {
                        stackChars[stackCharIndex] = list.get(stackCharIndex);
                        stackCharIndex--;
                    }
                    stackStr = new String(stackChars);
                    TypeValue<JSONDataType> stackObj = parse(stackStr);
                    if (stackObj.type() == JSONDataType.EMPTY) {
                        if (keyValueStack.size() <= 1) {
                            throw new JSONException(String.format("Missing element after \",\" delimiter: column %d", index));
                        } else {
                            //index++; continue;
                        }
                    } else if (stackObj.type() != JSONDataType.ILLEGAL) {
                        keyValueStack.top().value(stackObj.value());
                    } else {
                        throw new JSONException(String.format("Illegal json character sequence \"%s\": column %d", stackObj.value(), index));
                    }
                } else {
                    throw new JSONException(String.format("Missing \"[\" or \"{\" before \",\" delimiter: column %d", index));
                }
            } else if (ch == RIGHT_BRACE) {
                if (classStack.isEmpty()) {
                    throw new JSONException(String.format("Missing \"[\" or \"{\" before \",\" delimiter: column %d", index));
                }
                String stackStr;
                if (classStack.top().type() == JSONObject.class) {
                    List<Character> list = stack.popUtilBorder('{');
                    char[] stackChars = new char[list.size()];
                    int stackCharIndex = list.size() - 1;
                    while (stackCharIndex >= 0) {
                        stackChars[stackCharIndex] = list.get(stackCharIndex);
                        stackCharIndex--;
                    }
                    stackStr = new String(stackChars);
                    TypeValue<JSONDataType> stackObj = parse(stackStr);
                    if (stackObj.type() == JSONDataType.EMPTY) {
                        //index++; continue;
                    } else if (stackObj.type() != JSONDataType.ILLEGAL) {
                        keyValueStack.top().value(stackObj.value());
                    } else {
                        throw new JSONException(String.format("Illegal json character sequence \"%s\": column %d", stackObj.value(), index));
                    }

                    while (!keyValueStack.isEmpty() && !keyValueStack.top().isBorder()) {
                        KeyValue kv = keyValueStack.pop();
                        ((JSONObject) classStack.top().value()).put(kv.key(), kv.value());
                    }
                    keyValueStack.pop();

                    TypeValue<Class<? extends JSON>> topValue = classStack.pop();
                    if (classStack.isEmpty()) {
                        classStack.push(topValue);
                    } else if (!classStack.isEmpty() && classStack.top().type() == JSONArray.class) {
                        ((JSONArray) classStack.top().value()).add(topValue.value());
                    } else if (!classStack.isEmpty() && classStack.top().type() == JSONObject.class) {
                        keyValueStack.top().value(topValue.value());
                    }
                    stack.pop();
                } else {
                    throw new JSONException(String.format("Missing \"[\" delimiter: column %d", index));
                }
            } else if (ch == RIGHT_BRACKETS) {
                if (classStack.isEmpty()) {
                    throw new JSONException(String.format("Missing \"[\" or \"{\" before \",\" delimiter: column %d", index));
                }
                String stackStr;
                if (classStack.top().type() == JSONArray.class) {
                    List<Character> list = stack.popUtilBorder('[');
                    char[] stackChars = new char[list.size()];
                    int stackCharIndex = list.size() - 1;
                    while (stackCharIndex >= 0) {
                        stackChars[stackCharIndex] = list.get(stackCharIndex);
                        stackCharIndex--;
                    }
                    stackStr = new String(stackChars);
                    TypeValue<JSONDataType> stackObj = parse(stackStr);
                    if (stackObj.type() == JSONDataType.EMPTY) {
                        //index++; continue;
                    } else if (stackObj.type() != JSONDataType.ILLEGAL) {
                        ((JSONArray) classStack.top().value()).add(stackObj.value());
                    } else {
                        throw new JSONException(String.format("Illegal json character sequence \"%s\": column %d", stackObj.value(), index));
                    }

                    TypeValue<Class<? extends JSON>> topValue = classStack.pop();
                    if (classStack.isEmpty()) {
                        classStack.push(topValue);
                    } else if (!classStack.isEmpty() && classStack.top().type() == JSONArray.class) {
                        ((JSONArray) classStack.top().value()).add(topValue.value());
                    } else if (!classStack.isEmpty() && classStack.top().type() == JSONObject.class) {
                        keyValueStack.top().value(topValue.value());
                    }
                    stack.pop();
                } else {
                    throw new JSONException(String.format("Missing \"{\" delimiter: column %d", index));
                }
            } else if (ch == COLON) {
                String stackStr;
                List<Character> list = stack.popUtilBorder('{');
                char[] stackChars = new char[list.size()];
                int stackCharIndex = list.size() - 1;
                while (stackCharIndex >= 0) {
                    stackChars[stackCharIndex] = list.get(stackCharIndex);
                    stackCharIndex--;
                }
                stackStr = new String(stackChars);
                TypeValue<JSONDataType> stackObj = parse(stackStr);
                if (stackObj.type() == JSONDataType.STRING) {
                    keyValueStack.push(new KeyValue((String)stackObj.value(), null));
                } else {
                    throw new JSONException(String.format("Illegal json character sequence \"%s\": column %d", stackObj.value(), index));
                }
            } else {
                stack.push(ch);
            }
            index++;
        }
        if (!classStack.isEmpty()) {
            return new TypeValue<>(classStack.top().type(), classStack.top().value());
        } else {
            List<Character> list = stack.popUtilBorder(' ');
            char[] stackChars = new char[list.size()];
            int stackCharIndex = list.size() - 1;
            while (stackCharIndex >= 0) {
                stackChars[stackCharIndex] = list.get(stackCharIndex);
                stackCharIndex--;
            }
            String stackStr = new String(stackChars);
            TypeValue<JSONDataType> stackObj = parse(stackStr);
            if (stackObj.type() != JSONDataType.ILLEGAL) {
                return new TypeValue<>(JSONPrimery.class, stackObj.value());
            } else {
                throw new JSONException(String.format("Illegal json character sequence \"%s\": column %d", stackObj.value(), index));
            }
        }
    }

    public TypeValue<JSONDataType> parse(String input) {
        if (JSONDataType.EMPTY.matches(input)) {
            return new TypeValue<>(JSONDataType.EMPTY, "");
        } else if (JSONDataType.NULL.matches(input)) {
            return new TypeValue<>(JSONDataType.NULL, "null");
        } else if (JSONDataType.BOOL_TRUE.matches(input)) {
            return new TypeValue<>(JSONDataType.BOOL_TRUE, "true");
        } else if (JSONDataType.BOOL_FALSE.matches(input)) {
            return new TypeValue<>(JSONDataType.BOOL_FALSE, "false");
        } else if (JSONDataType.NUMBER.matches(input)) {
            if (input.matches("[0-9]+")) {
                BigInteger bigInteger = new BigInteger(input);
                return new TypeValue<>(JSONDataType.NUMBER, bigInteger);
            } else {
                BigDecimal bigDecimal = new BigDecimal(input);
                return new TypeValue<>(JSONDataType.NUMBER, bigDecimal);
            }
        } else if (JSONDataType.STRING.matches(input)) {
            return new TypeValue<>(JSONDataType.STRING, input.substring(1, input.length()-1));
        }
        return new TypeValue<>(JSONDataType.ILLEGAL, input);
    }

}
