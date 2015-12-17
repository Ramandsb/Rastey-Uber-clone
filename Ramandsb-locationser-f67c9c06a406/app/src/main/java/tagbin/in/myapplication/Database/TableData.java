package tagbin.in.myapplication.Database;

import android.provider.BaseColumns;

/**
 * Created by Ramandeep on 19-08-2015.
 */
public class TableData {
    public TableData() {

    }

    public static abstract class Tableinfo implements BaseColumns {
        public static final String CAB_NO = "cab_no";
        public static final String TIME = "time";
        public static final String TO_LOCATION = "to_locaiton";
        public static final String PICKUP_LOCATION = "pick_location";
        public static final String DATABASE_NAME = "dbrides";
        public static final String TABLE_NAME = "ridestable";

        public static final int database_version = 1;
    }
}