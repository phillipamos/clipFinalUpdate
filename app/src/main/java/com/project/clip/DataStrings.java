package com.project.clip;

public class DataStrings {
    //DATABASE SETUP
    public static final String DATABASE_NAME = "clipDB.db";
    public static final int DATABASE_VERSION = 1;



    //FINANCE DATA STRINGS

        //TABLE BILLS
        public static final String TABLE_BILLS    = "bills";  //TABLE NAME
        public static final String COLUMN_BILL_ID        = "_id";    //Bill ID
        public static final String COLUMN_BILL           = "bill";  //Column name bills
        public static final String COLUMN_BILL_DUE = "due_date";
        public static final String COLUMN_BILL_SITE = "website";
            //Bills table create string
            public static final String TABLE_CREATE_BILLS = "create table "
            + TABLE_BILLS + "("   + COLUMN_BILL_ID + " integer primary key autoincrement, "
                                  + COLUMN_BILL      + " text null, "
                                  + COLUMN_BILL_DUE  + " text null, "
                                  + COLUMN_BILL_SITE + " text null);";

        //TABLE ACCOUNTS
        public static final String TABLE_ACCOUNTS = "accounts"; //ACCOUNTS TABLE
        public static final String COLUMN_ACCOUNT_ID     =        "_id";      //ACCOUNT ID
        public static final String COLUMN_ACCOUNT_NAME   =        "name";     //ACCOUNT NAME
        public static final String COLUMN_ACCOUNT_WEBSITE=        "website";
        public static final String COLUMN_ACCOUNT_BALANCE   =  "balance";        //ACCOUNT VALUE
            //Account table create string
            public static final String TABLE_CREATE_ACCOUNT = "create table "
            + TABLE_ACCOUNTS + "(" + COLUMN_ACCOUNT_ID       + " integer primary key autoincrement, "
                                   + COLUMN_ACCOUNT_NAME     + " text null, "
                                   + COLUMN_ACCOUNT_WEBSITE  + " text null, "
                                   + COLUMN_ACCOUNT_BALANCE    + " text null);";

        //TABLE STOCKS
        public static final String TABLE_STOCKS   = "stocks"; //STOCKS TABLE
        public static final String COLUMN_STOCK_ID = "_id";   //STOCK ID
        public static final String COLUMN_STOCK_NAME = "name";
        public static final String COLUMN_STOCK_VALUE = "value";
             //Table create string
             public static final String TABLE_CREATE_STOCK = "create table "
            + TABLE_STOCKS + "(" + COLUMN_STOCK_ID    + " integer primary key autoincrement, "
                                 + COLUMN_STOCK_NAME  + " text null, "
                                 + COLUMN_STOCK_VALUE + " text null);";


        //TOTALS TABLE
        public static final String TABLE_TOTALS   = "totals"; //TOTALS TABLE
        public static final String COLUMN_TOTALS_ID = "_id";
        public static final String COLUMN_CREDIT_TOTAL = "credit_total";
        public static final String COLUMN_BANK_TOTAL   = "bank_total";
        public static final String COLUMN_STOCK_TOTAL  = "stock_total";
        public static final String COLUMN_ASSET_TOTAL  = "asset_total";
        public static final String COLUMN_CASH_TOTAL   = "cash_total";
            //Table create string
            public static final String TABLE_CREATE_TOTALS = "create table "
            +TABLE_TOTALS + "("  + COLUMN_TOTALS_ID    + " integer primary key autoincrement, "
                                 + COLUMN_CREDIT_TOTAL + " text null, "
                                 + COLUMN_BANK_TOTAL   + " text null, "
                                 + COLUMN_STOCK_TOTAL  + " text null, "
                                 + COLUMN_ASSET_TOTAL  + " text null, "
                                 + COLUMN_CASH_TOTAL   + " text_null);";



        //ASSETS TABLE
        public static final String TABLE_ASSETS  = "assets";
        public static final String COLUMN_ASSET_ID = "_id";
        public static final String COLUMN_ASSET_NAME = "name";
        public static final String COLUMN_ASSET_VALUE = "value";
        public static final String TABLE_CREATE_ASSETS = "create table "
            +TABLE_ASSETS + "("  + COLUMN_ASSET_ID     + " integer primary key autoincrement, "
                                 + COLUMN_ASSET_NAME  +  " text null, "
                                 + COLUMN_ASSET_VALUE + " text null); ";









    //CAREER - NETWORK DATA STRINGS
    public static final String TABLE_NETWORK   = "network"; //TABLE NAME
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
