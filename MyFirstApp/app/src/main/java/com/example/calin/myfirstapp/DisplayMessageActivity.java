package com.example.calin.myfirstapp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by calin on 7/17/15.
 */
public class DisplayMessageActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_message_layout);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.FIRST_MESSAGE);

        TextView textView = (TextView) findViewById(R.id.text_sent_message);
        textView.setText(message);
    }
}
