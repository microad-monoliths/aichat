package com.sprobe.aichat_library.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sprobe.aichat_library.api.AiChatService;
import com.sprobe.aichat_library.model.FollowResponse;
import com.sprobe.aichat_library.receiver.StartBroadcastAfterAMinuteReceiver;

import java.util.ArrayList;

import retrofit.RestAdapter;

public class AiChatHelper {

    private static final String TAG = AiChatHelper.class.getSimpleName();

    private static Fragment mFragment;

    private static String mUUID;

    private static int mQuestionNumberCounter;

    public static AiChatService getService() {

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter.create(AiChatService.class);
    }

    public static void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public static void setUUID(String UUID) {
        mUUID = UUID;
    }

    public static String getUUID() {
        return mUUID;
    }

    public static void setQuestionNUmberCounter(int counter) {
        mQuestionNumberCounter = counter;
    }

    public static int getQuestionNumberCounter() {
        return mQuestionNumberCounter;
    }

    public static void goToAiChatFragment(FragmentManager fragmentManager, int containerViewId, int UUID, Context context) {
        fragmentManager.beginTransaction()
                .add(containerViewId, mFragment)
                .disallowAddToBackStack()
                .commit();
    }

    /**
     * 現在の時刻をミリ秒単位で取得する.
     * @return 現在時刻ミリ秒
     */
    public static String getCurrentTimeMillis() {

        return String.valueOf(System.currentTimeMillis());

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

    private ArrayList<ArrayList<FollowResponse.Chips>> chipsToArray(ArrayList<FollowResponse.Chips> chips) {
        Log.d(TAG, "chipsToArray");
        ArrayList<ArrayList<FollowResponse.Chips>> chipsResultList = new ArrayList<>();
        ArrayList<FollowResponse.Chips> chipsResult = new ArrayList<>();

        for (int i = 0; i < chips.size(); i++) {
            if (chipsResult.size() != 1) {
                chipsResult.add(chips.get(i));
                chips.get(i).setSelected(false);
                if (i + 1 == chips.size()) {
                    chipsResultList.add(chipsResult);
                    chipsResult = new ArrayList<>();
                }
            } else {
                chipsResult.add(chips.get(i));
                chips.get(i).setSelected(false);
                chipsResultList.add(chipsResult);
                chipsResult = new ArrayList<>();
            }
        }

        return chipsResultList;
    }

}
