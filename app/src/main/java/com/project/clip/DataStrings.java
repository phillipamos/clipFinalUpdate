package com.project.clip;

public class DataStrings {
    //DATABASE SETUP
    public static final String DATABASE_NAME = "clipDB.db";
    public static final int DATABASE_VERSION = 1;



    //FINANCE DATA STRINGS
    public static final String TABLE_BILLS    = "bills";  //TABLE NAME
    public static final String    BILL_ID     = "_id";    //Bill ID
    public static final String BILL           = "bill";  //Column name bills
    public static final String TABLE_CREATE_BILLS = "create table "
            + TABLE_BILLS + "(" + BILL_ID
            + " integer primary key autoincrement, " + BILL
            + " text not null);";

    //CAREER - NETWORK DATA STRINGS
    public static final String TABLE_NETWORK   = "career"; //TABLE NAME
    public static final String COLUMN_NETWORK_ID      = "_id";  //NETWORK ID
    public static final String COLUMN_NAME     = "name";   //NAME COLUMN
    public static final String COLUMN_AFFILIATION = "affiliation";  //AFFILIATION COLUMN
    public static final String COLUMN_DATE_EST = "date";    //DATE COLUMN
    public static final String COLUMN_TIMES_USED = "used";  //TIMES USED COLUMN
    public static final String COLUMN_COMMENTS   = "comments";  //COMMENTS COLUMN

    public static final String TABLE_CREATE_NETWORK = "create table "
            + TABLE_NETWORK + "(" + COLUMN_NETWORK_ID   + " integer primary key autoincrement, "
                                  + COLUMN_NAME         + " text null,"
                                  + COLUMN_AFFILIATION  + " text null,"
                                  + COLUMN_DATE_EST     + " text null,"
                                  + COLUMN_TIMES_USED   + " text null,"
                                  + COLUMN_COMMENTS     + " text null);";














}
