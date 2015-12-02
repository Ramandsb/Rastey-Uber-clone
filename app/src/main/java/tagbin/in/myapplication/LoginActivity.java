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
        mAuth_key=makeRequest(mNumber, mPassword);
        if (mAuth_key.equals("")) {

            number.setError("UserName Not Registered");

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("auth_key",mAuth_key);
            editor.putString("username",mNumber);
            editor.putString("password",mPassword);
            editor.commit();
            Intent i = new Intent(this, StartService.class);
            startActivity(i);
        }
    }
    public void register(View view){
        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }

    public String makeRequest(final String number,final String password) {


        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, "pas url",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                             auth_key = response.getString("auth_key");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.v("writtem:%n %s", response.toString());

                    }

                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("error:%n %s", error.toString());


                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("number", number);
                params.put("password", password);

                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("header1", "header1");
                headers.put("header2", "header2");

                return headers;
            }


        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsObjRequest);
//        requestQueue.cancelAll(tag);

        return auth_key;
    }


}

