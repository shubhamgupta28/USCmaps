package usc.com.uscmaps.example1.shubham.uscmaps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shubham on 2/21/17.
 */

public class WaitListDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME1 = "building.db";
    private final static int DATABASE_VERSION = 7;

    public WaitListDBHelper(Context context) {
        super(context, DATABASE_NAME1, null, DATABASE_VERSION);

    }

    String SQL_BUILDING_CREATE_TABLE = "CREATE TABLE " + WaitListContract.WaitListEntry.TABLE_NAME +
            " ( "+
            WaitListContract.WaitListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WaitListContract.WaitListEntry.COLUMN_SERIAL + " TEXT NOT NULL, " +
            WaitListContract.WaitListEntry.COLUMN_CODE + " TEXT NOT NULL, " +
            WaitListContract.WaitListEntry.COLUMN_BUILDING_NAME + " TEXT NOT NULL, " +
            WaitListContract.WaitListEntry.COLUMN_ADDRESS + " TEXT "
            + ");";

    String SQL_PARKING_CREATE_TABLE = "CREATE TABLE " + WaitListContract.WaitListEntry.TABLE_NAME_PARKING +
            " ( "+
            WaitListContract.WaitListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WaitListContract.WaitListEntry.COLUMN_SYMBOL_PARKING + " TEXT NOT NULL, " +
            WaitListContract.WaitListEntry.COLUMN_NAME_PARKING + " TEXT NOT NULL, " +
            WaitListContract.WaitListEntry.COLUMN_ADDRESS_PARKING + " TEXT NOT NULL, " +
            WaitListContract.WaitListEntry.COLUMN_DESCRIPTION_PARKING + " TEXT "
            + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_BUILDING_CREATE_TABLE);
        db.execSQL(SQL_PARKING_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ WaitListContract.WaitListEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ WaitListContract.WaitListEntry.TABLE_NAME_PARKING);
        onCreate(db);
    }
}
