 
package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;


public class ChangePassword extends AppCompatDialogFragment  {

    private SQLiteDatabase db;

    private SQLiteOpenHelper openHelper;

    private CheckBox rememberMe;

    private String _phone;

    private String _empid ;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    //private static final String URL="https://prime-desk.brancetech.com/api/";
    private static final String URL="https://api.brancetech.com/qs/";

    private Context context;



    String  currentpass;



    public ChangePassword(Context applicationContext, SQLiteDatabase db) {

        context=applicationContext;

      // this.db=db;

        openHelper = new DatabaseHelper(applicationContext);

        this.db = openHelper.getWritableDatabase();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_changepassword,null);


        //ArrayList<String> details = getDataFromDatabase();


       //currentpass = details.get(2);


        builder.setView(view)
                .setTitle("Change password");


       // EditText phone = view.findViewById(R.id.phone);

        //EditText empid = view.findViewById(R.id.empid);

        EditText current = view.findViewById(R.id.current);
        EditText newpass = view.findViewById(R.id.newpass);
        EditText confirm = view.findViewById(R.id.confirmpass);
        Button change = view.findViewById(R.id.change);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if field are free

                String _current = getSha1Hex(current.getText().toString().trim());
                String _newpass = newpass.getText().toString().trim();
                String _confirm = confirm.getText().toString().trim();

                currentpass= getPassword();




                if(!_current.isEmpty() && !_newpass.isEmpty() && !_confirm.isEmpty()){

                    if(_current.equals(currentpass)){

                        if(_newpass.equals(_confirm)){


                            updatePassword(getSha1Hex(_newpass));
                            Toast.makeText(getContext(),"password updated succesfully",Toast.LENGTH_SHORT).show();
                            dismiss();


                        }

                        else{

                            Toast.makeText(getContext(),"new password doesn't match !!!",Toast.LENGTH_LONG).show();
                        }


                    }
                    else {
                        Toast.makeText(getContext(),"incorrect current password ",Toast.LENGTH_LONG).show();
                    }

                }
                else {

                    Toast.makeText(getContext(),"All fields must be filled  ",Toast.LENGTH_LONG).show();

                }

            }
        });







        //openHelper = new DatabaseHelper(getContext());


        //db = openHelper.getWritableDatabase();


        final AlertDialog dialog = builder.create();







        //ID= view.findViewById(R.id.identify);

        return dialog;
    }

    private void  updatePassword(String newpass){

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.ACOL_2,newpass);

        //long id = db.insert(DatabaseHelper.USER_DATA,null,contentValues)

        db.update(DatabaseHelper.ACCOUNT,contentValues,null,null);
        db.close();



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
    }

    private  String getPassword() {

        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ACCOUNT, null);

        String pass="";

        if (c.moveToFirst()) {

            do {

                pass = c.getString(1);
                Log.e("PASS,", pass);
                break;

            } while (c.moveToNext());

        }

     return  pass;
    }

}

