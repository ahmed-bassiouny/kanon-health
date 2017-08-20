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
 * Created by bassiouny on 20/08/17.
 */

public class ChatModelRepository {
    private DatabaseHelper db;
    Dao<ChatModel, Integer> chatModelDao;

    public ChatModelRepository(Context context) {
        DatabaseManager databaseManager = new DatabaseManager();
        db = databaseManager.getHelper(context);
        db.setWriteAheadLoggingEnabled(false);
        try {
            chatModelDao = db.getChatModeDao();
            chatModelDao.setObjectCache(false);
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public void createDoctorOrUser(ChatModel chatModel) {
        try {
            List<ChatModel> accountList =  chatModelDao.queryBuilder().where().eq("userID", chatModel.getUserID()).query();
            if(accountList.size()==0) {
                chatModelDao.create(chatModel);
            }else {
                chatModel.setIdLocalDatabase(accountList.get(0).getIdLocalDatabase());
                chatModelDao.update(chatModel);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    public void createClinic(ChatModel chatModel) {
        try {
            List<ChatModel> accountList =  chatModelDao.queryBuilder().where().eq("id", chatModel.getId()).query();
            if(accountList.size()==0) {
                chatModelDao.create(chatModel);
            }else {
                chatModel.setIdLocalDatabase(accountList.get(0).getIdLocalDatabase());
                chatModelDao.update(chatModel);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
    private List<ChatModel> getList(int type) {
        List<ChatModel> chatModels = null;
        try {
            QueryBuilder<ChatModel, Integer> queryBuilder = chatModelDao.queryBuilder();
            // prepare our sql statement
            if(type==UserInfo.DOCTOR)
                queryBuilder.where().eq("userType",UserInfo.DOCTOR);
            else if(type==UserInfo.CLINIC)
                queryBuilder.where().eq("userType", UserInfo.CLINIC);
            else if(type==UserInfo.PATIENT)
                queryBuilder.where().eq("userType", UserInfo.PATIENT);
            else
                queryBuilder.where().not().eq("userType", UserInfo.PATIENT);

            PreparedQuery<ChatModel> preparedQuery =queryBuilder.orderBy("idLocalDatabase", true).prepare();
            chatModels = chatModelDao.query(preparedQuery);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("getAllMessageChat: ", "HttpDocumentRepositry",e);
            chatModels=new ArrayList<>();
        }
        finally {
            return chatModels;
        }
    }
    public List<ChatModel> getDoctors() {
        return getList(UserInfo.DOCTOR);
    }
    public List<ChatModel> getClinics() {
        return getList(UserInfo.CLINIC);
    }
    public List<ChatModel> getUsers() {
        return getList(UserInfo.PATIENT);
    }
    public List<ChatModel> getAnother() {
        return getList(0);
    }
}
