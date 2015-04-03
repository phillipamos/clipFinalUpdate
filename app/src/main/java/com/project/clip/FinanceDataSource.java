package com.project.clip;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FinanceDataSource extends SQLiteOpenHelper{


    // Database fields
    private SQLiteDatabase database;
    //private DBAdapter dbHelper;
    private String[] allColumns = { DataStrings.BILL_ID,
            DataStrings.BILL};
    private String[] networkColumns = {DataStrings.COLUMN_NETWORK_ID,DataStrings.COLUMN_AFFILIATION,
    DataStrings.COLUMN_TIMES_USED,DataStrings.COLUMN_COMMENTS};



    public FinanceDataSource(Context context) {
        super(context, DataStrings.DATABASE_NAME, null, DataStrings.DATABASE_VERSION);
    }

    public void open() throws SQLException {
        database = getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
         database.execSQL(DataStrings.TABLE_CREATE_BILLS);
        database.execSQL(DataStrings.TABLE_CREATE_NETWORK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FinanceDataSource.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DataStrings.TABLE_BILLS);
        onCreate(db);
    }







    public FinanceData createBills(String bill_name) {
        ContentValues values = new ContentValues();
        values.put(DataStrings.BILL, bill_name);
        long insertId = database.insert(DataStrings.TABLE_BILLS, null,
                values);
        Cursor cursor = database.query(DataStrings.TABLE_BILLS,
                allColumns, DataStrings.BILL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FinanceData newBill = cursorToComment(cursor);
        cursor.close();
        return newBill;
    }



    public FinanceData createNetwork(String name, String affiliation, String date, String used,
    String comment){


        ContentValues values = new ContentValues();

        values.put(DataStrings.COLUMN_NAME, name);
        values.put(DataStrings.COLUMN_AFFILIATION,affiliation);
        values.put(DataStrings.COLUMN_DATE_EST, date);
        values.put(DataStrings.COLUMN_TIMES_USED, used);
        values.put(DataStrings.COLUMN_COMMENTS, comment);

        long insertId = database.insert(DataStrings.TABLE_NETWORK, null, values);

        Cursor cursor = database.query(DataStrings.TABLE_NETWORK, networkColumns,DataStrings.COLUMN_NETWORK_ID
        +" = "+ insertId,null,null,null,null);

        cursor.moveToFirst();
        FinanceData newNetwork = cursorToComment(cursor);
        cursor.close();
        return newNetwork;

    }












    public void deleteComment(FinanceData comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(DataStrings.TABLE_BILLS, DataStrings.BILL_ID
                + " = " + id, null);
    }



    public List<FinanceData> getAllComments(String TABLE) {
        List<FinanceData> comments = new ArrayList<FinanceData>();



        Cursor cursor = database.query(TABLE,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FinanceData comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public List<FinanceData> getAllNetwork(){

        List<FinanceData> comments = new ArrayList<FinanceData>();



        Cursor cursor = database.query(DataStrings.TABLE_NETWORK,
                networkColumns, null, null, null, null, null);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FinanceData comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;




    }

    private FinanceData cursorToComment(Cursor cursor) {
        FinanceData comment = new FinanceData();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }
}

