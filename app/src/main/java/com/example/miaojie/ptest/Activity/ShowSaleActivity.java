package com.example.miaojie.ptest.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miaojie.ptest.Adapter.SaleAdapter;
import com.example.miaojie.ptest.R;
import com.example.miaojie.ptest.Utils.LoadingDialog;
import com.example.miaojie.ptest.Utils.MyApplication;
import com.example.miaojie.ptest.pojo.Grob_var;
import com.example.miaojie.ptest.pojo.Sale;
import com.example.miaojie.ptest.pojo.SaleWeb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowSaleActivity extends AppCompatActivity {
    private List<Sale> sale_list = new ArrayList<>();
    private List<Sale> searchlist = new ArrayList<>();
    ListView listView;
    private boolean isSearch = false;
    Toolbar sale_toolbar;
    SearchView sale_searchview;
    RequestQueue requestQueue;
    LoadingDialog dialog;
    SaleAdapter saleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sale);
        initdata();
    }

    public void initdata()
    {
        dialog = new LoadingDialog(ShowSaleActivity.this,"������...");
        dialog.show();
        requestQueue = Volley.newRequestQueue(this);
        //����һ������
//        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        String url1 = "http://192.168.0.32:8080/mobilePlay/getPlay";

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileSale/getSaleByEmpId?empId="+MainActivity.employee.getEmp_id(), new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {
//                List<User> userList = gson.fromJson(string, new TypeToken<List<User>>() {}.getType());
//                List<User> userList = gson.fromJson(jsonArray, new TypeToken<List<User>>() {}.getType());
                try {
                    Gson gson = new Gson();
                    List<SaleWeb> saleWebs = new ArrayList<>();
                    saleWebs = gson.fromJson(s, new TypeToken<List<SaleWeb>>() {}.getType());
                    for(int i=0;i<saleWebs.size();i++)
                    {
                        Sale sale = new Sale();
                        sale.setEmp_id(saleWebs.get(i).getEmpId());
                        sale.setSale_change(saleWebs.get(i).getSaleChange());
                        sale.setSale_payment(saleWebs.get(i).getSalePayment());
                        sale.setSale_type(saleWebs.get(i).getSaleType());
                        sale.setSale_status(saleWebs.get(i).getSaleStatus());
                        sale.setSale_time(new Date(saleWebs.get(i).getSaleTime()));
                        sale.setSale_id(saleWebs.get(i).getSaleId());
                        sale_list.add(sale);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.close();
                init();

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ShowSaleActivity.this,"�����쳣",Toast.LENGTH_SHORT).show();
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }

    public void init()
    {
        listView = findViewById(R.id.sale_list);
        saleAdapter = new SaleAdapter(this,R.layout.sale_item,sale_list);
        listView.setAdapter(saleAdapter);
        sale_searchview = findViewById(R.id.saleSearchView);
        sale_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchlist.clear();
//                private int sale_type;// 1�����۵�\r\n            -1���˿
//                private int sale_status;//0������ 1�Ѹ���
                SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                for(int i=0;i<sale_list.size();i++){
                    StringBuffer type = new StringBuffer();
                    if(sale_list.get(i).getSale_type()==1)
                    {
                        type.append("���۵�");
                    }else{
                        type.append("�˿");
                    }
                    if(sale_list.get(i).getSale_status()==0)
                    {
                        type.append("������");
                    }else{
                        type.append("�Ѹ���");
                    }
                    type.append(format0.format(sale_list.get(i).getSale_time()));

                    if(type.toString().contains(newText)){
                        searchlist.add(sale_list.get(i));
                    }
                }
                if(searchlist.size()>0&&newText.length()>0){
                    saleAdapter = new SaleAdapter(MyApplication.getContext(),R.layout.sale_item,searchlist);
                    listView.setAdapter(saleAdapter);
                    isSearch=true;

                }
                if(newText.length()==0)
                {
                    saleAdapter = new SaleAdapter(MyApplication.getContext(),R.layout.sale_item,sale_list);
                    listView.setAdapter(saleAdapter);
                    isSearch=false;
                }
                return false;
            }
        });
        sale_toolbar = findViewById(R.id.sale_toolbar);
        sale_toolbar.setTitle("");
        setSupportActionBar(sale_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sale_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Sale sale = sale_list.get(i);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ShowSaleActivity.this);
                dialog.setTitle("����");
                dialog.setMessage("ȷ����Ʊ����");
                dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                            ��Ʊ�߼�refundTcket
                        get(sale);
                    }
                });
                dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "ȡ��", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ShowSaleActivity.this,"��ϸ�˵�",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void get(Sale sale){
        //����һ���������
        requestQueue = Volley.newRequestQueue(this);
        //����һ������

        StringRequest stringRequest =new StringRequest(Grob_var.host+"mobileTicket/refundTcket?SaleId="+sale.getSale_id(), new Response.Listener<String>() {
            //��ȷ�������ݻص�
            @Override
            public void onResponse(String s) {
                if(s.equals("succeed"))
                {
                    sale_list.clear();
                    initdata();
                    Toast.makeText(ShowSaleActivity.this,"��Ʊ�ɹ�",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ShowSaleActivity.this,"��Ʊʱ���ѹ�������",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {//�쳣��ļ�������
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ShowSaleActivity.this,"��Ʊʧ��",Toast.LENGTH_SHORT).show();
            }
        });
        //��get������ӵ�������
        requestQueue.add(stringRequest);
    }
}
