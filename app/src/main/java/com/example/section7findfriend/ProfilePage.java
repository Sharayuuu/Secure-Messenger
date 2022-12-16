package com.example.section7findfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.section7findfriend.userAuthentication.LoginPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {

    Button btnLogOff;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ImageView ivProfile;
    TextView fullName,email;
    String uName="",uEmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        fullName=findViewById(R.id.tv_Prof_uName);
        email=findViewById(R.id.tv_Prof_uEmail);

        btnLogOff=findViewById(R.id.btnLogoff);

        btnLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOff();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String Uid=firebaseUser.getUid();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d("p1", snapshot.child("fullName").getValue().toString());
                     uName = snapshot.child("fullName").getValue().toString();
                     uEmail = snapshot.child("email").getValue().toString();
                    fullName.setText(uName);
                    email.setText(uEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d("t1",uName);







    }

   private void LogOff()
   {

         FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfilePage.this, LoginPage.class);

        //To make sure user cant go  back

       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

       startActivity(intent);

   }

}