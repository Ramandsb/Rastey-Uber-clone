package tagbin.in.myapplication;

import tagbin.in.myapplication.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashActivity extends Activity {
    private ProgressBar progressBar;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progress);


    }

    @Override
    public void onStart() {
        // When the aplication starts, show the progressbar for 2 seconds. After that, execute loadHomeActivity runnable.
        long mStartTime = 0;
        if (mStartTime == 0L) {
            mStartTime = System.currentTimeMillis();
            mHandler.removeCallbacks(loadHomeActivity);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            progressBar.setProgress(0);
            mHandler.postDelayed(loadHomeActivity, 3000);
        }
        super.onStart();
    }

    // A runnable executed when the progressbar finishes which starts the HomeActivity.
    private Runnable loadHomeActivity = new Runnable() {
        public void run() {
            SharedPreferences  sharedPreferences = getSharedPreferences(LoginActivity.LOGINDETAILS, Context.MODE_PRIVATE);
         String   username=sharedPreferences.getString("username", "");

            if (username.equals("")) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }else {
                Intent i = new Intent(SplashActivity.this, StartService.class);
                startActivity(i);
                finish();

            }
        }

    };


}
