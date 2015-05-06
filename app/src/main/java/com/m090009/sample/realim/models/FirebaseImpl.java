package com.m090009.sample.realim.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.m090009.sample.realim.helpers.Utilities;
import com.m090009.sample.realim.interfaces.MessangerInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by tohamy on 5/2/15.
 */
public class FirebaseImpl {

    Firebase firebaseRootDataSource = null;
    private String userName = "Default";
    public static String CHATROOMS_REF = "chatrooms";
    public static String MESSAGES_REF = "messages";
    public static String USERS_REF = "users";

    public FirebaseImpl(Context context){
        Firebase.setAndroidContext(context);
        initFireBaseConnection(Utilities.FIREBASE_URL);
    }

    public void initFireBaseConnection(String firebaseUrl){
        Firebase fbRootDataSource = new Firebase(firebaseUrl);
        setFirebaseRootDataSource(fbRootDataSource);
    }

    public void addChatRoom(ChatRoom chatRoom){
        firebasePushToList(getFirebaseRootDataSource().child(CHATROOMS_REF),
                chatRoom);
    }

    public void getChatRooms(ValueEventListener valueEventListener){
        firebaseReadAllDataOnce(getFirebaseRootDataSource().child(CHATROOMS_REF),
                valueEventListener);
    }

    public void addUser(String userName, String chatRoomId){
        firebasePushToList(getFirebaseRootDataSource().
                child(CHATROOMS_REF).
                child(chatRoomId).
                child(USERS_REF), userName);
    }

    public void getChatRoomMessages(String chatRoomId,
                                    ChildEventListener childEventListener){
        firebaseReadNewData(getFirebaseRootDataSource().
                child(CHATROOMS_REF).
                child(chatRoomId), childEventListener);

    }

    public void addMessage(Message message, String chatRoomId){
        firebasePushToList(getFirebaseRootDataSource().
                child(CHATROOMS_REF).
                child(chatRoomId).
                    child(MESSAGES_REF), message);
    }

    public void firebaseSetValue(Firebase firebaseRef, Object value){
       firebaseRef.setValue(value);
    }

    public void firebaseUpdateChildValue(Firebase firebaseRef,
                                         String childId,
                                         Object value){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(childId, value);
        firebaseRef.updateChildren(map);
    }

    public void firebasePushToList(Firebase firebaseRef, Object value){
        firebaseRef.push().setValue(value);
    }

    public void firebaseReadAllDataOnce(Firebase firebaseRef,
                                 ValueEventListener valueEventListener){
        firebaseRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void firebaseReadNewData(Firebase firebaseRef,
                                    ChildEventListener childEventListener){
        firebaseRef.addChildEventListener(childEventListener);
    }

    public void firebaseRemoveListener(Firebase firebaseRef,
                                       ChildEventListener childEventListener){
        firebaseRef.removeEventListener(childEventListener);
    }

    public Firebase getFirebaseRootDataSource() {
        return firebaseRootDataSource;
    }

    public void setFirebaseRootDataSource(Firebase firebaseRootDataSource) {
        this.firebaseRootDataSource = firebaseRootDataSource;
    }
}
