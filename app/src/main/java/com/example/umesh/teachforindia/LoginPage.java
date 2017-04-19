package com.example.umesh.teachforindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by umesh on 14-Apr-17.
 */

public class LoginPage extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView welcomeMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user == null) {
            startActivity(new Intent(LoginPage.this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.profile_activity);

        welcomeMessage=(TextView) findViewById(R.id.welcome);

        String name= mAuth.getCurrentUser().getDisplayName();

        String welcome="Welcome!"+name;

        welcomeMessage.setText(welcome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create){
            startActivity(new Intent(LoginPage.this,CreateOpportunity.class));
        }

        else if (item.getItemId() == R.id.action_logout){
            mAuth.signOut();
            startActivity(new Intent(LoginPage.this,LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
