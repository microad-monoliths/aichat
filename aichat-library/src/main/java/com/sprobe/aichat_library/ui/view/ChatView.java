package com.sprobe.aichat_library.ui.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobe.aichat_library.R;
import com.sprobe.aichat_library.model.ChatModel;
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

import static com.sprobe.aichat_library.ui.fragment.AiChatFragment.chipsToArray;
import static com.sprobe.aichat_library.utils.SharedPreferenceConst.QUESTION_NUMBER_COUNTER;

public class ChatView extends RelativeLayout implements AiChatHostAdapter.Callback {

    protected static final String TAG = ChatView.class.getSimpleName();

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected RelativeLayout mRootLayout;
    protected RecyclerView mChatRecyclerView;
    protected TextView mBackToTopTextView;
    protected TextView mQuestionNumberTextView;
    protected TextView mQuestionNumber7TextView;
    protected ArrayList<ChatModel> mChatList;
    private ArrayList<ChatModel.ThankYou> mThankYouList;
    protected AiChatHostAdapter mAiChatHostAdapter;

    private int mDiagnosisLastPosition = 0;
    private int mLastPosition = 0;
    private SharedPreferenceUtils mPreference;

    private OnCLickBackToTopButtonListener onCLickBackToTopButtonListener;

    public ChatView(@NonNull @NotNull Context context) {
        super(context);
    }

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

    public ChatView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

        mPreference = SharedPreferenceUtils.getInstance(context);

        mChatList = new ArrayList<>();
        mThankYouList = new ArrayList<>();
        mAiChatHostAdapter = new AiChatHostAdapter();
        mAiChatHostAdapter.setCallback(this);
        mAiChatHostAdapter.setContext(context);
        mChatRecyclerView.setAdapter(mAiChatHostAdapter);

        mBackToTopTextView.setOnClickListener(v -> backToTopButtonClicked(Constants.TOP_OF_CHAT_LIST));

        mChatRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                updateLastTimeClicked();
                return false;
            }
        });

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

    }

    @Override
    public void onChipItemClick(ReplyRequest.Event event, ArrayList<ChatModel> chatModelArrayList) {

    }

    @Override
    public void updateLastTimeClicked() {

    }

    @Override
    public void scrollToPosition(int position) {

    }

    @Override
    public void onFinishClicked() {

    }

    @Override
    public void smoothScrollToPosition(int position) {

    }

    protected void setAttributes(TypedArray attrs) {

    }

    public interface OnCLickBackToTopButtonListener {
        public void onBackToTopButtonClick(int position);
    }

    public void setOnCLickBackToTopButtonListener(OnCLickBackToTopButtonListener onCLickBackToTopButtonListener) {
        this.onCLickBackToTopButtonListener = onCLickBackToTopButtonListener;
    }

    public void backToTopButtonClicked(int position) {
        if (onCLickBackToTopButtonListener != null) {

            onCLickBackToTopButtonListener.onBackToTopButtonClick(position);
            mChatRecyclerView.smoothScrollToPosition(position);
        }
    }

    public void addMessageToChatList(FollowResponse followResponse, ReplyResponse replyResponse) {
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
//                mChatRecyclerView.setNestedScrollingEnabled(true);
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
            int counter = mPreference.getIntValue(QUESTION_NUMBER_COUNTER, 0) + 1;
            mPreference.setValue(QUESTION_NUMBER_COUNTER, counter);
        }
    }

    public int getQuestionNumberCounter() {
        return mPreference.getIntValue(QUESTION_NUMBER_COUNTER, 0);
    }
}
