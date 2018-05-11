package com.example.james.shopifychallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProvinceActivity extends AppCompatActivity {

    private ListView lvFullList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);

        lvFullList = (ListView)findViewById(R.id.lvFullList);

        Intent intent = getIntent();
        Map<String, List<String>> orders = (HashMap<String, List<String>>)intent.getSerializableExtra("orders");

        Map<String, List<String>> ordersOrdered = new TreeMap<String, List<String>>(orders);

        List<String> outputProvince = new ArrayList<String>();

        for (Map.Entry<String, List<String>> pairProvince : ordersOrdered.entrySet()) {

            outputProvince.add(pairProvince.getKey());

            for (String orderName : pairProvince.getValue()) {
                outputProvince.add("    " + orderName);
            }
        }

        final ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_item, R.id.tvListItem,  outputProvince.toArray(new String[outputProvince.size()]));

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                lvFullList.setAdapter(provinceAdapter);
            }
        });

    }
}
