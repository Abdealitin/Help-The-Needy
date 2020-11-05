package com.example.helptheneedy.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helptheneedy.Model.Request;
import com.example.helptheneedy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class makeRequest extends AppCompatActivity {
    private ImageButton mRequestImage;
    private TextView mRequestTitle;
    private TextView mRequestDescripton;
    private Button mSubmitRequest;
    private DatabaseReference mRequestDatabase;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private static final int GALLERY_CODE = 1;
    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);

        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");

        mRequestImage = (ImageButton) findViewById(R.id.imageButton);
        mRequestTitle  = (TextView) findViewById(R.id.requestTitle);
        mRequestDescripton = (TextView) findViewById(R.id.requestDescription);
        mSubmitRequest = (Button) findViewById(R.id.sumitRequest);

        mRequestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE);
            }
        });

        mSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            mImageUri = data.getData();
            mRequestImage.setImageURI(mImageUri);
        }
    }

    private void startPosting() {
        mProgress.setMessage("Uploading....");
        mProgress.show();

        final String titleVal = mRequestTitle.getText().toString().trim();
        final String descVal =  mRequestDescripton.getText().toString().trim();
        if(!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mImageUri != null){
            final StorageReference filepath = mStorage.child("RequestImages").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            DatabaseReference newReq=mRequestDatabase.push();

                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("title", titleVal);
                            dataToSave.put("desc", descVal);
                            dataToSave.put("image", downloadUrl.toString());
                            dataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));
                            dataToSave.put("userid", mUser.getUid());

                            newReq.setValue(dataToSave);
                            mProgress.dismiss();

                            startActivity(new Intent(makeRequest.this, homePage.class));
                            finish();
                        }
                    });
                    //Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    /*DatabaseReference newReq=mRequestDatabase.push();

                    Map<String, String> dataToSave = new HashMap<>();
                    dataToSave.put("title", titleVal);
                    dataToSave.put("desc", descVal);
                    dataToSave.put("image", downloadUrl.toString());
                    dataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userid", mUser.getUid());

                    newReq.setValue(dataToSave);
                    mProgress.dismiss();*/
                }
            });
        }
    }
}