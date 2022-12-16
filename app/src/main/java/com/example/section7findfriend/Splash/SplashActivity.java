package com.example.section7findfriend.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.section7findfriend.HomePage;
import com.example.section7findfriend.R;
import com.example.section7findfriend.userAuthentication.LoginPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    ImageView ivSplash;
    TextView tvSplash;

    //Object of Animation Class
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash=findViewById(R.id.ivSplash);
        tvSplash=findViewById(R.id.tvSplash);

        //Load Animation...........
        animation = AnimationUtils.loadAnimation(this,R.anim.splash_animation);

        //Set Animation Listerner......
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //**************************************
                // Stay Logged in with Firebase to the app when closed [duplicate]

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent1;
                if(firebaseUser != null)
                {
                     intent1 = new Intent(SplashActivity.this, HomePage.class);
                }
                else
                {
                    intent1 = new Intent(SplashActivity.this, LoginPage.class);
                }

                startActivity(intent1);

                finish();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




    }

    //**********************************
    //Default method of activity lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        ivSplash.startAnimation(animation);
        tvSplash.startAnimation(animation);
    }

}