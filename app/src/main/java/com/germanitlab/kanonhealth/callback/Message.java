package com.germanitlab.kanonhealth.callback;

import java.util.ArrayList;

/**
 * Created by ahmed on 10/06/17.
 */


public interface Message <T>{
    void Response(ArrayList<T> specialitiesArrayList);
}
