package com.example.deepak.savetime;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class PhoneListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){


                context.stopService(new Intent(context,MyService.class));

            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){

                Intent i=new Intent(context,MyService.class);
                context.startService(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }




}
