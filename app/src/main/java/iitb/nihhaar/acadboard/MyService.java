package iitb.nihhaar.acadboard;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static iitb.nihhaar.acadboard.MainActivity.PREFS_FILE;

public class MyService extends Service {

    public static final String BROADCAST_ACTION = "iitb.nihhaar.acadboard.broadcasttest.displayevent";
    private final String TAG = "MyService";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;
    private SharedPreferences logins;
    private RequestQueue queue;
    private String username;
    private String password;
    private boolean flag;
    private String str;

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 5000); // 5 seconds
        }
    };

    @Override
    public void onCreate(){
        super.onCreate();
        logins = getApplication().getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        username = logins.getString("email","def");
        password = logins.getString("password","def");
        intent = new Intent(BROADCAST_ACTION);
        flag=false;

    }

    @Override
    public void onStart(Intent intent,int startId) {
        Log.d(TAG,"onStarted");
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        ConnectivityManager cmgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url1), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        if(!flag) {
                            File fileDir = getCacheDir();
                            File newFile = new File(fileDir,"Events.txt");
                            newFile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(newFile.getAbsolutePath());
                            fos.write(s.getBytes());
                            fos.close();
                            flag=true;
                            str = s;
                            sendBroadcast(intent);
                            Log.d(TAG,"PARSED JSON DATA FIRST TIME");
                        }
                        if(flag){
                            if(!str.equals(s)){
                                File fileDir = getCacheDir();
                                File newFile = new File(fileDir,"Events.txt");
                                newFile.createNewFile();
                                FileOutputStream fos = new FileOutputStream(newFile.getAbsolutePath());
                                fos.write(s.getBytes());
                                fos.close();
                                str = s;
                                sendBroadcast(intent);
                                Log.d(TAG,"PARSED JSON DATA NOT FIRST TIME");
                            }
                        }
                    }
                    catch(IOException e){
                        Log.d(TAG,"Exception occurred when writing data");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG,"Unable to Connect to Server");
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("student",username);
                    params.put("password",password);
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
            Log.d(TAG,"No network connection available.");
        }

    }

}
