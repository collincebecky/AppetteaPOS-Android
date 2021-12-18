package com.appettea.appetteaPOS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.security.MessageDigest;

public class CreateAccount extends AppCompatDialogFragment {

    private SQLiteDatabase db;
    static CreateAccount newInstance(SQLiteDatabase db) {
        return new CreateAccount(db);
    }

    public CreateAccount(SQLiteDatabase db){

        this.db = db;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_createaccount,null);

        //((TextView)tv).setText("This is an instance of MyDialogFragment");

        builder.setView(view);

        EditText phone = view.findViewById(R.id.bscontact);

        EditText bs_name = view.findViewById(R.id.bsname);

        EditText paybill = view.findViewById(R.id.paybill);

        EditText password = view.findViewById(R.id.password);

        EditText passconfirm  = view.findViewById(R.id.confirmpass);

        Button submit  = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(view1 -> {
            //-> insert into db
            String _phone = phone.getText().toString().trim();

            String _bs_name = bs_name.getText().toString().trim();

            String _paybill = paybill.getText().toString().trim();

            String _password = password.getText().toString().trim();

            String _passconfirm  = passconfirm.getText().toString().trim();


            if(!_phone.isEmpty()||! _bs_name.isEmpty()|| !_paybill.isEmpty()||!_password.isEmpty()|| !_passconfirm.isEmpty()){

                //Toast.makeText(getContext(),"new password doesnt match ,Try again",Toast.LENGTH_SHORT);

                if (!_password.equals(_passconfirm)){

                    Toast.makeText(getContext(),"new password doesnt match ,Try again",Toast.LENGTH_SHORT).show();

                    return;

                }

                registerAccount(_phone,
                        _bs_name ,
                        _paybill,
                        getSha1Hex(_password));

                Toast.makeText(getContext(),"Submitted successfully !!! ",Toast.LENGTH_SHORT).show();

                dismiss();


                //Log.e("hey", "hello ---------------------------------------> ");

            }

            else {

                Toast.makeText(getContext(), "No field should be empty !!! ", Toast.LENGTH_SHORT).show();
            }

        });
        setCancelable(false);


        final AlertDialog dialog = builder.create();

        return dialog;
    }
    private boolean registerAccount(String _phone,
                                    String _bs_name ,
                                    String _paybill,
                                    String _password){

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.ACOL_1,_bs_name);
        contentValues.put(DatabaseHelper.ACOL_2,_password);
        contentValues.put(DatabaseHelper.ACOL_3,_phone);
        contentValues.put(DatabaseHelper.ACOL_4,_paybill);

        long id = db.insert(DatabaseHelper.ACCOUNT,null,contentValues);
        if(id ==-1) {

            return false;
        }
        else{

            Log.e("LOG","insert successful");

            return true;
        }


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
}
