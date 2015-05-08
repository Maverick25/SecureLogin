package agency.alterway.edillion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import agency.alterway.edillion.R;
import agency.alterway.edillion.views.adapters.PagerDocumentAdapter;
import agency.alterway.edillion.views.widgets.SlidingTabLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    @InjectView(R.id.pager_documents)
    ViewPager pager;
    @InjectView(R.id.tabs)
    SlidingTabLayout tabs;
    @InjectView(R.id.fa_menu)
    FloatingActionsMenu faMenu;
    @InjectView(R.id.fab_addDocument)
    FloatingActionButton fabAddDocument;

    PagerDocumentAdapter pagerAdapter;
    CharSequence titles[]={"Ready","Approved","Deleted"};
    int numOfTabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setUpToolbar();

//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//        {
            fabAddDocument.setVisibility(View.GONE);
            faMenu.setVisibility(View.VISIBLE);
//        }
    }

    private void setUpToolbar()
    {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        setSupportActionBar(mToolbar);
        setTitle("Edillion");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        pagerAdapter =  new PagerDocumentAdapter(getSupportFragmentManager(),titles,numOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(pagerAdapter);

        // Assiging the Sliding Tab Layout View
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accent);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_addDocument)
    void addDocument()
    {
        Intent goToScan = new Intent(MainActivity.this,ScanActivity.class);
        startActivity(goToScan);
    }

    @OnClick(R.id.fab_selectPicture)
    void addSavedPicture()
    {
        Intent goToScan = new Intent(MainActivity.this,ScanActivity.class);
        goToScan.putExtra(getString(R.string.scan_selectedMode),getResources().getInteger(R.integer.selected_picture));
        startActivity(goToScan);
    }

    @OnClick(R.id.fab_cameraRequest)
    void takePhoto()
    {
        Intent goToScan = new Intent(MainActivity.this,ScanActivity.class);
        goToScan.putExtra(getString(R.string.scan_selectedMode),getResources().getInteger(R.integer.camera_request));
        startActivity(goToScan);
    }
}
