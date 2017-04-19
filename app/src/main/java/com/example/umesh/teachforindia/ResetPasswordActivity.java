package com.example.umesh.teachforindia;

/**
 * Created by umesh on 11-Apr-17.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText useremail;
    private Button btnReset, btnBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user != null) {
            startActivity(new Intent(ResetPasswordActivity.this, LoginPage.class));
            finish();
        }

        setContentView(R.layout.forget_password);


        useremail = (EditText) findViewById(R.id.input_email);
        btnReset = (Button) findViewById(R.id.btn_reset);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetPassword();
            }
        });
    }

    public void resetPassword(){

        if (!validate()) {
            btnReset.setEnabled(true);
            return;
        }

        btnReset.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordActivity.this,
                R.style.ThemeOverlay_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Reseting...");
        progressDialog.show();

        String email = useremail.getText().toString();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            onResetFailed();
                        }
                        progressDialog.dismiss();
                    }
                });


    }

    public void onResetFailed() {
        Toast.makeText(getBaseContext(), "Password Reset Failed", Toast.LENGTH_LONG).show();
        btnReset.setEnabled(true);
    }

    public void onResetSuccess() {
        btnReset.setEnabled(true);
        finish();
    }
    public boolean validate() {
        boolean valid = true;
        String email = useremail.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            useremail.setError("enter a valid email address");
            valid = false;
        } else {
            useremail.setError(null);
        }

        return valid;
    }


}