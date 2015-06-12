package agency.alterway.edillion.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import agency.alterway.edillion.R;
import agency.alterway.edillion.db.DatabaseHandler;
import agency.alterway.edillion.models.DocumentFile;
import agency.alterway.edillion.views.widgets.CameraPreview;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CameraActivity extends Activity
{
    private Camera mCamera;

    @InjectView(R.id.camera_preview)
    FrameLayout flCameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        ButterKnife.inject(this);

        mCamera = getCameraInstance();

        try
        {
            flCameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
            CameraPreview mCameraPreview = (CameraPreview) flCameraPreview.getChildAt(0);
            mCameraPreview.setCameraPreview(this, mCamera);
        }
        catch (Exception e)
        {
            releaseCamera();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy()
    {
        DatabaseHandler.close();
        releaseCamera();
        super.onDestroy();
    }

    private PictureCallback mPictureCallback = new PictureCallback()
    {

        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            DocumentFile document = new DocumentFile();

            document.setThumbnail(data);

            long id = DatabaseHandler.getInstance(CameraActivity.this).addDocument(document);

            Intent goToScan = new Intent(CameraActivity.this, ScanActivity.class);
            goToScan.putExtra(getString(R.string.scan_selectedMode), getResources().getInteger(R.integer.camera_request));
            goToScan.putExtra(getString(R.string.scan_photoToView), id);
            startActivity(goToScan);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    };

    public static Camera getCameraInstance()
    {
        Camera c = null;
        try
        {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e)
        {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void releaseCamera()
    {
        if (mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @OnClick(R.id.fab_takePicture)
    void takePicture()
    {
        mCamera.takePicture(null, null, mPictureCallback);
    }

    @OnClick(R.id.fab_selectPic)
    void selectPicture()
    {

    }

}