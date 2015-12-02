package tagbin.in.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import tagbin.in.myapplication.Volley.MakeRequest;
public class Registration extends AppCompatActivity {
    EditText name,mobno,cabno,password,conpass;
    String mName,mMobno,mCabno,mPass,mConpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText) findViewById(R.id.name);
        mobno= (EditText) findViewById(R.id.num);
        cabno= (EditText) findViewById(R.id.cabno);
        password= (EditText) findViewById(R.id.password);
        conpass= (EditText) findViewById(R.id.conPass);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mName= name.getText().toString();
               mMobno=mobno.getText().toString();
               mCabno= cabno.getText().toString();
               mPass= password.getText().toString();
              mConpass=  conpass.getText().toString();
                if (mPass.equals(mConpass)) {
                    MakeRequest makeRequest = new MakeRequest(Registration.this, "Pass Url");
                    makeRequest.makeRequest(mName, mMobno, mCabno, mPass,mConpass);
                    Intent i = new Intent(Registration.this, LoginActivity.class);
                    startActivity(i);
                }else password.setError("Password not match");
                finish();
            }
        });
    }

}
