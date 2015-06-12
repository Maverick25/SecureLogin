package agency.alterway.edillion.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import agency.alterway.edillion.R;
import agency.alterway.edillion.controllers.MainController;
import agency.alterway.edillion.controllers.injections.MainInjection;
import agency.alterway.edillion.models.DocumentFile;
import agency.alterway.edillion.models.User;
import agency.alterway.edillion.utils.PressMode;
import agency.alterway.edillion.views.adapters.DocumentsAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements DocumentsAdapter.ActivityCallback,MainInjection
{
    private MenuItem menuApprove;
    private MenuItem menuDelete;

    private CheckBox selectAllBox;
    private TextView totalNumber;

    public static User currentUser;
    private DocumentsAdapter adapter;
    private List<DocumentFile> documentFiles;
    private SharedPreferences preferences;

    @InjectView(R.id.toolbar_actionbar)
    Toolbar              mToolbar;
    @InjectView(R.id.total_info_bar)
    View                 totalInfo;
    @InjectView(R.id.fa_menu)
    FloatingActionsMenu  faMenu;
    @InjectView(R.id.fab_addDocument)
    FloatingActionButton fabAddDocument;
    @InjectView(R.id.document_recycler)
    RecyclerView         documentsView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentUser = getIntent().getParcelableExtra(getString(R.string.tag_current_user));
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setUpToolbar();

        documentsView.setHasFixedSize(true);

        // The number of Columns
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        documentsView.setLayoutManager(mLayoutManager);

        documentFiles = new ArrayList<>();

        adapter = new DocumentsAdapter(this,documentFiles);
        documentsView.setAdapter(adapter);

        selectAllBox = (CheckBox) totalInfo.findViewById(R.id.all_check);
        totalNumber = (TextView) totalInfo.findViewById(R.id.count_value);

        selectAllBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (buttonView.getTag() != null) {
                    buttonView.setTag(null);
                    return;
                }

                if(isChecked)
                {
                    adapter.setMode(PressMode.ALL_SELECTED);
                    menuApprove.setTitle(R.string.menu_approveSelected);
                    menuDelete.setTitle(getString(R.string.menu_deleteSelected));
                }
                else
                {
                    adapter.setMode(PressMode.NONE_SELECTED);
                    menuApprove.setTitle(R.string.menu_approveAll);
                    menuDelete.setTitle(getString(R.string.menu_deleteAll));
                }
            }
        });


//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//        {
//        fabAddDocument.setVisibility(View.GONE);
//        faMenu.setVisibility(View.VISIBLE);

        fetchDocuments();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        MainController.getInstance(this).getDocumentFiles();
    }

    private void setUpToolbar()
    {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbar.setPadding(getResources().getDimensionPixelOffset(R.dimen.edge_margin), 0, 0, 0);
    }

    private void updateTotalBar()
    {
        totalNumber.setText(String.valueOf(documentFiles.size()));
    }


    private void fetchDocuments()
    {
        MainController.getInstance(this).getDocumentFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuApprove = menu.findItem(R.id.menu_approve);
        menuDelete = menu.findItem(R.id.menu_delete);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try
        {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.menu_approve:

                    return true;
                case R.id.menu_delete:

                    return true;
                case R.id.menu_logout:
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putBoolean(getString(R.string.tag_save_login), false);
                    edit.putBoolean(getString(R.string.tag_logout), true);
                    edit.apply();

                    Intent logout = new Intent(this, LoginActivity.class);
                    startActivity(logout);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    return true;
                default:
                    throw new Exception();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method that creates and shows an Error Dialog
     * after an error occurrence.
     */
    private void showErrorDialog(String message)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .neutralText(getString(R.string.dialog_dismiss))
                .title(getString(R.string.error))
                .content(message)
                .build();

        dialog.show();
    }

    @OnClick(R.id.fab_addDocument)
    void addDocument()
    {
        Intent goToScan = new Intent(MainActivity.this,CameraActivity.class);
        startActivityForResult(goToScan,0);
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

    @Override
    public void isLongSelected(boolean isSelected)
    {
        if(isSelected)
        {
            menuApprove.setTitle(R.string.menu_approveSelected);
            menuDelete.setTitle(getString(R.string.menu_deleteSelected));
        }
        else
        {
            menuApprove.setTitle(R.string.menu_approveAll);
            menuDelete.setTitle(getString(R.string.menu_deleteAll));
        }
    }

    @Override
    public void isNotAllSelected()
    {
        selectAllBox.setChecked(false);
    }

    @Override
    public void goViewPhoto(int position)
    {
        Intent goToScan = new Intent(MainActivity.this,ScanActivity.class);
        goToScan.putExtra(getString(R.string.scan_selectedMode),getResources().getInteger(R.integer.view_photo));
        goToScan.putExtra(getString(R.string.scan_photoToView),documentFiles.get(position).getThumbnail());
        startActivity(goToScan);
    }

    @Override
    public void onReceivedDocuments(List<DocumentFile> documents)
    {
        documentFiles = documents;

        adapter = new DocumentsAdapter(this,documentFiles);
        documentsView.setAdapter(adapter);

        updateTotalBar();
    }

    @Override
    public void onErrorMain(String message)
    {
        showErrorDialog(message);
    }
}
