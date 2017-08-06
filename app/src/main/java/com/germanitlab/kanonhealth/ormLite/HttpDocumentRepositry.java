package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.Document;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by bassiouny on 06/08/17.
 */

public class HttpDocumentRepositry {
    private DatabaseHelper db;
    Dao<Document, Integer> documentsDao;
    private Context context;

    public HttpDocumentRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        this.context = context;
        db = databaseManager.getHelper(context);
        try {
            documentsDao = db.getHttpDocumentDao();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Error In Constructor","HttpDocumentRepositry: ", e);
        }
    }
    public void createOrUpate(Document document){
        try {
            documentsDao.createOrUpdate(document);
        } catch (SQLException e) {
            Crashlytics.logException(e);
            Log.e("createOrUpate: ", "HttpDocumentRepositry",e);
        }
    }
    public List<Document> getAllDocumentChat() {
        // get chat from me and another user
        try {
            QueryBuilder<Document, Integer> queryBuilder = documentsDao.queryBuilder();
            // prepare our sql statement
            PreparedQuery<Document> preparedQuery = queryBuilder.orderBy(Document.KEY_DOCUMENT_ID, true).prepare();
            List<Document> messageList = documentsDao.query(preparedQuery);
            return messageList;
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("getAllMessageChat: ", "HttpDocumentRepositry",e);
        }
        return null;
    }
}

