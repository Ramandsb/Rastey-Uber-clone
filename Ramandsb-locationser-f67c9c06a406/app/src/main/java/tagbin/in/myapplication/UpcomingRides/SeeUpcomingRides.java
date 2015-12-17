package tagbin.in.myapplication.UpcomingRides;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import tagbin.in.myapplication.Database.DatabaseOperations;
import tagbin.in.myapplication.Database.TableData;
import tagbin.in.myapplication.R;

public class SeeUpcomingRides extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    ArrayList arrayList;
    private MyAdapter mAdapter;
    private List<DataItems> resList = new ArrayList<DataItems>();
    private List<DataItems> databaselist = new ArrayList<DataItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_upcoming_rides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerview= (RecyclerView) findViewById(R.id.Reclist);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new MyAdapter(this);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setHasFixedSize(false);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList();

        final DatabaseOperations dop = new DatabaseOperations(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i= 0;i<10;i++){
                    dop.putInformation(dop,"cab_no :"+i,i+":"+i,"Delhi :"+i,"Gurgaon :"+i);

                }
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaselist= dop.readData(dop);
                mAdapter.setMovies((ArrayList<DataItems>) databaselist, true);
            }
        });

    }

}
