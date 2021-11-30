package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PinCodeInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_input);
    }
    public void setPinCode(View view){

        EditText pinCodeView = findViewById(R.id.editText);
        String pinCode = pinCodeView.getText().toString();

        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();

        if(pinCode.isEmpty()){
            Toast.makeText(this, "Enter Pincode", Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("pincode", pinCode);
            editor.apply();
            Intent intent = new Intent(this, NameInput.class);
            startActivity(intent);
        }
    }
}
