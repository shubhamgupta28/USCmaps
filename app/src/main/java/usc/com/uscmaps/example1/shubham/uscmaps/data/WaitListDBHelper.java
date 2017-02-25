package usc.com.uscmaps.example1.shubham.uscmaps.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shubham on 2/21/17.
 */

public class WaitListDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME1 = "building.db";
    private final static int DATABASE_VERSION = 6;

//    public WaitListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME1, null, DATABASE_VERSION);
//
//    }
    public WaitListDBHelper(Context context) {
        super(context, DATABASE_NAME1, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_BUILDING_CREATE_TABLE = "CREATE TABLE " + WaitListContract.WaitListEntry.TABLE_NAME +
                " ( "+
                WaitListContract.WaitListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WaitListContract.WaitListEntry.COLUMN_SERIAL + " TEXT NOT NULL, " +
                WaitListContract.WaitListEntry.COLUMN_CODE + " TEXT NOT NULL, " +
                WaitListContract.WaitListEntry.COLUMN_BUILDING_NAME + " TEXT NOT NULL, " +
                WaitListContract.WaitListEntry.COLUMN_ADDRESS + " TEXT "
                + ");";
        db.execSQL(SQL_BUILDING_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ WaitListContract.WaitListEntry.TABLE_NAME);
        onCreate(db);
    }

//    public static Cursor getWordMatches(SQLiteDatabase db, String symbol) {
////        String select = "Select COLUMN_BUILDING_NAME, COLUMN_ADDRESS from"+ WaitListContract.WaitListEntry.TABLE_NAME
////                +
////                " Where title_raw  " + "'%Smith%'" +
////                ")";
//        Log.e("getWordMatches", symbol);
//        Cursor cursor = db.query(
//                WaitListContract.WaitListEntry.TABLE_NAME,
//                new String[] {"COLUMN_BUILDING_NAME", "COLUMN_ADDRESS"},
//                "COLUMN_CODE = ? " ,
//                new String[] { "SGM" },
//                null,
//                null,
//                null);
//
//        Log.e("getWordMatches" , ""+cursor.getCount());
//        return cursor;
//
////        (SQliteDatabase) db.query(
////                "mytable" /* table */,
////                new String[] { "name" } /* columns */,
////                "id = ?" /* where or selection */,
////                new String[] { "john" } /* selectionArgs i.e. value to replace ? */,
////                null /* groupBy */,
////                null /* having */,
////                null /* orderBy */
////        );
//    }
}