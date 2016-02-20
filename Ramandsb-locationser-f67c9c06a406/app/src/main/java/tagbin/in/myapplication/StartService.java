package tagbin.in.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.CharacterPickerDialog;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.github.glomadrian.loadingballs.factory.path.Star;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import tagbin.in.myapplication.Database.DatabaseOperations;
import tagbin.in.myapplication.Database.TableData;
import tagbin.in.myapplication.Gcm.Config;
import tagbin.in.myapplication.Gcm.ShareExternalServer;
import tagbin.in.myapplication.UpcomingRides.MyAdapter;
import tagbin.in.myapplication.UpcomingRides.SeeUpcomingRides;
import tagbin.in.myapplication.Volley.AppController;

public class StartService extends AppCompatActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMyLocationButtonClickListener, NavigationView.OnNavigationItemSelectedListener {

    DatabaseOperations dop;
    public static Double mylat = 0.000, mylong = 0.000;
    public static final String BROADCAST_ACTION = "Hello World";
    TextView latTv, longTv;
    private GoogleMap mMap;
    LatLng latLng;
    Dialog dialog;
    Sensor Sensorrotation;
    Marker marker;
    String usrname;
    LatLng start;
    float[] mRotationMatrix;
    public static String Logout_url = Config.BASE_URL + "logout/";
    String jernydoneUrl = Config.BASE_URL + "endTrip/";
    String getOtpurl = Config.BASE_URL + "otp/";
    SharedPreferences sharedPreferences;
    SharedPreferences login_shared;
    SharedPreferences sha;
    AlertDialog alert;
    TextView messageView;
    ProgressBar progressBar;
    public Button journey;
    public static boolean visible = false;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    TextView navdraname;
    View header;
    String Auth_key;
    String otps;
    String showEndTrip;
    //    String uni="";
    float declination;
    //    SensorEventListener myListener;
    SensorManager mSensorManager;
    Sensor mSensoracc, mSensormag, mSensorgrav;
    //SensorEventListener myListener;
    static float[] magval, accval, ResVec, accval1 = new float[4];
    static float[] Orival = new float[4];
    static float[] Ri, Ii, Ro = new float[16];
    static double[] Angles = new double[3];
    long currtime = 0;

    Sensor sensor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerReceiver(uiUpdated, new IntentFilter("LOCATION_UPDATED"));
        registerReceiver(logout, new IntentFilter("logout"));
        setUpMapIfNeeded();
        Intent intent = new Intent(StartService.this, NotifyService.class);
        StartService.this.startService(intent);
        customDialog();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        login_shared = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        usrname = login_shared.getString("username", "");
        showEndTrip = login_shared.getString("started", "false");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        navdraname = (TextView) header.findViewById(R.id.navdraname);
        navdraname.setText(usrname);
        journey = (Button) findViewById(R.id.Journey);
        journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                makeotpJsonObjReq();

            }
        });
//        if (visible){
//            journey.setVisibility(View.VISIBLE);
//        }else journey.setVisibility(View.INVISIBLE);
        if (showEndTrip.equals("false")) {
            journey.setVisibility(View.INVISIBLE);
        } else if (showEndTrip.equals("true")) {
            journey.setVisibility(View.VISIBLE);
        }

    }

    public void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void endtripDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void showDialog() {

        alert.show();
        progressBar.setVisibility(View.VISIBLE);
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigationdrawer, menu);
        MenuItem rides = menu.findItem(R.id.rides);
        DatabaseOperations dop = new DatabaseOperations(this);
        rides.setTitle(dop.getProfilesCount());
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uiUpdated);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.rides) {
            Intent i = new Intent(this, SeeUpcomingRides.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == android.R.id.home) {

            drawer.openDrawer(GravityCompat.START);
            return true;
        }


        return super.onOptionsItemSelected(item);
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
                mMap.getUiSettings().setTiltGesturesEnabled(false);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                start = new LatLng(28.502683, 77.085969);
                marker = mMap.addMarker(new MarkerOptions()
                        .flat(true)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.myc))
                        .anchor(0.5f, 0.5f).position(start));
//                DraggableCircle draggableCircle = new DraggableCircle(start,100000,mMap);
                int mFillColor = Color.HSVToColor(
                        38, new float[]{Color.parseColor("#55C5FD"), 1, 1});
                mMap.addCircle(new CircleOptions().center(start).radius(100).fillColor(mFillColor).strokeWidth(0));
                CameraPosition INIT =
                        new CameraPosition.Builder()
                                .target(new LatLng(28.502683, 77.085969))
                                .zoom(17.5F)
                                .bearing(300F) // orientation
                                .build();

                // use map to move camera into position
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
                changeCamera(CameraUpdateFactory.newCameraPosition(INIT),null);
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
            }
        }
    }

//    static public void rotateMarker(final Marker marker, final float toRotation, GoogleMap map) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final float startRotation = marker.getRotation();
//        final long duration = 1555;
//
//        final Interpolator interpolator = new LinearInterpolator();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//
//                float rot = t * toRotation + (1 - t) * startRotation;
//
//                marker.setRotation(-rot > 180 ? rot / 2 : rot);
//                if (t < 1.0) {
//                    // Post again 16ms later.
//                    handler.postDelayed(this, 16);
//                }
//            }
//        });
//    }


    private BroadcastReceiver uiUpdated = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            mylat = Double.parseDouble(intent.getExtras().getString("myLat"));
            mylong = Double.parseDouble(intent.getExtras().getString("myLong"));
            declination = intent.getExtras().getFloat("declination");
            ArrayList<LatLng> latlong = new ArrayList<LatLng>();

            Log.d("vals", intent.getExtras().getString("myLat") + "  " + intent.getExtras().getString("myLong"));
            DatabaseOperations dop = new DatabaseOperations(StartService.this);
//            Long tsLong = System.currentTimeMillis()/1000;
//            if (uni.equals("")){
//                uni=tsLong.toString();
//            }else {
//                uni=uni;
//            }
////            String ts = tsLong.toString();
            Log.d("LocationCount is : ", dop.getlatlngCount());

            latLng = new LatLng(mylat, mylong);
            latlong.add(latLng);
            int i = 0;
            // marker.setRotation();///
            animateMarker(marker, latLng, false);

//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong.get(i)));
            changeCamera(CameraUpdateFactory.newLatLng(latlong.get(i)),null);


        }
    };
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {

                int duration = 2000;

                mMap.animateCamera(update, Math.max(duration, 1), callback);




    }
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
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
//        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();


    }


    @Override
    public boolean onMyLocationButtonClick() {
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(logout, new IntentFilter("logout"));
    }


    public void logoutdialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        // set title
        alertDialogBuilder.setTitle("Are you Sure ?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to Logout!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showDialog();
                        logoutRequest();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void logoutRequest() {


        final SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        final String k = sharedPreferences.getString("key", "");
        final String cab_no = sharedPreferences.getString("username", "");
//        final String apikey=u+":"+k;
//        Log.d("shkey",apikey);
        Auth_key = "ApiKey " + cab_no + ":" + sharedPreferences.getString("auth_key", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("cab_no", cab_no);
        postParam.put("logout", "yes");
        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Logout_url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("response", response.toString());

                        dismissDialog();
//                        clearAllPrefs();
                        SharedPreferences.Editor logouteditor = sharedPreferences.edit();
                        logouteditor.putString("auth_key", "");
                        logouteditor.commit();
                        stopservice();
                        Intent i = new Intent(StartService.this, LoginActivity.class);
                        startActivity(i);
                        finish();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                displayErrors(error);
                Log.d("error", error.toString());
            }
        }) {

//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                return headers;
//            }
//            @Override
//            public Map<String, String> getParams() {
//                HashMap<String, String> params = new HashMap<String, String>();
//                params.put("key",k);
//
//                return params;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", Auth_key);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    private final BroadcastReceiver logout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    public void otpdialog() {

        LayoutInflater li = LayoutInflater.from(StartService.this);
        View promptsView = li.inflate(R.layout.otpdialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                StartService.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setTitle("Enter OTP to End Trip")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                 otps =  userInput.getText().toString();
                                showDialog();
                                makeJsonObjReq(otps);
                                Log.d("username", usrname);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(logout);
    }

    private void makeJsonObjReq(String otp) {
        final SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        sha = getSharedPreferences(SeeUpcomingRides.SELECTEDRIDEDETAILS, Context.MODE_PRIVATE);
        String phone = sha.getString("phone", "");
        String clientname = sha.getString("clientname", "");
        final String user_id = sha.getString("user_id", "");
        String cab_no = sha.getString("cab_no", "");
        String user = sharedPreferences.getString("username", "");
        final String Auth_key = "ApiKey " + user + ":" + sharedPreferences.getString("auth_key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("user_id", user_id);
        postParam.put("username", cab_no);
        postParam.put("lat", mylat.toString());
        postParam.put("lng", mylong.toString());
        postParam.put("client_name", clientname);
        postParam.put("phone", phone);
        postParam.put("trip", "End");
        postParam.put("otp", otp);

        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                jernydoneUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        String message=null;String success = null;
                        try {
                            message = response.getString("message");
                            success= response.getString("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (message.equals("Unauthorized")) {
                            logoutRequest();
                        }
                        if (success.equals("true")) {
                            progressBar.setVisibility(View.GONE);
                            messageView.setText("Job Finished");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("started", "false");
                            editor.commit();
                            SharedPreferences.Editor login = login_shared.edit();
                            login.putString("arrived", "false");
                            login.commit();
//                        visible=false;
                            DatabaseOperations dop = new DatabaseOperations(StartService.this);
                            dop.eraseData(dop, TableData.Tableinfo.LOC_TABLE_NAME);
                            ShowDetailsDetailFragment.show = false;
                            ShowDetailsDetailFragment.arr_show = true;
                            dop = new DatabaseOperations(StartService.this);
                            dop.deleteRow(dop, user_id);
                            finish();
                            Intent i = getIntent();
                            startActivity(i);
                            NotifyService.putLatln = false;
                            journey.setVisibility(View.INVISIBLE);
                        }else {
                            progressBar.setVisibility(View.GONE);
                            messageView.setText(message);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                displayErrors(error);
                Log.d("error", error.toString());
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", Auth_key);
                return headers;
            }


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makeotpJsonObjReq() {
        final SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        sha = getSharedPreferences(SeeUpcomingRides.SELECTEDRIDEDETAILS, Context.MODE_PRIVATE);
        final String user_id = sha.getString("user_id", "");
        String phone = sha.getString("phone", "");
        String cab_no = sha.getString("cab_no", "");
        String user = sharedPreferences.getString("username", "");
        final String Auth_key = "ApiKey " + user + ":" + sharedPreferences.getString("auth_key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("user_id", user_id);
        postParam.put("phone", phone);
        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                getOtpurl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        String message = null;
                        try {
                            message = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (message.equals("Unauthorized")) {
                            logoutRequest();
                        }
                        dismissDialog();
                        otpdialog();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                displayErrors(error);
                Log.d("error", error.toString());
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", Auth_key);
                return headers;
            }


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void displayErrors(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }

    public void stopservice() {
        NotifyService.request = false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }






    private void updateCamera(float bearing) {
        CameraPosition oldPos = mMap.getCameraPosition();

        CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(this, SeeUpcomingRides.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_gallery) {
            logoutdialog();
        } else if (id == R.id.call) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01148844884"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    ////////////////////////////////


}