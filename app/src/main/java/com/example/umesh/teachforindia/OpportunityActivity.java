package com.example.umesh.teachforindia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * Created by umesh on 14-Apr-17.
 */

public class OpportunityActivity extends AppCompatActivity {

    private RecyclerView oppList;
    private DatabaseReference mDatabase;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if ( user != null) {
            startActivity(new Intent(OpportunityActivity.this, LoginPage.class));
            finish();
        }

        setContentView(R.layout.opportunity_activity);

        mSwipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.orange, R.color.green );

        oppList=(RecyclerView) findViewById(R.id.opportunity_list);
        oppList.setHasFixedSize(true);
        oppList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase= FirebaseDatabase.getInstance().getReference().child("opportunity");

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("TAG", "onRefresh called from SwipeRefreshLayout");

                        onStart();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Opportunity,OpportunityViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Opportunity, OpportunityViewHolder>(

                Opportunity.class,
                R.layout.opp_row,
                OpportunityViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(OpportunityViewHolder viewHolder, final Opportunity model, int position) {


                viewHolder.setOpportunity_title(model.getOpportunity_title());
                viewHolder.setOpportunity_description(model.getOpportunity_description());
                viewHolder.setUrl(getApplicationContext(),model.getUrl());

                 viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         String title=model.getOpportunity_title().toString().trim();
                         String description=model.getOpportunity_description().toString().trim();
                         String location=model.getOpportunity_location().toString().trim();
                         String start_date=model.getStart_date().toString().trim();
                         String end_date=model.getEnd_date().toString().trim();
                         String email=model.getEmail().toString().trim();
                         String url=model.getUrl();
                         Intent intent=new Intent(OpportunityActivity.this,OpportunityInfoActivity.class);
                         intent.putExtra("title",title);
                         intent.putExtra("description",description);
                         intent.putExtra("location",location);
                         intent.putExtra("start_date",start_date);
                         intent.putExtra("end_date",end_date);
                         intent.putExtra("email",email);
                         intent.putExtra("url",url);
                         startActivity(intent);
                     }
                 });

            }
        };

        oppList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class OpportunityViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public OpportunityViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }

        public void setOpportunity_title(String opportunity_title) {

            TextView opp_title=(TextView) mView.findViewById(R.id.opp_title);
            opp_title.setText(opportunity_title);

        }

        public void setOpportunity_description(String opportunity_description) {

            TextView opp_desc=(TextView) mView.findViewById(R.id.opp_desc);
            opp_desc.setText(opportunity_description);

        }

        public void setUrl(Context ctx, String url) {

            ImageView imageUrl=(ImageView) mView.findViewById(R.id.opp_image);
            Picasso.with(ctx).load(url).into(imageUrl);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.opportunity_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh){
            Intent i = new Intent(OpportunityActivity.this,OpportunityActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
