package agency.alterway.edillion.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import agency.alterway.edillion.fragments.CameraFragment;
import agency.alterway.edillion.fragments.DescriptionFragment;

/**
 * Created by marekrigan on 08/05/15.
 */
public class PagerScanAdapter extends FragmentStatePagerAdapter
{
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public PagerScanAdapter(FragmentManager fm) {
        super(fm);
        this.NumbOfTabs = 2;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                CameraFragment camera = new CameraFragment();
                return camera;
            case 1:
                DescriptionFragment description = new DescriptionFragment();
                return description;
            default:
                return null;
        }
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
