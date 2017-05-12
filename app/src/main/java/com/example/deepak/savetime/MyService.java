package com.example.deepak.savetime;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    Timer mTimer;
    MediaRecorder recorder;
     Handler mHandler;
    final static String MY_ACTION = "MY_ACTION";
    final static String MY_DATA = "MY_DATA";


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();


        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");


        mTimer = new Timer();
        mTimer.schedule(new MyTimer(recorder),0,1000);

        try {
            if(recorder!=null){
                recorder.prepare();
                recorder.start();
            }

        }
        catch(IllegalStateException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }




    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            Log.d("start","service Running");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.START_STICKY;
    }



     public class MyTimer extends TimerTask {
         private MediaRecorder recorder;
         int i = 0;
         int sum = 0;
         int sumPer = 0;
         int volPer = 0;


         public MyTimer(MediaRecorder recorder) {
             this.recorder = recorder;
         }

         @Override
         public void run() {

             Intent intent = new Intent();
             intent.setAction(MY_ACTION);

             Intent currentI = new Intent();
             currentI.setAction(MY_DATA);

             int amplitude = recorder.getMaxAmplitude();
             double amplitudeDb = 20 * Math.log10((double) Math.abs(amplitude));


             currentI.putExtra("CurrentNoise", amplitude);
             sendBroadcast(currentI);

             Log.d("avg", String.valueOf(amplitude));

             volPer = amplitude / 32000;

             if (i < 8) {
                 sum += amplitude;
             } else {
                 Thread.currentThread().interrupted();
                 int suu = sum / 7;
                 AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


                 intent.putExtra("AvgNoise", suu);
                 sendBroadcast(intent);

                 int p = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) / 7;
                 int pm = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 15;
                // int pc = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)/6;

                 AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                 boolean b = am.isWiredHeadsetOn();
                 boolean mb=am.isMusicActive();

                 if ((audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL &&
                         ((audioManager.isMusicActive() == false ) || (
                                 audioManager.isMusicActive() == true && b == true))
                 )) {


                     if (suu <= 1000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (1), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 1), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 8), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (1), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 1), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 3), 0);

                         }
                     } else if (suu > 1000 && suu <= 2000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 2), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 2), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 8), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (2), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 2), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 4), 0);

                         }
                     } else if (suu > 2000 && suu <= 4000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (3), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 3), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 9), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 3), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 3), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 6), 0);

                         }
                     } else if (suu > 4000 && suu <= 6000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (4), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 4), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 9), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 4), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 4), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 8), 0);

                         }
                     } else if (suu > 6000 && suu <= 8000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 5), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 5), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 10), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 5), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 5), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 10), 0);

                         }
                     } else if (suu > 8000 && suu <= 12000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 6), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 6), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 12), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 6), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 6), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 12), 0);

                         }
                     } else if (suu > 12000 && suu <= 16000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 7), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 6), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 13), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (7), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 6), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 14), 0);

                         }
                     } else if (suu > 16000) {

                         if (b&&mb) {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 8), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 7), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 15), 0);
                         }
                         else {
                             audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, ( 8), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_RING, (p * 7), 0);
                             audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (pm * 15), 0);

                         }
                     }
                    sum=0;i=0;

                 }
             }
                 i++;
             }
         }

    public void onDestroy() {
        try {
            mTimer.cancel();
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("service","Service Destroyed");
        Intent intent = new Intent("com.example.deepak.savetime");
        intent.putExtra("yourvalue", "dee_man");
        sendBroadcast(intent);
    }



}
