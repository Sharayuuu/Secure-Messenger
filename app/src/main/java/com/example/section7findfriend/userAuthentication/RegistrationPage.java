package com.example.section7findfriend.userAuthentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.section7findfriend.HomePage;
import com.example.section7findfriend.R;
import com.example.section7findfriend.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationPage extends AppCompatActivity {

    EditText RName,RAge,REmail,RPassword;
    Button RRegBtn;
    ProgressBar RProgress;
    ImageView RProfile;
    String userId;
    TextView Uforgot;
    //------------------------------------

    //-------------------------------
    private FirebaseAuth mAuth; //For Firebase Authentication
    private DatabaseReference mDatabase;    //For Real time Database



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        this.bindCOmponents();
        this.addListerner();
        Uforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationPage.this,LoginPage.class);
                startActivity(intent);
            }
        });

    }

    private void addListerner()
    {

        //For Register Button Action
        RRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }
    //-------------------------

//***************************************************************
//for Selecting Image and Uploading on server




//***************************Done*************************************
//----------------------------------------------
            private void bindCOmponents()
            {
                RName = findViewById(R.id.RName);
                RAge=findViewById(R.id.RAge);
                REmail=findViewById(R.id.REmail);
                RPassword=findViewById(R.id.RPassword);
                RProgress=findViewById(R.id.RProgressBar);
                RRegBtn = findViewById(R.id.RRegBtn);
                RProfile=findViewById(R.id.RimageView);
                Uforgot=findViewById(R.id.UForgot);
            }
    //-------------------------------------------------------
    //Check Wether user entered correct value or not

    private boolean validateUserDetails()
    {
        String email=REmail.getText().toString().trim();
        String password=RPassword.getText().toString().trim();
        String fullName=RName.getText().toString().trim();
        String age =RAge.getText().toString().trim();

        if(fullName.isEmpty())
        {
            RName.setError("Full Name is Required");
            RName.requestFocus();
            return false;
        }

        if(age.isEmpty())
        {
            RAge.setError("Age is Required");
            RAge.requestFocus();
            return false;
        }
        if(email.isEmpty())
        {
            REmail.setError("Email is Required");
            REmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            REmail.setError("Please provide Valid Email");
            REmail.requestFocus();
            return false;
        }
        if(password.isEmpty())
        {
            RPassword.setError("Password is Required");
            RPassword.requestFocus();
            return false;
        }
        if(password.length() < 6)
        {
            RPassword.setError("Password should At least 6 length");
            RPassword.requestFocus();
            return false;
        }
        return  true;
    }
//---------------------------------------------------------------

    private void registerUser()
    {
        if(validateUserDetails())
        {
            String name =RName.getText().toString().trim();
            String age = RAge.getText().toString().trim();
            String email=REmail.getText().toString().trim();
            String password=RPassword.getText().toString().trim();


            //need to get Firebase Authentication instance
            mAuth = FirebaseAuth.getInstance();

            //Default method provided by Firebase by their tutorial
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //to upload Profile Image on server
                             //   UploadImage();

                                //User Account Created in Firebase Authentication
                                //----------------------------------
                                //need to to Input Data to Realtime Databse using FirebaseDatabase object
                                mDatabase = FirebaseDatabase.getInstance().getReference();

                                //to get current users uid(unique id generated by authentication)
                                String UserId = mAuth.getCurrentUser().getUid();


                                //Create java Class user to store input data at once object
                                User user = new User(UserId,name,email,age);

                                //store user data in Realtimetime Database in child-user format

                                mDatabase.child("users").child(UserId).setValue(user);



                                //Remaining tasks
                                // --------------------------------
                                Toast.makeText(RegistrationPage.this, "User Registration Done", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegistrationPage.this, HomePage.class);
                                startActivity(i);
                                //
                            }
                            else
                            {
                                Toast.makeText(RegistrationPage.this, "User Registration Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        //--------------------------
    }
//-------------------------------------------

}