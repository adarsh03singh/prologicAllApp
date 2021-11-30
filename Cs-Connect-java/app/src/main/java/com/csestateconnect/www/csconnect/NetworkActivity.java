package com.csestateconnect.www.csconnect;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NetworkActivity extends AppCompatActivity {

    ConstraintLayout loader;
    private Button retryButton;
    NetworkInformation networkInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        loader = (ConstraintLayout) findViewById(R.id.progress_bar_layout);
        retryButton = findViewById(R.id.network_retry_button);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                networkInformation = new NetworkInformation(getApplicationContext());
                if (networkInformation.isConnectingToInternet() == true) {
                    loader.setVisibility(View.INVISIBLE);
                    inflateActivityOrFragment();
                }else if(networkInformation.isConnectingToInternet() == false){
                    loader.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void inflateActivityOrFragment() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
