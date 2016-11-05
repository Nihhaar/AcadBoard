package iitb.nihhaar.acadboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nihhaar on 11/5/2016.
 */

class QuestionAdapter extends ArrayAdapter<Ques> {
    public QuestionAdapter(Context context, ArrayList<Ques> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ques user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_more1, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.que);
        // Populate the data into the template view using the data object
        tvName.setText(user.que);
        // Return the completed view to render on screen
        return convertView;
    }

}
