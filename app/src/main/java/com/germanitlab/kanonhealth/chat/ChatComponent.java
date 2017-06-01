package com.germanitlab.kanonhealth.chat;

import com.germanitlab.kanonhealth.application.AppComponent;
import com.germanitlab.kanonhealth.db.customAnotations;

import dagger.Component;

/**
 * Created by Mo on 3/19/17.
 */

@customAnotations.PerActivity
@Component(dependencies = AppComponent.class , modules = ChatModule.class)
public interface ChatComponent {

    void inject(ChatActivity chatActivity);

}
