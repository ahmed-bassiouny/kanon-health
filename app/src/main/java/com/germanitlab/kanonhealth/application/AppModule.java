package com.germanitlab.kanonhealth.application;

import android.app.Application;
import android.content.Context;

import com.germanitlab.kanonhealth.db.customAnotations;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mo on 3/17/17.
 */

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    @customAnotations.ApplicationContext
    Context provideContext(){
        return mApplication;
    }

    @Provides
    Application provideApplication(){
        return mApplication;
    }

    @Provides
    @customAnotations.DatabaseInfo
    String provideDataBaseName(){
        return "praxis.db";
    }

    @Provides
    @customAnotations.DatabaseInfo
    Integer provideDatabaseVersion(){
        return 1;
    }

}
