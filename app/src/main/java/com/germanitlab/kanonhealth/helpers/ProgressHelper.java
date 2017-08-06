package com.germanitlab.kanonhealth.helpers;

import android.content.Context;

import com.germanitlab.kanonhealth.R;

import dmax.dialog.SpotsDialog;

/**
 * Created by milad on 8/6/17.
 */

public class ProgressHelper {

    private static SpotsDialog currentProgress = null;

    public static void showProgressBar(Context context) {
        if (currentProgress == null) {
            currentProgress = new SpotsDialog(context, R.style.CustomProgressDialog);
        }
    }

    public static void hideProgressBar() {
        if (currentProgress != null) {
            currentProgress.dismiss();
            currentProgress = null;
        }
    }
}
