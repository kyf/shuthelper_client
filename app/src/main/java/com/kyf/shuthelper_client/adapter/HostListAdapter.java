package com.kyf.shuthelper_client.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kyf.shuthelper_client.R;

import java.util.List;
import java.util.Map;


/**
 * Created by kyf on 2015/10/1.
 */
public class HostListAdapter extends BaseAdapter {

    private static final String LogTag = "HostListAdapter";

    private Context mContext;

    private List<Map<String, String>> dataset;

    public HostListAdapter(Context context, List<Map<String, String>> ds) {
        mContext = context;
        dataset = ds;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0 || position > (getCount() - 1)) return null;
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result;
        TextView  hostnameview, ipview;
        ContactHolder contactHolder;

        if (convertView != null) {
            result = convertView;
            contactHolder = (ContactHolder) result.getTag();
            hostnameview = contactHolder.hostnameview;
            ipview = contactHolder.ipview;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(R.layout.hostitem, null);
            contactHolder = new ContactHolder();
            hostnameview = (TextView) result.findViewById(R.id.hostnameview);
            ipview = (TextView) result.findViewById(R.id.ipview);

            contactHolder.hostnameview = hostnameview;
            contactHolder.ipview = ipview;

            result.setTag(contactHolder);
        }

        Map<String, String> current = dataset.get(position);
        String hostname = current.get("hostname");
        String ip = current.get("ip");

        hostnameview.setText(hostname);
        ipview.setText(ip);

        return result;
    }

    public class ContactHolder {

        public TextView hostnameview, ipview;

    }
}
