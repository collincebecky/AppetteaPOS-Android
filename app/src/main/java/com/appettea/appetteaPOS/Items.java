package com.appettea.appetteaPOS;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

public class Items {

    private Context context;
    private FragmentManager fragmentManager;

    private SQLiteDatabase db;

    private LinearLayout store;

    private HashMap<String,Integer> monitor;

    private FirstFragment firstFragment;

    private HashMap<String, Pair<TextView,TextView>> mapItemDiscript;

    private HashMap<String,Vector<Object>> storeItemInfo;

    private TextView totalCost;

    private View view;




    boolean isDoubleClicked=false;

    Handler handler=new Handler();
    Runnable r=new Runnable(){
        @Override
        public void run(){
            //Actions when Single Clicked
            isDoubleClicked=false;
        }
    };
    public Items(Context context){

        this.context =context;


    };



    public Items(Context context, FragmentManager fragmentManager, @Nullable  LinearLayout store,@Nullable LinearLayout linearLayout, SQLiteDatabase db){
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.store = store;
        this.db = db;

    }

    public Items(Context context, FragmentManager fragmentManager, HashMap<String,Integer> monitor, @Nullable  LinearLayout store,@Nullable LinearLayout linearLayout, SQLiteDatabase db,@Nullable FirstFragment firstFragment){
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.store = store;
        this.db = db;
        this.monitor = monitor;
        this.firstFragment=firstFragment;

    }

    public Items(Context context, FragmentManager fragmentManager, HashMap<String,Integer> monitor,LinearLayout store, SQLiteDatabase db,@Nullable FirstFragment firstFragment){
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.store = store;
        this.db = db;
        this.monitor = monitor;
        this.firstFragment = firstFragment;
    }

    public Items(Context context, FragmentManager fragmentManager, HashMap<String, Integer> monitor, HashMap<String, Pair<TextView,TextView>> mapItemDiscript,HashMap<String,Vector<Object>> storeItemInfo,TextView totalCost, SQLiteDatabase db) {

        this.fragmentManager = fragmentManager;
        this.context = context;
        this.db = db;
        this.monitor =monitor;
        //this.linearLayouts = linearLayouts;

        this.storeItemInfo = storeItemInfo;


        this.totalCost = totalCost;


        this.mapItemDiscript=mapItemDiscript;
    }

    public LinearLayout buildMenuItem(String id, String _description,
                                      String _price, Bitmap image, @Nullable String qty ){

        LinearLayout parent = new LinearLayout(context);

        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setWeightSum(1.0f);

        parent.setPadding(5, 5, 5, 5);


        ImageView imageView = new ImageView(context);


        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                192 ,150);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M ){
            params = new LinearLayout.LayoutParams(
                    230, 150);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(false);
        imageView.setClickable(true);
        //imageView.setImageResource(R.drawable.food);

        imageView.setImageBitmap(image);



        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditItem editItem = new EditItem(context,null);
                editItem.show(fragmentManager,"editItem");


            }
        });

         */
        imageView.setOnClickListener(view -> {
            if(isDoubleClicked){
                //Actions when double Clicked

                //EditItem editItem = new EditItem(context,store,db);

                //editItem.setData(id,_description,state, units,category,_price,image,date);
                //editItem.show(fragmentManager,"editItem");



                isDoubleClicked=false;
                //remove callbacks for Handlers
                handler.removeCallbacks(r);
            }else{
                isDoubleClicked=true;
                handler.postDelayed(r,500);
            }
        });



        //imageView.setLayoutParams(params);

        parent.addView(imageView, params);

        LinearLayout child = new LinearLayout(context);

        child.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView description = new TextView(context);
        description.setLayoutParams(new LinearLayout.LayoutParams(110, LinearLayout.LayoutParams.MATCH_PARENT));
        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        description.setText(_description);
        //description.setTextColor(Color.GRAY);
        description.setTextColor(Color.BLACK);
        description.setBackgroundColor(Color.parseColor("#F0F0F0"));

        child.addView(description);


        TextView price = new TextView(context);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params1.weight = 1.0f;
        price.setLayoutParams(params1);
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setText(" @Ksh "+_price);
        price.setGravity(Gravity.CENTER);
        price.setTextColor(Color.parseColor("#000000"));
        price.setBackgroundColor(Color.GREEN);

        child.addView(price);

        parent.addView(child);

        LinearLayout child3 = new LinearLayout(context);
        LinearLayout.LayoutParams bparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        bparam.setMargins(1,1,1,1);
        Button button1 = new Button(context);
        button1.setLayoutParams(bparam);
        button1.setBackgroundColor(Color.WHITE);
        button1.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        button1.setTextColor(Color.BLUE);
        button1.setTextSize(23);


        String amt  = qty == null || qty == "null" ? "0": qty;

        button1.setText(amt);
        button1.setPadding(4, 4, 4, 4);


        Button button = new Button(context);
        bparam.weight = 1.0f;
        button.setLayoutParams(bparam);
        button.setBackgroundColor(Color.WHITE);
        button.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        button.setTextSize(23);
        button.setTextColor(Color.RED);
        button.setText("-");
        button.setPadding(4, 4, 4, 4);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = Integer.parseInt(button1.getText().toString());
                --i;
                if(i>=0) {
                    button1.setText(String.valueOf(i));

                    monitor.put(id, monitor.get(id) - 1);

                    if(mapItemDiscript != null){


                        mapItemDiscript.get(id).first.setText(i +" x "+_description);


                        int val = monitor.get(id);


                        int tprice = Integer.parseInt(_price)*val;

                        mapItemDiscript.get(id).second.setText(String.valueOf(tprice));

                        totalCost.setText("TOTAL : "+calculateCost());


                    }

                }

            }
        });

        child3.addView(button);
        child3.addView(button1);

        Button button2 = new Button(context);
        button2.setBackgroundColor(Color.WHITE);
        button2.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        button2.setTextSize(23);
        button2.setTextColor(Color.GREEN);
        button2.setLayoutParams(bparam);
        button2.setText("+");
        button2.setPadding(4, 4, 4, 4);

        button2.setOnClickListener(view -> {
            int i = Integer.parseInt(button1.getText().toString());
            ++i;
            button1.setText(String.valueOf(i));

            monitor.put(id,i);

            if (firstFragment!=null)
            firstFragment.previewAnimation();

            if(mapItemDiscript != null){


                mapItemDiscript.get(id).first.setText(i +" x "+_description);


                int val = monitor.get(id);



                int tprice = Integer.parseInt(_price)*val;

                mapItemDiscript.get(id).second.setText(String.valueOf(tprice));

                totalCost.setText("TOTAL : "+calculateCost());

                Log.e("TAG","MONITOR -----> "+monitor.toString());

            }



        });

        child3.addView(button2);


        parent.addView(child3);



        return parent;

    }

    public LinearLayout buildItem(String id,
                                  String _description,
                                  String state,
                                  String units,
                                  String _instock,
                                  String sold_,
                                  String category,
                                  String _price,
                                  Bitmap image,
                                  String date)
    {

        LinearLayout parent = new LinearLayout(context);

        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setWeightSum(1.0f);
        parent.setPadding(5, 5, 5, 5);


        ImageView imageView = new ImageView(context);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                192 ,150);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M ){
            params = new LinearLayout.LayoutParams(
                    230, 150);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(false);
        imageView.setClickable(true);
        //imageView.setImageResource(R.drawable.food);

        imageView.setImageBitmap(image);



        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditItem editItem = new EditItem(context,null);
                editItem.show(fragmentManager,"editItem");


            }
        });

         */
        imageView.setOnClickListener(view -> {
            if(isDoubleClicked){
                //Actions when double Clicked

                EditItem editItem = new EditItem(context,store,db);

                editItem.setData(id,_description,state, units,_instock,sold_,category,_price,image,date);
                editItem.show(fragmentManager,"editItem");


                isDoubleClicked=false;
                //remove callbacks for Handlers
                handler.removeCallbacks(r);
            }else{
                isDoubleClicked=true;
                handler.postDelayed(r,500);
            }
        });



        //imageView.setLayoutParams(params);

        parent.addView(imageView, params);

        LinearLayout child = new LinearLayout(context);

        child.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));





        TextView description = new TextView(context);
        description.setLayoutParams(new LinearLayout.LayoutParams(110, LinearLayout.LayoutParams.MATCH_PARENT));
        description.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        description.setText(_description);
        //description.setTextColor(Color.GRAY);
        description.setTextColor(Color.BLACK);
        description.setBackgroundColor(Color.parseColor("#F0F0F0"));

        child.addView(description);


        TextView price = new TextView(context);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params1.weight = 1.0f;
        price.setLayoutParams(params1);
        price.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        price.setText(" @Ksh "+_price);
        price.setGravity(Gravity.CENTER);
        price.setTextColor(Color.parseColor("#000000"));
        price.setBackgroundColor(Color.GREEN);

        child.addView(price);

        parent.addView(child);


        LinearLayout child2 = new LinearLayout(context);
        child2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));


        TextView recorded = new TextView(context);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params2.weight = 1.0f;
        recorded.setLayoutParams(params2);
        recorded.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        recorded.setText("recorded");
        recorded.setGravity(Gravity.CENTER);
        recorded.setTextColor(Color.parseColor("#FFFFFF"));

        child2.addView(recorded);


        TextView instock = new TextView(context);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params3.weight = 1.0f;
        instock.setLayoutParams(params3);
        instock.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        instock.setText("instock");
        instock.setGravity(Gravity.CENTER);
        instock.setTextColor(Color.parseColor("#FFFFFF"));

        child2.addView(instock);

        TextView sold = new TextView(context);
        sold.setLayoutParams(params3);
        sold.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sold.setText("sold");
        sold.setGravity(Gravity.CENTER);
        sold.setTextColor(Color.parseColor("#FFFFFF"));

        child2.addView(sold);

        parent.addView(child2);
        
        LinearLayout child3 = new LinearLayout(context);
        child2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 35));

        Button button = new Button(context);
        LinearLayout.LayoutParams bparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        bparam.setMargins(1,1,1,1);
        bparam.weight = 1.0f;
        button.setLayoutParams(bparam);
        button.setBackgroundColor(Color.WHITE);
        button.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        button.setTextSize(15);
        button.setTextColor(Color.BLUE);
        button.setText(units);
        button.setPadding(4, 4, 4, 4);

        child3.addView(button);

        Button button1 = new Button(context);
        button1.setLayoutParams(bparam);
        button1.setBackgroundColor(Color.WHITE);
        button1.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        button1.setTextColor(Color.BLUE);
        button1.setTextSize(15);

        button1.setText(_instock);
        button1.setPadding(4, 4, 4, 4);

        child3.addView(button1);

       // String sold_ = String.valueOf(Integer.valueOf(units) - Integer.parseInt(_instock));

        Button button2 = new Button(context);
        button2.setBackgroundColor(Color.WHITE);
        button2.setBackground(context.getResources().getDrawable(R.drawable.button_background));
        button2.setTextSize(15);
        button2.setTextColor(Color.RED);
        button2.setLayoutParams(bparam);
        button2.setText(sold_);
        button2.setPadding(4, 4, 4, 4);

        child3.addView(button2);

        parent.addView(child3);

        return parent;


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
    private void previewAnimation(View view ){

        final Animation myAnim = AnimationUtils.loadAnimation(context , R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);



    }

}
