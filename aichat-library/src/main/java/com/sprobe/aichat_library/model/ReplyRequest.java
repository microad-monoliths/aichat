package com.sprobe.aichat_library.model;

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
        private FollowResponse.Chips chip;

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

        public FollowResponse.Chips getChip() {
            return chip;
        }

        public void setChip(FollowResponse.Chips chip) {
            this.chip = chip;
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
