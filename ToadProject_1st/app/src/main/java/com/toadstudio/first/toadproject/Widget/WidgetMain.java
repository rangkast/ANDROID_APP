package com.toadstudio.first.toadproject.Widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.toadstudio.first.toadproject.MainActivity;
import com.toadstudio.first.toadproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WidgetMain  extends AppWidgetProvider
{
    private static final String TAG = "Toad_Widget";
    private static final int WIDGET_UPDATE_INTERVAL = 30000;
    private static PendingIntent mSender;
    private static AlarmManager mManager;

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        String action = intent.getAction();
        // 위젯 업데이트 인텐트를 수신했을 때
        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
        {
    //        Log.d(TAG, "android.appwidget.action.APPWIDGET_UPDATE");
            removePreviousAlarm();

            long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
            mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
            mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mManager.set(AlarmManager.RTC, firstTime, mSender);
        }
        // 위젯 제거 인텐트를 수신했을 때
        else if(action.equals("android.appwidget.action.APPWIDGET_DISABLED"))
        {
      //      Log.d(TAG, "android.appwidget.action.APPWIDGET_DISABLED");
            removePreviousAlarm();
        }
    }

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // 현재 클래스로 등록된 모든 위젯의 리스트를 가져옴
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for(int i = 0 ; i < N ; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);

    //        Toast.makeText(context, "onUpdate(): [" + String.valueOf(i) + "] " + String.valueOf(appWidgetId), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 위젯의 형태를 업데이트합니다.
     *
     * @param context 컨텍스트
     * @param appWidgetManager 위젯 메니저
     * @param appWidgetId 업데이트할 위젯 아이디
     */
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        //Date now = new Date();

        String today = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
     //   updateViews.setTextViewText(R.id.widgettext,  today);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", "WidgetMain");
        PendingIntent pe = PendingIntent.getActivity(context, 0, intent, 0);
        updateViews.setOnClickPendingIntent(R.id.imageButton_my, pe);

        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    /**
     * 예약되어있는 알람을 취소합니다.
     */
    public void removePreviousAlarm()
    {
        if(mManager != null && mSender != null)
        {
            mSender.cancel();
            mManager.cancel(mSender);
        }
    }
}
