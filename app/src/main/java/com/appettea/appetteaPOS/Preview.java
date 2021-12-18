
package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;


public class Preview extends AppCompatDialogFragment  {





    private SQLiteDatabase db;



    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private Context context;


    private HashMap<String,Integer> monitor;

    private HashMap<String,Integer> prepreview = new HashMap<>();

    private HashMap<String,Vector<Object>> storeItemInfo;

    private CheckOutDialogue checkOutDialogue;
    
    private Items item;


    private LinearLayout previewContainer;

    private LinearLayout cart;

    private Vector<LinearLayout> linearLayouts;

    private HashMap<String, Pair<TextView,TextView>> mapItemDiscript;

    private TextView totalCost;

    public Preview (Context applicationContext, HashMap<String, Integer> monitor, HashMap<String,Vector<Object>> storeItemInfo,SQLiteDatabase db) {

        context=applicationContext;
        this.monitor = monitor;
        this.storeItemInfo = storeItemInfo;

        this.prepreview = (HashMap<String, Integer>) monitor.clone();

        this.db=db;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.preview_dialogue,null);

        builder.setView(view)
                .setTitle("[ PREVIEW ]");



        mapItemDiscript = new HashMap<>();
        previewContainer = view.findViewById(R.id.prevCont);
        cart = view.findViewById(R.id.cart);


        Button checkout = view.findViewById(R.id.checkout);
        Button cancel    = view.findViewById(R.id.cancel);

        totalCost = view.findViewById(R.id.total_cost);

        item = new Items(getContext(),getFragmentManager(),prepreview,mapItemDiscript,storeItemInfo,totalCost,db);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkOut();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();


            }
        });

        final AlertDialog dialog = builder.create();

        if(monitor.isEmpty()) dismiss();;

        createItems();


        return dialog;
    }
    private void checkOut(){

        FragmentManager fragmentManager = getFragmentManager();

        checkOutDialogue= new CheckOutDialogue(getContext(),prepreview,storeItemInfo,db);

        checkOutDialogue.show(fragmentManager,"url dialog");


        setShowsDialog(false);

        


        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                //do sth

                dismiss();
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
            }
        }, false);





       // dismiss();

        //reload views

        //createItems();

    }
    @Subscribe
    public void onPhoneNumberReceived(OnReceiverEvent event){

        MessageParser messageParser = event.getSender();

        //listener.passSms(messageParser);

        checkOutDialogue.setMessagePerser(messageParser);

        dismiss();

    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);

    }
    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

    }
    
    private void createItems(){


        if(!monitor.isEmpty()) {

            int total = 0;

            for (String key : monitor.keySet()
            ) {


                String            id = (String) storeItemInfo.get(key).elementAt(0);
                String   description = (String) storeItemInfo.get(key).elementAt(1);
                String         price = (String) storeItemInfo.get(key).elementAt(7);
                Bitmap         image = (Bitmap) storeItemInfo.get(key).elementAt(8);


                //items are all things bundled

                LinearLayout _item = item.buildMenuItem(id,description,price,image,String.valueOf(monitor.get(key)));

                previewContainer.addView(_item);

                int val = prepreview.get(key);

                int tprice = Integer.parseInt(price)*val;

                total+=tprice;

                LinearLayout _cartItem = createDisplay(String.valueOf(key),String.valueOf(prepreview.get(key)),description,String.valueOf(tprice));

                //linearLayouts.add(_cartItem);

                cart.addView(_cartItem );

            }


            totalCost.setText("TOTAL : "+total);

        }

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


    private LinearLayout createDisplay(String tag,String amt, String _items,String price){

        LinearLayout linearLayout = new LinearLayout(getContext());

        linearLayout.setTag(tag);
        TextView item = new TextView(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,60,1.0f);

        item.setGravity(Gravity.CENTER);
        item.setLayoutParams(params);

        item.setText(amt+" "+_items);
        TextView cost = new TextView(context);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,60,1.0f);

        cost.setText(price);
        cost.setLayoutParams(params1);

        cost.setGravity(Gravity.CENTER);

        linearLayout.addView(item);
        linearLayout.addView(cost);

        mapItemDiscript.put(tag,new Pair<>(item,cost));

        return  linearLayout;


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    /**
     * Detects left and right swipes across a view.
     */



}

