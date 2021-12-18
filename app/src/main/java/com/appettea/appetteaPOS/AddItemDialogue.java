 
package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;


public class AddItemDialogue extends AppCompatDialogFragment {

    static final int REQUEST_IMAGE_OPEN = 1;

    private DialogListener listener;

    private SQLiteDatabase db;

    private SQLiteOpenHelper openHelper;

    private CheckBox rememberMe;

    private String _phone;

    private String _empid ;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private  LinearLayout store;

    //private static final String URL="https://prime-desk.brancetech.com/api/";
    private static final String URL="https://api.brancetech.com/qs/";

    private Context context;

    private Cursor cursor;

    private ImageView imageView;


    public AddItemDialogue (Context applicationContext,LinearLayout store, SQLiteDatabase db) {

        context=applicationContext;
        this.store = store;

        this.db=db;
    }

    public  interface DialogListener{

        void startCamera(ImageView imageView);

        void startCameraView(ImageView imageView);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_additem,null);


        Button upload = view.findViewById(R.id.upload);

        imageView = view.findViewById(R.id.foodImage);

        EditText category = view.findViewById(R.id.category);
        EditText description = view.findViewById(R.id.description);
        EditText units = view.findViewById(R.id.units);
        EditText price = view.findViewById(R.id.price);

        Button submit = view.findViewById(R.id.submit);

        Button pickColor = view.findViewById(R.id.color);


        RadioButton fluid = view.findViewById(R.id.fluid);
        RadioButton track = view.findViewById(R.id.track);


        Button capture = view.findViewById(R.id.capture);
        fluid.setChecked(true);

        pickColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker();
            }
        });
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(15 - s.toString().length() == 0 ){

                    Toast.makeText(getContext(), "Enough text", Toast.LENGTH_SHORT).show();

                }
                //tv_counter.setText(80 - s.toString().length() + "/80");

            }
        });

        capture.setOnClickListener(view12 -> captureImg(imageView));


        upload.setOnClickListener(view1 -> {
            //startUpload();
            startUpload(imageView);

        });

        fluid.setOnClickListener(view15 -> {
            if(fluid.isChecked()){

                units.setEnabled(false);
                track.setChecked(false);

            }
            else {

                units.setEnabled(true);
                track.setChecked(true);


            }
        });

        track.setOnClickListener(view14 -> {

            if(track.isChecked()){

                units.setEnabled(true);
                fluid.setChecked(false);
            }
            else {
                units.setEnabled(false);
                fluid.setChecked(true);

            }
        });



        submit.setOnClickListener(view13 -> {

           String _category  =   category.getText().toString().trim().toUpperCase(Locale.ROOT);
           String _description = description.getText().toString().trim();
           String _units =       units.getText().toString().trim();
           String _price =       price.getText().toString().trim();

            if(imageView.getDrawable() == null) {
                Toast.makeText(getContext(), "upload image to continue", Toast.LENGTH_SHORT).show();
                return;

            }


                //category,price ,description,units and image view  not empty
            else if(!_category.isEmpty() && !_description.isEmpty() && !_units.isEmpty() && !_price.isEmpty() ){


                int track1 = 1;

                if(fluid.isChecked()){

                    track1 = 0;


                }

                //-> check for zero units posibility
                if(track1==1){

                    if(_units.isEmpty() || _units.equals("0") || !isNumeric(_units) ) {

                        Toast.makeText(getContext(),"invalid input for units ", Toast.LENGTH_SHORT).show();

                        return;

                    }
                }

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                //Bitmap image  = BitmapFactory.decodeResource(context.getResources(), R.drawable.soda);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] bite = stream.toByteArray();
                    /*
                    if (image!=null && !image.isRecycled()){
                        image.recycle(); image = null;
                    }


                     */

                if(!isValidImage(bite)){

                    Toast.makeText(context,"Invalid Image,please try a different one",Toast.LENGTH_SHORT).show();


                }


                if(insertData(camelCase(_description),String.valueOf(track1),_units,_category,_price,bite,currentDate)){

                    Toast.makeText(getContext(),"added Successfully",Toast.LENGTH_LONG).show();
                    createItems();
                    dismiss();

                }
                else  Toast.makeText(getContext(),"add Failed ,Fill all spaces",Toast.LENGTH_LONG).show();


            }

            else{

                Toast.makeText(getContext(), "No field should be left empty ", Toast.LENGTH_SHORT).show();
            }


        });
       // rememberMe = view.findViewById(R.id.remember);



        builder.setView(view)
                .setTitle("ADD ITEM");


        final AlertDialog dialog = builder.create();


        return dialog;
    }
    private boolean isValidImage(byte[] bytes)

    {

        Bitmap image = null;

        try {

            image =  getBitmap(bytes);
        }
        catch (NullPointerException nullPointerException){

            return false;

        }

        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


    private void  captureImg(ImageView imageView){

        listener.startCameraView(imageView);

    }
    private byte[] getBitmapBytes_(Bitmap image)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bite = stream.toByteArray();
        image.recycle();

        return bite;
    }

    private void startUpload(ImageView imageView){

            listener.startCamera(imageView);

    }


    private byte[] getBitmapBytes(Bitmap bitmap)
    {
        int chunkNumbers = 10;
        int bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();
        byte[] imageBytes = new byte[bitmapSize];
        int rows, cols;
        int chunkHeight, chunkWidth;
        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight() / rows;
        chunkWidth = bitmap.getWidth() / cols;

        int yCoord = 0;
        int bitmapsSizes = 0;

        for (int x = 0; x < rows; x++)
        {
            int xCoord = 0;
            for (int y = 0; y < cols; y++)
            {
                Bitmap bitmapChunk = Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkWidth, chunkHeight);
                byte[] bitmapArray = getBytesFromBitmapChunk(bitmapChunk);
                System.arraycopy(bitmapArray, 0, imageBytes, bitmapsSizes, bitmapArray.length);
                bitmapsSizes = bitmapsSizes + bitmapArray.length;
                xCoord += chunkWidth;

                bitmapChunk.recycle();
                bitmapChunk = null;
            }
            yCoord += chunkHeight;
        }

        return imageBytes;
    }

    private byte[] getBytesFromBitmapChunk(Bitmap bitmap)
    {
        int bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmapSize);
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
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

                // Do something Here with values
            } while (c.moveToNext());
        }



        return c;
    }


    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            // Do work with full size photo saved at fullPhotoUri
        ...
        }

    void startUpload(){
        //access filepath to get
        }

 */

    public boolean insertData(String description,
                           String state,
                           String units ,
                           String category,
                           String price,
                           byte[] image,
                           String currentDate
                          )
    {

        //->pass Data to Database ------------------------>
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COL_1,description);
        contentValues.put(DatabaseHelper.COL_2,state);
        contentValues.put(DatabaseHelper.COL_3,units);
        contentValues.put(DatabaseHelper.COL_4,units);
        contentValues.put(DatabaseHelper.COL_5,"0");
        contentValues.put(DatabaseHelper.COL_6,category);

        contentValues.put(DatabaseHelper.COL_7,price);
        contentValues.put(DatabaseHelper.COL_8,image);
        contentValues.put(DatabaseHelper.COL_9,currentDate);


        long id = db.insert(DatabaseHelper.ITEMS,null,contentValues);
        if(id ==-1) {




            return false;
        }
        else{

            Log.e("LOG","insert successful");

            return true;
        }


    }

    private void createItems() {


        //-> fetch from d

        Items item = new Items(getContext(),getFragmentManager() ,store,null,db );

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

                    Toast.makeText(getContext(),"Corrupted item detected ,will be removed \n use a different image for item",Toast.LENGTH_LONG).show();

                    deleteItem(id);

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
    private Bitmap getBitmap(int color){


        Rect rect = new Rect(0, 0, 1, 1);


        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawRect(rect, paint);
        return  image;


    }


    public static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private String camelCase(String line){

            String build ="";

            String[] words = line.split("\\s");
            for (String s : words) {


                build+=capitalize(s)+" ";


            }



        return build;


    }


    private boolean deleteItem(String id){


        return db.delete(DatabaseHelper.ITEMS,"ID=?" ,new String[]{id})>0;


    }

    private Bitmap getBitmap(byte[] image){

        return  BitmapFactory.decodeByteArray(image, 0 , image.length);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            listener=(DialogListener) context;

        }
        catch (ClassCastException e){
            throw  new ClassCastException(context.toString());

        }

    }

    /**
     * Detects left and right swipes across a view.
     */

}

