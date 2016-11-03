package iitb.nihhaar.acadboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

/**
 * Created by Nihhaar on 10/31/2016.
 */

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Settings</font>"));
        if(savedInstanceState == null){
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content,fragment,fragment.getClass().getSimpleName()).commit();
        }
    }

}
