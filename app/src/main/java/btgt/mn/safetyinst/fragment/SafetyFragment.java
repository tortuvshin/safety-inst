package btgt.mn.safetyinst.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import btgt.mn.safetyinst.R;
import btgt.mn.safetyinst.database.SNoteTable;

public class SafetyFragment extends Fragment {

    ScrollView scrollView;
    View rootView;
    SNoteTable sNoteTable;

    public SafetyFragment() {
        // Required empty public constructor
    }

    public static SafetyFragment newInstance() {
        SafetyFragment fragment = new SafetyFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sNoteTable = new SNoteTable(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_safety, container, false);
        scrollView = (ScrollView) rootView.findViewById(R.id.contentScroll);
        return rootView;
    }
}
