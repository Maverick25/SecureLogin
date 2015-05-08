package agency.alterway.edillion.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import agency.alterway.edillion.R;

/**
 * Created by marekrigan on 02/05/15.
 */
public class ReadyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v =inflater.inflate(R.layout.tab_ready,container,false);
        return v;
    }
}
