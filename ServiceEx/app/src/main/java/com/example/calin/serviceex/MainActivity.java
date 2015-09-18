package com.example.calin.serviceex;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class MainActivity extends Activity {

    TextView downloadEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadEditText = (TextView) findViewById(R.id.downloadText);
        downloadEditText.setMovementMethod(new ScrollingMovementMethod());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FileService.TRANSACTION_DONE);

        registerReceiver(downloadReciever, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startFileService(View view) {

        Intent intent = new Intent(this, FileService.class);

        intent.putExtra("url", "https://www.newthinktank.com/wordpress/lotr.txt");

        this.startService(intent);

    }

    private BroadcastReceiver downloadReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("FileService", "Service Recieved");

            showFileContents();
        }
    };

    public void showFileContents() {

        StringBuilder sb;

        try {

            FileInputStream fis = this.openFileInput("myFile");

            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(isr);

            sb = new StringBuilder();

            String line;

            while((line = bufferedReader.readLine()) != null) {

                sb.append(line).append("\n");

            }

            downloadEditText.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
