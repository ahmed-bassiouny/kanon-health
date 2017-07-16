package com.germanitlab.kanonhealth.chat;

import android.app.Activity;
import android.content.Context;

import com.germanitlab.kanonhealth.db.customAnotations;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mo on 3/18/17.
 */

@Module
public class ChatModule {

    private Activity mActivity;

    public ChatModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    public Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @customAnotations.PerActivity
    public Context provideContext() {
        return mActivity;
    }
}
