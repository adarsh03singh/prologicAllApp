package org.gstsuvidhakendra.mygsk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.EditText;

public class MyReceiver extends BroadcastReceiver {
    private static EditText editText;
    public void setEditText(EditText editText){
        MyReceiver.editText = editText;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage sms:msgs){
            String message = sms.getMessageBody();
            String number = sms.getOriginatingAddress();
            String otp = message.split(":")[1];
            String sender = number.split("-")[1];
            editText.setText(otp);

            /*if(sender == "MYGSTSK"){
                Toast.makeText(context, "OTP verified: "+sender, Toast.LENGTH_SHORT).show();
                Intent newIntent = new Intent(OtpInput, GskList.class);
                startActivity(newIntent);
            }*/

            //Toast.makeText(context, "Sender: "+sender, Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "Number: "+number, Toast.LENGTH_SHORT).show();
        }
    }
}
