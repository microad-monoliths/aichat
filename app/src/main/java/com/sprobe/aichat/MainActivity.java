package com.sprobe.aichat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sprobe.aichat_library.model.FollowRequest;
import com.sprobe.aichat_library.model.FollowResponse;
import com.sprobe.aichat_library.ui.view.ChatView;
import com.sprobe.aichat_library.utils.AiChatHelper;
import com.sprobe.aichat_library.utils.SharedPreferenceUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_PREFERENCE;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_SEGMENT;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_USER_ID;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.MONOLITHS_DISPLAY_ID_PLAIN_TEXT;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.QUESTION_NUMBER_COUNTER;

public class MainActivity extends AppCompatActivity {

    ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferenceUtils mPreference = SharedPreferenceUtils.getInstance(this);

        mPreference.setValue(MONOLITHS_DISPLAY_ID_PLAIN_TEXT, "317493dc-f597-4f1e-a764-9b258e1ffee8");

        mPreference.setValue(QUESTION_NUMBER_COUNTER, 0);

        chatView = findViewById(R.id.chatView);

        chatView.setOnCLickBackToTopButtonListener(new ChatView.OnCLickBackToTopButtonListener() {
            @Override
            public void onBackToTopButtonClick(int position) {

            }
        });

        FollowRequest.Attributes attributes = new FollowRequest.Attributes();
        attributes.setPreferences(AI_CHAT_PREFERENCE);
        attributes.setSegment(AI_CHAT_SEGMENT);

        FollowRequest.User user = new FollowRequest.User();
        user.setAttributes(attributes);
        user.setId(AI_CHAT_USER_ID);

        FollowRequest.Event eventRequest = new FollowRequest.Event();
        eventRequest.setTimestamp(AiChatHelper.getCurrentTimeMillis());
        eventRequest.setUser(user);

        FollowRequest request = new FollowRequest();
        request.setEvent(eventRequest);

        //For monolith and tico's case DISPLAY ID is used in the Follow API parameter originalPlatformId
        request.setOriginalPlatformId(mPreference.getStringValue(MONOLITHS_DISPLAY_ID_PLAIN_TEXT, null));

        AiChatHelper.getService().follow(request, new Callback<FollowResponse>() {
            @Override
            public void success(FollowResponse followResponse, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}