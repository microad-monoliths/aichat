package com.sprobe.aichat_library.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobe.aichat_library.R;
import com.sprobe.aichat_library.model.ChatModel;
import com.sprobe.aichat_library.model.FollowResponse;
import com.sprobe.aichat_library.model.ReplyRequest;
import com.sprobe.aichat_library.utils.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;

import static android.view.View.TRANSLATION_Y;

public class AiChatHostAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_TITLE = 0;
    public static final int VIEW_TYPE_TEXT = 1;
    public static final int VIEW_TYPE_CHIPS = 2;
    public static final int VIEW_TYPE_RECOMMENDATION = 3;
    public static final int VIEW_TYPE_RECOMMENDATION_ITEMS = 4;
    public static final int VIEW_TYPE_FINISH = 5;
    public static final int VIEW_TYPE_THANK_YOU = 6;

    private ArrayList<ChatModel> mChatList;

    // Start Screen
    private int mFirstLeftImage;
    private int mSecondLeftImage;
    private int mThirdLeftImage;
    private int mFirstRightImage;
    private int mSecondRightImage;
    private int mThirdRightImage;
    private boolean mAnimate = true;
    private String mHeaderText = null;
    private String mBigTitleText = null;
    private String mSubtitleOneText = null;
    private String mSubtitleTwoText = null;
    private Drawable mStartButtonGradient;

    // Diagnosis Screen
    private Drawable mChipGradient;
    private Typeface mChipTypeFace;
    private float mChipTextSize = 0;
    private Typeface mQuestionNumberTypeface;
    private Typeface mTextTypeface;

    // Loading Screen
    private String mLoadingText = null;

    // Recommended Skincare Screen
    private int mSkincareImage;

    // Recommended Items Screen
    private int mRatingPrimaryColor = 0;
    private Drawable mRatingBackgroundColor;
    private int mItemNumberOneImage;
    private int mItemNumberTwoImage;
    private int mItemNumberThreeImage;

    // Final Button
    private String mFinalText = null;
    private Drawable mFinalColor;

    // Thank You Screen
    private String mThankYouTitleText = null;
    private String mThankYouSubtitleText = null;
    private Typeface mThankYouTitleFont;
    private Typeface mThankYouSubtitleFont;

    private Context mContext;

    // Callback
    private Callback mCallback;

    public AiChatHostAdapter() {
        mChatList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void addChatList(ArrayList<ChatModel> chatList) {
        mChatList.addAll(chatList);
    }

    public void addChatMessage(ChatModel chatModel) {
        mChatList.add(chatModel);
    }

    public ArrayList<ChatModel> getChatList() {
        return mChatList;
    }

    public void remove(int position) {
        mChatList.remove(position);
    }

    @NonNull
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("AiChatAdapter", "viewType " + viewType);
        if (viewType == VIEW_TYPE_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_start, parent, false);
            return new VHTitle(view);
        } else if (viewType == VIEW_TYPE_TEXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_text, parent, false);
            return new VHText(view);
        } else if (viewType == VIEW_TYPE_CHIPS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_diagnosis, parent, false);
            return new VHChip(view);
        } else if (viewType == VIEW_TYPE_RECOMMENDATION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_result, parent, false);
            return new VHRecommendation(view);
        } else if (viewType == VIEW_TYPE_RECOMMENDATION_ITEMS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_result_item_number, parent, false);
            return new VHRecommendationItems(view);
        } else if (viewType == VIEW_TYPE_FINISH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_finish_diagnosis_button, parent, false);
            return new VHFinish(view);
        } else if (viewType == VIEW_TYPE_THANK_YOU) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_thank_you, parent, false);
            return new VHThankYou(view);
        }
        throw new RuntimeException("there is no type that matches the type "
                + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mChatList.size();

    }

    @Override
    public int getItemViewType(int position) {
        switch (mChatList.get(position).getType()) {
            case "title":
                return VIEW_TYPE_TITLE;
            case "chip":
                return VIEW_TYPE_CHIPS;
            case "recommendation":
                return VIEW_TYPE_RECOMMENDATION;
            case "recommendation_items":
                return VIEW_TYPE_RECOMMENDATION_ITEMS;
            case "finish":
                return VIEW_TYPE_FINISH;
            case "thank_you":
                return VIEW_TYPE_THANK_YOU;
            default:
                return VIEW_TYPE_TEXT;
        }
    }

    public void clearItems() {
        mChatList.clear();
    }

    public interface Callback {
        void onStartClick(ArrayList<ChatModel> list);

        void onChipItemClick(ReplyRequest.Event event, ArrayList<ChatModel> chatModelArrayList);

        void updateLastTimeClicked();

        void scrollToPosition(int position);

        void onFinishClicked();

        void smoothScrollToPosition(int position);
    }

    public class VHTitle extends BaseViewHolder {

        Button mStartBtn;

        ConstraintLayout mLayout;

        ImageView mDuoImageView;

        ImageView mOrbisImageView;

        ImageView mDecenciaImageView;

        ImageView mAstaLiftImageView;

        ImageView mClearImageView;

        ImageView mOrbisUdImageView;

        long mLastClickTime = 0;

        TextView mHeaderTextView;

        TextView mBigTitleTextView;

        TextView mSubtitleOneTextView;

        TextView mSubtitleTwoTextView;

        public VHTitle(View itemView) {
            super(itemView);

            mStartBtn = itemView.findViewById(R.id.startButton);
            mLayout = itemView.findViewById(R.id.start_constraintLayout);
            mDuoImageView = itemView.findViewById(R.id.duo_imageView);
            mOrbisImageView = itemView.findViewById(R.id.orbis_imageView);
            mDecenciaImageView = itemView.findViewById(R.id.decencia_imageView);
            mAstaLiftImageView = itemView.findViewById(R.id.astalift_imageView);
            mClearImageView = itemView.findViewById(R.id.clear_imageView);
            mOrbisUdImageView = itemView.findViewById(R.id.orbis_ud_imageView);
            mHeaderTextView = itemView.findViewById(R.id.header_textView);
            mBigTitleTextView = itemView.findViewById(R.id.bigTitle_textView);
            mSubtitleOneTextView = itemView.findViewById(R.id.subTitleOne_textView);
            mSubtitleTwoTextView = itemView.findViewById(R.id.subTitleTwo_textView);

            if (mAnimate) {
                animateObject(mDuoImageView, 0);
                animateObject(mOrbisImageView, 700);
                animateObject(mDecenciaImageView, 300);
                animateObject(mAstaLiftImageView, 500);
                animateObject(mClearImageView, 0);
                animateObject(mOrbisUdImageView, 1000);
            }

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            ChatModel object = mChatList.get(0);

            mStartBtn.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                mCallback.onStartClick(mChatList);
            });

            mLayout.setOnClickListener(v -> {
                mCallback.updateLastTimeClicked();
            });

            if (mFirstLeftImage != 0) {
                Picasso.get().load(mFirstLeftImage).into(mDuoImageView);
            }

            if (mSecondLeftImage != 0) {
                Picasso.get().load(mSecondLeftImage).into(mOrbisImageView);
            }

            if (mThirdLeftImage != 0) {
                Picasso.get().load(mThirdLeftImage).into(mDecenciaImageView);
            }

            if (mFirstRightImage != 0) {
                Picasso.get().load(mFirstRightImage).into(mAstaLiftImageView);
            }

            if (mSecondRightImage != 0) {
                Picasso.get().load(mSecondRightImage).into(mClearImageView);
            }

            if (mThirdRightImage != 0) {
                Picasso.get().load(mThirdRightImage).into(mOrbisUdImageView);
            }

            if (mHeaderText != null) {
                mHeaderTextView.setText(mHeaderText);
            }

            if (mBigTitleText != null) {
                mBigTitleTextView.setText(mBigTitleText);
            }

            if (mSubtitleOneText != null) {
                mSubtitleOneTextView.setText(mSubtitleOneText);
            }

            if (mSubtitleTwoText != null) {
                mSubtitleTwoTextView.setText(mSubtitleTwoText);
            }

            if (mStartButtonGradient != null) {
                mStartBtn.setBackground(mStartButtonGradient);
            }
        }

        private void animateObject(ImageView imageView, long delay) {
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(TRANSLATION_Y, 20f);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhY);
            animator.setDuration(3000);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setStartDelay(delay);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator);
            animatorSet.start();
        }
    }

    public class VHText extends BaseViewHolder {

        TextView mMessage;

        ConstraintLayout mLayout;

        public VHText(View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.chat_bubble);
            mLayout = itemView.findViewById(R.id.text_constraintLayout);

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatModel object = mChatList.get(position);

            if (object.getText() != null) {
                mMessage.setText(object.getText());
            }

            mLayout.setOnClickListener(v -> {
                mCallback.updateLastTimeClicked();
            });
        }

        @Override
        protected void clear() {

        }
    }

    public class VHChip extends BaseViewHolder implements DiagnosisAdapter.Callback {

        TextView mMessage;

        RecyclerView mChipRecyclerView;

        DiagnosisAdapter mDiagnosisAdaptor;

        ConstraintLayout mLayout;

        TextView mQuestionNumber;

        public VHChip(View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.question_message_textView);
            mChipRecyclerView = itemView.findViewById(R.id.diagnosis_chip_recyclerView);
            mLayout = itemView.findViewById(R.id.diagnosis_constraintLayout);
            mQuestionNumber = itemView.findViewById(R.id.question_number_textView);

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatModel object = mChatList.get(position);

            if (object.getText() != null) {
                mMessage.setText(object.getText());
            }

            mQuestionNumber.setText(MessageFormat.format("Q{0}", object.getQuestionNumber()));
            mLayout.setOnClickListener(v -> mCallback.updateLastTimeClicked());
            mDiagnosisAdaptor = new DiagnosisAdapter(object.getMessageId(), object.getType(), position);
            mDiagnosisAdaptor.addItems(object.getChips());
            mDiagnosisAdaptor.setCallback(this);
            mDiagnosisAdaptor.setContext(mContext);
            if (mChipGradient != null) {
                mDiagnosisAdaptor.setChipGradient(mChipGradient);
            }
            if (mChipTypeFace != null) {
                mDiagnosisAdaptor.setChipTypeface(mChipTypeFace);
            }
            if (mChipTextSize != 0) {
                mDiagnosisAdaptor.setChipTextSize(mChipTextSize);
            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(mChipRecyclerView.getContext(), 1);
            mLayoutManager.setStackFromEnd(false);
            mChipRecyclerView.setLayoutManager(mLayoutManager);
            mChipRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mChipRecyclerView.setAdapter(mDiagnosisAdaptor);
            mDiagnosisAdaptor.notifyItemInserted(object.getChips().size());

            if (mQuestionNumberTypeface != null) {
                mQuestionNumber.setTypeface(mQuestionNumberTypeface);
            }
            if (mTextTypeface != null) {
                mMessage.setTypeface(mTextTypeface);
            }
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onItemClick(ArrayList<ArrayList<FollowResponse.Chips>> chipsList, FollowResponse.Chips chips, String messageId, String type, int position) {
            // Update Chips
            mChatList.get(position).getChips().clear();
            mChatList.get(position).getChips().addAll(chipsList);
            mDiagnosisAdaptor.notifyDataSetChanged();

            // Send Reply Request
            // Reply Request Event

            ReplyRequest.Chip chip = new ReplyRequest.Chip();
            chip.setId(chips.getId());
            chip.setText(chips.getText());

            ReplyRequest.Event event = new ReplyRequest.Event();
            event.setChip(chip);
            event.setMessageId(messageId);
            event.setType(type);

            // Send Reply API
            mCallback.onChipItemClick(event, mChatList);
        }
    }

    public class VHRecommendation extends BaseViewHolder {

        AVLoadingIndicatorView mProgressBar;

        TextView mLoadingTextView;

        TextView mTitleTextView;

        CardView mCardView;

        TextView mResultOneTextView;

        TextView mResultTwoTextView;

        TextView mResultItemTextView;

        ImageView mSkincareImageView;

        public VHRecommendation(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.progressBar);
            mLoadingTextView = itemView.findViewById(R.id.loading_textView);
            mTitleTextView = itemView.findViewById(R.id.title_textView);
            mCardView = itemView.findViewById(R.id.card_view);
            mResultOneTextView = itemView.findViewById(R.id.result_one_textView);
            mResultTwoTextView = itemView.findViewById(R.id.result_two_textView);
            mResultItemTextView = itemView.findViewById(R.id.titleResult_textView);
            mSkincareImageView = itemView.findViewById(R.id.mainImage_imageView);

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatModel object = mChatList.get(position);

            if (mLoadingText != null) {
                mLoadingTextView.setText(mLoadingText);
            }

            if (object.getMessagesList() == null) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLoadingTextView.setVisibility(View.VISIBLE);
            } else {

                if (mSkincareImage != 0) {
                    Picasso.get().load(mSkincareImage).into(mSkincareImageView);
                }

                if (object.getMessagesList() != null) {
                    mResultOneTextView.setText(object.getMessagesList().get(0).getText());
                }

                if (object.getMessagesList() != null) {
                    mResultTwoTextView.setText(object.getMessagesList().get(1).getText());
                }

                mProgressBar.setVisibility(View.GONE);
                mLoadingTextView.setVisibility(View.GONE);
                mTitleTextView.setVisibility(View.VISIBLE);
                mCardView.setVisibility(View.VISIBLE);
                mResultItemTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void clear() {

        }
    }

    public class VHRecommendationItems extends BaseViewHolder implements RecommendationDetailsAdapter.Callback {

        TextView mTitleTextView;

        TextView mItemNameTextView;

        TextView mPackageNameTextView;

        RatingBar mRecommendationScoreBar;

        TextView mRatingTextView;

        ConstraintLayout mRatingBarLayout;

        ImageView mMainImageImageView;

        ImageView mQuestionNumberImageView;

        TextView mSeeMoreButton;

        TextView mCloseButton;

        ImageView mSeeMoreImageView;

        ImageView mCloseImageView;

        ImageView mQrCodeImageView;

        ConstraintLayout mExpandableListLayout;

        RecyclerView mRecyclerView;

        RecommendationDetailsAdapter mAdapter;

        public VHRecommendationItems(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.title_textView);
            mItemNameTextView = itemView.findViewById(R.id.itemName_textView);
            mPackageNameTextView = itemView.findViewById(R.id.packageName_textView);
            mRecommendationScoreBar = itemView.findViewById(R.id.rating_bar);
            mRatingTextView = itemView.findViewById(R.id.ratingText_textView);
            mRatingBarLayout = itemView.findViewById(R.id.ratingBar_layout);
            mMainImageImageView = itemView.findViewById(R.id.mainImage_imageView);
            mQuestionNumberImageView = itemView.findViewById(R.id.questionNumber_imageView);
            mCloseButton = itemView.findViewById(R.id.close_button);
            mSeeMoreButton = itemView.findViewById(R.id.see_more_button);
            mExpandableListLayout = itemView.findViewById(R.id.expandable_list_layout);
            mSeeMoreImageView = itemView.findViewById(R.id.see_more_imageView);
            mCloseImageView = itemView.findViewById(R.id.close_imageView);
            mRecyclerView = itemView.findViewById(R.id.resultItemDetails_recyclerView);
            mQrCodeImageView = itemView.findViewById(R.id.qrCode_imageView);

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatModel object = mChatList.get(position);

            if (object.getRecommendationItem().getDetails() != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter = new RecommendationDetailsAdapter();
                mAdapter.addItems(object.getRecommendationItem().getDetails());
                mAdapter.setCallback(this);
                GridLayoutManager mLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), 1);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setNestedScrollingEnabled(false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mAdapter);
            }

            if (object.getRecommendationItem().getMetadata() != null) {
                if (object.getRecommendationItem().getMetadata().getTitle() != null) {
                    mTitleTextView.setText(object.getRecommendationItem().getMetadata().getTitle());
                }
                if (object.getRecommendationItem().getMetadata().getItemName() != null) {
                    mItemNameTextView.setText(object.getRecommendationItem().getMetadata().getItemName());
                }
                if (object.getRecommendationItem().getMetadata().getPackageName() != null) {
                    mPackageNameTextView.setText(object.getRecommendationItem().getMetadata().getPackageName());
                }
                if (object.getRecommendationItem().getMetadata().getRecommendationScore() != null) {
                    mRecommendationScoreBar.setRating(Float.parseFloat(object.getRecommendationItem().getMetadata().getRecommendationScore()));
                }
                if (object.getRecommendationItem().getMetadata().getMainImageUrl() != null) {
                    Picasso.get().load(object.getRecommendationItem().getMetadata().getMainImageUrl()).into(mMainImageImageView);
                }
                if (object.getRecommendationItem().getMetadata().getQrCodeUrl() != null) {
                    Picasso.get().load(object.getRecommendationItem().getMetadata().getQrCodeUrl()).into(mQrCodeImageView);
                }

                switch (object.getRecommendationItem().getOrder()) {
                    case 0:
                        if (mItemNumberOneImage != 0) {
                            Picasso.get().load(mItemNumberOneImage).into(mQuestionNumberImageView);
                        } else {
                            Picasso.get().load(R.drawable.number_one).into(mQuestionNumberImageView);
                        }
                        break;
                    case 1:
                        if (mItemNumberTwoImage != 0) {
                            Picasso.get().load(mItemNumberTwoImage).into(mQuestionNumberImageView);
                        } else {
                            Picasso.get().load(R.drawable.number_two).into(mQuestionNumberImageView);
                        }
                        break;
                    case 2:
                        if (mItemNumberThreeImage != 0) {
                            Picasso.get().load(mItemNumberThreeImage).into(mQuestionNumberImageView);
                        } else {
                            Picasso.get().load(R.drawable.number_three).into(mQuestionNumberImageView);
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }

                boolean isExpanded = object.getRecommendationItem().isExpandable();
                mExpandableListLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                mSeeMoreButton.setText(isExpanded ? mContext.getResources().getString(R.string.text_close) : mContext.getResources().getString(R.string.text_see_more));
                if (isExpanded) {
                    Picasso.get().load(R.drawable.up_arrow).into(mSeeMoreImageView);
                } else {
                    Picasso.get().load(R.drawable.down_arrow).into(mSeeMoreImageView);
                }

                mSeeMoreButton.setOnClickListener(v -> {
                    object.getRecommendationItem().setExpandable(!object.getRecommendationItem().isExpandable());
                    notifyDataSetChanged();
                    mCallback.scrollToPosition(position);
                    mCallback.updateLastTimeClicked();
                });

                mSeeMoreImageView.setOnClickListener(v -> {
                    object.getRecommendationItem().setExpandable(!object.getRecommendationItem().isExpandable());
                    notifyDataSetChanged();
                    mCallback.scrollToPosition(position);
                    mCallback.updateLastTimeClicked();
                });

                mCloseImageView.setOnClickListener(v -> {
                    object.getRecommendationItem().setExpandable(!object.getRecommendationItem().isExpandable());
                    notifyDataSetChanged();
                    mCallback.updateLastTimeClicked();
                });

                mCloseButton.setOnClickListener(v -> {
                    object.getRecommendationItem().setExpandable(!object.getRecommendationItem().isExpandable());
                    notifyDataSetChanged();
                    mCallback.updateLastTimeClicked();
                });

                if (mRatingPrimaryColor != 0) {
                    mRatingTextView.setTextColor(mRatingPrimaryColor);
                    mRecommendationScoreBar.setProgressTintList(ColorStateList.valueOf(mRatingPrimaryColor));
                    mRecommendationScoreBar.setSecondaryProgressTintList(ColorStateList.valueOf(mRatingPrimaryColor));
                }

                if (mRatingBackgroundColor != null) {
                    mRatingBarLayout.setBackground(mRatingBackgroundColor);
                }
            }
        }
    }

    public class VHFinish extends BaseViewHolder {

        Button mFinishButton;

        long mLastClickTime = 0;

        public VHFinish(View itemView) {
            super(itemView);

            mFinishButton = itemView.findViewById(R.id.finish_diagnosis_textView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            mFinishButton.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                mCallback.onFinishClicked();
                mCallback.updateLastTimeClicked();
            });

            if (mFinalText != null) {
                mFinishButton.setText(mFinalText);
            }

            if (mFinalColor != null) {
                mFinishButton.setBackground(mFinalColor);
            }
        }
    }

    public class VHThankYou extends BaseViewHolder implements ThankYouAdapter.Callback {

        TextView mBackToTopTextView;

        RecyclerView mRecyclerView;

        ThankYouAdapter mAdapter;

        TextView mTitleTextView;

        TextView mSubtitleTextView;

        public VHThankYou(View itemView) {
            super(itemView);

            mRecyclerView = itemView.findViewById(R.id.thankYou_recyclerview);
            mBackToTopTextView = itemView.findViewById(R.id.backToTop_textView);
            mTitleTextView = itemView.findViewById(R.id.title_textView);
            mSubtitleTextView = itemView.findViewById(R.id.subtitle_textView);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatModel object = mChatList.get(position);

            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new ThankYouAdapter();
            mAdapter.addItems(object.getThankYouList());
            mAdapter.setCallback(this);
            if (mItemNumberOneImage != 0) {
                mAdapter.setItemNumberOneImage(mItemNumberOneImage);
            }
            if (mItemNumberTwoImage != 0) {
                mAdapter.setItemNumberTwoImage(mItemNumberTwoImage);
            }
            if (mItemNumberThreeImage != 0) {
                mAdapter.setItemNumberThreeImage(mItemNumberThreeImage);
            }
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyItemInserted(object.getThankYouList().size());

            mBackToTopTextView.setOnClickListener(v -> {
                mCallback.smoothScrollToPosition(0);
                mCallback.updateLastTimeClicked();
            });

            if (mThankYouTitleText != null) {
                mTitleTextView.setText(mThankYouTitleText);
            }

            if (mThankYouSubtitleText != null) {
                mSubtitleTextView.setText(mThankYouSubtitleText);
            }

            if (mThankYouTitleFont != null) {
                mTitleTextView.setTypeface(mThankYouTitleFont);
            }

            if (mThankYouSubtitleFont != null) {
                mSubtitleTextView.setTypeface(mThankYouSubtitleFont);
            }

        }
    }

    public void setFirstLeftImage(int drawable) {
        this.mFirstLeftImage = drawable;
    }

    public void setSecondLeftImage(int drawable) {
        this.mSecondLeftImage = drawable;
    }

    public void setThirdLeftImage(int drawable) {
        this.mThirdLeftImage = drawable;
    }

    public void setFirstRightImage(int drawable) {
        this.mFirstRightImage = drawable;
    }

    public void setSecondRightImage(int drawable) {
        this.mSecondRightImage = drawable;
    }

    public void setThirdRightImage(int drawable) {
        this.mThirdRightImage = drawable;
    }

    public void doAnimateView(boolean animate) {
        this.mAnimate = animate;
    }

    public void setHeaderText(String header) {
        this.mHeaderText = header;
    }

    public void setBigTitleText(String bigTitle) {
        this.mBigTitleText = bigTitle;
    }

    public void setSubtitleOneText(String subtitle) {
        this.mSubtitleOneText = subtitle;
    }

    public void setSubtitleTwoText(String subtitle) {
        this.mSubtitleTwoText = subtitle;
    }

    public void setStartButtonGradient(Drawable gradient) {
        this.mStartButtonGradient = gradient;
    }

    // Diagnosis Screen
    public void setChipGradient(Drawable gradient) {
        this.mChipGradient = gradient;
    }

    public void setChipFont(Typeface typeface) {
        this.mChipTypeFace = typeface;
    }

    public void setChipSize(float size) {
        this.mChipTextSize = size;
    }

    public void setQuestionNumberFont(Typeface typeface) {
        this.mQuestionNumberTypeface = typeface;
    }

    public void setTextFont(Typeface typeface) {
        this.mTextTypeface = typeface;
    }

    // Loading Screen
    public void setLoadingText(String text) {
        this.mLoadingText = text;
    }

    // Recommended SkinCare Screen
    public void setSkincareImage(int id) {
        this.mSkincareImage = id;
    }

    // Recommended Items Screen
    public void setRatingColor(int primary) {
        this.mRatingPrimaryColor = primary;
    }

    public void setRatingBackgroundColor(Drawable background) {
        this.mRatingBackgroundColor = background;
    }

    public void setItemNumberOneImage(int image) {
        this.mItemNumberOneImage = image;
    }

    public void setItemNumberTwoImage(int image) {
        this.mItemNumberTwoImage = image;
    }

    public void setItemNumberThreeImage(int image) {
        this.mItemNumberThreeImage = image;
    }

    // Final Button
    public void setFinalButtonText(String text) {
        this.mFinalText = text;
    }

    public void setFinalButtonColor(Drawable drawable) {
        this.mFinalColor = drawable;
    }

    // Thank You Screen
    public void setThankYouTitleText(String text) {
        this.mThankYouTitleText = text;
    }

    public void setThankYouSubtitleText(String text) {
        this.mThankYouSubtitleText = text;
    }

    public void setThankYouTitleFont(Typeface typeface) {
        this.mThankYouTitleFont = typeface;
    }

    public void setThankYouSubtitleFont(Typeface typeface) {
        this.mThankYouSubtitleFont = typeface;
    }
}
