package com.glitterlabs.voteme.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.glitterlabs.voteme.R;
import com.glitterlabs.voteme.models.UserInfo;
import com.glitterlabs.voteme.models.Vote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;

public class MainActivity extends AppCompatActivity {
    Button submit_button;
    public Vote voteme;
    public String vote;
    TextView vote_here_tv;
    public boolean count=false;
    String userId;
    LinearLayout hiddenLayout,voteLinearLayout;
    CardView cardView_main_activity;
    public FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    RadioGroup radioGroup;
    SharedPreferences sharedPreferences;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView txtHillaryVote;
    private TextView txtDonaldVote;
    private ProgressDialog progressBar;
    AppCompatRadioButton radioVoteButton;
    private int hillaryvotes=0;
    private int donaldvotes=0;
    private String VOTEKEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_one);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        initViews();
       // showProgressDialog();
        getExtras();
        getData();
        getShared();
        hideProgressDialog();
        registerEvents();

    }

    private void getShared() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("myprofile", Context.MODE_PRIVATE);
        sharedPreferences1.getBoolean("TRUE", count);
        vote_here_tv.setText("thanks for the voting...");
        cardView_main_activity.setVisibility(View.GONE);
        submit_button.setVisibility(View.GONE);

    }


    private void hideProgressDialog() {
        ProgressDialog progressBar = new ProgressDialog(this);
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.hide();
        }


    }
    private void showProgressDialog() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("loading please wait ...");
        progressBar.show();//displays the progress bar
    }


    private void getData() {

        //fire base code to fetch the data from the server
        hideProgressDialog();
        mDatabase.child("userInfo").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    if(postSnapshot.getValue(UserInfo.class).getVote()!=null) {
                        if (postSnapshot.getValue(UserInfo.class).getVote().equals("hillary")) {
                            hillaryvotes = hillaryvotes + 1;
                        } else if (postSnapshot.getValue(UserInfo.class).getVote().equals("donald")) {
                            donaldvotes = donaldvotes + 1;

                        }
                    }
                    System.out.println(">>>>nnn" + postSnapshot.getValue(UserInfo.class).getVote());
                    hideProgressDialog();
                }
                txtHillaryVote.setText(String.valueOf(hillaryvotes));
                txtDonaldVote.setText(String.valueOf(donaldvotes));

                hillaryvotes=0;
                donaldvotes=0;


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void getExtras() {
        try {
            sharedPreferences = getSharedPreferences("mypre", Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("TOKEN", "");
            //Toast.makeText(getApplicationContext(), "token" + userId, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
          //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        try{
            sharedPreferences = getSharedPreferences("Vote", Context.MODE_PRIVATE);
            VOTEKEY = sharedPreferences.getString("VOTE", "");
            if(VOTEKEY==vote){
               // cardView_main_activity.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"you have already voted do not vcte again",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    ValueEventListener valueEventListener1=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    private void registerEvents() {
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (count == false) {
                        isInternetOn();
                        count = true;
                        SharedPreferences sharedPreferences1 = getSharedPreferences("myprofile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.putBoolean("TRUE", count);
                        editor.commit();
              /*  Thread t=new Thread(){
                  public void run(){
                      try{

                          sleep(3000);
                      }catch (Exception e){
                          e.printStackTrace();

                      }finally {
                          showProgressDialog();
                      }
                  }
                };
                t.start();*/


                        vote_here_tv.setText("Thanks for the vote...!");
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioVoteButton = (AppCompatRadioButton) findViewById(selectedId);

                        vote = radioVoteButton.getText().toString().trim();
                        UserInfo userInfo = new UserInfo();
                        if (selectedId == 0)
                            userInfo.setVote(vote);
                        SharedPreferences sharedPreferences = getSharedPreferences("Vote", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.putString("VOTE", vote);
                        editor1.commit();


                        mDatabase.child("userInfo").child(userId).child("vote").setValue(vote);
                        if (vote.equals("hillary")) {
                            //voteLinearLayout.setVisibility(View.GONE);
                        }
                        if (vote.equals("donald")) {
                            //voteLinearLayout.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "you have already voted", Toast.LENGTH_LONG).show();
                    }
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


    private void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        submit_button = (Button) findViewById(R.id.submit_button);
        txtHillaryVote = (TextView) findViewById(R.id.txtHillaryVote);
        txtDonaldVote = (TextView) findViewById(R.id.txtDonaldVote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbMain);
        Typeface tf = Typeface.createFromAsset(getAssets(), "rexlia.ttf");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Vote Me");
        cardView_main_activity= (CardView) findViewById(R.id.cardView_main_activity);
        txtHillaryVote.setTypeface(tf);
        txtDonaldVote.setTypeface(tf);
        vote_here_tv= (TextView) findViewById(R.id.vote_here_tv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void writeToFireBase(String token, Vote vote) {


        mDatabase.child("userInfo").child(token).child("votes").setValue(vote);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu:
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
                finish();

                return true;
            case R.id.profile_menu:
                Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
                startActivity(intent);

                return true;
            case R.id.signout_menu:

                FirebaseAuth.getInstance().signOut();
                Intent i1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i1);
                finish();

            case R.id.feedbackButton:
                Intent feed=new Intent(MainActivity.this,FeedbackActivity.class);
                startActivity(feed);




        }
        return super.onOptionsItemSelected(item);
    }
}
