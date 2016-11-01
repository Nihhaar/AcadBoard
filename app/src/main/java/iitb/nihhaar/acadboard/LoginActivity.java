package iitb.nihhaar.acadboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

import static iitb.nihhaar.acadboard.MainActivity.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private TextView tv_register;
    private EditText inputEmail;
    private EditText inputPassword;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private SharedPreferences logins;
    Button button_sign;
    private int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#EA4C89'> IITB Acad Feed </font>"));

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        tv_register = (TextView) findViewById(R.id.register);
        tv_register.setOnClickListener(this);

        inputEmail = (EditText)findViewById(R.id.username);
        inputPassword = (EditText)findViewById(R.id.password);
        button_sign = (Button) findViewById(R.id.signin);
        button_sign.setOnClickListener(this);
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);
        setGooglePlusButtonText(signInButton,"Sign in with Google    ");


    }


    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
        } else {
            //updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            signIn();
        }

        if(v== button_sign){
            if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
            {
                logins = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
                SharedPreferences.Editor addlogin = logins.edit();
                addlogin.putString("email",inputEmail.getText().toString());
                addlogin.putString("password",inputPassword.getText().toString());
                addlogin.putBoolean("Signedin",true);
                addlogin.commit();
                Intent test = new Intent(LoginActivity.this,CalendarActivity.class);
                startActivity(test);
                finish();
                //NetAsync(view);
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
}
