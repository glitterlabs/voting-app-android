package com.glitterlabs.voteme.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Home on 11/2/2016.
 */

public class LoginActivity extends AppCompatActivity {
    EditText mPasswordField;
    private static final String TAG = "EmailPassword";
    ProgressDialog progressBar;
    EditText mEmailField;
    Button login_Button;
    TextView sign_up_textView;
    String regEx =
            "^(([w-]+.)+[w-]+|([a-zA-Z]{1}|[w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]).([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]).([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[w-]+.)+[a-zA-Z]{2,4})$";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String strEmailAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        mauthListener();
        registerEvnts();
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
                // updateUI(user);
                // [END_EXCLUDE]
            }
        };
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            // mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            //  Toast.makeText(getApplicationContext(), "email password status:" + user.getEmail(), Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "firebase status:" + user.getUid(), Toast.LENGTH_LONG).show();

            //  findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            //  findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            //  findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            // sign_up_textView.setText("sign out");
            //Toast.makeText(getApplicationContext(), "sign out", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            mPasswordField.setError("enter correct password");
                            mEmailField.setError("enter correct email id");

                        }

                        // [START_EXCLUDE]

                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void hideProgressDialog() {
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

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            //Validation for Invalid Email Address

            mEmailField.setError("Invalid Email");
        }

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");

            valid = false;
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void initViews() {

        mPasswordField = (EditText) findViewById(R.id.passwordEditText);
        mEmailField = (EditText) findViewById(R.id.emailEditText);
        login_Button = (Button) findViewById(R.id.login_Button);
        sign_up_textView = (TextView) findViewById(R.id.sign_up_Button);


    }


    private void registerEvnts() {


        sign_up_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmailField.getText().toString().trim();
                final String password = mPasswordField.getText().toString().trim();
                validateForm();
                //btnValidateEmailAddress();
                isInternetOn();

                signIn(email, password);


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
}



