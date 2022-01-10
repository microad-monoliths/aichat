package com.sprobe.aichat_library.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobe.aichat_library.R;
import com.sprobe.aichat_library.model.ReplyResponse;
import com.sprobe.aichat_library.utils.BaseViewHolder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecommendationDetailsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_1 = 1;

    public static final int VIEW_TYPE_2 = 2;

    private ArrayList<ReplyResponse.Details> mList;

    // Callback
    private Callback mCallback;

    public RecommendationDetailsAdapter() {
    }

    public void addItems(ArrayList<ReplyResponse.Details> list) {
        mList = list;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("RecommendationDetails", "viewType " + viewType);
        if (viewType == VIEW_TYPE_1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_result_details_one, parent, false);
            return new ViewHolder1(view);
        } else if (viewType == VIEW_TYPE_2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_result_details_two, parent, false);
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
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_1;
        } else {
            return VIEW_TYPE_2;
        }
    }

    public interface Callback {
    }

    public class ViewHolder1 extends BaseViewHolder {

        TextView mHeaderTextView;

        ImageView mImageUrlImageView;

        TextView mDescriptionTextView;

        public ViewHolder1(View itemView) {
            super(itemView);

            mHeaderTextView = itemView.findViewById(R.id.mainHeadline_textView);
            mImageUrlImageView = itemView.findViewById(R.id.imageUrl_imageView);
            mDescriptionTextView = itemView.findViewById(R.id.description_textView);

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ReplyResponse.Details object = mList.get(position);

            if (object.getHeader() != null) {
                Log.d("RecommendationDetails", "header " + object.getHeader());
                mHeaderTextView.setText(object.getHeader());
            }
            if (object.getImageUrl() != null) {
                Log.d("RecommendationDetails", "image URl " + object.getImageUrl());
                Picasso.get().load(object.getImageUrl()).into(mImageUrlImageView);
            }
            if (object.getDescription() != null) {
                Log.d("RecommendationDetails", "description " + object.getDescription());
                mDescriptionTextView.setVisibility(View.VISIBLE);
                mDescriptionTextView.setText(object.getDescription());
            }

        }
    }

    public class ViewHolder2 extends BaseViewHolder {

        TextView mHeaderTextView;

        ImageView mImageUrlImageView;

        TextView mDescriptionTextView;

        public ViewHolder2(View itemView) {
            super(itemView);

            mHeaderTextView = itemView.findViewById(R.id.mainHeadline_textView);
            mImageUrlImageView = itemView.findViewById(R.id.imageUrl_imageView);
            mDescriptionTextView = itemView.findViewById(R.id.description_textView);

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ReplyResponse.Details object = mList.get(position);

            if (object.getHeader() != null) {
                Log.d("RecommendationDetails", "header " + object.getHeader());
                mHeaderTextView.setText(object.getHeader());
            }
            if (object.getImageUrl() != null) {
                Log.d("RecommendationDetails", "image URl " + object.getImageUrl());
                Picasso.get().load(object.getImageUrl()).into(mImageUrlImageView);
            }
            if (object.getDescription() != null) {
                Log.d("RecommendationDetails", "description " + object.getDescription());
                mDescriptionTextView.setVisibility(View.VISIBLE);
                mDescriptionTextView.setText(object.getDescription());
            } else {
                mDescriptionTextView.setVisibility(View.GONE);
            }

        }
    }
}
