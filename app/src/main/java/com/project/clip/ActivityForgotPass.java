package com.project.clip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Phillip on 4/13/2015.
 */
public class ActivityForgotPass extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);


       final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        Button getPassButton = (Button)findViewById(R.id.button_getPass);
        Button cancel = (Button)findViewById(R.id.button_cancelPass);
        final TextView passwordOut = (TextView)findViewById(R.id.textView_passwordOut);
        final EditText passEntry = (EditText)findViewById(R.id.editText_questionEntry);

        getPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String answer = passEntry.getText().toString();
                String correct = prefs.getString("SecurityQ", null);


                if(answer.equals(correct)){

                    String[] pieces = prefs.getString("UserPass",null).split(":");
                    passwordOut.setText("Password: " + pieces[1]);
                }
                else{
                    Toast toast = Toast.makeText(ActivityForgotPass.this,"Incorrect answer",Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityForgotPass.this,LoginActivity.class);
                startActivity(intent);
            }
        });
       prefs.getString("SecurityQ",null);

        TextView securityQTextView = (TextView)findViewById(R.id.textView_secQoutput);

        securityQTextView.setText(prefs.getString("forgotPassQ","None selected"));


    }

}
