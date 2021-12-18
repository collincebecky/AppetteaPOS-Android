
package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;


public class CheckOutDialogue extends AppCompatDialogFragment implements FirstFragment.GetSms {



    private DialogListener listener;

    private SQLiteDatabase db;

    private SQLiteOpenHelper openHelper;

    private CheckBox rememberMe;

    private String _phone;

    private String _empid ;

    private TextView name;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private Context context;

    private Cursor cursor;

    private HashMap<String,Integer> monitor;

    private HashMap<String,Vector<Object>> storeItemInfo;

    private TextView cost;

    private TextView phone;

    public CheckOutDialogue (Context applicationContext, HashMap<String, Integer> monitor, HashMap<String,Vector<Object>> storeItemInfo,SQLiteDatabase db) {

        context=applicationContext;
        this.monitor = monitor;
        this.storeItemInfo = storeItemInfo;

         this.db=db;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.checkout_dialogue,null);


        //Button ticket = view.findViewById(R.id.ticket);

        //rememberMe = view.findViewById(R.id.remember);



        builder.setView(view)
                .setTitle("[ CHECKOUT ]");


        final AlertDialog dialog = builder.create();


        phone  = view.findViewById(R.id.phone);

        name = view.findViewById(R.id.name);

        cost  = view.findViewById(R.id.total_cost);

        Button complete = view.findViewById(R.id.complete);


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reduce stock if tracked on each
                    //->update database
                int items = 0;

                if(!monitor.isEmpty()) {

                    for (String key : monitor.keySet()
                    ) {

                        String state = (String) storeItemInfo.get(key).elementAt(2);

                        String instock = (String) storeItemInfo.get(key).elementAt(4);

                        String _sold = (String) storeItemInfo.get(key).elementAt(5);


                        //items are all things bundled

                        items += monitor.get(key);

                        //--------------------------------------//
                        //                                      //
                        //--------------------------------------//

                        if (state.equals("1") ) {

                            if(Integer.parseInt(instock) - monitor.get(key)>0){
                                //-> Reduce the number in the db


                                int n_units = Integer.parseInt(instock) - monitor.get(key);

                                int sold = Integer.parseInt(_sold) + monitor.get(key);

                                updateData(key,String.valueOf(n_units),String.valueOf(sold));

                            }

                        }

                        else{

                            ///-> cant number of sold if not tracking
                            int sold = Integer.parseInt(_sold) + monitor.get(key);
                            updateDataSold(key,String.valueOf(sold));

                        }


                    }

                    // TODO record checkout #DONE

                    String mode = name.getText().toString().isEmpty()? "CASH" : "MPESA" ;

                    recordCheckout(String.valueOf(items),getDate(),getTime(),String.valueOf(calculateCost()),mode);


                   // listener.passComplete(true);



                }


                dismiss();


                // show pay success

                showPaySuccess();



            }
        });

        cost.setText("Ksh "+ calculateCost() +".00");



//        int height = (int)(getResources().getDisplayMetrics().heightPixels*1.0);


        //dialog.getWindow().setLayout(width, height);




        TextView paybill = view.findViewById(R.id.paybill);


        String data[] = getPaybillNoAndName();


        String n = "PAYBILL : "+data[1]+"\n ACCOUNT NO: "+data[0];

        paybill.setText(n);

        this.setCancelable(true);

        //ID= view.findViewById(R.id.identify);

        return dialog;
    }

    private void showPaySuccess(){

        TicketDialogue url_dialog= new TicketDialogue(getContext(),phone.getText().toString(),monitor,storeItemInfo);

        url_dialog.show(getFragmentManager(),"url dialog");



    }

    private String[] getPaybillNoAndName(){

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ACCOUNT, null);

        String[] paybill=null;

        if (c.moveToFirst()) {

            do {

                paybill =  new String[]{c.getString(0),c.getString(3)} ;

                break;

            } while (c.moveToNext());

        }

        return paybill;

    }

    public String getTime() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());


        return strDate;

    }

    public String getDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = sdf.format(c.getTime());


        return strDate;

    }

    public boolean recordCheckout(String items,String date,String time,String cash,String mode){

        //->pass Data to Database ------------------------>
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.CCOL_1,items);
        contentValues.put(DatabaseHelper.CCOL_2,date);
        contentValues.put(DatabaseHelper.CCOL_3,time);
        contentValues.put(DatabaseHelper.CCOL_4,cash);
        contentValues.put(DatabaseHelper.CCOL_5,mode);

        long id = db.insert(DatabaseHelper.CHECKOUTS,null,contentValues);
        if(id ==-1)
            return false;
        else
            return true;

    }



    public boolean recordTransaction(String code ,String name ,String phone, String date,String time,String amount){


        //->pass Data to Database ------------------------>
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.TCOL_1,code);
        contentValues.put(DatabaseHelper.TCOL_2,name);
        contentValues.put(DatabaseHelper.TCOL_3,phone);
        contentValues.put(DatabaseHelper.TCOL_4,date);
        contentValues.put(DatabaseHelper.TCOL_5,time);
        contentValues.put(DatabaseHelper.TCOL_6,amount);



        long id = db.insert(DatabaseHelper.TRANSACTIONS,null,contentValues);
        if(id ==-1)
            return false;
        else
            return true;

    }
    public void updateData(String id,
                           String instock,
                           String sold
    )
    {

        //->pass Data to Database ------------------------>
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_4,instock);
        contentValues.put(DatabaseHelper.COL_5,sold);

        db.update(DatabaseHelper.ITEMS,contentValues,"ID= ?",new String[]{id});


    }
    private void updateDataSold(
            String id,
            String sold
    ){

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COL_5,sold);

        db.update(DatabaseHelper.ITEMS,contentValues,"ID= ?",new String[]{id});

    }


    private Integer calculateCost(){

        int total = 0;

        for (String id:monitor.keySet()
             ) {

            int val = monitor.get(id);

            String price = (String) storeItemInfo.get(id).elementAt(7);

            int tprice = Integer.parseInt(price)*val;

            total+=tprice;


        }

        return  total;

    }

    public void setPhone(String phone_) {

        Toast.makeText(getContext(), "Payment received successfully !!", Toast.LENGTH_SHORT).show();

        phone.setText(phone_);

    }


    public  void setMessagePerser(MessageParser messageParser){

        //-> add contact to text edit
        //-> match cash with payment
        //-> payment success

        Toast.makeText(getContext(), "Payment received successfully !!", Toast.LENGTH_SHORT).show();

        phone.setText("     "+messageParser.getPhone());

        phone.setTextColor(Color.GREEN);

        name.setText(messageParser.getName());

        name.setTextColor(Color.GREEN);

        Log.e("TAG --> Parse",messageParser.getKsh());

        Log.e("TAG --> log",cost.getText().toString());

        String costText = cost.getText().toString().trim();

        if(messageParser.getKsh().equals(costText.replace(" ",""))){

            cost.setTextColor(Color.GREEN);

            name.setText("     "+messageParser.getName());

            name.setTextColor(Color.GREEN);

            recordTransaction(messageParser.getCode() , messageParser.getName(),messageParser.getPhone(),messageParser.getDate(),messageParser.getTime(),messageParser.getKsh());
        }



    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            listener= (DialogListener) context;

        }
        catch (ClassCastException e){

            Log.e("TAG","COULDN'T MATCH CONTEXT");


        }

    }

    @Override
    public void passSms(MessageParser messageParser) {

        //-> tell

        //-> add contact to text edit
        //-> match cash with payment
        //-> payment success

        Toast.makeText(getContext(), "Payment received successfully !!", Toast.LENGTH_SHORT).show();

        phone.setText(messageParser.getPhone());

        if(messageParser.getKsh().equals(cost.getText().toString().trim())){

            cost.setTextColor(Color.GREEN);


            //Toast.


        }

        //recordTransaction(map.get("code") , map.get(" name") ,map.get("phone"),map.get("date"),map.get("time"),map.get("ksh"));

        //String build = "Code : "+ map.get("code")+'\n'+"phone : "+map.get("phone")+'\n'+"Received :"+map.get("ksh")+'\n'+"Name : "+ map.get("name")+"\n"+"Date :" + map.get("date")+"\n"+"Time  :"+map.get("time");

    }


    public  interface DialogListener{

        void passComplete(boolean state);

    }



    /**
     * Detects left and right swipes across a view.
     */



}

