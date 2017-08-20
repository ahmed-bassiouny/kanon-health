package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.Document;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
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
        db = databaseManager.getHelper(context);
        db.setWriteAheadLoggingEnabled(false);
        try {
            documentsDao = db.getHttpDocumentDao();
            documentsDao.setObjectCache(false);
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
        List<Document> documents=null;
        try {
            QueryBuilder<Document, Integer> queryBuilder = documentsDao.queryBuilder();
            // prepare our sql statement
            PreparedQuery<Document> preparedQuery = queryBuilder.orderBy("documentId", true).prepare();
            documents = documentsDao.query(preparedQuery);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("getAllMessageChat: ", "HttpDocumentRepositry",e);
            documents=new ArrayList<>();
        }finally {
            return documents;
        }
    }
}

