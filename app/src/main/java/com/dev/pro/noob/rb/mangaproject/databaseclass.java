package com.dev.pro.noob.rb.mangaproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by RB on 13-07-2015.
 */
public class databaseclass
{
    databasehelper dbh;
    public databaseclass(Context context)
    {
        dbh  = new databasehelper(context);
    }
    public long insert(String manganame, int chapternumber)
    {
        SQLiteDatabase sq = dbh.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(databasehelper.MANGANAME,manganame);
        contentValues.put(databasehelper.CHAPTERNUMBER,chapternumber);
        long id = sq.insert(databasehelper.TABLE_NAME,null,contentValues);
        return id;
    }
    public int delete(String name,int chapternumber)
    {
        SQLiteDatabase db = dbh.getWritableDatabase();
        String[] arg={Integer.toString(chapternumber)};
        int status = db.delete(databasehelper.TABLE_NAME,databasehelper.CHAPTERNUMBER+" =?",arg);
        return status;

    }
    public ArrayList<ArrayList<String>> getAllData()
    {

        SQLiteDatabase db = dbh.getWritableDatabase();
        String[] columns = {databasehelper.UID,databasehelper.MANGANAME,databasehelper.CHAPTERNUMBER};
        Cursor cursor = db.query(databasehelper.TABLE_NAME, columns, null, null, null, null, null, null);
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> list1= new ArrayList<>();
        ArrayList<String> list2= new ArrayList<>();
        while(cursor.moveToNext())
        {
            final String name = cursor.getString(cursor.getColumnIndex(databasehelper.MANGANAME));
            final int number = cursor.getInt(cursor.getColumnIndex(databasehelper.CHAPTERNUMBER));
            list1.add(name);
            list2.add(Integer.toString(number));
        }
        data.add(list1);
        data.add(list2);
        return data;
    }
    class databasehelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "downloadsdatabase";
        private static final String TABLE_NAME = "downloadlist";
        private static final int DATABASE_VERSION = 3;
        private static final String UID = "_id";
        private static final String MANGANAME = "Manganame";
        private static final String CHAPTERNUMBER = "Chapternumber";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MANGANAME + " VARCHAR(255), " + CHAPTERNUMBER + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public databasehelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
        {
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }
    }
}
