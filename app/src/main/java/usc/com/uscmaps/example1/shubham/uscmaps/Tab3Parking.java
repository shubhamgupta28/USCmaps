package usc.com.uscmaps.example1.shubham.uscmaps;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
 */

public class Tab3Parking extends Fragment implements ParkingListAdapter.ListItemClickListenerParking {

    private SQLiteDatabase      mDb;
    RecyclerView                waitlistRecyclerView;
    LinearLayoutManager         layoutManager;
    private ParkingListAdapter  mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_parking, container, false);

        /**
         * Because we are using a fragment, we have to initialize all the views inside the OnCreateView
         * and not the OnCreate
         */
        waitlistRecyclerView = (RecyclerView) rootView.findViewById(R.id.parkingRecyclerView);

        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        waitlistRecyclerView.setLayoutManager(layoutManager);


        WaitListDBHelper dbHelper = new WaitListDBHelper(this.getContext());

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        //Fill the database with fake data
        InsertDataUtil.insertFakeParkingData(mDb);

        // Get all parking info from the database and save in a cursor
        //TODO Cursor created, now create a recycler view for the parking data
        // BuildingListAdapter, RecyclerView

        Cursor cursorParking = getAllParkingInfo();
        mAdapter = new ParkingListAdapter(cursorParking, this.getContext(), this);
        waitlistRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private Cursor getAllParkingInfo() {
        return mDb.query(
                WaitListContract.WaitListEntry.TABLE_NAME_PARKING,
                new String[] {"name_parking"},
                null,
                null,
                null,
                null,
                "name_parking" + " ASC"
        );
    }



    @Override
    public void onClick(String weatherForDay) {
        Context context = getContext();
        Toast.makeText(context, weatherForDay, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("RecyclerViewValueParking", weatherForDay);
        intent.putExtra("IdentifyClass", "Tab3Parking");
        intent.putExtra("changeTab", "changeTab");
        intent.setClass(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
