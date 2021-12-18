package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;

public  class Login extends AppCompatDialogFragment {

    private SQLiteDatabase db;
    static Login newInstance(SQLiteDatabase db) {
        return new Login(db);
    }

    public Login(SQLiteDatabase db){

        this.db = db;
    }
    private CheckBox rememberMe;

    private String _phone;

    private String _empid ;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LoginListener listener;




    //private static final String URL="https://prime-desk.brancetech.com/api/";
    private static final String URL="https://api.brancetech.com/qs/";

    private Context context;

    private Cursor cursor;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_login,null);

        builder.setView(view);

        Button submit = view.findViewById(R.id.submit);

        rememberMe = view.findViewById(R.id.remember);

        TextInputLayout phone = view.findViewById(R.id.phone);

        TextInputLayout empid = view.findViewById(R.id.empid);

        submit.setOnClickListener(v -> {

            String phonenum = phone.getEditText().getText().toString();

            String _empid = empid.getEditText().getText().toString();

            Log.e("[THE TEXT",phonenum);



            //_ID = ID.getText().toString();

            if (!phonenum.isEmpty()) {

               // verify password match

                if(isMatch(getSha1Hex(_empid))){

                    // dismiss view

                    Toast.makeText(getContext(),"welcome ",Toast.LENGTH_SHORT).show();

                    try {

                        listener.isLogin(true);
                    }catch (NullPointerException e){

                    }

                    this.dismiss();

                }


            }

            else{

                Toast.makeText(getContext(), "ID required ...", Toast.LENGTH_SHORT).show();

               // editText.requestFocus();


            }





        });

        /*

        sharedPreferences=context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        _phone=sharedPreferences.getString("phone","");
        _empid=sharedPreferences.getString("empid","");

         */
        ////////////////////////////////////////////////////////////////////

        // phone.getEditText().setText(_phone);

        //  empid.getEditText().setText(_empid);


        setCancelable(false);




        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        final AlertDialog dialog = builder.create();

        return dialog;
    }

    private boolean isMatch(String match){

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ACCOUNT +" WHERE "+DatabaseHelper.ACOL_2+"=?", new String[]{match});

        return  c.getCount()>0;


    }
    public  interface LoginListener{

        void isLogin(boolean state);

    }
    public static String getSha1Hex(String clearString)
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes)
            {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
            return null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        try {

            listener=(LoginListener) context;

        }
        catch (ClassCastException e){
            //throw  new ClassCastException(context.toString());

        }





    }


}