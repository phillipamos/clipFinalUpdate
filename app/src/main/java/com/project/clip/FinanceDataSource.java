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
    //private String[] allColumns = { DataStrings.BILL_ID,
    //        DataStrings.BILL};
    private String[] networkColumns = {DataStrings.COLUMN_NETWORK_ID,DataStrings.COLUMN_AFFILIATION,
    DataStrings.COLUMN_TIMES_USED,DataStrings.COLUMN_COMMENTS};
    private String[] accountsColumns = {DataStrings.COLUMN_ACCOUNT_ID,DataStrings.COLUMN_ACCOUNT_NAME,
    DataStrings.COLUMN_ACCOUNT_WEBSITE, DataStrings.COLUMN_ACCOUNT_BALANCE};
    private String[] stocksColumns = {DataStrings.COLUMN_STOCK_ID, DataStrings.COLUMN_STOCK_NAME,
    DataStrings.COLUMN_STOCK_VALUE};
    private String[] billsColumns = {DataStrings.COLUMN_BILL_ID, DataStrings.COLUMN_BILL,
    DataStrings.COLUMN_BILL_DUE, DataStrings.COLUMN_BILL_SITE};
    private String[] totalsColumns = {DataStrings.COLUMN_TOTALS_ID, DataStrings.COLUMN_CREDIT_TOTAL,
    DataStrings.COLUMN_BANK_TOTAL, DataStrings.COLUMN_STOCK_TOTAL, DataStrings.COLUMN_ASSET_TOTAL,
    DataStrings.COLUMN_CASH_TOTAL};
    private String[] assetsColumns = {DataStrings.COLUMN_ASSET_ID,DataStrings.COLUMN_ASSET_NAME,
    DataStrings.COLUMN_ASSET_VALUE};


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
        database.execSQL(DataStrings.TABLE_CREATE_ACCOUNT);
        database.execSQL(DataStrings.TABLE_CREATE_TOTALS);
        database.execSQL(DataStrings.TABLE_CREATE_STOCK);
        database.execSQL(DataStrings.TABLE_CREATE_ASSETS);


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
        values.put(DataStrings.COLUMN_BILL, bill_name);
        long insertId = database.insert(DataStrings.TABLE_BILLS, null,
                values);
        Cursor cursor = database.query(DataStrings.TABLE_BILLS,
                billsColumns, DataStrings.COLUMN_BILL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FinanceData newBill = cursorToComment(cursor);
        cursor.close();
        return newBill;
    }



    /*********CREATE ACCOUNTS FUNCTION - CREATES NEW ACCOUNT ROW*******/
    public FinanceData createAccount (String name, String website, String balance){
        ContentValues values = new ContentValues();


        values.put(DataStrings.COLUMN_ACCOUNT_WEBSITE,"https://"+website);
        values.put(DataStrings.COLUMN_ACCOUNT_NAME, name);
        values.put(DataStrings.COLUMN_ACCOUNT_BALANCE,balance);
        long insertId = database.insert(DataStrings.TABLE_ACCOUNTS, null,
                values);
        Cursor cursor = database.query(DataStrings.TABLE_ACCOUNTS,
                accountsColumns, DataStrings.COLUMN_ACCOUNT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FinanceData newAccount = cursorToComment(cursor);
        cursor.close();
        return newAccount;
    }

    /********CREATE STOCKS FUNCTION - CREATES NEW STOCK ROW************/
    public FinanceData createStock(String name, String value){
        ContentValues values = new ContentValues();


        values.put(DataStrings.COLUMN_STOCK_NAME,name);     //Inserts name of stock into name column
        values.put(DataStrings.COLUMN_STOCK_VALUE, value);  //Inserts value of stock into value column

        long insertId = database.insert(DataStrings.TABLE_STOCKS, null,
                values);

        Cursor cursor = database.query(DataStrings.TABLE_STOCKS,
                stocksColumns, DataStrings.COLUMN_STOCK_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FinanceData newStock = cursorToComment(cursor);
        cursor.close();
        return newStock;

    }

    /********CREATE TOTALS FUNCTION - CREATE NEW ACCOUNT TOTALS*******/
    public void createTotal(String creditTotal, String bankTotal, String stockTotal, String assetTotal, String cashTotal){
        ContentValues values = new ContentValues();





        //If statements for individual totals
        if(creditTotal != null){

            //Add value passed in to current value in the database
            String credit = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS,DataStrings.COLUMN_CREDIT_TOTAL,"_id","1");
            //Calculate the new value
            float oldTotal;

            //Ensure that old value is numeric to be parsed to Int
            if(isNumeric(credit)) {
                oldTotal = Float.parseFloat(credit);
            }
            else{
                oldTotal=0;
            }
            float add      = Float.parseFloat(creditTotal);
            float newTotal = oldTotal+add;
            String total = Float.toString(newTotal);

            //Store new value into database
            values.put(DataStrings.COLUMN_CREDIT_TOTAL,total);
            database.update(DataStrings.TABLE_TOTALS,values,"_id=1",null);
        }
        else if(bankTotal != null){
            values.put(DataStrings.COLUMN_BANK_TOTAL, bankTotal);
            database.update(DataStrings.TABLE_TOTALS,values,"_id=1",null);
        }
        else if(stockTotal !=null){


            String stock = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS,DataStrings.COLUMN_STOCK_TOTAL,"_id","1");
            float oldTotal;
            if(isNumeric(stock)) {
                oldTotal = Float.parseFloat(stock);
            }
            else{
                oldTotal=0;
            }
            float add      = Float.parseFloat(stockTotal);
            float newTotal = oldTotal+add;
            String total = Float.toString(newTotal);
            values.put(DataStrings.COLUMN_STOCK_TOTAL, total);
            database.update(DataStrings.TABLE_TOTALS,values,"_id=1",null);



        }
        else if(assetTotal !=null){
            String asset = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS,DataStrings.COLUMN_ASSET_TOTAL,"_id","1");
            float oldTotal;
            if(isNumeric(asset)) {
                oldTotal = Float.parseFloat(asset);
            }
            else{
                oldTotal=0;
            }
            float add      = Float.parseFloat(assetTotal);
            float newTotal = oldTotal+add;
            String total = Float.toString(newTotal);

            values.put(DataStrings.COLUMN_ASSET_TOTAL, total);
            database.update(DataStrings.TABLE_TOTALS,values,"_id=1",null);
        }
         else if(cashTotal !=null){
            String money = MainActivity.database.getFromDb(DataStrings.TABLE_TOTALS,DataStrings.COLUMN_CASH_TOTAL,"_id","1");
            float oldTotal;
            if(isNumeric(money)) {
                oldTotal = Float.parseFloat(money);
            }
            else{
                oldTotal=0;
            }
            float add      = Float.parseFloat(cashTotal);
            float newTotal = oldTotal+add;
            String total = Float.toString(newTotal);

            values.put(DataStrings.COLUMN_CASH_TOTAL, total);
            database.update(DataStrings.TABLE_TOTALS,values,"_id=1",null);
        }


       else{
            values.put(DataStrings.COLUMN_CREDIT_TOTAL,"0");
            values.put(DataStrings.COLUMN_BANK_TOTAL, "0");
            values.put(DataStrings.COLUMN_STOCK_TOTAL, "0");
            values.put(DataStrings.COLUMN_ASSET_TOTAL, "0");
            values.put(DataStrings.COLUMN_CASH_TOTAL, "0");

            long insertId = database.insert(DataStrings.TABLE_TOTALS, null,
                    values);

            Cursor cursor = database.query(DataStrings.TABLE_TOTALS,
                    totalsColumns, DataStrings.COLUMN_TOTALS_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            FinanceData newTotals = cursorToComment(cursor);
            cursor.close();
           // return newTotals;
        }

    }

    /******************CREATE BILLS REMINDER**************************/
    public void createReminder(String name, String address, String date){
        ContentValues values = new ContentValues();

        values.put(DataStrings.COLUMN_BILL, name + "      " + date);
        values.put(DataStrings.COLUMN_BILL_SITE, address);
        values.put(DataStrings.COLUMN_BILL_DUE, date);



        long insertId = database.insert(DataStrings.TABLE_BILLS, null, values);

        Cursor cursor = database.query(DataStrings.TABLE_BILLS, billsColumns,DataStrings.COLUMN_BILL_ID
                +" = "+ insertId,null,null,null,null);

        cursor.moveToFirst();
        cursor.close();



    }


    /****************CREATE ASSETS FUNCTION***************************/
    public void createAsset(String name, String value){
        ContentValues values = new ContentValues();

        values.put(DataStrings.COLUMN_ASSET_NAME, name);
        values.put(DataStrings.COLUMN_ASSET_VALUE, value);



        long insertId = database.insert(DataStrings.TABLE_ASSETS, null, values);

        Cursor cursor = database.query(DataStrings.TABLE_ASSETS, assetsColumns,DataStrings.COLUMN_ASSET_ID
                +" = "+ insertId,null,null,null,null);

        cursor.moveToFirst();
        cursor.close();
    }



    public String getFromDb(String tableName, String select, String selectBy, String selectName){
        String selection = "0";

        Cursor c = database.query(tableName, new String[] {select}, selectBy + "=" + selectName, null, null, null, null);
        if(c.getCount() == 1){
            c.moveToFirst();
            selection = c.getString(c.getColumnIndex(select));
        }

        c.close();
        //database.close();
        Log.d("ClipDB", select + "=" + selection);
        return selection;
    }



    public List<String> getAllFromDb(String tableName, String column) {
        List<String> list = new ArrayList<String>();
        Cursor c = database.rawQuery("select * from " + tableName, null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c
                        .getColumnIndex(column));

                list.add(name);
                c.moveToNext();


            }


        }
        c.moveToFirst();
        c.close();
        return list;
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



    public String[] returnString()
    {
        Cursor dbCursor = database.query(DataStrings.TABLE_NETWORK, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();
        return columnNames;
    }








    public boolean deleteRow(String table, String id, String idNumber)
    {
        return database.delete(table, id + " = " + idNumber, null) > 0;
    }



    public List<FinanceData> getAllTotals(String TABLE) {
        List<FinanceData> comments = new ArrayList<FinanceData>();



        Cursor cursor = database.query(TABLE,
                totalsColumns, null, null, null, null, null);


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

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}

