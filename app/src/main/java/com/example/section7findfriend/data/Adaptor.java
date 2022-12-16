package com.example.section7findfriend.data;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.section7findfriend.chat.ChatFragment;
import com.example.section7findfriend.findfriends.FindFriendsFragment;
import com.example.section7findfriend.requests.RequestFriendFragment;
import com.google.android.material.tabs.TabLayout;
/*
     [   Adaptor ]
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Well adapters in Android are basically a bridge between the UI components and
    the data  source that fill data into the UI Component

    [ ViewPager ]
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   ViewPager is a layout manager that allows the user to flip left and right throug
   h pages of data. It is mostly found in apps like Youtube, Snapchat where the user
   shifts right – left to switch to a screen. Instead of using activities fragments
    are used. It is also used to guide the user through the app when the user launches
    the app for the first time


  [   FragmentPagerAdapter ]
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    This adapter should be used when the number of fragments is fixed.
     An application that has 3 tabs which won’t change during the runtime
     of the application. This tutorial will be using FragmentPagerAdapter


 */



public class Adaptor extends FragmentPagerAdapter
{
    TabLayout tabLayout;

    public Adaptor(@NonNull FragmentManager fm, int behavior, TabLayout tabLayout) {
        super(fm, behavior);
        this.tabLayout=tabLayout;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                ChatFragment chatFragment=new ChatFragment();
                return  chatFragment;
            case 1:
                RequestFriendFragment requestFriendFragment=new RequestFriendFragment();
                return requestFriendFragment;

            case 2:
                FindFriendsFragment findFriendsFragment=new FindFriendsFragment();
                return  findFriendsFragment;

        }

        return null;
    }

    @Override
    public int getCount() {
        return tabLayout.getTabCount();
    }
}


