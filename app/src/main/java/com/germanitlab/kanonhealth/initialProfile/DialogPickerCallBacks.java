package com.germanitlab.kanonhealth.initialProfile;

import android.content.Intent;

/**
 * Created by Mo on 3/30/17.
 */

public interface DialogPickerCallBacks {
    public void onGalleryClicked(Intent intent);
    public void onCameraClicked();

    public void deleteMyImage();
}
