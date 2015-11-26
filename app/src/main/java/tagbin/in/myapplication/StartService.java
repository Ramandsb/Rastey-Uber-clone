package tagbin.in.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import tagbin.in.myapplication.Gcm.ShareExternalServer;

public class StartService extends AppCompatActivity implements GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener,GoogleMap.OnMyLocationButtonClickListener  {

   static Double mylat, mylong;
    public static final String BROADCAST_ACTION = "Hello World";
    TextView latTv,longTv;
    private GoogleMap mMap;
    LatLng latLng;
   static float rotation= 0;
    SensorManager mSensorManager;
    Sensor mSensoracc, mSensormag, mSensorgrav;
    //SensorEventListener myListener;
    static float[] magval, accval, ResVec, accval1 = new float[4];
    static float[] Orival = new float[4];
    static float[] Ri, Ii, Ro = new float[16];
    static double[] Angles = new double[3];
    long currtime = 0;
    Marker marker;
    LatLng start;
//TextView orx,ory,orz;
    ////////////////////GCM WORKING/////////////////////////////////
public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "604246263412";//Project number

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    EditText mDisplay;
    Button send;

    String regid ="";
    static final String TAG = "GCM";
    /////////
    ShareExternalServer appUtil;
    String regId;
    AsyncTask<Void, Void, String> shareRegidTask;
    ////////

    ///////////////////////GCM END/////////////////////////////
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        latTv = (TextView) findViewById(R.id.lat);
        longTv = (TextView) findViewById(R.id.longi);
        mDisplay= (EditText) findViewById(R.id.display);
        registerReceiver(uiUpdated, new IntentFilter("LOCATION_UPDATED"));
        setUpMapIfNeeded();
        //RegisterListeners();
        Intent intent = new Intent(StartService.this, NotifyService.class);
        StartService.this.startService(intent);
        ////////////////////////GCM START/////////////////////////
        context = getApplicationContext();
        appUtil = new ShareExternalServer();

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            mDisplay.append("\nPlay Service Exist\n");
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.d("getRegis",regid);

            if (regid.isEmpty()) {
                Log.d("isEmpty",regid);
                registerInBackground();


            }else{
                mDisplay.append("\nFrom Shared Pref " + regid);
                sendtowebapp(regid);
                Log.d("sent",regid);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        ////////////////////////GCM END///////////////////////


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
                mMap.setBuildingsEnabled(true);
                mMap.setTrafficEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);

               // mMap.setMyLocationEnabled(true);
                //mMap.getUiSettings().setMyLocationButtonEnabled(true);
                 start = new LatLng(28.502683 , 77.085969);
                marker=mMap.addMarker(new MarkerOptions()
                        .flat(true)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.redcar))
                        .anchor(0.5f, 0.5f).position(start));
//                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                    @Override
//                    public void onMyLocationChange(Location location) {
//                        mMap.clear();
//                        marker=mMap.addMarker(new MarkerOptions()
//                                .flat(false)
//                                .icon(BitmapDescriptorFactory
//                                        .fromResource(R.mipmap.carr))
//                                .anchor(0.5f, 0.5f).position(start));
//                        latTv.setText("myLocationChange" + location.getLatitude() + " " + location.getLongitude());
//                        LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
//
//                        animateMarker(marker,ll,false);
//
//
////                        Log.d("rotation",""+orX());
//                    }
//                });
//

//               float vv= (float) Math.toDegrees(orX());
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

            longTv.setText("serviceLoc"+intent.getExtras().getString("myLat") + "  " + intent.getExtras().getString("myLong"));
            Log.d("vals", intent.getExtras().getString("myLat") + "  " + intent.getExtras().getString("myLong"));
//            mMap.addMarker( new MarkerOptions()
//                    .position( new LatLng(mylat, mylong) )
//                    .title("Location")
//                    .snippet("First Marker")).showInfoWindow();
             latLng = new LatLng(mylat, mylong);
            latlong.add(latLng);
            int i =0;
           // marker.setRotation();///
            animateMarker(marker,latLng,false);

//                mMap.clear();
//               Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.car);
//
//
//            mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(latlong.get(i)).icon(BitmapDescriptorFactory.fromBitmap(image)));

            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong.get(i)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong.get(i)));

            // Zoom in the Google Map

//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        }
    };
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0)
                {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
                else
                {
                    if (hideMarker)
                    {
                        marker.setVisible(false);
                    }
                    else
                    {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


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


    @Override
    public boolean onMyLocationButtonClick() {
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";

        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        // return getSharedPreferences(DemoActivity.class.getSimpleName(),
        //         Context.MODE_PRIVATE);
        return null;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>(){


            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.d("register",msg);
                    sendtowebapp(regid);
                    storeRegistrationId(context, regid);
                    //mDisplay.append("here");
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    // sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

        }.execute(null,null,null);

    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

//    public void onClick(final View view) {
//        Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
//        if (view == findViewById(R.id.send)) {
//            Toast.makeText(context, "Send Clicked", Toast.LENGTH_LONG).show();
//            new AsyncTask<Object, Object, Object>() {
//
//                @Override
//                protected Object doInBackground(Object... params) {
//                    String msg = "";
//                    try {
//                        Bundle data = new Bundle();
//                        data.putString("my_message", "Hello World");
//                        data.putString("my_action",
//                                "com.google.android.gcm.demo.app.ECHO_NOW");
//                        String id = Integer.toString(msgId.incrementAndGet());
//                        gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
//                        msg = "Sent message";
//                    } catch (IOException ex) {
//                        msg = "Error :" + ex.getMessage();
//                    }
//                    return msg;
//                }
//
//                @SuppressWarnings("unused")
//                protected void onPostExecute(String msg) {
//                    mDisplay.append(msg + "\n");
//                }
//            }.execute(null, null, null);
//        } else if (view == findViewById(R.id.clear)) {
//            mDisplay.setText("");
//        }



//    }


    public void sendtowebapp(final String registeredid){


        Log.d("MainActivity", "regId: " + registeredid);

        final Context context = this;
        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil.shareRegIdWithAppServer(context, registeredid);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Log.d("onPostExecute",result);
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);
    }
    ////////////////////////////////////////////////////////////////////////////
}
