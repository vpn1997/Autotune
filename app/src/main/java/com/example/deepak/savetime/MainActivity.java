package com.example.deepak.savetime;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class MainActivity extends AppCompatActivity {

    MyReceiver myReceiver;
    MyCReceiver myCReceiver;
    ToggleButton tButton;
    TextView cIntensity,aIntensity;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    int i=0;
    int datapassed;
    int data;
    private LineGraphSeries<DataPoint> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestP();
        startBroadcast();
           notification();

        tButton=(ToggleButton)findViewById(R.id.toggleButton);
        tButton.setChecked(true);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tButton.isChecked()){
                    startService(new Intent(MainActivity.this, MyService.class));
                   // tButton.setBackgroundColor(Color.RED);
                    startBroadcast();


                }
                else{
                    stopService(new Intent(MainActivity.this, MyService.class));
                     //tButton.setBackgroundColor(Color.GREEN);
                    stopBroadcast();

                }
            }
        });


        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(33000);
        viewport.setScrollable(true);



//        AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
//            public void onAudioFocusChange(int focusChange) {
//                stopService(new Intent(getApplicationContext(),MyService.class));
//                AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//                am.abandonAudioFocus(null);
//
//            }
//    };


//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        PhoneStateListener phoneStateListener = new PhoneStateListener() {
//
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//
//                String number = incomingNumber;
//                if (state == TelephonyManager.CALL_STATE_RINGING) {
//                    Toast.makeText(getApplicationContext(), "Ringing",
//                            Toast.LENGTH_SHORT).show();
//                    callState=1;
//
//                }
//
//                if(state == TelephonyManager.CALL_STATE_OFFHOOK){
//                    Toast.makeText(getApplicationContext(), "Recieved",
//                            Toast.LENGTH_SHORT).show();
//                    callState=2;
//                }
//
//                if(state == TelephonyManager.CALL_STATE_IDLE){
//                    Toast.makeText(getApplicationContext(), "idel State",
//                            Toast.LENGTH_SHORT).show();
//                    callState=0;
//                }
//            }
//
//        };
//        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
//


//
//        final MediaRecorder recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//


//        Timer timer = new Timer();
//
//        timer.scheduleAtFixedRate(new RecorderTask(recorder), 0, 1000);
//        recorder.setOutputFile("/dev/null");
//
//        try {
//            recorder.prepare();
//            recorder.start();
//        }
//        catch(IllegalStateException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//
//        }

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        myCReceiver = new MyCReceiver();
        IntentFilter intentF = new IntentFilter();
        intentF.addAction(MyService.MY_DATA);
        registerReceiver(myCReceiver, intentF);
//
//        private class RecorderTask extends TimerTask {
//            TextView sound = (TextView) findViewById(R.id.decibel);
//            TextView info = (TextView) findViewById(R.id.info);
//            private MediaRecorder recorder;
//            int i = 0;
//            int sum = 0;
//            int sumPer = 0;
//            int volPer = 0;
//
//            public RecorderTask(MediaRecorder recorder) {
//                this.recorder = recorder;
//            }
//
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int amplitude = recorder.getMaxAmplitude();
//                        double amplitudeDb = 20 * Math.log10((double) Math.abs(amplitude));
//                        double samp = amplitudeDb * .75;
//                        sound.setText("" + amplitude);
//                        volPer = amplitude / 32000;
//                        if (i < 6) {
//                            sum += amplitude;
//                        } else {
//                            info.setText("" + Integer.toString(sum / 5));
//                            Thread.currentThread().interrupted();
//                            sumPer = (sum) / (5 * 32500);
//                            int suu = sum / 5;
//                            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                            int p = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) / 7;
//                            int pm = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 7;
//
//                            //   Log.d("call state", String.valueOf(callState));
//
////                      Ringtone oRingtone = RingtoneManager.getRingtone(MainActivity.this, Uri.parse(Settings.System.RINGTONE));
////                        if(oRingtone.isPlaying()){
////                            audioManager.setStreamVolume(AudioManager.STREAM_RING,(p*7),0);
////                        }
////                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
////
////                        Ringtone defaultRingtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
////                        defaultRingtone.play();
////                            if(defaultRingtone.isPlaying()==true){
////                                audioManager.setStreamVolume(AudioManager.STREAM_RING,(p*7),0);
////                                Toast.makeText(MainActivity.this,""+ defaultRingtone.toString(),Toast.LENGTH_SHORT).show();
////
////                            }
//
//                            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL &&
//                                    audioManager.isMusicActive() == false && audioManager.isSpeakerphoneOn() == false &&
//                                    callState == 0
//                                    ) {
//
//                                SeekBar seekBar = (SeekBar) findViewById(R.id.volumeBar);
//                                seekBar.setMinimumHeight(0);
//                                seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
//
//                                if (suu <= 1000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 1), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 1), 0);
//
//                                    seekBar.setProgress(p * 1);
//                                } else if (suu > 1000 && suu <= 2000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 2), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 2), 0);
//
//                                    seekBar.setProgress(p * 2);
//                                } else if (suu > 2000 && suu <= 4000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 3), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 3), 0);
//
//                                    seekBar.setProgress(p * 3);
//                                } else if (suu > 4000 && suu <= 8000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 4), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 4), 0);
//
//                                    seekBar.setProgress(p * 4);
//                                } else if (suu > 8000 && suu <= 12000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 5), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 5), 0);
//
//                                    seekBar.setProgress(p * 5);
//                                } else if (suu > 12000 && suu <= 15000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 6), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 6), 0);
//
//                                    seekBar.setProgress(p * 6);
//                                } else if (suu > 15000 && suu <= 18000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 7), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 7), 0);
//
//                                    seekBar.setProgress(p * 7);
//                                } else if (suu > 18000) {
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 7), 0);
//                                    audioManager.setStreamVolume(AudioManager.STREAM_RING, (pm * 7), 0);
//
//                                    audioManager.shouldVibrate(AudioManager.VIBRATE_TYPE_RINGER);
//                                    seekBar.setProgress(p * 7);
//                                }
//
//
//                            }
//
//                            if (i < 10) {
//                                sum = 0;
//                                i = 0;
//                            }
//
//                        }
//                        i++;
//
//
//                    }
//                });
//            }
//        }


        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

//      //  SeekBar seekBar = (SeekBar) findViewById(R.id.volumeBar);
//         seekBar.setMinimumHeight(0);
//          seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));



    }

    private void notification() {

        notification = new NotificationCompat.Builder(this);
       notification.setSmallIcon(R.drawable.sound);
        notification.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.sound));
        notification.setTicker("Auto Volume");
        notification.setOngoing(true);
        notification.setContentTitle("Go to Auto Volume");
        notification.setWhen(System.currentTimeMillis());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());

    }

    private void stopBroadcast() {
        PackageManager pm  = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, PhoneListener.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void startBroadcast() {
        PackageManager pm  = MainActivity.this.getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, PhoneListener.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }


    private class MyCReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

             datapassed = intent.getIntExtra("CurrentNoise", 0);
                cIntensity=(TextView)findViewById(R.id.currentIntensity);
                cIntensity.setText(""+datapassed);

        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            data = intent.getIntExtra("AvgNoise", 0);
            aIntensity=(TextView)findViewById(R.id.avgIntensity);
            aIntensity.setText(""+data);
             setSeekBarLevel(data);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        unregisterReceiver(myCReceiver);

        Log.d("activity", "Main Activity destroyed");

    }


    private void setSeekBarLevel(int getdata) {

       int  suu=getdata;
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
           boolean b=audioManager.isWiredHeadsetOn();
        boolean mb=audioManager.isMusicActive();
        int pr = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) / 7;
        int pm = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 15;
      //  int pc = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)/6;

        SeekBar callSeek = (SeekBar) findViewById(R.id.callSeek);
        callSeek.setMinimumHeight(1);
        callSeek.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
        callSeek.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        SeekBar ringSeek = (SeekBar) findViewById(R.id.ringSeek);
        ringSeek.setMinimumHeight(0);
        ringSeek.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        ringSeek.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        SeekBar mediaSeek = (SeekBar) findViewById(R.id.mediaSeek);
        mediaSeek.setMinimumHeight(0);
        mediaSeek.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mediaSeek.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });




       if(audioManager.getRingerMode()!=AudioManager.RINGER_MODE_NORMAL){
           ringSeek.setProgress(0);
       }

        if ((audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL &&
                ((audioManager.isMusicActive() == false ) || (
                        audioManager.isMusicActive() == true && b == true))
                )) {


            if (suu <= 1000) {

                if (b&&mb) {
                    callSeek.setProgress(1);
                    ringSeek.setProgress(pr);
                    mediaSeek.setProgress(pm*8);
                }
                else {
                    callSeek.setProgress(1);
                    ringSeek.setProgress(pr);
                    mediaSeek.setProgress(3*pm);
                }
            } else if (suu > 1000 && suu <= 2000) {

                if (b&&mb) {
                    callSeek.setProgress(2);
                    ringSeek.setProgress(2*pr);
                    mediaSeek.setProgress(8*pm);
                }
                else {
                    callSeek.setProgress(2);
                    ringSeek.setProgress(2*pr);
                    mediaSeek.setProgress(4*pm);

                }
            } else if (suu > 2000 && suu <= 4000) {

                if (b&&mb) {
                    callSeek.setProgress(3);
                    ringSeek.setProgress(3*pr);
                    mediaSeek.setProgress(9*pm);
                }
                else {
                    callSeek.setProgress(3);
                    ringSeek.setProgress(3*pr);
                    mediaSeek.setProgress(6*pm);
                }
            } else if (suu > 4000 && suu <= 6000) {

                if (b&&mb) {
                    callSeek.setProgress(4);
                    ringSeek.setProgress(4*pr);
                    mediaSeek.setProgress(9*pm);
                }
                else {
                    callSeek.setProgress(4);
                    ringSeek.setProgress(4*pr);
                    mediaSeek.setProgress(8*pm);

                }
            } else if (suu > 6000 && suu <= 8000) {

                if (b&&mb) {
                    callSeek.setProgress(5);
                    ringSeek.setProgress(5*pr);
                    mediaSeek.setProgress(10*pm);
                }
                else {
                    callSeek.setProgress(5);
                    ringSeek.setProgress(5*pr);
                    mediaSeek.setProgress(10*pm);

                }
            } else if (suu > 8000 && suu <= 12000) {

                if (b&&mb) {
                    callSeek.setProgress(6);
                    ringSeek.setProgress(6*pr);
                    mediaSeek.setProgress(12*pm);
                }
                else {
                    callSeek.setProgress(6);
                    ringSeek.setProgress(6*pr);
                    mediaSeek.setProgress(12*pm);

                }
            } else if (suu > 12000 && suu <= 16000) {

                if (b&&mb) {
                    callSeek.setProgress(7);
                    ringSeek.setProgress(6*pr);
                    mediaSeek.setProgress(13*pm);
                }
                else {
                    callSeek.setProgress(7);
                    ringSeek.setProgress(6*pr);
                    mediaSeek.setProgress(14*pm);

                }
            } else if (suu > 16000) {

                if (b&&mb) {
                    callSeek.setProgress(8);
                    ringSeek.setProgress(7*pr);
                    mediaSeek.setProgress(15*pm);
                }
                else {
                    callSeek.setProgress(8);
                    ringSeek.setProgress(7*pr);
                    mediaSeek.setProgress(15*pm);

                }
            }


        }
     }



    private void addEntry() {

        series.appendData(new DataPoint(i++,datapassed), false,10);


    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    public void requestP() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , 10);
            }

        } else
            startService(new Intent(this, MyService.class));

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, MyService.class));
                }
                else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Please Allow Permissions", Toast.LENGTH_LONG).show();

                    finish();
                    System.exit(0);
                }
                break;
            }
            default:
                break;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_about: {

                Intent k = new Intent(MainActivity.this, About.class);
                startActivity(k);
            }
            break;
            default:
                break;
        }
        return false;
    }
}