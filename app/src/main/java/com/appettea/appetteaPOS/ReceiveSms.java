package com.appettea.appetteaPOS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

//import org.greenrobot.eventbus.Subscri

import androidx.annotation.NonNull;


import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class ReceiveSms extends BroadcastReceiver {

    private ReceiveSms.GetSms listener;
    /**
     * Detects left and right swipes across a view.
     */
    /*
    public ReceiveSms(Context context){

        try {

            listener=(ReceiveSms.GetSms)  context;



        }
        catch (ClassCastException e){
            throw  new ClassCastException(context.toString());

        }


    }
/*
    public ReceiveSms(){


        //get
    }

 */

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "SMS RECEIVED", Toast.LENGTH_SHORT).show();
      //  listener=(ReceiveSms.GetSms)  context;

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){

            Bundle bundle = intent.getExtras();
            SmsMessage [] msgs;
            String msg_from;
            if(bundle != null){
                try {
                    Object[] pdus  = (Object[]) bundle.get("pdus");

                    msgs = new SmsMessage[pdus.length];

                    for (int i = 0; i< msgs.length;i++){

                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                        msg_from = msgs[i].getOriginatingAddress();

                        String msgBody = msgs[i].getMessageBody();

                        if(isCorrect(msg_from,msgBody)){

                            HashMap<String,String> map = parseMessage(msgBody);

                            String build = "Code : "+ map.get("code")+'\n'+"phone : "+map.get("phone")+'\n'+"Received :"+map.get("ksh")+'\n'+"Name : "+ map.get("name")+"\n"+"Date :" + map.get("date")+"\n"+"Time  :"+map.get("time");
                            //map.put("code",code); map.put("date",date); map.put("time",time); map.put("ksh",ksh); map.put("phone",phone);map.put("name",person_name);

                            Toast.makeText(context, "From : "+msg_from+" \n"+build, Toast.LENGTH_SHORT).show();

                            //listener.passSms(map);

                            EventBus.getDefault().post(new OnReceiverEvent(new MessageParser(msgBody)));

                        }


                    }
                }
                catch (Exception e){

                    e.printStackTrace();
                }
            }

        }

    }

    private HashMap<String,String> parseMessage(String message){

        HashMap<String,String> map = new HashMap<>();

        String msgList = message.replace(".","");


        String[] items = msgList.split(" ");

        String person_name="";

        String date = "";

        String time = "";

        String ksh = "";

        String phone = "";

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

        String code = items[0];


        map.put("code",code); map.put("date",date); map.put("time",time); map.put("ksh",ksh); map.put("phone",phone);map.put("name",person_name);

        return  map;
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


    public  interface GetSms{

        public  void passSms(HashMap<String,String> map);

    }

    public void  setListener(Context context){

        this.listener=(GetSms) context;

    }



    private boolean isCorrect(String msgFrom,String message){

        if (message.contains("Utility") && msgFrom.equals("MPESA")) return true;

        return false;


    }
}
