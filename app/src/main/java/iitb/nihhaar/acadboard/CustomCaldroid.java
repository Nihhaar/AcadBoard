package iitb.nihhaar.acadboard;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by Nihhaar on 11/4/2016.
 */

public class CustomCaldroid extends CaldroidFragment {

    @Override
    protected int getGridViewRes() {
        return R.layout.custom_grid_layout;
    }

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CaldroidSampleCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}
