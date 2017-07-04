package com.germanitlab.kanonhealth.httpchat;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.DownloadListener;
import com.germanitlab.kanonhealth.callback.UploadListener;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.germanitlab.kanonhealth.helpers.Constants.folder;

/**
 * Created by bassiouny on 28/06/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<Message> mMessages;
    private Activity activity;
    private int userID;
    private String passowrd;
    List<Integer> list = new ArrayList<>();
    private boolean selected = false;
    private boolean show_privacy = false; // it's attribte to decide if it's document (show prrivacy) or chat (disappear privacy)
    private PrefManager prefManager;
    private InternetFilesOperations internetFilesOperations;

    public ChatAdapter(List<Message> messages, Activity activity, boolean show_privacy) {
        this.mMessages = messages;
        this.activity = activity;
        this.show_privacy = show_privacy;
        prefManager = new PrefManager(activity);
        userID = prefManager.getInt(PrefManager.USER_ID);
        passowrd = prefManager.getData(PrefManager.USER_PASSWORD);
        userID = 3;
        internetFilesOperations = InternetFilesOperations.getInstance(activity.getApplicationContext());
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        switch (type) {
            case Constants.IMAGE_MESSAGE:
                ViewGroup imageMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_image_message, parent, false);
                ImageViewHolder imageMessageViewHolder = new ImageViewHolder(imageMessage);
                return imageMessageViewHolder;
            case Constants.AUDIO_MESSAGE:
                ViewGroup audioMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_audio_message, parent, false);
                AudioViewHolder audioMessageViewHolder = new AudioViewHolder(audioMessage);
                return audioMessageViewHolder;
            default:
                ViewGroup chatTextMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_text_message, parent, false);
                TextMsgViewHolder chatTextMessageViewHolder = new TextMsgViewHolder(chatTextMessage);
                return chatTextMessageViewHolder;
        }
        /*switch (type) {
            case Constants.IMAGE_MESSAGE:
                ViewGroup imageMessage = (ViewGroup) mInflater.inflate(R.layout.chat_image_message_cell, parent, false);
                ImageViewHolder imageMessageViewHolder = new ImageViewHolder(imageMessage);
                return imageMessageViewHolder;
            case Constants.AUDIO_MESSAGE:
                ViewGroup audioMessage = (ViewGroup) mInflater.inflate(R.layout.chat_audio_message_cell, parent, false);
                AudioViewHolder audioMessageViewHolder = new AudioViewHolder(audioMessage);
                return audioMessageViewHolder;
            case Constants.VIDEO_MESSAGE:
                ViewGroup videoMessage = (ViewGroup) mInflater.inflate(R.layout.chat_video_message_cell, parent, false);
                VideoViewHolder videoMessageViewHolder = new VideoViewHolder(videoMessage);
                return videoMessageViewHolder;
            case Constants.LOCATION_MESSAGE:
                ViewGroup locationMessage = (ViewGroup) mInflater.inflate(R.layout.chat_location_message_cell, parent, false);
                LocationViewHolder locationViewHolder = new LocationViewHolder(locationMessage);
                return locationViewHolder;
            default:
                //for text message
                ViewGroup chatTextMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_text_message, parent, false);
                TextMsgViewHolder chatTextMessageViewHolder = new TextMsgViewHolder(chatTextMessage);
                return chatTextMessageViewHolder;
        }*/
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        switch (mMessages.get(position).getType()) {
            case Constants.IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) baseViewHolder;
                setImageMessage(imageViewHolder, position);
                break;
            case Constants.AUDIO:
                AudioViewHolder audioViewHolder = (AudioViewHolder) baseViewHolder;
                setAudioMessage(audioViewHolder, position);
                break;
            case Constants.VIDEO:
                // VideoViewHolder videoViewHolder = (VideoViewHolder) baseViewHolder;
                //setVideoMessage(videoViewHolder, position);
                break;
            case Constants.LOCATION:
                //LocationViewHolder locationViewHolder = (LocationViewHolder) baseViewHolder;
                //setLocationMessage(locationViewHolder, position);
                break;
            default:
                //for text message
                TextMsgViewHolder textMsgViewHolder = (TextMsgViewHolder) baseViewHolder;
                setTextMessage(textMsgViewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        try {
            String type = mMessages.get(position).getType();

            switch (type) {

                case Constants.TEXT:
                    return Constants.TEXT_MESSAGE;
                case Constants.AUDIO:
                    return Constants.AUDIO_MESSAGE;
                case Constants.VIDEO:
                    return Constants.VIDEO_MESSAGE;
                case Constants.IMAGE:
                    return Constants.IMAGE_MESSAGE;
                case Constants.LOCATION:
                    return Constants.LOCATION_MESSAGE;
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


        return -1;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mMessageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            if (null == message) return;
            mMessageView.setText(message);
        }

        public void setImage(Bitmap bmp) {
            if (null == mImageView) return;
            if (null == bmp) return;
            mImageView.setImageBitmap(bmp);
        }

    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public Toolbar toolbar;

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    /*------------------------------this code just copied from old code :( --------------------------------------*/

    /***** declare layout ****/
    // Image
    public class ImageViewHolder extends BaseViewHolder {
        public ImageView image_message;
        public TextView message, date,privacy_txt;
        public ImageView status, privacy_image;
        public RelativeLayout background;
        public LinearLayout messageContainer;
        public ProgressBar progress_view_download;

        public ImageViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);

            image_message = (ImageView) itemView.findViewById(R.id.image_message);
            progress_view_download = (ProgressBar) itemView.findViewById(R.id.progress_view_download);


        }
    }

    // Audio
    public class AudioViewHolder extends BaseViewHolder {
        public LinearLayout ll_play;
        public LinearLayout message_container;
        public RelativeLayout background;
        public ImageButton btn_play_pause;
        public TextView tv_music_current_loc,tv_music_duration,date,privacy_txt;
        public ImageView privacy_image,status;
        public ProgressBar progress_view_download;
        public SeekBar seek_bar_music;

        private MediaPlayer mp;
        // Handler to update UI timer, progress bar etc,.
        private Handler mHandler = new Handler();

        private MediaUtilities utils;


        public AudioViewHolder(View itemView) {
            super(itemView);
            message_container = (LinearLayout) itemView.findViewById(R.id.message_container);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            btn_play_pause = (ImageButton) itemView.findViewById(R.id.btn_play_pause);
            tv_music_current_loc = (TextView) itemView.findViewById(R.id.tv_music_current_loc);
            seek_bar_music = (SeekBar) itemView.findViewById(R.id.seek_bar_music);
            tv_music_duration = (TextView) itemView.findViewById(R.id.tv_music_duration);
            progress_view_download = (ProgressBar) itemView.findViewById(R.id.progress_view_download);
            date = (TextView) itemView.findViewById(R.id.date);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);
            status=(ImageView)itemView.findViewById(R.id.status);
            ll_play=(LinearLayout)itemView.findViewById(R.id.ll_play);
        }
    }

    // Video
    public class VideoViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public ImageView myMessage, hisMessage;
        public FrameLayout myFrameVideo, hisFrameVideo;
        public ProgressBar progressBar, progressViewDownload;
        public RelativeLayout messageContainer;
        public TextView tvDate, tvDateMy;
        public ImageView imgMessageStatus;

        public VideoViewHolder(View itemView) {
            super(itemView);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myMessage = (ImageView) itemView.findViewById(R.id.my_video_message);
            hisMessage = (ImageView) itemView.findViewById(R.id.his_video_message);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_view);
            progressViewDownload = (ProgressBar) itemView.findViewById(R.id.progress_view_download);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            imgMessageStatus = (ImageView) itemView.findViewById(R.id.my_message_status);
            myFrameVideo = (FrameLayout) itemView.findViewById(R.id.my_frame_video);
            hisFrameVideo = (FrameLayout) itemView.findViewById(R.id.his_frame_video);
        }
    }

    //Location
    public class LocationViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public ImageView myMessage, hisMessage;
        public ProgressBar myProgressBar, hisProgressBar;
        public RelativeLayout messageContainer;
        public ImageView imgMessageStatus;
        public TextView tvDate, tvDateMy;


        public LocationViewHolder(View itemView) {
            super(itemView);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            imgMessageStatus = (ImageView) itemView.findViewById(R.id.my_message_status);
            myMessage = (ImageView) itemView.findViewById(R.id.my_image_message);
            hisMessage = (ImageView) itemView.findViewById(R.id.his_image_message);
            myProgressBar = (ProgressBar) itemView.findViewById(R.id.my_progress_view);
            hisProgressBar = (ProgressBar) itemView.findViewById(R.id.his_progress_view);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            imgMessageStatus = (ImageView) itemView.findViewById(R.id.my_message_status);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
        }
    }

    // Text
    public static class TextMsgViewHolder extends BaseViewHolder {
        public LinearLayout messageContainer;
        public TextView message, date,privacy_txt;
        public ImageView status, privacy_image;
        RelativeLayout background;

        public TextMsgViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);
        }
    }

                /*--------------processing layout---------------*/
    //---------------------------------------------------------------------------

    private void setTextMessage(final TextMsgViewHolder textMsgViewHolder, final int position) {
        try {
            final Message textMessage = mMessages.get(position);
            showLayout_Privacy(textMessage,position,textMsgViewHolder.privacy_image,textMsgViewHolder.messageContainer,textMsgViewHolder.background,textMsgViewHolder.status,textMsgViewHolder.privacy_txt,textMsgViewHolder.date);
            textMsgViewHolder.message.setText(textMessage.getMsg());


            textMsgViewHolder.background.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick(textMessage, textMsgViewHolder.background);
                    return true;
                }
            });
            textMsgViewHolder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclick(textMessage, textMsgViewHolder.background);
                }
            });

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }
    private void setImageMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {

            final Message message = mMessages.get(position);
            showLayout_Privacy(message,position,imageViewHolder.privacy_image,imageViewHolder.messageContainer,imageViewHolder.background,imageViewHolder.status,imageViewHolder.privacy_txt,imageViewHolder.date);



            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            ImageHelper.setImage(imageViewHolder.image_message,Constants.CHAT_SERVER_URL_IMAGE+"/"+message.getMsg(),imageViewHolder.progress_view_download,activity);
            Uri imageUri = Uri.fromFile(new File(message.getMsg()));
            Glide.with(activity).load(Constants.CHAT_SERVER_URL_IMAGE+"/"+message.getMsg()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    imageViewHolder.progress_view_download.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).into(imageViewHolder.image_message);
            //imageViewHolder.date.setText(message.getSent_at());
            //setImagePrivacy(message.getPrivacy(), imageViewHolder.privacy_image);

            /*if (!new File(message.getMsg()).exists()) {
                String fileName = message.getMsg().substring(message.getMsg().lastIndexOf("/") + 1);
                File file = new File(folder, fileName);
                if (file.exists()) {
                    message.setMsg(file.getPath());
                    message.setLoaded(true);
                    message.setLoading(false);
                }
            }*/


            //ImageHelper.setImage(imageViewHolder.image_message, imageUri, activity);

            /*if (!message.isLoaded() && !message.isLoading()) {

                message.setLoading(true);

                imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
                internetFilesOperations.uploadFileWithProgress(imageViewHolder.progress_view_download, Constants.UPLOAD_URL, message.getMsg(), new UploadListener() {
                    @Override
                    public void onUploadFinish(final String result) {
                        (activity).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message.setLoading(false);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.has("error")) {
                                        message.setLoaded(true);
                                        showAlerDialog(Constants.UPLOAD_URL, Html.fromHtml(jsonObject.getString("error")).toString());
                                    } else {
                                        if (!folder.exists()) folder.mkdirs();
                                        String source = message.getMsg();
                                        File destination = new File(folder, jsonObject.getString("file_url").substring(jsonObject.getString("file_url").lastIndexOf("/") + 1));
                                        if (moveFile(new File(source), destination)) {
                                            message.setMsg(destination.getPath());
                                        }
                                        message.setLoaded(true);
                                        sendMessage(jsonObject.getString("file_url"), Constants.IMAGE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } else {
                imageViewHolder.progress_view_download.setVisibility(View.INVISIBLE);
            }
*/
            imageViewHolder.messageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(mMessages.get(position).getMsg());

                    if (file.exists())
                        Util.getInstance(activity).showPhoto(Uri.fromFile(file));
                }
            });
            imageViewHolder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected) {
                        if (!list.contains(message.get_Id()))
                            selectItem(imageViewHolder.background, message);
                        else
                            unselectItem(imageViewHolder.background, message);
                    } else {

                        File file = new File(mMessages.get(position).getMsg());

                        if (file.exists())
                            Util.getInstance(activity).showPhoto(Uri.fromFile(file));
                    }
                }
            });
            imageViewHolder.background.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick(message, imageViewHolder.background);
                    return true;
                }
            });


        }catch (Exception e){
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }
    private void setAudioMessage(final AudioViewHolder audioViewHolder, final int position) {
        try {
            final Message mediaMessage = mMessages.get(position);
            //int totalDuration = audioViewHolder.mp.getDuration();

            showLayout_Privacy(mediaMessage,position,audioViewHolder.privacy_image,audioViewHolder.message_container,audioViewHolder.background,audioViewHolder.status,audioViewHolder.privacy_txt,audioViewHolder.date);




                // Mediaplayer
                audioViewHolder.mp = new MediaPlayer();
                audioViewHolder.utils = new MediaUtilities();

                final Runnable mUpdateTimeTask = new Runnable() {
                    public void run() {
                        long totalDuration = audioViewHolder.mp.getDuration();
                        long currentDuration = audioViewHolder.mp.getCurrentPosition();
                        // Displaying Total Duration time
                        // Displaying time completed playing
                        // Updating progress bar
                        audioViewHolder.tv_music_duration.setText("" + audioViewHolder.utils.milliSecondsToTimer(totalDuration));
                        int progress =  (audioViewHolder.utils.getProgressPercentage(currentDuration, totalDuration));
                        audioViewHolder.seek_bar_music.setProgress(progress);

                        // Running this thread after 100 milliseconds
                        audioViewHolder.mHandler.postDelayed(this, 30);
                    }
                };
                // Listeners

                audioViewHolder.seek_bar_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        long currentDuration = audioViewHolder.mp.getCurrentPosition();
                        audioViewHolder.tv_music_duration.setText("" + audioViewHolder.utils.milliSecondsToTimer(currentDuration));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        audioViewHolder.mHandler.removeCallbacks(mUpdateTimeTask);


                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        audioViewHolder.mHandler.removeCallbacks(mUpdateTimeTask);
                        int totalDuration = audioViewHolder.mp.getDuration();
                        int currentPosition = audioViewHolder.utils.progressToTimer(seekBar.getProgress(), totalDuration);
                        audioViewHolder.tv_music_duration.setText("" + audioViewHolder.utils.milliSecondsToTimer(totalDuration));
                        // forward or backward to certain seconds
                        audioViewHolder.mp.seekTo(currentPosition);

                        // update timer progress again
                        audioViewHolder.mHandler.postDelayed(mUpdateTimeTask, 100);

                    }
                }); // Important
                audioViewHolder.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        audioViewHolder.btn_play_pause.setImageResource(R.drawable.ic_music_play);
                        audioViewHolder.seek_bar_music.setProgress(0);
                    }
                }); // Important


                audioViewHolder.btn_play_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (audioViewHolder.mp.isPlaying()) {
                            if (audioViewHolder.mp != null) {
                                audioViewHolder.mp.pause();
                                // Changing button image to play button
                                audioViewHolder.btn_play_pause.setImageResource(R.drawable.ic_music_play);
                            }
                        } else {
                            // Resume song
                            if (audioViewHolder.mp != null) {
                                audioViewHolder.mp.start();
                                // Changing button image to pause button
                                audioViewHolder.btn_play_pause.setImageResource(R.drawable.ic_music_pause);
                            }
                        }
                    }
                });
                if (!new File(mediaMessage.getMsg()).exists()) {
                    String fileName = mediaMessage.getMsg().substring(mediaMessage.getMsg().lastIndexOf("/") + 1);
                    File file = new File(folder, fileName);

                    if (file.exists()) {
                        audioViewHolder.ll_play.setVisibility(View.VISIBLE);
                        mediaMessage.setMsg(file.getPath());
                        mediaMessage.setLoaded(true);
                        mediaMessage.setLoading(false);

                        try {
                            audioViewHolder.mp.reset();
                            audioViewHolder.mp.setDataSource(file.getPath());
                            audioViewHolder.mp.prepare();
                            audioViewHolder.seek_bar_music.setProgress(0);
                            audioViewHolder.seek_bar_music.setMax(100);

                            // Updating progress bar
                            audioViewHolder.mHandler.postDelayed(mUpdateTimeTask, 100);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        internetFilesOperations.downloadUrlWithProgress(audioViewHolder.progress_view_download, mediaMessage.getType(), mediaMessage.getMsg(), new DownloadListener() {
                            @Override
                            public void onDownloadFinish(final String pathOFDownloadedFile) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mediaMessage.setMsg(pathOFDownloadedFile);
                                        mediaMessage.setLoaded(true);

                                        try {
                                            audioViewHolder.mp.reset();
                                            audioViewHolder.mp.setDataSource(mediaMessage.getMsg());
                                            audioViewHolder.mp.prepare();
                                            long currentDuration = audioViewHolder.mp.getCurrentPosition();
                                            audioViewHolder.tv_music_duration.setText("" + audioViewHolder.utils.milliSecondsToTimer(currentDuration));
                                            audioViewHolder.seek_bar_music.setProgress(0);
                                            audioViewHolder.seek_bar_music.setMax(100);
                                            Uri uri = Uri.fromFile(new File(mediaMessage.getMsg()));
                                            mediaMessage.setMsg(uri.toString());
                                            audioViewHolder.ll_play.setVisibility(View.VISIBLE);
                                            // Updating progress bar
                                            audioViewHolder.mHandler.postDelayed(mUpdateTimeTask, 100);
                                        } catch (IllegalArgumentException e) {
                                            e.printStackTrace();
                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                    }

                } else {

                    try {
                        audioViewHolder.mp.reset();
                        audioViewHolder.mp.setDataSource(mediaMessage.getMsg());
                        audioViewHolder.mp.prepare();

                        // set Progress bar values
                        audioViewHolder.seek_bar_music.setProgress(0);
                        audioViewHolder.seek_bar_music.setMax(100);

                        // Updating progress bar
                        audioViewHolder.mHandler.postDelayed(mUpdateTimeTask, 100);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }





            /*
            audioViewHolder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!selected) {
                        changeToolbar(true);
                    }
                    if (!list.contains(mediaMessage.get_Id()))
                        selectItem(audioViewHolder.messageContainer, mediaMessage);
                    else {
                        unselectItem(audioViewHolder.messageContainer, mediaMessage);
                    }

                    return true;
                }
            });
            audioViewHolder.messageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected)
                        if (!list.contains(mediaMessage.get_Id()))
                            selectItem(audioViewHolder.messageContainer, mediaMessage);
                        else {
                            unselectItem(audioViewHolder.messageContainer, mediaMessage);
                        }
                    else {

                    }
                }
            });*/
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }



                /*-------------------------- Additional important Methods------------*/

    public void selectItem(RelativeLayout messageContainer, Message message) {
        messageContainer.setBackgroundResource(R.color.gray_black);
        list.add(message.get_Id());
    }

    public void changeToolbar(Boolean select) {
        selected = true;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        Toolbar toolbar1 = (Toolbar) activity.findViewById(R.id.toolbar2);
        if (select) {
            toolbar.setVisibility(View.GONE);
            toolbar1.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
            toolbar1.setVisibility(View.GONE);
        }
    }

    private void unselectItem(RelativeLayout messageContainer, Message message) {
        try {
            messageContainer.setBackgroundResource(0);
            list.remove(list.indexOf(message.get_Id()));
            if (list.size() == 0) {
                changeToolbar(false);
                selected = false;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void longClick(Message textMessage, RelativeLayout background) {
        if (!selected) {
            changeToolbar(true);
        }
        if (!list.contains(textMessage.get_Id()))
            selectItem(background, textMessage);
        else
            unselectItem(background, textMessage);
    }

    private void onclick(Message textMessage, RelativeLayout background) {
        if (selected) {
            if (!list.contains(textMessage.get_Id()))
                selectItem(background, textMessage);
            else {
                unselectItem(background, textMessage);
            }
        }
    }

    public void setImagePrivacy(int privacy, ImageView image,TextView txtPrivacy) {
        if (privacy == 0) {
//            image.setBackgroundResource(R.drawable.red);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red));
            }
            txtPrivacy.setText("Private");
        }
        else if (privacy == 1) {
//            image.setBackgroundResource(R.drawable.blue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue));
            }
            txtPrivacy.setText("Doctor");
        }
        else if (privacy == 2) {
//            image.setBackgroundResource(R.drawable.green);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green));
            }
            txtPrivacy.setText("Public");
        }
    }

    private void changePrivacy(final ImageView imagePrivacy, final int pos, final TextView txtPrivacy) {
        imagePrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setMessage(R.string.change_privacy_msg);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ja",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int position) {
                                dialog.cancel();
                                new HttpCall(activity, new ApiResponse() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        if (mMessages.get(pos).getPrivacy() == 0)
                                            mMessages.get(pos).setPrivacy(1);

                                        else if (mMessages.get(pos).getPrivacy() == 1)
                                            mMessages.get(pos).setPrivacy(2);

                                        else
                                            mMessages.get(pos).setPrivacy(0);
                                        setImagePrivacy(mMessages.get(pos).getPrivacy(),imagePrivacy,txtPrivacy);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        Toast.makeText(activity, activity.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();

                                    }
                                }).updatePrivacy(String.valueOf(userID), passowrd, mMessages.get(pos).get_Id(), (mMessages.get(pos).getPrivacy() + 1));
                            }
                        });

                builder1.setNegativeButton(
                        "Nein",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
    public void showAlerDialog(String title, String message) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        try {
            builder.create().show();
        } catch (Exception ex) {
        }

    }
    public boolean moveFile(File source, File destination) {

        try {
            FileInputStream inStream = new FileInputStream(source);
            FileOutputStream outStream = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            //delete the original file
            source.delete();


            return true;

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showLayout_Privacy(Message message,int position,ImageView imagePrivacy,LinearLayout messageContainer,RelativeLayout background,ImageView status,TextView txtPrivacy,TextView date){
        if (show_privacy) {
            setImagePrivacy(message.getPrivacy(), imagePrivacy,txtPrivacy);
            changePrivacy(imagePrivacy, position,txtPrivacy);
            imagePrivacy.setVisibility(View.VISIBLE);
            imagePrivacy.setVisibility(View.VISIBLE);
        } else {
            imagePrivacy.setVisibility(View.GONE);
            txtPrivacy.setVisibility(View.GONE);
        }
        if (mMessages.get(position).getFrom_id() == userID) {
            background.setGravity(Gravity.RIGHT);
            messageContainer.setBackgroundResource(R.drawable.bubble_in);
            status.setVisibility(View.VISIBLE);

            if (mMessages.get(position).getSeen() == 1) {
                status.setImageResource(R.drawable.readnew);
            } else if (mMessages.get(position).getIs_delivered() == 1) {
                status.setImageResource(R.drawable.receivenew);
            } else if (mMessages.get(position).getIs_forward() == 1) {
                status.setImageResource(R.drawable.sentnew);
            } else
                status.setImageResource(R.drawable.pending);
        } else {
            background.setGravity(Gravity.LEFT);
            messageContainer.setBackgroundResource(R.drawable.bubble_out);
            status.setVisibility(View.GONE);

        }
        try {
            String[] split = message.getSent_at().split(" ")[1].split(":");
            date.setText(split[0] + " " + split[1]);
        } catch (Exception ex) {
            date.setText(message.getSent_at());
        }
    }
}

