package com.germanitlab.kanonhealth.interfaces;

import android.view.View;

/**
 * Created by Eslam A.Gwad on 3/22/16.
 */
public interface MyClickListener {

    void onClick(View view, int position);

    void onClick(Object object);

    void onLongClick(View view, int position);
}
