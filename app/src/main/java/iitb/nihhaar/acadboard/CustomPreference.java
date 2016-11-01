package iitb.nihhaar.acadboard;

import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Nihhaar on 11/1/2016.
 */

class CustomPreference extends Preference {

    private Context ctx;

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public CustomPreference(Context context) {
        super(context);
        ctx = context;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        setCustomStyle(view);
    }

    private void setCustomStyle(View view) {
        RippleDrawable drawable = (RippleDrawable) ctx.getDrawable(R.drawable.my_ripple_background);
        view.setBackground(drawable);
    }

}
