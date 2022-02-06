package com.example.dima.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import static com.example.dima.myapplication.WiFi_Service.PREFS_NAME;

public class ScrollingActivity extends AppCompatActivity {


    static Switch mySwitch;
    static SeekBar mySeekBar;
    static TextView myTextView;
    static boolean work_bool=false;
    static int work_time=45;
    static int work_time_pos=3;
    static boolean activity_bool=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mySwitch=(Switch)findViewById(R.id.switch1);
        mySeekBar=(SeekBar)findViewById(R.id.seekBar2);
        myTextView=(TextView) findViewById(R.id.textView2);

        if (mySeekBar != null) {
            mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    switch(progress) {
                        case 0:
                            work_time=20;
                            break;
                        case 1:
                            work_time=25;
                            break;
                        case 2:
                            work_time=30;
                            break;
                        case 3:
                            work_time=45;
                            break;
                        case 4:
                            work_time=60;
                            break;
                        case 5:
                            work_time=90;
                            break;
                        case 6:
                            work_time=120;
                            break;
                        default:
                            work_time=45;
                            break;
                    }
                    String sec = getResources().getString(R.string.sec);
                    myTextView.setText(String.valueOf(work_time)+sec);

                    SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("work_time_pos", work_time_pos);
                    editor.apply();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        if (mySwitch != null) {
            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    work_bool = isChecked;

                    SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("work_bool", work_bool);
                    editor.apply();
                }
            });
        }

        activity_bool=true;

        Intent intent1 = new Intent(this, WiFi_Service.class);
        startService(intent1);
    }

    @Override
    protected void onDestroy() {
        activity_bool=false;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
}
