package com.germanitlab.kanonhealth;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.crashlytics.android.Crashlytics;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class VideoFragment extends DialogFragment implements EasyVideoCallback {


    private EasyVideoPlayer player;
    private String video;

    public VideoFragment(String video) {
        this.video = video;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.video_fragment, container, false);

        try {
            player = (EasyVideoPlayer) view.findViewById(R.id.player);
            getDialog().setTitle("Videos");
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            // Grabs a reference to the player view

            // Sets the callback to this Activity, since it inherits EasyVideoCallback
            player.setCallback(this);

            // Sets the source to the HTTP URL held in the TEST_URL variable.
            // To play files, you can use Uri.fromFile(new File("..."))
            player.setSource(Uri.parse(video));

            // From here, the player view will show a progress indicator until the player is prepared.
            // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.

            // Defaults to false. Immediately starts playback when the player becomes prepared.
            player.setAutoPlay(true);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        player.pause();
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
