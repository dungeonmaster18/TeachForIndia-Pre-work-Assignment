package com.example.umesh.teachforindia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by umesh on 13-Apr-17.
 */

public class CreateOpportunity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;

    private ImageButton selectImage;
    private EditText useremail,title,description,from,to,location;
    private Button applyBtn,cancelBtn;
    private static final int GALLERY_REQUEST=1;
    private Uri imageUri=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user == null) {
            startActivity(new Intent(CreateOpportunity.this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.create_opportunity);

        useremail=(EditText) findViewById(R.id.input_email);
        title=(EditText) findViewById(R.id.opportunity_title);
        description=(EditText) findViewById(R.id.opportunity_description);
        location=(EditText) findViewById(R.id.location);
        to=(EditText) findViewById(R.id.date_to);
        from=(EditText) findViewById(R.id.date_from);
        selectImage=(ImageButton) findViewById(R.id.choose_btn);
        applyBtn=(Button)findViewById(R.id.btn_create);
        cancelBtn=(Button) findViewById(R.id.btn_cancel);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createOpportunity();
            }
        });

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(100);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public void createOpportunity(){

        if (!validate()) {
            onCreateFailed();
            return;
        }

        applyBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreateOpportunity.this,
                R.style.ThemeOverlay_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Posting Opportunity...");
        progressDialog.show();

        final String email = useremail.getText().toString().trim();
        final String opp_title=title.getText().toString().trim();
        final String opp_desc=description.getText().toString().trim();
        final String opp_location=location.getText().toString().trim();
        final String opp_to=to.getText().toString().trim();
        final String opp_from=from.getText().toString().trim();
        final String uid = mAuth.getCurrentUser().getUid();

        String imageUrl=random();

        StorageReference filepath=mStorageRef.child("Post_Images").child(imageUrl);

        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get a URL to the uploaded content
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("opportunity");
                DatabaseReference newPost=mDatabase.push();
                newPost.child("email").setValue(email);
                newPost.child("opportunity_title").setValue(opp_title);
                newPost.child("opportunity_description").setValue(opp_desc);
                newPost.child("opportunity_location").setValue(opp_location);
                newPost.child("start_date").setValue(opp_from);
                newPost.child("end_date").setValue(opp_to);
                newPost.child("url").setValue(downloadUrl.toString());
                newPost.child("uid").setValue(uid);

                Intent intent = new Intent(CreateOpportunity.this, LoginPage.class);
                progressDialog.dismiss();
                startActivity(intent);
                Toast.makeText(CreateOpportunity.this,
                        "Successfully Posted Opportunity " ,
                        Toast.LENGTH_SHORT).show();
                onCreateSuccess();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        progressDialog.dismiss();
                        Toast.makeText(CreateOpportunity.this,
                                "Something Went Wrong!" ,
                                Toast.LENGTH_SHORT).show();
                        applyBtn.setEnabled(true);
                        cancelBtn.setEnabled(true);

                    }
                });


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onCreateSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);


    }

    public void onCreateSuccess() {
        applyBtn.setEnabled(true);
        cancelBtn.setEnabled(true);

    }

    public void onCreateFailed() {
        Toast.makeText(getBaseContext(), "Attempt To Post Opportunity Failed!", Toast.LENGTH_LONG).show();

        applyBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = useremail.getText().toString().trim();
        String opp_title=title.getText().toString().trim();
        String opp_desc=description.getText().toString().trim();
        String opp_location=location.getText().toString().trim();
        String opp_to=to.getText().toString().trim();
        String opp_from=from.getText().toString().trim();

        final String dateEx ="^\\d{2}\\/\\d{2}\\/(\\d{2}|\\d{4})$";
        Pattern date_pattern;
        Matcher to_matcher,from_matcher;
        date_pattern = Pattern.compile(dateEx);
        to_matcher = date_pattern.matcher(opp_to);
        from_matcher = date_pattern.matcher(opp_from);

        if (imageUri == null) {
            Toast.makeText(getBaseContext(), "Please Select a Post image", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            useremail.setError("enter a valid email address");
            valid = false;
        } else {
            useremail.setError(null);
        }

        if (opp_title.isEmpty() || opp_title.length() < 5) {
            title.setError("at least 5 characters");
            valid = false;
        } else {
            title.setError(null);
        }

        if (opp_desc.isEmpty() || opp_desc.length() < 10 || opp_desc.length() > 50) {
            description.setError("Description can contain minimum 10 and maximum 50 characters");
            valid = false;
        } else {
            description.setError(null);
        }

        if (opp_location.isEmpty() ) {
            location.setError("Please provide a location");
            valid = false;
        } else {
            location.setError(null);
        }

        if (opp_from.isEmpty() || !from_matcher.matches() ) {
            from.setError("Date format is wrong eg:DD/MM/YY");
            valid = false;
        } else {
            from.setError(null);
        }

        if (opp_to.isEmpty() || !to_matcher.matches() ) {
            to.setError("Date format is wrong eg:DD/MM/YY");
            valid = false;
        } else {
            to.setError(null);
        }


        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){

           Uri mImageUri=data.getData();
            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(2,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                selectImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}