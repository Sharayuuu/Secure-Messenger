package com.example.section7findfriend.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section7findfriend.R;

import java.util.List;

public class ChatListAdaptor extends RecyclerView.Adapter<ChatListAdaptor.ChatListViewHolder> {

    //Local variable for Adaptor
    private Context context;
    private List<ChatListModel> chatListModelList;

    public ChatListAdaptor(Context context, List<ChatListModel> chatListModelList) {
        this.context = context;
        this.chatListModelList = chatListModelList;
    }
    //***********************************************************






    @NonNull
    @Override
    public ChatListAdaptor.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout,parent,false);

        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdaptor.ChatListViewHolder holder, int position) {

         ChatListModel chatListModel = chatListModelList.get(position);

         holder.fullName.setText(chatListModel.getUserName());

         //Done...............

        holder.cl_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("user_key",chatListModel.getUserId());
                intent.putExtra("user_Name",chatListModel.getUserName());
                Log.d("","User_key : Before Pass "+chatListModel.getUserId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatListModelList.size();
    }


  //*****************************************************************************
    public class ChatListViewHolder  extends  RecyclerView.ViewHolder{

      private LinearLayout linearLayout;
        private TextView fullName;
      private TextView lastMessage;
      private TextView lastMessageTime;
      private TextView unreadCount ;
      private ImageView clProfile;
      private LinearLayout cl_ll;





      public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);


            linearLayout = itemView.findViewById(R.id.cl_ll);
            fullName=itemView.findViewById(R.id.cl_fullName);
            lastMessage=itemView.findViewById(R.id.cl_lastMsg);
            lastMessageTime=itemView.findViewById(R.id.cl_lastMsgTime);
            unreadCount=itemView.findViewById(R.id.cl_UnreadCount);
            clProfile=itemView.findViewById(R.id.cl_Profile);
            cl_ll=itemView.findViewById(R.id.cl_ll);


        }
    }
}
