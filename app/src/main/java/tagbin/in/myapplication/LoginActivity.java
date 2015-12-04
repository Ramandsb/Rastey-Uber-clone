package tagbin.in.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String url ="http://192.168.0.9:8000/api/v1/CreateUserResource/login/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        number= (EditText) findViewById(R.id.number);
        password= (EditText) findViewById(R.id.password);
        sharedPreferences = getSharedPreferences(LOGINDETAILS, Context.MODE_PRIVATE);



    }

    public void login(View view) {
        mNumber = number.getText().toString();
        mPassword = password.getText().toString();
     //   mAuth_key=
      makeJsonObjReq(mNumber, mPassword);

       if(sharedPreferences.getString("key","").equals("")){
           number.setError("LoginFailed");
       }else {
           Log.d("key",sharedPreferences.getString("key",""));
           Intent i = new Intent(LoginActivity.this,StartService.class);
           startActivity(i);

       }

    }
    public void register(View view){
        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }

    private void makeJsonObjReq(final String username,final String password) {



        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("username", username);
        postParam.put("password", password);

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
                            if (key.equals("")){
                                Log.d("key",key);

                            }else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("key", key);
                                editor.putString("username", username);
                                editor.commit();
                               Log.d("vals", sharedPreferences.getAll().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
//                hideProgressDialog();
                Log.d("error", error.toString());
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

