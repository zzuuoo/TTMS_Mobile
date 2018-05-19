package com.example.miaojie.ptest.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.SeatTableManage;
import com.example.miaojie.ptest.pojo.OrderInfo;
import com.example.miaojie.ptest.pojo.Seat;

import java.util.ArrayList;

public class SeatManageActivity extends AppCompatActivity {

    SeatTableManage seatTable;
    ArrayList<Seat> seats =null;
    ArrayList<Seat> chooseSeats = null;
    private Handler handler;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_manage);
        seatTable = (SeatTableManage) findViewById(R.id.seatmanagetableView);
        seatTable.setScreenName("我是测试厅");//设置屏幕名称
        seatTable.setMaxSelected(1000);//设置最多选中
        seats=new ArrayList<>();

        chooseSeats=new ArrayList<>();
//        handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                for(int i=0;i<orderInfos.size();i++)
//                {
//                    if(isThisSession(orderInfos.get(i)))
//                    {
//                        for(int j=0;j<orderInfos.get(i).getSeatInfos().size();j++)
//                            seatInfos.add(orderInfos.get(i).getSeatInfos().get(j));
//                    }
//                }
//                seatTableView.invalidate();
//            }
//        };
        seatTable.setSeatChecker(new SeatTableManage.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {

                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                Log.e("座位信息",seats.size()+"");
                for(int i=0;i<seats.size();i++)
                {

                    Seat seatInfo=seats.get(i);
                    Log.e("选择座位",seatInfo.getSeat_row()+"--"+row);
                    if(seatInfo.getSeat_row()-1==row&&seatInfo.getSeat_column()-1==column)
                        return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                Seat seat = new Seat();
                seat.setSeat_column(column+1);
                seat.setSeat_row(row+1);
                chooseSeats.add(seat);
            }

            @Override
            public void unCheck(int row, int column) {
                int i;
                for(i=0;i<chooseSeats.size();i++)
                {
                    Seat seatInfo=chooseSeats.get(i);
                    if(seatInfo.getSeat_row()-1==row&&seatInfo.getSeat_column()-1==column)
                        break;
                }
                chooseSeats.remove(i);
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTable.setData(10,15);
        //toolbar.setTitle(getIntent().getStringExtra("MovieName"));

        toolbar= (Toolbar) findViewById(R.id.mansge_seat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.update_seat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!MainActivity.isLogin)
//                {
//                    Toast.makeText(SeatManageActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                MainActivity.orderInfo.setSeatInfos(chooseSeatInfos);
//                MainActivity.orderInfo.setTicketNumber(chooseSeatInfos.size()+"");
//                Date date=new Date();
//                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String now=simpleDateFormat.format(date);
//                MainActivity.orderInfo.setBornTime(now);
//                OrderInfo orderInfo= MainActivity.orderInfo;
//                Log.e("Mainac",MainActivity.orderInfo.toString());
//                String seats="";
//                for(int i=0;i<chooseSeatInfos.size();i++)
//                {
//                    //Log.e("asd",chooseSeatInfos.get(i).getSeatX()+"--"+chooseSeatInfos.get(i).getSeatY());
//                    if(chooseSeatInfos.size()-1==i)
//                        seats+=chooseSeatInfos.get(i).getSeatX()+" "+chooseSeatInfos.get(i).getSeatY();
//                    else
//                        seats+=chooseSeatInfos.get(i).getSeatX()+" "+chooseSeatInfos.get(i).getSeatY()+",";
//
//                }
//                Log.e("qwe",seats);
//                //add(String tickets, float money, String time, String studio_name, String move_name, String start, String end, String seats, String usrname)
//
//                String finalSeats = seats;
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//
//                        IndentMgr.add(MainActivity.orderInfo.getTicketNumber()+"",
//                                MainActivity.orderInfo.getPrice(),
//                                MainActivity.orderInfo.getBornTime(),
//                                MainActivity.orderInfo.getCinemaName(),
//                                MainActivity.orderInfo.getMovieName(),
//                                MainActivity.orderInfo.getStartTime(),
//                                MainActivity.orderInfo.getEndTime(),
//                                finalSeats,
//                                MainActivity.userInfo.getUserName()
//                        );
//
//                    }
//
//                }.start();
//                for(int i=0;i<chooseSeatInfos.size();i++)
//                    seatInfos.add(chooseSeatInfos.get(i));
//                chooseSeatInfos.clear();
//                seatTableView.removeAll();
//                seatTableView.invalidate();
//
//                Toast.makeText(ChoosSeatActivity.this, "购买成功", Toast.LENGTH_SHORT).show();

            }
        });

//        toolbar.setNavigationOnClickListener();
    }

    private boolean isThisSession(OrderInfo orderInfo)
    {
        if(orderInfo.getMovieName().equals(MainActivity.orderInfo.getMovieName())&&
                orderInfo.getCinemaName().equals(MainActivity.orderInfo.getCinemaName())&&
                orderInfo.getStartTime().equals(MainActivity.orderInfo.getStartTime()))
            return true;
        return false;
    }

}