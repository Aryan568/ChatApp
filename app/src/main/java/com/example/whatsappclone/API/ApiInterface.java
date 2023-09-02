package com.example.whatsappclone.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization: key=" + "AAAADuyH4CE:APA91bF9dOMcLErwjc6E0Mqhudsjo6EvelmDpz9E0YMZtUzOnuR5bIZiTvV2_5e2o9SGpN_4wxBM4cSrAnIvoJYQ13ApeDhRZW-_9mXtKhMbNvm2UuOut2nC3V3pmu5u_hThcoke9qlU", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<Notification> sendNotification(@Body Notification notification);

}
