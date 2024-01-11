package com.example.whatsappclone.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization: key=" + "", ""})

    @POST("fcm/send")
    Call<Notification> sendNotification(@Body Notification notification);

}
