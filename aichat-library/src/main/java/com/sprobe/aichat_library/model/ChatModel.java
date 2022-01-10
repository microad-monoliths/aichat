package com.sprobe.aichat_library.model;

import java.util.ArrayList;

public class ChatModel {

    public ChatModel() {
    }

    private String messageId;

    private ArrayList<ArrayList<FollowResponse.Chips>> chips;

    private String type;

    private String text;

    private String imageUri;

    private int questionNumber;

    private boolean isLastMessage;

    private boolean clicked;

    private ArrayList<FollowResponse.Messages> messagesList;

    private ReplyResponse.Recommendations recommendationItem;

    private ArrayList<ThankYou> thankYouList;

    public static class ThankYou {

        private String itemName;

        private String qrCodeUrl;

        private String imageUrl;

        private int order;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

    public ArrayList<ThankYou> getThankYouList() {
        return thankYouList;
    }

    public void setThankYouList(ArrayList<ThankYou> thankYouList) {
        this.thankYouList = thankYouList;
    }

    public ArrayList<FollowResponse.Messages> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(ArrayList<FollowResponse.Messages> messagesList) {
        this.messagesList = messagesList;
    }

    public ReplyResponse.Recommendations getRecommendationItem() {
        return recommendationItem;
    }

    public void setRecommendationItem(ReplyResponse.Recommendations recommendationItem) {
        this.recommendationItem = recommendationItem;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ArrayList<ArrayList<FollowResponse.Chips>> getChips() {
        return chips;
    }

    public void setChips(ArrayList<ArrayList<FollowResponse.Chips>> chips) {
        this.chips = chips;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public boolean isLastMessage() {
        return isLastMessage;
    }

    public void setLastMessage(boolean lastMessage) {
        isLastMessage = lastMessage;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
