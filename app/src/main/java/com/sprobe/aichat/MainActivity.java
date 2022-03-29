package com.sprobe.aichat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sprobe.aichat_library.ui.view.ChatView;
import com.sprobe.aichat_library.utils.SharedPreferenceUtils;

import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_PREFERENCE_VALUE;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_SEGMENT_VALUE;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_USER_ID_VALUE;
import static com.sprobe.aichat_library.utils.Constants.DEVICE_ID_TEXT_VALUE;
import static com.sprobe.aichat_library.utils.Constants.ORIGINAL_PLATFORM_ID_TEXT_VALUE;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.AI_CHAT_PREFERENCE;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.AI_CHAT_SEGMENT;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.AI_CHAT_USER_ID;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.DEVICE_ID;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.ORIGINAL_PLATFORM_ID;

public class MainActivity extends AppCompatActivity {

    ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferenceUtils mPreference = SharedPreferenceUtils.getInstance(this);

        mPreference.setValue(DEVICE_ID, DEVICE_ID_TEXT_VALUE);
        mPreference.setValue(AI_CHAT_PREFERENCE, AI_CHAT_PREFERENCE_VALUE);
        mPreference.setValue(AI_CHAT_SEGMENT, AI_CHAT_SEGMENT_VALUE);
        mPreference.setValue(AI_CHAT_USER_ID, AI_CHAT_USER_ID_VALUE);
        mPreference.setValue(ORIGINAL_PLATFORM_ID, ORIGINAL_PLATFORM_ID_TEXT_VALUE);

        chatView = findViewById(R.id.chatView);

//        chatView.setFirstLeftImage(R.drawable.clear_image);
//        chatView.doAnimateView(false);
//        chatView.setHeaderText("Hello World");
//        chatView.setBigTitleText("Hello World");
//        chatView.setSubtitleOneText("Hello World");
//        chatView.setSubtitleTwoText("Hello World");
//        chatView.setLogoImage(R.drawable.arrow_icon);
//        chatView.setStartButtonGradient(getResources().getDrawable(R.drawable.chip_button_gradient_normal, null));
//        chatView.setChipGradient(getResources().getDrawable(R.drawable.chip_button_gradient_normal, null));
        /*Typeface typeface = ResourcesCompat.getFont(this, R.font.academy_engraved_std_regular);
        chatView.setChipFont(typeface);*/
//        chatView.setChipTextSize(15f);
        /*Typeface typeface = ResourcesCompat.getFont(this, R.font.noto_sans_jp_regular);
        chatView.setQuestionNumberFont(typeface);*/
        /*Typeface typeface = ResourcesCompat.getFont(this, R.font.noto_sans_jp_regular);
        chatView.setTextFont(typeface);*/
//        chatView.setLoadingText("Hello World");
//       chatView.setSkincareImage(R.drawable.astalift_image_new);
//        chatView.setRatingBarColor(ContextCompat.getColor(this, R.color.black));
//        chatView.setRatingBackgroundColor(getDrawable(R.color.black));
//        chatView.setItemNumberOneImage(R.drawable.arrow_icon);
//        chatView.setItemNumberThreeImage(R.drawable.arrow_icon);
//        chatView.setItemNumberTwoImage(R.drawable.arrow_icon);
//        chatView.setFinalButtonColor(getResources().getDrawable(R.drawable.chip_button_gradient_normal, null));
//        chatView.setFinalButtonText("Finish Hello World");
//        chatView.setThankYouTitleText("Thank You Hello World!");
//        chatView.setThankYouSubtitleText("Bye bye");
        /*Typeface typeface = ResourcesCompat.getFont(this, R.font.noto_sans_jp_regular);
        chatView.setThankYouTitleFont(typeface);
        Typeface typeface1 = ResourcesCompat.getFont(this, R.font.academy_engraved_std_regular);
        chatView.setThankYouSubtitleFont(typeface1);*/

    }
}