package com.example.miaojie.ptest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.pojo.Play;

import java.util.List;

public class PlayAdapter extends ArrayAdapter<Play>
{
    private int resourceid;
    private Context context;
     public PlayAdapter(@NonNull Context context, int resource, @NonNull List<Play> objects) {
         super(context, resource, objects);
         resourceid = resource;
         this.context=context;
     }

     public View getView(int position, View convertView, ViewGroup parent)
     {
         Play p = getItem(position);
         View view;
         if(convertView==null)
         {
             view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
         }else{
             view = convertView;
         }
         TextView play_name = (TextView)view.findViewById(R.id.Play_Item_Name);
         TextView play_length = (TextView)view.findViewById(R.id.play_length);
         TextView play_introduction = (TextView)view.findViewById(R.id.Play_Item_Introduction);
         TextView play_lang_type = (TextView)view.findViewById(R.id.Play_Item_type_lang);
         ImageView play_image = (ImageView)view.findViewById(R.id.play_Item_Img);
         TextView price = (TextView)view.findViewById(R.id.play_price);
         TextView status = (TextView)view.findViewById(R.id.play_status);
         price.setText(String.valueOf(p.getPlay_ticket_price()));
         if(p.getPlay_status()==0){
             status.setText("待售");
         }else{
             status.setText("已售");
         }
         play_image.setImageResource(R.drawable.a1);
//         Toast.makeText(getContext(),  p.getPlay_length()+"",Toast.LENGTH_SHORT).show();
         play_name.setText(p.getPlay_name());
         play_length.setText(String.valueOf(p.getPlay_length())+"分钟");
         play_introduction.setText(p.getPlay_introdution());
         play_lang_type.setText("语言:"+p.getPlay_lang()+"\n类型："+p.getPlay_type());

         return view;
     }
 }
