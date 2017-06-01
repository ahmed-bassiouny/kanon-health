package com.germanitlab.kanonhealth.db;

import android.content.Context;
import android.content.res.Resources;

import com.germanitlab.kanonhealth.models.messages.Message;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mo on 3/17/17.
 */

@Singleton
public class DataManger {

    private Context mContext;
    private DbHelper mDbHelper;


    @Inject
    public DataManger(@customAnotations.ApplicationContext Context context, DbHelper dbHelper){
        mContext = context;
        mDbHelper = dbHelper;
    }

    public Long createMessage(Message message) throws Exception {
        return mDbHelper.insertMessage(message);
    }
    public Message getMessage(Integer messageId) throws Resources.NotFoundException, NullPointerException {
        return mDbHelper.getMessage(messageId);
    }
}
