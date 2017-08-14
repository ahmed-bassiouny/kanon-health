package com.germanitlab.kanonhealth.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.germanitlab.kanonhealth.R;

import dmax.dialog.SpotsDialog;

/**
 * Created by andy on 7/25/17.
 */

public class ParentActivity extends AppCompatActivity {

    public  void ImagePickerCallBack(Uri uri){};

    private SpotsDialog currentProgress = null;

    public void showProgressBar() {
        if (currentProgress == null) {
            currentProgress = new SpotsDialog(ParentActivity.this, R.style.CustomProgressDialog);
            currentProgress.show();
        }
    }

    public void hideProgressBar() {
        if (currentProgress != null) {
            currentProgress.dismiss();
            currentProgress = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressBar();
    }
}
