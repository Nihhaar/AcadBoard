package iitb.nihhaar.acadboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Nihhaar on 10/31/2016.
 */

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content,fragment,fragment.getClass().getSimpleName()).commit();
        }
    }

}
