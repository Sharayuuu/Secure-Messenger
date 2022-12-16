package com.example.section7findfriend.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.section7findfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {


  //***********************************************************
    private RecyclerView recyclerView;
    private TextView chatListEmpty;


    private DatabaseReference databaseReferenceChat,databaseReferenceUser;
    private FirebaseUser currentUser;



    //----------------------------
    private  ChatListAdaptor chatListAdaptor;
    private List<ChatListModel> chatListModelList;


    //here we will not use value event Listerner but we use ChildEventListerner
    private ChildEventListener childEventListener;

    //bcz we need to fecth data in sorted orderBy(timestamp) order from FirebaseDatabase
    private Query query;



//**************************************************************************************
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
//************************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvChats);
        chatListEmpty=view.findViewById(R.id.tvEmptyChat);

        chatListModelList = new ArrayList<>();
        chatListAdaptor = new ChatListAdaptor(getActivity(),chatListModelList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        //we want to show last receive message on first so we need to display layout in reverse order
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatListAdaptor);

        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReferenceChat = FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        query = databaseReferenceChat.orderByChild("timestamp");

        childEventListener = new ChildEventListener() {



            //Atmost similar to onChildChage
            //but if new friend added then onChildAdded will be trigged
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                UpdateList(snapshot,true,snapshot.getKey());

            }
            //
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               UpdateList(snapshot,false, snapshot.getKey());

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        //we have initilize childEventListerner now we
        //need to attach it to query

        query.addChildEventListener(childEventListener);
        chatListEmpty.setVisibility(View.VISIBLE);

    }

    private void UpdateList(DataSnapshot dataSnapshot,boolean isNew,String userId)
    {
        chatListEmpty.setVisibility(View.GONE);

        String lastMessage;
        String lastmessageTime;
        String unreadCount;

        lastmessageTime="";
        lastMessage="";
        unreadCount="";



        // to get Name of user

        databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Here we must make sure that fullName is not null:
              String  fullName = snapshot.child("fullName").getValue()!= null ?
                      snapshot.child("fullName").getValue().toString() : "";


                //Now to add fullName in List we need
                ChatListModel chatListModel = new ChatListModel(userId,fullName,unreadCount,lastMessage,lastmessageTime);

                chatListModelList.add(chatListModel);

                //when ever there is change in adaptor we need to notify it to RecycleView
                chatListAdaptor.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed to fetch Chat List",Toast.LENGTH_SHORT).show();

            }
        });
    }

    //we want to stop Listerner to this Event so we override onDestroy()


    @Override
    public void onDestroy() {
        super.onDestroy();
        query.removeEventListener(childEventListener);
    }
}