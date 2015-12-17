package tagbin.in.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import junit.framework.TestCase;

public class StartTrip extends AppCompatActivity {
    String cab_no,time,to_loc,pickup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
       cab_no= intent.getExtras().getString("cab_no");
      time=  intent.getExtras().getString("time");
      to_loc=  intent.getExtras().getString("to_loc");
      pickup=  intent.getExtras().getString("pickup");
        TextView cab= (TextView) findViewById(R.id.mcab_no);
        TextView tim= (TextView) findViewById(R.id.mtime);
        TextView to= (TextView) findViewById(R.id.mto_location);
        TextView pickk= (TextView) findViewById(R.id.mpick);
        cab.setText(cab_no);
        tim.setText(time);
        to.setText(to_loc);
        pickk.setText(pickup);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Trip Started", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
