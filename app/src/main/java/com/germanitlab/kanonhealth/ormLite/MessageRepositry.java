package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Geram IT Lab on 16/05/2017.
 */

public class MessageRepositry {

    private DatabaseHelper db;
    Dao<Message, Integer> messagesDao;
    private Context context ;

    public MessageRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        this.context = context ;
        db = databaseManager.getHelper(context);
        try {
            messagesDao = db.getMessagesDao();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public int create(Message message) {
        try {
            if (!messagesDao.idExists(message.get_Id()))
                return messagesDao.create(message);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.erre_database_insert), Toast.LENGTH_SHORT).show();
        }
        return 0;
    }


    public double count() {
        try {
            return messagesDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Message> getAll(int id) {
        try {

            QueryBuilder<Message, Integer> queryBuilder =
                    messagesDao.queryBuilder();
// the 'password' field must be equal to "qwerty"
            queryBuilder.where().eq("to_id", id).or().eq("from_id", id);
// prepare our sql statement
            PreparedQuery<Message> preparedQuery = queryBuilder.orderBy("id", true).prepare();
// query for all accounts that have "qwerty" as a password
            List<Message> messageList = messagesDao.query(preparedQuery);
            return messageList;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_getting_database), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
