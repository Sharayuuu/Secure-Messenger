package com.example.section7findfriend.requests;

import android.content.Context;
import android.provider.ContactsContract;
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
import com.google.firebase.database.ServerValue;

import org.w3c.dom.Text;

import java.util.List;

public class RequestAdaptor extends RecyclerView.Adapter<RequestAdaptor.RequestViewHolder> {

  //*****************************************************
    //we need Context to switch from one activity to another from class
    private Context context;
    private List<RequestModel> requestModelList;




    //Constructor
    public RequestAdaptor(Context context, List<RequestModel> requestModelList) {
        this.context = context;
        this.requestModelList = requestModelList;
    }

    //************************************
    @NonNull
    @Override
    public RequestAdaptor.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friend_request_layout,parent,false);

        return new RequestViewHolder(view);
        //this will call to our ViewHolder class
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdaptor.RequestViewHolder holder, int position) {
        //onBind method we get Holder class from this we can acess all the Views
        // and using position we can acess the data


        //First we fetch data in Request Model
        RequestModel requestModel = requestModelList.get(position);

        holder.tvFullName.setText(requestModel.getUserName());
        /*
            get Firebase cloud Profile picture from Here

         */
        //Now Done with RequestAdaptor Class so now we implement the FriendRequest in Fragment Class...

    //*****************************************************************************
    // Request Accept Deny Button Functions

        DatabaseReference databaseReference;
        DatabaseReference databaseReferenceChild;
        FirebaseUser currentUser;

        databaseReference= FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        databaseReferenceChild = FirebaseDatabase.getInstance().getReference().child("Chats");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
//*******************************************************************************************************

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.btnDeny.setVisibility(View.GONE);
                holder.btnAccept.setVisibility(View.GONE);
                holder.pbDecision.setVisibility(View.GONE);

                //-----------------------------------
                String userId =   requestModel.getUserId();
                databaseReferenceChild.child(currentUser.getUid()).child(userId).child("timestamp").setValue(ServerValue.TIMESTAMP)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            databaseReferenceChild.child(userId).child(currentUser.getUid()).child("timestamp").setValue(ServerValue.TIMESTAMP)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful())
                                                            {
                                                                //  //now after acepting request we need to change stauts of request_type in "FriendRequest"
                                                                databaseReference.child(currentUser.getUid()).child(userId).child("request_type").setValue("accepted")
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if(task.isSuccessful())
                                                                                {
                                                                                    databaseReference.child(userId).child(currentUser.getUid()).child("request_type").setValue("accepted")
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                                    if(task.isSuccessful())
                                                                                                    {
                                                                                                        holder.pbDecision.setVisibility(View.GONE);
                                                                                                        holder.btnAccept.setVisibility(View.VISIBLE);
                                                                                                        holder.btnDeny.setVisibility(View.VISIBLE);
                                                                                                    }
                                                                                                    else
                                                                                                    {
                                                                                                        //else method handling function created below
                                                                                                        handleException(holder,task.getException());
                                                                                                    }

                                                                                                }
                                                                                            });

                                                                                }
                                                                                else
                                                                                {
                                                                                    //else method handling function created below
                                                                                    handleException(holder,task.getException());
                                                                                }
                                                                            }
                                                                        });

                                                            }
                                                            else
                                                            {
                                                                //else method handling function created below
                                                                handleException(holder,task.getException());
                                                            }

                                                        }
                                                    });

                                        }
                                        else
                                        {
                                            //else method handling function created below
                                                handleException(holder,task.getException());
                                        }
                                    }
                                });












            }
        });



    //*****************************************************************************************************
        holder.btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               holder.btnDeny.setVisibility(View.GONE);
               holder.btnAccept.setVisibility(View.GONE);
               holder.pbDecision.setVisibility(View.GONE);


              String userId =   requestModel.getUserId();

              databaseReference.child(currentUser.getUid()).child(userId).child("request_type")
                      .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                              if(task.isSuccessful())
                              {
                                  databaseReference.child(userId).child(currentUser.getUid()).child("request_type")
                                          .setValue(null)
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {

                                                  if(task.isSuccessful())
                                                  {
                                                      holder.pbDecision.setVisibility(View.GONE);
                                                      holder.btnAccept.setVisibility(View.VISIBLE);
                                                      holder.btnDeny.setVisibility(View.VISIBLE);
                                                      //****************************************


                                                  }
                                                  else
                                                  {
                                                      Toast.makeText(context.getApplicationContext(),"Failed to Deny Request",Toast.LENGTH_SHORT).show();
                                                      holder.pbDecision.setVisibility(View.GONE);
                                                      holder.btnAccept.setVisibility(View.VISIBLE);
                                                      holder.btnAccept.setVisibility(View.VISIBLE);
                                                  }

                                              }
                                          });
                              }
                              else
                              {
                                  Toast.makeText(context.getApplicationContext(),"Failed to Deny Request",Toast.LENGTH_SHORT).show();
                                  holder.pbDecision.setVisibility(View.GONE);
                                  holder.btnAccept.setVisibility(View.VISIBLE);
                                  holder.btnAccept.setVisibility(View.VISIBLE);
                              }

                          }
                      });




            }
        });



    }

    private void handleException(RequestViewHolder holder, Exception exception) {
        Toast.makeText(context.getApplicationContext(),"fail to Accept request",Toast.LENGTH_SHORT).show();
        holder.pbDecision.setVisibility(View.GONE);
        holder.btnAccept.setVisibility(View.VISIBLE);
        holder.btnDeny.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return requestModelList.size();
    }

    //Created View Holder Class inside RequestAdaptor

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        //We want object of call the views
        //called from onCreateViewHolder(passing View)
        // here we play with xml file who was convert into programable by above function call using LayoutInflator()

        private TextView tvFullName;
        private ImageView ivProfile;
        private Button btnAccept,btnDeny;
        private ProgressBar pbDecision;


        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            //here we will all blind ui componets with xml ids

            tvFullName=itemView.findViewById(R.id.fr_FullName);
            ivProfile=itemView.findViewById(R.id.fr_profileImage);
            btnAccept=itemView.findViewById(R.id.fr_btnAccept);
            btnDeny=itemView.findViewById(R.id.fr_btnDeny);
            pbDecision=itemView.findViewById(R.id.fr_progressDecision);

        }


    }
}
