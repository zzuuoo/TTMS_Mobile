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

    static class ViewHolder{
        TextView studio_name;
        TextView studio_row ;
        TextView studio_col ;
        TextView studio_introduction ;
        TextView studio_status ;
        TextView studio_id ;
    }

    private int resourceid;
    public StudioAdapter(@NonNull Context context, int resource, @NonNull List<Studio> objects) {
        super(context, resource, objects);
        resourceid = resource;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        Studio s = getItem(position);

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceid, parent, false);
            holder = new ViewHolder();
            holder.studio_name = (TextView)view.findViewById(R.id.studio_name);
            holder.studio_row = (TextView)view.findViewById(R.id.studio_row);
            holder.studio_col = (TextView)view.findViewById(R.id.studio_col);
            holder.studio_introduction = (TextView)view.findViewById(R.id.studio_introduction);
            holder.studio_status = (TextView)view.findViewById(R.id.studio_status);
            holder.studio_id = (TextView)view.findViewById(R.id.studio_id);
            convertView = view;
            convertView.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
//        TextView studio_name = (TextView)view.findViewById(R.id.studio_name);
//        TextView studio_row = (TextView)view.findViewById(R.id.studio_row);
//        TextView studio_col = (TextView)view.findViewById(R.id.studio_col);
//        TextView studio_introduction = (TextView)view.findViewById(R.id.studio_introduction);
//        TextView studio_status = (TextView)view.findViewById(R.id.studio_status);
//        TextView studio_id = (TextView)view.findViewById(R.id.studio_id);
        holder.studio_name.setText(s.getStudio_name());
        holder.studio_row.setText(s.getStudio_row_count()+"");
        holder.studio_col.setText(s.getStudio_col_count()+"");
        holder.studio_introduction.setText(s.getStudio_introduction());
        if(s.getStudio_flag()<1){
            holder.studio_status.setText("´ýÓÃ");
        }else{
            holder.studio_status.setText("ÔÚÓÃ");
        }

        holder.studio_id.setText(s.getStudio_id()+"");



        return view;
    }
}
