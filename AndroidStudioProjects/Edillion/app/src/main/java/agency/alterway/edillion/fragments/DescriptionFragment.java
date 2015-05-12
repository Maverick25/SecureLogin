package agency.alterway.edillion.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import agency.alterway.edillion.R;

/**
 *
 * Created by marekrigan on 08/05/15.
 */
public class DescriptionFragment extends Fragment
{
    ImageView documentView;
    byte[] bitmapBytes;
    /**
     * A frgment for showing all the time entries for a given date.
     *
     * @return A new instance of fragment TimeEntryFragment.
     */
    public static DescriptionFragment newInstance() {
        Log.v("DESCRIPTION", "newInstance");
        return new DescriptionFragment();
    }

    public DescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        documentView = (ImageView) inflater.inflate(R.layout.fragment_photo, container, false);

        documentView.setFitsSystemWindows(true);

        return documentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(bitmapBytes != null) {
            Bitmap bmp;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bmp = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
            documentView.setImageBitmap(bmp);
        }
    }

    public DescriptionFragment setUp(byte[] bitmapBytes)
    {
        this.bitmapBytes = bitmapBytes;
        return this;
    }

}
