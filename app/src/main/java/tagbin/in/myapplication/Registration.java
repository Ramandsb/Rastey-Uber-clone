package tagbin.in.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tagbin.in.myapplication.Volley.AppController;
import tagbin.in.myapplication.Volley.CustomRequest;
import tagbin.in.myapplication.Volley.MakeRequest;
public class Registration extends AppCompatActivity {
    EditText name, mobno, cabno, password, conpass;
    String mName, mMobno, mCabno, mPass, mConpass;
    String url ="http://192.168.0.9:8000/api/v1/CreateUserResource/create_user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText) findViewById(R.id.name);
        mobno = (EditText) findViewById(R.id.num);
        cabno = (EditText) findViewById(R.id.cabno);
        password = (EditText) findViewById(R.id.password);
        conpass = (EditText) findViewById(R.id.conPass);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = name.getText().toString();
                mMobno = mobno.getText().toString();
                mCabno = cabno.getText().toString();
                mPass = password.getText().toString();
                mConpass = conpass.getText().toString();
                if (mPass.equals(mConpass)) {
//                    MakeRequest makeRequest = new MakeRequest(Registration.this, "Pass Url");
                    makeJsonObjReq(mName, mMobno, mCabno, mPass);
//                    Intent i = new Intent(Registration.this, LoginActivity.class);
//                    startActivity(i);
                } else password.setError("Password not match");
                // finish();
            }
        });
    }

    private void makeJsonObjReq(String name,String username,String cab_no,String password) {



        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("username", username);
        postParam.put("name", name);
        postParam.put("cab_no", cab_no);
        postParam.put("password", password);

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
                                finish();
                            }else Toast.makeText(Registration.this,"Sign up Error",Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
//                hideProgressDialog();
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
