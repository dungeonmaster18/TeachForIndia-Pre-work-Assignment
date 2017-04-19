package com.example.umesh.teachforindia;

/**
 * Created by umesh on 08-Apr-17.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText useremail, pass, cpass, username,empid;
    private Button signupbtn,loginbtn;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user != null) {
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        }

        setContentView(R.layout.signup);
        //Get Firebase auth instance

        useremail = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);
        username = (EditText) findViewById(R.id.input_name);
        cpass=(EditText) findViewById(R.id.confirm_password);
        empid=(EditText) findViewById(R.id.employee_id);
        signupbtn = (Button) findViewById(R.id.btn_signup);
        loginbtn=(Button) findViewById(R.id.btn_signin);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });

        final String name = username.getText().toString().trim();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(user!=null){
//                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                            .setDisplayName(name).build();
//                    user.updateProfile(profileUpdates);
//                }
//            }
//        };

    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupbtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.ThemeOverlay_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = username.getText().toString().trim();
        final String email = useremail.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String employeeid=empid.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,"Account already exists",
                                    Toast.LENGTH_SHORT).show();
                            signupbtn.setEnabled(true);
                        }
                        else{
                            String uid= mAuth.getCurrentUser().getUid();
                            FirebaseUser user=mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            mFirebaseDatabase = mFirebaseInstance.getReference().child("users").push();
                            mFirebaseDatabase.child("name").setValue(name);
                            mFirebaseDatabase.child("employeeid").setValue(employeeid);
                            mFirebaseDatabase.child("emailid").setValue(email);
                            mFirebaseDatabase.child("uid").setValue(uid);
                            onSignupSuccess();
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,
                                    "Successfully Registered " ,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        //onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        if(mAuthListener != null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }


    public void onSignupSuccess() {
        final FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
//                            Toast.makeText(RegisterActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("TAG", "sendEmailVerification", task.getException());
//                            Toast.makeText(RegisterActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        signupbtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        signupbtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = username.getText().toString().trim();
        String email = useremail.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String cpassword=cpass.getText().toString().trim();
        String employeeid=empid.getText().toString().trim();

        final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}";
        Pattern pass_pattern;
        Matcher pass_matcher;

        pass_pattern = Pattern.compile(PASSWORD_PATTERN);
        pass_matcher = pass_pattern.matcher(password);

        if (name.isEmpty() || name.length() < 3) {
            username.setError("at least 3 characters");
            valid = false;
        } else {
            username.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            useremail.setError("enter a valid email address");
            valid = false;
        } else {
            useremail.setError(null);
        }

        if (password.isEmpty()  || !pass_matcher.matches()) {
            pass.setError("Password must contain atleast a Capital Letter[A-z},atleast a number[0-9],at least one lowercase letter[a-z] & a special character between 8 to 20");
            valid = false;
        } else {
            pass.setError(null);
        }

        if (cpassword.isEmpty() || !cpassword.equals(password)) {
            cpass.setError("Password doesn't matches");
            valid = false;
        } else {
            cpass.setError(null);
        }

        if (employeeid.isEmpty()) {
            empid.setError("Please Enter a Valid EmployeeID");
            valid = false;
        } else {
            empid.setError(null);
        }

        return valid;
    }
}

