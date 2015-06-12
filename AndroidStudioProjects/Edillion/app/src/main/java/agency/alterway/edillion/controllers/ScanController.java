package agency.alterway.edillion.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

import agency.alterway.edillion.EdillionApplication;
import agency.alterway.edillion.controllers.injections.ScanInjection;
import agency.alterway.edillion.models.DocumentFile;
import agency.alterway.edillion.network.ImageRequest;
import agency.alterway.edillion.network.VolleyRequestHandler;

/**
 *
 * Created by marekrigan on 26/05/15.
 */
public class ScanController
{
    private static ScanController instance;
    private static ScanInjection  injection;
    private RequestQueue queue;

    private ScanController(ScanInjection injection)
    {
        ScanController.injection = injection;
    }

    public static ScanController getInstance(ScanInjection injection)
    {
        if (instance == null)
        {
            instance = new ScanController(injection);
        }
        else
        {
            ScanController.injection = injection;
        }
        return instance;
    }

    public static ScanController getInstance()
    {
        if(instance == null)
        {
            instance = new ScanController(null);
        }
        else
        {
            ScanController.injection = null;
        }
        return instance;
    }

    public void setImageOnView(byte[] image, View view)
    {
        try
        {
            BitmapConversionTask task = new BitmapConversionTask();
            task.execute(image,view);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void uploadDocument(DocumentFile document)
    {
        //TODO: to finish
        queue = VolleyRequestHandler.getInstance(false).getQueue();

        ImageRequest request = new ImageRequest("", new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        }, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

            }
        },null );
    }

    private class BitmapConversionTask extends AsyncTask<Object, Void, HashMap<String,Object>>
    {
        @Override
        protected HashMap<String,Object> doInBackground(Object... bytesnViews)
        {
            try
            {
                byte[] data = (byte[]) bytesnViews[0];
                View view = (View) bytesnViews[1];

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                HashMap<String,Object> postObjects = new HashMap<>();
                postObjects.put("bitmap",rotate(bitmap,90));
                postObjects.put("view",view);
                // return
                // decodeSampledBitmapFromByteArray(data,mCameraPreview.getWidth(),mCameraPreview.getHeight());
                return postObjects;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        public Bitmap rotate(Bitmap src, float degree) {
            // create new matrix
            Matrix matrix = new Matrix();
            // setup rotation degree
            matrix.postRotate(degree);

            // return new bitmap rotated using matrix
            return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        }

        @Override
        protected void onPostExecute(HashMap<String,Object> postObjects)
        {
            try
            {
                View view = (View) postObjects.get("view");
                Bitmap bitmap = (Bitmap) postObjects.get("bitmap");

                if (postObjects.get("view") instanceof ImageView)
                {
                    ((ImageView) view).setImageBitmap(bitmap);
                }
                else
                {
                    view.setBackground(new BitmapDrawable(EdillionApplication.getAppContext().getResources(), bitmap));
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
