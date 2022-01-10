package com.sprobe.aichat_library.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sprobe.aichat_library.api.AiChatService;
import com.sprobe.aichat_library.receiver.StartBroadcastAfterAMinuteReceiver;

import retrofit.RestAdapter;

public class AiChatHelper {

    private static final String TAG = AiChatHelper.class.getSimpleName();

    private Fragment mFragment;

    private int mUUID;

    private int mQuestionNumberCounter;

    public static AiChatService getService() {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build();

        return restAdapter.create(AiChatService.class);
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void setUUID(int UUID) {
        mUUID = UUID;
    }

    public int getUUID() {
        return mUUID;
    }

    public void setQuestionNUmberCounter(int counter) {
        mQuestionNumberCounter = counter;
    }

    public int getQuestionNumberCounter() {
        return mQuestionNumberCounter;
    }

    public void goToAiChatFragment(FragmentManager fragmentManager, int containerViewId, int UUID, Context context) {
        fragmentManager.beginTransaction()
                .add(containerViewId, mFragment)
                .disallowAddToBackStack()
                .commit();
    }

    /**
     * 現在の時刻をミリ秒単位で取得する.
     * @return 現在時刻ミリ秒
     */
    public static long getCurrentTimeMillis() {

        return System.currentTimeMillis();

    }

    public static void lastTimeClicked(Context context, int startPendingIntent) {
        Log.d(TAG, "lastTimeClicked");

        boolean alarmUp = (PendingIntent.getBroadcast(context, startPendingIntent,
                new Intent(context, StartBroadcastAfterAMinuteReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp) {
            // Cancel Alarm
            Log.d(TAG, "Cancel Alarm");
            Intent cancelAlarmIntent = new Intent(context, StartBroadcastAfterAMinuteReceiver.class);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context,
                    startPendingIntent, cancelAlarmIntent, 0);
            AlarmManager cancelAlarmMngr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            cancelAlarmMngr.cancel(cancelPendingIntent);

        }

        // Set Alarm to 1 min after last clicked
        Log.d(TAG, "Set Alarm");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, StartBroadcastAfterAMinuteReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, startPendingIntent, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);

    }


}
