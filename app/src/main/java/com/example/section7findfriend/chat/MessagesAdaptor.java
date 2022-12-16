package com.example.section7findfriend.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section7findfriend.EncryptionAlgo.Algo;
import com.example.section7findfriend.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdaptor extends RecyclerView.Adapter<MessagesAdaptor.MessageViewHolder> {

    private Context context;
    private List<MessageModel> messageModelList;
    private FirebaseAuth firebaseAuth;


    public MessagesAdaptor(Context context, List<MessageModel> messageModelList) {
        this.context = context;
        this.messageModelList = messageModelList;
    }


    @NonNull
    @Override
    public MessagesAdaptor.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Layout Inflater will convert Xml page into Programmable Page
        View view = LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false);

        //it can only return MessageViewHoler Format
        return new MessageViewHolder(view);
    }

    // All Important Logic written in onBlindViewHOlder method
    @Override
    public void onBindViewHolder(@NonNull MessagesAdaptor.MessageViewHolder holder, int position) {

        Log.d("","Abhi_Position:"+position);

        //Message Position

        MessageModel message = messageModelList.get(position);

        firebaseAuth=FirebaseAuth.getInstance();
        String currentUserId=firebaseAuth.getCurrentUser().getUid();

        //To know from where message Come
        String fromUserId = message.getMessageFrom();

        Log.d("","fromUserId:"+fromUserId);

        //It imporatant in  Converting Current time(TimeStamp) into Time Date Format
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String dateTime = sfd.format(new Date(message.getMessageTime()));

        String [] splitString = dateTime.split(" ");
        //got get only HH-MM from Array
        String messageTime = splitString[1];

        String sam=message.getMessage();
        Log.d("","Before CurrentUserId:"+currentUserId+": fromUserId: "+sam);

     //   String deyMessage= Algo.decrypt(message.getMessage());
      //  String text=message.getMessage();
      //  Log.i("P1",text);
       // String deyText2=Algo.SimpleDecryption(sam);
          String deyText2=Algo.decrypt(sam);
        Log.i("P2",deyText2);
        //When Message is Send by User himself  then should be on Right hand Side
      if(fromUserId.equals(currentUserId))
        {
            holder.llSend.setVisibility(View.VISIBLE);
            holder.llReceived.setVisibility(View.GONE);

            //holder.tvSendMessage.setText(deyMessage);
            holder.tvSendMessage.setText(deyText2);
            holder.tvSendMessageTime.setText(messageTime);
        }//Else Message is Recieve so show it on Left hand Side
        else
        {
            holder.llSend.setVisibility(View.GONE);
            holder.llReceived.setVisibility(View.VISIBLE);
       //     holder.tvReceivedMessage.setText(deyMessage);
            holder.tvReceivedMessage.setText(deyText2);
            Log.i("P3",deyText2);
            holder.tvReceivedMessageTime.setText(messageTime);
        }


    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }


    //View  Holder Class which can hold all item from XML page in Java Variables
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        //All Components from XMl
        private LinearLayout llSend ,llReceived;
        private TextView tvSendMessage,tvSendMessageTime,tvReceivedMessage,tvReceivedMessageTime;
        private ConstraintLayout clMessage;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            //Blinding Components

            llSend=itemView.findViewById(R.id.llSent);
            llReceived=itemView.findViewById(R.id.llReceived);
            tvSendMessage=itemView.findViewById(R.id.tvSendMessage);
            tvSendMessageTime=itemView.findViewById(R.id.tvSendMessageTIme);
            tvReceivedMessage=itemView.findViewById(R.id.tvReceivedMessage);
            tvReceivedMessageTime=itemView.findViewById(R.id.tvRecivedMessageTIme);
            clMessage=itemView.findViewById(R.id.clMessage);

        }
    }
}
