package com.sprobe.aichat_library.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobe.aichat_library.R;
import com.sprobe.aichat_library.model.ChatModel;
import com.sprobe.aichat_library.model.FollowRequest;
import com.sprobe.aichat_library.model.FollowResponse;
import com.sprobe.aichat_library.model.ReplyRequest;
import com.sprobe.aichat_library.model.ReplyResponse;
import com.sprobe.aichat_library.ui.adapter.AiChatHostAdapter;
import com.sprobe.aichat_library.utils.AiChatHelper;
import com.sprobe.aichat_library.utils.SharedPreferenceUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sprobe.aichat_library.ui.fragment.AiChatFragment.chipsToArray;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_PREFERENCE_VALUE;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_SEGMENT_VALUE;
import static com.sprobe.aichat_library.utils.Constants.AI_CHAT_USER_ID_VALUE;
import static com.sprobe.aichat_library.utils.Constants.MONOLITHS_DISPLAY_ID_PLAIN_TEXT_VALUE;
import static com.sprobe.aichat_library.utils.Constants.TOP_OF_CHAT_LIST;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.AI_CHAT_PREFERENCE;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.AI_CHAT_SEGMENT;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.AI_CHAT_USER_ID;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.MONOLITHS_DISPLAY_ID_PLAIN_TEXT;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.QUESTION_NUMBER_COUNTER;

public class ChatView extends RelativeLayout implements AiChatHostAdapter.Callback {

    protected static final String TAG = ChatView.class.getSimpleName();

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected RelativeLayout mRootLayout;
    protected RecyclerView mChatRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected TextView mBackToTopTextView;
    protected TextView mQuestionNumberTextView;
    protected TextView mQuestionNumber7TextView;
    protected ImageView mLogoImageView;
    protected ArrayList<ChatModel> mChatList;
    protected ArrayList<ChatModel.ThankYou> mThankYouList;
    protected AiChatHostAdapter mAiChatHostAdapter;
    private ReplyRequest mLastChipRequest;

    protected int mDiagnosisLastPosition = 0;
    protected int mLastPosition = 0;
    protected SharedPreferenceUtils mPreference;
    protected FollowRequest.Attributes mAttributes;
    protected FollowRequest.User mUser;
    protected FollowRequest.Event mEvent;
    protected String mOriginalPlatformId;

    public ChatView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ChatView,
                0, 0);
        setAttributes(a);
        a.recycle();
    }

    protected void init(Context context) {

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        // load rootview from xml
        View rootView = mLayoutInflater.inflate(R.layout.view_chatview, this, true);

        // initialize UI
        mChatRecyclerView = rootView.findViewById(R.id.recycler_view);
        mBackToTopTextView = rootView.findViewById(R.id.backToTop_textView);
        mQuestionNumberTextView = rootView.findViewById(R.id.questionNumber_textView);
        mQuestionNumber7TextView = rootView.findViewById(R.id.questionNumber7_textView);
        mLogoImageView = rootView.findViewById(R.id.logo_ImageView);

        mPreference = SharedPreferenceUtils.getInstance(context);

        mPreference.setValue(QUESTION_NUMBER_COUNTER, 0);
        mOriginalPlatformId = mPreference.getStringValue(MONOLITHS_DISPLAY_ID_PLAIN_TEXT, MONOLITHS_DISPLAY_ID_PLAIN_TEXT_VALUE);

        mChatRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(false);
        mLayoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(mLayoutManager);

        mChatList = new ArrayList<>();
        mThankYouList = new ArrayList<>();
        mAiChatHostAdapter = new AiChatHostAdapter();
        mAiChatHostAdapter.setCallback(this);
        mAiChatHostAdapter.setContext(context);
        mChatRecyclerView.setAdapter(mAiChatHostAdapter);

        showTitle();

        mBackToTopTextView.setOnClickListener(v -> mChatRecyclerView.smoothScrollToPosition(TOP_OF_CHAT_LIST));

        // Updates text of Question Number TextView
        // Also handles visibility of back to top and question number text views
        mChatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int recyclePosition = ((LinearLayoutManager) Objects.requireNonNull(mChatRecyclerView.getLayoutManager()))
                            .findFirstVisibleItemPosition();
                    if (recyclePosition != mLastPosition && recyclePosition != 0) {
                        mBackToTopTextView.setVisibility(View.VISIBLE);
                        if (recyclePosition <= mDiagnosisLastPosition) {
                            mQuestionNumberTextView.setText(MessageFormat.format("Q{0}", recyclePosition));
                            mQuestionNumberTextView.setVisibility(View.VISIBLE);
                            mQuestionNumber7TextView.setVisibility(View.VISIBLE);
                        } else {
                            mQuestionNumberTextView.setVisibility(View.GONE);
                            mQuestionNumber7TextView.setVisibility(View.GONE);
                        }
                    } else {
                        mBackToTopTextView.setVisibility(View.GONE);
                        mQuestionNumberTextView.setVisibility(View.GONE);
                        mQuestionNumber7TextView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onStartClick(ArrayList<ChatModel> list) {
        Log.d(TAG, "onStartClick");
        Log.d(TAG, "isClicked " + list.get(0).isClicked());
        if (!list.get(0).isClicked()) {
            sendFollowAPI();
        }
    }

    @Override
    public void onChipItemClick(ReplyRequest.Event event, ArrayList<ChatModel> chatModelArrayList) {
        Log.d(TAG, "onChipItemClick");
        // Update List
        mChatList.clear();
        mChatList.addAll(chatModelArrayList);

        event.setUser(mUser);
        event.setTimestamp(AiChatHelper.getCurrentTimeMillis());

        ReplyRequest request = new ReplyRequest();
        request.setEvent(event);
        request.setOriginalPlatformId(mOriginalPlatformId);

        mLastChipRequest = request;
        if (getQuestionNumberCounter() <= 7) {
            sendReplyAPI(request);
        } else {
            // Dummy Recommendation
            Log.d(TAG, "add recommendation");
            ChatModel chatModel = new ChatModel();
            chatModel.setMessageId(null);
            chatModel.setChips(null);
            chatModel.setImageUri(null);
            chatModel.setText(null);
            chatModel.setType("recommendation");

            mQuestionNumberTextView.setVisibility(View.GONE);
            mQuestionNumber7TextView.setVisibility(View.GONE);

            mAiChatHostAdapter.addChatMessage(chatModel);
            mAiChatHostAdapter.notifyItemInserted(mAiChatHostAdapter.getItemCount() - 1);
            mChatRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);
//            mRecyclerView.setNestedScrollingEnabled(false);
            mChatRecyclerView.setOnTouchListener((v, event1) -> true);

            mLastChipRequest = request;
            sendReplyAPI(request);
        }
    }

    @Override
    public void scrollToPosition(int position) {
        Log.d(TAG, "scrollToPosition " + position);
        mLayoutManager.scrollToPositionWithOffset(position, 0);
    }

    @Override
    public void updateLastTimeClicked() {

    }

    @Override
    public void onFinishClicked() {
        Log.d(TAG, "onFinishClicked");

        ChatModel chatModel = new ChatModel();
        chatModel.setThankYouList(mThankYouList);
        chatModel.setType("thank_you");

        mAiChatHostAdapter.remove(mAiChatHostAdapter.getItemCount() - 1);
        mAiChatHostAdapter.addChatMessage(chatModel);
        mAiChatHostAdapter.notifyItemChanged(mAiChatHostAdapter.getItemCount() - 1);
        mChatRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);
        mBackToTopTextView.setVisibility(View.GONE);
        mLastPosition = mAiChatHostAdapter.getItemCount() - 1;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        Log.d(TAG, "smoothScrollToPosition " + position);
        mChatRecyclerView.smoothScrollToPosition(position);
    }

    protected void setAttributes(TypedArray attrs) {

    }

    private void sendFollowAPI() {
        Log.d(TAG, "sendFollowAPI");

        // Set Attributes data
        mAttributes = new FollowRequest.Attributes();
        mAttributes.setPreferences(mPreference.getStringValue(AI_CHAT_PREFERENCE, AI_CHAT_PREFERENCE_VALUE));
        mAttributes.setSegment(mPreference.getStringValue(AI_CHAT_SEGMENT, AI_CHAT_SEGMENT_VALUE));

        // Set User data
        mUser = new FollowRequest.User();
        mUser.setAttributes(mAttributes);
        mUser.setId(mPreference.getStringValue(AI_CHAT_USER_ID, AI_CHAT_USER_ID_VALUE));

        // Set FollowRequest params
        mEvent = new FollowRequest.Event();
        mEvent.setUser(mUser);
        mEvent.setTimestamp(AiChatHelper.getCurrentTimeMillis());

        FollowRequest followRequest = new FollowRequest();
        followRequest.setEvent(mEvent);
        followRequest.setOriginalPlatformId(mPreference.getStringValue(MONOLITHS_DISPLAY_ID_PLAIN_TEXT, MONOLITHS_DISPLAY_ID_PLAIN_TEXT_VALUE));

        Log.d(TAG, "Follow Request Preferences: " + mAttributes.getPreferences());
        Log.d(TAG, "Follow Request Segment: " + mAttributes.getSegment());
        Log.d(TAG, "Follow Request ID: " + mUser.getId());
        Log.d(TAG, "Follow Request TimeStamp: " + mEvent.getTimestamp());
        Log.d(TAG, "Follow Request UUID: " + followRequest.getOriginalPlatformId());

        AiChatHelper.getService().follow(followRequest, new Callback<FollowResponse>() {
            @Override
            public void success(FollowResponse followResponse, Response response) {
                Log.d(TAG, "sendFollowAPI: success");

                setChatModel(followResponse, null);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "sendFollowAPI: failure " + error.getMessage());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Wait for 5 seconds!!");
                        sendFollowAPI();
                    }
                }, 5000);
            }
        });
    }

    private void sendReplyAPI(ReplyRequest replyRequest) {
        Log.d(TAG, "sendReplyRequest");

        Log.d(TAG, "Reply Request OriginalPlatformID: " + replyRequest.getOriginalPlatformId());
        Log.d(TAG, "Reply Request Preferences: " + mAttributes.getPreferences());
        Log.d(TAG, "Reply Request Segment: " + mAttributes.getSegment());
        Log.d(TAG, "Reply Request ID: " + mUser.getId());
        Log.d(TAG, "Reply Request Timestamp: " + replyRequest.getEvent().getTimestamp());
        Log.d(TAG, "Reply Request MessageId: " + replyRequest.getEvent().getMessageId());
        Log.d(TAG, "Reply Request Type: " + replyRequest.getEvent().getType());
        Log.d(TAG, "Reply Request ChipId: " + replyRequest.getEvent().getChip().getId());
        Log.d(TAG, "Reply Request ChipText: " + replyRequest.getEvent().getChip().getText());

        AiChatHelper.getService().reply(replyRequest, new Callback<ReplyResponse>() {
            @Override
            public void success(ReplyResponse replyResponse, Response response) {
                Log.d(TAG, "sendReplyAPI: success");

                setChatModel(null, replyResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "sendReplyAPI: failure " + error.getMessage());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Wait for 5 seconds!!");
                        sendReplyAPI(mLastChipRequest);
                    }
                }, 5000);
            }
        });

    }

    private void setChatModel(FollowResponse followResponse, ReplyResponse replyResponse) {
        Log.d(TAG, "setChatModel");
        mChatList.clear();
        if (followResponse != null) {
            if (followResponse.getMessages() != null) {
                int messageSize = followResponse.getMessages().size();
                for (int i = 0; i < messageSize; i++) {
                    if (followResponse.getMessages().get(i).getType().equalsIgnoreCase("chip")) {
                        int lastChat = mChatList.size() - 1;
                        mChatList.get(lastChat).setType(followResponse.getMessages().get(i).getType());
                        mChatList.get(lastChat).setChips(chipsToArray(followResponse.getMessages().get(i).getChips()));
                        mChatList.get(lastChat).setMessageId(followResponse.getMessages().get(i).getId());
                    } else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setMessageId(followResponse.getMessages().get(i).getId());
                        chatModel.setType(followResponse.getMessages().get(i).getType());
                        chatModel.setText(followResponse.getMessages().get(i).getText());
                        chatModel.setQuestionNumber(getQuestionNumberCounter());
                        chatModel.setLastMessage(followResponse.isLastMessage());
                        mChatList.add(chatModel);
                    }
                }
            }
        } else {
            if (!replyResponse.isLastMessage()) {
                int messageSize = replyResponse.getMessages().size();
                for (int i = 0; i < messageSize; i++) {
                    if (replyResponse.getMessages().get(i).getType().equalsIgnoreCase("chip")) {
                        int lastChat = mChatList.size() - 1;
                        mChatList.get(lastChat).setType(replyResponse.getMessages().get(i).getType());
                        mChatList.get(lastChat).setChips(chipsToArray(replyResponse.getMessages().get(i).getChips()));
                        mChatList.get(lastChat).setMessageId(replyResponse.getMessages().get(i).getId());
                    } else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setMessageId(replyResponse.getMessages().get(i).getId());
                        chatModel.setType(replyResponse.getMessages().get(i).getType());
                        chatModel.setText(replyResponse.getMessages().get(i).getText());
                        chatModel.setQuestionNumber(getQuestionNumberCounter());
                        chatModel.setLastMessage(replyResponse.isLastMessage());
                        mChatList.add(chatModel);
                    }
                }
            } else {
                ChatModel chatModel = new ChatModel();
                chatModel.setMessagesList(replyResponse.getMessages());
                chatModel.setType("recommendation");
                chatModel.setLastMessage(replyResponse.isLastMessage());

                mChatList.add(chatModel);
//                mRecyclerView.setNestedScrollingEnabled(true);
                mChatRecyclerView.setOnTouchListener((v, event) -> false);
            }
        }

        mAiChatHostAdapter.addChatMessage(mChatList.get(0));
        if (mChatList.get(0).getType().equals("chip") && !mChatList.get(0).isLastMessage()) {
            mDiagnosisLastPosition = mAiChatHostAdapter.getItemCount() - 1;
        }
        if (mChatList.get(0).isLastMessage()) {
            mAiChatHostAdapter.remove(mAiChatHostAdapter.getItemCount() - 2);
            mAiChatHostAdapter.notifyItemRemoved(mAiChatHostAdapter.getItemCount() - 1);
            mChatRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);

            for (int i = 0; i < replyResponse.getRecommendations().size(); i++) {
                replyResponse.getRecommendations().get(i).setOrder(i);
                ChatModel chatModel = new ChatModel();
                chatModel.setRecommendationItem(replyResponse.getRecommendations().get(i));
                chatModel.setType("recommendation_items");

                // Add Data into Finish List
                ChatModel.ThankYou thankYou = new ChatModel.ThankYou();
                thankYou.setImageUrl(replyResponse.getRecommendations().get(i).getMetadata().getMainImageUrl());
                thankYou.setItemName(replyResponse.getRecommendations().get(i).getMetadata().getItemName());
                thankYou.setQrCodeUrl(replyResponse.getRecommendations().get(i).getMetadata().getQrCodeUrl());
                thankYou.setOrder(i);

                mThankYouList.add(thankYou);

                mAiChatHostAdapter.addChatMessage(chatModel);
                mAiChatHostAdapter.notifyItemInserted(mAiChatHostAdapter.getItemCount() - 1);
            }

            // Add Finish diagnosis button
            ChatModel chatModel = new ChatModel();
            chatModel.setType("finish");
            mAiChatHostAdapter.addChatMessage(chatModel);
            mAiChatHostAdapter.notifyItemInserted(mAiChatHostAdapter.getItemCount() - 1);

        } else {
            mAiChatHostAdapter.notifyItemInserted(mAiChatHostAdapter.getItemCount() - 1);
            mChatRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);
        }

        if (!mChatList.get(0).isLastMessage()) {
            int counter = mPreference.getIntValue(QUESTION_NUMBER_COUNTER, 1) + 1;
            mPreference.setValue(QUESTION_NUMBER_COUNTER, counter);
        }
    }

    public int getQuestionNumberCounter() {
        return mPreference.getIntValue(QUESTION_NUMBER_COUNTER, 0) + 1;
    }

    private void showTitle() {
        Log.d(TAG, "showTitle");
        ChatModel chatModel = new ChatModel();
        chatModel.setMessageId(null);
        chatModel.setChips(null);
        chatModel.setImageUri(null);
        chatModel.setText(null);
        chatModel.setType("title");
        chatModel.setClicked(false);

        mAiChatHostAdapter.addChatMessage(chatModel);
        mAiChatHostAdapter.notifyDataSetChanged();
    }

    // Customization functions
    // Start Screen
    public void setFirstLeftProductImage(int id) {
        mAiChatHostAdapter.setFirstLeftImage(id);
    }

    public void setSecondLeftProductImage(int id) {
        mAiChatHostAdapter.setSecondLeftImage(id);
    }

    public void setThirdLeftProductImage(int id) {
        mAiChatHostAdapter.setThirdLeftImage(id);
    }

    public void setFirstRightProductImage(int id) {
        mAiChatHostAdapter.setFirstRightImage(id);
    }

    public void setSecondRightProductImage(int id) {
        mAiChatHostAdapter.setSecondRightImage(id);
    }

    public void setThirdRightProductImage(int id) {
        mAiChatHostAdapter.setThirdRightImage(id);
    }

    public void doAnimateView(boolean animate) {
        mAiChatHostAdapter.doAnimateView(animate);
    }

    public void setHeaderText(String text) {
        mAiChatHostAdapter.setHeaderText(text);
    }

    public void setBigTitleText(String text) {
        mAiChatHostAdapter.setBigTitleText(text);
    }

    public void setSubtitleOneText(String text) {
        mAiChatHostAdapter.setSubtitleOneText(text);
    }

    public void setSubtitleTwoText(String text) {
        mAiChatHostAdapter.setSubtitleTwoText(text);
    }

    public void setLogoImage(int id) {
        Picasso.get().load(id).into(mLogoImageView);
    }

    public void setStartButtonGradient(Drawable gradient) {
        mAiChatHostAdapter.setStartButtonGradient(gradient);
    }

    //Diagnosis Screen
    public void setChipGradient(Drawable gradient) {
        mAiChatHostAdapter.setChipGradient(gradient);
    }

    public void setChipFont(Typeface typeface) {
        mAiChatHostAdapter.setChipFont(typeface);
    }

    public void setChipTextSize(float size) {
        mAiChatHostAdapter.setChipSize(size);
    }

    public void setQuestionNumberFont(Typeface typeface) {
        mAiChatHostAdapter.setQuestionNumberFont(typeface);
    }

    public void setTextFont(Typeface typeface) {
        mAiChatHostAdapter.setTextFont(typeface);
    }

    // Loading Screen
    public void setLoadingText(String text) {
        mAiChatHostAdapter.setLoadingText(text);
    }

    // Recommended Skincare Screen
    public void setSkincareImage(int id) {
        mAiChatHostAdapter.setSkincareImage(id);
    }

    // Recommended Items Screen
    public void setRatingBarColor(int primaryColor) {
        mAiChatHostAdapter.setRatingColor(primaryColor);
    }

    public void setRatingBackgroundColor(Drawable background) {
        mAiChatHostAdapter.setRatingBackgroundColor(background);
    }

    public void setItemNumberOneImage(int drawable) {
        mAiChatHostAdapter.setItemNumberOneImage(drawable);
    }

    public void setItemNumberTwoImage(int drawable) {
        mAiChatHostAdapter.setItemNumberTwoImage(drawable);
    }

    public void setItemNumberThreeImage(int drawable) {
        mAiChatHostAdapter.setItemNumberThreeImage(drawable);
    }

    // Final Button
    public void setFinalButtonText(String text) {
        mAiChatHostAdapter.setFinalButtonText(text);
    }

    public void setFinalButtonColor(Drawable drawable) {
        mAiChatHostAdapter.setFinalButtonColor(drawable);
    }

    // Thank You Screen
    public void setThankYouTitleText(String text) {
        mAiChatHostAdapter.setThankYouTitleText(text);
    }

    public void setThankYouSubtitleText(String text) {
        mAiChatHostAdapter.setThankYouSubtitleText(text);
    }

    public void setThankYouTitleFont(Typeface typeface) {
        mAiChatHostAdapter.setThankYouTitleFont(typeface);
    }

    public void setThankYouSubtitleFont(Typeface typeface) {
        mAiChatHostAdapter.setThankYouSubtitleFont(typeface);
    }
}
