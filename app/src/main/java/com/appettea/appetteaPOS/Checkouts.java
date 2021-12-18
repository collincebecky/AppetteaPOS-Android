package com.appettea.appetteaPOS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Checkouts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Checkouts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SQLiteDatabase db;
    private DatabaseHelper openHelper;

    private TableLayout tableLayout;

    public Checkouts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Checkout.
     */
    // TODO: Rename and change types and number of parameters
    public static Checkouts newInstance(String param1, String param2) {
        Checkouts fragment = new Checkouts();
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
    private TableRow createRow(String _date, String _items, String _time, String _cash,String _mode){

        TableRow tableRow =  new TableRow(getContext());

        TextView items  = new TextView(getContext());

        items.setText(_items);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.weight=1.0f;

        items.setBackgroundColor(Color.parseColor("#b0b0b0"));

        items.setTextColor(Color.BLACK);
        items.setTextSize(12);

        items.setGravity(Gravity.CENTER);

        items.setLayoutParams(params);

        tableRow.addView(items);

        TextView date = new TextView(getContext());

        date.setText(_date);

        date.setBackgroundColor(Color.parseColor("#b0b0b0"));

        date.setTextColor(Color.BLACK);
        date.setTextSize(12);

        date.setGravity(Gravity.CENTER);

        date.setLayoutParams(params);

        tableRow.addView(date);

        TextView time = new TextView(getContext());

        time.setText(_time);

        time.setBackgroundColor(Color.parseColor("#b0b0b0"));

        time.setTextColor(Color.BLACK);
        time.setTextSize(12);

        time.setGravity(Gravity.CENTER);

        time.setLayoutParams(params);


        tableRow.addView(time);

        TextView cash  = new TextView(getContext());

        cash.setText(_cash);

        cash.setBackgroundColor(Color.parseColor("#b0b0b0"));

        cash.setTextColor(Color.BLACK);
        cash.setTextSize(12);

        cash.setGravity(Gravity.CENTER);

        cash.setLayoutParams(params);

        tableRow.addView(cash);


        TextView mode  = new TextView(getContext());

        mode.setText(_mode);

        mode.setBackgroundColor(Color.parseColor("#b0b0b0"));

        mode.setTextColor(Color.BLACK);
        mode.setTextSize(12);
        mode.setLayoutParams(params);
        tableRow.addView(mode);


        return  tableRow;
    }
    private void createRows(){

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.CHECKOUTS, null);

        int amt = 0;


        if (c.moveToFirst()) {

            int counter = 1;
            do {
                // Passing values
                String date  = c.getString(1);


                    String id = c.getString(0);

                    String items = c.getString(2);
                    String time =  c.getString(3);
                    String cash =  c.getString(4);
                    String mode = c.getString(5);

                    amt+=Integer.parseInt(cash);

                    TableRow tableRow = createRow(date, items, time, cash,mode);

                    tableLayout.addView(tableRow);
                    counter++;



            } while (c.moveToNext());

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        openHelper = new DatabaseHelper(getContext());

        db = openHelper.getWritableDatabase();

        View view  = inflater.inflate(R.layout.fragment_checkout, container, false);

        tableLayout = view.findViewById(R.id.tbl_checkout);

        Button back  = view.findViewById(R.id.back);


        createRows();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchFragment(new ThirdFragment());

            }
        });


        return  view;
    }

    private  void switchFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                // add to backstack
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();

    }



}