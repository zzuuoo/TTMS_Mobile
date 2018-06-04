package com.example.miaojie.ptest.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static class SingltonHolder{
        private static final MyDatabaseHelper MY_DATABASE_HELPER = new MyDatabaseHelper(MyApplication.getContext(),"ttms.db",null,1);
    }

    public static final MyDatabaseHelper getInstance(){
        return SingltonHolder.MY_DATABASE_HELPER;
    }



    public static final String CREATE_PLAY = "CREATE TABLE `play` (\n" +
            "  `play_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `play_type` varchar(11) DEFAULT NULL,\n" +
            "  `play_lang` varchar(11) DEFAULT NULL,\n" +
            "  `play_name` varchar(200) DEFAULT NULL,\n" +
            "  `play_introduction` varchar(500) DEFAULT NULL,\n" +
            "  `play_image` varchar(1000) DEFAULT NULL,\n" +
            "  `play_length` int(11) DEFAULT NULL,\n" +
            "  `play_ticket_price` decimal(10,2) DEFAULT NULL,\n" +
            "  `play_status` smallint(6) DEFAULT NULL \n" +
            ") ";

    public static final String CREATE_STUDIO= "CREATE TABLE `studio` (\n" +
            "  `studio_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `studio_name` varchar(100) NOT NULL,\n" +
            "  `studio_row_count` int(11) DEFAULT NULL,\n" +
            "  `studio_col_count` int(11) DEFAULT NULL,\n" +
            "  `studio_introduction` varchar(2000) DEFAULT NULL,\n" +
            "  `studio_flag` smallint(6) DEFAULT NULL \n" +
            ") ";

    public static final String CREATE_SCHEDULE = "CREATE TABLE `schedule` (\n" +
            "  `sched_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `studio_id` INTEGER DEFAULT NULL,\n" +
            "  `play_id` INTEGER DEFAULT NULL,\n" +
            "  `sched_time` datetime NOT NULL,\n" +
            "  `sched_ticket_price` decimal(10,2) DEFAULT NULL,\n" +
            "  CONSTRAINT `FK_play_sched` FOREIGN KEY (`play_id`) REFERENCES `play` (`play_id`),\n" +
            "  CONSTRAINT `FK_studio_sched` FOREIGN KEY (`studio_id`) REFERENCES `studio` (`studio_id`)\n" +
            ")";

    public static final String CREATE_SEAT= "CREATE TABLE `seat` (\n" +
            "  `seat_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `studio_id` INTEGER DEFAULT NULL,\n" +
            "  `seat_row` int(11) DEFAULT NULL,\n" +
            "  `seat_column` int(11) DEFAULT NULL,\n" +
            "  `seat_status` smallint(6) NOT NULL DEFAULT '1',\n" +
            "  CONSTRAINT `FK_studio_seat` FOREIGN KEY (`studio_id`) REFERENCES `studio` (`studio_id`)\n" +
            ") ";

    public static final String CREATE_TICKET = "CREATE TABLE `ticket` (\n" +
            "  `ticket_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `seat_id` INTEGER DEFAULT NULL,\n" +
            "  `sched_id` INTEGER DEFAULT NULL,\n" +
            "  `ticket_price` decimal(10,2) DEFAULT NULL,\n" +
            "  `ticket_status` smallint(6) DEFAULT NULL ,\n" +
            "  `ticket_locked_time` datetime DEFAULT NULL,\n" +
            "  CONSTRAINT `FK_sched_ticket` FOREIGN KEY (`sched_id`) REFERENCES `schedule` (`sched_id`),\n" +
            "  CONSTRAINT `FK_seat_ticket` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`)\n" +
            ")";

    public static final String CREATE_USER = "CREATE TABLE `user` (\n" +
            "  `emp_no` varchar(20) NOT NULL ,\n" +
            "  `emp_pass` varchar(20) NOT NULL DEFAULT '123456' ,\n" +
            "  `type` tinyint(4) NOT NULL DEFAULT '0' ,\n" +
            "  `head_path` varchar(500) DEFAULT NULL ,\n" +
            "  PRIMARY KEY (`emp_no`),\n" +
            "  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`emp_no`) REFERENCES `employee` (`emp_no`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ";

    public static final String CREATE_EMPLOYEE = "CREATE TABLE `employee` (\n" +
            "  `emp_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `emp_no` varchar(20) NOT NULL UNIQUE,\n" +
            "  `emp_name` varchar(100) NOT NULL,\n" +
            "  `emp_tel_num` varchar(20) DEFAULT NULL,\n" +
            "  `emp_addr` varchar(200) DEFAULT NULL,\n" +
            "  `emp_email` varchar(100) DEFAULT NULL\n" +
            ") ";

    public static final String CREATE_SALE_ITEM = "CREATE TABLE `sale_item` (\n" +
            "  `sale_item_id` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `ticket_id` INTEGER DEFAULT NULL,\n" +
            "  `sale_ID` INTEGER DEFAULT NULL,\n" +
            "  `sale_item_price` decimal(10,2) DEFAULT NULL,\n" +
            "  CONSTRAINT `FK_sale_sale_item` FOREIGN KEY (`sale_ID`) REFERENCES `sale` (`sale_ID`),\n" +
            "  CONSTRAINT `FK_ticket_sale_item` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`ticket_id`)\n" +
            ") ";

    public static final String CREATE_SALE = "CREATE TABLE `sale` (\n" +
            "  `sale_ID` INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
            "  `emp_id` INTEGER DEFAULT NULL,\n" +
            "  `sale_time` datetime DEFAULT NULL,\n" +
            "  `sale_payment` decimal(10,2) DEFAULT NULL,\n" +
            "  `sale_change` decimal(10,2) DEFAULT NULL,\n" +
            "  `sale_type` smallint(6) DEFAULT NULL,\n" +
            "  `sale_status` smallint(6) DEFAULT NULL ,\n" +
            "  CONSTRAINT `FK_employee_sale` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`emp_id`)\n" +
            ")";


    private Context context;

    private MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PLAY);
        sqLiteDatabase.execSQL(CREATE_EMPLOYEE);
        sqLiteDatabase.execSQL(CREATE_SCHEDULE);
        sqLiteDatabase.execSQL(CREATE_STUDIO);
        sqLiteDatabase.execSQL(CREATE_SEAT);
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_TICKET);
        sqLiteDatabase.execSQL(CREATE_SALE);
        sqLiteDatabase.execSQL(CREATE_SALE_ITEM);
        sqLiteDatabase.execSQL("INSERT INTO "
                + "employee"
                + " (emp_no, emp_name)"
                + " VALUES ('root', 'root');");
        sqLiteDatabase.execSQL("INSERT INTO "
                + "user"
                + " (emp_no, emp_pass, type, head_path)"
                + " VALUES ('root', 'tooy', 1,'root');");
//        Toast.makeText(context,"创建数据库成功",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists play");
        sqLiteDatabase.execSQL("drop table if exists user");
        sqLiteDatabase.execSQL("drop table if exists employee");
        sqLiteDatabase.execSQL("drop table if exists studio");
        sqLiteDatabase.execSQL("drop table if exists schedule");
        sqLiteDatabase.execSQL("drop table if exists seat");
        sqLiteDatabase.execSQL("drop table if exists ticket");
        sqLiteDatabase.execSQL("drop table if exists sale");
        sqLiteDatabase.execSQL("drop table if exists sale_item");
        sqLiteDatabase.execSQL("drop table if exists data_dict");
        onCreate(sqLiteDatabase);
    }
}
