package agency.alterway.edillion.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import agency.alterway.edillion.fragment.ApprovedFragment;
import agency.alterway.edillion.fragment.DeletedFragment;
import agency.alterway.edillion.fragment.ReadyFragment;

/**
 * Created by marekrigan on 02/05/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                ReadyFragment ready = new ReadyFragment();
                return ready;
            case 1:
                ApprovedFragment approved = new ApprovedFragment();
                return approved;
            case 2:
                DeletedFragment deleted = new DeletedFragment();
                return deleted;
            default:
                return null;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}