package com.example.whatsappclone.API;

public class NotificationData {

    private String title, body, uid, fcm;

    public NotificationData(String title, String body, String uid, String fcm) {
        this.title = title;
        this.body = body;
        this.uid = uid;
        this.fcm = fcm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }
}
