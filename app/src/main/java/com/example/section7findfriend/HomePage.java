package com.example.section7findfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.section7findfriend.data.Adaptor;
import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity {

    //---------------------
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //------------------------------

    //menu Profile corner





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        //Blinding Components
        tabLayout=findViewById(R.id.tabMain);
        viewPager=findViewById(R.id.vpMain);

        //Method call for seting page viwer
        setViewPager();
    }

    //****************************************
    //Create Menu at right corner

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.mnuProfile)
        {
            Intent intent = new Intent(HomePage.this,ProfilePage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //***************************************
    //we are adding tabs in TabLayouts

    private  void setViewPager()
    {
        //********************************************************
        // Adding  tabs in Tablayout object custom
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_chat));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_requests));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.tab_findfriends));
        //********************************************************

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //Create Adaptor class for FragmentPageViwer (parameter Fragment , behaviour)

        Adaptor adaptor = new Adaptor(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout);

        //Set Adaptor on viewPage
        viewPager.setAdapter(adaptor);

        //To switch Between tabs which are selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Result changes After selection other tabs
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    //*************************************************
    //Part 4 Handling on Back press on main activity


    private boolean doubleBackPressed = false;

    @Override
    public void onBackPressed() {
        //       super.onBackPressed();


        if(tabLayout.getSelectedTabPosition()>0)
        {
            tabLayout.selectTab(tabLayout.getTabAt(0));


        }
        else
        {
            if(doubleBackPressed)
            {
                finishAffinity();
            }
            else
            {
                doubleBackPressed=true;
                Toast.makeText(this, R.string.press_back_to_exit,Toast.LENGTH_LONG).show();

                //Delay
                android.os.Handler handler = new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPressed=false;
                    }
                },2000);
            }
        }


    }




}