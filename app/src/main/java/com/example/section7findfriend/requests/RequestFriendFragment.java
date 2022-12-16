package com.example.section7findfriend.requests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.section7findfriend.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.internal.RecaptchaActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFriendFragment extends Fragment {

       //Now to Implment Friend Request after designing RequestAdaptor
        //---------------------------------------------------

        private RecyclerView recyclerView;
        private RequestAdaptor requestAdaptor;
        private List<RequestModel> requestModelList;

        private TextView tvEmptyRequestList;

        //---------------------------------------

        private DatabaseReference databaseReferenceRequests;
        private DatabaseReference getDatabaseReferenceRequestUser; // to get UserName from  "user" after receiveing "friendRequest"
        private FirebaseUser currentUser;







  //**************************************************************************************
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestFriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFriendFragment newInstance(String param1, String param2) {
        RequestFriendFragment fragment = new RequestFriendFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_friend, container, false);
    }

    //************************************************************************************************************

    //now we need to override method called onViewCreated


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            //Binding ui components

            recyclerView=view.findViewById(R.id.rvRequestFriends);
            tvEmptyRequestList=view.findViewById(R.id.tvEmptyRequestList);


            //now on recycle view we set Layout Maganer as Linear Layout
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            //now intilize requestModel List
            requestModelList = new ArrayList<>();

            //intilze Adaptor
            requestAdaptor = new RequestAdaptor(getActivity(),requestModelList);

            //now set this Adaptor on RecyleView
            recyclerView.setAdapter(requestAdaptor);


            //now to get Current user
            currentUser = FirebaseAuth.getInstance().getCurrentUser();

            //---------------------
            getDatabaseReferenceRequestUser=FirebaseDatabase.getInstance().getReference().child("users");
            databaseReferenceRequests = FirebaseDatabase.getInstance().getReference().child("FriendRequest").child(currentUser.getUid());
            //------------------------

            tvEmptyRequestList.setVisibility(View.VISIBLE);


            //now to get data from databaseref

            databaseReferenceRequests.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //after getting data in snapshot

                    requestModelList.clear(); //clear list

                                            // with this we get iterable list of snapshot
                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        if(ds.exists())
                        {
                            String requestType = ds.child("request_type").getValue().toString();
                            //from here we will show only those who have type : Receive : "
                            if(requestType.equals("receive"))
                            {
                                String  userId = ds.getKey();

                                getDatabaseReferenceRequestUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String userName = snapshot.child("fullName").getValue().toString();

                                        //Now we have Data
                                        // so we will create one RequestModel Object

                                        RequestModel requestModel = new RequestModel(userId,userName);

                                        //now we will add that object in List
                                        requestModelList.add(requestModel);

                                        //now we will notify adaptor about changes that happen
                                        requestAdaptor.notifyDataSetChanged();

                                        //now we found data , so we fill hide textview which say emptyRequest List
                                        tvEmptyRequestList.setVisibility(View.GONE);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(),"Fail to get Request Data",Toast.LENGTH_SHORT).show();
                                    }
                                });



                            }
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),"Fail to get Request Data",Toast.LENGTH_SHORT).show();
                }
            });


    }
}