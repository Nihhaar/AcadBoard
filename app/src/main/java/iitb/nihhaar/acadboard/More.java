package iitb.nihhaar.acadboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class More extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Log.d("Again","CAme Here");
        Intent here = getIntent();
        String name = here.getStringExtra("name");
        String type = here.getStringExtra("type");
        String description = here.getStringExtra("description");
        TextView tv1 = (TextView)findViewById(R.id.name);
        TextView tv2 = (TextView)findViewById(R.id.type);
        TextView tv3 = (TextView)findViewById(R.id.description);
        tv1.setText(name);
        tv2.setText(type);
        tv3.setText(description);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+name+"</font>"));

    }
}
