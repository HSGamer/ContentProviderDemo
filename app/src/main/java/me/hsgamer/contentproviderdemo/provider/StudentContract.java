package me.hsgamer.contentproviderdemo.provider;

import android.net.Uri;

public class StudentContract {
    //region Table
    public static final String TABLE_NAME = "students";
    public static final String ID = "student_id";
    public static final String COL_NAME = "student_name";
    public static final String COL_YEAR = "student_year";
    //endregion

    //region URI
    public static final String AUTHORITY = "me.hsgamer.contentproviderdemo.provider";
    public static final String BASE_PATH = "students";
    public static final String URL = "content://" + AUTHORITY + "/" + BASE_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    //endregion

    //region MIME type
    public static final String MIME_SINGLE_STUDENT = "vnd.android.cursor.item/vnd." + AUTHORITY + ".students";
    public static final String MIME_MULTI_STUDENT = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".students";
    //endregion
}
