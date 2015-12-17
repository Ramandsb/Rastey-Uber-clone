package tagbin.in.myapplication.UpcomingRides;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tagbin.in.myapplication.Database.DatabaseOperations;
import tagbin.in.myapplication.Database.TableData;
import tagbin.in.myapplication.R;
import tagbin.in.myapplication.StartTrip;

public class SeeUpcomingRides extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private MyAdapter mAdapter;
    private List<DataItems> arrayList = new ArrayList<DataItems>();
    private List<DataItems> databaselist = new ArrayList<DataItems>();
    Button click;
    DataItems dataItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_upcoming_rides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerview= (RecyclerView) findViewById(R.id.Reclist);
        click = (Button) findViewById(R.id.click);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new MyAdapter(this);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setHasFixedSize(false);
        mRecyclerview.setLayoutManager(linearLayoutManager);

//        click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               Log.d("clicked",""+mRecyclerview.getChildAdapterPosition(v));
//            }
//        });
//        final GestureDetector mGestureDetector = new GestureDetector(SeeUpcomingRides.this, new GestureDetector.SimpleOnGestureListener() {
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return true;
//            }
//
        arrayList = new ArrayList();
//        });
        ItemClickSupport.addTo(mRecyclerview).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
               dataItems= databaselist.get(position);
                String cab_no = dataItems.getCab_no();
                String time = dataItems.getTime();
                String to_loc = dataItems.getTo_loc();
                String pickup = dataItems.getPick();
                Intent intent = new Intent(SeeUpcomingRides.this, StartTrip.class);
                intent.putExtra("cab_no",cab_no);
                intent.putExtra("time",time);
                intent.putExtra("to_loc",to_loc);
                intent.putExtra("pickup",pickup);
                startActivity(intent);
                Toast.makeText(SeeUpcomingRides.this,"clicked"+position+"  //"+dataItems.getCab_no(),Toast.LENGTH_LONG).show();
            }
        });


        final DatabaseOperations dop = new DatabaseOperations(this);
        dop.eraseData(dop);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i= 0;i<10;i++){
                    dop.putInformation(dop,"cab_no :"+i,"Time :"+i+":"+i,"Delhi :"+i,"Gurgaon :"+i);

                }
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaselist= dop.readData(dop);
                mAdapter.setData((ArrayList<DataItems>) databaselist, true);
            }
        });

    }

}
