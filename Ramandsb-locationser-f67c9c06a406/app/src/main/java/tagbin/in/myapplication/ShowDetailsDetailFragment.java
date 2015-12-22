package tagbin.in.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tagbin.in.myapplication.UpcomingRides.SeeUpcomingRides;

public class ShowDetailsDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     *
     */
    TextView cab ,tym,pick;
    SharedPreferences sharedPreferences;
    String cab_no,time,pickup,user_id;
    public static final String ARG_ITEM_ID = "item_id";


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
            Log.d("values",cab_no+"///"+time+"///"+pickup);
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
         cab= (TextView) rootView.findViewById(R.id.cab_no);
         tym= (TextView) rootView.findViewById(R.id.time);
         pick= (TextView) rootView.findViewById(R.id.pickup);
        cab.setText(cab_no);
        tym.setText(time);
        pick.setText(pickup);
        return rootView;
    }
}
