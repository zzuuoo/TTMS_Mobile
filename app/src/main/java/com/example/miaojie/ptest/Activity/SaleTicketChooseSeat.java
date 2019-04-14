package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyDatabaseHelper;
import com.example.miaojie.ptest.Utils.SeatTable;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Play;
import com.example.miaojie.ptest.pojo.PlayWeb;
import com.example.miaojie.ptest.pojo.Sale;
import com.example.miaojie.ptest.pojo.Schedule;
import com.example.miaojie.ptest.pojo.Seat;
import com.example.miaojie.ptest.pojo.SeatWeb;
import com.example.miaojie.ptest.pojo.Studio;
import com.example.miaojie.ptest.pojo.StudioWeb;
import com.example.miaojie.ptest.pojo.Ticket;
import com.example.miaojie.ptest.pojo.TicketWeb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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
    private Play play = new Play();
    private Studio studio = new Studio();
    private LoadingDialog dialog;
    private Button buy;
    RequestQueue requestQueue;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_ticket_choose_seat);
        Intent intent = getIntent();
        schedule = (Schedule)intent.getSerializableExtra("schedule");
        initdata();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 2:
                        dialog.close();
                        Toast.makeText(getApplicationContext(),"�ɹ���Ʊ",Toast.LENGTH_SHORT).show();
                        chooseSeats.clear();
                        seatTableView.removeAll();
                        seatTableView.invalidate();
                        break;
                    case 3:
                        i++;
                        if(i>3)
                        {
                            i=0;
                            Toast.makeText(getApplicationContext(),"�������",Toast.LENGTH_SHORT).show();
                            dialog.close();
                            init();
                        }
                        break;
                    case 4:
                        break;
                    case 5:
                        dialog.close();
                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        dialog.close();
                        Toast.makeText(getApplicationContext(),"��ѡƱ�ѱ�����������ѡƱ",Toast.LENGTH_LONG).show();
                        break;
                    case 7:
                        dialog.close();
                        break;
                    case 8:
                        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����");
                        dialog.show();
                        break;



                }
            }
        };

    }
    public JSONArray getArray(List<Ticket> tickets)
    {
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<tickets.size();i++) {
            Map<String, String> map = new HashMap<>();
            map.put("schedId", tickets.get(i).getSched_id() + "");
            map.put("seatId", tickets.get(i).getSeat_id() + "");
            map.put("ticketId", tickets.get(i).getTicket_id() + "");//�������
            map.put("ticketPrice", tickets.get(i).getTicket_price() + "");//�������
            map.put("ticketStatus", tickets.get(i).getTicket_status() + "");//�������
            JSONObject jsonObject = new JSONObject(map);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
    /**
     * ����Ʊ
     */
    public void buyTicket(List<Ticket> listt)
    {
//        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����");
//        dialog.show();
        JSONArray jsonArray = getArray(listt);
        OkHttpClient client = new OkHttpClient();//����OkHttpClient����
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//��������Ϊjson��ʽ��
        RequestBody body = RequestBody.create(JSON, jsonArray.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileTicket/updateTicketList?emp_id="+MainActivity.employee.getEmp_id())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message message = new Message();
                message.what = 5;
                handler.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.body().string().equals("failed"))
                {
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
//                        finish();
                }else{
                    initTicket();
                    Message message = new Message();
                    message.what = 2;
//                    message.arg1=Integer.valueOf(response.body().string());
//                    Toast.makeText(SaleTicketChooseSeat.this,response.body().string(),Toast.LENGTH_SHORT).show();
                    handler.sendMessage(message);
                }
            }

        });

//        OkHttpClient client1 = new OkHttpClient();//����OkHttpClient����
//        MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");//��������Ϊjson��ʽ��
//        RequestBody body1 = RequestBody.create(JSON1, jsonArray.toString());
//        Request request1 = new Request.Builder()
//                .url(Grob_var.host+"mobileTicket/selectTicketListBytickets")
//                .post(body1)
//                .build();
//        client1.newCall(request1).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                Message message = new Message();
//                message.what = 5;
//                handler.sendMessage(message);
//
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//
//            }
//
//        });

    }
    public void updateTicket()
    {

        List<Ticket> ticketBuy = new ArrayList<>();
        for(Seat s:chooseSeats)
        {
            Ticket ticket = new Ticket();
            ticket.setSched_id(schedule.getSched_id());
            ticket.setSeat_id(s.getSeat_id());
            ticket.setTicket_status(1);
            ticketBuy.add(ticket);
        }
        buyTicket(ticketBuy);

    }

    //�������ݳ�ʼ��
    public  void init()
    {
        toolbar= findViewById(R.id.sale_ticket_seat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView= findViewById(R.id.sale_ticket_play_name);
        textView.setText(play.getPlay_name());

        seatTableView = findViewById(R.id.sale_ticket_seatView);
        seatTableView.setScreenName(studio.getStudio_name());//������Ļ����
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
                        ticketd =t;
                    }
                }
            }


                return ticketd != null && ticketd.getTicket_status() == 1;
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
        buy = findViewById(R.id.buy_ticket_button_new);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.isLogin)
                {
                    Toast.makeText(getApplicationContext(),"���¼",Toast.LENGTH_SHORT).show();
                    return;
                 }
                 if(chooseSeats.size()<1)
                 {
                     Toast.makeText(getApplicationContext(),"����ѡ��",Toast.LENGTH_SHORT).show();
                     return;
                 }
//                 dialog = new LoadingDialog(SaleTicketChooseSeat.this,"�����С�����");
//                dialog.show();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                        ContentValues contentValues = new ContentValues();
//                        int count = 0;
//                        Cursor cursor=null;
//                        Date date=null;
//                        for(Seat s:chooseSeats)
//                        {
//
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
//
//                            contentValues.clear();
//                            contentValues.put("ticket_status",1);
//                            int x = sqLiteDatabase.update("ticket",contentValues,"seat_id = ? and sched_id = ?",
//                                    new String[]{String.valueOf(s.getSeat_id()),String.valueOf(schedule.getSched_id())});
//                            count =count+x;
//                        }
//                        if(cursor!=null)
//                        {
//                            cursor.close();
//                        }
//                        initTicket();
//                        Message message = new Message();
//                        message.what = 2;
//                        message.arg1=count;
//                        handler.sendMessage(message);
//                    }
//                }).start();
                /**
                 * ��Ʊ����
                 */

                dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����");
                dialog.show();
                lockTickets();
//                showSale();


            }
        });

    }

    /**
     * ���������Ա�ȷ��
     */
    public  void showSale()
    {
        Sale sale = new Sale();
        sale.setSale_status(1);
        sale.setSale_time(new Date());
        sale.setSale_type(1);
        sale.setSale_payment(schedule.getSched_ticket_price()*chooseSeats.size());
        sale.setSale_change(0);
        sale.setEmp_id(MainActivity.employee.getEmp_id());
        Looper.prepare();
        AlertDialog.Builder builder = new AlertDialog.Builder(SaleTicketChooseSeat.this);
        builder.setTitle("����")
                .setMessage("ӰƬ��"+play.getPlay_name()+"\n" +
                        "������"+chooseSeats.size()+"��\n" +
                        "�ܽ��:"+sale.getSale_payment())
                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Message message = new Message();
                        message.what=8;
                        handler.sendMessage(message);
                        AddSale(sale);
                        //���⴦��Ʊ
                    }
                })
                .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                                    unlockTickets();
                        //ȡ��Ʊ�ļ���
                    }
                });
        builder.show();
        Looper.loop();// ����loop�е�ѭ�����鿴��Ϣ����

    }


    /**
     * ��Ʊ���м���
     */
    private void lockTickets()
    {
//        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����");
//        dialog.show();
        List<Ticket> ticketLock = new ArrayList<>();
        for(Seat s:chooseSeats)
        {
            Ticket ticket = new Ticket();
            ticket.setSched_id(schedule.getSched_id());
            ticket.setSeat_id(s.getSeat_id());
            ticketLock.add(ticket);
        }

        JSONArray jsonArray = getArray(ticketLock);
        OkHttpClient client = new OkHttpClient();//����OkHttpClient����
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//��������Ϊjson��ʽ��
        RequestBody body = RequestBody.create(JSON, jsonArray.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileTicket/lockTicketList")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message message = new Message();
                message.what = 5;
                handler.sendMessage(message);
                unlockTickets();

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.body().string().equals("failed"))
                {
                    Message message = new Message();
                    message.what = 6;
                    handler.sendMessage(message);
//                        finish();
                }else{
//                    dialog.close();
                    Message message = new Message();
                    message.what = 7;
                    handler.sendMessage(message);
                    showSale();
                }
            }

        });

    }

    private void unlockTickets()
    {
        List<Ticket> ticketLock = new ArrayList<>();
        for(Seat s:chooseSeats)
        {
            Ticket ticket = new Ticket();
            ticket.setSched_id(schedule.getSched_id());
            ticket.setSeat_id(s.getSeat_id());
            ticketLock.add(ticket);
        }

        JSONArray jsonArray = getArray(ticketLock);
        OkHttpClient client = new OkHttpClient();//����OkHttpClient����
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//��������Ϊjson��ʽ��
        RequestBody body = RequestBody.create(JSON, jsonArray.toString());
        Request request = new Request.Builder()
                .url(Grob_var.host+"mobileTicket/unlockTicketList")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Message message = new Message();
                message.what = 5;
                handler.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if(response.body().string().equals("failed"))
                {
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
//                        finish();
                }else{

                }
            }

        });

    }




    /**
     * ��Ӷ������ݲ���ʾ
     * @param sale
     */
    private void AddSale(Sale sale) {



//        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        Map<String,String> map = new HashMap<>();
//        map.put("empId",sale.getEmp_id()+"");//�������
//        map.put("saleChange",sale.getSale_change()+"");//�������
//        map.put("salePayment",sale.getSale_payment()+"");//�������
//        map.put("saleStatus",sale.getSale_status()+"");//�������
//        map.put("saleTime",format1.format(sale.getSale_time()));//�������
//        map.put("saleType",sale.getSale_type()+"");
//
//        JSONObject jsonObject = new JSONObject(map);
//
//        OkHttpClient client = new OkHttpClient();//����OkHttpClient����
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//��������Ϊjson��ʽ��
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .url(Grob_var.host+"mobileSale/insertSale")
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Message message = new Message();
//                message.what = 5;
//                handler.sendMessage(message);
//
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                Message message = new Message();
//                if(response.body().string().equals("succeed"))
//                {
////                    dialog.close();
//                    updateTicket();
//                }else{
//                    message.what = 5;
//                    Toast.makeText(SaleTicketChooseSeat.this,"ʧ��",Toast.LENGTH_SHORT).show();
//                }
//                handler.sendMessage(message);
//            }
//
//        });
        updateTicket();

    }

    /**
     * ���¼���Ʊ����
     */
    public void initTicket()
    {
        SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        Cursor cursor ;
        i=3;
        tickets.clear();
        getTicketBySc();
//        cursor = sqLiteDatabase.query("ticket", null, " sched_id = ?", new String[]{String.valueOf(schedule.getSched_id())}, null, null, null);
//        if (cursor.moveToFirst()) {
//
//            do {
//                Ticket ticket = new Ticket();
//                ticket.setTicket_id(cursor.getInt(cursor.getColumnIndex("ticket_id")));
//                ticket.setSched_id(cursor.getInt(cursor.getColumnIndex("sched_id")));
//                ticket.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
//                ticket.setTicket_price(cursor.getDouble(cursor.getColumnIndex("ticket_price")));
//                if(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))!=null)
//                {
//                    try {
//                        ticket.setTicket_locked_time(format.parse(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ticket.setTicket_status(cursor.getInt(cursor.getColumnIndex("ticket_status")));
//                tickets.add(ticket);
//
//            } while (cursor.moveToNext());
//        }
    }

    /**
     * ��ȡ�ݳ�������
     */
    public void getSeatByStudioId(){
        //����һ���������
//        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����...");
//        dialog.show();
        requestQueue = Volley.newRequestQueue(this);
        String url = Grob_var.host+"/mobileSeat/getSeatByStudioId?studioId="+schedule.getStudio_id();

        StringRequest stringRequest =new StringRequest(url, new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    List<SeatWeb> seatWebs = new ArrayList<>();
                    seatWebs = gson.fromJson(s, new TypeToken<List<SeatWeb>>() {}.getType());
                    for(int i=0;i<seatWebs.size();i++)
                    {
                        Seat seat = new Seat();
                        seat.setSeat_status(seatWebs.get(i).getSeatStatus());
                        seat.setSeat_column(seatWebs.get(i).getSeatColumn());
                        seat.setSeat_row(seatWebs.get(i).getSeatRow());
                        seat.setStudio_id(seatWebs.get(i).getStudioId());
                        seat.setSeat_id(seatWebs.get(i).getSeatId());
                        seats.add(seat);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    /**
     * ��ȡ��Ӧ�ݳ��ƻ���Ʊ
     */
    public void getTicketBySc(){
        //����һ���������
//        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����...");
//        dialog.show();
        requestQueue = Volley.newRequestQueue(this);
        String url = Grob_var.host+"/mobileTicket/getTicketBySchedId?sched_id="+schedule.getSched_id();

        StringRequest stringRequest =new StringRequest(url, new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    List<TicketWeb> ticketWebs = new ArrayList<>();
                    ticketWebs = gson.fromJson(s, new TypeToken<List<TicketWeb>>() {}.getType());
                    for(int i=0;i<ticketWebs.size();i++)
                    {
                       Ticket ticket = new Ticket();
                       ticket.setTicket_id(ticketWebs.get(i).getTicketId());
                       ticket.setTicket_status(ticketWebs.get(i).getTicketStatus());
                       if (ticketWebs.get(i).getTicketLockedTime()>0)
                       {
                           ticket.setTicket_locked_time(new Date(ticketWebs.get(i).getTicketLockedTime()));
                       }
                       ticket.setTicket_price(ticketWebs.get(i).getTicketPrice());
                       ticket.setSeat_id(ticketWebs.get(i).getSeatId());
                       ticket.setSched_id(ticketWebs.get(i).getSchedId());
                       tickets.add(ticket);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    /**
     * ͨ���ݳ���id�ݳ�������
     */
    public void getStudioById(){
        //����һ���������
//        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"����...");
//        dialog.show();
        requestQueue = Volley.newRequestQueue(this);
        String url = Grob_var.host+"/mobileStudio/getStudioById?studio_id="+schedule.getStudio_id();

        StringRequest stringRequest =new StringRequest(url, new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    StudioWeb studioWebs;
                    studioWebs = gson.fromJson(s, StudioWeb.class);
                        studio = new Studio();
                        studio.setStudio_id(studioWebs.getStudioId());
                        studio.setStudio_flag(studioWebs.getStudioFlag());
                        studio.setStudio_introduction(studioWebs.getStudioIntroduction());
                        studio.setStudio_row_count(studioWebs.getStudioRowCount());
                        studio.setStudio_col_count(studioWebs.getStudioColCount());
                        studio.setStudio_name(studioWebs.getStudioName());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }



            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    /**
     * ��ȡӰƬ���ݣ�ͨ������
     */
    public void getPlayById(){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobilePlay/getPlayById?play_id="+schedule.getPlay_id(), new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {
//                Toast.makeText(SaleTicketChooseSeat.this,s,Toast.LENGTH_SHORT).show();
                try {
                    Gson gson = new Gson();
                    PlayWeb playWeb;
                    playWeb = gson.fromJson(s, PlayWeb.class);
                    play = new Play();
                    play.setPlay_id(playWeb.getPlayId());
                    play.setPlay_introdution(playWeb.getPlayIntroduction());
                    play.setPlay_type(playWeb.getPlayType());
                    play.setPlay_length(playWeb.getPlayLength());
                    play.setPlay_lang(playWeb.getPlayLang());
                    play.setPlay_image(playWeb.getPlayImage());
                    play.setPlay_ticket_price(playWeb.getPlayTicketPrice());
                    play.setPlay_name(playWeb.getPlayName());
                    play.setPlay_status(playWeb.getPlayStatus());
//                    Toast.makeText(SaleTicketChooseSeat.this,play.getPlay_name(),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(SaleTicketChooseSeat.this,volleyError.toString(),Toast.LENGTH_SHORT).show();
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);

            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    /**
     * ��ʼ������
     */
    public void initdata()
    {
        i=0;
        dialog = new LoadingDialog(SaleTicketChooseSeat.this,"��������..");
        dialog.show();
        seats.clear();
        getPlayById();
        getStudioById();
        getSeatByStudioId();
        getTicketBySc();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                SQLiteDatabase sqLiteDatabase = MyDatabaseHelper.getInstance().getWritableDatabase();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//
//                Cursor cursor = sqLiteDatabase.query("play", null, " play_id = ?", new String[]{String.valueOf(schedule.getPlay_id())}, null, null, null);
//                if (cursor.moveToFirst()) {
//
//                    do {
//                        play = new Play();
//                        play.setPlay_name(cursor.getString(cursor.getColumnIndex("play_name")));
//                        play.setPlay_status(cursor.getInt(cursor.getColumnIndex("play_status")));
//                        play.setPlay_ticket_price(cursor.getDouble(cursor.getColumnIndex("play_ticket_price")));
//                        play.setPlay_image(cursor.getString(cursor.getColumnIndex("play_image")));
//                        play.setPlay_length(cursor.getInt(cursor.getColumnIndex("play_length")));
//                        play.setPlay_introdution(cursor.getString(cursor.getColumnIndex("play_introduction")));
//                        play.setPlay_id(cursor.getInt(cursor.getColumnIndex("play_id")));
//                        Log.e("play_id", cursor.getInt(cursor.getColumnIndex("play_id")) + "");
//                        play.setPlay_lang(cursor.getString(cursor.getColumnIndex("play_lang")));
//                        play.setPlay_type(cursor.getString(cursor.getColumnIndex("play_type")));
//                    } while (cursor.moveToNext());
//                }
//                cursor = sqLiteDatabase.query("studio", null, " studio_id = ?", new String[]{String.valueOf(schedule.getStudio_id())}, null, null, null);
//                if (cursor.moveToFirst()) {
//
//                    do {
//                        studio = new Studio();
//                        studio.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
//                        studio.setStudio_name(cursor.getString(cursor.getColumnIndex("studio_name")));
//                        studio.setStudio_col_count(cursor.getInt(cursor.getColumnIndex("studio_col_count")));
//                        studio.setStudio_row_count(cursor.getInt(cursor.getColumnIndex("studio_row_count")));
//                        studio.setStudio_introduction(cursor.getString(cursor.getColumnIndex("studio_introduction")));
//                        studio.setStudio_flag(cursor.getInt(cursor.getColumnIndex("studio_flag")));
//                    } while (cursor.moveToNext());
//
//                }
//
//                cursor = sqLiteDatabase.query("ticket", null, " sched_id = ?", new String[]{String.valueOf(schedule.getSched_id())}, null, null, null);
//                if (cursor.moveToFirst()) {
//                    tickets.clear();
//                    do {
//                        Ticket ticket = new Ticket();
//                        ticket.setTicket_id(cursor.getInt(cursor.getColumnIndex("ticket_id")));
//                        ticket.setSched_id(cursor.getInt(cursor.getColumnIndex("sched_id")));
//                        ticket.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
//                        ticket.setTicket_price(cursor.getDouble(cursor.getColumnIndex("ticket_price")));
//                        if(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))!=null)
//                        {
//                            try {
//                                ticket.setTicket_locked_time(format.parse(cursor.getString(cursor.getColumnIndex("ticket_locked_time"))));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        ticket.setTicket_status(cursor.getInt(cursor.getColumnIndex("ticket_status")));
//                        tickets.add(ticket);
//
//                    } while (cursor.moveToNext());
//                }
//                cursor = sqLiteDatabase.query("seat", null, " studio_id = ?", new String[]{String.valueOf(studio.getStudio_id())}, null, null, null);
//                if (cursor.moveToFirst()) {
//                        seats.clear();
//                    do {
//                        Seat seat = new Seat();
//                       seat.setSeat_id(cursor.getInt(cursor.getColumnIndex("seat_id")));
//                       seat.setSeat_status(cursor.getInt(cursor.getColumnIndex("seat_status")));
//                       seat.setSeat_column(cursor.getInt(cursor.getColumnIndex("seat_column")));
//                       seat.setSeat_row(cursor.getInt(cursor.getColumnIndex("seat_row")));
//                       seat.setStudio_id(cursor.getInt(cursor.getColumnIndex("studio_id")));
//                       seats.add(seat);
//
//                    } while (cursor.moveToNext());
//                }
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        }).start();


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        unlockTickets();
    }


}
