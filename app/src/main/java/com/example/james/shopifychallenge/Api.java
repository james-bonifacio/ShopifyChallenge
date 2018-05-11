package com.example.james.shopifychallenge;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Api {

    public HashMap<String, List<String>> getOrdersByProvince(String url) {

        JSONObject res = requestJson(url);

        JSONArray orders = null;

        try {
            orders = res.getJSONArray("orders");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, List<String>> ordersByProvince = new HashMap<String, List<String>>(orders.length());

        for (int i=0; i<orders.length(); i++) {

            String province = null;

            try {
                JSONObject test1 = orders.getJSONObject(i);
                JSONObject test2 = test1.getJSONObject("billing_address");
                province = orders.getJSONObject(i).getJSONObject("billing_address").getString("province");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String name = null;

            try {
                name = orders.getJSONObject(i).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (province != null && name != null) {
                if (!ordersByProvince.containsKey(province)) {

                    List<String> orderNames = new ArrayList<String>();
                    orderNames.add(name);
                    ordersByProvince.put(province, orderNames);

                } else {

                    List<String> orderNames = ordersByProvince.get(province);
                    orderNames.add(name);

                    ordersByProvince.put(province, orderNames);
                }
            }
        }

        return ordersByProvince;
    }

    public HashMap<String, List<String>> getOrdersByYear(String url) {

        JSONObject res = requestJson(url);

        JSONArray orders = null;

        try {
            orders = res.getJSONArray("orders");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, List<String>> ordersByYear = new HashMap<String, List<String>>(orders.length());

        for (int i=0; i<orders.length(); i++) {

            String province = null;

            try {
                province = orders.getJSONObject(i).getString("created_at").substring(0,4);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String name = null;

            try {
                name = orders.getJSONObject(i).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (province != null && name != null) {
                if (!ordersByYear.containsKey(province)) {

                    List<String> orderNames = new ArrayList<String>();
                    orderNames.add(name);
                    ordersByYear.put(province, orderNames);

                } else {

                    List<String> orderNames = ordersByYear.get(province);
                    orderNames.add(name);

                    ordersByYear.put(province, orderNames);
                }
            }
        }

        return ordersByYear;
    }

    private JSONObject requestJson (String requestUrl) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;

        try {

            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            result = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //parse JSON
        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;

    }


}
