package com.m090009.sample.realim.models;

import android.provider.ContactsContract;

import com.firebase.client.DataSnapshot;

import java.util.Map;

/**
 * Created by tohamy on 5/2/15.
 */
public class Message {
    private String messageText;
    private String userName;
    private String chatRoom;
    private String imageUrl;
    private boolean isImage;
    private boolean isLike;

    public Message() {
    }

    public static Message fromJsonToObject(Map<String, Object> messageMap){
        Message message = null;
        if(messageMap != null) {
            message = new Message();
            message.setUserName((String) messageMap.
                    get("userName"));
            if(!(boolean) messageMap.get("image")){
                message.setMessageText((String) messageMap.
                        get("messageText"));
            } else{
                message.setImage((boolean) messageMap.
                        get("image"));
                message.setImageUrl((String) messageMap.
                        get("imageUrl"));
                message.setMessageText("");
            }

        }
//        message.setUserName((String) dataSnapshot.
//                child("isImage").
//                getValue());
        return message;
    }

    public Message(String messageText,
                   String userName,
                   String chatRoom,
                   boolean isLike) {
        this.messageText = messageText;
        this.userName = userName;
        this.chatRoom = chatRoom;
        this.setLike(isLike);
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.setImage(true);
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean isImage) {
        this.isImage = isImage;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }
}
