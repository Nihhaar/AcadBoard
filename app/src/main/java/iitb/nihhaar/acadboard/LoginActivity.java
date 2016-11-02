package iitb.nihhaar.acadboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import static iitb.nihhaar.acadboard.MainActivity.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String DEBUG_TAG = "HttpExample";
    Button button_sign;
    private TextView tv_register;
    private EditText inputEmail;
    private EditText inputPassword;
    private SharedPreferences logins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#EA4C89'> IITB Acad Feed </font>"));

        tv_register = (TextView) findViewById(R.id.register);
        tv_register.setOnClickListener(this);

        inputEmail = (EditText)findViewById(R.id.username);
        inputPassword = (EditText)findViewById(R.id.password);
        button_sign = (Button) findViewById(R.id.signin);
        button_sign.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v== button_sign){
            if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
            {
                logins = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
                ConnectivityManager cmgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cmgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    GetRequest details = new GetRequest();
                    details.execute(getResources().getString(R.string.url));
                    SharedPreferences.Editor addlogin = logins.edit();
                    addlogin.putString("email",inputEmail.getText().toString());
                    addlogin.putString("password",inputPassword.getText().toString());
                    addlogin.putBoolean("Signedin",true);
                    addlogin.commit();
                    //Intent test = new Intent(LoginActivity.this,CalendarActivity.class);
                    //startActivity(test);
                } else {
                    Toast.makeText(this,"No network connection available.",Toast.LENGTH_SHORT).show();
                }
            }
            else if ( ( !inputEmail.getText().toString().equals("")) )
            {
                Toast.makeText(getApplicationContext(),
                        "Password fields empty", Toast.LENGTH_SHORT).show();
            }
            else if ( ( !inputPassword.getText().toString().equals("")) )
            {
                Toast.makeText(getApplicationContext(),
                        "Email field empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Email and Password field are empty", Toast.LENGTH_SHORT).show();
            }
        }

        if(v == tv_register){
            Intent reg = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(reg);
        }
    }

    private String connectUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class GetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return connectUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        }
    }
}
