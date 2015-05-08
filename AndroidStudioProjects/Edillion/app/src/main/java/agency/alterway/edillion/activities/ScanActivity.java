package agency.alterway.edillion.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import agency.alterway.edillion.R;
import agency.alterway.edillion.utils.ScanMode;
import agency.alterway.edillion.views.adapters.PagerScanAdapter;
import butterknife.InjectView;

public class ScanActivity extends AppCompatActivity {

    private ScanMode mode;

    PagerScanAdapter pagerAdapter;

    @InjectView(R.id.toolbar_actionbar)
    Toolbar mToolbar;
    @InjectView(R.id.pager_scanFragments)
    ViewPager pager;
    @InjectView(R.id.fab_approveDocument)
    FloatingActionButton fabApproveDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scan);

        mode = ScanMode.fromInt(getIntent().getIntExtra(getString(R.string.scan_selectedMode),-1));

        setScreen(savedInstanceState);
    }

    private void setScreen(Bundle savedInstanceState)
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
//                if (null == savedInstanceState) {
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.container, CameraFragment.newInstance())
//                            .commit();
//                }
//            }
//            else
//            {
                switch(mode)
                {
                    case CAMERA_REQUEST:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, getResources().getInteger(R.integer.camera_request));
                        break;
                    case SELECT_PICTURE:
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, getResources().getInteger(R.integer.selected_picture));
                        break;
                    case UNKNOWN:
                        throw new Exception();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUpToolbar()
    {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        setSupportActionBar(mToolbar);
        setTitle("Description");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        pagerAdapter =  new PagerScanAdapter(getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(pagerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {
            if(resultCode == RESULT_OK)
            {
                ByteArrayOutputStream stream;
                byte[] byteArray;

                mode = ScanMode.fromInt(requestCode);
                switch (mode)
                {
                    case CAMERA_REQUEST:
                        Bitmap photo = (Bitmap) data.getExtras().get("data");

                        stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        setUpToolbar();

                        break;
                    case SELECT_PICTURE:
                        Uri selectedImage = data.getData();
                        Bitmap pic = decodeUri(selectedImage);

                        stream = new ByteArrayOutputStream();
                        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

                        setUpToolbar();

                        break;
                    case UNKNOWN:
                        throw new Exception();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
    {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }
}
