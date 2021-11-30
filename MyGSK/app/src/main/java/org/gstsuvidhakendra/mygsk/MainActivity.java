package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (ContextCompat.checkSelfPermission(this, "android.permission.RECEIVE_SMS")
                != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_CODE_ASK_PERMISSIONS = 124;
            ActivityCompat.requestPermissions(this,new String[]{"android.permission.RECEIVE_SMS"},REQUEST_CODE_ASK_PERMISSIONS);
            // Permission is not granted
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_SMS")
                != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_SMS"},REQUEST_CODE_ASK_PERMISSIONS);
            // Permission is not granted
        }*/


        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        final String leadStatus = shrd.getString("lead_status","no");

        Log.d("leadStatus", "The response is " + leadStatus);

        if(leadStatus.equals("added")){
            Intent intent = new Intent(this, GskList.class);
            startActivity(intent);
        }else  if(leadStatus.equals("assigned")){
            Intent intent = new Intent(this, InnerMainScreen.class);
            startActivity(intent);
        }else{

        }
    }
    public void selectLanguage(View view){
        //Intent intent = new Intent(this, PinCodeInput.class);
        //EditText
        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);


        String lang;
        Button langButton;
        langButton = (Button)view;
        lang = langButton.getText().toString();

        //SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();
        editor.putString("language", lang);
        editor.apply();

        //Toast.makeText(this, lang, Toast.LENGTH_SHORT).show();

        //EditText editText1 = findViewById(R.id.editText1);

        /*String message = "Order for " + editText1.getText().toString() + ", "
                + editText2.getText().toString() + " & "
                + editText3.getText().toString() + " has been successfully placed";*/
        locationEnabled();

//            Intent intent = new Intent(this, PinCodeInput.class);
//            startActivity(intent);

    }

    private void locationEnabled () {
        LocationManager lm = (LocationManager)
                getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(MainActivity. this )
                    .setMessage( "GPS Enable" )
                    .setPositiveButton( "Settings" , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton( "Cancel" , null )
                    .show() ;
        }else {
            Intent intent = new Intent(this, PinCodeInput.class);
            startActivity(intent);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
