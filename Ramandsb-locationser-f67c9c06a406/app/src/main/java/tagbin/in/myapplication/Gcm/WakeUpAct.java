package tagbin.in.myapplication.Gcm;


import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tagbin.in.myapplication.LoginActivity;
import tagbin.in.myapplication.R;
import tagbin.in.myapplication.StartService;
import tagbin.in.myapplication.Volley.AppController;

public class WakeUpAct extends Activity {
    PowerManager pm;
    PowerManager.WakeLock wl;
    KeyguardManager km;
    KeyguardManager.KeyguardLock kl;
    TextView cab_no,time,from,to;
    String url =Config.BASE_URL+"driver_job/";
    String username;
    TextView tvDialog;
    ProgressBar progressBar;
    AlertDialog alert;
    TextView messageView;
    StartService startService;String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wake_up);
        Log.i("INFO", "onCreate() in DismissLock");
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        km=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        kl=km.newKeyguardLock("INFO");
        customDialog();
        tvDialog= (TextView) findViewById(R.id.tvdialog);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("message");
            cab_no = (TextView) findViewById(R.id.mcab_no);
            time = (TextView) findViewById(R.id.mtime);
            from = (TextView) findViewById(R.id.from);
            to = (TextView) findViewById(R.id.to);
            try {
                JSONObject jsonObject = new JSONObject(value);
              String cab= (String) jsonObject.get("cab_no");
                cab_no.setText(cab);
                String froms= (String)  jsonObject.get("from");
                from.setText(froms);
                String times= (String)   jsonObject.get("time");
                time.setText(times);
                 user_id= (String)   jsonObject.get("user_id");
                Log.d("seperatedJson",""+cab+"//"+froms+"//"+user_id+"//"+times);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("vsl",value);
        }
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire(); //wake up the screen
        kl.disableKeyguard();// dismiss the keyguard
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ring);
        mp.start();

         startService= new StartService();
        FloatingActionButton accept = (FloatingActionButton) findViewById(R.id.Accept);
        FloatingActionButton reject = (FloatingActionButton) findViewById(R.id.Reject);
        SharedPreferences  sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();

                showDialog();
                makeJsonObjReq("true");
                StartService.visible =true;



            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                showDialog();
                makeJsonObjReq("false");
                StartService.visible=false;
            }
        });

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
    }
    public void dismissDialog(){
        alert.dismiss();
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
    public void redirectTomap(){
        Intent i = new Intent(WakeUpAct.this, StartService.class);
        startActivity(i);
        WakeUpAct.this.finish();
    }
    public void redirectTologin(){
        Intent i = new Intent(WakeUpAct.this, LoginActivity.class);
        startActivity(i);
        WakeUpAct.this.finish();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        wl.release(); //when the activiy pauses, we should realse the wakelock
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        wl.acquire();//must call this!
    }
    private void makeJsonObjReq(String response) {



        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("username",username);
        postParam.put("success", response);
        postParam.put("user_id",user_id);
        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());



        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response){

                        Log.d("response", response.toString());
                        try {

                            if (response.getString("success").equals("true")){

                                dismissDialog();
                                if (username.equals("")){
                                    redirectTologin();
                                }else {
                                    redirectTomap();
                                }
                            }else {
                                dismissDialog();
                                if (username.equals("")){
                                    redirectTologin();
                                }else {
                                    redirectTomap();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                displayErrors(error);
                Log.d("error",error.toString());
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

}