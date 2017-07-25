package com.germanitlab.kanonhealth.helpers;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by andy on 7/25/17.
 */

public abstract class ParentActivity extends AppCompatActivity {

    public abstract void ImagePickerCallBack(Uri uri);

}
