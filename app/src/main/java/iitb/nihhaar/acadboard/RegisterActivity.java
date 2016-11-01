package iitb.nihhaar.acadboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private TextView tv_signin;
    private EditText inputEmail;
    private EditText inputPassword1;
    private EditText inputPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#EA4C89'> IITB Acad Feed </font>"));


        tv_signin = (TextView) findViewById(R.id.signin);
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(reg);
            }
        });

        inputEmail = (EditText)findViewById(R.id.username);
        inputPassword1 = (EditText)findViewById(R.id.password1);
        inputPassword2 = (EditText)findViewById(R.id.password2);

        final Button button_sign = (Button) findViewById(R.id.register);
        button_sign.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword1.getText().toString().equals("")) && (inputPassword1.getText().toString().equals(inputPassword2.getText().toString())) )
                {
                    //NetAsync(view);
                }
                else if ( ( inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }

                else if (!(inputPassword1.getText().toString().equals(inputPassword2.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Both Passwords must be Same",Toast.LENGTH_SHORT).show();
                }
                else if ( ( inputPassword1.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password fields empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
