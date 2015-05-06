package com.m090009.sample.realim.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m090009.sample.realim.R;
import com.m090009.sample.realim.fragments.ChatRoomsFragment;
import com.m090009.sample.realim.interfaces.OnAddUser;
import com.m090009.sample.realim.models.ChatRoom;
import com.m090009.sample.realim.models.Dialogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tohamy on 5/3/15.
 */
public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder>{

    private List<ChatRoom> dataSet;
    private OnAddUser callback;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.buildAddUsernameDialog(context,callback, getPosition());
                    Log.d("CHATROOMS", "Element " + getPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.chatroom_cell_Text);
        }
    }

    public ChatRoomsAdapter(List dataSet,
                            OnAddUser callback,
                            Context context) {
        this.dataSet = dataSet;
        this.callback = callback;
        this.context = context;
    }

    public void updateDataSource(ArrayList<ChatRoom> chatRooms){
        this.dataSet = chatRooms;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatrooms_cellview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(this.
                dataSet.
                get(position).
                getName());
    }

    @Override
    public int getItemCount() {
        return this.dataSet.size();
    }

}
