package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.User;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<User> {

    private int resourceid;

    static class ViewHolder{
        TextView account ;
        TextView type ;
    }

    public EmployeeAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        resourceid = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        User u = getItem(position);
        View v;
        ViewHolder holder;
        if(convertView==null)
        {
            v = LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
            convertView=v;
            holder = new ViewHolder();
            holder.account= (TextView)v.findViewById(R.id.account);
            holder.type= (TextView)v.findViewById(R.id.type_user);
            convertView.setTag(holder);

        }else {
            v= convertView;
            holder=(ViewHolder) convertView.getTag();
        }

//        TextView account = (TextView)v.findViewById(R.id.account);
//        TextView type = (TextView)v.findViewById(R.id.type_user);

        Log.e("employeenoUUUU",u.getEmp_no());
        if(u.getType()==1){
            holder.type.setText("管理员");
        }else{
            holder.type.setText("售票员");
        }
        holder.account.setText(u.getEmp_no());
        return v;
    }

}
