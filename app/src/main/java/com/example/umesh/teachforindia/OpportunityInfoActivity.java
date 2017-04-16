package com.example.umesh.teachforindia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by umesh on 15-Apr-17.
 */

public class OpportunityInfoActivity extends AppCompatActivity {

    private TextView title,description,location,startDate,endDate,email;
    private Button applyBtn;
    private ImageView oppImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.opportunity_info);

        title=(TextView) findViewById(R.id.opportunity_title);
        description=(TextView) findViewById(R.id.opportunity_description);
        location=(TextView) findViewById(R.id.opportunity_location);
        startDate=(TextView) findViewById(R.id.start_date);
        endDate=(TextView) findViewById(R.id.end_date);
        email=(TextView) findViewById(R.id.email_id) ;
        oppImage=(ImageView) findViewById(R.id.opp_image);
        applyBtn=(Button) findViewById(R.id.btn_apply);


        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String opptitle = b.getString("title");
        String oppdesc = b.getString("description");
        String opplocation = b.getString("location");
        String start_Date = b.getString("start_date");
        String end_Date = b.getString("end_date");
        final String useremail=b.getString("email");
        String image=b.getString("url");
        title.setText(opptitle);
        description.setText(oppdesc);
        location.setText(opplocation);
        startDate.setText(start_Date);
        endDate.setText(end_Date);
        email.setText(useremail);

        Picasso.with(getApplicationContext())
                .load(image)
                .into(oppImage);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(OpportunityInfoActivity.this,ApplyOpportunity.class);
                intent.putExtra("useremail",useremail);
                startActivity(intent);
            }
        });
    }

}

