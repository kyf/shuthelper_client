package com.kyf.shuthelper_client;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ListView;

import com.kyf.shuthelper_client.adapter.HostListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity {

    private ListView hostlistview;

    private List<Map<String, String>> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayout = R.layout.activity_home;
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_host_list);

        initView();
    }

    private void initView(){
        hostlistview = (ListView) findViewById(R.id.hostlistview);
        dataSet = new ArrayList<Map<String, String>>();

        /*demo begin*/
        Map<String, String> item = new HashMap<String, String>();
        item.put("hostname", "");
        item.put("ip", "");
        /*demo end*/

        HostListAdapter hostListAdapter = new HostListAdapter(this, dataSet);
        hostlistview.setAdapter(hostListAdapter);
    }

}
