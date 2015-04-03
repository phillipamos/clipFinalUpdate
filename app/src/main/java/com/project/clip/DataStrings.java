package com.project.clip;

public class DataStrings {
    //Database strings
    public static final String TABLE_BILLS    = "bills";  //Table name
    public static final String    BILL_ID     = "_id";    //Bill ID
    public static final String BILL           = "bill";  //Bill

    public static final String TABLE_CREATE_BILLS = "create table "
            + TABLE_BILLS + "(" + BILL_ID
            + " integer primary key autoincrement, " + BILL
            + " text not null);";

}
