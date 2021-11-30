package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AllServices extends AppCompatActivity {
    public static final String serivice = "com.codewithharry.multiscreen.SERVICE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);
    }
    public void goToDescription(View view){
        //TextView textView = findViewById(R.id.service_description)
        String seriveName = (String) view.getTag();
        Intent intent = new Intent(this, ServiceDetails.class);
        intent.putExtra(serivice, seriveName);
        startActivity(intent);
        //Toast.makeText(this, seriveName, Toast.LENGTH_SHORT).show();
    }
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
