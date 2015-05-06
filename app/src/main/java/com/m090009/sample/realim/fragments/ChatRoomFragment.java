package com.m090009.sample.realim.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.m090009.sample.realim.R;
import com.m090009.sample.realim.adapters.ChatRoomAdapter;
import com.m090009.sample.realim.adapters.ChatRoomsAdapter;
import com.m090009.sample.realim.interfaces.AfterImageTaken;
import com.m090009.sample.realim.models.ChatRoom;
import com.m090009.sample.realim.models.FirebaseImpl;
import com.m090009.sample.realim.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tohamy on 5/3/15.
 */
public class ChatRoomFragment extends Fragment implements AfterImageTaken{
    private RecyclerView mainListView;
    private String fName = "ChatRoom";
    private ChatRoom chatRoom;
    private ArrayList<Message> dataSet;
    private String userName;
    private Button sendButton;
    private EditText messageInput;
    private ChatRoomAdapter adapter;
    private FirebaseImpl firebase;
    private TextView title;
    public static ChatRoomFragment newInstance(ArrayList<Message> list,
                                                ChatRoom chatRoom,
                                                String userName,
                                                FirebaseImpl firebase,
                                                TextView title
    ){
        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();
        chatRoomFragment.setDataSet(list);
        chatRoomFragment.setChatRoom(chatRoom);
        chatRoomFragment.setUserName(userName);
        chatRoomFragment.setFirebase(firebase);
        chatRoomFragment.setTitle(title);
        return chatRoomFragment;
    }

    public ChatRoomFragment(){}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeChatRoomAdapter(this.dataSet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_chatroom,null);
        setUpFragmentViews(view);
        return view;
    }

    public void setUpFragmentViews(View view){
        setUpRecyclerView(view);
        setUpMessageInput(view);
        setUpSendButton(view);
    }

    public void addMessageToChatRoomDataSet(Message message){
        if(message != null) {
//            this.dataSet.add(message);
            this.adapter.addNewMessage(message);
            this.title.setText("Messages ("+(this.adapter.getDataSet().size()-1)+")");
            ((LinearLayoutManager)this.mainListView.getLayoutManager()).
                    scrollToPositionWithOffset(this.adapter.getDataSet().size()-1, 0);
        }
    }

    public void setUpRecyclerView(View view){
        this.mainListView = getRecyclerView(view);
        this.mainListView.setItemAnimator(new DefaultItemAnimator());
    }

    public RecyclerView getRecyclerView(View view){
        return setRecyclerViewLayoutManager(
                (RecyclerView) view.findViewById(R.id.messagesView)
        );
    }

    public RecyclerView setRecyclerViewLayoutManager(RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        return recyclerView;
    }

    public void setUpMessageInput(View view){
        this.messageInput = getMessageInput(view);
    }

    public EditText getMessageInput(View view){
        return (EditText) view.findViewById(R.id.message_input);
    }

    public void setUpSendButton(View view){
        this.sendButton = getSendButton(view);
        this.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(messageInput.getText().toString(),
                        userName,
                        chatRoom.getId(),
                        false);
                addMessageToFireBase(message);
                messageInput.setText("");
            }
        });
    }

    public void addMessageToFireBase(Message message){
        this.firebase.addMessage(message, chatRoom.getId());
    }

    public Button getSendButton(View view){
        return (Button) view.findViewById(R.id.sendMessage);
    }

    public void makeChatRoomAdapter(List<Message> dataSet){
        ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(this.userName,
                dataSet,
                getActivity());
        setViewAdapter(chatRoomAdapter);
    }

    public void setViewAdapter(ChatRoomAdapter chatRoomAdapter){
        this.mainListView.setAdapter(chatRoomAdapter);
        this.adapter = chatRoomAdapter;
    }

    @Override
    public void afterImageTaken(String url) {
        Message message = new Message();
        message.setUserName(this.userName);
        message.setImageUrl(url);
        message.setImage(true);
        this.firebase.addMessage(message, this.chatRoom.getId());
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public RecyclerView getMainListView() {
        return mainListView;
    }

    public void setMainListView(RecyclerView mainListView) {
        this.mainListView = mainListView;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoomName) {
        this.chatRoom = chatRoomName;
    }

    public ArrayList<Message> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<Message> dataSet) {
        this.dataSet = dataSet;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public FirebaseImpl getFirebase() {
        return firebase;
    }

    public void setFirebase(FirebaseImpl firebase) {
        this.firebase = firebase;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }
}
