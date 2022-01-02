package com.appettea.appetteaPOS;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends TemplateFragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String name = "Menu";

    private SQLiteDatabase db;

    private LinearLayout store;

    private SQLiteOpenHelper openHelper;

    private FirstFragment.GetSms listener;

    private LinearLayout categoryHost;

    private Button current;

    private   Button showAll;

    public HashMap<String, Vector<Object>> storeItemInfo;

    public HashMap<String,Integer> monitor;

    private Button preview;

    private CheckOutDialogue checkOutDialogue;

    private boolean isSearch=false;

    public FirstFragment() {
        // Required empty public constructor
    }
    @Override
    public String getCurrentName(){

        return name;
    }

    public static FirstFragment createInstance() {
        return new FirstFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void checkOut(){

            FragmentManager fragmentManager = getFragmentManager();

            checkOutDialogue= new CheckOutDialogue(getContext(),monitor,storeItemInfo,db);

            checkOutDialogue.show(fragmentManager,"url dialog");



        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                //do sth

                createItems();
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
            }
        }, false);

            //reload views



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        //((AppCompatActivity)getActivity()).getDelegate().setSupportActionBar(view.findViewById(R.id.toolbar));

        setHasOptionsMenu(true);

        //creatAccount();


        storeItemInfo = new HashMap<>();

        monitor = new HashMap<>();

        // Add meal

        preview  = view.findViewById(R.id.preview);

        setListener(requireContext());
        //==================================================================
        store = view.findViewById(R.id.store);

        openHelper = new DatabaseHelper(getContext());

        db = openHelper.getWritableDatabase();


        categoryHost = view.findViewById(R.id.host_category);


        showAll = view.findViewById(R.id.SHOW_ALL);

        current = showAll;

        showAll.setOnClickListener(

                view1 -> {

                    createItems();

                    current.setTextColor(Color.WHITE);

                    showAll.setTextColor(Color.GREEN);

                }
        );

        //fetch();

        createItems();

        ///=======================================================================

        Button checkout = view.findViewById(R.id.checkout);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreview();
                //Toast.makeText(getContext(), "Should show preview", Toast.LENGTH_SHORT).show();
            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View view) {

                checkOut();

            }

        });
        // Inflate the layout for this fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //checkOut();
                //creatAccount(getChildFragmentManager());

            }
        },3000);


        return view;
    }


    private void cleanMonitor(){

        // remove all 0 value of monitor

        for (String id: monitor.keySet())
             {

            int value = monitor.get(id);

            if(String.valueOf(value).equals("0")) monitor.remove(id);

        }
        for( int id = 0; id < monitor.keySet().size(); id++ )
        {
            String value =String.valueOf( monitor.get(id));
            if(value.equals("0"))
            {
                monitor.remove(id);
                id--;
            }
        }


    }


    private void showPreview(){

        FragmentManager fragmentManager = getFragmentManager();

        //cleanMonitor();

        Preview preview = new Preview(getContext(),monitor,storeItemInfo,db);

        preview.show(fragmentManager ,"preview");

        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
                //do sth

                createItems();
                fragmentManager.unregisterFragmentLifecycleCallbacks(this);
            }
        }, false);


    }


    private void createCategories(String category){


        Button button = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,55);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER);
        button.setText(category);
        button.setTextSize(14);
        button.setBackground(getResources().getDrawable(R.drawable.beautifull_button_background_categories));
        button.setTextColor(Color.WHITE);
        button.setAllCaps(true);

        //ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(getContext(), android.R.color.white));

        //button .setBackground(getContext().getResources().getDrawable(R.drawable.button_background));
        //button.setBackgroundColor(Color.LTGRAY);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                current.setTextColor(Color.WHITE);

                button.setTextColor(Color.GREEN);

                showAll.setTextColor(Color.WHITE);

                current = button;

                fetchWithCategory(category);

            }
        });

        categoryHost.addView(button);

    }


    private void fetchWithCategory(String category ){


        isSearch=true;
        Items item = new Items(getContext(),getFragmentManager(),monitor,store,null,db,this);

        // Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ITEMS WH, null);

        Cursor c = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.ITEMS+
                        " WHERE " + DatabaseHelper.COL_6 + " = ? ",new String[]{category}
        );
        int counter = 0;
        if (c.moveToFirst()) {
            store.removeAllViews();

            LinearLayout host  =  new LinearLayout(getContext());
            host.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            do {
                // Passing values
                if(counter == 4 ){

                    store.addView(host);
                    counter=0;
                    host = new LinearLayout(getContext());
                   // continue;


                }

                String id = c.getString(0);
                String description  = c.getString(1);
                String state = c.getString(2);
                String units = c.getString(3);
                String instock = c.getString(4);
                String sold = c.getString(5);
                String _category = c.getString(6);
                String price = c.getString(7);

                Bitmap image = null;
                try {

                    image =  getBitmap(c.getBlob(8));;
                }
                catch (NullPointerException nullPointerException){

                    Toast.makeText(getContext(),"Null item detected ,Item wont be shown",Toast.LENGTH_LONG).show();

                    continue;


                }

                String date =  c.getString(9);

                //LinearLayout _item = item.buildItem(id,description,state, units,instock,sold,_category,price,image,date);


                String num = isSearch ? String.valueOf(monitor.get(id)) : null;

                LinearLayout _item = item.buildMenuItem(id,description,price,image,num);


                host.addView(_item);

                counter++;

            } while (c.moveToNext());

            store.addView(host);

        }

    }
    private void createItems(){

        ArrayList<String> categories = new ArrayList<>();

        if(!isSearch)monitor.clear();

        Items item = new Items(getContext(),getFragmentManager(),monitor,store,db,this);

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ITEMS, null);

        int counter = 0;

        int match  = getScreenResolution(getContext());


        if (c.moveToFirst()) {
            store.removeAllViews();

            //categoryHost.removeAllViews();
            //remove all except first

            removeAllViewsEXceptFirst(categoryHost);

            LinearLayout host  =  new LinearLayout(getContext());
            host.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            do {
                // Passing values
                //if(counter == 4 ){
                if(counter == match){

                    store.addView(host);
                    counter=0;
                    host = new LinearLayout(getContext());
                   // continue;


                }

                String id = c.getString(0);
                String description  = c.getString(1);
                String state = c.getString(2);
                String units = c.getString(3);
                String instock = c.getString(4);
                String sold = c.getString(5);
                String category = c.getString(6);
                String price = c.getString(7);

                byte[] bite = c.getBlob(8);

                Bitmap image = null;
                try {

                    image =  getBitmap(bite);
                }
                catch (NullPointerException nullPointerException){

                    Toast.makeText(getContext(),"Null item detected ,Item wont be shown",Toast.LENGTH_LONG).show();

                    continue;


                }


                String date =  c.getString(9);

                Vector<Object> n = new Vector<>();

                n.add(id);n.add(description);n.add(state);n.add(units);n.add(instock);n.add(sold);n.add(category);n.add(price);n.add(image);n.add(date);

                storeItemInfo.put(id,n);

                if(!categories.contains(category)){

                    createCategories(category);

                    categories.add(category);

                }

                String num = isSearch ? String.valueOf(monitor.get(id)) : null;

                LinearLayout _item = item.buildMenuItem(id,description,price,image,num);

                host.addView(_item);

                counter++;

            } while (c.moveToNext());

            store.addView(host);


        }



    }
    private Bitmap getBitmap(byte[] image){

        return  BitmapFactory.decodeByteArray(image, 0 , image.length);
    }

    @Subscribe
    public void onPhoneNumberReceived(OnReceiverEvent event){

        MessageParser messageParser = event.getSender();

        //listener.passSms(messageParser);

        checkOutDialogue.setMessagePerser(messageParser);

        Toast.makeText(getContext(), "This great again"+ messageParser.getPhone(), Toast.LENGTH_SHORT).show();

    }

    private static int getScreenResolution(Context context) {


        return getScreenSize(context);
    }
    private static int getScreenSize(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
//        String toastMsg = "Screen size is neither large, normal or small";
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int orientation = display.getRotation();

        int i = 0;
        switch (screenSize) {

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                i = 3;
//                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                i = 3;
//                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                //                toastMsg = "Large screen";
                if (orientation == Surface.ROTATION_90
                        || orientation == Surface.ROTATION_270) {
                    // TODO: add logic for landscape mode here
                    i = 4;
                } else {
                    i = 4;
                }


                break;


        }
//        customeToast(toastMsg);

        Log.e("TAG","SIZE "+i);
        return i;
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


    public  interface GetSms{

        void passSms(MessageParser parser);

    }

    public void  setListener(Context context){

        this.listener=(GetSms) getTargetFragment();

    }
    @Override
    public  void onResume() {




        super.onResume();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener=(GetSms)  getTargetFragment();
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    /*
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);


        //try {

            listener=(GetSms) context;

        //}
       // catch (ClassCastException e){
       //     throw  new ClassCastException(context.toString());

        //}


    }

     */
    private void removeAllViewsEXceptFirst(ViewGroup viewGroup){

        int count = viewGroup.getChildCount()-1;

        for (int i = count; i > 0; i--) {

            viewGroup.removeViewAt(i);

        }

    }

    public void previewAnimation(){

        final Animation myAnim = AnimationUtils.loadAnimation(getContext() , R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);

        myAnim.setInterpolator(interpolator);


        preview.startAnimation(myAnim);



    }




}
