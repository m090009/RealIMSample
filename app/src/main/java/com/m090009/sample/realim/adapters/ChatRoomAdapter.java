package com.m090009.sample.realim.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.m090009.sample.realim.R;
import com.m090009.sample.realim.helpers.ImageOperations;
import com.m090009.sample.realim.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tohamy on 5/3/15.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>{
    private String userName;
    private List<Message> dataSet;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mainLayout;
        public TextView messageTitleLeft;
        public TextView messageTitleRight;
        public TextView messageBody;
        public CardView cardView;
        public SimpleDraweeView messageImageView;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("CHATROOM", "Element " + getPosition() + " clicked.");
                }
            });
            this.messageTitleLeft = (TextView) v.findViewById(R.id.message_title_TextView_left);
            this.messageTitleRight = (TextView) v.findViewById(R.id.message_title_TextView_right);
            this.messageBody = (TextView) v.findViewById(R.id.message_body_TextView);
            this.messageImageView = (SimpleDraweeView) v.findViewById(R.id.message_imageView);
            this.cardView = (CardView) v.findViewById(R.id.message_card_view);
            this.mainLayout = (LinearLayout) v;

        }
        public void setMessageFields(final Message message, String direction){
            String userLetter = message.getUserName().toUpperCase().charAt(0) + "";
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayUserName(message.getUserName());
                }
            };
            if(direction.equals("R")) {
                hideView(this.messageTitleLeft);
                showView(this.messageTitleRight);
                this.messageTitleRight.setText(userLetter);
                this.messageTitleRight.setOnClickListener(clickListener);
            }
            else {
                hideView(this.messageTitleRight);
                showView(this.messageTitleLeft);
                this.messageTitleLeft.setText(userLetter);
                this.messageTitleLeft.setOnClickListener(clickListener);
            }

            if(message.isImage()){
                showView(this.messageImageView);
//                this.messageImageView.setProgressBarImage(new ProgressBarDrawable());
                Uri uri =  Uri.parse(message.getImageUrl());
                ImageOperations.requestImageResize(ImageOperations.IMAGE_MESSAGE_WIDTH,
                        ImageOperations.IMAGE_MESSAGE_HEIGHT,
                        uri,
                        this.messageImageView);
                this.messageBody.setAutoLinkMask(Linkify.WEB_URLS);
                this.messageBody.setMaxWidth((int)context.getResources().getDimension(R.dimen.image_width));
                this.messageBody.setText(message.getImageUrl());
            } else {
                hideView(this.messageImageView);
                this.messageBody.setText(message.getMessageText());
            }
        }

        public void hideView(View view) {
            view.setVisibility(View.GONE);
        }

        public void showView(View view){
            if(view.getVisibility() == View.GONE)
                view.setVisibility(View.VISIBLE);
        }

        public void displayUserName(String userName){
            Toast.makeText(context, userName, Toast.LENGTH_SHORT).show();
        }

        public void clearView(View view) {
//            setViewParameters(view, 0, 0);
        }

        public void setViewParameters(View view,
                                      int width,
                                      int height){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    view.getLayoutParams());
            params.width = width;
            params.height = height;
            view.setLayoutParams(params);
        }
    }

    public void updateDataSource(ArrayList<Message> messages){
        this.dataSet = messages;
        this.notifyDataSetChanged();
    }

    public void addNewMessage(Message message){
        if(message != null) {
            int position = this.dataSet.size();
            this.dataSet.add(message);
            notifyItemInserted(position);
            this.notifyItemRangeInserted(position, 1);
        }
    }

    public ChatRoomAdapter(String userName, List dataSet, Context context) {
        this.dataSet = dataSet;
        this.userName = userName;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatroom_cellview_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String direction = "L";
        if(userName.equals(this.dataSet.get(position).getUserName())){
            direction = "R";
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.Blue_Light));
            holder.messageBody.setTextColor(context.getResources().getColor(R.color.White));
            holder.mainLayout.setGravity(Gravity.RIGHT);
        } else{
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.OffWhite));
            holder.messageBody.setTextColor(context.getResources().getColor(R.color.Black));
            holder.mainLayout.setGravity(Gravity.LEFT);
        }
        holder.setMessageFields(this.dataSet.get(position), direction);
    }

    @Override
    public int getItemCount() {
        return this.dataSet.size();
    }

    public List<Message> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Message> dataSet) {
        this.dataSet = dataSet;
    }
}
