package usc.com.uscmaps.example1.shubham.uscmaps;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListContract;
import usc.com.uscmaps.example1.shubham.uscmaps.data.WaitListDBHelper;


/**
 * The problem here was that I was trying to access the database without getting a Readable
 * instance of it. Hence, I declared an instance of WaitListHelper and created a database instance.
 */
public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    public static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        WaitListDBHelper dbHelper = new WaitListDBHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(TAG, "Inside onNewIntent ");
        handleIntent(intent);
    }



    private void handleIntent(Intent intent) {
        Log.e(TAG, "Inside handleNewIntent "+intent.getAction());

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.e("check app crash", ""+intent);
            String query = intent.getStringExtra(SearchManager.QUERY);
            query= query.toUpperCase();
            Cursor searchedResultCursor = getWordMatches(query);

            searchedResultCursor.moveToFirst();

            if(searchedResultCursor.getCount() == 0){
                Toast.makeText(getApplicationContext()  , "The Building Symbol you entered doesn't exist. Try Again!",
                        Toast.LENGTH_LONG).show();
            }
            else{

//                Log.e("handleIntent", ""+ DatabaseUtils.dumpCursorToString(searchedResultCursor));
                broadcastIntent(searchedResultCursor.getString(searchedResultCursor.getColumnIndex("address")),
                        searchedResultCursor.getString(searchedResultCursor.getColumnIndex("buldingName")));
            }
        }
        else if (intent.getExtras() != null) {
            String buildingIntentCheck = intent.getExtras().getString("IdentifyClass");
//            Log.e("check app crash", buildingIntentCheck);
//            Log.e("check app crash", ""+intent);


            if (buildingIntentCheck != null && buildingIntentCheck.equals("Tab1Building")) {
                String query = intent.getStringExtra("RecyclerViewValue");
                query= query.toUpperCase();
                Cursor searchedResultCursor = getBuildingNameMatch(query);
                searchedResultCursor.moveToFirst();
                if(searchedResultCursor.getCount() == 0){
                    Toast.makeText(getApplicationContext()  , "The Building Symbol you entered doesn't exist. Try Again!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                  broadcastIntent(searchedResultCursor.getString(searchedResultCursor.getColumnIndex("address")),
                            searchedResultCursor.getString(searchedResultCursor.getColumnIndex("buldingName")));
                }
            }


        }

    }

    public void broadcastIntent(String destination, String buildingName){
        Intent intent2= new Intent("CustomIntent");
        intent2.putExtra("message", destination);
        intent2.putExtra("buildingName", buildingName);
        intent2.setAction("com.broadcast.searchQuery");
        sendBroadcast(intent2);
    }

    private Cursor getWordMatches(String symbol) {
            return mDb.query(
                WaitListContract.WaitListEntry.TABLE_NAME,
                new String[] {"buldingName", "address"},
                "code = ? " ,
                new String[] { symbol },
                null,
                null,
                null);
    }

    private Cursor getBuildingNameMatch(String buildingName) {
        return mDb.query(
                WaitListContract.WaitListEntry.TABLE_NAME,
                new String[] {"buldingName", "address"},
                "buldingName = ? " ,
                new String[] { buildingName },
                null,
                null,
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1Buildings one = new Tab1Buildings();
                    return one;
//                    return Tab1Buildings.newInstance();
                case 1:
                    Tab2Map two = new Tab2Map();
                    return two;
//                    return Tab2Map.newInstance();
                case 2:
                    Tab3Parking three = new Tab3Parking();
                    return three;
//                    return Tab3Parking.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Buildings";
                case 1:
                    return "Welcome!";
                case 2:
                    return "Parking";
            }
            return null;
        }
    }
}
