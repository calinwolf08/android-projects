package com.example.calin.texting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    EditText txtMsgEditText, pNumEditText, messagesEditText;
    Button sendButton;

    static String messages = "";

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMsgEditText = (EditText) findViewById(R.id.txtMsgEditText);
        pNumEditText = (EditText) findViewById(R.id.pNumEditText);
        messagesEditText = (EditText) findViewById(R.id.messagesEditText);
        sendButton = (Button) findViewById(R.id.sendButton);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {

                        // Wait 5 seconds and then execute the code in run()
                        Thread.sleep(5000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                // Update the messagesEditText
                                messagesEditText.setText(messages);
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();


    }


    public void sendMessages(View view) {

        String phoneNum = pNumEditText.getText().toString();
        String message = txtMsgEditText.getText().toString();

        try {

            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(phoneNum, null, message,
                    null, null);

            Toast.makeText(this, "message sent", Toast.LENGTH_LONG).show();



        }catch (IllegalArgumentException e) {

            Toast.makeText(this, "Enter valid number and message", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

        messages = messages + "You: " + message + "\n";
    }

    public static class SmsReceiver extends BroadcastReceiver {

        final SmsManager smsManager = SmsManager.getDefault();

        public SmsReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();

            try {

                if(bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for(int i=0; i<pdusObj.length; i++) {

                        SmsMessage smsMessage =
                                SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                        String phoneNumber = smsMessage.getDisplayOriginatingAddress();
                        String message = smsMessage.getMessageBody();

                        messages = messages + /*phoneNumber +*/ " : " + message + "\n";
                    }
                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver");
            }
        }
    }

    public static class MMSReceiver extends BroadcastReceiver{

        public MMSReceiver(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            throw new UnsupportedOperationException("Not Implemented Yet");
        }
    }

    public static class HeadlessSmsSendService extends BroadcastReceiver{

        public HeadlessSmsSendService(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            throw new UnsupportedOperationException("Not Implemented Yet");
        }
    }

    @Override
    protected void onDestroy() {

        mHandler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}
