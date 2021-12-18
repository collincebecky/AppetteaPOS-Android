package com.appettea.appetteaPOS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends TemplateFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SQLiteDatabase db;

    private LinearLayout store;

    private SQLiteOpenHelper openHelper;


    private LinearLayout categoryHost;

    private Button current;

    private   Button showAll;
    protected String name ="Stock";

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public String getCurrentName(){

        return name;
    }

    public static SecondFragment createInstance() {
        return new SecondFragment();
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {

        SecondFragment fragment = new SecondFragment();
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
    private void showAddItem(LinearLayout store){



        AddItemDialogue dialogue = new AddItemDialogue(getContext(),store,db);

        dialogue.show(getFragmentManager(),"show dialog");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_second, container, false);

        Button additem = view.findViewById(R.id.add_item);


        store = view.findViewById(R.id.store);


        openHelper = new DatabaseHelper(getContext());

        db = openHelper.getWritableDatabase();


        categoryHost = view.findViewById(R.id.host_category);


        showAll = view.findViewById(R.id.SHOW_ALL);

        current = showAll;

        showAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        createItems();

                        current.setTextColor(Color.WHITE);

                        showAll.setTextColor(Color.GREEN);

                    }
                }
        );



        //fetch();

        createItems();

        /*
        ImageView imageView =  view.findViewById (R.id.foodImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditItem editItem = new EditItem(getContext(),null);
                editItem.show(getFragmentManager(),"editItem");


            }
        });

         */

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItem(store);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private Cursor fetch(){



            Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ITEMS, null);
            if (c.moveToFirst()) {
                do {
                    // Passing values
                    Log.e("TAG id >>> ",c.getString(0));
                    Log.e("TAG description >>> ",c.getString(1));
                    Log.e("TAG state>>> ",c.getString(2));
                    Log.e("TAG category >>> ",c.getString(3));
                    Log.e("TAG  price >>> ",c.getString(5));
                    Log.e("TAG image >>> ",c.getString(6));

                    //break;

                    // Do something Here with values
                } while (c.moveToNext());
            }


        return c;
    }

    private void createCategories(String category){


        Button button = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,55);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER);
        button.setText(category);
        button.setTextSize(14);
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


        Items item = new Items(getContext(),getFragmentManager(),store,null,db);

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
                    continue;


                }
                String id = c.getString(0);
                String description  = c.getString(1);
                String state = c.getString(2);
                String units = c.getString(3);
                String instock = c.getString(4);
                String sold = c.getString(5);
                String _category = c.getString(6);
                String price = c.getString(7);
                //Bitmap image =  getBitmap(c.getBlob(8));;
                Bitmap image = null;
                try {

                    image =  getBitmap(c.getBlob(8));
                }
                catch (NullPointerException nullPointerException){

                    Toast.makeText(getContext(),"Null item detected ,Item wont be shown",Toast.LENGTH_LONG).show();

                    continue;


                }

                String date =  c.getString(9);

                LinearLayout _item = item.buildItem(id,description,state, units,instock,sold,_category,price,image,date);

                host.addView(_item);



                //break;

                // Do something Here with values
                counter++;
            } while (c.moveToNext());


            store.addView(host);


        }



    }

    private void removeAllViewsEXceptFirst(ViewGroup viewGroup){

        int count = viewGroup.getChildCount()-1;

        for (int i = count; i > 0; i--) {

            viewGroup.removeViewAt(i);

        }

    }

    private void createItems(){
        ArrayList<String> categories = new ArrayList<>();

        //-> fetch from d

        Items item = new Items(getContext(),getFragmentManager(),store,null,db);

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ITEMS, null);
        int counter = 0;
        if (c.moveToFirst()) {
            store.removeAllViews();

            //categoryHost.removeAllViews();
            //remove all except first

            removeAllViewsEXceptFirst(categoryHost);

            LinearLayout host  =  new LinearLayout(getContext());
            host.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            do {
                // Passing values
                if(counter == 4 ){

                    store.addView(host);
                    counter=0;
                    host = new LinearLayout(getContext());
                    continue;


                }
                String id = c.getString(0);
                String description  = c.getString(1);
                String state = c.getString(2);
                String units = c.getString(3);
                String instock = c.getString(4);
                String sold = c.getString(5);
                String category = c.getString(6);
                String price = c.getString(7);
               // Bitmap image =  getBitmap(c.getBlob(8));;
                Bitmap image = null;
                try {

                    image =  getBitmap(c.getBlob(8));
                }
                catch (NullPointerException nullPointerException){

                    Toast.makeText(getContext(),"Corrupted item detected ,will be removed \n use a different image for item",Toast.LENGTH_LONG).show();

                    deleteItem(id);

                    continue;


                }

                String date =  c.getString(9);

                if(!categories.contains(category)){

                    createCategories(category);

                    categories.add(category);


                }

                LinearLayout _item = item.buildItem(id,description,state, units,instock,sold,category,price,image,date);

                host.addView(_item);



                //break;

                // Do something Here with values
                counter++;
            } while (c.moveToNext());


            store.addView(host);


        }



    }
    private Bitmap getBitmap(byte[] image){

        return  BitmapFactory.decodeByteArray(image, 0 , image.length);
    }

    private boolean deleteItem(String id){


        return db.delete(DatabaseHelper.ITEMS,"ID=?" ,new String[]{id})>0;


    }


}