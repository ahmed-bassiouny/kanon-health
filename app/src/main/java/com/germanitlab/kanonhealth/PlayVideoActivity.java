package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.helpers.Constants;

public class PlayVideoActivity extends Activity {
    ProgressDialog pDialog;
    VideoView videoView ;
    String path ;
    String doctor_data ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        try {
            videoView =(VideoView) findViewById(R.id.video_view);
            path = getIntent().getStringExtra("video_path");
            doctor_data = getIntent().getStringExtra("doctor_id");
            videoView = (VideoView) findViewById(R.id.video_view);
            // Execute StreamVideo AsyncTask
            // Create a progressbar
            pDialog = new ProgressDialog(PlayVideoActivity.this);
            // Set progressbar title
            pDialog.setTitle("Android Video Streaming Tutorial");
            // Set progressbar message
            pDialog.setMessage("Buffering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            // Show progressbar
            pDialog.show();

            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        PlayVideoActivity.this);
                mediacontroller.setAnchorView(videoView);
                // Get the URL from String VideoURL
                String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
                Uri video = Uri.parse(Constants.CHAT_SERVER_URL + "/" + path );
                videoView.setMediaController(mediacontroller);
                videoView.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Intent intent = new Intent(getApplicationContext() , DoctorProfile.class);
                    intent.putExtra("doctor_data" , doctor_data);
                    intent.putExtra("has_document" ,true);
                    startActivity(intent);
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Intent intent = new Intent(getApplicationContext() , DoctorProfile.class);
                    intent.putExtra("doctor_data" , doctor_data);
                    intent.putExtra("has_document" ,true);
                    startActivity(intent);
                    return false;
                }
            });

            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoView.start();
                }
            });
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_playing), Toast.LENGTH_SHORT).show();
            Log.e("Passcode", "Activity ",e );
        }

    }
}
