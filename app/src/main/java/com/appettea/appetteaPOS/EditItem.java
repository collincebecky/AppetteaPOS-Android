
package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;


import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;


public class EditItem extends AppCompatDialogFragment implements Login.LoginListener  {



    private DialogListener listener;

    private SQLiteDatabase db;



    private Context context;

    private String id ;
    private String description;
    private String state;
    private String units;
    private String category;
    private String price;
    private String instock;
    private String sold;
    private Bitmap image;
    private String date;

    //---------------------------

    private TextView vdate;
    private EditText vdescription ;
    private EditText vcategory ;
    private EditText  vprice ;
    private EditText vunits ;
    private EditText vinstoke ;
    private EditText vsold ;
    private LinearLayout store;

    private boolean isAllowAccess=false;

    private ImageView imageView;


    public EditItem (Context applicationContext,LinearLayout store, SQLiteDatabase db) {

        context=applicationContext;

        this.store= store;

        this.db=db;
    }

    private void  captureImg(ImageView imageView){
        //->startCamera
        //listener.

    }

    @Override
    public void isLogin(boolean state) {
        isAllowAccess = state;
    }

    public  interface DialogListener{

        void startCamera(ImageView imageView);

    }

    private void startUpload(ImageView imageView){


        listener.startCamera(imageView);


    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edititem,null);



        // rememberMe = view.findViewById(R.id.remember);

         builder.setView(view);//.setTitle("ViEW ITEM");




         vdate = view.findViewById(R.id.dateAdded);
         vdescription = view.findViewById(R.id.description);
         vcategory  = view.findViewById(R.id.category);
         vprice = view.findViewById(R.id.price);
         vunits = view.findViewById(R.id.units);
         vinstoke  = view.findViewById(R.id.instoke);
         vsold  = view.findViewById(R.id.sold);


        vcategory.setText(this.category);
        vdescription.setText(this.description);
        vinstoke.setText(this.instock);
        vdate.setText(this.date);
        vunits.setText(this.units);
        vsold.setText(this.sold);
        vprice.setText(this.price);


        imageView = view.findViewById(R.id.foodImage);


        Button capture = view.findViewById(R.id.capture);


        Button pickColor = view.findViewById(R.id.color);

        Button upload = view.findViewById(R.id.upload);

        RadioButton fluid = view.findViewById(R.id.fluid);
        RadioButton track = view.findViewById(R.id.track);

        if (state.equals("1")){
            track.setChecked(true);

        }
        else  {

            fluid.setChecked(true);
            vunits.setEnabled(false);
            vinstoke.setEnabled(false);
        }


        capture.setOnClickListener(view12 -> captureImg(imageView));

        pickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker();
            }
        });


        upload.setOnClickListener(view1 -> {
            //startUpload();
            startUpload(imageView);

        });

        fluid.setOnClickListener(view15 -> {
            if(fluid.isChecked()){

                track.setChecked(false);
                vunits.setEnabled(false);
                vinstoke.setEnabled(false);
            }
            else {

                track.setChecked(true);
                vunits.setEnabled(true);
                vinstoke.setEnabled(true);

            }
        });

        track.setOnClickListener(view14 -> {

            if(track.isChecked()){

                fluid.setChecked(false);
                vunits.setEnabled(true);
                vinstoke.setEnabled(true);
            }
            else {
                fluid.setChecked(true);
                vunits.setEnabled(false);
                vinstoke.setEnabled(false);

            }
        });

        Button update  = view.findViewById(R.id.update);

        Button delete = view.findViewById(R.id.delete);

        //ImageView imageView = view.findViewById(R.id.foodImage);

        //-spawn login and allow update to continue if true

        //createLogin();




        update.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                //->TODO check if line edit is empty





                //if(!isAllowAccess) return;




                String upcatergory = vcategory.getText().toString().trim();
                String updescription = vdescription.getText().toString().trim();
                String upinstock = vinstoke.getText().toString().trim();
                String upvdate = vdate.getText().toString().trim();
                String upunits = vunits.getText().toString().trim();
                String upsold  = vsold.getText().toString().trim();
                String upprice  = vprice.getText().toString().trim();

                Bitmap image_ = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image_.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bite = stream.toByteArray();

                if (!upcatergory.equals(category) ||!updescription.equals(description) || !upunits.equals(units)|| !upprice.equals(price)|| !compareBitmap(image_,image)){

                    //update data to the database where id equals id


                    // check if track checked disalLow non digit and 0 and empty

                    if(track.isChecked()){

                        if(!isGoodInput(vunits)||!isGoodInput(vsold) || !isGoodInput(vprice)) {

                            Toast.makeText(getContext(),"invalid input ",Toast.LENGTH_SHORT).show();

                            return;
                        }

                        state = "1";

                    }
                    else  state = "0";

                    updateDataLegit(
                             id,
                            updescription,
                            state,
                            upunits,
                            upinstock,
                            upsold,
                            upcatergory,
                            upprice,
                            bite ,
                            upvdate);



                    //->refresh items

                    createItems();

                    Toast.makeText(getContext(), "Update successful", Toast.LENGTH_SHORT).show();
                    dismiss();

                }

                Toast.makeText(getContext(), "No change was made ", Toast.LENGTH_SHORT).show();


            }
        });

        vsold.setEnabled(false);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if(deleteItem(id)){

                                    Toast.makeText(context,"Delete successfully ",Toast.LENGTH_SHORT).show();

                                    dismiss();

                                }


                             }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        //++++++++\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        //openHelper = new DatabaseHelper(getContext());


        //db = openHelper.getWritableDatabase();


        final AlertDialog dialog = builder.create();

        //ID= view.findViewById(R.id.identify);

        return dialog;
    }

    private void showColorPicker(){

        new ColorPickerDialog()
                .withAlphaEnabled(false)
                .withPresets(Color.RED, Color.GREEN, Color.BLUE,Color.YELLOW,Color.GRAY,Color.BLACK,Color.CYAN,Color.WHITE)
                .withListener(new OnColorPickedListener<ColorPickerDialog>() {
                    @Override
                    public void onColorPicked(@Nullable ColorPickerDialog dialog, int color) {
                        // a color has been picked; use it

                        imageView.setImageBitmap(getBitmap(color));


                    }
                })
                .show(getFragmentManager(), "colorPicker");




    }

    public static Bitmap getBitmap(int color){


        Rect rect = new Rect(0, 0, 1, 1);


        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawRect(rect, paint);
        return  image;


    }


    private boolean deleteItem(String id){


       return db.delete(DatabaseHelper.ITEMS,"ID=?" ,new String[]{id})>0;


    }



    private void updateDataLegit(String id,
                                 String description ,
                                 String state ,
                                 String units ,
                                 String instock ,
                                 String sold,
                                 String category,
                                 String price,
                                 byte[] image,
                                 String date){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_1,description);
        contentValues.put(DatabaseHelper.COL_2,state);
        contentValues.put(DatabaseHelper.COL_3,units);
        contentValues.put(DatabaseHelper.COL_4,instock);
        contentValues.put(DatabaseHelper.COL_5,sold);
        contentValues.put(DatabaseHelper.COL_6,category);
        contentValues.put(DatabaseHelper.COL_7,price);
        contentValues.put(DatabaseHelper.COL_8,image);
        contentValues.put(DatabaseHelper.COL_9,date);



        db.update(DatabaseHelper.ITEMS,contentValues,"ID=?" ,new String[]{id});

    }

    private boolean isGoodInput(EditText editItem){

        String s = editItem.getText().toString().trim();

        if(s.isEmpty() || s.equals("0") || s.contains("-") || !isNumeric(s))

            return false;

        return true;

    }
    private void createLogin(){

        DialogFragment newFragment = Login.newInstance(db);

        newFragment.show(getFragmentManager(), "dialog");

    }

    private void createItems() {


        //-> fetch from d

        Items item = new Items(getContext(),getFragmentManager() ,store,null,db);

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ITEMS, null);
        int counter = 0;
        if (c.moveToFirst()) {

            store.removeAllViews();
            LinearLayout host = new LinearLayout(getContext());
            host.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            do {
                // Passing values
                if (counter == 4) {

                    store.addView(host);
                    counter = 0;
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
                Bitmap image = null;
                try {

                    image =  getBitmap(c.getBlob(8));;
                }
                catch (NullPointerException nullPointerException){

                    Toast.makeText(getContext(),"Null item detected ,Item wont be shown",Toast.LENGTH_LONG).show();

                    continue;


                }
                String date =  c.getString(9);

                LinearLayout _item = item.buildItem(id,description,state, units,instock,sold,category,price,image,date);

                host.addView(_item);


                //break;

                // Do something Here with values
                counter++;
            } while (c.moveToNext());


            store.addView(host);


        }


    }
    public boolean compareBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);

        return Arrays.equals(buffer1.array(), buffer2.array());
    }

    private Bitmap getBitmap(byte[] image){

        return  BitmapFactory.decodeByteArray(image, 0 , image.length);
    }

    public void setData(String id,String description,String state, String units,String instock,String sold,String category,String price,Bitmap image,String date){

       this.id = id;

       this.description = description;

       this.state = state;

       this.units = units;

       this.category = category;

       this.instock = instock;

       this.sold = sold;

       this.price = price;

       this.image = image;

       this.date = date;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            listener=(EditItem.DialogListener) context;

        }
        catch (ClassCastException e){
            throw  new ClassCastException(context.toString());

        }


    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }




    /**
     * Detects left and right swipes across a view.
     */



}

