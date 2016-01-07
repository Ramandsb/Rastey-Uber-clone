package tagbin.in.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tagbin.in.myapplication.Database.DatabaseOperations;
import tagbin.in.myapplication.Gcm.Config;
import tagbin.in.myapplication.UpcomingRides.SeeUpcomingRides;
import tagbin.in.myapplication.Volley.AppController;


public class ShowDetailsDetailActivity extends AppCompatActivity {
    String cab_no, time, pickup, user_id, status;
    // String url = Config.BASE_URL + "startTrip/";
    SharedPreferences SELECTEDRIDEDETAILS_sharedPreferences;
    SharedPreferences Logindetails_sharedPreferences;
    FloatingActionButton fab;
    DatabaseOperations dop;
    TextView messageView;
    ProgressBar progressBar;
    AlertDialog alert;

    public static boolean show = false;

    String jernydoneUrl = Config.BASE_URL + "cancel/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdetails_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        SELECTEDRIDEDETAILS_sharedPreferences = getSharedPreferences(SeeUpcomingRides.SELECTEDRIDEDETAILS, Context.MODE_PRIVATE);
        Logindetails_sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        cab_no = SELECTEDRIDEDETAILS_sharedPreferences.getString("cab_no", "");
        time = SELECTEDRIDEDETAILS_sharedPreferences.getString("time", "");
        pickup = SELECTEDRIDEDETAILS_sharedPreferences.getString("pickup", "");
        user_id = SELECTEDRIDEDETAILS_sharedPreferences.getString("user_id", "");
        status = SELECTEDRIDEDETAILS_sharedPreferences.getString("status", "");
        dop = new DatabaseOperations(this);
        customDialog();
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                makeJsonObjReq();
//                fab.setVisibility(View.INVISIBLE);
//            }
//        });
//        if (show==true){
//            fab.setVisibility(View.VISIBLE);
//        }else fab.setVisibility(View.INVISIBLE);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ShowDetailsDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ShowDetailsDetailFragment.ARG_ITEM_ID));
            ShowDetailsDetailFragment fragment = new ShowDetailsDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.showdetails_detail_container, fragment)
                    .commit();
        }
    }

    public void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void showDialog() {

        alert.show();
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, SeeUpcomingRides.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cance to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cancel, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(this, SeeUpcomingRides.class);
            startActivity(i);
            finish();
            return true;
        } else if (id == R.id.mnu_cancel) {
            cancelRide();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cancelRide() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        // set title
        alertDialogBuilder.setTitle("Are you Sure ?");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to Cancel the Ride!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showDialog();
                        makeJsonObjReq("yes");
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

    private void makeJsonObjReq(String s) {
        final SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        SharedPreferences sha = getSharedPreferences(SeeUpcomingRides.SELECTEDRIDEDETAILS, Context.MODE_PRIVATE);
        final String user_id = sha.getString("user_id", "");
        String time = sha.getString("time", "");
        String cab_no = sha.getString("cab_no", "");
        String user = sharedPreferences.getString("username", "");
        String name = sha.getString("clientname", "");
        String phone = sha.getString("phone", "");
        final String Auth_key = "ApiKey " + user + ":" + sharedPreferences.getString("auth_key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("user_id", user_id);
        postParam.put("username", cab_no);
        postParam.put("name", name);
        postParam.put("phone", phone);
        postParam.put("time", time);
        postParam.put("trip", "cancel");

        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                jernydoneUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        String message = null;
                        try {
                            message = response.getString("message");
                            dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (message.equals("Unauthorized")) {
                            messageView.setText("User Unauthorized");
                            logoutRequest();
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("started", "false");
                        editor.commit();
                        SharedPreferences.Editor login = sharedPreferences.edit();
                        login.putString("arrived", "false");
                        login.commit();
                        ShowDetailsDetailFragment.show = false;
                        ShowDetailsDetailFragment.arr_show = true;
                        dop.deleteRow(dop, user_id);
                        Intent i = new Intent(ShowDetailsDetailActivity.this, SeeUpcomingRides.class);
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


    public void logoutRequest() {


        final SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final String k = sharedPreferences.getString("key", "");
        final String cab_no = sharedPreferences.getString("username", "");
//        final String apikey=u+":"+k;
//        Log.d("shkey",apikey);
        final String Auth_key = "ApiKey " + cab_no + ":" + sharedPreferences.getString("auth_key", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("cab_no", cab_no);
        postParam.put("logout", "yes");
        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                StartService.Logout_url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("response", response.toString());
//

                        Intent dialogIntent = new Intent(ShowDetailsDetailActivity.this, LoginActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
//                        clearAllPrefs();
                        SharedPreferences.Editor logouteditor = sharedPreferences.edit();
                        logouteditor.putString("auth_key", "");
                        logouteditor.commit();
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

    public void clearAllPrefs() {
        final SharedPreferences prefs = getSharedPreferences(
                Registration.STOREGCMID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        SharedPreferences loginDetails = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor lEditor = loginDetails.edit();
        lEditor.clear();
        lEditor.commit();

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


}