package com.lge.systraceapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver{
    private static final String TAG = "SystraceApp:AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //    if (MainActivity.btn_status == 0){
                Intent in = new Intent(context, RestartService.class);
                context.startForegroundService(in);
    //        }
        } else {
            Intent in = new Intent(context, Systrace_service.class);
            context.startService(in);
        }
    }

}
