package com.sprobe.aichat_library.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyRequest {

    @SerializedName("originalPlatformId")
    private String originalPlatformId;

    @SerializedName("event")
    private Event event;

    public static class Event {

        @SerializedName("timestamp")
        private String timestamp;

        @SerializedName("messageId")
        private String messageId;

        @SerializedName("type")
        private String type;

        @SerializedName("user")
        private FollowRequest.User user;

        @SerializedName("chip")
        private Chip chip;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public FollowRequest.User getUser() {
            return user;
        }

        public void setUser(FollowRequest.User user) {
            this.user = user;
        }

        public Chip getChip() {
            return chip;
        }

        public void setChip(Chip chip) {
            this.chip = chip;
        }
    }

    public static class Chip{

        @SerializedName("id")
        private String id;

        @SerializedName("text")
        private String text;

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
    }


    public String getOriginalPlatformId() {
        return originalPlatformId;
    }

    public void setOriginalPlatformId(String originalPlatformId) {
        this.originalPlatformId = originalPlatformId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
