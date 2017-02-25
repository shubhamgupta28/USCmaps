package usc.com.uscmaps.example1.shubham.uscmaps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Shubham on 2/18/17.
 */

public class Tab3Parking extends Fragment {

//    public static Tab3Parking newInstance() {
//
//        Bundle args = new Bundle();
//
//        Tab3Parking fragment = new Tab3Parking();
//        fragment.setArguments(args);
//        return fragment;
//    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_parking, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
