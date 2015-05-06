package com.m090009.sample.realim.interfaces;

import com.m090009.sample.realim.models.Message;

/**
 * Created by tohamy on 5/2/15.
 */
public interface MessangerInterface {
    public String getUserName();
    public void addChatRoom(String chatRoomName);
    public void addUser(String chatRoom, String userName);
//    public void removeUser(String userName);
    public void sendMessage(Message message);
    public Message receiveMessage();
    public void sendImage(byte[] imageData);
}
