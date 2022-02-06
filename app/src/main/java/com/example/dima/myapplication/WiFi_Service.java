package com.example.dima.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static android.provider.Settings.System.WIFI_SLEEP_POLICY;
import static com.example.dima.myapplication.ScrollingActivity.activity_bool;
import static com.example.dima.myapplication.ScrollingActivity.mySeekBar;
import static com.example.dima.myapplication.ScrollingActivity.mySwitch;
import static com.example.dima.myapplication.ScrollingActivity.myTextView;
import static com.example.dima.myapplication.ScrollingActivity.work_bool;
import static com.example.dima.myapplication.ScrollingActivity.work_time;
import static com.example.dima.myapplication.ScrollingActivity.work_time_pos;
import static com.example.dima.myapplication.Utils.getDiskCacheDir;

public class WiFi_Service extends Service {

    public static final String PREFS_NAME = "MyPrefsFile";
    static String file_log;
    static boolean screen_on_off=true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        boolean b=false;
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            boolean pros=false;
            if (action.equalsIgnoreCase(ACTION_SCREEN_ON))
            {
                screen_on_off=true;
                pros=true;
            }
            if (action.equalsIgnoreCase(ACTION_SCREEN_OFF))
            {
                screen_on_off=false;
            }
            //wrireLogFile("vvv");
            Log.d("ttt", "rec");
            boolean pr1=screen_on_off;
            int z=Settings.System.getInt(getContentResolver(),WIFI_SLEEP_POLICY,-1);
            if(z==2)
            {
                pr1=true;
            }
            if(!b && work_bool && pr1) {
                b=true;
                int time=work_time*1000;
                if(pros)
                    time=10000;
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ttt", "time_out");
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo info = wifiManager.getConnectionInfo();
                        int ssid = info.getLinkSpeed();
                        if (ssid == -1) {
                            boolean wifiEnabled = wifiManager.isWifiEnabled();
                            if (wifiEnabled) {
                                wifiManager.setWifiEnabled(false);
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "WiFi-Close!", Toast.LENGTH_SHORT);
                                toast.show();
                                Log.d("ttt", "off");
                                //wrireLogFile("false");
                            }
                        }
                        b=false;
                        //wrireLogFile("del_30");
                    }
                }, time);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void wrireLogFile(String s) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        s=currentDateTimeString+" : "+s;
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file_log, true)));
            out.println(s);
            out.close();
        } catch (IOException v) {
            //exception handling left as an exercise for the reader
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ttt", "onStartCommand");
        File f=getDiskCacheDir(this,"log");
        boolean b=f.exists();
        if (!b) {
            f.mkdirs();
        }
        File fileLog = new File(f, "log.log");
        file_log=fileLog.getAbsolutePath();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        screen_on_off = pm.isScreenOn();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        work_bool = settings.getBoolean("work_bool", false);
        work_time_pos = settings.getInt("work_time_pos", 3);
        switch(work_time_pos) {
            case 0:
                work_time=10;
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
        if(activity_bool) {
            mySwitch.setChecked(work_bool);
            mySeekBar.setProgress(work_time_pos);
            String sec = getResources().getString(R.string.sec);
            myTextView.setText(String.valueOf(work_time)+sec);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ACTION_SCREEN_ON);
        intentFilter.addAction(ACTION_SCREEN_OFF);
        registerReceiver(receiver, intentFilter);

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onCreate() {
        Log.d("ttt", "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("ttt", "onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
        Intent intent1 = new Intent(this, WiFi_Service.class);
        this.startService(intent1);
    }

}
