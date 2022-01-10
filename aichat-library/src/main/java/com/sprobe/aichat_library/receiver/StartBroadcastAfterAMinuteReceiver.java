package com.sprobe.aichat_library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sprobe.aichat_library.event.StartBroadcastAfterAMinuteEvent;

import org.greenrobot.eventbus.EventBus;


public class StartBroadcastAfterAMinuteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new StartBroadcastAfterAMinuteEvent());
    }
}
