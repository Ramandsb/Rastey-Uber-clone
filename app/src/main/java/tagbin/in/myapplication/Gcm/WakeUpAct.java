package tagbin.in.myapplication.Gcm;


import android.app.KeyguardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tagbin.in.myapplication.R;

public class WakeUpAct extends Activity {
    PowerManager pm;
    PowerManager.WakeLock wl;
    KeyguardManager km;
    KeyguardManager.KeyguardLock kl;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up);

        Log.i("INFO", "onCreate() in DismissLock");
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        km=(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        kl=km.newKeyguardLock("INFO");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("message");
            tv = (TextView) findViewById(R.id.mes);
            tv.setText(value);
            Log.d("vsl",value);
        }
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire(); //wake up the screen
        kl.disableKeyguard();// dismiss the keyguard
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ring);
        mp.start();
        Button b1 = (Button) findViewById(R.id.Accept);
        Button b2 = (Button) findViewById(R.id.Reject);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
            }
        });




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

}