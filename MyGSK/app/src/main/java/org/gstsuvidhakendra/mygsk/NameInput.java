package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NameInput extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input);
    }
    public void setName(View view){
        EditText nameView = findViewById(R.id.editText2);
        String leadName = nameView.getText().toString();

        SharedPreferences shrd = getSharedPreferences("gsk_lead", MODE_PRIVATE);
        SharedPreferences.Editor editor = shrd.edit();


        if(leadName.isEmpty()){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("leadName", leadName);
            editor.apply();

            Intent intent = new Intent(this, MobileInput.class);
            startActivity(intent);
        }




    }
}
