package org.github.myibu.json.decoder;

import org.github.myibu.json.*;
import org.github.myibu.json.util.KeyValue;
import org.github.myibu.json.util.Stack;
import org.github.myibu.json.util.TypeValue;

import java.util.List;

import static org.github.myibu.json.JSON.EMPTY_CHARS;

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
            if (ch == '[') {
                // remove character in [' ', '\t', '\r', '\n']
                while (!stack.isEmpty() && EMPTY_CHARS.contains(stack.top())) {
                    stack.pop();
                }
                stack.push(ch);
                classStack.push(new TypeValue<>(JSONArray.class, new JSONArray()));
            } else if (ch == '{') {
                // remove character in [' ', '\t', '\r', '\n']
                while (!stack.isEmpty() && EMPTY_CHARS.contains(stack.top())) {
                    stack.pop();
                }
                stack.push(ch);
                classStack.push(new TypeValue<>(JSONObject.class, new JSONObject()));
                keyValueStack.push(KeyValue.KEY_VALUE_BORDER);
            } else if (ch == ',') {
                // remove character in [' ', '\t', '\r', '\n']
                while (!stack.isEmpty() && EMPTY_CHARS.contains(stack.top())) {
                    stack.pop();
                }
                String stackStr = "";
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
                            throw new JSONException();
                        } else {
                            index++; //index++; continue;
                        }
                    } else if (stackObj.type() != JSONDataType.ILLEGAL) {
                        ((JSONArray) classStack.top().value()).add(stackObj.value());
                    } else {
                        throw new JSONException();
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
                            throw new JSONException();
                        } else {
                            //index++; continue;
                        }
                    } else if (stackObj.type() != JSONDataType.ILLEGAL) {
                        keyValueStack.top().value(stackObj.value());
                    } else {
                        throw new JSONException();
                    }
                } else {
                    throw new JSONException();
                }
//                System.out.println("char:[" + ch + "],classStack=" + classStack);
//                System.out.println("char:[" + ch + "],keyValueStack=" + keyValueStack);
//                System.out.println("char:[" + ch + "],stack=" + stack);
            } else if (ch == '}') {
                if (classStack.isEmpty()) {
                    throw new JSONException();
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
                        throw new JSONException();
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
                    throw new JSONException();
                }
//                System.out.println("char:[" + ch + "],classStack=" + classStack);
//                System.out.println("char:[" + ch + "],keyValueStack=" + keyValueStack);
//                System.out.println("char:[" + ch + "],stack=" + stack);
            } else if (ch == ']') {
                if (classStack.isEmpty()) {
                    throw new JSONException();
                }
                String stackStr = "";
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
                        throw new JSONException();
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
                    throw new JSONException();
                }
//                System.out.println("char:[" + ch + "],classStack=" + classStack);
//                System.out.println("char:[" + ch + "],keyValueStack=" + keyValueStack);
//                System.out.println("char:[" + ch + "],stack=" + stack);
            } else if (ch == ':') {
                String stackStr = "";
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
                    throw new JSONException();
                }

//                System.out.println("char:[" + ch + "],classStack=" + classStack);
//                System.out.println("char:[" + ch + "],keyValueStack=" + keyValueStack);
//                System.out.println("char:[" + ch + "],stack=" + stack);
            } else {
                stack.push(ch);
            }
            index++;
        }
        if (!classStack.isEmpty()) {
            return new TypeValue(classStack.top().type(), classStack.top().value());
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
                return new TypeValue<>(JSONPrimary.class, stackObj.value());
            } else {
                throw new JSONException();
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
        } else if (JSONDataType.NUMBER_INT.matches(input)) {
            return new TypeValue<>(JSONDataType.NUMBER_INT, Integer.parseInt(input.strip()));
        } else if (JSONDataType.NUMBER_FLOAT.matches(input)) {
            return new TypeValue<>(JSONDataType.NUMBER_FLOAT, Float.parseFloat(input.strip()));
        } else if (JSONDataType.STRING.matches(input)) {
            return new TypeValue<>(JSONDataType.STRING, input.substring(1, input.length()-1));
        }
        return new TypeValue<>(JSONDataType.ILLEGAL, null);
    }

}
