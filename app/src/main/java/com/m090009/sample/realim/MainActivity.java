package com.m090009.sample.realim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.m090009.sample.realim.fragments.ChatRoomFragment;
import com.m090009.sample.realim.fragments.ChatRoomsFragment;
import com.m090009.sample.realim.fragments.NoNetworkFragment;
import com.m090009.sample.realim.helpers.ImageOperations;
import com.m090009.sample.realim.helpers.NetworkPreferences;
import com.m090009.sample.realim.interfaces.AfterImageTaken;
import com.m090009.sample.realim.interfaces.OnNetworkStateChanges;
import com.m090009.sample.realim.models.AwsS3Impl;
import com.m090009.sample.realim.models.ChatRoom;
import com.m090009.sample.realim.models.Dialogs;
import com.m090009.sample.realim.models.FirebaseImpl;
import com.m090009.sample.realim.models.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity implements
        ChatRoomsFragment.ChatRoomsFragmentInteraction,
        OnNetworkStateChanges {
    private TextView mainTitle;
    private FirebaseImpl firebase;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentImageName;
    private AwsS3Impl amazonS3;
    private AfterImageTaken afterImageTakenCallback;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private NetworkPreferences networkPreferences;
    private AlertDialog alertDialog = null;
    private TextView toolbarMiddleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(getApplicationContext());
        init();
    }

    public void init() {
        setToolBar();
        initNetworkPreferences();
        initFireBase();
        initAmazonS3();
        getViews();
    }

    public void setToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle the menu item
                        return true;
                    }
                });

        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        this.toolbarMiddleTitle = (TextView) toolbar.findViewById(R.id.toolbar_middle_title);
    }

    public void initNetworkPreferences(){
        this.networkPreferences = new NetworkPreferences(this, this);
    }

    public void initFireBase(){
        this.firebase = new FirebaseImpl(this);
//        mockAddChatRooms();
    }

    public void initAmazonS3(){
        this.amazonS3 = new AwsS3Impl(this);
    }

    public void getViews(){
//        this.mainTitle = (TextView) findViewById(R.id.main_title);
        addChatRoomsFragment();
    }

    public void addChatRoomsFragment(){
        makeFragmentWithDataSet(getChatRooms());
    }

    public ArrayList<ChatRoom> getChatRooms(){
        ArrayList<ChatRoom> mockDataList = new ArrayList<ChatRoom>();
        return mockDataList;
    }

    public void mockAddChatRooms(){
        ArrayList<ChatRoom> chatRooms = new ArrayList<>();
        chatRooms.add(new ChatRoom("Movies"));
        chatRooms.add(new ChatRoom("Tech"));
        chatRooms.add(new ChatRoom("Sports"));
        chatRooms.add(new ChatRoom("Politics"));
        chatRooms.add(new ChatRoom("Music"));
        chatRooms.add(new ChatRoom("Art"));
        chatRooms.add(new ChatRoom("Business"));
        for(ChatRoom cR : chatRooms)
            this.firebase.addChatRoom(cR);
    }


    public void makeFragmentWithDataSet(final ArrayList dataSet) {
        final ChatRoomsFragment chatRoomsFragment = ChatRoomsFragment.newInstance(dataSet, this);
        this.setProgressDialog(Dialogs.buildProgressDialog(this,
                getString(R.string.chatrooms_progressDialog_message)));
        setToolBarTitle("ChatRooms");
        this.firebase.getChatRooms(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    chatRooms.add(ChatRoom.fromJsonToObject(child, child.getKey()));
                }
                toolbarMiddleTitle.setText("("+chatRooms.size()+")");
                chatRoomsFragment.updateChatRoomsDataSet(chatRooms);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        addFragmentToView(chatRoomsFragment, chatRoomsFragment.getfName());
    }

    public void addFragmentToView(Fragment newFragment, String tag){
        this.getSupportFragmentManager().
                beginTransaction().
                add(R.id.main_content, newFragment, tag).
                addToBackStack(newFragment.getTag()).
                commit();
//        changePageTitle(tag.replace("Fragment", ""));
    }

    public void setToolBarTitle(String title){
        this.toolbar.setTitle(title);
    }

    @Override
    public void onStartNewChatRoom(ChatRoom chatroom, String userName) {
        addChatRoomFragment(chatroom, userName);
    }

    public void addChatRoomFragment(ChatRoom chatroom, String userName){
        makeChatRoomFragmentWithDataSet(getChatRoomMessages(chatroom.getId()),
                chatroom,
                userName,
                this.toolbarMiddleTitle);
    }



    public void makeChatRoomFragmentWithDataSet(ArrayList dataSet,
                                                ChatRoom chatroom,
                                                String userName,
                                                TextView middleTitle){
        final ChatRoomFragment chatRoomFragment = ChatRoomFragment.newInstance(dataSet,
                chatroom,
                userName,
                this.firebase,
                middleTitle);
        setToolBarTitle(chatroom.getName());
        this.afterImageTakenCallback = chatRoomFragment;
        this.firebase.addUser(userName, chatroom.getId());
        this.firebase.getChatRoomMessages(chatroom.getId()+"/"+FirebaseImpl.MESSAGES_REF, new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newMessage = (Map<String, Object>) dataSnapshot.getValue();
                Message message = Message.fromJsonToObject(newMessage);
                chatRoomFragment.addMessageToChatRoomDataSet(message);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        addFragmentToView(chatRoomFragment, chatRoomFragment.getfName());
    }

    public ArrayList<Message> getChatRoomMessages(String chatRoomName){
        ArrayList<Message> mockDataList = new ArrayList();

        return mockDataList;
    }

    public void takeAPicture(View view){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = ImageOperations.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                currentImageName = photoFile.getName();
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile));
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkPreferences.init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkPreferences.removeReciver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkPreferences.removeReciver();
    }

    @Override
    public void onNetworkStateChanges(boolean network) {
        if(!network){
            if(alertDialog == null)
                alertDialog = Dialogs.buildNoConnectionAlert(this);
        } else {
            if(alertDialog != null) {
                alertDialog.dismiss();
                alertDialog = null;
                if(!this.amazonS3.isReady()){
                    this.amazonS3.init(true);
                }
            }
        }
    }

    public TextView getToolbarMiddleTitle() {
        return toolbarMiddleTitle;
    }

    public void setToolbarMiddleTitleText(String text) {
        this.toolbarMiddleTitle.setText(text);
    }

    public void makeNoNetworkFragment() {
        final NoNetworkFragment noNetworkFragment = NoNetworkFragment.newInstance();
        setToolBarTitle(noNetworkFragment.getfName());
        addFragmentToView(noNetworkFragment, noNetworkFragment.getfName());
    }

    public void removeNoNetworkFragment(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Get image thumb file
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            File thumbFile = ImageOperations.persistImage(imageBitmap, this.currentImageName, this);
            //Upload to Amazon
            this.amazonS3.uploadPicture(thumbFile, this.afterImageTakenCallback);
            //Send message to Firebase
//            this.afterImageTakenCallback.afterImageTaken(imageUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
