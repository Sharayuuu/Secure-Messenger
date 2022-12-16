package com.example.section7findfriend.findfriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.section7findfriend.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FindFriendAdaptor extends  RecyclerView.Adapter<FindFriendAdaptor.FindFriendViewHolder>
{
    //*****************************************************
    //we need Context to switch from one activity to another from class

        private Context context;
        private List<FindFriendModel> findFriendModelList;
        private  ImageView imageView;

    public FindFriendAdaptor(Context context, List<FindFriendModel> findFriendModels) {
        this.context = context;
        this.findFriendModelList = findFriendModels;

    }
    //-------------------------------------------------------------



    //***********************************************
    // For Send Frequest Buttons

    private DatabaseReference friendRequestDatabase;
    private FirebaseUser currentUser;
    private String userId;

    // now do work on [ onBlindViewHolder ]
    //*****************************************************




    @NonNull
    @Override
    public FindFriendAdaptor.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        imageView = parent.findViewById(R.id.ivProfile);
         View view = LayoutInflater.from(context).inflate(R.layout.find_friends_layout,parent,false);
        //this will call to our ViewHolder class
        return new FindFriendViewHolder(view);
    }

//***********************************************************************************

    @Override
    public void onBindViewHolder(@NonNull FindFriendAdaptor.FindFriendViewHolder holder, int position) {
        //onBind method we get Holder class from this we can acess all the Views
        // and using position we can acess the data

        //Object of FriendRequestModel

        FindFriendModel friendModel = findFriendModelList.get(position);

        holder.tvFullName.setText(friendModel.getUserName());

        /*
        String photoName = friendModel.getUserId()+".jpg";
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("images/"+photoName);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.get().load(uri).into(imageView);


            }
        });

         */


    //****************************************************
    // Again to Send Friquest Button Function

        friendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

// /*
       //*** check statues of request parameter(last one which is true/false) of FindFriendParameter
        if(friendModel.isRequestSent())
        {
            holder.btnSendRequest.setVisibility(View.GONE);
            holder.btnCancelrequest.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btnSendRequest.setVisibility(View.VISIBLE);
            holder.btnCancelrequest.setVisibility(View.GONE);
        }
// */



        // Before handling CLick we Need to check up statues of button as  above
        holder.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnSendRequest.setVisibility(View.GONE);
                holder.pbRequest.setVisibility(View.VISIBLE);

                //to get userId of user whose sent button has beed click
                userId=friendModel.getUserId();

                friendRequestDatabase.child(currentUser.getUid()).child(userId).child("request_type")
                        .setValue("sent")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    friendRequestDatabase.child(userId).child(currentUser.getUid()).child("request_type")
                                            .setValue("receive")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                      //  Toast.makeText(context,"Done Secessfully",Toast.LENGTH_SHORT).show();
                                                        holder.btnSendRequest.setVisibility(View.GONE);
                                                        holder.btnCancelrequest.setVisibility(View.VISIBLE);
                                                        holder.pbRequest.setVisibility(View.GONE);
                                                    }
                                                    else
                                                    {
                                                    //    Toast.makeText(context,"Not Sucessfull",Toast.LENGTH_SHORT).show();
                                                        holder.btnSendRequest.setVisibility(View.VISIBLE);
                                                        holder.btnCancelrequest.setVisibility(View.GONE);
                                                        holder.pbRequest.setVisibility(View.GONE);
                                                    }


                                                }
                                            });
                                }
                                else
                                {
                                //    Toast.makeText(context,"Not Sucessfull",Toast.LENGTH_SHORT).show();
                                    holder.btnSendRequest.setVisibility(View.VISIBLE);
                                    holder.btnCancelrequest.setVisibility(View.GONE);
                                    holder.pbRequest.setVisibility(View.GONE);
                                }

                            }
                        }); // addOnCompleteListerner

            }   //btn.onCLick
        });// btn.setOnClickListerner


        //****************************************************
        //For Cancel Button

        // Before handling CLick we Need to check up statues of button as  above
        holder.btnCancelrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnCancelrequest.setVisibility(View.GONE);
                holder.pbRequest.setVisibility(View.VISIBLE);

                //to get userId of user whose sent button has beed click
                userId=friendModel.getUserId();

                //TO we want to unset Value at "FriendRequest->CurrentUid->(userId)->"request_type"=null
                friendRequestDatabase.child(currentUser.getUid()).child(userId).child("request_type")
                        .setValue(null)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    friendRequestDatabase.child(userId).child(currentUser.getUid()).child("request_type")
                                            .setValue(null)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(context,"Done Secessfully",Toast.LENGTH_SHORT).show();
                                                        holder.btnSendRequest.setVisibility(View.VISIBLE);
                                                        holder.btnCancelrequest.setVisibility(View.GONE);
                                                        holder.pbRequest.setVisibility(View.GONE);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(context,"Not Sucessfull",Toast.LENGTH_SHORT).show();
                                                        holder.btnSendRequest.setVisibility(View.GONE);
                                                        holder.btnCancelrequest.setVisibility(View.VISIBLE);
                                                        holder.pbRequest.setVisibility(View.GONE);
                                                    }


                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(context,"Not Sucessfull",Toast.LENGTH_SHORT).show();
                                    holder.btnSendRequest.setVisibility(View.GONE);
                                    holder.btnCancelrequest.setVisibility(View.VISIBLE);
                                    holder.pbRequest.setVisibility(View.GONE);
                                }

                            }
                        }); // addOnCompleteListerner

            }   //btn.onCLick
        });// btn.setOnClickListerner



    }//BindViewHolder
//******************************************************************************************
    @Override
    public int getItemCount() {
        return findFriendModelList.size();
    }

//*********************************************************************************************
//              ViewHolder Class Implementation

    public class FindFriendViewHolder extends  RecyclerView.ViewHolder {

        private ImageView ivProfile;
        private TextView tvFullName;
        private Button btnSendRequest,btnCancelrequest;
        private ProgressBar pbRequest;



        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvFullName=itemView.findViewById(R.id.tvFullName);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
            btnCancelrequest = itemView.findViewById(R.id.btnCancelRequest);
            pbRequest = itemView.findViewById(R.id.pbRequest);
        }
    }
}
