package com.sprobe.aichat_library.ui.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobe.aichat_library.R;
import com.sprobe.aichat_library.model.FollowResponse;
import com.sprobe.aichat_library.utils.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class DiagnosisAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_1 = 1;

    public static final int VIEW_TYPE_2 = 2;

    private ArrayList<ArrayList<FollowResponse.Chips>> mChips;

    private final String mMessageId;

    private final String mType;

    private Context mContext;

    private boolean isClickable = true;

    private final int mChipPosition;

    private long mLastClickTime = 0;

    // Callback
    private Callback mCallback;

    public DiagnosisAdapter(String messageId, String type, int position) {
        mChips = new ArrayList<>();
        mMessageId = messageId;
        mType = type;
        mChipPosition = position;
    }

    public void addItems(ArrayList<ArrayList<FollowResponse.Chips>> chips) {
        mChips.addAll(chips);
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_diagnosis_chip_cardview, parent, false);
            return new ViewHolder1(view);
        } else if (viewType == VIEW_TYPE_2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_diagnosis_chips_cardview, parent, false);
            return new ViewHolder2(view);
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
        return mChips.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChips.get(position).size() > 1) {
            return VIEW_TYPE_2;
        } else {
            return VIEW_TYPE_1;
        }
    }

    public interface Callback {
        void onItemClick(ArrayList<ArrayList<FollowResponse.Chips>> chipsList, FollowResponse.Chips chips, String messageId, String type, int position);
    }

    public class ViewHolder1 extends BaseViewHolder {

        Button mChipMessageButton;

        public ViewHolder1(View itemView) {
            super(itemView);

            mChipMessageButton = itemView.findViewById(R.id.chip_message_button);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ArrayList<FollowResponse.Chips> object = mChips.get(position);

            if (object.get(0).getText() != null) {
                mChipMessageButton.setText(object.get(0).getText());
            }

            if (object.get(0).isSelected()) {
                isClickable = false;
                mChipMessageButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_button_gradient_pressed));
            }

            mChipMessageButton.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (isClickable) {
                    Log.d("Message", object.get(0).getText());
                    mChipMessageButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_button_gradient_pressed));
                    object.get(0).setSelected(true);
                    mCallback.onItemClick(mChips, object.get(0), mMessageId, mType, mChipPosition);
                }
            });
        }
    }

    public class ViewHolder2 extends BaseViewHolder {

        Button mChip1MessageButton;

        Button mChip2MessageButton;

        public ViewHolder2(View itemView) {
            super(itemView);

            mChip1MessageButton = itemView.findViewById(R.id.chip_one_message_button);
            mChip2MessageButton = itemView.findViewById(R.id.chip_two_message_button);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ArrayList<FollowResponse.Chips> object = mChips.get(position);

            if (object.get(0).getText() != null) {
                mChip1MessageButton.setText(object.get(0).getText());
            }

            if (object.get(1).getText() != null) {
                mChip2MessageButton.setText(object.get(1).getText());
            }

            if (object.get(0).isSelected()) {
                isClickable = false;
                mChip1MessageButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_button_gradient_pressed));
            }

            if (object.get(1).isSelected()) {
                isClickable = false;
                mChip2MessageButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_button_gradient_pressed));
            }

            mChip1MessageButton.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (isClickable) {
                    Log.d("Message", object.get(0).getText());
                    mChip1MessageButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_button_gradient_pressed));
                    object.get(0).setSelected(true);
                    mCallback.onItemClick(mChips, object.get(0), mMessageId, mType, mChipPosition);
                }
            });

            mChip2MessageButton.setOnClickListener(v -> {
                if (isClickable) {
                    Log.d("Message", object.get(1).getText());
                    mChip2MessageButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chip_button_gradient_pressed));
                    object.get(1).setSelected(true);
                    mCallback.onItemClick(mChips, object.get(1), mMessageId, mType, mChipPosition);
                }
            });

        }
    }
}
