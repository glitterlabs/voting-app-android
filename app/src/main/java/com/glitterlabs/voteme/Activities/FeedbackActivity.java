package com.glitterlabs.voteme.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.glitterlabs.voteme.R;
import com.glitterlabs.voteme.models.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Home on 11/11/2016.
 */

public class FeedbackActivity extends AppCompatActivity{
    Button feedbackButton;
    EditText feedbackEditText;
    private DatabaseReference mDatabase;
    private SharedPreferences sharedPreferences;
    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);

        initViews();

        registEvents();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void registEvents() {
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feed=feedbackEditText.getText().toString();
                sharedPreferences = getSharedPreferences("mypre", Context.MODE_PRIVATE);
                userId = sharedPreferences.getString("TOKEN", "");
                mDatabase.child("userInfo").child(userId).child("feedback").setValue(feed);
                finish();

            }
        });
    }

    private void initViews() {
        feedbackButton= (Button) findViewById(R.id.feedbackButton);
        feedbackEditText= (EditText) findViewById(R.id.feedbackEditText);
    }
}