package com.sprobe.aichat_library.model;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    public static class Details {

        @SerializedName("typeUrl")
        public String typeUrl;

        @SerializedName("value")
        public String value;

        public String getTypeUrl() {
            return typeUrl;
        }

        public void setTypeUrl(String typeUrl) {
            this.typeUrl = typeUrl;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
