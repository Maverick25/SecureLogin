package agency.alterway.edillion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import agency.alterway.edillion.models.DocumentFile;

/**
 * Created by marekrigan on 05/06/15.
 */
public class DatabaseHandler implements DBFinals
{
    private static DatabaseHandler instance;
    private static DBHelper        dbHelper;
    private static SQLiteDatabase  database;

    private DatabaseHandler(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    public static DatabaseHandler getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHandler(context);
        }
        if (database == null || !database.isOpen()) {
            open();
        }
        return instance;
    }

    private static void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public static void close()
    {
        if (dbHelper != null)
        {
            dbHelper.close();
        }
    }

    public List<DocumentFile> getAllDocuments()
    {
        List<DocumentFile> documents = new ArrayList<>();
        try
        {
            Cursor cursor = database.query(true, TABLE_DOCUMENTS, null, null, null, null, null, null, null);

            if (cursor.moveToFirst())
            {
                DocumentFile document;
                do
                {
                    long id = cursor.getLong(0);
                    String description = cursor.getString(1);
                    byte[] thumbnail = cursor.getBlob(2);

                    document = new DocumentFile(id,description,thumbnail);

                    documents.add(document);
                } while(cursor.moveToNext());

                cursor.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return documents;
    }

    public long addDocument(DocumentFile document)
    {
        try
        {
            ContentValues values = new ContentValues();

            if(document.getDescription() != null)
            {
                values.put(DESCRIPTION, document.getDescription());
            }

            values.put(THUMBNAIL, document.getThumbnail());

            return database.insert(TABLE_DOCUMENTS, null, values);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public DocumentFile getDocument(long id)
    {
        try
        {
            Cursor cursor = database.query(true,TABLE_DOCUMENTS,null,ID+" = ?",new String[]{String.valueOf(id)},null,null,null,null);

            DocumentFile document = null;
            if (cursor.moveToFirst())
            {
                long documentId = cursor.getInt(0);
                String description = cursor.getString(1);
                byte[] thumbnail = cursor.getBlob(2);

                document = new DocumentFile(documentId,description,thumbnail);

                cursor.close();
            }

            return document;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public long updateDocument(DocumentFile document)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put(DESCRIPTION, document.getDescription());
            values.put(THUMBNAIL, document.getThumbnail());

            long id = database.update(TABLE_DOCUMENTS, values, ID+" = ?", new String[] {String.valueOf(document.getId())});

            return id;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteDocument(long id)
    {
        try
        {
            database.delete(TABLE_DOCUMENTS,ID+" = ?",new String[]{String.valueOf(id)});
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteAllDocuments()
    {
        try
        {
            database.delete(TABLE_DOCUMENTS,null,null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}

