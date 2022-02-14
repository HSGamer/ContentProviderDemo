package me.hsgamer.contentproviderdemo.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import me.hsgamer.contentproviderdemo.provider.StudentContract;

public class StudentDatabase extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "StudentDatabase";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "student_db";

    private static final String CREATE_TABLE_QUERY = "create table " + StudentContract.TABLE_NAME
            + " ("
            + StudentContract.ID + " integer primary key autoincrement,"
            + StudentContract.COL_NAME + " text not null,"
            + StudentContract.COL_YEAR + " integer not null"
            + ");";

    public StudentDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DEBUG_TAG, "Upgrading database. Existing contents will be lost. [" + oldVersion + "]->[" + newVersion + "]");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StudentContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
