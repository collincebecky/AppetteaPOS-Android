package com.appettea.appetteaPOS;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends TemplateFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected String name = "Sales";

    private TableLayout tableLayout;

    private SQLiteDatabase db;

    private LinearLayout store;

    private SQLiteOpenHelper openHelper;

    private String currentDate;

    private TextView orders;

    private TextView revenue;

    public static ThirdFragment createInstance() {
        return new ThirdFragment();
    }

   private LineChart lineChart;
   @Override
   public String getCurrentName(){

       return name;
   }


    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view   = inflater.inflate(R.layout.fragment_third, container, false);


        openHelper = new DatabaseHelper(getContext());

        db = openHelper.getWritableDatabase();


        tableLayout = view.findViewById(R.id.tbl_checkout);

        orders  = view.findViewById(R.id.orders);

        revenue = view.findViewById(R.id.revenue);

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        createRows();


        Button checkouts = view.findViewById(R.id.checkout);


        Button transactions = view.findViewById(R.id.transaction);


        checkouts.setOnClickListener(view1 -> {

            switchFragment(new Checkouts());

        });

        transactions.setOnClickListener(view12 -> switchFragment(new Transactions()));


        /*

        lineChart =  view.findViewById(R.id.linechart);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        lineChart.setDragEnabled(true);

        lineChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0,60f));
        yValues.add(new Entry(1,50f));
        yValues.add(new Entry(2,35f));
        yValues.add(new Entry(3,40f));
        yValues.add(new Entry(4,70f));
        yValues.add(new Entry(5,60f));
        yValues.add(new Entry(6,20f));
        yValues.add(new Entry(7,60f));

        LineDataSet lineDataSet = new LineDataSet(yValues,"DataSet 1");

        lineDataSet.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(lineDataSet);

        //lineDataSet.setColor(Color.WHITE);

        LineData lineData = new LineData(dataSets);

        lineDataSet.setColors(new int[]{R.color.colorPrimary ,R.color.colorPrimary },getContext());



        lineChart.setData(lineData);

        lineChart.getAxisLeft().setTextColor(Color.WHITE);

        lineChart.getAxisRight().setTextColor(Color.WHITE);

        lineChart.getLineData().setValueTextColor(Color.WHITE);

        lineChart.getXAxis().setTextColor(Color.WHITE);
        YAxis rightYAxis = lineChart.getAxisRight();
        //rightYAxis.setEnabled(false);
        YAxis leftYAxis = lineChart.getAxisLeft();
       // leftYAxis.setEnabled(false);
        XAxis topXAxis = lineChart.getXAxis();
        //topXAxis.setEnabled(false);




       // LineData data = new LineData()


        // Inflate the layout for this fragment

         */


        return view;
    }
    public void switchFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                // add to backstack
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
       // mContentFragment = fragment;
    }


    private TableRow createRow(String _date,String _items,String _time,String _cash,String _mode){

        TableRow tableRow =  new TableRow(getContext());

        TextView date  = new TextView(getContext());

        date.setText(_date);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.weight=1.0f;

        date.setBackgroundColor(Color.parseColor("#b0b0b0"));

        date.setTextColor(Color.BLACK);
        date.setTextSize(12);

        date.setLayoutParams(params);

        tableRow.addView(date);

        TextView items = new TextView(getContext());

        items.setText(_items);


        items.setBackgroundColor(Color.parseColor("#b0b0b0"));

        items.setTextColor(Color.BLACK);
        items.setTextSize(12);

        items.setLayoutParams(params);

        tableRow.addView(items);

        TextView time = new TextView(getContext());

        time.setText(_time);


        time.setBackgroundColor(Color.parseColor("#b0b0b0"));

        time.setTextColor(Color.BLACK);
        time.setTextSize(12);

        time.setLayoutParams(params);

        tableRow.addView(time);

        TextView cash  = new TextView(getContext());

        cash.setText(_cash);

        cash.setBackgroundColor(Color.parseColor("#b0b0b0"));

        cash.setTextColor(Color.BLACK);
        cash.setTextSize(12);
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

        //tableLayout.removeAllViews();
        if (c.moveToFirst()) {

            int counter = 0;
            do {
                // Passing values
                String date  = c.getString(2);


                Log.e("TAG    ","------"+amt);

               if(isToday(date)) {
                    String id = c.getString(0);

                    String items = c.getString(1);
                    String time =  c.getString(3);
                    String cash =  c.getString(4);
                    String mode = c.getString(5);

                    amt+=Integer.parseInt(cash);


                    Log.e("TAG    ","------"+amt);

                    TableRow tableRow = createRow(date, items, time, cash,mode);

                    tableLayout.addView(tableRow);
                    counter++;

               }

            } while (c.moveToNext());

            orders.setText(String.valueOf(counter)+"    Orders");

            revenue.setText("Revenue \n Ksh "+String.valueOf(amt));

        }



    }

    boolean isToday(String _date){

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        try {

            Date today = format.parse(currentDate);

            Date dday = format.parse(_date);

            if(dday.equals(today)){

                return true;}

            else return  false;

        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

    }

}