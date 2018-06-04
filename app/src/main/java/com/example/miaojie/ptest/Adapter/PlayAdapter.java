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
    static class ViewHolder{
        TextView play_name ;
        TextView play_length ;
        TextView play_introduction ;
        TextView play_lang_type;
        ImageView play_image ;
        TextView price ;
        TextView status ;
    }
    private int resourceid;
    private Context context;
     public PlayAdapter(@NonNull Context context, int resource, @NonNull List<Play> objects) {
         super(context, resource, objects);
         resourceid = resource;
         this.context=context;
     }

     public View getView(int position, View convertView, ViewGroup parent)
     {
         ViewHolder holder;
         Play p = getItem(position);
         View view;
         if(convertView==null)
         {
             view= LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
             convertView=view;
             holder = new ViewHolder();
             holder.play_name = (TextView)view.findViewById(R.id.Play_Item_Name);
             holder.play_length = (TextView)view.findViewById(R.id.play_length);
             holder.play_introduction = (TextView)view.findViewById(R.id.Play_Item_Introduction);
             holder.play_lang_type = (TextView)view.findViewById(R.id.Play_Item_type_lang);
             holder.play_image = (ImageView)view.findViewById(R.id.play_Item_Img);
             holder.price = (TextView)view.findViewById(R.id.play_price);
             holder.status = (TextView)view.findViewById(R.id.play_status);
             convertView.setTag(holder);
         }else{
             view = convertView;
             holder = (ViewHolder)convertView.getTag();
         }

         holder.price.setText(String.valueOf(p.getPlay_ticket_price()));
         if(p.getPlay_status()<=0){
             holder.status.setText("待售");
         }else{
             holder.status.setText("在售");
         }
         holder.play_image.setImageResource(R.drawable.a1);
//         Toast.makeText(getContext(),  p.getPlay_length()+"",Toast.LENGTH_SHORT).show();
         holder.play_name.setText(p.getPlay_name());
         holder.play_length.setText(String.valueOf(p.getPlay_length())+"分钟");
         holder.play_introduction.setText(p.getPlay_introdution());
         holder.play_lang_type.setText("语言:"+p.getPlay_lang()+"\n类型:"+p.getPlay_type());

         return view;
     }
 }
