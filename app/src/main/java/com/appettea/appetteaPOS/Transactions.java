package com.appettea.appetteaPOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
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
 * Use the {@link Transactions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Transactions extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TableLayout tableLayout;

    private SQLiteDatabase db;

    private SQLiteOpenHelper openHelper;

    public Transactions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Transaction.
     */
    // TODO: Rename and change types and number of parameters
    public static Transactions newInstance(String param1, String param2) {
        Transactions fragment = new Transactions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void printPdf(View view){
        /*
        Context context= getContext();
        PrintManager printManager= (PrintManager) context.getSystemService(context.PRINT_SERVICE);
        PrintDocumentAdapter adapter=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
          //  adapter=view.createPrintDocumentAdapter();
        }
        String JobName=getString(R.string.app_name) +"Document";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            PrintJob printJob=printManager.print(JobName,adapter,new PrintAttributes.Builder().build());
        }

         */

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_transaction, container, false);

        openHelper = new DatabaseHelper(getContext());

        db = openHelper.getWritableDatabase();

        Button back  = view.findViewById(R.id.back);

        tableLayout = view.findViewById(R.id.tbl_checkout);

        createRows();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switchFragment(new ThirdFragment());

            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    private TableRow createRow
                (String _code ,String _name ,String _phone, String _date,String _time,String _amount){

        TableRow tableRow =  new TableRow(getContext());

        TextView code = new TextView(getContext());

        code.setText(_code);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.weight=1.0f;
        code.setLayoutParams(params);

        code.setBackgroundColor(Color.parseColor("#b0b0b0"));


        code.setTextColor(Color.BLACK);


        code.setTextSize(12);

        tableRow.addView(code);

        TextView name = new TextView(getContext());

        name.setText(_name);


        name.setBackgroundColor(Color.parseColor("#b0b0b0"));

        name.setTextColor(Color.BLACK);
        name.setTextSize(12);
        name.setLayoutParams(params);

        tableRow.addView(name);



        TextView phone = new TextView(getContext());

        phone.setText(_phone);


        phone.setBackgroundColor(Color.parseColor("#b0b0b0"));

        phone.setTextColor(Color.BLACK);
        phone.setTextSize(12);

        phone.setLayoutParams(params);

        tableRow.addView(phone);



        TextView date  = new TextView(getContext());

        date.setText(_date);

        date.setBackgroundColor(Color.parseColor("#b0b0b0"));

        date.setTextColor(Color.BLACK);
        date.setTextSize(12);

        date.setLayoutParams(params);

        tableRow.addView(date);



        TextView time  = new TextView(getContext());

        time.setText(_time);

        time.setBackgroundColor(Color.parseColor("#b0b0b0"));

        time.setTextColor(Color.BLACK);
        time.setTextSize(12);

        time.setLayoutParams(params);


        tableRow.addView(time);



        TextView amount  = new TextView(getContext());

        amount.setText(_amount);

        amount.setBackgroundColor(Color.parseColor("#b0b0b0"));

        amount.setTextColor(Color.BLACK);
        amount.setTextSize(12);

        amount.setLayoutParams(params);
        tableRow.addView(amount);

        return  tableRow;

    }
    private void createRows(){

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TRANSACTIONS, null);

        int amt = 0;


       // tableLayout.removeAllViews();


        if (c.moveToFirst()) {


            do {
                // Passing values
                    String code  = c.getString(1);

                    String name = c.getString(2);
                    String phone = c.getString(3);
                    String date =  c.getString(4);
                    String time =  c.getString(5);
                    String amount =  c.getString(6);

                    TableRow tableRow = createRow(code,name, phone, date,time,amount);

                    tableLayout.addView(tableRow);






            } while (c.moveToNext());

        }

    }
    private  void switchFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                // add to backstack
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
        // mContentFragment = fragment;
    }
}