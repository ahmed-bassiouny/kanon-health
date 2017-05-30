package com.germanitlab.kanonhealth.chat;

import dagger.Component;
import com.germanitlab.kanonhealth.application.AppComponent;
import com.germanitlab.kanonhealth.db.customAnotations;

/**
 * Created by Mo on 3/19/17.
 */

@customAnotations.PerActivity
@Component(dependencies = AppComponent.class , modules = ChatModule.class)
public interface ChatComponent {

    void inject(ChatActivity chatActivity);

}
