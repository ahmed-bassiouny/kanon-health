package com.germanitlab.kanonhealth.httpchat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.DownloadListener;
import com.germanitlab.kanonhealth.chat.MapsActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        this.activity = activity;
        this.show_privacy = show_privacy;
        prefManager = new PrefManager(activity);
        userID = prefManager.getInt(PrefManager.USER_ID);
        passowrd = prefManager.getData(PrefManager.USER_PASSWORD);
        internetFilesOperations = InternetFilesOperations.getInstance(activity.getApplicationContext());
        setList(messages);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        switch (type) {
            case Constants.IMAGE_MESSAGE:
            case Constants.LOCATION_MESSAGE:
            case Constants.VIDEO_MESSAGE:
                ViewGroup imageMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_image_message, parent, false);
                ImageViewHolder imageMessageViewHolder = new ImageViewHolder(imageMessage);
                return imageMessageViewHolder;
            case Constants.AUDIO_MESSAGE:
                ViewGroup audioMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_audio_message, parent, false);
                AudioViewHolder audioMessageViewHolder = new AudioViewHolder(audioMessage);
                return audioMessageViewHolder;
            case Constants.UNDEFINED_MESSAGE:
                ViewGroup undefined= (ViewGroup) mInflater.inflate(R.layout.item_chat_undefined_message, parent, false);
                ImageViewHolder undefinedViewHolder = new ImageViewHolder(undefined);
                return undefinedViewHolder;
            default:
                ViewGroup chatTextMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_text_message, parent, false);
                TextMsgViewHolder chatTextMessageViewHolder = new TextMsgViewHolder(chatTextMessage);
                return chatTextMessageViewHolder;
        }
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
                ImageViewHolder VideoViewHolder = (ImageViewHolder) baseViewHolder;
                setVideoMessage(VideoViewHolder,position);
                break;
            case Constants.LOCATION:
                ImageViewHolder locationViewHolder = (ImageViewHolder) baseViewHolder;
                setLocationMessage(locationViewHolder, position);
                break;
            case Constants.UNDEFINED:
                ImageViewHolder undefinedViewHolder = (ImageViewHolder) baseViewHolder;
                setUndefinedMessage(undefinedViewHolder, position);
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
                case Constants.UNDEFINED:
                    return Constants.UNDEFINED_MESSAGE;
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
        public TextView message, date, privacy_txt;
        public ImageView status, privacy_image;
        public RelativeLayout background;
        public LinearLayout messageContainer;
        public ProgressBar progress_view_download, pbar_loading;

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
            pbar_loading = (ProgressBar) itemView.findViewById(R.id.pbar_loading);

        }
    }

    // Audio
    public class AudioViewHolder extends BaseViewHolder {
        public LinearLayout ll_play;
        public LinearLayout message_container;
        public RelativeLayout background;
        public ImageButton btn_play_pause;
        public TextView tv_music_current_loc, tv_music_duration, date, privacy_txt;
        public ImageView privacy_image, status;
        public ProgressBar progress_view_download, pbar_loading;
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
            status = (ImageView) itemView.findViewById(R.id.status);
            ll_play = (LinearLayout) itemView.findViewById(R.id.ll_play);
            pbar_loading = (ProgressBar) itemView.findViewById(R.id.pbar_loading);
        }
    }

    // Text
    public static class TextMsgViewHolder extends BaseViewHolder {
        public LinearLayout messageContainer;
        public TextView message, date, privacy_txt;
        public ImageView status, privacy_image;
        public RelativeLayout background;
        public ProgressBar pbar_loading;


        public TextMsgViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);
            pbar_loading = (ProgressBar) itemView.findViewById(R.id.pbar_loading);
        }
    }

                /*--------------processing layout---------------*/
    //---------------------------------------------------------------------------

    private void setTextMessage(final TextMsgViewHolder textMsgViewHolder, final int position) {
        try {
            final Message textMessage = mMessages.get(position);
            showLayout_Privacy(textMessage, position, textMsgViewHolder.privacy_image, textMsgViewHolder.messageContainer, textMsgViewHolder.background,
                    textMsgViewHolder.status, textMsgViewHolder.privacy_txt, textMsgViewHolder.date, textMsgViewHolder.pbar_loading);
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
            showLayout_Privacy(message, position, imageViewHolder.privacy_image, imageViewHolder.messageContainer, imageViewHolder.background
                    , imageViewHolder.status, imageViewHolder.privacy_txt, imageViewHolder.date, imageViewHolder.pbar_loading);
            if(message.getImageText()!=null){
                imageViewHolder.message.setText(message.getImageText());
                imageViewHolder.message.setVisibility(View.VISIBLE);
            }else
                imageViewHolder.message.setVisibility(View.GONE);
            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            ImageHelper.setImage(imageViewHolder.image_message, Constants.CHAT_SERVER_URL_IMAGE + "/" + message.getMsg(), imageViewHolder.progress_view_download, activity);
            imageViewHolder.image_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imageViewHolder.progress_view_download.getVisibility()==View.VISIBLE)
                        return;
                    Dialog dialog = new Dialog(activity);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(activity.getLayoutInflater().inflate(R.layout.show_image, null));
                    ImageView img = (ImageView) dialog.findViewById(R.id.img);
                    ProgressBar pbar = (ProgressBar) dialog.findViewById(R.id.pbar);
                    ImageHelper.setImage(img, Constants.CHAT_SERVER_URL_IMAGE + "/" + message.getMsg(), pbar, activity);
                    dialog.show();
                }
            });
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


        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUndefinedMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {

            final Message message = mMessages.get(position);


            try {
                String[] split = message.getSent_at().split(" ")[1].split(":");
                imageViewHolder.date.setText(split[0] + " " + split[1]);
            } catch (Exception ex) {
                imageViewHolder.date.setText(message.getSent_at());
            }

            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            if(message.getImageText()!=null){
                imageViewHolder.message.setText(message.getImageText());
                imageViewHolder.message.setVisibility(View.VISIBLE);
            }else
                imageViewHolder.message.setVisibility(View.GONE);
            imageViewHolder.status.setImageResource(R.drawable.pending);


        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void setAudioMessage(final AudioViewHolder audioViewHolder, final int position) {
        try {
            final Message mediaMessage = mMessages.get(position);
            //int totalDuration = audioViewHolder.mp.getDuration();

            showLayout_Privacy(mediaMessage, position, audioViewHolder.privacy_image, audioViewHolder.message_container, audioViewHolder.background,
                    audioViewHolder.status, audioViewHolder.privacy_txt, audioViewHolder.date, audioViewHolder.pbar_loading);


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
                    int progress = (audioViewHolder.utils.getProgressPercentage(currentDuration, totalDuration));
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
                    audioViewHolder.progress_view_download.setVisibility(View.GONE);
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
                } else {
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

    private void setLocationMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {

            final Message locationMessage = mMessages.get(position);
            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            showLayout_Privacy(locationMessage, position, imageViewHolder.privacy_image, imageViewHolder.messageContainer, imageViewHolder.background
                    , imageViewHolder.status, imageViewHolder.privacy_txt, imageViewHolder.date, imageViewHolder.pbar_loading);
            imageViewHolder.message.setVisibility(View.GONE);
            imageViewHolder.image_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jsonObject = new JSONObject(locationMessage.getMsg());
                        Intent intent = new Intent(activity, MapsActivity.class);
                        intent.putExtra("lat", jsonObject.getString("lat"));
                        intent.putExtra("long", jsonObject.getString("long"));
                        activity.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                        Toast.makeText(activity, R.string.cant_find_location, Toast.LENGTH_SHORT).show();
                    }
                }
            });

         /*   imageViewHolder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        String msg = "{" + locationMessage.getMsg() + "}";
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            showLocationOptions("Location", position, jsonObject.getString("lat"), jsonObject.getString("long"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });*/

            JSONObject jsonObject = new JSONObject(locationMessage.getMsg());
            double lat = jsonObject.getDouble("lat");
            double lng = jsonObject.getDouble("long");
            String URL = "http://maps.google.com/maps/api/staticmap?center=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&zoom=15&size=200x200&sensor=false";
            ImageHelper.setImage(imageViewHolder.image_message, URL, imageViewHolder.progress_view_download, activity);


           /* locationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String msg = "{" + locationMessage.getMsg() + "}";
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(msg);
                        double lat = jsonObject.getDouble("lat");
                        double lng = jsonObject.getDouble("long");
                        Util.getInstance(context).showLocation(lat, lng);
                    } catch (JSONException e) {
                        Log.e("ex", e.getMessage());
                    }
                }
            });
*/
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setVideoMessage(final ImageViewHolder imageViewHolder, final int position){

        try {
            final Message message = mMessages.get(position);
            showLayout_Privacy(message, position, imageViewHolder.privacy_image, imageViewHolder.messageContainer, imageViewHolder.background
                    , imageViewHolder.status, imageViewHolder.privacy_txt, imageViewHolder.date, imageViewHolder.pbar_loading);
            imageViewHolder.message.setVisibility(View.GONE);

            if (!new File(message.getMsg()).exists()) {
                final String fileName = message.getMsg().substring(message.getMsg().lastIndexOf("/") + 1);
                final File file = new File(folder, fileName);

                if (file.exists()) {
                    message.setLoaded(true);
                    message.setLoading(false);
                    message.setMsg(file.getPath());
                    imageViewHolder.progress_view_download.setVisibility(View.GONE);

                    Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(file.getPath(),
                            MediaStore.Video.Thumbnails.MICRO_KIND);

                    imageViewHolder.image_message.setImageBitmap(videoThumbnail);


                    playViedo(imageViewHolder.messageContainer, file.getPath());

                } else {
                    imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
                    internetFilesOperations.downloadUrlWithProgress(imageViewHolder.progress_view_download, message.getType(), message.getMsg(), new DownloadListener() {
                        @Override
                        public void onDownloadFinish(final String pathOFDownloadedFile) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    message.setMsg(pathOFDownloadedFile);
                                    message.setLoaded(true);
                                    imageViewHolder.image_message.setImageBitmap(ThumbnailUtils.createVideoThumbnail(message.getMsg(),
                                            MediaStore.Video.Thumbnails.MICRO_KIND));

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });
                                    Uri uri = Uri.fromFile(new File(message.getMsg()));
                                    message.setMsg(uri.toString());

                                    playViedo(imageViewHolder.messageContainer, message.getMsg());
                                }
                            });
                        }
                    });
                }
            }
        }catch (Exception e){
            Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show();
            Log.e("Chat Adapter", "setVideoMessage: ",e );
            Crashlytics.logException(e);
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

    public void setImagePrivacy(int privacy, ImageView image, TextView txtPrivacy) {
        if (privacy == 0) {
//            image.setBackgroundResource(R.drawable.red);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red));
            }
            txtPrivacy.setText("Private");
        } else if (privacy == 1) {
//            image.setBackgroundResource(R.drawable.blue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue));
            }
            txtPrivacy.setText("Doctor");
        } else if (privacy == 2) {
//            image.setBackgroundResource(R.drawable.green);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green));
            }
            txtPrivacy.setText("Public");
        }
    }

    private void changePrivacy(final ImageView imagePrivacy, final int pos, final TextView txtPrivacy, final ProgressBar progressBar) {
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
                                progressBar.setVisibility(View.VISIBLE);
                                new HttpCall(activity, new ApiResponse() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        if (mMessages.get(pos).getPrivacy() == 0)
                                            mMessages.get(pos).setPrivacy(1);

                                        else if (mMessages.get(pos).getPrivacy() == 1)
                                            mMessages.get(pos).setPrivacy(2);

                                        else
                                            mMessages.get(pos).setPrivacy(0);
                                        progressBar.setVisibility(View.GONE);
                                        setImagePrivacy(mMessages.get(pos).getPrivacy(), imagePrivacy, txtPrivacy);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        Toast.makeText(activity, activity.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

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

    private void showLayout_Privacy(Message message, int position, ImageView imagePrivacy, LinearLayout messageContainer, RelativeLayout background, ImageView status, TextView txtPrivacy, TextView date, ProgressBar progressBar) {
        if (show_privacy) {
            setImagePrivacy(message.getPrivacy(), imagePrivacy, txtPrivacy);
            changePrivacy(imagePrivacy, position, txtPrivacy, progressBar);
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
            } else if (mMessages.get(position).getIs_forward() == 0) {
                status.setImageResource(R.drawable.sentnew);
            } else if (mMessages.get(position).getIs_forward() == 1)
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

    private void showLocationOptions(String title, final int position, String lat, String lng) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setTitle(title);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, R.layout.listitem, R.id.textview);
        arrayAdapter.add("Share");
        arrayAdapter.add("Forward");
        arrayAdapter.add("Delete");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                if (which == 0) {

                } else if (which == 1) {
//                    startChatUsersActivity(position);
                } else if (which == 2) {

//                    int messageId = ((LocationMessage) messageArrayList.get(position).getMessageObject()).get_Id();
//                    deleteMessage(position, messageId);
                }
            }
        });

        try {
            builderSingle.show();
        } catch (Exception ex) {
        }

    }

    public void playViedo(View view, final String path) {
        try {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    File file = new File(path);
                    if (file.exists())
                        Util.getInstance(activity).showVideo(Uri.fromFile(file));
                    else {
                        String url = Constants.CHAT_SERVER_URL + "/" + path;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setDataAndType(Uri.parse(url), "video/*");
                        activity.startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.cany_play_video), Toast.LENGTH_SHORT).show();
        }

    }

    public void setList(List<Message> messages ){
        this.mMessages=messages;

    }

    public void setItem(Message item) {
        this.mMessages.add(item);
    }
}

