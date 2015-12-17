package tagbin.in.myapplication.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import tagbin.in.myapplication.UpcomingRides.DataItems;

/**
 * Created by Ramandeep on 19-08-2015.
 */
public class DatabaseOperations extends SQLiteOpenHelper {


    public DatabaseOperations(Context context) {
        super(context, TableData.Tableinfo.DATABASE_NAME, null, database_version);
    }

    public static final int database_version = 2;
    public String CREATE_QUERY = "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME + "(" + TableData.Tableinfo.CAB_NO + " TEXT," +TableData.Tableinfo.TIME + " TEXT,"+TableData.Tableinfo.TO_LOCATION + " TEXT," + TableData.Tableinfo.PICKUP_LOCATION + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(DatabaseOperations dop, String cab_no, String time,String to,String pick)

    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.CAB_NO, cab_no);
        cv.put(TableData.Tableinfo.TIME, time);
        cv.put(TableData.Tableinfo.TO_LOCATION, to);
        cv.put(TableData.Tableinfo.PICKUP_LOCATION, pick);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME, null, cv);
        Log.d("Database Created","true");

    }
    public Cursor getInformation(DatabaseOperations dop) {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloumns = {TableData.Tableinfo.CAB_NO, TableData.Tableinfo.TIME,TableData.Tableinfo.TO_LOCATION,TableData.Tableinfo.PICKUP_LOCATION,};
        Cursor CR = SQ.query(TableData.Tableinfo.TABLE_NAME, coloumns, null, null, null, null, null);

        return CR;


    }

    public ArrayList<DataItems> readData(DatabaseOperations dop) {
        ArrayList<DataItems> listData = new ArrayList<>();
        SQLiteDatabase SQ = dop.getReadableDatabase();

        Log.d("DatabasRead","");
        String[] coloumns = {TableData.Tableinfo.CAB_NO, TableData.Tableinfo.TIME,TableData.Tableinfo.TO_LOCATION,TableData.Tableinfo.PICKUP_LOCATION,};
        Cursor cursor = SQ.query(TableData.Tableinfo.TABLE_NAME, coloumns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                DataItems item = new DataItems();
                item.setCab_no(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CAB_NO)));
                item.setTime(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.TIME)));
                item.setTo_loc(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.TO_LOCATION)));
                item.setPick(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.PICKUP_LOCATION)));
                listData.add(item);
                Log.d("Database read", "true");
            }
            while (cursor.moveToNext());
        }
        return listData;
    }

    public  void eraseData(DatabaseOperations dop){
        SQLiteDatabase db = dop.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TableData.Tableinfo.TABLE_NAME, null, null);
        Log.d("Database Erased","true");
    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.

    }


}