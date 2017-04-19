package com.example.umesh.teachforindia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.umesh.teachforindia.R.id.signup;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user != null) {
            startActivity(new Intent(MainActivity.this, LoginPage.class));
            finish();
        }

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        StartAnimations();
        Button btn_signin,btn_signup,btn_opportunity;

        btn_signin=(Button) findViewById(R.id.signin);
        btn_signup=(Button) findViewById(R.id.signup);
        btn_opportunity=(Button) findViewById(R.id.browse);
        btn_signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signin activity
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signin activity
                Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        btn_opportunity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(MainActivity.this,OpportunityActivity.class);
                startActivity(i);
            }
        });



    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(MainActivity.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    MainActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    MainActivity.this.finish();
                }

            }
        };

    }

}
