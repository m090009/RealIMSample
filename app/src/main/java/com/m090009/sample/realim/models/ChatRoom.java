package com.m090009.sample.realim.models;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by tohamy on 5/4/15.
 */
public class ChatRoom {
    private String name;
    private Map<String, Object> users;
    private List<Message> messages;
    private String id;

    public ChatRoom(){

    }

    public static ChatRoom fromJsonToObject(DataSnapshot dataSnapshot, String id){
        ChatRoom chatRoom = new ChatRoom();
        Map<String, Object> chatRoomMap = (Map<String, Object>) dataSnapshot.getValue();

        chatRoom.setName((String) chatRoomMap.get("name"));
        chatRoom.setId(id);
        chatRoom.setUsers((Map<String, Object>)chatRoomMap.get("users"));

        return chatRoom;
    }

    public ChatRoom(String name) {
        this.name = name;
    }

    public void getUsersFromSnapshot(Map<String, Object> chatRoom){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Object> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
