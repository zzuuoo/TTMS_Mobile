package com.example.miaojie.ptest.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaojie.ptest.Activity.LoginActivity;
import com.example.miaojie.ptest.Activity.MainActivity;
import com.example.miaojie.ptest.R;

/**
 * Created by miaojie on 2017/3/23.
 */

public class PersonalFragment extends Fragment{
    private View view;
    private TextView userNickName;
    private ImageView head_image;
    private ListMenuItemView userOrder;
    private ListMenuItemView logout;
    private ListMenuItemView about_me;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_personal,container,false);
        userNickName= (TextView) view.findViewById(R.id.me_layout_name_tips);
        head_image = (ImageView)view.findViewById(R.id.me_layout_head_image);
        about_me = (ListMenuItemView) view.findViewById(R.id.me_layout_about_us);
        about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("����");
                dialog.setMessage("�����⣬����ϵ��18829071821");
                dialog.setCancelable(true);
                dialog.show();
            }
        });
        logout = (ListMenuItemView)view.findViewById(R.id.me_layout_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.isLogin)
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("����");
                    dialog.setMessage("ȷ��ע����");
                    dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.isLogin=false;
                            MainActivity.user=null;
                            userNickName.setText("�����¼");
                            head_image.setImageResource(R.drawable.home_people);
                            userNickName.setClickable(true);
                            head_image.setClickable(true);
                            getActivity().finish();
                            startActivity(new Intent(getContext(), LoginActivity.class));

                        }
                    });
                    dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "ȡ��", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();


                }
            }
        });
        userOrder= (ListMenuItemView) view.findViewById(R.id.me_layout_my_infomation);
        userOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!MainActivity.isLogin)
//                {
//                    Toast.makeText(getContext(),"δ��¼�����ȵ�¼",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                startActivity(new Intent(getContext(), OrderInfoActivity.class));
            }
        });
        userNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.isLogin)
                {
                    getActivity().finish();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }else{

                }

            }
        });
        head_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.isLogin)
                {
                    getActivity().finish();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }else{

                }
            }
        });
        view.findViewById(R.id.me_layout_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("����");
                dialog.setMessage("ȷ���˳���");
                dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.isLogin=false;
                        MainActivity.user=null;
                        userNickName.setText("�����¼");
                        head_image.setImageResource(R.drawable.home_people);
                        userNickName.setClickable(true);
                        head_image.setClickable(true);
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "ȡ��", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(MainActivity.user!=null&&MainActivity.isLogin)
        {
            userNickName.setText(MainActivity.user.getEmp_no());
            head_image.setImageResource(R.drawable.eye_can_see);
        }


    }
}
