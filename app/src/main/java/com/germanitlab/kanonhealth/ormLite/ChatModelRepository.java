package com.germanitlab.kanonhealth.ormLite;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.j256.ormlite.dao.Dao;

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
    public void createAnother(ChatModel chatModel) {
        try {
            List<ChatModel> doctorList =  chatModelDao.queryBuilder().where().eq("userID", chatModel.getId()).query();
            if(doctorList.size()==0){
                List<ChatModel> clinicList =  chatModelDao.queryBuilder().where().eq("id", chatModel.getId()).query();
                if(clinicList.size()==0){
                    chatModelDao.create(chatModel);
                }else {
                    chatModel.setIdLocalDatabase(clinicList.get(0).getIdLocalDatabase());
                    chatModelDao.update(chatModel);
                }
            }else {
                chatModel.setIdLocalDatabase(doctorList.get(0).getIdLocalDatabase());
                chatModelDao.update(chatModel);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }
}
