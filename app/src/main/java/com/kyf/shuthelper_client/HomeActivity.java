package com.kyf.shuthelper_client;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.kyf.shuthelper_client.adapter.HostListAdapter;
import com.kyf.shuthelper_client.view.MyListView;
import com.kyf.shuthelper_client.view.MyLoading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity implements MyListView.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView nohostview;

    private MyLoading myLoading;

    private HostListAdapter hostListAdapter;

    private static final String LogTag = "HomeActivity";

    private MyListView hostlistview;

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
        hostlistview = (MyListView) findViewById(R.id.hostlistview);
        nohostview = (TextView) findViewById(R.id.nohostview);
        nohostview.setOnClickListener(this);
        myLoading = new MyLoading(this);
        myLoading.setContent(getResources().getString(R.string.find_host_tip));
        myLoading.show();
        hostlistview.setDividerHeight(0);
        hostlistview.setonRefreshListener(this);
        hostlistview.setOnItemClickListener(this);
        dataSet = new ArrayList<Map<String, String>>();

        fillDataSet();

        hostListAdapter = new HostListAdapter(this, dataSet);
        hostlistview.setAdapter(hostListAdapter);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch(id){
            case R.id.nohostview:{
                myLoading.show();
                onRefresh();
                break;
            }
        }
    }

    private void fillDataSet(){
        dataSet.clear();
        for(int i = 0; i < 0; i++) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("hostname", "我的计算机11111");
            item.put("ip", "192.168.0.99");
            dataSet.add(item);
        }
        if(dataSet.size() > 0){
            nohostview.setVisibility(View.GONE);
            hostlistview.setVisibility(View.VISIBLE);
        }else{
            hostlistview.setVisibility(View.GONE);
            nohostview.setVisibility(View.VISIBLE);
        }
        myLoading.dismiss();
    }

    @Override
    public void onRefresh(){
        fillDataSet();
        hostListAdapter.notifyDataSetChanged();
        hostlistview.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View var2, int var3, long var4){
        TextView ip = (TextView) parent.findViewById(R.id.ipview);
        AlertView alert = new AlertView(this);

    }

}
