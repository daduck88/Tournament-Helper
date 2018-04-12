package com.tournament.helper;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Indian Dollar on 3/5/2017.
 */

public class Contract implements BaseColumns {

    public static final String TABLE_NAME = "tournaments_list";
    public static final String COL_NAME_TEXT = "name";

    public static final String SCHEMA = "content://";
    public static final String AUTHORITY = "com.tournament.helper";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEMA + AUTHORITY);
    public static final String PATH_TOURNAMENTS = "tournaments";
    public static final Uri PATH_TOURNAMENTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOURNAMENTS).build();

}
