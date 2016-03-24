package com.arguide.william.arguide.View;

/**
 * Created by william on 15-4-11.
 */
import java.util.List;
import com.arguide.william.arguide.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private TextView textView;
    private List<String> mSelfData;

    public MyAdapter(Context context, List<String> data) {
        this.mSelfData = data;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mSelfData.size();
    }

    public Object getItem(int position) {
        return mSelfData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 生成ListView的Item布局。
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.my_listview_item, null);
        }
        String str = mSelfData.get(position);
        textView = (TextView)convertView.findViewById(R.id.listview_item_text);
        textView.setText(str);
        return convertView;
    }

}
