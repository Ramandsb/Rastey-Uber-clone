package tagbin.in.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String username, password, LoginStatus, User_type, First_name = null;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Auth_keypref = "MyAuth";
    public static final String Statuspref = "MyStatus";
    public static final String User_Typepref = "MyUsertype";
    public static final String User_Namepref = "MyUserName";
    SharedPreferences sharedpreferences;
    private String urlJsonObj = "http://192.168.2.5";

    // json array response url
    private String urlJsonArry = "http://192.168.2.5/login.php";
    String Auth_key = "";
    String Auth_key1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v){
        Intent i = new Intent(MainActivity.this,StartService.class);
                startActivity(i);

    }

}
