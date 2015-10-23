package com.kyf.shuthelper_client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.kyf.shuthelper_client.adapter.HostListAdapter;
import com.kyf.shuthelper_client.util.Network;
import com.kyf.shuthelper_client.view.MyListView;
import com.kyf.shuthelper_client.view.MyLoading;

import org.json.JSONException;
import org.json.JSONObject;

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

    private Network myNetwork;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001: {
                    Toast.makeText(HomeActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1002: {
                    Toast.makeText(HomeActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1003: {
                    List<String> hosts = (List<String>) msg.obj;
                    for (String host : hosts) {
                        host = host.trim();
                        try {
                            JSONObject jsonObject = new JSONObject(host);
                            String ip = jsonObject.getString("ip");
                            String hostname = jsonObject.getString("hostname");
                            Map<String, String> item = new HashMap<String, String>();
                            item.put("hostname", hostname);
                            item.put("ip", ip);
                            dataSet.add(item);
                        } catch (JSONException e) {

                        }
                    }


                    if (dataSet.size() > 0) {
                        nohostview.setVisibility(View.GONE);
                        hostlistview.setVisibility(View.VISIBLE);
                    } else {
                        hostlistview.setVisibility(View.GONE);
                        nohostview.setVisibility(View.VISIBLE);
                    }
                    hostListAdapter.notifyDataSetChanged();
                    hostlistview.onRefreshComplete();
                    myLoading.dismiss();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayout = R.layout.activity_home;
        super.onCreate(savedInstanceState);

        MyApplication app = (MyApplication) getApplication();
        app.setHandler(myHandler);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_host_list);

        initView();
    }

    private void initView() {
        hostlistview = (MyListView) findViewById(R.id.hostlistview);
        nohostview = (TextView) findViewById(R.id.nohostview);
        nohostview.setOnClickListener(this);
        myLoading = new MyLoading(this);
        myLoading.setContent(getResources().getString(R.string.find_host_tip));
        myLoading.setCanceledOnTouchOutside(false);
        myLoading.show();
        hostlistview.setDividerHeight(0);
        hostlistview.setonRefreshListener(this);
        hostlistview.setOnItemClickListener(this);
        dataSet = new ArrayList<Map<String, String>>();
        myNetwork = new Network(this);

        fillDataSet();

        hostListAdapter = new HostListAdapter(this, dataSet);
        hostlistview.setAdapter(hostListAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.nohostview: {
                onRefresh();
                break;
            }
        }
    }

    private void fillDataSet() {
        dataSet.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                myNetwork.listenUDP();
            }
        }).start();

        myNetwork.boardcast();
        hostlistview.setVisibility(View.GONE);
        nohostview.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        myLoading.show();
        fillDataSet();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View var2, int position, long var4) {
        Map<String, String> item = dataSet.get(position - 1);
        String hostname = item.get("hostname");
        final String ip = item.get("ip");
        alertView = new AlertView("提示", null, "取消", new String[]{"关闭主机", "重启主机"}, new String[]{}, this, AlertView.Style.ActionSheet, new OnItemClickListener(){
            @Override
            public void onItemClick(Object var1, int var2) {
                String cmd = "";
                switch(var2) {
                    case 0: {
                        cmd = "shut";
                        break;
                    }
                    case 1:{
                        cmd = "reboot";
                        break;
                    }
                }
                if(cmd.equals(""))return;
                myNetwork.setURI("http://" + ip + ":7070/" + cmd);
                myNetwork.sendCmd();

            }
        });
        alertView.show();
    }



}
