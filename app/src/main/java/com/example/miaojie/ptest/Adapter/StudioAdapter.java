package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.Studio;

import java.util.List;

public class StudioAdapter extends ArrayAdapter<Studio> {
    private int resourceid;
    public StudioAdapter(@NonNull Context context, int resource, @NonNull List<Studio> objects) {
        super(context, resource, objects);
        resourceid = resource;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Studio s = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceid, parent, false);
        } else {
            view = convertView;
        }
        TextView studio_name = (TextView)view.findViewById(R.id.studio_name);
        TextView studio_row = (TextView)view.findViewById(R.id.studio_row);
        TextView studio_col = (TextView)view.findViewById(R.id.studio_col);
        TextView studio_introduction = (TextView)view.findViewById(R.id.studio_introduction);
        TextView studio_status = (TextView)view.findViewById(R.id.studio_status);
        TextView studio_id = (TextView)view.findViewById(R.id.studio_id);
        studio_name.setText(s.getStudio_name());
        studio_row.setText(s.getStudio_row_count()+"");
        studio_col.setText(s.getStudio_col_count()+"");
        studio_introduction.setText(s.getStudio_introduction());
        studio_status.setText(s.getStudio_flag()+"");
        studio_id.setText(s.getStudio_id()+"");



        return view;
    }
}
