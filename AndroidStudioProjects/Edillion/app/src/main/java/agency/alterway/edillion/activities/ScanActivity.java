package agency.alterway.edillion.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import agency.alterway.edillion.R;
import agency.alterway.edillion.controllers.ScanController;
import agency.alterway.edillion.controllers.injections.ScanInjection;
import agency.alterway.edillion.db.DatabaseHandler;
import agency.alterway.edillion.models.DocumentFile;
import agency.alterway.edillion.utils.ScanMode;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ScanActivity extends AppCompatActivity implements ScanInjection
{

    private ScanMode mode;
    private Bitmap   documentPicture;
    private Uri      imageUri;
    private static final int DESCRIPTION_SCREEN   = 0;
    private static final int DOCUMENT_VIEW_SCREEN = 1;

    private EditText descriptionField;

    @InjectView(R.id.toolbar_bottom)
    Toolbar              bottomToolbar;
    @InjectView(R.id.switcher_scan)
    ViewSwitcher         switcher;
    @InjectView(R.id.fab_approveDocument)
    FloatingActionButton fabApproveDocument;
    @InjectView(R.id.scan_review)
    RelativeLayout       scanView;
    @InjectView(R.id.imageView_document)
    ImageView            documentView;
    @InjectView(R.id.add_or_delete)
    ImageButton          addOrDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scan);
        ButterKnife.inject(this);

        setUpToolbar();

        mode = ScanMode.fromInt(getIntent().getIntExtra(getString(R.string.scan_selectedMode), -1));

        setScreen();
    }

    private void setScreen()
    {
        try
        {
            switch (mode)
            {
                case VIEW_PHOTO:
                    addOrDeleteButton.setImageResource(R.drawable.delete);
                case CAMERA_REQUEST:
                    long id = getIntent().getLongExtra(getString(R.string.scan_photoToView),-1);
                    DocumentFile document = DatabaseHandler.getInstance(this).getDocument(id);

                    ScanController.getInstance(this).setImageOnView(document.getThumbnail(), scanView);
                    ScanController.getInstance(this).setImageOnView(document.getThumbnail(), documentView);
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
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUpToolbar()
    {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        setSupportActionBar(bottomToolbar);

        descriptionField = (EditText) bottomToolbar.findViewById(R.id.bottom_field);
        descriptionField.setText("Description");

        setTitle(descriptionField.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {
            switcher.setDisplayedChild(DESCRIPTION_SCREEN);
            if(resultCode == RESULT_OK)
            {
                ByteArrayOutputStream stream;
                final byte[] byteArray;

                mode = ScanMode.fromInt(requestCode);
                switch (mode)
                {
                    case CAMERA_REQUEST:
                        documentPicture = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                        documentPicture = rotateBitmap(documentPicture,90);
                        scanView.setBackground(new BitmapDrawable(getResources(),documentPicture));
                        documentView.setImageBitmap(documentPicture);
                        break;
                    case SELECT_PICTURE:
                        Uri selectedImage = data.getData();
                        Bitmap pic = decodeUri(selectedImage);

                        stream = new ByteArrayOutputStream();
                        pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();

//                        new Handler().post(new Runnable() {
//                            public void run() {
//                                try {
//                                    descriptionFragment = descriptionFragment.setUp(byteArray);
//                                    getSupportFragmentManager()
//                                            .beginTransaction()
//                                            .replace(R.id.pager_scanFragments, descriptionFragment)
//                                            .commit();
//                                } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });

//                        documentView.setImageBitmap(pic);
                        scanView.setBackground(new BitmapDrawable(getResources(),rotateBitmap(pic,90)));
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

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @OnClick(R.id.scan_review)
    void showFullDocument()
    {
        switcher.setInAnimation(this,R.anim.fade_in);
        switcher.setOutAnimation(this, R.anim.fade_out);
        switcher.setDisplayedChild(DOCUMENT_VIEW_SCREEN);
        if(descriptionField.getVisibility() == View.VISIBLE)
        {
            descriptionField.setVisibility(View.GONE);
            setTitle(descriptionField.getText().toString());
        }
    }

    @OnClick(R.id.imageView_document)
    void showDescriptionView()
    {
        switcher.setInAnimation(this,R.anim.fade_in);
        switcher.setOutAnimation(this,R.anim.fade_out);
        switcher.setDisplayedChild(DESCRIPTION_SCREEN);
    }

    @OnClick(R.id.add_or_delete)
    void addOrDeleteAction()
    {

    }

    @OnClick(R.id.go_back)
    void goBackAction()
    {
        finish();
    }

    @OnClick(R.id.toolbar_bottom)
    void changeDescription()
    {
        setTitle("");
        descriptionField.setVisibility(View.VISIBLE);
    }

}
