package com.germanitlab.kanonhealth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.callback.DownloadListener;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.models.messages.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by Geram IT Lab on 08/05/2017.
 */

public class DoctorDocumentAdapter extends  RecyclerView.Adapter<DoctorDocumentAdapter.BaseViewHolder> implements View.OnClickListener, View.OnLongClickListener  {

    private List<Message> mMessages;
    private Activity activity;
    private InternetFilesOperations internetFilesOperations;

    public DoctorDocumentAdapter(List<Message> messages, Activity activity) {
        this.activity = activity;
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
        public TextView  date, privacy_txt;
        public ImageView status, privacy_image;
        public FrameLayout messageContainer;
        public ProgressBar progress_view_download, pbar_loading;

        public ImageViewHolder(View itemView) {
            super(itemView);
            messageContainer = (FrameLayout) itemView.findViewById(R.id.message_container);
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
        public FrameLayout message_container;
        public ImageView btn_play_pause;
        public SeekBar seek_bar_music;
        public ProgressBar loading;
        public TextView tv_music_duration, date, privacy_txt;
        public ImageView privacy_image, status;
        public ProgressBar pbar_loading;
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
            loading =(ProgressBar)itemView.findViewById(R.id.loading);

        }
    }

    // Text
    public static class TextMsgViewHolder extends BaseViewHolder {
        public FrameLayout messageContainer;
        public TextView message, date, privacy_txt;
        public ImageView status, privacy_image;
        public ProgressBar pbar_loading;


        public TextMsgViewHolder(View itemView) {
            super(itemView);
            messageContainer = (FrameLayout) itemView.findViewById(R.id.message_container);
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
            textMsgViewHolder.message.setText(textMessage.getMsg());
            setTime(textMessage.getSent_at(),textMsgViewHolder.date);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setImageMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {

            final Message message = mMessages.get(position);
            setTime(message.getSent_at(),imageViewHolder.date);

            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            ImageHelper.setImage(imageViewHolder.image_message, ApiHelper.SERVER_IMAGE_URL + message.getMsg(), imageViewHolder.progress_view_download);
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
                    ImageHelper.setImage(img, ApiHelper.SERVER_IMAGE_URL + message.getMsg(), pbar);
                    dialog.show();
                }
            });
            imageViewHolder.status.setVisibility(View.GONE);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ", e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }


    private void setAudioMessage(final AudioViewHolder audioViewHolder, final int position) {
        try {
            final Message mediaMessage = mMessages.get(position);
            audioViewHolder.status.setVisibility(View.GONE);
            setTime(mediaMessage.getSent_at(),audioViewHolder.date);

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
                File file = new File(Constants.folder, fileName);


                audioViewHolder.btn_play_pause.setVisibility(View.VISIBLE);
                audioViewHolder.loading.setVisibility(View.GONE);
                if (file.exists()) {
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
                    audioViewHolder.btn_play_pause.setVisibility(View.GONE);
                    audioViewHolder.loading.setVisibility(View.VISIBLE);
                    internetFilesOperations.downloadUrlWithProgress(audioViewHolder.loading, mediaMessage.getType(), mediaMessage.getMsg(), new DownloadListener() {
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
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setLocationMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {

            final Message locationMessage = mMessages.get(position);
            imageViewHolder.status.setVisibility(View.GONE);
            setTime(locationMessage.getSent_at(),imageViewHolder.date);
            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
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

            JSONObject jsonObject = new JSONObject(locationMessage.getMsg());
            double lat = jsonObject.getDouble("lat");
            double lng = jsonObject.getDouble("long");
            String URL = "http://maps.google.com/maps/api/staticmap?center=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&zoom=15&size=200x200&sensor=false";
            ImageHelper.setImage(imageViewHolder.image_message, URL, imageViewHolder.progress_view_download);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(activity, activity.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void setVideoMessage(final ImageViewHolder imageViewHolder, final int position){

        try {
            final Message message = mMessages.get(position);
            imageViewHolder.status.setVisibility(View.GONE);
            setTime(message.getSent_at(),imageViewHolder.date);
            if (!new File(message.getMsg()).exists()) {
                final String fileName = message.getMsg().substring(message.getMsg().lastIndexOf("/") + 1);
                final File file = new File(Constants.folder, fileName);

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

    public void setList(List<Message> messages ){
        this.mMessages=messages;

    }

    public void setItem(Message item) {
        this.mMessages.add(item);
    }

    private void setTime(String time ,TextView txtdate){
        try {
            String[] split = time.split(" ")[1].split(":");
            txtdate.setText(split[0] + ":" + split[1] + " ");
        } catch (Exception ex) {
            txtdate.setText(time);
        }
    }
}

