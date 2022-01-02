
package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.HashMap;
import java.util.Vector;


public class TicketDialogue extends AppCompatDialogFragment  {


    private SQLiteDatabase db;

    private SQLiteOpenHelper openHelper;

    private CheckBox rememberMe;

    private String _phone;

    private String _empid ;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private Receipt receipt;




    //private static final String URL="https://prime-desk.brancetech.com/api/";
    private static final String URL="https://api.brancetech.com/qs/";

    private Context context;

    private Cursor cursor;

    private String phone;

    private HashMap<String,Integer> monitor;

    private HashMap<String,Vector<Object>> storeItemInfo;


    public TicketDialogue(Context applicationContext, String phone, HashMap<String,Integer> monitor,
                          HashMap<String, Vector<Object>> storeItemInfo) {

        this.context=applicationContext;

        this.monitor=monitor;

        this.storeItemInfo=storeItemInfo;

        this.phone = phone;

       // receipt = new Receipt(getContext(),monitor,storeItemInfo);



        // this.db=db;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_ticket,null);

        builder.setView(view);
        //        .setTitle("[ - TICKET - ]");

        final AlertDialog dialog = builder.create();

        ImageView mImgCheck =  view.findViewById(R.id.imageView);

        ((Animatable) mImgCheck.getDrawable()).start();



        view.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();

            }
        });

        view.findViewById(R.id.ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Toast.makeText(getContext(), "No printer attached yet", Toast.LENGTH_SHORT).show();
               // sendSMS(phone,getMessage());


                //->receipt dialog

                showReceipt();



            }
        });


        return dialog;
    }

    private void showReceipt(){

        Receipt receipt = new Receipt(context,monitor,storeItemInfo);

        receipt.show(getFragmentManager(),"");


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
    public void sendSMS(String number,String message){


        Log.e("NUM ",number);
        Log.e("NUM ",message);

        SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage(number,null, message, null, null);
    }


    private String getMessage(){

        return  receipt.getReceipt();

    }


    /**
     * Detects left and right swipes across a view.
     */



}

