package usc.com.uscmaps.example1.shubham.uscmaps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import usc.com.uscmaps.example1.shubham.uscmaps.data.InsertDataUtil;
import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListContract;
import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListDBHelper;

/**
 * Created by Shubham on 2/18/17.
 *
 * The problem is that when you initialse anything inside the Fragment like RecylerView, or anything,
 * we dont do it inside the OnCreate, rather the onCreateView as the Fragment and activity have
 * different life cycles.
 *
 *
 * https://developer.android.com/samples/RecyclerView/src/com.example.android.recyclerview/RecyclerViewFragment.html
 */


public class Tab1Buildings extends Fragment implements BuildingListAdapter.ListItemClickListener {

    private SQLiteDatabase mDb;
    RecyclerView waitlistRecyclerView;
    LinearLayoutManager layoutManager;
    private BuildingListAdapter mAdapter;

//    public static Tab1Buildings newInstance() {
//
//        Bundle args = new Bundle();
//
//        Tab1Buildings fragment = new Tab1Buildings();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_buildings, container, false);

        /**
         * Because we are using a fragment, we have to initialize all the views inside the OnCreateView
         * and not the OnCreate
         */
        waitlistRecyclerView = (RecyclerView) rootView.findViewById(R.id.recylerViewBuilding);

        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        waitlistRecyclerView.setLayoutManager(layoutManager);


        WaitListDBHelper dbHelper = new WaitListDBHelper(this.getContext());

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        //Fill the database with fake data
        InsertDataUtil.insertFakeData(mDb);

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllGuests();

//        Log.e("Tab1Building",  "col Count: "+cursor.getColumnCount());

        // COMPLETED (10) Pass the entire cursor to the adapter rather than just the count
        // Create an adapter for that cursor to display the data

//        mAdapter = new BuildingListAdapter(cursor,this.getContext());
        mAdapter = new BuildingListAdapter(cursor,this.getContext(), this);

//        mAdapter = new BuildingListAdapter(getContext(), this);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        return mDb.query(
                WaitListContract.WaitListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitListContract.WaitListEntry.COLUMN_SERIAL

        );
    }

//    @Override
//    public void recyclerViewItemClicked(View v, int clickedPosition) {
//        Log.e("in recyclerViewItemClic", ""+clickedPosition);
//        Log.e("in recyclerViewItemClic", ""+v.toString());
//        RecyclerView.ViewHolder h = (RecyclerView.ViewHolder)v.getTag();
//
//
////        mAdapter = new BuildingListAdapter(getContext(), this);
//    }

    @Override
    public void onClick(String weatherForDay) {
        Context context = getContext();
        Toast.makeText(context, weatherForDay, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("RecyclerViewValue", weatherForDay);
        intent.putExtra("IdentifyClass", "Tab1Building");
        intent.setClass(getContext(), MainActivity.class);
        startActivity(intent);
    }
}