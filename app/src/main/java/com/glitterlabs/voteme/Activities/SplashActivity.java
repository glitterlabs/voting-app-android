package com.glitterlabs.voteme.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.glitterlabs.voteme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Home on 10/26/2016.
 */

public class SplashActivity extends AppCompatActivity {
    SharedPreferences  sharedPreferences;
    String userId=null;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "05jtUe1ela6L5rbvq6mk4calL";
    private static final String TWITTER_SECRET = "qP0eM2tQ0a4l65Wf8WlaUF33nfAM3JTqBZoKPUTbFmNnNg9HI8";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private  FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    SharedPreferences sharedPreferences=getSharedPreferences("mypre",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("TOKEN",user.getUid());
                    editor.commit();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed out
                    Intent intent=new Intent(getApplicationContext(),WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        };
        splashMethod();//This method is responsible to take the activity to the next screen.
    }


    private void splashMethod() {
        Thread thread=new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    mAuth.addAuthStateListener(mAuthListener);

                }
            }
        };
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
