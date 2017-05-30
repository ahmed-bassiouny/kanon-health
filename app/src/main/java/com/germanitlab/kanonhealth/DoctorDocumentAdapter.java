package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


/**
 * Created by Geram IT Lab on 08/05/2017.
 */

public class DoctorDocumentAdapter extends RecyclerView.Adapter<DoctorDocumentAdapter.MyViewHolder> {

    private List<Message> messageList;
    ProgressDialog pDialog;
    Context context;
    private MediaPlayer mp;
    Activity activity;
    ProgressDialog progressDialog;
    ScrollView profile_layout;
    LinearLayout video_layout;
    private MediaUtilities utils;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, tvMusicCurrentLoc, tvMusicDuration;
        public ImageView videoView;
        public ImageView imageView;
        public ImageButton btnPlayPause;
        public SeekBar seekBarMusic;
        public RelativeLayout audioView;
        public LinearLayout videoLayout, imageLayout, textLayout;
        public ProgressBar myProgressView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text_view);
            tvMusicCurrentLoc = (TextView) view.findViewById(R.id.tv_music_current_loc);
            tvMusicDuration = (TextView) view.findViewById(R.id.tv_music_duration);
            videoView = (ImageView) view.findViewById(R.id.video_view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            btnPlayPause = (ImageButton) view.findViewById(R.id.btn_play_pause);
            seekBarMusic = (SeekBar) view.findViewById(R.id.seek_bar_music);
            audioView = (RelativeLayout) view.findViewById(R.id.audio_view);
            videoLayout = (LinearLayout) view.findViewById(R.id.video_layout);
            imageLayout = (LinearLayout) view.findViewById(R.id.image_layout);
            textLayout = (LinearLayout) view.findViewById(R.id.text_layout);
            myProgressView = (ProgressBar) view.findViewById(R.id.my_progress_view);
        }
    }


    public DoctorDocumentAdapter(List<Message> messageList, Context context, Activity activity, ScrollView profile_layout) {
        this.messageList = messageList;
        this.context = context;
        pDialog = new ProgressDialog(context);
        mp = new MediaPlayer();
        this.activity = activity;
        this.profile_layout = profile_layout;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_document_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (message.getType().equals(Constants.TEXT)) {
            holder.textLayout.setVisibility(View.VISIBLE);
            holder.textView.setText(message.getMsg());
        } else if (message.getType().equals(Constants.VIDEO)) {
            createVideo(holder, position, message);
        } else if (message.getType().equals(Constants.AUDIO)) {
            createAudio(holder, position, message);
        } else if (message.getType().equals(Constants.IMAGE)) {
            createImage(holder, position, message);
        }
    }

    private void createImage(MyViewHolder holder, int position, Message message) {
        holder.imageLayout.setVisibility(View.VISIBLE);
        Picasso.with(context).load(Constants.CHAT_SERVER_URL + "/" + message.getMsg()).resize(150, 200).into(holder.imageView);
    }

    private void createAudio(final MyViewHolder holder, int position, Message message) {
        holder.audioView.setVisibility(View.VISIBLE);
        final MediaPlayer mp = new MediaPlayer();
        final Handler mHandler = new Handler();
        utils = new MediaUtilities();

        final String url = "https://chat.gagabay.com:3001/public/files/1493901067962.m4a";
        final Runnable mUpdateTimeTask = new Runnable() {
            public void run() {
                long totalDuration = mp.getDuration();
                long currentDuration = mp.getCurrentPosition();


                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                holder.seekBarMusic.setProgress(progress);
                if (mp.isPlaying())
                    mHandler.postDelayed(this, 100);
                else
                    holder.seekBarMusic.setProgress(0);

                // Running this thread after 100 milliseconds
            }
        };

        holder.btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mp.isPlaying()) {
                        if (mp != null) {
                            // Changing button image to play button
                            holder.btnPlayPause.setImageResource(R.drawable.stop);
                            holder.seekBarMusic.setProgress(0);
                            mp.reset();
                        }
                    } else {
                        holder.myProgressView.setVisibility(View.VISIBLE);
                        holder.btnPlayPause.setImageResource(R.drawable.ic_music_pause);
                        mp.setDataSource(url);
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.prepare();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mHandler.postDelayed(mUpdateTimeTask, 30);
                                holder.myProgressView.setVisibility(View.GONE);
                                mp.start();
                            }
                        });
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                holder.btnPlayPause.setImageResource(R.drawable.ic_music_play);
                                holder.seekBarMusic.setProgress(0);
                                mp.reset();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("Exception", "Exception in streaming mediaplayer e = " + e);
                }

            }
        });
        holder.seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                long currentDuration = mp.getCurrentPosition();
                holder.tvMusicDuration.setText(utils.milliSecondsToTimer(currentDuration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mp.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
                holder.tvMusicDuration.setText(utils.milliSecondsToTimer(totalDuration));

                // forward or backward to certain seconds
                mp.seekTo(currentPosition);

                // update timer progress again
                mHandler.postDelayed(mUpdateTimeTask, 100);

            }
        });


    }

    private void createVideo(final MyViewHolder holder, int position, final Message message) {
        holder.videoLayout.setVisibility(View.VISIBLE);
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video_layout.setVisibility(View.VISIBLE);
                profile_layout.setVisibility(View.GONE);
                final Dialog dialog = new Dialog(activity);// add here your class name
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                dialog.setContentView(R.layout.activity_play_video);
                final VideoView mVideoView = (VideoView) dialog.findViewById(R.id.video_view);
                final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_view);
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                dialog.setCancelable(true);
//add your own xml with defied with and height of videoview
                String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
                mVideoView.setVideoURI(Uri.parse(VideoURL));
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        progressBar.setVisibility(View.GONE);
                        DisplayMetrics metrics = new DisplayMetrics();
                        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        mVideoView.start();
                    }
                });
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (i == KeyEvent.KEYCODE_BACK) {
                            video_layout.setVisibility(View.GONE);
                            profile_layout.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                        return true;
                    }
                });
                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        dialog.dismiss();
                        video_layout.setVisibility(View.GONE);
                        profile_layout.setVisibility(View.VISIBLE);
                    }
                });
                dialog.show();

            }

        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
