package com.example.umesh.teachforindia;

/**
 * Created by umesh on 19-Apr-17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class SortOpportunityActivity extends AppCompatActivity {

    Button submitBtn;
    public String selection;
    Spinner sortbySpinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_opportunity);

        sortbySpinner=(Spinner) findViewById(R.id.select_opportunity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sortby_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortbySpinner.setAdapter(adapter);

        submitBtn=(Button) findViewById(R.id.btn_submit);

        sortbySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection=sortbySpinner.getSelectedItem().toString().trim();
                Log.v("TAG",selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selection.equals("Location")){
                    selection="opportunity_location";
                }
                else if(selection.equals("Title")){
                    selection="opportunity_title";
                }
                else if(selection.equals("Start Date")){
                    selection="start_date";
                }
                else if(selection.equals("None")){
                    selection="none";
                }
                else if (selection.equals("End Date")){
                    selection="end_date";
                }

                Intent intent=new Intent(SortOpportunityActivity.this,OpportunityActivity.class);
                intent.putExtra("select",selection);
                startActivity(intent);
            }
        });


    }
}
