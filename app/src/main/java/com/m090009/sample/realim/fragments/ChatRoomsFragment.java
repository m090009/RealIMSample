package com.m090009.sample.realim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m090009.sample.realim.adapters.ChatRoomsAdapter;
import com.m090009.sample.realim.interfaces.OnAddUser;
import com.m090009.sample.realim.models.ChatRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tohamy on 5/3/15.
 */
public class ChatRoomsFragment extends Fragment implements OnAddUser{
    private RecyclerView mainListView;
    private List<ChatRoom> dataSet;
    private String fName = "ChatRooms";
    private ChatRoomsFragmentInteraction callback;
    private ChatRoomsAdapter adapter;
    private TextView toolbarTextView;

    public static ChatRoomsFragment newInstance(ArrayList<ChatRoom> list,
                                                ChatRoomsFragmentInteraction callback,
                                                TextView textView){
        ChatRoomsFragment chatRoomsFragment = new ChatRoomsFragment();
        chatRoomsFragment.setChatRoomsDataSet(list);
        chatRoomsFragment.callback = callback;
        chatRoomsFragment.setToolbarTextView(textView);
        return chatRoomsFragment;
    }

    public ChatRoomsFragment(){}

    public void setChatRoomsDataSet(ArrayList<ChatRoom> list){
        this.dataSet = list;
    }

    public void updateChatRoomsDataSet(ArrayList<ChatRoom> list){
        this.dataSet = list;
        this.adapter.updateDataSource(list);
    }

    public void addNewChatRoom(){

    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeChatRoomsAdapter(this.dataSet);
    }

    public void makeChatRoomsAdapter(List<ChatRoom> dataSet){
        ChatRoomsAdapter chatRoomsAdapter = new ChatRoomsAdapter(dataSet,
                this,
                getActivity());
        this.adapter = chatRoomsAdapter;
        setViewAdapter(chatRoomsAdapter);
    }

    public void setViewAdapter(ChatRoomsAdapter chatRoomsAdapter){
        this.mainListView.setAdapter(chatRoomsAdapter);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mainListView = setUpRecyclerView(getActivity());
        return this.mainListView;
    }

    public RecyclerView setUpRecyclerView(Context context){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        return makeRecyclerView(getActivity(), linearLayoutManager);
    }

    public RecyclerView makeRecyclerView(Context context,
                                         RecyclerView.LayoutManager layoutManager){
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    @Override
    public void onFinishAddUserDialog(String userName, int position) {
        this.callback.onStartNewChatRoom(this.dataSet.get(position),
                userName);
    }

    @Override
    public boolean hasUserName(String username, int position) {
        boolean hasUsername = true;
        if(this.dataSet.get(position).getUsers()!= null) {
            if (!this.dataSet.get(position).getUsers().containsValue(username))
                hasUsername = false;
        } else
            hasUsername = false;
        return hasUsername;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getToolbarTextView().setText("("+this.dataSet.size()+")");
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public TextView getToolbarTextView() {
        return toolbarTextView;
    }

    public void setToolbarTextView(TextView toolbarTextView) {
        this.toolbarTextView = toolbarTextView;
    }

    public interface ChatRoomsFragmentInteraction {
        public void onStartNewChatRoom(ChatRoom chatroom, String userName);
    }


}
