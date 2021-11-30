package com.csestateconnect.www.csconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    try{
                        Intent smsIntent = new Intent("otp");
                        smsIntent.putExtra("message", message);

                        LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                    }catch (Exception e){

                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}