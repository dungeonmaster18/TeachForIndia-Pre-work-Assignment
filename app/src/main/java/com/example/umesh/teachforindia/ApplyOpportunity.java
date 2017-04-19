package com.example.umesh.teachforindia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by umesh on 13-Apr-17.
 */

public class ApplyOpportunity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,useremail,mobileno,availablefrom,availableto,reason;
    private Button applyBtn,cancelBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user != null) {
            startActivity(new Intent(ApplyOpportunity.this, LoginPage.class));
            finish();
        }


        setContentView(R.layout.apply_opportunity);

        name = (EditText) findViewById(R.id.input_name);
        useremail = (EditText) findViewById(R.id.input_email);
        mobileno = (EditText) findViewById(R.id.mobile_number);
        availablefrom = (EditText) findViewById(R.id.date_from);
        availableto = (EditText) findViewById(R.id.date_to);
        reason = (EditText) findViewById(R.id.short_description);
        applyBtn = (Button) findViewById(R.id.btn_apply);
        applyBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String email = b.getString("useremail");


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void sendEmail() {
        //Getting content for email

        if(!validate()){
            onApplyFailure();
            return;
        }

        String username=name.getText().toString().trim();
        String email = useremail.getText().toString().trim();
        String mobile_no=mobileno.getText().toString().trim();
        String available_from=availablefrom.getText().toString().trim();
        String available_to=availableto .getText().toString().trim();
        String shortDescription=reason.getText().toString().trim();

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String sendemail=b.getString("useremail").toString().trim();

        String message ="                                 Applicant Deatils"+System.lineSeparator()+System.lineSeparator()+"             Name: "+username+System.lineSeparator()+"             Email: "+email+System.lineSeparator()+"             Mobile NO: "+mobile_no+System.lineSeparator()+"             Available From: "+available_from+System.lineSeparator()+"             Avalaible To: "+available_to+System.lineSeparator()+"             why want to volunteer: "+shortDescription;

        //Creating SendMail object
        SendMail sm = new SendMail(this,sendemail,"Notification For Application", message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onClick(View v) {
        sendEmail();
    }

    public class Config {
        public static final String EMAIL ="dungeonmaster018@gmail.com";
        public static final String PASSWORD ="Umesh@1996";
    }

    public class SendMail extends AsyncTask<Void,Void,Void> {

        //Declaring Variables
        private Context context=getApplicationContext();
        private Session session;

        //Information to send email
        private String email;
        private String subject;
        private String message;

        //Progressdialog to show while sending email
        private ProgressDialog progressDialog ;

        //Class Constructor
        public SendMail(Context context, String email, String subject, String message){
            //Initializing variables
            this.context = context;
            this.email = email;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing progress dialog while sending email
            progressDialog= ProgressDialog.show(context,"Please wait...","Applying For Opportunity...",true);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Dismissing the progress dialog
            progressDialog.dismiss();
            //Showing a success message
            Toast.makeText(context,"Successfully Applied For The Opporturnity!",Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Creating properties
            Properties props = new Properties();

            //Configuring properties for gmail
            //If you are not using gmail you may need to change the values
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            //Creating a new session
            session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                        }
                    });

            try {
                //Creating MimeMessage object
                MimeMessage mm = new MimeMessage(session);

                mm.setFrom(new InternetAddress("noreply@teachforindia-51336.firebaseapp.com", "NoReply-TeachForIndia"));

                mm.setReplyTo(InternetAddress.parse("no_reply@bdcd.com", false));

                mm.setSubject(subject, "UTF-8");

                mm.setText(message, "UTF-8");

                mm.setSentDate(new Date());

                mm.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));

//                //Setting sender address
//                mm.setFrom(new InternetAddress(""));
//                //Adding receiver
//                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
//                //Adding subject
//                mm.setSubject(subject);
//                //Adding message
//                mm.setText(message);

                //Sending email
                Transport.send(mm);

            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void onApplyFailure() {
        Toast.makeText(getBaseContext(), "Attempt To Apply For Opportunity Failed!", Toast.LENGTH_LONG).show();
        applyBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username=name.getText().toString().trim();
        String email = useremail.getText().toString().trim();
        String mobile_no=mobileno.getText().toString().trim();
        String available_from=availablefrom.getText().toString().trim();
        String available_to=availableto .getText().toString().trim();
        String shortDescription=reason.getText().toString().trim();

        final String dateEx ="^\\d{2}\\/\\d{2}\\/(\\d{2}|\\d{4})$";
        Pattern date_pattern;
        Matcher to_matcher,from_matcher;
        date_pattern = Pattern.compile(dateEx);
        to_matcher = date_pattern.matcher(available_to);
        from_matcher = date_pattern.matcher(available_from);

        if (username.isEmpty() || username.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            useremail.setError("enter a valid email address");
            valid = false;
        } else {
            useremail.setError(null);
        }

        if(mobile_no.isEmpty() || mobile_no.length()!=10){

            mobileno.setError("Please Enter your mobile number without country code eg:9874561230");
            valid = false;

        } else {
            mobileno.setError(null);
        }

        if (available_from.isEmpty() || !from_matcher.matches() ) {
            availablefrom.setError("Date format is wrong eg:DD/MM/YY or DD/MM/YYYY");
            valid = false;
        } else {
            availablefrom.setError(null);
        }

        if (available_to.isEmpty() || !to_matcher.matches() ) {
            availableto.setError("Date format is wrong eg:DD/MM/YY or DD/MM/YYYY");
            valid = false;
        } else {
            availableto.setError(null);
        }

        if (shortDescription.isEmpty() || shortDescription.length() < 30 || shortDescription.length() > 50) {
            reason.setError("Description can contain minimum 30 and maximum 50 characters");
            valid = false;
        } else {
            reason.setError(null);
        }

        return valid;
    }
}
