package com.sprobe.aichat_library.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReplyResponse {

    @SerializedName("messages")
    private ArrayList<FollowResponse.Messages> messages;

    @SerializedName("isLastMessage")
    private boolean isLastMessage;

    @SerializedName("recommendations")
    private ArrayList<Recommendations> recommendations;

    public static class Recommendations {

        @SerializedName("id")
        private String id;

        @SerializedName("brandId")
        private String brandId;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("details")
        private ArrayList<Details> details;

        @SerializedName("metadata")
        private Metadata metadata;

        private int order;

        private boolean expandable;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean isExpandable() {
            return expandable;
        }

        public void setExpandable(boolean expandable) {
            this.expandable = expandable;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public ArrayList<Details> getDetails() {
            return details;
        }

        public void setDetails(ArrayList<Details> details) {
            this.details = details;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }
    }
    public static class Details {

        @SerializedName("header")
        private String header;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("description")
        private String description;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Metadata {

        @SerializedName("itemName")
        private String itemName;

        @SerializedName("mainImageUrl")
        private String mainImageUrl;

        @SerializedName("packageName")
        private String packageName;

        @SerializedName("qrCodeUrl")
        private String qrCodeUrl;

        @SerializedName("recommendationScore")
        private String recommendationScore;

        @SerializedName("title")
        private String title;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getMainImageUrl() {
            return mainImageUrl;
        }

        public void setMainImageUrl(String mainImageUrl) {
            this.mainImageUrl = mainImageUrl;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public String getRecommendationScore() {
            return recommendationScore;
        }

        public void setRecommendationScore(String recommendationScore) {
            this.recommendationScore = recommendationScore;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public ArrayList<FollowResponse.Messages> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<FollowResponse.Messages> messages) {
        this.messages = messages;
    }

    public boolean isLastMessage() {
        return isLastMessage;
    }

    public void setLastMessage(boolean lastMessage) {
        isLastMessage = lastMessage;
    }

    public ArrayList<Recommendations> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(ArrayList<Recommendations> recommendations) {
        this.recommendations = recommendations;
    }
}