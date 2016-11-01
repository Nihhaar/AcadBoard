package iitb.nihhaar.acadboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_FILE = "login_prefs";
    private SharedPreferences logins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logins = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        boolean exists = logins.getBoolean("Signedin",false);
        if(exists){
            Intent loggedin = new Intent(MainActivity.this,CalendarActivity.class);
            startActivity(loggedin);
        }
        else{
            Intent noacc = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(noacc);
        }
        finish();
    }
}
