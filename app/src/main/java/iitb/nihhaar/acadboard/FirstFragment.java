package iitb.nihhaar.acadboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static iitb.nihhaar.acadboard.MainActivity.PREFS_FILE;


public class FirstFragment extends Fragment {

    private static final String TAG = "FirstFragment";
    public static HashMap<String,ArrayList<Events> > hm;
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    private CustomCaldroid caldroidFragment;
    private Date lastdate;
    private Intent intent;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"RECIEVED BROADCAST BABY!!!");
            updateUI(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        hm = new HashMap<String, ArrayList<Events> >();
        intent = new Intent(getActivity(),MyService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_first, container, false);
        // Inflate the layout for this fragment
        caldroidFragment = new CustomCaldroid();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        lastdate = new Date(Calendar.YEAR,Calendar.MONTH,Calendar.DATE);

        android.support.v4.app.FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                ColorDrawable transparent = new ColorDrawable(Color.TRANSPARENT);
                caldroidFragment.setTextColorForDate(R.color.black,lastdate);
                caldroidFragment.setBackgroundDrawableForDate(transparent,lastdate);
                LayerDrawable sh = (LayerDrawable)getResources().getDrawable(R.drawable.fill_circle);
                caldroidFragment.setBackgroundDrawableForDate(sh,date);
                caldroidFragment.setTextColorForDate(R.color.white,date);
                lastdate=date;
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String formatdate = df.format(date);
                Log.d(TAG,formatdate);
                for(String key: hm.keySet()){
                    Log.d("ASSHOLE",key);
                }
                cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item_card);
                //if(!hm.isEmpty() && hm.containsKey(formatdate)) {
                   // ArrayList<Events> eventsets = hm.get(formatdate);
                  //  Log.d("CARD",eventsets.get(0).getName());

                    for (int i = 0; i < 3; i++) {
                         Card card = new Card("COURSE", "TYPE");
                         cardArrayAdapter.add(card);
                    }
                listView.setAdapter(cardArrayAdapter);
                //}
                caldroidFragment.refreshView();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                try {
                    for (String seldate : hm.keySet()) {
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        Date date = df.parse(seldate);
                        caldroidFragment.setTextColorForDate(android.R.color.holo_red_dark, date);
                    }
                }
                catch(Exception e){
                    Log.d(TAG,"FUCKED");
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
//        caldroidFragment.setShowNavigationArrows(false);

        listView = (ListView) view.findViewById(R.id.card_listView);
        listView.addHeaderView(new View(getActivity()));
        listView.addFooterView(new View(getActivity()));
        /*cardArrayAdapter = new CardArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item_card);
        for (int i = 0; i < 5; i++) {
            Card card = new Card("Card " + (i+1) + " Line 1", "Card " + (i+1) + " Line 2");
            cardArrayAdapter.add(card);
        }*/
        listView.setAdapter(cardArrayAdapter);
        caldroidFragment.refreshView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().startService(intent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(MyService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(intent);
    }

    private void updateUI(Intent intent) {
        try {
            File cachedir = getActivity().getCacheDir();
            File events = new File(cachedir.getPath()+"/"+"Events.txt");
            FileReader fr = new FileReader(events);
            StringBuffer sbuffer = new StringBuffer();
            BufferedReader dataIO = new BufferedReader(fr);
            String strLine = null;
            while ((strLine = dataIO.readLine()) != null) {
                sbuffer.append(strLine + "\n");
            }
            dataIO.close();
            fr.close();
            Toast.makeText(getActivity(),"Updated just now",Toast.LENGTH_SHORT).show();

            strLine = sbuffer.toString();
            JSONObject json = new JSONObject(strLine);
            JSONArray jsonarr = json.optJSONArray("json_data");
            for(int i=0;i<jsonarr.length();i++) {
                JSONArray temp = jsonarr.getJSONObject(i).optJSONArray("course_data");
                //String res = jsonarr1.getJSONObject(0).getString("course_name");
                String name = temp.getJSONObject(0).getString("course_name");
                JSONArray assignments = temp.getJSONObject(1).optJSONArray("Assignments");
                JSONArray feedbacks = temp.getJSONObject(2).optJSONArray("Feedbacks");
                for(int j=0;j<assignments.length();j++){
                    Events e1 = new Events(name,"Assignment",assignments.getJSONObject(i).getString("description"));
                    String date = assignments.getJSONObject(i).getString("date");
                    ArrayList<Events> el1 = hm.get(date);
                    if(el1 == null){
                        el1 = new ArrayList<Events>();
                        el1.add(e1);
                        hm.put(date,el1);
                    }
                    else{
                        el1.add(e1);
                        hm.put(date,el1);
                    }
                }
                for(int j=0;j<feedbacks.length();j++){
                    Events e1 = new Events(name,"Feedback",feedbacks.getJSONObject(i).getString("id"));
                    String date = feedbacks.getJSONObject(i).getString("date");
                    ArrayList<Events> el2 = hm.get(date);
                    if(el2 == null){
                        el2 = new ArrayList<Events>();
                        el2.add(e1);
                        hm.put(date,el2);
                    }
                    else{
                        el2.add(e1);
                        hm.put(date,el2);

                    }
                }
            }
            caldroidFragment.refreshView();
        }
        catch(IOException e){
            Log.d(TAG,"There is a IOException");
        }
        catch (JSONException je){
            throw new RuntimeException(je);
        }
    }

}
