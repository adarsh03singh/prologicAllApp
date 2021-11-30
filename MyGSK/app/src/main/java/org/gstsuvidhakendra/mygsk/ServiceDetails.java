package org.gstsuvidhakendra.mygsk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        Intent intent = getIntent();
        String sericeName = intent.getStringExtra(AllServices.serivice);
        TextView serviceNameView = findViewById(R.id.serice_name);
        serviceNameView.setText(sericeName);
        //Button buttonView = findViewById(R.id.button7);
    }
    public void setLeadType(View view){
        Toast.makeText(this, "Soon we will contact you", Toast.LENGTH_SHORT).show();
    }
}
