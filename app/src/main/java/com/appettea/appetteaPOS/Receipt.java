package com.appettea.appetteaPOS;

import android.content.Context;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

public class Receipt {

    private HashMap<String,Integer> monitor;

    private HashMap<String, Vector<Object>> storeItemInfo;

    private Context context;

    private int amt = 0;


    public Receipt(Context context, HashMap<String, Integer> monitor,

                   HashMap<String,Vector<Object>> storeItemInfo ){


        this.context = context;
        this.monitor = monitor;
        this.storeItemInfo = storeItemInfo;


    }
    public String getDate(){
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        // String _currentDate = new SimpleDateFormat("MMMMM dd, yyyy", Locale.getDefault()).format(new Date());

        DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = format1.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
        String dateString = format2.format(date);

        return  dateString;

    }

    private String getTime(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());


        return  strDate;
    }

    public String getReceipt(){


        String Header =

                "******Resturant Management*******;\n"
                + "Date:     Time:"+getTime()+"\n;"
                + "         Original Receipt        \n;"
                + "---------------------------------\n;"
                + "Product Description              \n;"
                + "Name             Qty       Amount\n;"

                + "---------------------------------\n;"
                +getItemsName()+"\n;"
                +"\n;---------------------------------\n;"
                + "Total Amount:      Ksh."+amt+"\n;"
                + "---------------------------------\n;"
                + "---------------------------------\n;"
                + "For Further Detail                \n;"
                + "Please Call/Whatsapp 0796770664\n;"
                + "---------------------------------\n;"
                + "       Software Developed by      \n ;"
                + "             Appettea  ;          \n;"
                + "*********************************\n;"
                + "            Thank You             \n;"
                + "_________________________________\n;";

        String Header2 =


                        "Date:     Time:"+getTime()+"\n;"
                        + "         Receipt        \n;"
                        + "---------------------------------\n;"
                        + "Name             Qty       Amount\n;"
                        + "---------------------------------\n;"
                        +getItemsName()+"\n;"
                        +"\n;---------------------------------\n;"
                        + "Total Amount:      Ksh."+amt+"\n;"
                        + "For Further Detail                \n;"
                        + "Please Call/Whatsapp 0796770664\n;"
                        + "---------------------------------\n;"
                        + "       Software Developed by      \n ;"
                        + "             Appettea  ;          \n;"
                        + "_________________________________\n;";


        String Header3 ="Thanks for purchasing  from us welcome order "+getTime()+ " item names "+getItemsName() + "total amount  "+ amt;


        return Header3;

    }

    private String getItemsName(){

        String items = "";

        int i = 0;
        /*
        for (String n:monitor.keySet()
             ) {

            int amount = monitor.get(n);

            Vector<String> b = storeItemInfo.get(n);

            int t_amount = (int)Integer.parseInt(b.get(5))*amount;

            String h = b.get(1)+"      "+amount+"      "+t_amount;

            amt+=t_amount;

            if (i<monitor.size()) h+='\n';


            items=items+h;
            i++;



        }

         */

        return items;

    }




}
