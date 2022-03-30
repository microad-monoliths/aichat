package com.sprobe.aichat_library.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobe.aichat_library.R;
import com.sprobe.aichat_library.model.ChatModel;
import com.sprobe.aichat_library.utils.BaseViewHolder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ThankYouAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<ChatModel.ThankYou> mList;

    private int mItemNumberOneImage;
    private int mItemNumberTwoImage;
    private int mItemNumberThreeImage;

    // Callback
    private Callback mCallback;

    public ThankYouAdapter() {
    }

    public void addItems(ArrayList<ChatModel.ThankYou> list) {
        mList = list;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_thank_you_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface Callback {
    }

    public class ViewHolder extends BaseViewHolder {

        TextView mItemNameTextView;

        ImageView mMainImageImageView;

        ImageView mQrCodeImageView;

        ImageView mQuestionNumberImageView;


        public ViewHolder(View itemView) {
            super(itemView);

            mItemNameTextView = itemView.findViewById(R.id.itemName_textView);
            mMainImageImageView = itemView.findViewById(R.id.imageUrl_imageView);
            mQrCodeImageView = itemView.findViewById(R.id.qrCode_imageView);
            mQuestionNumberImageView = itemView.findViewById(R.id.questionNumber_imageView);

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ChatModel.ThankYou object = mList.get(position);

            if (object.getItemName() != null) {
                mItemNameTextView.setText(object.getItemName());
            }
            if (object.getImageUrl() != null) {
                Picasso.get().load(object.getImageUrl()).into(mMainImageImageView);
            }
            if (object.getQrCodeUrl() != null) {
                Picasso.get().load(object.getQrCodeUrl()).into(mQrCodeImageView);
            }

            switch (position) {
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

        }
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

}
