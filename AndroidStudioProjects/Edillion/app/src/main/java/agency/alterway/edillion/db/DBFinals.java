package agency.alterway.edillion.db;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by marekrigan on 05/06/15.
 */
public interface DBFinals
{
    SimpleDateFormat formatter      = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    // Database Info
    int    DATABASE_VERSION = 1;
    String DATABASE_NAME    = "edillion.db";

    // Table Names
    String TABLE_DOCUMENTS = "table_documents";

    // TABLE_DOCUMENTS Table - column names
    String ID   = "document_id";
    String DESCRIPTION = "document_description";
    String THUMBNAIL   = "document_thumbnail";

    String CREATE_TABLE_DOCUMENTS = "CREATE TABLE " + TABLE_DOCUMENTS +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DESCRIPTION + " TEXT,"
            + THUMBNAIL + " BLOB)";

}
