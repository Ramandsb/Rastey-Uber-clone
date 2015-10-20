package tagbin.in.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StartService extends AppCompatActivity implements GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener {

   static Double mylat, mylong; public static final String BROADCAST_ACTION = "Hello World";
    TextView latTv,longTv;
    private GoogleMap mMap;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        Button buttonStartService = (Button)findViewById(R.id.startservice);
        Button buttonStopService = (Button)findViewById(R.id.stopservice);
        latTv = (TextView) findViewById(R.id.lat);
        longTv = (TextView) findViewById(R.id.longi);
        registerReceiver(uiUpdated, new IntentFilter("LOCATION_UPDATED"));
        setUpMapIfNeeded();

        buttonStartService.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(StartService.this, NotifyService.class);
                StartService.this.startService(intent);
            }
        });

        buttonStopService.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setAction(NotifyService.ACTION);
                intent.putExtra("RQS", NotifyService.STOP_SERVICE);
                sendBroadcast(intent);
            }
        });

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Stmap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMarkerDragListener(this);
                mMap.setOnMapLongClickListener(this);
                mMap.setOnMapClickListener(this);

                CameraPosition INIT =
                        new CameraPosition.Builder()
                                .target(new LatLng(19.0222, 72.8666))
                                .zoom(17.5F)
                                .bearing(300F) // orientation
                                .tilt( 50F) // viewing angle
                                .build();

                // use map to move camera into position
                mMap.moveCamera( CameraUpdateFactory.newCameraPosition(INIT) );

                //create initial marker
//                mMap.addMarker( new MarkerOptions()
//                        .position( new LatLng(19.0216, 72.8646) )
//                        .title("Location")
//                        .snippet("First Marker")).showInfoWindow();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private BroadcastReceiver uiUpdated= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

       mylat =     Double.parseDouble(intent.getExtras().getString("myLat"));
       mylong  =   Double.parseDouble(intent.getExtras().getString("myLong"));
            ArrayList<LatLng> latlong = new ArrayList<LatLng>();
//
//            mylat=(intent.getExtras().getString("myLat"));
//            mylong=(Double)(intent.getExtras().getString("myLong"));
            latTv.setText(intent.getExtras().getString("myLat"));
            longTv.setText(intent.getExtras().getString("myLong"));
            Log.d("vals", intent.getExtras().getString("myLat") + "  " + intent.getExtras().getString("myLong"));
//            mMap.addMarker( new MarkerOptions()
//                    .position( new LatLng(mylat, mylong) )
//                    .title("Location")
//                    .snippet("First Marker")).showInfoWindow();
            LatLng latLng = new LatLng(mylat, mylong);
            latlong.add(latLng);
            int i =0;

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latlong.get(i)));

            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong.get(i)));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        }
    };


    @Override
    public void onMapClick(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        mMap.addMarker(new MarkerOptions()
                .position(arg0)
                .draggable(true));

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker arg0) {

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();


    }
}
