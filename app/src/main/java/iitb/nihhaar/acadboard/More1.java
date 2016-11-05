package iitb.nihhaar.acadboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static iitb.nihhaar.acadboard.MainActivity.PREFS_FILE;

public class More1 extends AppCompatActivity {

    private SharedPreferences logins;
    private RequestQueue queue;
    private Intent intent1;
    private String str;
    private ListView ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more1);
        intent1 = getIntent();
        str = "Cannot Connect to Server";

        logins = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        ConnectivityManager cmgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url2), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    str = s;
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
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
                    params.put("feedback",intent1.getStringExtra("description"));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(stringRequest);
        } else {
            Toast.makeText(this,"No network connection available.",Toast.LENGTH_SHORT).show();
        }

        if(!str.equals("Cannot Connect to Sever")) {
           try {
               //Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
               QuestionAdapter adapter = new QuestionAdapter(this, new ArrayList<Ques>());
               JSONObject json = new JSONObject(str);
               JSONArray jsonarr = json.optJSONArray("question_data");
               for (int i = 0; i < jsonarr.length(); i++) {
                   String str = jsonarr.getJSONObject(i).getString("question");
                   Ques que = new Ques(str);
                   adapter.add(que);
               }
               ls = (ListView) findViewById(R.id.listque);
               ls.setAdapter(adapter);
           }
           catch (JSONException je){
               Log.d("More1","There is JSONEXception");
           }

        }

    }
}
