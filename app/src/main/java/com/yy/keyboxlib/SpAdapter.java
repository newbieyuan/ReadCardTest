package com.yy.keyboxlib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yy.serialporttest.R;

import java.util.List;

/**
 * 创建人：   yy
 * 创建时间： 2018/10/23
 * 功能描述:
 * 版本号：
 */
public class SpAdapter extends BaseAdapter {
    private List<String> serialPortPathList;
    private Context context;

    public SpAdapter(List<String> serialPortPathList, Context context) {
        this.serialPortPathList = serialPortPathList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return serialPortPathList== null ? 0:serialPortPathList.size();
    }

    @Override
    public Object getItem(int i) {
        return serialPortPathList== null ? null:serialPortPathList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler viewHodler = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.sp_item, null);
            viewHodler = new ViewHodler();
            viewHodler.name = (TextView) view.findViewById(R.id.tvName);
            viewHodler.path = (TextView) view.findViewById(R.id.tvPath);
            view.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) view.getTag();
        }

        String serialPortPath = serialPortPathList.get(i);
        viewHodler.path.setText(serialPortPath);
        return view;
    }

    class ViewHodler {
        TextView name, path;
    }
}
