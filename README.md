
# json4j

The package is for [JSON](https://www.json.org/json-en.html) conversion in Java enviroment.
<table>
<thead>
<tr><th>JSON Type</th><th>JSON Type Example</th><th>Java Type</th></tr>
</thead></tr>
</thead>
<tbody>
<tr><td>object</td><td>{}</td><td>JSONObject</td></tr>
<tr><td>array</td><td>[]</td><td>JSONArray</td></tr>
<tr><td>string</td><td>&quot;example&quot;</td><td>String</td></tr>
<tr><td>number</td><td>1/1.0</td><td>BigInteger/BigDecimal</td></tr>
<tr><td>"true"</td><td>true</td><td>boolean</td></tr>
<tr><td>"false"</td><td>false</td><td>boolean</td></tr>
<tr><td>"null"</td><td>null</td><td>null</td></tr>
</tbody>
</table>

## Implements

> Convert a JSON string into a JSON object

`JSON.parse(value)` is based on stack structure.
There are three stacks in the program, `stack` stack for characters,` class_stack` stack for list and dict, `keyvalue_stack` stack for name/value pair.
Scan each character in the string, do the following:
1.  If is `[`, put type/value into `class_stack` stack
2.  If is `{`, put type/value into the `class_stack` stack, and put an empty key/value pair (named` border`) into the `keyvalue_stack` stack
3. If is`,`, according to `class_stack` stack top type (list and dict),` list` is to operate on the top of the `class_stack` stack and call its append method, `dict` is pushed into the `keyvalue_stack` stack
4. If is `]`, popping all the elements on the top of the `stack` stack until it encounters `[`
5. If is `}`, pop the `keyvalue_stack` stack and pop all the elements on the top of the stack until it encounters  `border` 
6. If is `:`, operate on the top of the `keyvalue_stack` stack and modify the value in the key/value pair
7. Other, put characters into the `stack` stack
> Convert JSON values to JSON strings

`JSON.stringify(value)` is using recursive method.
1. If it is a basic type in Python (number, string, bool, None), return the corresponding string (1/1.0, "example", true/false, null)
2. If it is list and dict, repeat step 1 for each element in the list and dict
3. Others, It is a class, consider as a dict type, terating over member variables of a class, do the same action as step 1

## Installation
```bash
<dependency>
  <groupId>com.github.myibu</groupId>
  <artifactId>json4j</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Examples

 - Convert a JSON string into a JSON object.

```java
JSONObject jsonObject = JSONObject.parse("[\"foo\", {\"bar\": [\"baz\", null, 1.0, 2, true]}]");
// or write as following if you donot known the type of th json String
Object jo = JSON.parse("[\"foo\", {\"bar\": [\"baz\", null, 1.0, 2, true]}]");
```

 - Convert JSON values to JSON strings.

```java
Map<String,String> map = new HashMap<>();
map.put("1", "00");
String jsonString = JSON.toJSONString(map);
```

- Format JSON object or JSON strings

```java
Map<String,String> map = new HashMap<>();
map.put("1", "00");
String formatedJsonString = JSON.format(map);
```

