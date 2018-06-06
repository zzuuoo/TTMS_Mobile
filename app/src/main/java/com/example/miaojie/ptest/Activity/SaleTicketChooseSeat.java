package com.example.miaojie.ptest.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.Utils.SeatTable;
import com.example.miaojie.ptest.pojo.Play;
import com.example.miaojie.ptest.pojo.Schedule;
import com.example.miaojie.ptest.pojo.Seat;
import com.example.miaojie.ptest.pojo.Studio;
import com.example.miaojie.ptest.pojo.Ticket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaleTicketChooseSeat extends AppCompatActivity {

    private SeatTable seatTableView;
    private Toolbar toolbar;
    private TextView textView;
    private ArrayList<Seat> seats = new ArrayList<>();
    private ArrayList<Seat>chooseSeats = new ArrayList<>();
    private ArrayList<Ticket> tickets = new ArrayList<>();
//    private ArrayList<Ticket> choosetickets = new ArrayList<>();
    private Handler handler;
    private Schedule schedule;
    private Play play;
    private Studio studio;
    private LoadingDialog dialog;
    private Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_ticket_choose_seat);
        Intent intent = getIntent();
        schedule = (Schedule)intent.getSerializableExtra("schedule");
        dialog = new LoadingDialog(this,"加载中...");
        initdata();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        dialog.close();
                        init();
                        break;
                    case 2:
                        dialog.close();
                        Toast.makeText(getApplicationContext(),"成功买了了"+msg.arg1+"张票",Toast.LENGTH_SHORT).show();
                        chooseSeats.clear();
                        seatTableView.removeAll();
                        seatTableView.invalidate();
                        break;
                }
            }
        };

    }
    public  void init()
    {
        toolbar= (Toolbar) findViewById(R.id.sale_ticket_seat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView= (TextView) findViewById(R.id.sale_ticket_play_name);
        textView.setText(play.getPlay_name());

        seatTableView = (SeatTable) findViewById(R.id.sale_ticket_seatView);
        seatTableView.setScreenName(studio.getStudio_name());//设置屏幕名称
        seatTableView.setData(studio.getStudio_row_count(),studio.getStudio_col_count());

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {

                for(Seat s:seats)
                {
                    if(row==s.getSeat_row()-1&&column==s.getSeat_column()-1)
                    {
                        if(s.getSeat_status()==0)
                        {
                            return false;
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                Seat seatd=null;
                Ticket ticketd=null;
            for(Seat s :seats)
            {
                if(row==s.getSeat_row()-1&&column==s.getSeat_column()-1)
                {
                    seatd=s;

                }
            }
            if(seatd!=null)
            {
                for(Ticket t :tickets)
                {
                    if(t.getSeat_id()==seatd.getSeat_id())
                    {

                    }
                }
            }
            for(Ticket t :tickets)
            {
                    if(t.getSeat_id()==seatd.getSeat_id())
                    {
                        ticketd=t;
                    }
            }

            if(ticketd!=null&&ticketd.getTicket_status()==1)
            {
                    return true;
            }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                for(Seat s :seats)
                {
                    if(s.getSeat_column()-1==column&&s.getSeat_row()-1==row)
                    {
                        chooseSeats.add(s);
                    }
                }
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
        buy = (Button)findViewById(R.id.buy_ticket_button_new);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.isLogin)
                {
                    Toast.makeText(getApplicationContext(),"请登录",Toast.LENGTH_SHORT).show();
                    return;
                 }
                 if(chooseSeats.size()<1)
                 {
                     Toast.makeText(getApplicationContext(),"请先选座",Toast.LENGTH_SHORT).show();
                     return;
                 }
                 dialog = new LoadingDialog(SaleTicketChooseSeat.this,"购买中。。。");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        ContentValues contentValues = new ContentValues();
                        int count = 0;
                        Cursor cursor=null;
                        Date date=null;
                        for(Seat s:chooseSeats)
                        {

//                            cursor = sqLiteDatabase.query("ticket",null,"seat_id = ? and sched_id = ?",
//                                    new String[]{String.valueOf(s.getSeat_id()),String.valueOf(schedule.getSched_id())},null,null,null);
//                            if(cursor.moveToFirst()){
//                                try {
//                                    date = format.parse(cursor.getString(cursor.getColumnIndex("ticket_locked_time")));
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            if(date!=null&&date.getTime()+60*60*2*1000<new Date().getTime()){
//                                continue;
//                            }

                            contentValues.clear();
                            contentValues.put("ticket_status",1);
                            int x = sqLiteDatabase.update("ticket",contentValues,"seat_id = ? and sched_id = ?",
                                    new String[]{String.valueOf(s.getSeat_id()),String.valueOf(schedule.getSched_id())});
                            count =count+x;
                        }
                        if(cursor!=null)
                        {
                            cursor.close();
                        }
                        initTicket();
                        Message message = new Message();
                        message.what = 2;
                        message.arg1=count;
                        handler.sendMessage(message);
                    }
                }).start();


            }
        });

    }

    public void initTicket()
    {
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        Cursor cursor ;

        tickets.clear();
        cursor = sqLiteDatabase.query("ticket", null, " sched_id = ?", new String[]{String.valueOf(schedule.getSched_id())}, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                Ticket ticket = new Ticket();
                ticket.setTicket_id(cursor.getInt(cursor.getColumnIndex("ticket_id")));
                ticket.setSched_id(cursor.getInt(cursor.getColumnIndex("sched_id")));
                ticket.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
                ticket.setTicket_price(cursor.getDouble(cursor.getColumnIndex("ticket_price")));
                if(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))!=null)
                {
                    try {
                        ticket.setTicket_locked_time(format.parse(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                ticket.setTicket_status(cursor.getInt(cursor.getColumnIndex("ticket_status")));
                tickets.add(ticket);

            } while (cursor.moveToNext());
        }
    }

    public void initdata()
    {
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


                Cursor cursor = sqLiteDatabase.query("play", null, " play_id = ?", new String[]{String.valueOf(schedule.getPlay_id())}, null, null, null);
                if (cursor.moveToFirst()) {

                    do {
                        play = new Play();
                        play.setPlay_name(cursor.getString(cursor.getColumnIndex("play_name")));
                        play.setPlay_status(cursor.getInt(cursor.getColumnIndex("play_status")));
                        play.setPlay_ticket_price(cursor.getDouble(cursor.getColumnIndex("play_ticket_price")));
                        play.setPlay_image(cursor.getString(cursor.getColumnIndex("play_image")));
                        play.setPlay_length(cursor.getInt(cursor.getColumnIndex("play_length")));
                        play.setPlay_introdution(cursor.getString(cursor.getColumnIndex("play_introduction")));
                        play.setPlay_id(cursor.getInt(cursor.getColumnIndex("play_id")));
                        Log.e("play_id", cursor.getInt(cursor.getColumnIndex("play_id")) + "");
                        play.setPlay_lang(cursor.getString(cursor.getColumnIndex("play_lang")));
                        play.setPlay_type(cursor.getString(cursor.getColumnIndex("play_type")));
                    } while (cursor.moveToNext());
                }
                cursor = sqLiteDatabase.query("studio", null, " studio_id = ?", new String[]{String.valueOf(schedule.getStudio_id())}, null, null, null);
                if (cursor.moveToFirst()) {

                    do {
                        studio = new Studio();
                        studio.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
                        studio.setStudio_name(cursor.getString(cursor.getColumnIndex("studio_name")));
                        studio.setStudio_col_count(cursor.getInt(cursor.getColumnIndex("studio_col_count")));
                        studio.setStudio_row_count(cursor.getInt(cursor.getColumnIndex("studio_row_count")));
                        studio.setStudio_introduction(cursor.getString(cursor.getColumnIndex("studio_introduction")));
                        studio.setStudio_flag(cursor.getInt(cursor.getColumnIndex("studio_flag")));
                    } while (cursor.moveToNext());

                }

                cursor = sqLiteDatabase.query("ticket", null, " sched_id = ?", new String[]{String.valueOf(schedule.getSched_id())}, null, null, null);
                if (cursor.moveToFirst()) {
                    tickets.clear();
                    do {
                        Ticket ticket = new Ticket();
                        ticket.setTicket_id(cursor.getInt(cursor.getColumnIndex("ticket_id")));
                        ticket.setSched_id(cursor.getInt(cursor.getColumnIndex("sched_id")));
                        ticket.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
                        ticket.setTicket_price(cursor.getDouble(cursor.getColumnIndex("ticket_price")));
                        if(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))!=null)
                        {
                            try {
                                ticket.setTicket_locked_time(format.parse(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        ticket.setTicket_status(cursor.getInt(cursor.getColumnIndex("ticket_status")));
                        tickets.add(ticket);

                    } while (cursor.moveToNext());
                }
                cursor = sqLiteDatabase.query("seat", null, " studio_id = ?", new String[]{String.valueOf(studio.getStudio_id())}, null, null, null);
                if (cursor.moveToFirst()) {
                        seats.clear();
                    do {
                        Seat seat = new Seat();
                       seat.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
                       seat.setSeat_status(cursor.getInt(cursor.getColumnIndex("seat_status")));
                       seat.setSeat_column(cursor.getInt(cursor.getColumnIndex("seat_column")));
                       seat.setSeat_row(cursor.getInt(cursor.getColumnIndex("seat_row")));
                       seat.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
                       seats.add(seat);

                    } while (cursor.moveToNext());
                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();


    }


}
