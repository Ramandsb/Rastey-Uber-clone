package tagbin.in.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import tagbin.in.myapplication.Database.DatabaseOperations;
import tagbin.in.myapplication.Gcm.Config;
import tagbin.in.myapplication.UpcomingRides.SeeUpcomingRides;
import tagbin.in.myapplication.Volley.AppController;

public class ShowDetailsDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     *
     */
    TextView cab ,tym,pick;
    View status_bar;
    SharedPreferences sharedPreferences;
    String cab_no,time,pickup,user_id,status;
    View buttonsView;
    Button goback;
    String url = Config.BASE_URL + "driver_job/";
    public static final String ARG_ITEM_ID = "item_id";
    View  avail,notAvail;
    TextView messageView;
    ProgressBar progressBar;
    AlertDialog alert;
//    http://192.168.0.4:8001/api/v1/CreateUserResource/pending_job_status/
    DatabaseOperations dop;
    String statusurl = Config.BASE_URL + "pending_job_status/";


    public ShowDetailsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {


            Activity activity = this.getActivity();
sharedPreferences = getActivity().getSharedPreferences(SeeUpcomingRides.SELECTEDRIDEDETAILS, Context.MODE_PRIVATE);
           cab_no= sharedPreferences.getString("cab_no", "");
          time=  sharedPreferences.getString("time","");
         pickup=   sharedPreferences.getString("pickup","");
            user_id=sharedPreferences.getString("user_id","");
            status=sharedPreferences.getString("status","");
            customDialog();
            Log.d("values",cab_no+"///"+time+"///"+user_id+"////"+status);
            makeJsonObjReq();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Click to Start Journey");
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.showdetails_detail, container, false);

        status_bar=rootView.findViewById(R.id.status_bar);
         cab= (TextView) rootView.findViewById(R.id.cab_no);
         tym= (TextView) rootView.findViewById(R.id.time);
         pick= (TextView) rootView.findViewById(R.id.pickup);
        buttonsView= rootView.findViewById(R.id.buttons_view);
        goback = (Button) rootView.findViewById(R.id.gobackBut);
        avail=rootView.findViewById(R.id.available);
        notAvail=rootView.findViewById(R.id.notAvailcontainer);
        FloatingActionButton accept = (FloatingActionButton) rootView.findViewById(R.id.Accept);
        FloatingActionButton reject = (FloatingActionButton) rootView.findViewById(R.id.Reject);
         dop = new DatabaseOperations(getActivity());
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjReq("true");
                showDialog();
                dop.putStatus(dop,"accept",user_id);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjReq("false");
                showDialog();
                dop.putStatus(dop, "reject", user_id);
                dop.deleteRow(dop, user_id);
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dop.deleteRow(dop,user_id);
                getActivity().finish();
            }
        });
        cab.setText(status);
        tym.setText(time);
        pick.setText(pickup);
        if (status.equals("pending")){
            status_bar.setBackgroundColor(getResources().getColor(R.color.red));
        }else if (status.equals("accept")){
            buttonsView.setVisibility(View.INVISIBLE);
            notAvail.setVisibility(View.GONE);
            status_bar.setBackgroundColor(getResources().getColor(R.color.green));

        }
        return rootView;
    }
    public void customDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
    private void makeJsonObjReq(final String string) {
        SharedPreferences  sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        final String Auth_key="ApiKey "+cab_no+":"+sharedPreferences.getString("auth_key","");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("username", cab_no);
        postParam.put("success", string);
        postParam.put("user_id", user_id);
        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("response", response.toString());
                        buttonsView.setVisibility(View.INVISIBLE);
                        if (string.equals("true")){
                            messageView.setText("Ride Accepter");
                            progressBar.setVisibility(View.GONE);
                        }else {
                           if(string.equals("false")){
                                messageView.setText("Ride Rejected");
                               progressBar.setVisibility(View.GONE);
                            }
                        }
                        dismissDialog();



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
                headers.put("Authorization",Auth_key);
                return headers;
            }



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
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
    private void makeJsonObjReq() {
        SharedPreferences  sharedPreferences = getActivity().getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
        final String Auth_key="ApiKey "+cab_no+":"+sharedPreferences.getString("auth_key","");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("user_id", user_id);
        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                statusurl, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("response", response.toString());
                        try {
                            if (response.getString("success").equals("true")){
                                avail.setVisibility(View.VISIBLE);
                                buttonsView.setVisibility(View.GONE);
                                notAvail.setVisibility(View.GONE);
                            }else avail.setVisibility(View.GONE);
                            notAvail.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Log.d("error", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
//                headers.put("Authorization",Auth_key);
                return headers;
            }



        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
}
