package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bassiouny on 06/08/17.
 */

public class UserInfoRepositry {
    private DatabaseHelper db;
    Dao<UserInfo, Integer> doctorsDao;

    public UserInfoRepositry(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        db.setWriteAheadLoggingEnabled(false);
        try {
            doctorsDao = db.getUsersDao();
            doctorsDao.setObjectCache(false);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public void createDoctor(UserInfo userInfo) {
        try {
            List<UserInfo> accountList =  doctorsDao.queryBuilder().where().eq("userID", userInfo.getUserID()).query();
            if(accountList.size()==0) {
                doctorsDao.create(userInfo);
            }else {
                userInfo.setIdLocalDatabase(accountList.get(0).getIdLocalDatabase());
                doctorsDao.update(userInfo);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public void createClinic(UserInfo userInfo) {
        try {
            List<UserInfo> accountList =  doctorsDao.queryBuilder().where().eq("id", userInfo.getId()).query();
            if(accountList.size()==0){
                doctorsDao.create(userInfo);
            }else {
                userInfo.setIdLocalDatabase(accountList.get(0).getIdLocalDatabase());
                doctorsDao.update(userInfo);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public UserInfo getDoctor(int userID) {
        try {

            QueryBuilder<UserInfo, Integer> queryBuilder = doctorsDao.queryBuilder();
            List<UserInfo> accountList =  doctorsDao.queryBuilder().where().eq("idLocalDatabase", userID).query();
            if (accountList.size() == 1)
                return accountList.get(0);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }
    public List<UserInfo> selectDoctorsOrClinic(boolean selectDoctor) {
        List<UserInfo> userInfoArrayList = null;
        try {
            QueryBuilder<UserInfo, Integer> queryBuilder = doctorsDao.queryBuilder();
            // prepare our sql statement
            if(selectDoctor)
                queryBuilder.where().eq("userType",UserInfo.DOCTOR);
            else
                queryBuilder.where().eq("userType",UserInfo.CLINIC);
            PreparedQuery<UserInfo> preparedQuery =queryBuilder.orderBy("idLocalDatabase", true).prepare();
            userInfoArrayList = doctorsDao.query(preparedQuery);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("getAllMessageChat: ", "HttpDocumentRepositry",e);
            userInfoArrayList=new ArrayList<>();
        }
        finally {
            return userInfoArrayList;
        }
    }

}
