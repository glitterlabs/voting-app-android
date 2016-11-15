package com.glitterlabs.voteme.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glitterlabs.voteme.R;
import com.glitterlabs.voteme.models.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.twitter.sdk.android.core.models.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by Home on 10/27/2016.
 */

public class ActivityUserProfile extends Activity {
    private static final int RESULT_LOAD_IMAGE = 1;


    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    private static final int RESULT_CAMERA_CODE=22;
   // private static final String BASE_URL="https://firebasestorage.googleapis.com/";
    ImageView profileImageView;
    Button fb_next;
    private DatabaseReference mDatabase;
    private String picturePath;
    private FirebaseStorage storage;
    EditText firstNameEditText,lastNameEditText;
    private UploadTask uploadTask;

    private String token;
    private DatabaseReference userInfoDB;
    UserInfo userInfo=new UserInfo();
    private SharedPreferences sharedPreferences;
    private String phno;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        token= getIntent().getStringExtra("TOKEN");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userInfoDB= mDatabase.child("userInfo").child(token);
        initView();

        isInternetOn();

         storage = FirebaseStorage.getInstance();
        registerEvetns();
    }


    private void registerEvetns() {
        userInfoDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fb_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submitPost();

            }


        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popup = new PopupMenu(ActivityUserProfile.this, profileImageView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.profile, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.Camera) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, RESULT_CAMERA_CODE);

                        }

                        if (item.getItemId() == R.id.Gallery) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, RESULT_LOAD_IMAGE);
                        }
                        if (item.getItemId() == R.id.cancel_action) {
                           // finish();
                        }

                       //Toast.makeText(ActivityUserProfile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                    popup.show();//showing popup menu





            }
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            }
        });

    }

    private void submitPost() {
        uploadPicToFirebase();

        // [END single_value_read]
        }

    private void writeToFireBase(String token,UserInfo userInfo) {

        mDatabase.child("userInfo").child(token).setValue(userInfo);

    }

    private void writeNewPost(String userId, String firstName, String lastName, String image) {
    }

    private void uploadPicToFirebase() {
        showdilog();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://vote-me-663f4.appspot.com");
        Uri file = Uri.fromFile(new File(picturePath));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("image url",downloadUrl.toString());
                String imageUrl=downloadUrl.toString();
             userInfo.setImage(imageUrl);

                final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();

                userInfo.setFirstName(firstName);
                userInfo.setLastName(lastName);
                sharedPreferences=getSharedPreferences("mypre", Context.MODE_PRIVATE);
                phno=sharedPreferences.getString("phone","");
                userInfo.setNumber(phno);
                hideDilog();


                // Title is required
                if (TextUtils.isEmpty(firstName)) {
                    firstNameEditText.setError(REQUIRED);
                    return;
                }else

                if (TextUtils.isEmpty(lastName)) {
                    lastNameEditText.setError(REQUIRED);
                    return;
                }else{

                    writeToFireBase(token,userInfo);
                }


            }
        });
    }

    private void hideDilog() {
        if(progressBar!=null && progressBar.isShowing())
        {
            progressBar.hide();
        }
    }

    private void showdilog() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Profile updating please wait ...");
        progressBar.show();//displays the progress bar
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE )
        {
            if(resultCode==RESULT_OK){
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                 picturePath = cursor.getString(columnIndex);
                Log.d("path",picturePath);
                cursor.close();
                //   profileImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                Glide
                        .with(ActivityUserProfile.this)

                        .load(new File(picturePath))
                        .bitmapTransform(new CropCircleTransformation(ActivityUserProfile.this))

                        .placeholder(R.drawable.ic_logo)
                        .crossFade()
                        .into(profileImageView);
            }

            // String picturePath contains the path of selected Image
        }else{
            onCaptureImageResult(data);

        }
    }

    private void initView() {
        fb_next = (Button) findViewById(R.id.next_Button);
        firstNameEditText= (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText= (EditText) findViewById(R.id.lastNameEditText);

        profileImageView= (ImageView) findViewById(R.id.profileImageView);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        picturePath=destination.getAbsolutePath();
        Glide
                .with(ActivityUserProfile.this)
                .load(destination)
                .bitmapTransform(new CropCircleTransformation(ActivityUserProfile.this))
                .crossFade()
                .into(profileImageView);

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

