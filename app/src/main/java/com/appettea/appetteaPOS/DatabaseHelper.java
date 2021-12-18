package com.appettea.appetteaPOS;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME="register.db";

        public static final String TRANSACTIONS = "transactions";
        public static final String ITEMS="items";
        public static  final String CHECKOUTS = "checkouts";
        public static final String ACCOUNT = "account";

        public static final String COL_1="description";
        public static final String COL_2="state";
        public static final String COL_3="units";
        public static final String COL_4 = "instock";
        public static final String COL_5 = "sold";
        public static final String COL_6="category";
        public static final String COL_7="price";
        public static final String COL_8="image";
        public static final String COL_9="date";

        public static  final  String TCOL_1 = "code";
        public static  final  String TCOL_2 = "name";
        public static  final  String TCOL_3 = "phone";
        public static  final  String TCOL_4 = "date";
        public static  final  String TCOL_5 = "time";
        public static  final  String TCOL_6 = "amount";

        public static  final  String CCOL_1 = "items";
        public static  final  String CCOL_2=  "date";
        public static  final  String CCOL_3=  "time";
        public static  final  String CCOL_4=  "cash";
        public static  final  String CCOL_5 = "mode";

        public static  final  String ACOL_1 = "bs_name";
        public static  final  String ACOL_2=  "password";
        public static  final  String ACOL_3=  "phone";
        public static  final  String ACOL_4=  "paybill";



        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + ITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,description TEXT,state TEXT,units TEXT,instock TEXT,sold TEXT, category TEXT,price TEXT,image BLOB not null,date TEXT)");

            db.execSQL("CREATE TABLE " + TRANSACTIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,code TEXT,name TEXT,phone TEXT, date TEXT,time TEXT,amount TEXT)");

            db.execSQL("CREATE TABLE " + CHECKOUTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,items TEXT,date TEXT,time TEXT, cash TEXT,mode TEXT)");

            db.execSQL("CREATE TABLE " + ACCOUNT + " (bs_name TEXT,password TEXT,phone TEXT, paybill TEXT)");

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " +ITEMS);

            db.execSQL("DROP TABLE IF EXISTS " +TRANSACTIONS);

            db.execSQL("DROP TABLE IF EXISTS " +CHECKOUTS);

            db.execSQL("DROP TABLE IF EXISTS " +ACCOUNT);

            onCreate(db);
        }
    }

