package com.germanitlab.kanonhealth.inquiry;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Mo on 3/7/17.
 */

public interface OnChoiceSelectedListener {
    public void OnChoiceSelected(String levelName, @Nullable Fragment fragment);
}
