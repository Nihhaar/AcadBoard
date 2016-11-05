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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

import static iitb.nihhaar.acadboard.MainActivity.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginCheck";
    Button button_sign;
    private TextView tv_register;
    private EditText inputEmail;
    private EditText inputPassword;
    private SharedPreferences logins;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'> IITB Acad Feed </font>"));

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
                    queue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("success")) {
                                SharedPreferences.Editor addlogin = logins.edit();
                                addlogin.putString("email",inputEmail.getText().toString());
                                addlogin.putString("password",inputPassword.getText().toString());
                                addlogin.putBoolean("Signedin",true);
                                addlogin.apply();
                                Intent test = new Intent(LoginActivity.this, CalendarActivity.class);
                                startActivity(test);
                                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                              Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(),"Unable to Connect to Server",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("student",inputEmail.getText().toString());
                            params.put("password",inputPassword.getText().toString());
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    stringRequest.setTag(TAG);
                    queue.add(stringRequest);
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

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

}
