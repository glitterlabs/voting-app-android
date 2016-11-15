package com.glitterlabs.voteme.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.glitterlabs.voteme.Activities.LoginActivity;
import com.glitterlabs.voteme.R;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Home on 10/26/2016.
 */
public class UserNumberActivity extends AppCompatActivity {
    Button auth_button_register;
    private static final String TWITTER_KEY = "05jtUe1ela6L5rbvq6mk4calL";
    private static final String TWITTER_SECRET = "qP0eM2tQ0a4l65Wf8WlaUF33nfAM3JTqBZoKPUTbFmNnNg9HI8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_number);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
        auth_button_register= (Button) findViewById(R.id.auth_button_register);
        final DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                SharedPreferences sharedPreferences=getSharedPreferences("mypre", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("phone",phoneNumber);
                editor.commit();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
        auth_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digitsButton.performClick();
            }
        });

    }
}
