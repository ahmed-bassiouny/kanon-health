package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;

/**
 * Created by bassiouny on 06/08/17.
 */

public class ChatModelRepositry {
    private DatabaseHelper db;
    Dao<ChatModel, Integer> doctorsDao;
    private Context context;

    public ChatModelRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        this.context = context;
        try {
            doctorsDao = db.getChatModelsDao();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public Dao.CreateOrUpdateStatus createOrUpdate(ChatModel chatModel) {
        try {
            return doctorsDao.createOrUpdate(chatModel);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }
    public ChatModel getDoctor(int userID) {
        try {

            QueryBuilder<ChatModel, Integer> queryBuilder = doctorsDao.queryBuilder();
            queryBuilder.where().eq(ChatModel.KEY_USERID, userID);
            PreparedQuery<ChatModel> preparedQuery = queryBuilder.prepare();
            List<ChatModel> accountList =  doctorsDao.queryBuilder().where().eq(ChatModel.KEY_USERID, userID).query();
            if (accountList.size() == 1)
                return accountList.get(0);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }
}
