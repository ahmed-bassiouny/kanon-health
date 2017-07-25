package com.germanitlab.kanonhealth.helpers;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by andy on 7/25/17.
 */

public abstract class ParentFragment extends Fragment {

    public abstract void ImagePickerCallBack(Uri uri);

}
