package com.sprobe.aichat_library.api;

import com.sprobe.aichat_library.model.FollowRequest;
import com.sprobe.aichat_library.model.FollowResponse;
import com.sprobe.aichat_library.model.ReplyRequest;
import com.sprobe.aichat_library.model.ReplyResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface AiChatService {

    // Follow Api Request
//    @POST("/v1/webhook/vendor/follow")
    @POST("/v1/webhook/vendor/test/follow") // <-- Test Api Endpoint
    void follow(@Body FollowRequest followRequest,
                Callback<FollowResponse> callback);
    

    // Reply Api Request
//    @POST("/v1/webhook/vendor/reply")
    @POST("/v1/webhook/vendor/test/reply") // <-- Test Api Endpoint
    void reply(@Body ReplyRequest replyRequest,
               Callback<ReplyResponse> callback);

}
