package btgt.mn.safetyinst.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import btgt.mn.safetyinst.R;

public class AddInfoFragment extends Fragment {


    public AddInfoFragment() {

    }


    public static AddInfoFragment newInstance() {
        AddInfoFragment fragment = new AddInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_info, container, false);
    }
}
