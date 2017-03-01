package usc.com.uscmaps.example1.shubham.uscmaps;

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
        InsertDataUtil.insertFakeParkingData(mDb);

        // Get all building info from the database and save in a cursor
        Cursor cursor = getAllGuests();
        mAdapter = new BuildingListAdapter(cursor, this.getContext(), this);

        // Get all parking info from the database and save in a cursor
        //TODO Cursor created, now create a recycler view for the parking data
        Cursor cursorParking = getAllParkingInfo();

        waitlistRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private Cursor getAllParkingInfo() {
        return mDb.query(
                WaitListContract.WaitListEntry.TABLE_NAME_PARKING,
                null,
                null,
                null,
                null,
                null,
                WaitListContract.WaitListEntry.COLUMN_SYMBOL_PARKING
        );
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

    /**
     * Send Intent with the class information and the listItem from Recycler view, that has been
     * clicked and selected.
     * @param weatherForDay
     */
    @Override
    public void onClick(String weatherForDay) {
//        Context context = getContext();
//        Toast.makeText(context, weatherForDay, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("RecyclerViewValue", weatherForDay);
        intent.putExtra("IdentifyClass", "Tab1Building");
        intent.putExtra("changeTab", "changeTab");
        intent.setClass(getContext(), MainActivity.class);
        startActivity(intent);
    }
}