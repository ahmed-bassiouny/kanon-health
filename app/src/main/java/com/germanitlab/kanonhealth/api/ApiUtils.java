package com.germanitlab.kanonhealth.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by milad on 8/8/17.
 */

public class ApiUtils {


    public static HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static ArrayList<Object> toList(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


    public static String removeNulls(String jsonString) {
        String result = jsonString;
        try {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> data = new Gson().fromJson(jsonString, type);

            for (Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                if (entry.getValue() == null) {
                    it.remove();
                } else if (entry.getValue() instanceof ArrayList) {
                    if (((ArrayList<?>) entry.getValue()).isEmpty()) {
                        it.remove();
                    }
                } else if (TextUtils.isEmpty(entry.getValue().toString())) {
                    it.remove();
                }
            }
            result = new GsonBuilder().setPrettyPrinting().create().toJson(data);
        } catch (Exception e) {
            Log.e("milad","parsing error" , e);
        } finally {
            return result;
        }
    }
}
