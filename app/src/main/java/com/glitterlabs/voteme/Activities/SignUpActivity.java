package com.glitterlabs.voteme.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.glitterlabs.voteme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * Created by Home on 11/2/2016.
 */

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    EditText emailEditText;
    // EditText mEmailField;
    private static final String TAG = "EmailPassword";
    EditText passwordEditText;
    EditText confirmPassEditText;
    Button email_password_buttons;
    TextView SingInTextView;
    TextView sign_up_textView;
    private ProgressDialog progressBar;
    private String token = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        mAuth = FirebaseAuth.getInstance();
        mauthListener();
        initViews();


        registerEvents();
    }

    private void mauthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        //Toast.makeText(getApplicationContext(), "create account", Toast.LENGTH_LONG).show();
        if (!validateForm()) {
            return;
        }

        showProgressDialog();


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        //  Toast.makeText(getApplicationContext(), "crete user with Email:on complete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                        if (task.isSuccessful()) {
                            if (token != null) {
                                Intent i = new Intent(getApplicationContext(), ActivityUserProfile.class);
                                i.putExtra("TOKEN", token);
                                startActivity(i);
                                SharedPreferences sharedPreferences = getSharedPreferences("mypre", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("TOKEN", token);
                                editor.commit();
                                finish();


                            }
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "authentication failed",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private void hideProgressDialog() {
        //ProgressDialog progressBar = new ProgressDialog(this);
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.hide();
        }


    }

    private void showProgressDialog() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("singing up wait ...");
        progressBar.show();//displays the progress bar
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;


        String email = emailEditText.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            //Validation for Invalid Email Address

            emailEditText.setError("Invalid Email");
        }
        if (TextUtils.isEmpty(email)) {
            passwordEditText.setError("Required.");
            valid = false;
        }


        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        }
        String cpassword=confirmPassEditText.getText().toString();
        if(TextUtils.isEmpty(cpassword)){
            confirmPassEditText.setError("Required");
        }
        if(!cpassword.equals(password)){
            passwordEditText.setError("password not match");
            confirmPassEditText.setError("password not match");

        }
        if(password.length()<=5){
            confirmPassEditText.setError("6 characters required");

        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        //  hideProgressDialog();
        if (user != null) {
token=user.getUid();
        } else {

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
    // [END on_stop_remove_listener]

    private void registerEvents() {

        email_password_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
                isInternetOn();
                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                if (passwordEditText.getText().toString().equals(confirmPassEditText.getText().toString())) {
                    createAccount(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), " password is not matching", Toast.LENGTH_LONG).show();
                }


            }
        });
        SingInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();


            }
        });

    }

    private void initViews() {
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        // emailEditText= (EditText) findViewById(R.id.);
        // emailEditText= (EditText) findViewById(R.id.emailEditText);
        confirmPassEditText = (EditText) findViewById(R.id.confirmPassEditText);
        email_password_buttons = (Button) findViewById(R.id.sign_up_Button);
        SingInTextView = (TextView) findViewById(R.id.SingInTextView);
        sign_up_textView = (TextView) findViewById(R.id.sign_up_textView);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);


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

}
