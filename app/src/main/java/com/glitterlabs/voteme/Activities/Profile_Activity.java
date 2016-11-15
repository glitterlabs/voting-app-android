package com.glitterlabs.voteme.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glitterlabs.voteme.R;
import com.glitterlabs.voteme.models.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;


/**
 * Created by Home on 10/26/2016.
 */

public class Profile_Activity extends AppCompatActivity {
    TextView nameTextView, numberTextView, voteTextView,emailTextView;
    ImageView profileImageView;
    private DatabaseReference mDatabase;
    CollapsingToolbarLayout collapsing_toolbar;
    private DatabaseReference userInfoDB;
    private SharedPreferences sharedPreferences;
    private StorageReference mStorage;
    private String userId;
    // private String userId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        mStorage = FirebaseStorage.getInstance().getReference();

        initViews();

        createUserProfile();
        toolBar();

        getExtras();
        isInternetOn();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("userId");
    }

    private void createUserProfile() {
        UserInfo userInfo = new UserInfo();
        String name = userInfo.getFirstName() + " " + userInfo.getLastName();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email=user.getEmail();
        emailTextView.setText(email);
       // nameTextView.setText(name);
    }

    private void getExtras() {


        try {
            sharedPreferences = getSharedPreferences("mypre", Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("TOKEN", " ");

         //   Toast.makeText(getApplicationContext(), "token" + userId, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
          //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

      /*  SharedPreferences sharedPreferences1=getSharedPreferences("ProfilePre", Context.MODE_PRIVATE);

        String  first=sharedPreferences1.getString("first",null);
        String last = sharedPreferences1.getString("last",null);*/


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userInfoDB = mDatabase.child("userInfo");

        userInfoDB.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    UserInfo user = dataSnapshot.getValue(UserInfo.class);
                    String first = user.getFirstName();
                    String last = user.getLastName();
                    String number = user.getNumber();
                    String vote = user.getVote();
                    voteTextView.setText(vote);

                    collapsing_toolbar.setTitle(first + " " + last);
                    numberTextView.setText(number);
                    Log.d("URL", user.getImage());
                /*UserInfo userInfo=new UserInfo();
                String name =userInfo.getFirstName()+" "+userInfo.getLastName();*/


                    Glide
                            .with(Profile_Activity.this)
                            .load(user.getImage())
                            .bitmapTransform(new CropSquareTransformation(Profile_Activity.this))
                            .crossFade()
                            .into(profileImageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    private void initViews() {
       // nameTextView=findViewById(R.id.nameTextView);

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        numberTextView = (TextView) findViewById(R.id.numberTextView);
        voteTextView = (TextView) findViewById(R.id.voteTextView);
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        emailTextView= (TextView) findViewById(R.id.emailTextView);

    }

    private void toolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Uncomment the below code to Set the message and title from the strings.xml file
            //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

            //Setting message manually and performing action on button click
            builder.setMessage("Make sure that your internet connection is on?")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Internet Connection Error");
            alert.show();
            return false;
        }
        return false;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
