package tagbin.in.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tagbin.in.myapplication.Database.DatabaseOperations;
import tagbin.in.myapplication.Database.TableData;
import tagbin.in.myapplication.Gcm.Config;
import tagbin.in.myapplication.Gcm.ShareExternalServer;
import tagbin.in.myapplication.Volley.AppController;
import tagbin.in.myapplication.Volley.CustomRequest;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.LOCATION_HARDWARE;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    EditText number,password;
    String mNumber,mPassword;
    String auth_key,mAuth_key;
    SharedPreferences sharedPreferences;
    public static String LOGINDETAILS= "loginDetails";
    String url = Config.BASE_URL+"login/";
    ////////////////////GCM WORKING/////////////////////////////////
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String STOREGCMID="gcmRegid";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    List categories;
    List idList;


    String SENDER_ID = "604246263412";//Project number
    AlertDialog alert;
    TextView messageView;

    Dialog dialog;
    String GcmRegistration="";
    GoogleCloudMessaging gcm;
    //    AtomicInteger msgId = new AtomicInteger();
//    SharedPreferences prefs;
    Context context;
    Spinner carSpinner;
//    EditText mDisplay;
//    Button send;

    String regid ="";
    static final String TAG = "GCM";
    String selectedCar_type;
    String selected_id;
    /////////
    ShareExternalServer appUtil;
    //    String regId;
    AsyncTask<Void, Void, String> shareRegidTask;
    TextView tvDialog;
    ProgressBar progressBar;
    ////////

    ///////////////////////GCM END/////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        number= (EditText) findViewById(R.id.number);
        password= (EditText) findViewById(R.id.password);
        sharedPreferences = getSharedPreferences(LOGINDETAILS, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customDialog();
//        gcmInitialise();



    }
    public void customDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView)customView.findViewById(R.id.tvdialog);
        progressBar= (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void showDialog(){

        alert.show();
        messageView.setText("Loading");
    }
    public void dismissDialog(){
        alert.dismiss();
    }

    public void login(View view) {
        mNumber = number.getText().toString();
        mPassword = password.getText().toString();
     //   mAuth_key=
        showDialog();
//        if (GcmRegistration.equals("")){
//            gcmInitialise();
//        }else {
            makeJsonObjReq(mNumber, mPassword,GcmRegistration);
//        }



    }

    public void register(View view){
        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }

    private void makeJsonObjReq(final String username,final String password,String gcmregid) {



        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("username", username);
        postParam.put("password", password);
//        postParam.put("gcm_regid", gcmregid);

        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());

                        try {
                            String key= response.getString("key");
                            String success= response.getString("success");
                            String username= response.getString("username");
                           String saveuser= sharedPreferences.getString("username", "nothing");
                            if (username.equals(saveuser)){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("auth_key",key);
                                editor.commit();
                            }else {
                                clearAllPrefs();
                                DatabaseOperations dop = new DatabaseOperations(LoginActivity.this);
                                dop.eraseData(dop, TableData.Tableinfo.TABLE_NAME);
                                dop.eraseData(dop, TableData.Tableinfo.LOC_TABLE_NAME);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", username);
                                editor.putString("auth_key",key);
                                editor.putString("started","false");
                                editor.putString("arrived","false");
                                editor.commit();

                            }

                               Log.d("sharedPreferences", sharedPreferences.getAll().toString());
                            Intent i = new Intent(LoginActivity.this,StartService.class);
                            startActivity(i);
                            finish();
                            dismissDialog();
//                            if (key.equals("")){
//                                Log.d("key",key);
//
//                            }else {
//                                editor.putString("key", key);
//                                editor.putString("username", username);
//                                editor.commit();
//                               Log.d("sharedPreferences", sharedPreferences.getAll().toString());
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Log.d("error", error.toString());
                displayErrors(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    public void clearAllPrefs(){
        final SharedPreferences prefs = getSharedPreferences(
                Registration.STOREGCMID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        SharedPreferences  loginDetails = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor lEditor= loginDetails.edit();
        lEditor.clear();
        lEditor.commit();

    }

    public void gcmInitialise(){
        ////////////////////////GCM START/////////////////////////
        context = getApplicationContext();
        appUtil = new ShareExternalServer();

        showDialog();
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
//            mDisplay.append("\nPlay Service Exist\n");
            gcm = GoogleCloudMessaging.getInstance(this);
            //regid = getRegistrationId(context);
           // Log.d("getRegis", regid);
            registerInBackground();
//            if (regid.isEmpty()) {
//                Log.d("isEmpty",regid);
//                registerInBackground();
//
//
//            }else{
////                mDisplay.append("\nFrom Shared Pref " + regid);
//                GcmRegistration =regid;
//                dismissDialog();
//                sendtowebapp(regid);
//                Log.d("sent",regid);
//            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        ////////////////////////GCM END///////////////////////

    }


    public void displayErrors(VolleyError error){
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
        final SharedPreferences prefs = getSharedPreferences(STOREGCMID, Context.MODE_PRIVATE);
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
//                mDisplay.append(msg + "\n");
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
                    GcmRegistration=regid;
                    dismissDialog();
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
                STOREGCMID, Context.MODE_PRIVATE);
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
//                Toast.makeText(getApplicationContext(), result,
//                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////gcm end//////////////////////////////


}

