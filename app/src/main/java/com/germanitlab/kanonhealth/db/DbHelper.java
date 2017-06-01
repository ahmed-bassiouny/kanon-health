package com.germanitlab.kanonhealth.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.germanitlab.kanonhealth.models.messages.Message;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mo on 3/17/17.
 */

@Singleton
public class DbHelper extends SQLiteOpenHelper {

    //Messages Table
    public static final String MESSAGES_TABLE_NAME = "messages";
    public static final String MESSAGES_COLUMN_MESSAGE_ID = "id";
    public static final String MESSAGES_COLUMN_MESSAGE_FROM_ID = "from_id";
    public static final String MESSAGES_COLUMN_MESSAGE_TO_ID = "to_id";
    public static final String MESSAGES_COLUMN_MESSAGE_SENT_AT = "sent_at";
    public static final String MESSAGES_COLUMN_MESSAGE_TYPE = "type";
    public static final String MESSAGES_COLUMN_MESSAGE_MSG = "msg";
    public static final String MESSAGES_COLUMN_MESSAGE_MINE = "mine";
    public static final String MESSAGES_COLUMN_MESSAGE_LOADED = "loaded";
    public static final String MESSAGES_COLUMN_MESSAGE_LOADING = "loading";
    public static final String MESSAGES_COLUMN_MESSAGE_STATUS = "status";
    public static final String MESSAGES_COLUMN_MESSAGE_SEEN = "seen";

    @Inject
    public DbHelper(@customAnotations.ApplicationContext Context context,
                    @customAnotations.DatabaseInfo String dbName,
                    @customAnotations.DatabaseInfo Integer version){
        super(context,dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        messagesTableCreateStatement(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MESSAGES_TABLE_NAME);
        onCreate(db);
    }


    public void messagesTableCreateStatement (SQLiteDatabase database){
        try {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + MESSAGES_TABLE_NAME + "("
                            + MESSAGES_COLUMN_MESSAGE_ID + "TEXT, "
                            + MESSAGES_COLUMN_MESSAGE_TO_ID + "INTEGER, "
                            + MESSAGES_COLUMN_MESSAGE_FROM_ID + "INTEGER, "
                            + MESSAGES_COLUMN_MESSAGE_SENT_AT + "INTEGER, "
                            + MESSAGES_COLUMN_MESSAGE_TYPE + "TEXT, "
                            + MESSAGES_COLUMN_MESSAGE_MSG + "TEXT, "
                            + MESSAGES_COLUMN_MESSAGE_MINE + "VARCHAR(10), "
                            + MESSAGES_COLUMN_MESSAGE_LOADED + "INTEGER, "
                            + MESSAGES_COLUMN_MESSAGE_LOADING + "INTEGER, "
                            + MESSAGES_COLUMN_MESSAGE_STATUS + "TEXT, "
                            + MESSAGES_COLUMN_MESSAGE_SEEN + "VARCHAR(10), "
                            + ")"
            );
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    protected Long insertMessage (Message message) throws Exception {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MESSAGES_COLUMN_MESSAGE_ID, message.get_Id());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_TO_ID, message.getTo_id());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_FROM_ID, message.getFrom_id());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_SENT_AT, message.getSent_at());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_TYPE, message.getType());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_MSG, message.getMsg());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_MINE, message.isMine());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_LOADED, message.isLoaded());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_LOADING, message.isLoading());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_STATUS, message.getStatus());
            contentValues.put(MESSAGES_COLUMN_MESSAGE_SEEN, message.getSeen());
            return  db.insert(MESSAGES_TABLE_NAME, null, contentValues);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    protected Message getMessage (Integer messageId) throws Resources.NotFoundException, NullPointerException {
        Cursor cursor = null;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                    + MESSAGES_TABLE_NAME
                    + " WHERE "
                    + MESSAGES_COLUMN_MESSAGE_ID
                    + " = ? "
                    ,new String[] {});

            if (cursor.getCount() > 0) {
                Message message = new Message();
                message.setId(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_ID));
                message.setTo_id(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_TO_ID));
                message.setFrom_id(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_FROM_ID));
                message.setSent_at(String.valueOf(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_SENT_AT)));
                message.setType(String.valueOf(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_TYPE)));
                message.setMsg(String.valueOf(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_MSG)));

//                message.setLoaded(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_LOADED));
//                message.setLoading(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_LOADING));
                message.setStatus(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_STATUS));
                message.setSeen(cursor.getColumnIndex(MESSAGES_COLUMN_MESSAGE_SEEN));

                return message;
            } else {
                throw new Resources.NotFoundException("Message with id " + messageId + " doesn't exists");
            }
        } catch (NullPointerException e){
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


}
