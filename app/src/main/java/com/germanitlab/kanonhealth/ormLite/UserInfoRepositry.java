package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;

/**
 * Created by bassiouny on 06/08/17.
 */

public class UserInfoRepositry {
    private DatabaseHelper db;
    Dao<UserInfo, Integer> doctorsDao;
    private Context context;

    public UserInfoRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        db.setWriteAheadLoggingEnabled(false);
        this.context = context;
        try {
            doctorsDao = db.getUsersDao();
            doctorsDao.setObjectCache(false);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public Dao.CreateOrUpdateStatus createOrUpdate(UserInfo userInfo) {
        Dao.CreateOrUpdateStatus result = null;
        try {
            result =  doctorsDao.createOrUpdate(userInfo);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        finally {
            return result;
        }
    }
    public UserInfo getDoctor(int userID) {
        try {

            QueryBuilder<UserInfo, Integer> queryBuilder = doctorsDao.queryBuilder();
            queryBuilder.where().eq("userID", userID);
            PreparedQuery<UserInfo> preparedQuery = queryBuilder.prepare();
            List<UserInfo> accountList =  doctorsDao.queryBuilder().where().eq("userID", userID).query();
            if (accountList.size() == 1)
                return accountList.get(0);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }
}
