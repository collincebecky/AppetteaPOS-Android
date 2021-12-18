package com.appettea.appetteaPOS;

import android.util.Log;

import java.util.HashMap;

public class MessageParser {

    private String person_name="";

    private String date = "";

    private String time = "";

    private String ksh = "";

    private String phone = "";

    private String code = "";

    public MessageParser(String message){

       // HashMap<String,String> map = new HashMap<>();

        String msgList = message.replace(".","");


        String[] items = msgList.split(" ");



        int i = 0;
        for (String item : items) {
            Log.e("TAG >", items[i]);

            if(i == 3) date = items[i];

            if(i == 5) time = items[i]+items[i+1];

            if(i == 7) ksh = items[i];





            if (i>10 && isNumeric(items[i])){phone  = items[i]; break;}

            if (i>9 && !isNumeric(items[i])) person_name += items[i]+" ";

            i++;


        }

        code = items[0];


        //map.put("code",code); map.put("date",date); map.put("time",time); map.put("ksh",ksh); map.put("phone",phone);map.put("name",person_name);


    }

    public String getName() {

        return person_name;
    }

    public String getDate() {

        return date;
    }

    public String getPhone() {

        return phone;
    }

    public String getTime() {

        return time;
    }
    public String getKsh(){



        return addChar(ksh, '.', ksh.length()-2);
    }
    public String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }
    public  String getCode(){
        return code;
    }

    public static boolean isNumeric(String strNum) {
        boolean ret = true;
        try {

            Double.parseDouble(strNum);

        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

}
