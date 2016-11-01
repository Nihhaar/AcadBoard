package iitb.nihhaar.acadboard;

import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import static iitb.nihhaar.acadboard.MainActivity.*;

/**
 * Created by Nihhaar on 10/31/2016.
 */

class SettingsFragment extends PreferenceFragment {

    private SharedPreferences prefs;
    private SharedPreferences logins;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Preference loginmail = (Preference)findPreference("username");
        loginmail.setSummary(getActivity().getSharedPreferences(PREFS_FILE,MODE_PRIVATE).getString("email","Not Logged in"));
        Preference button = (Preference)findPreference("logout");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent login = new Intent(getActivity(),LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logins = getActivity().getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
                SharedPreferences.Editor remLogin = logins.edit();
                remLogin.clear();
                remLogin.commit();
                startActivity(login);
                getActivity().finish();
                return true;
            }
        });
    }
}
