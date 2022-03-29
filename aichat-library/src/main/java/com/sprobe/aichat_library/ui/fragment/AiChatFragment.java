package com.sprobe.aichat_library.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.sprobe.aichat_library.utils.Constants;
import com.sprobe.aichat_library.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sprobe.aichat_library.utils.Constants.ORIGINAL_PLATFORM_ID_TEXT_VALUE;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.MONOLITHS_DISPLAY_ID_PLAIN_TEXT;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.ORIGINAL_PLATFORM_ID;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.QUESTION_NUMBER_COUNTER;

public class AiChatFragment extends Fragment implements AiChatHostAdapter.Callback {

    private static final String TAG = AiChatFragment.class.getSimpleName();

    private View mView;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private Context mContext;

    private AiChatHostAdapter mAiChatHostAdapter;

    private ArrayList<ChatModel> mList;

    private String mUUID;

    private SharedPreferenceUtils mPreference;

    private int mStartPendingIntent;

    private FollowRequest.Attributes mAttributes;

    private FollowRequest.User mUser;

    private FollowRequest.Event mEvent;

    private TextView mBackToTopTextView;

    private TextView mQuestionNumberTextView;

    private TextView mQuestionNumber7TextView;

    private int mQuestionNumberCounter = 1;

    private ReplyRequest mLastChipRequest;

    private ArrayList<ChatModel.ThankYou> mThankYouList = new ArrayList<>();

    private int mLastPosition = 0;

    private int mDiagnosisLastPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_ai_chat, container, false);
        mContext = getActivity();

        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mBackToTopTextView = mView.findViewById(R.id.backToTop_textView);
        mQuestionNumberTextView = mView.findViewById(R.id.questionNumber_textView);
        mQuestionNumber7TextView = mView.findViewById(R.id.questionNumber7_textView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(false);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPreference = SharedPreferenceUtils.getInstance(getContext());
        mUUID = mPreference.getStringValue(MONOLITHS_DISPLAY_ID_PLAIN_TEXT, "");
        mPreference.setValue(QUESTION_NUMBER_COUNTER, 0);
        mStartPendingIntent = 101;

        mList = new ArrayList<>();
        mAiChatHostAdapter = new AiChatHostAdapter();
        mAiChatHostAdapter.setCallback(this);
        mAiChatHostAdapter.setContext(getActivity());
        mRecyclerView.setAdapter(mAiChatHostAdapter);
        Log.d(TAG, mAiChatHostAdapter.getItemCount() + "");

        showTitle();

        mBackToTopTextView.setOnClickListener(v -> {
            mRecyclerView.smoothScrollToPosition(0);
            updateLastTimeClicked();
        });

        mRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                updateLastTimeClicked();
                return false;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "OnScrolled idle");
                    int recyclePosition = ((LinearLayoutManager) Objects.requireNonNull(mRecyclerView.getLayoutManager()))
                            .findFirstVisibleItemPosition();
                    Log.d(TAG, "Recycle Position " + recyclePosition);
                    Log.d(TAG, "Diagnosis Position " + mDiagnosisLastPosition);
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

        return mView;
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

    @Override
    public void onStartClick(ArrayList<ChatModel> list) {
        Log.d(TAG, "onStartClick");
        Log.d(TAG, "isClicked " + list.get(0).isClicked());
        Log.d(TAG, "mQuestionNumberCounter " + mQuestionNumberCounter);
        if (!list.get(0).isClicked()) {
//        sendFollowAPI();

            // Dummy Chips
            if (mQuestionNumberCounter <= 7) {
                setChatModel(null, dummyReplyApi(mQuestionNumberCounter));
                mQuestionNumberCounter += 1;
            }
            AiChatHelper.lastTimeClicked(mContext, mStartPendingIntent);
        }
    }

    private void sendFollowAPI() {
        Log.d(TAG, "sendFollowAPI");

        // Set Attributes data
        mAttributes = new FollowRequest.Attributes();
        mAttributes.setPreferences(Constants.AI_CHAT_PREFERENCE_VALUE);
        mAttributes.setSegment(Constants.AI_CHAT_SEGMENT_VALUE);

        // Set User data
        mUser = new FollowRequest.User();
        mUser.setAttributes(mAttributes);
        mUser.setId(Constants.AI_CHAT_USER_ID_VALUE);

        // Set FollowRequest params
        mEvent = new FollowRequest.Event();
        mEvent.setUser(mUser);
        mEvent.setTimestamp(String.valueOf(AiChatHelper.getCurrentTimeMillis()));

        FollowRequest followRequest = new FollowRequest();
        followRequest.setEvent(mEvent);
        followRequest.setOriginalPlatformId(mPreference.getStringValue(ORIGINAL_PLATFORM_ID, ORIGINAL_PLATFORM_ID_TEXT_VALUE));

        Log.d(TAG, "Follow Request Preferences: " + mAttributes.getPreferences());
        Log.d(TAG, "Follow Request Segment: " + mAttributes.getSegment());
        Log.d(TAG, "Follow Request ID: " + mUser.getId());
        Log.d(TAG, "Follow Request TimeStamp: " + mEvent.getTimestamp());
        Log.d(TAG, "Follow Request UUID: " + followRequest.getOriginalPlatformId());

        updateLastTimeClicked();

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

    private void setChatModel(FollowResponse followResponse, ReplyResponse replyResponse) {
        Log.d(TAG, "setChatModel");
        mList.clear();
        if (followResponse != null) {
            if (followResponse.getMessages() != null) {
                int messageSize = followResponse.getMessages().size();
                for (int i = 0; i < messageSize; i++) {
                    if (followResponse.getMessages().get(i).getType().equalsIgnoreCase("chip")) {
                        int lastChat = mList.size() - 1;
                        mList.get(lastChat).setType(followResponse.getMessages().get(i).getType());
                        mList.get(lastChat).setChips(chipsToArray(followResponse.getMessages().get(i).getChips()));
                        mList.get(lastChat).setMessageId(followResponse.getMessages().get(i).getId());
                    } else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setMessageId(followResponse.getMessages().get(i).getId());
                        chatModel.setType(followResponse.getMessages().get(i).getType());
                        chatModel.setText(followResponse.getMessages().get(i).getText());
                        chatModel.setLastMessage(followResponse.isLastMessage());
                        mList.add(chatModel);
                    }
                }
            }
        } else {
            if (!replyResponse.isLastMessage()) {
                int messageSize = replyResponse.getMessages().size();
                for (int i = 0; i < messageSize; i++) {
                    if (replyResponse.getMessages().get(i).getType().equalsIgnoreCase("chip")) {
                        int lastChat = mList.size() - 1;
                        mList.get(lastChat).setType(replyResponse.getMessages().get(i).getType());
                        mList.get(lastChat).setChips(chipsToArray(replyResponse.getMessages().get(i).getChips()));
                        mList.get(lastChat).setMessageId(replyResponse.getMessages().get(i).getId());
                    } else {
                        ChatModel chatModel = new ChatModel();
                        chatModel.setMessageId(replyResponse.getMessages().get(i).getId());
                        chatModel.setType(replyResponse.getMessages().get(i).getType());
                        chatModel.setText(replyResponse.getMessages().get(i).getText());
                        chatModel.setQuestionNumber(mQuestionNumberCounter);
                        chatModel.setLastMessage(replyResponse.isLastMessage());
                        mList.add(chatModel);
                    }
                }
            } else {
                ChatModel chatModel = new ChatModel();
                chatModel.setMessagesList(replyResponse.getMessages());
                chatModel.setType("recommendation");
                chatModel.setLastMessage(replyResponse.isLastMessage());

                mList.add(chatModel);
//                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setOnTouchListener((v, event) -> false);
            }
        }

        mAiChatHostAdapter.addChatMessage(mList.get(0));
        if (mList.get(0).getType().equals("chip") && !mList.get(0).isLastMessage()) {
            mDiagnosisLastPosition = mAiChatHostAdapter.getItemCount() - 1;
        }
        if (mList.get(0).isLastMessage()) {
            mAiChatHostAdapter.remove(mAiChatHostAdapter.getItemCount() - 2);
            mAiChatHostAdapter.notifyItemRemoved(mAiChatHostAdapter.getItemCount() - 1);
            mRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);

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
            mRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);
        }

        if (!mList.get(0).isLastMessage()) {
            int counter = mPreference.getIntValue(QUESTION_NUMBER_COUNTER, 0) + 1;
            mPreference.setValue(QUESTION_NUMBER_COUNTER, counter);
        }
    }

    public static ArrayList<ArrayList<FollowResponse.Chips>> chipsToArray(ArrayList<FollowResponse.Chips> chips) {
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

    @Override
    public void onChipItemClick(ReplyRequest.Event event, ArrayList<ChatModel> chatModelArrayList) {
        Log.d(TAG, "onChipItemClick");
        // Update List
        mList.clear();
        mList.addAll(chatModelArrayList);

        event.setUser(mUser);
        event.setTimestamp(String.valueOf(AiChatHelper.getCurrentTimeMillis()));

        ReplyRequest request = new ReplyRequest();
        request.setEvent(event);
        request.setOriginalPlatformId(mPreference.getStringValue(ORIGINAL_PLATFORM_ID, null));
        AiChatHelper.lastTimeClicked(mContext, mStartPendingIntent);
//        sendReplyAPI(request);

        // Dummy Chips
        if (mQuestionNumberCounter <= 7) {
            setChatModel(null, dummyReplyApi(mQuestionNumberCounter));
            mQuestionNumberCounter += 1;
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
            mRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);
//            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setOnTouchListener((v, event1) -> true);

            mLastChipRequest = request;
            sendReplyAPI(request);
        }
    }

    private void sendReplyAPI(ReplyRequest replyRequest) {
        Log.d(TAG, "sendReplyRequest");

        Log.d(TAG, "Reply Request OriginalPlatformID: " + replyRequest.getOriginalPlatformId());
        Log.d(TAG, "Reply Request Timestamp: " + replyRequest.getEvent().getTimestamp());
        Log.d(TAG, "Reply Request MessageId: " + replyRequest.getEvent().getMessageId());
        Log.d(TAG, "Reply Request Type: " + replyRequest.getEvent().getType());
        Log.d(TAG, "Reply Request ChipId: " + replyRequest.getEvent().getChip().getId());
        Log.d(TAG, "Reply Request ChipText: " + replyRequest.getEvent().getChip().getText());

        updateLastTimeClicked();

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

    @Override
    public void updateLastTimeClicked() {
        Log.d(TAG, "updateLastTimeClicked");
        AiChatHelper.lastTimeClicked(mContext, mStartPendingIntent);
    }

    @Override
    public void scrollToPosition(int position) {
        Log.d(TAG, "position " + position);
        mLayoutManager.scrollToPositionWithOffset(position, 0);
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
        mRecyclerView.smoothScrollToPosition(mAiChatHostAdapter.getItemCount() - 1);
        mBackToTopTextView.setVisibility(View.GONE);
        mLastPosition = mAiChatHostAdapter.getItemCount() - 1;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
        mQuestionNumberCounter = 1;
        mAiChatHostAdapter.clearItems();
        mList.clear();
        mThankYouList.clear();
    }

    private ReplyResponse dummyReplyApi(int questionNumber) {
        ReplyResponse replyResponse = new ReplyResponse();
        ArrayList<FollowResponse.Chips> chipsList = new ArrayList<>();
        ArrayList<FollowResponse.Messages> messageList = new ArrayList<>();
        FollowResponse.Chips chips1 = new FollowResponse.Chips();
        chips1.setSelected(false);
        FollowResponse.Chips chips2 = new FollowResponse.Chips();
        chips2.setSelected(false);
        FollowResponse.Chips chips3 = new FollowResponse.Chips();
        chips3.setSelected(false);
        FollowResponse.Chips chips4 = new FollowResponse.Chips();
        chips4.setSelected(false);
        FollowResponse.Chips chips5 = new FollowResponse.Chips();
        chips5.setSelected(false);
        FollowResponse.Chips chips6 = new FollowResponse.Chips();
        chips6.setSelected(false);
        FollowResponse.Messages chipsMessage = new FollowResponse.Messages();
        FollowResponse.Messages textMessage = new FollowResponse.Messages();

        switch (questionNumber) {
            case 1:
                textMessage.setType("text");
                textMessage.setText("一番気になる「肌のお悩み」はどれですか？");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("乾燥");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("毛穴");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("ニキビ・肌荒れ");
                chipsList.add(chips3);

                chips4.setId("1");
                chips4.setText("シミ・そばかす・くすみ");
                chipsList.add(chips4);

                chips5.setId("1");
                chips5.setText("しわ・たるみ");
                chipsList.add(chips5);

                chips6.setId("1");
                chips6.setText("chips6");
                chipsList.add(chips6);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            case 2:
                textMessage.setType("text");
                textMessage.setText("どんな時に肌が敏感になっていると感じますか？");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("季節の変わり目");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("ホルモンバランスが崩れたとき");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("ストレスを感じたとき");
                chipsList.add(chips3);

                chips4.setId("1");
                chips4.setText("常に感じる");
                chipsList.add(chips4);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            case 3:
                textMessage.setType("text");
                textMessage.setText("「紫外線対策」はどのくらいしていますか？");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("晴れてる日はしている");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("夏の間だけ");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("外でのスポーツやレジャーのときだけ");
                chipsList.add(chips3);

                chips4.setId("1");
                chips4.setText("あまり気にしていない");
                chipsList.add(chips4);

                chips5.setId("1");
                chips5.setText("毎日");
                chipsList.add(chips5);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            case 4:
                textMessage.setType("text");
                textMessage.setText("あなたの「 ご年齢 」を教えてください。");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("〜20代");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("30代");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("40代");
                chipsList.add(chips3);

                chips4.setId("1");
                chips4.setText("50代");
                chipsList.add(chips4);

                chips5.setId("1");
                chips5.setText("60代〜");
                chipsList.add(chips5);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            case 5:
                textMessage.setType("text");
                textMessage.setText("今のスキンケアで「気になること・不安なこと」はありますか？");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("自分の肌に合っているのかわからない");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("正しいケアができているのか不安");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("もっと良いアイテムがあるのではと思う");
                chipsList.add(chips3);

                chips4.setId("1");
                chips4.setText("特にない");
                chipsList.add(chips4);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            case 6:
                textMessage.setType("text");
                textMessage.setText("「理想のお手入れ」に近いものはどれですか？");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("できるだけ簡単に済ませたい");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("少し手間をかけても必要なケアはしたい");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("丁寧にしっかりケアしたい");
                chipsList.add(chips3);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            case 7:
                textMessage.setType("text");
                textMessage.setText("スキンケア選びの「決め手」として近いものを教えてください。");
                messageList.add(textMessage);

                chips1.setId("1");
                chips1.setText("できるだけ簡単に済ませたい");
                chipsList.add(chips1);

                chips2.setId("1");
                chips2.setText("価格よりも効果を重視");
                chipsList.add(chips2);

                chips3.setId("1");
                chips3.setText("こだわりはなく\n" +
                        "できるだけ低価格なもの");
                chipsList.add(chips3);

                chips4.setId("1");
                chips4.setText("気に入ったものは\n" +
                        "高価でも買ってしまう");
                chipsList.add(chips4);

                chipsMessage.setType("chip");
                chipsMessage.setChips(chipsList);
                messageList.add(chipsMessage);

                replyResponse.setMessages(messageList);
                replyResponse.setRecommendations(null);

                return replyResponse;

            default:
                return replyResponse;
        }
    }
}