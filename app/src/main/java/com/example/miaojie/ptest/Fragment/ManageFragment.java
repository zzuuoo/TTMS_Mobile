package com.example.miaojie.ptest.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.miaojie.ptest.Activity.MainActivity;
import com.example.miaojie.ptest.Activity.PlayManageActivity;
import com.example.miaojie.ptest.Activity.ScheduleManageActivity;
import com.example.miaojie.ptest.Activity.StudioManageActivity;
import com.example.miaojie.ptest.Activity.StudioSeatManageActivity;
import com.example.miaojie.ptest.Activity.UserManageActivity;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.MyApplication;

public class ManageFragment extends Fragment implements View.OnClickListener{

    Button play,user,schedule,seat,studio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_management,null,false);
        play= root.findViewById(R.id.button_play);
        play.setOnClickListener(this);
        user= root.findViewById(R.id.button_user);
        user.setOnClickListener(this);
        schedule= root.findViewById(R.id.button_schedule);
        schedule.setOnClickListener(this);
        seat= root.findViewById(R.id.button_seat);
        seat.setOnClickListener(this);
        studio= root.findViewById(R.id.button_studio);
        studio.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(MainActivity.user!=null&&MainActivity.user.getType()==1)
        {
            switch (view.getId()){
                case R.id.button_play:
                    intent = new Intent(getActivity(), PlayManageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_schedule:
                    intent = new Intent(getActivity(), ScheduleManageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_seat:
                    intent = new Intent(getActivity(), StudioSeatManageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_studio:
                    intent = new Intent(getActivity(), StudioManageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_user:
                    intent = new Intent(getActivity(), UserManageActivity.class);
                    startActivityForResult(intent,1);
                    break;
                default:
                    break;
            }
        }else{
            Toast.makeText(MyApplication.getContext(),"��û��Ȩ��",Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if(resultCode==1)
                {
                    Log.d("1111","1111");
                    Toast.makeText(getActivity(),"qq",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
