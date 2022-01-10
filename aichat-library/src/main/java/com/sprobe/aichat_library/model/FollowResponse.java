package com.sprobe.aichat_library.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FollowResponse {

    @SerializedName("messages")
    private ArrayList<Messages> messages;

    @SerializedName("isLastMessage")
    private boolean isLastMessage;

    public static class Messages {

        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("type")
        private String type;

        @Expose
        @SerializedName("text")
        private String text;

        @Expose
        @SerializedName("chips")
        private ArrayList<Chips> chips;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public ArrayList<Chips> getChips() {
            return chips;
        }

        public void setChips(ArrayList<Chips> chips) {
            this.chips = chips;
        }
    }

    public static class Chips {

        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("text")
        private String text;

        @Expose
        @SerializedName("imageUri")
        private String imageUri;

        private boolean selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public static class Recommendations {

        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("imageUrl")
        private String imageUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public ArrayList<Messages> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Messages> messages) {
        this.messages = messages;
    }

    public boolean isLastMessage() {
        return isLastMessage;
    }

    public void setLastMessage(boolean lastMessage) {
        isLastMessage = lastMessage;
    }
}
