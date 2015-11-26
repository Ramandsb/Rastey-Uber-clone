package tagbin.in.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    String drivername, vehicalno, LoginStatus, User_type, First_name = null;
    EditText name,cabno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name= (EditText) findViewById(R.id.name);
        cabno= (EditText) findViewById(R.id.vehicalno);
    }

    public void login(View v){
        drivername= name.getText().toString();

        vehicalno= cabno.getText().toString();
        Intent i = new Intent(MainActivity.this,StartService.class);
                startActivity(i);

    }

}
