package usc.com.uscmaps.example1.shubham.uscmaps.data;

import android.provider.BaseColumns;

/**x
 * Created by Shubham on 2/21/17.
 */

public class WaitListContract {
    public static final class WaitListEntry implements BaseColumns{
        public static final String TABLE_NAME = "building";
        public static final String COLUMN_SERIAL = "serial";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_BUILDING_NAME = "buldingName";
        public static final String COLUMN_ADDRESS = "address";


        public static final String TABLE_NAME_PARKING = "parking";
        public static final String COLUMN_SYMBOL_PARKING = "symbol_parking";
        public static final String COLUMN_NAME_PARKING = "name_parking";
        public static final String COLUMN_ADDRESS_PARKING = "address_parking";
        public static final String COLUMN_DESCRIPTION_PARKING = "description_parking";



    }
}
