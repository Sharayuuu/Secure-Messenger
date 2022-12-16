package com.example.section7findfriend.findfriends;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.section7findfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * Use the {@link FindFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFriendsFragment extends Fragment {

    private RecyclerView rvFindFriends;
    private TextView  tvEmptyFriendList;
    private ProgressBar pbRequest;
    private FindFriendAdaptor findFriendAdaptor ;
    private List<FindFriendModel> findFriendModelList;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceFriendRequest;

    private FirebaseUser currentUser;



//*********************************************************
//used for saving states of sendreq , cancelreq button even after closing application

//*********************************************************************************************************************
//Ignore this
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFriendsFragment newInstance(String param1, String param2) {
        FindFriendsFragment fragment = new FindFriendsFragment();
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
//**********************************************************************************************


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFindFriends = view.findViewById(R.id.rvFindFriends);
        pbRequest=view.findViewById(R.id.pbRequest);
        tvEmptyFriendList = view.findViewById(R.id.tvEmptyFriendsList);



  //*************************************************************************************************

        //recycleview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFindFriends.setLayoutManager(layoutManager);

        //FindModel Adaptor intilize with Arraylist
        findFriendModelList = new ArrayList<>();

        findFriendAdaptor = new FindFriendAdaptor(getActivity(),findFriendModelList);


        //setting Adaptor on RecycleView
        rvFindFriends.setAdapter(findFriendAdaptor);



 //********************************************************************************************************


        //get databaseRef of "users"
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //get DatabaseRef of "FriendReqeust->(CurrentUid)..
        databaseReferenceFriendRequest = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(currentUser.getUid());

        tvEmptyFriendList.setVisibility(View.VISIBLE);

         /*
        Sorting Data
        ~~~~~~~~~~~

                to retrieve sorted data , start by specifice on the method
                ______________________________________________________
                1) orderByChild()
                2) orderByKey()			-> order result by child keys
                3) orderByValue()		->order result by child values

                    ex ;
                        Query messageQuery = databaseReference.orderByKey();
         */

        //Above


        Query query = databaseReference.orderByChild("fullName");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                findFriendModelList.clear();


                //For getting user on fragement from DbRef "users"
                for(DataSnapshot ds : snapshot.getChildren())
                {

                    String userId = ds.getKey();   //userId of select ds users

                    //To Make sure that current loged users should not get fetch from users list DB
                    if(userId.equals(currentUser.getUid()))
                    {
                        continue;
                    }


                    if(ds.child("fullName").getValue()!=null)
                    {

                        String fullName = ds.child("fullName").getValue().toString();


                        //**********************************************************************
                        // Due tue last parameter as false, buttons states can't restore everytime time close
                        // so Need to check status
                                                                        //who from user list
                       // now to get access of "FriendRequest->CurrUid->(userId);
                        databaseReferenceFriendRequest.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {

                                    String requestType = snapshot.child("request_type").getValue().toString();

                                    if(requestType.equals("sent"))
                                    {

                                        findFriendModelList.add(new FindFriendModel(fullName,userId,true));
                                        findFriendAdaptor.notifyDataSetChanged();
                                    }
                                }
                                else
                                {


                                    findFriendModelList.add(new FindFriendModel(fullName,userId,false));
                                    findFriendAdaptor.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                pbRequest.setVisibility(View.GONE);

                            }
                        });
                        tvEmptyFriendList.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                tvEmptyFriendList.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.fail_userlist,Toast.LENGTH_LONG).show();


            }
        });

    }
}