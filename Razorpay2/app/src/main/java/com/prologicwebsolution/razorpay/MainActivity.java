package com.prologicwebsolution.razorpay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    TextView payStatus;
    Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checkout use for load all razor library
        // hamlogo ko loading time phase na karna pade isliye ise preload krate h
        Checkout.preload(getApplicationContext());


        payButton = findViewById(R.id.paybutton);
        payStatus = findViewById(R.id.payStatus);


        payButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                PaymentNow("200");
            }
        });
    }

    public void PaymentNow(String amount){

        final Activity activity = this;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_ENKopQVUjc3bu5");
        checkout.setImage(R.drawable.ic_launcher_foreground);

        // razorpay all amount convert in cent thats why
        // the reason when we given in rupee this is convert in doller
        double finalAmount = Float.parseFloat(amount)*100;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Prologic Web Solutions");
            options.put("description", "Refence no. #5536737");
            options.put("image", "https://woocommerce.com/wp-content/uploads/2021/01/fb-razorpay@2x.png");
            options.put("theme.color", "#e3665d");
            options.put("currency", "INR");
            options.put("amount", finalAmount+"");
            options.put("prefill.email", "adadad@gmail.com");
            options.put("prefill.contact", "98657577257");
            checkout.open(activity,options);

        }catch (Exception e){
           Log.e("Tag", "Error in starting Razorpay checkout",e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success",Toast.LENGTH_SHORT).show();
        payStatus.setText(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed",Toast.LENGTH_SHORT).show();
        payStatus.setText(s);
    }
}