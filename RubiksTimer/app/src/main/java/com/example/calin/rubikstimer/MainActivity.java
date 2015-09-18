package com.example.calin.rubikstimer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    final int START = 1;
    final int STOP = 2;
    final int UPDATE = 3;
    final int DELAY = 10;

    Spinner inspectionSpinner;
    Button startButton, resetButton;
    EditText timerEditText;
    boolean counting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inspectionSpinner = (Spinner) findViewById(R.id.inspection_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.inspection_times_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inspectionSpinner.setAdapter(adapter);

        startButton = (Button) findViewById(R.id.start_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        timerEditText = (EditText) findViewById(R.id.timer_edit_text);

        counting = false;
        resetButton.setClickable(false);

        timerEditText.setText("00:00:00");
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

    public void startStopButton(View view) {

        if (!counting) {    //start timer

            String inspectionValue = inspectionSpinner.getSelectedItem().toString();

            if (inspectionValue.equals("")) {

                Toast.makeText(this, "Select an Inspection Time", Toast.LENGTH_SHORT).show();

            } else {

                String temp[] = inspectionValue.split(" ");
                startTimer(temp[0]);

            }
        } else {            //stop timer

            counting = false;
            resetButton.setClickable(true);
            startButton.setClickable(false);
        }

    }

    private void startTimer(String t) {

        final int time = Integer.parseInt(t);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


        startButton.setClickable(false);

        timerEditText.setTextColor(Color.RED);

        Thread timer = new Thread() {
            @Override
            public void run() {

                int count = time;

                while(count > 0) {

                    final String cur = String.valueOf(count--);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            timerEditText.setText(cur);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerEditText.setTextColor(Color.BLACK);
                        startButton.setClickable(true);
                        startStopWatch();
                    }
                });
            }
        };

        timer.start();
    }

    public void startStopWatch() {

        counting = true;

        Thread timerThread = new Thread() {
            long curTime, startTime;

            @Override
            public void run() {

                startTime = System.currentTimeMillis();

                while(counting) {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {

                        String cur;

                        @Override
                        public void run() {
                            curTime = System.currentTimeMillis() - startTime;
                            updateTime(curTime);
                        }
                    });
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkHighScore();
            }

        };

        timerThread.start();
    }

    private void checkHighScore() {

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        final String prev = prefs.getString("HighScore", "EMPTY");
        final String cur = timerEditText.getText().toString();

        if(prev.equals("EMPTY") || prev.equals("00:00:00") || cur.compareTo(prev) < 0) {
            //new highscore or no high score set

            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("HighScore", cur);
            edit.commit();

            new Thread() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("prev: " + prev + ", cur: " + cur);
                            Toast.makeText(getApplicationContext(),
                                    "New High Score: " + cur, Toast.LENGTH_SHORT).show();
                            System.out.println("now: " + timerEditText.getText().toString());
                        }
                    });
                }
            }.start();

        }

    }

    public void updateTime(Long curTime) {

        String vals[] = {String.valueOf(curTime / 60000), //min
                        String.valueOf((curTime / 1000) % 60), //sec
                        String.valueOf((curTime / 10) % 100)}; //milli

        for(int i=0; i<3; i++) {
            if(Integer.parseInt(vals[i]) < 10)
                vals[i] = "0" + vals[i];
        }

        timerEditText.setText(vals[0] + ":" + vals[1] + ":" + vals[2]);
    }

    public void resetTimer(View view) {
        timerEditText.setText("00:00:00");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        resetButton.setClickable(false);
        startButton.setClickable(true);
    }
}
