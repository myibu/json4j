import org.github.myibu.json.JSON;
import org.github.myibu.json.JSONArray;
import org.github.myibu.json.JSONObject;

import java.util.HashMap;

public class TestJson {
    public static void main(String[] args) {
        JSONObject jsonObject = JSONObject.toJSONObject("{\"a\":\"a\",\"b\":[1,2]}");
        JSONArray jsonArray = JSONArray.toJSONArray("[1,2]");
        System.out.println(JSON.toJSONString(jsonObject));
        System.out.println(JSON.toJSONString(new Person()));
    }
}
