package com.sprobe.aichat_library.model;

import com.google.gson.annotations.SerializedName;

public class FollowRequest {

    @SerializedName("originalPlatformId")
    private String originalPlatformId;

    @SerializedName("event")
    private Event event;

    public static class Event {

        @SerializedName("timestamp")
        private String timestamp;

        @SerializedName("user")
        private User user;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class User {

        @SerializedName("id")
        private String id;

        @SerializedName("attributes")
        private Attributes attributes;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }
    }

    public static class Attributes {

        @SerializedName("preferences")
        private String preferences;

        @SerializedName("segment")
        private String segment;

        public String getPreferences() {
            return preferences;
        }

        public void setPreferences(String preferences) {
            this.preferences = preferences;
        }

        public String getSegment() {
            return segment;
        }

        public void setSegment(String segment) {
            this.segment = segment;
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
