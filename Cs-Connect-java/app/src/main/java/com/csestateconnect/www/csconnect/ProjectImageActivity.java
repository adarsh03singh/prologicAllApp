package com.csestateconnect.www.csconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.csestateconnect.www.csconnect.adapters.ProjectImageAdapter;
import com.csestateconnect.www.csconnect.adapters.ProjectImageSliderAdapter;
import com.csestateconnect.www.csconnect.models.lead.GetActivity;
import com.csestateconnect.www.csconnect.models.project.Project;
import com.csestateconnect.www.csconnect.models.project_detail.ProjectImageList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectImageActivity extends AppCompatActivity {

private Button back_button;
    private static ViewPager mPager;
    NetworkInformation networkInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_image);

        mPager = findViewById(R.id.project_images_slider);
        Intent intent = getIntent();
        String data = intent.getStringExtra("project_value");
        data = data.substring(1, data.length()-1);
        String StringImages[] = data.split(", ");
        List<String> imageValue ;
        imageValue = Arrays.asList(StringImages);

            ProjectImageAdapter mSliderAdapter = new ProjectImageAdapter(getApplicationContext(),imageValue);
            mPager.setAdapter(mSliderAdapter);

        back_button = (Button) findViewById(R.id.image_back_button);
        networkInformation = new NetworkInformation(getApplicationContext());
        if (networkInformation.isConnectingToInternet() == false) {
//no connection
            networkErrorPopup();
        } else

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void networkErrorPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press Retry to Connect.");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (networkInformation.isConnectingToInternet() == false) {
//no connection
                    networkErrorPopup();
                } else
                    recreate();
            }
        });
        builder.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
