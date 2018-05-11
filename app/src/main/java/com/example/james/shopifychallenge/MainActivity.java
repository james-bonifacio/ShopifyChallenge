package com.example.james.shopifychallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    private ListView lvProvince;
    private ListView lvYear;
    private HashMap<String, List<String>> ordersByProvince;
    private HashMap<String, List<String>> ordersByYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvProvince = (ListView)findViewById(R.id.lvProvince);
        lvYear = (ListView)findViewById(R.id.lvYear);

        new NetworkThread().execute(URL);
    }

    public void onClick(View v) {

        Intent intent = new Intent(this, ProvinceActivity.class);
        intent.putExtra("orders", ordersByProvince);
        startActivity(intent);

    }

    private class NetworkThread extends AsyncTask<String, String, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {

            Api api = new Api();
            ordersByProvince = api.getOrdersByProvince(params[0]);
            ordersByYear = api.getOrdersByYear(params[0]);

            List<String> outputProvince = new ArrayList<String>();

            for (HashMap.Entry<String, List<String>> pairProvince : ordersByProvince.entrySet()) {

                String outputLine = pairProvince.getValue().size() + " orders from " + pairProvince.getKey();
                outputProvince.add(outputLine);
            }

            List<String> outputYear = new ArrayList<String>();

            for (HashMap.Entry<String, List<String>> pairYear : ordersByYear.entrySet()) {
                List<String> orders = pairYear.getValue();
                String outputLine = orders.size() + " orders created in " + pairYear.getKey();
                outputYear.add(outputLine);

                for (int i=0; i < Math.min(orders.size(), 10); i++) {
                    outputYear.add(orders.get(i));
                }
            }


            final ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.list_item, R.id.tvListItem,  outputProvince.toArray(new String[outputProvince.size()]));

            final ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.list_item, R.id.tvListItem,  outputYear.toArray(new String[outputYear.size()]));

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    lvProvince.setAdapter(provinceAdapter);
                    lvYear.setAdapter(yearAdapter);
                }
            });


            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }


    }//end Network Thread
}
