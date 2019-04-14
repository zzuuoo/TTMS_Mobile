package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.Sale;

import java.text.SimpleDateFormat;
import java.util.List;

public class SaleAdapter extends ArrayAdapter<Sale> {

    static class ViewHolder{
        TextView sale_type ;
        TextView salePayment ;
        TextView saleChange ;
        TextView sale_status ;
        TextView sale_time ;
    }
    private int resourceid;
    public SaleAdapter(@NonNull Context context, int resource, @NonNull List<Sale> objects) {
        super(context, resource, objects);
        this.resourceid=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        Sale s = getItem(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceid, parent, false);
            holder = new ViewHolder();
            holder.sale_status = view.findViewById(R.id.sale_status);
            holder.sale_time = view.findViewById(R.id.sale_time);
            holder.sale_type= view.findViewById(R.id.sale_type);
            holder.saleChange= view.findViewById(R.id.saleChange);
            holder.salePayment= view.findViewById(R.id.salePayment);
            convertView = view;
            convertView.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.salePayment.setText(s.getSale_payment()+"");
        holder.saleChange.setText(s.getSale_change()+"");
        holder.sale_time.setText(format.format(s.getSale_time()));
        if(s.getSale_type()==1)
        {
            holder.sale_type.setText("销售单");
        }
        else{
            holder.sale_type.setText("退款单");
        }
        if(s.getSale_status()==1)
        {
            holder.sale_status.setText("已付款");
        }else{
            holder.sale_status.setText("待付款");
        }


        return view;
    }
}
