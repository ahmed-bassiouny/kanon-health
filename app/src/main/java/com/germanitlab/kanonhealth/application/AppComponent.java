package com.germanitlab.kanonhealth.application;

import android.app.Application;
import android.content.Context;

import com.germanitlab.kanonhealth.db.DataManger;
import com.germanitlab.kanonhealth.db.customAnotations;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mo on 3/17/17.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(AppController appController);

    @customAnotations.ApplicationContext
    Context getContext();

    Application getApplication();

    DataManger getDataManger();
}
