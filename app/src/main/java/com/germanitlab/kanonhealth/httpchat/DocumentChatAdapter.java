package com.germanitlab.kanonhealth.httpchat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.MapsActivity;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.callback.DownloadListener;
import com.germanitlab.kanonhealth.forward.ForwardActivity;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.helpers.Util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.germanitlab.kanonhealth.helpers.Constants.folder;


/**
 * Created by bassiouny on 06/08/17.
 */

public class DocumentChatAdapter extends RecyclerView.Adapter<DocumentChatAdapter.BaseViewHolder> implements View.OnLongClickListener {

    private List<Document> documents;
    private Activity activity;
    private int userID;
    List<Integer> list = new ArrayList<>();
    HashSet<Integer> selectedPositions = new HashSet<>();
    private boolean selected = false;
    private InternetFilesOperations internetFilesOperations;
    private final int FORWARDMSG = 5;
    ImageView forward;
    boolean myDocument;

    public DocumentChatAdapter(List<Document> documents, final Activity activity,boolean myDocument) {
        this.activity = activity;
        userID = PrefHelper.get(activity, PrefHelper.KEY_USER_ID,-1);
        internetFilesOperations = InternetFilesOperations.getInstance(activity.getApplicationContext());
        this.myDocument=myDocument;
        setList(documents);
        forward = (ImageView) activity.findViewById(R.id.imgbtn_forward);

        if (forward != null)
            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.size() == 0)
                        Toast.makeText(activity, R.string.please_select_messages, Toast.LENGTH_LONG).show();
                    else {
                        Intent intent = new Intent(activity, ForwardActivity.class);
                        intent.putExtra("list", (ArrayList<Integer>) list);
                        intent.putExtra("chat_doctor_id", userID);
                        activity.startActivityForResult(intent, FORWARDMSG);
                    }
                }
            });
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        switch (type) {
            case Message.MESSAGE_TYPE_IMAGE_NUM:
            case Message.MESSAGE_TYPE_LOCATION_NUM:
            case Message.MESSAGE_TYPE_VIDEO_NUM:
                ViewGroup imageMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_image_message, parent, false);
                ImageViewHolder imageMessageViewHolder = new ImageViewHolder(imageMessage);
                return imageMessageViewHolder;
            case Message.MESSAGE_TYPE_AUDIO_NUM:
                ViewGroup audioMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_audio_message, parent, false);
                AudioViewHolder audioMessageViewHolder = new AudioViewHolder(audioMessage);
                return audioMessageViewHolder;
            case Message.MESSAGE_TYPE_UNDEFINED_NUM:
                ViewGroup undefined = (ViewGroup) mInflater.inflate(R.layout.item_chat_undefined_message, parent, false);
                UndefinedViewHolder undefinedViewHolder = new UndefinedViewHolder(undefined);
                return undefinedViewHolder;
            default:
                ViewGroup chatTextMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_text_message, parent, false);
                TextMsgViewHolder chatTextMessageViewHolder = new TextMsgViewHolder(chatTextMessage);
                return chatTextMessageViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        if (documents.get(position).getType() != null)
            switch (documents.get(position).getType()) {
                case Message.MESSAGE_TYPE_IMAGE:
                    ImageViewHolder imageViewHolder = (ImageViewHolder) baseViewHolder;
                    setImageMessage(imageViewHolder, position);
                    break;
                case Message.MESSAGE_TYPE_AUDIO:
                    AudioViewHolder audioViewHolder = (AudioViewHolder) baseViewHolder;
                    setAudioMessage(audioViewHolder, position);
                    break;
                case Message.MESSAGE_TYPE_VIDEO:
                    ImageViewHolder VideoViewHolder = (ImageViewHolder) baseViewHolder;
                    setVideoMessage(VideoViewHolder, position);
                    break;
                case Message.MESSAGE_TYPE_LOCATION:
                    ImageViewHolder locationViewHolder = (ImageViewHolder) baseViewHolder;
                    setLocationMessage(locationViewHolder, position);
                    break;
                case Message.MESSAGE_TYPE_UNDEFINED:
                    UndefinedViewHolder undefinedViewHolder = (UndefinedViewHolder) baseViewHolder;
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
        if (documents.get(position).getType() != null) {
            switch (documents.get(position).getType()) {
                case Message.MESSAGE_TYPE_AUDIO:
                    return Message.MESSAGE_TYPE_AUDIO_NUM;
                case Message.MESSAGE_TYPE_VIDEO:
                    return Message.MESSAGE_TYPE_VIDEO_NUM;
                case Message.MESSAGE_TYPE_IMAGE:
                    return Message.MESSAGE_TYPE_IMAGE_NUM;
                case Message.MESSAGE_TYPE_LOCATION:
                    return Message.MESSAGE_TYPE_LOCATION_NUM;
                case Message.MESSAGE_TYPE_UNDEFINED:
                    return Message.MESSAGE_TYPE_UNDEFINED_NUM;
                default:
                    return Message.MESSAGE_TYPE_TEXT_NUM;
            }
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return documents.size();
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
        public TextView date, privacy_txt;
        public ImageView status, privacy_image, play_video;
        public FrameLayout messageContainer;
        public ProgressBar progress_view_download, pbar_loading;
        public RelativeLayout relative_main;
        public View select_item;

        public ImageViewHolder(View itemView) {
            super(itemView);
            messageContainer = (FrameLayout) itemView.findViewById(R.id.message_container);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);
            play_video = (ImageView) itemView.findViewById(R.id.play_video);
            image_message = (ImageView) itemView.findViewById(R.id.image_message);
            progress_view_download = (ProgressBar) itemView.findViewById(R.id.progress_view_download);
            pbar_loading = (ProgressBar) itemView.findViewById(R.id.pbar_loading);
            relative_main = (RelativeLayout) itemView.findViewById(R.id.relative_main);
            select_item = (View) itemView.findViewById(R.id.select_item);
        }
    }

    // Audio
    public class AudioViewHolder extends BaseViewHolder {
        public FrameLayout message_container;
        public ImageView btn_play_pause;
        public SeekBar seek_bar_music;
        public ProgressBar loading;
        public TextView tv_music_duration, date, privacy_txt;
        public ImageView privacy_image, status;
        public ProgressBar pbar_loading;
        public RelativeLayout relative_main;
        public View select_item;
        private MediaPlayer mp;
        // Handler to update UI timer, progress bar etc,.
        private Handler mHandler = new Handler();

        private MediaUtilities utils;


        public AudioViewHolder(View itemView) {
            super(itemView);
            message_container = (FrameLayout) itemView.findViewById(R.id.message_container);
            btn_play_pause = (ImageView) itemView.findViewById(R.id.btn_play_pause);
            seek_bar_music = (SeekBar) itemView.findViewById(R.id.seek_bar_music);
            tv_music_duration = (TextView) itemView.findViewById(R.id.tv_music_duration);
            date = (TextView) itemView.findViewById(R.id.date);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);
            status = (ImageView) itemView.findViewById(R.id.status);
            pbar_loading = (ProgressBar) itemView.findViewById(R.id.pbar_loading);
            loading = (ProgressBar) itemView.findViewById(R.id.loading);
            relative_main = (RelativeLayout) itemView.findViewById(R.id.relative_main);
            select_item = (View) itemView.findViewById(R.id.select_item);
        }
    }

    // Text
    public static class TextMsgViewHolder extends BaseViewHolder {
        public FrameLayout messageContainer;
        public TextView message, date, privacy_txt;
        public ImageView status, privacy_image;
        public ProgressBar pbar_loading;
        public RelativeLayout relative_main;
        public View select_item;

        public TextMsgViewHolder(View itemView) {
            super(itemView);
            messageContainer = (FrameLayout) itemView.findViewById(R.id.message_container);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            privacy_txt = (TextView) itemView.findViewById(R.id.privacy_txt);
            pbar_loading = (ProgressBar) itemView.findViewById(R.id.pbar_loading);
            relative_main = (RelativeLayout) itemView.findViewById(R.id.relative_main);
            select_item = (View) itemView.findViewById(R.id.select_item);
        }
    }

    public class UndefinedViewHolder extends BaseViewHolder {
        public TextView date;

        public UndefinedViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }

                /*--------------processing layout---------------*/
    //---------------------------------------------------------------------------

    private void setTextMessage(final TextMsgViewHolder textMsgViewHolder, final int position) {
        try {
            final Document textMessage = documents.get(position);
            showLayout_Privacy(textMessage, position, textMsgViewHolder.privacy_image, textMsgViewHolder.messageContainer,
                    textMsgViewHolder.status, textMsgViewHolder.privacy_txt, textMsgViewHolder.date, textMsgViewHolder.pbar_loading, textMsgViewHolder.relative_main);
            textMsgViewHolder.message.setText(StringEscapeUtils.unescapeJava(textMessage.getDocument()));
            setTextSelector(textMsgViewHolder.messageContainer, textMsgViewHolder.select_item);

            textMsgViewHolder.relative_main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick(textMessage, textMsgViewHolder.select_item, position);
                    return true;
                }
            });
            textMsgViewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onclick(textMessage, textMsgViewHolder.select_item, position);
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

            final Document message = documents.get(position);
            showLayout_Privacy(message, position, imageViewHolder.privacy_image, imageViewHolder.messageContainer, imageViewHolder.status, imageViewHolder.privacy_txt,
                    imageViewHolder.date, imageViewHolder.pbar_loading, imageViewHolder.relative_main);
            imageViewHolder.play_video.setVisibility(View.GONE);
            imageViewHolder.image_message.setImageBitmap(null);
            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            ImageHelper.setImage(imageViewHolder.image_message, ApiHelper.SERVER_IMAGE_URL + message.getMedia(), imageViewHolder.progress_view_download);
            imageViewHolder.image_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageViewHolder.progress_view_download.getVisibility() == View.VISIBLE)
                        return;
                    Dialog dialog = new Dialog(activity);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(activity.getLayoutInflater().inflate(R.layout.show_image, null));
                    ImageView img = (ImageView) dialog.findViewById(R.id.img);
                    ProgressBar pbar = (ProgressBar) dialog.findViewById(R.id.pbar);
                    ImageHelper.setImage(img, ApiHelper.SERVER_IMAGE_URL + message.getMedia(), pbar);
                    dialog.show();
                }
            });

            imageViewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected) {
                        if (!list.contains(message.getDocumentId())) {
                            selectItem(imageViewHolder.select_item, message, position);
                            selectedPositions.add(position);
                        } else {
                            unselectItem(imageViewHolder.select_item, message, position);
                            selectedPositions.remove(position);
                        }
                    }
                }
            });
            imageViewHolder.relative_main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick(message, imageViewHolder.select_item, position);
                    return true;
                }
            });


        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUndefinedMessage(final UndefinedViewHolder imageViewHolder, final int position) {
        try {

            final Document message = documents.get(position);


            try {
                String[] split = message.getDateTime().split(" ")[1].split(":");
                imageViewHolder.date.setText(split[0] + ":" + split[1] + " ");
            } catch (Exception ex) {
                imageViewHolder.date.setText(message.getDateTime());
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void setAudioMessage(final AudioViewHolder audioViewHolder, final int position) {
        try {
            final Document mediaMessage = documents.get(position);
            //int totalDuration = audioViewHolder.mp.getDuration();
            showLayout_Privacy(mediaMessage, position, audioViewHolder.privacy_image, audioViewHolder.message_container,
                    audioViewHolder.status, audioViewHolder.privacy_txt, audioViewHolder.date, audioViewHolder.pbar_loading, audioViewHolder.relative_main);


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
            if (!new File(mediaMessage.getMedia()).exists()) {
                String fileName = mediaMessage.getMedia().substring(mediaMessage.getMedia().lastIndexOf("/") + 1);
                File file = new File(folder, fileName);

                audioViewHolder.btn_play_pause.setVisibility(View.VISIBLE);
                audioViewHolder.loading.setVisibility(View.GONE);
                if (file.exists()) {
                    mediaMessage.setMedia(file.getPath());
//                    mediaMessage.setLoaded(true);
//                    mediaMessage.setLoading(false);

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

                    audioViewHolder.btn_play_pause.setVisibility(View.GONE);
                    audioViewHolder.loading.setVisibility(View.VISIBLE);
                    internetFilesOperations.downloadUrlWithProgress(audioViewHolder.loading, mediaMessage.getType(), mediaMessage.getMedia(), new DownloadListener() {
                        @Override
                        public void onDownloadFinish(final String pathOFDownloadedFile) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mediaMessage.setMedia(pathOFDownloadedFile);
//                                    mediaMessage.setLoaded(true);

                                    try {
                                        audioViewHolder.mp.reset();
                                        audioViewHolder.mp.setDataSource(mediaMessage.getMedia());
                                        audioViewHolder.mp.prepare();
                                        long currentDuration = audioViewHolder.mp.getCurrentPosition();
                                        audioViewHolder.tv_music_duration.setText("" + audioViewHolder.utils.milliSecondsToTimer(currentDuration));
                                        audioViewHolder.seek_bar_music.setProgress(0);
                                        audioViewHolder.seek_bar_music.setMax(100);
                                        Uri uri = Uri.fromFile(new File(mediaMessage.getMedia()));
                                        mediaMessage.setMedia(uri.toString());
                                        audioViewHolder.btn_play_pause.setVisibility(View.VISIBLE);
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
                    audioViewHolder.mp.setDataSource(mediaMessage.getMedia());
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


            audioViewHolder.relative_main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!selected) {
                        changeToolbar(true);
                    }
                    if (!list.contains(mediaMessage.getDocumentId())) {
                        selectItem(audioViewHolder.select_item, mediaMessage, position);
                        selectedPositions.add(position);
                    } else {
                        unselectItem(audioViewHolder.select_item, mediaMessage, position);
                        selectedPositions.remove(position);
                    }

                    return true;
                }
            });
            audioViewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected)
                        if (!list.contains(mediaMessage.getDocumentId())) {
                            selectItem(audioViewHolder.select_item, mediaMessage, position);
                            selectedPositions.add(position);
                        } else {
                            unselectItem(audioViewHolder.select_item, mediaMessage, position);
                            selectedPositions.remove(position);
                        }
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setLocationMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {

            final Document locationMessage = documents.get(position);
            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            imageViewHolder.play_video.setVisibility(View.GONE);
            showLayout_Privacy(locationMessage, position, imageViewHolder.privacy_image, imageViewHolder.messageContainer
                    , imageViewHolder.status, imageViewHolder.privacy_txt, imageViewHolder.date, imageViewHolder.pbar_loading, imageViewHolder.relative_main);

            imageViewHolder.image_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jsonObject = new JSONObject(locationMessage.getDocument());
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
            imageViewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected) {
                        if (!list.contains(locationMessage.getDocumentId())) {
                            selectItem(imageViewHolder.select_item, locationMessage, position);
                            selectedPositions.add(position);
                        } else {
                            unselectItem(imageViewHolder.select_item, locationMessage, position);
                            selectedPositions.remove(position);
                        }
                    }
                }
            });
            imageViewHolder.relative_main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick(locationMessage, imageViewHolder.select_item, position);
                    return true;
                }
            });
            imageViewHolder.image_message.setImageBitmap(null);
            JSONObject jsonObject = new JSONObject(locationMessage.getDocument());
            double lat = jsonObject.getDouble("lat");
            double lng = jsonObject.getDouble("long");
            String URL = "http://maps.google.com/maps/api/staticmap?center=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&zoom=15&size=200x200&sensor=false";
            ImageHelper.setImage(imageViewHolder.image_message, URL, imageViewHolder.progress_view_download);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.cant_find_location), Toast.LENGTH_SHORT).show();
        }

    }

    private void setVideoMessage(final ImageViewHolder imageViewHolder, final int position) {

        try {
            final Document message = documents.get(position);
            imageViewHolder.play_video.setVisibility(View.GONE);
            imageViewHolder.image_message.setImageBitmap(null);
            showLayout_Privacy(message, position, imageViewHolder.privacy_image, imageViewHolder.messageContainer
                    , imageViewHolder.status, imageViewHolder.privacy_txt, imageViewHolder.date, imageViewHolder.pbar_loading, imageViewHolder.relative_main);
            final String fileName = message.getMedia().substring(message.getMedia().lastIndexOf("/") + 1);
            final File file = new File(folder, fileName);
            imageViewHolder.progress_view_download.setVisibility(View.GONE);

            if (!new File(message.getMedia()).exists()) {
                if (file.exists()) {
                    message.setMedia(file.getPath());
                    Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(file.getPath(),
                            MediaStore.Video.Thumbnails.MICRO_KIND);

                    imageViewHolder.image_message.setImageBitmap(videoThumbnail);


                    playViedo(imageViewHolder.play_video, file.getPath());
                    imageViewHolder.play_video.setVisibility(View.VISIBLE);

                } else {
                    imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
                    internetFilesOperations.downloadUrlWithProgress(imageViewHolder.progress_view_download, message.getType(), message.getMedia(), new DownloadListener() {
                        @Override
                        public void onDownloadFinish(final String pathOFDownloadedFile) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    message.setMedia(pathOFDownloadedFile);
                                    imageViewHolder.image_message.setImageBitmap(ThumbnailUtils.createVideoThumbnail(message.getMedia(),
                                            MediaStore.Video.Thumbnails.MICRO_KIND));

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });
                                    Uri uri = Uri.fromFile(new File(message.getMedia()));
                                    message.setMedia(uri.toString());

                                    playViedo(imageViewHolder.messageContainer, message.getMedia());
                                    imageViewHolder.play_video.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
            }else{
                message.setMedia(file.getPath());
                Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(file.getPath(),
                        MediaStore.Video.Thumbnails.MICRO_KIND);
                imageViewHolder.image_message.setImageBitmap(videoThumbnail);
                playViedo(imageViewHolder.play_video, file.getPath());
                imageViewHolder.play_video.setVisibility(View.VISIBLE);
            }
            imageViewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected) {
                        if (!list.contains(message.getDocumentId())) {
                            selectItem(imageViewHolder.select_item, message, position);
                            selectedPositions.add(position);
                        } else {
                            unselectItem(imageViewHolder.select_item, message, position);
                            selectedPositions.remove(position);
                        }
                    }
                }
            });
            imageViewHolder.relative_main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClick(message, imageViewHolder.select_item, position);
                    return true;
                }
            });
        } catch (Exception e) {
            Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show();
            Log.e("Chat Adapter", "setVideoMessage: ", e);
            Crashlytics.logException(e);
        }
    }
                /*-------------------------- Additional important Methods------------*/

    public void selectItem(View select_item, Document document, int position) {
        select_item.setVisibility(View.VISIBLE);
        list.add(document.getDocumentId());
        selectedPositions.add(position);
    }

    public void changeToolbar(Boolean select) {
        selected = true;
        if (select) {
            forward.setVisibility(View.VISIBLE);
        } else {
            forward.setVisibility(View.INVISIBLE);
        }
    }

    private void unselectItem(View select_item, Document document, int position) {
        try {
            select_item.setVisibility(View.GONE);
            list.remove(list.indexOf(document.getDocumentId()));
            selectedPositions.remove(position);
            if (list.size() == 0) {
                changeToolbar(false);
                selected = false;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void longClick(Document textMessage, View select_item, int position) {
        if (!selected) {
            changeToolbar(true);
        }
        if (!list.contains(textMessage.getDocumentId()))
            selectItem(select_item, textMessage, position);
        else
            unselectItem(select_item, textMessage, position);
    }

    public void setImagePrivacy(int privacy, ImageView image, TextView txtPrivacy) {
        if (privacy == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red));
            }
            txtPrivacy.setText(R.string.privacy_private);
        } else if (privacy == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue));
            }
            txtPrivacy.setText(R.string.doctor);
        } else if (privacy == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green));
            }
            txtPrivacy.setText(R.string.privacy_public);
        }
    }

    private void changePrivacy(final ImageView imagePrivacy, final int pos, final TextView txtPrivacy, final ProgressBar progressBar) {
        View.OnClickListener clickPrivacy = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setMessage(R.string.change_privacy_msg);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, final int position) {
                                dialog.cancel();
                                progressBar.setVisibility(View.VISIBLE);
                                imagePrivacy.setVisibility(View.GONE);
                                txtPrivacy.setVisibility(View.GONE);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int newPrivacy;
                                        if(documents.get(pos).getPrivacy()==0)
                                            newPrivacy=1;
                                        else if(documents.get(pos).getPrivacy()==1)
                                            newPrivacy=2;
                                        else
                                            newPrivacy=0;
                                        boolean result = ApiHelper.postDocumentPrivacy(documents.get(pos).getDocumentId(), newPrivacy, activity);
                                        if (result) {
                                            if (documents.get(pos).getPrivacy() == 0)
                                                documents.get(pos).setPrivacy(1);

                                            else if (documents.get(pos).getPrivacy() == 1)
                                                documents.get(pos).setPrivacy(2);

                                            else
                                                documents.get(pos).setPrivacy(0);
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.GONE);
                                                    setImagePrivacy(documents.get(pos).getPrivacy(), imagePrivacy, txtPrivacy);
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        } else {
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(activity, activity.getResources().getString(R.string.privacy_not_change), Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    imagePrivacy.setVisibility(View.VISIBLE);
                                                    txtPrivacy.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }
                        });

                builder1.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        };
        txtPrivacy.setOnClickListener(clickPrivacy);
        imagePrivacy.setOnClickListener(clickPrivacy);

    }

    private void showLayout_Privacy(Document message, final int position, final ImageView imagePrivacy, final FrameLayout messageContainer, final ImageView status, final TextView txtPrivacy, TextView date, ProgressBar progressBar, final RelativeLayout relative_main) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(myDocument) {
            setImagePrivacy(message.getPrivacy(), imagePrivacy, txtPrivacy);
            changePrivacy(imagePrivacy, position, txtPrivacy, progressBar);
            imagePrivacy.setVisibility(View.VISIBLE);
            txtPrivacy.setVisibility(View.VISIBLE);

            params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            params.setMarginStart(200);
            messageContainer.setLayoutParams(params);
            messageContainer.setBackgroundResource(R.drawable.bubble_in_doc);
        }else {
            imagePrivacy.setVisibility(View.GONE);
            txtPrivacy.setVisibility(View.GONE);

            params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            params.setMarginEnd(200);
            messageContainer.setLayoutParams(params);
            messageContainer.setBackgroundResource(R.drawable.bubble_out_doc);
        }
        status.setVisibility(View.GONE);
        try {
            String[] split = message.getDateTime().split(" ")[1].split(":");
            date.setText(split[0] + ":" + split[1] + " ");
        } catch (Exception ex) {
            date.setText(message.getDateTime());
        }
    }

    /*private void showLocationOptions(String title, final int position, String lat, String lng) {

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

    }*/

    public void playViedo(View view, final String path) {
        try {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    File file = new File(path);
                    if (file.exists())
                        Util.getInstance(activity).showVideo(Uri.fromFile(file));
                    else {
                        String url = ApiHelper.SERVER_IMAGE_URL + path;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setDataAndType(Uri.parse(url), "video/*");
                        activity.startActivity(intent);
                    }
                }
            });
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.cant_play_video), Toast.LENGTH_SHORT).show();
        }

    }

    public void setList(List<Document> documents) {
        this.documents = documents;

    }

    public void clearSelected() {
        forward.setVisibility(View.INVISIBLE);
    }

    private void onclick(Document textMessage, View selected_item, int position) {
        if (selected) {
            if (!list.contains(textMessage.getDocumentId()))
                selectItem(selected_item, textMessage, position);
            else {
                unselectItem(selected_item, textMessage, position);
            }
        }
    }

    private void setTextSelector(final FrameLayout messageContainer, final View view) {
        messageContainer.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.height = messageContainer.getHeight();
                view.setLayoutParams(params);
            }
        });


    }
}

