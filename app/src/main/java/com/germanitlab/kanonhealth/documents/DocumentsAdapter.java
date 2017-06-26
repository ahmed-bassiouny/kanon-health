package com.germanitlab.kanonhealth.documents;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.UploadListener;
import com.germanitlab.kanonhealth.chat.MapsActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.InternetFilesOperations;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.emitter.Emitter;

import static com.germanitlab.kanonhealth.R.id.progress_view_download;
import static com.germanitlab.kanonhealth.helpers.Constants.folder;

/**
 * Created by Mo on 3/22/17.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.BaseViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;
    Activity context;
    private InternetFilesOperations internetFilesOperations;
    DocumentsChatFragment chatFragment;
    PrefManager prefManager;
    public boolean selected = false;
    List<Integer> list = new ArrayList<>();



    public DocumentsAdapter(List<Message> messages, Activity context, DocumentsChatFragment chatFragment) {
        mMessages = messages;
        this.context = context;
        this.chatFragment = chatFragment;
        internetFilesOperations = InternetFilesOperations.getInstance(context.getApplicationContext());
        prefManager = new PrefManager(context);
    }

    public void setList(List<Message> mMessages) {
        this.mMessages = mMessages;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int type) {

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (type) {
            case Constants.IMAGE_MESSAGE:
                ViewGroup imageMessage = (ViewGroup) mInflater.inflate(R.layout.documents_image_message_cell, parent, false);
                ImageViewHolder imageMessageViewHolder = new ImageViewHolder(imageMessage);
                return imageMessageViewHolder;
            case Constants.AUDIO_MESSAGE:
                ViewGroup audioMessage = (ViewGroup) mInflater.inflate(R.layout.documents_audio_message_cell, parent, false);
                AudioViewHolder audioMessageViewHolder = new AudioViewHolder(audioMessage);
                return audioMessageViewHolder;
            case Constants.VIDEO_MESSAGE:
                ViewGroup videoMessage = (ViewGroup) mInflater.inflate(R.layout.documents_video_message_cell, parent, false);
                VideoViewHolder videoMessageViewHolder = new VideoViewHolder(videoMessage);
                return videoMessageViewHolder;
            case Constants.LOCATION_MESSAGE:
                ViewGroup locationMessage = (ViewGroup) mInflater.inflate(R.layout.documents_location_message_cell, parent, false);
                LocationViewHolder locationViewHolder = new LocationViewHolder(locationMessage);
                return locationViewHolder;
            default://for text message
                ViewGroup chatTextMessage = (ViewGroup) mInflater.inflate(R.layout.documents_text_message_cell, parent, false);
                TextMsgViewHolder chatTextMessageViewHolder = new TextMsgViewHolder(chatTextMessage);
                return chatTextMessageViewHolder;
        }
    }

    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        String type = mMessages.get(position).getType();
        if (type.equals(Constants.IMAGE)) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) baseViewHolder;
            setImageMessage(imageViewHolder, position);
        } else if (type.equals(Constants.VIDEO)) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) baseViewHolder;
            setVideoMessage(videoViewHolder, position);
        } else if (type.equals(Constants.AUDIO)) {
            AudioViewHolder audioViewHolder = (AudioViewHolder) baseViewHolder;
            setAudioMessage(audioViewHolder, position);
        } else if (type.equals(Constants.LOCATION)) {
            LocationViewHolder locationViewHolder = (LocationViewHolder) baseViewHolder;
            setLocationMessage(locationViewHolder, position);
        } else {
            TextMsgViewHolder textMsgViewHolder = (TextMsgViewHolder) baseViewHolder;
            setTextMessage(textMsgViewHolder, position);
        }
/*        switch (type) {
            case Constants.IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) baseViewHolder;
                setImageMessage(imageViewHolder, position);
                break;
            case Constants.VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) baseViewHolder;
                setVideoMessage(videoViewHolder, position);
                break;
            case Constants.AUDIO:
                AudioViewHolder audioViewHolder = (AudioViewHolder) baseViewHolder;
                setAudioMessage(audioViewHolder, position);
                break;
            case Constants.LOCATION:
                LocationViewHolder locationViewHolder = (LocationViewHolder) baseViewHolder;
                setLocationMessage(locationViewHolder, position);
                break;
            default://for text message
                TextMsgViewHolder textMsgViewHolder = (TextMsgViewHolder) baseViewHolder;
                setTextMessage(textMsgViewHolder, position);
        }*/
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

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

        return -1;
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

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }

//=============================================================

    private void setTextMessage(final TextMsgViewHolder textMsgViewHolder, final int position) {
        try {
            final Message textMessage = mMessages.get(position);
            textMsgViewHolder.myMessage.setText(textMessage.getMsg());
            setImagePrivacy(textMessage.getPrivacy() , textMsgViewHolder.privacyImage);


            try {
                String[] split = textMessage.getSent_at().split(" ");
                textMsgViewHolder.tvDateMy.setText(split[0] + " " + split[1] + " " + split[2] + " " + split[3] + " " + split[4]);
            } catch (Exception ex) {
                textMsgViewHolder.tvDateMy.setText(textMessage.getSent_at());
            }
//            textMsgViewHolder.background.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (!selected) {
//                        changeToolbar(true);
//                    }
//                    if (!list.contains(textMessage.get_Id()))
//                        selectItem(textMsgViewHolder.background, textMessage);
//                    else {
//                        unselectItem(textMsgViewHolder.background, textMessage);
//                    }
//                    return true;
//                }
//            });
            textMsgViewHolder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected) {
                        if (!list.contains(textMessage.get_Id()))
                            selectItem(textMsgViewHolder.background, textMessage);
                        else {
                            unselectItem(textMsgViewHolder.background, textMessage);
                        }
                    }
                }
            });
            textMsgViewHolder.privacyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage(R.string.change_privacy_msg);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ja",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    final int privacy ;
                                    if(mMessages.get(position).getPrivacy() == 0)
                                        privacy = 1 ;
                                    else if(mMessages.get(position).getPrivacy() == 1)
                                        privacy = 2 ;
                                    else
                                        privacy = 0 ;
                                    new HttpCall(context ,new ApiResponse() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            mMessages.get(position).setPrivacy(privacy);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            Toast.makeText(context, context.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();

                                        }
                                    }).updatePrivacy(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD) , mMessages.get(position).get_Id() , privacy);
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
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }




    }

    //-----------------------------------------------------------------------------------------------------


    private void setImageMessage(final ImageViewHolder imageViewHolder, final int position) {
        try {
            Log.d("date form Image ", mMessages.get(position).getSent_at().toString());

            final Message message = mMessages.get(position);
            imageViewHolder.tvDateMy.setText(message.getSent_at());

            setImagePrivacy(message.getPrivacy() , imageViewHolder.privacy_image);

            if (!new File(message.getMsg()).exists()) {
                String fileName = message.getMsg().substring(message.getMsg().lastIndexOf("/") + 1);
                File file = new File(folder, fileName);
                if (file.exists()) {
                    message.setMsg(file.getPath());
                    message.setLoaded(true);
                    message.setLoading(false);
                }
            }

            Uri imageUri = Uri.fromFile(new File(message.getMsg()));
            Glide.with(context).load(imageUri).into(imageViewHolder.myMessage);


            if (!message.isLoaded() && !message.isLoading()) {

                message.setLoading(true);

                imageViewHolder.progressBar.setVisibility(View.VISIBLE);
                internetFilesOperations.uploadFileWithProgress(imageViewHolder.progressBar, Constants.UPLOAD_URL, message.getMsg(), new UploadListener() {
                    @Override
                    public void onUploadFinish(final String result) {
                        ((Activity) context).runOnUiThread(new Runnable() {
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
                imageViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }

/*        imageViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, Document_Update.class);
                intent.putExtra("data" ,  message);
                intent.putExtra("type", Constants.IMAGE);
                context.startActivity(intent);
                return true;
            }
        });*/
            imageViewHolder.myMessageContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(mMessages.get(position).getMsg());

                    if (file.exists())
                        Util.getInstance(context).showPhoto(Uri.fromFile(file));
                }
            });
            imageViewHolder.myMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected) {
                        if (!list.contains(message.get_Id()))
                            selectItem(imageViewHolder.messageContainer, message);
                        else
                            unselectItem(imageViewHolder.messageContainer, message);
                    }  else {

                        File file = new File(mMessages.get(position).getMsg());

                        if (file.exists())
                            Util.getInstance(context).showPhoto(Uri.fromFile(file));
                    }
                }
            });
            imageViewHolder.myMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!selected) {
                        changeToolbar(true);
                    }
                    if (!list.contains(message.get_Id()))
                        selectItem(imageViewHolder.messageContainer, message);
                    else {
                        unselectItem(imageViewHolder.messageContainer, message);
                    }
                    return true;
                }
            });

            imageViewHolder.privacy_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage(R.string.change_privacy_msg);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ja",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    final int privacy ;
                                    if(mMessages.get(position).getPrivacy() == 0)
                                        privacy = 1 ;
                                    else if(mMessages.get(position).getPrivacy() == 1)
                                        privacy = 2 ;
                                    else
                                        privacy = 0 ;
                                    new HttpCall(context ,new ApiResponse() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            mMessages.get(position).setPrivacy(privacy);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            Toast.makeText(context, context.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                                        }
                                    }).updatePrivacy(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), mMessages.get(position).get_Id() , privacy);
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
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }


    private void setVideoMessage(final VideoViewHolder videoViewHolder, final int position) {
        try {

            final Message mediaMessage = mMessages.get(position);

            videoViewHolder.tvDateMy.setText(mediaMessage.getSent_at());

            if (!new File(mediaMessage.getMsg()).exists()) {
                String fileName = mediaMessage.getMsg().substring(mediaMessage.getMsg().lastIndexOf("/") + 1);
                File file = new File(folder, fileName);
                if (file.exists()) {
                    mediaMessage.setMsg(file.getPath());
                    mediaMessage.setLoaded(true);
                    mediaMessage.setLoading(false);
                    setVideoOnClick(videoViewHolder.myFrameVideo, file.getPath(), mediaMessage);
                }
            }
            setImagePrivacy(mediaMessage.getPrivacy() , videoViewHolder.privacyImage);

            setVideoOnClick(videoViewHolder.myFrameVideo, mediaMessage.getMsg(), mediaMessage);
            videoViewHolder.privacyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage(R.string.change_privacy_msg);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ja",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    final int privacy ;
                                    if(mMessages.get(position).getPrivacy() == 0)
                                        privacy = 1 ;
                                    else if(mMessages.get(position).getPrivacy() == 1)
                                        privacy = 2 ;
                                    else
                                        privacy = 0 ;
                                    new HttpCall(context ,new ApiResponse() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            mMessages.get(position).setPrivacy(privacy);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            Toast.makeText(context, context.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();

                                        }
                                    }).updatePrivacy(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD) , mMessages.get(position).get_Id() , privacy);
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


            videoViewHolder.myMessage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(mediaMessage.getMsg(),
                    MediaStore.Video.Thumbnails.MICRO_KIND));


            if (!mediaMessage.isLoaded() && !mediaMessage.isLoading()) {
                mediaMessage.isLoading();
                videoViewHolder.progressBar.setVisibility(View.VISIBLE);
                internetFilesOperations.uploadFileWithProgress(videoViewHolder.progressBar, Constants.UPLOAD_URL, mediaMessage.getMsg(), new UploadListener() {
                    @Override
                    public void onUploadFinish(final String result) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    if(result!=null) {
                                        JSONObject jsonObject = new JSONObject(result);
                                        if (jsonObject.has("error")) {
                                            //show dialog
                                            mediaMessage.setLoaded(true);
                                            showAlerDialog("Video", Html.fromHtml(jsonObject.getString("error")).toString());
                                        } else {

                                            if (!folder.exists()) folder.mkdirs();

                                            String source = mediaMessage.getMsg();
                                            File destination = new File(folder, jsonObject.getString("file_url").substring(jsonObject.getString("file_url").lastIndexOf("/") + 1));
                                            if (moveFile(new File(source), destination)) {
                                                mediaMessage.setMsg(destination.getPath());

                                                videoViewHolder.myMessage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(mediaMessage.getMsg(),
                                                        MediaStore.Video.Thumbnails.MICRO_KIND));
                                            }
                                            mediaMessage.setLoaded(true);
                                            mediaMessage.setLoading(false);

                                            setVideoOnClick(videoViewHolder.myFrameVideo, mediaMessage.getMsg(), mediaMessage);

                                            sendMessage(jsonObject.getString("file_url"), Constants.VIDEO);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } else {
                videoViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    public void setVideoOnClick(View view, final String path, final Message message) {
/*        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, Document_Update.class);
                intent.putExtra("data" , (Parcelable) message );
                intent.putExtra("type", Constants.VIDEO);
                context.startActivity(intent);
                return true ;
            }
        });*/
try {
    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            File file = new File(path);
            if (file.exists())
                Util.getInstance(context).showVideo(Uri.fromFile(file));
            else {
                String url = Constants.CHAT_SERVER_URL + "/" + path;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setDataAndType(Uri.parse(url), "video");
                context.startActivity(intent);
            }
        }
    });
}catch (Exception e){
    Crashlytics.logException(e);
    Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
}

    }

    //-----------------------------------------------------------------------------------------------------
    private void setAudioMessage(final AudioViewHolder audioViewHolder, final int position) {
        try {
            final Message mediaMessage = mMessages.get(position);

            audioViewHolder.tvDateMy.setText(mediaMessage.getSent_at());

            // Mediaplayer
            audioViewHolder.mp = new MediaPlayer();
            audioViewHolder.utils = new MediaUtilities();

            final Runnable mUpdateTimeTask = new Runnable() {
                public void run() {
                    long totalDuration = audioViewHolder.mp.getDuration();
                    long currentDuration = audioViewHolder.mp.getCurrentPosition();


                    // Updating progress bar
                    int progress = (int) (audioViewHolder.utils.getProgressPercentage(currentDuration, totalDuration));
                    //Log.d("Progress", ""+progress);
                    audioViewHolder.seekBarMusic.setProgress(progress);

                    // Running this thread after 100 milliseconds
                    audioViewHolder.mHandler.postDelayed(this, 30);
                }
            };

            setImagePrivacy(mediaMessage.getPrivacy() , audioViewHolder.privacy_image);

            // Listeners
            audioViewHolder.privacy_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage(R.string.change_privacy_msg);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ja",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    final int privacy ;
                                    if(mMessages.get(position).getPrivacy() == 0)
                                        privacy = 1 ;
                                    else if(mMessages.get(position).getPrivacy() == 1)
                                        privacy = 2 ;
                                    else
                                        privacy = 0 ;
                                    new HttpCall(context ,new ApiResponse() {
                                        @Override
                                        public void onSuccess(Object response) {
                                            mMessages.get(position).setPrivacy(privacy);
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailed(String error) {
                                            Toast.makeText(context, context.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
                                        }
                                    }).updatePrivacy(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD) , mMessages.get(position).get_Id() , privacy);
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
            audioViewHolder.seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    long currentDuration = audioViewHolder.mp.getCurrentPosition();
                    audioViewHolder.tvMusicDuration.setText("" + audioViewHolder.utils.milliSecondsToTimer(currentDuration));
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
                    audioViewHolder.tvMusicDuration.setText("" + audioViewHolder.utils.milliSecondsToTimer(totalDuration));

                    // forward or backward to certain seconds
                    audioViewHolder.mp.seekTo(currentPosition);

                    // update timer progress again
                    audioViewHolder.mHandler.postDelayed(mUpdateTimeTask, 100);

                }
            }); // Important
            audioViewHolder.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    audioViewHolder.playPauseButton.setImageResource(R.drawable.ic_music_play);
                }
            }); // Important

            audioViewHolder.playPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (audioViewHolder.mp.isPlaying()) {
                        if (audioViewHolder.mp != null) {
                            audioViewHolder.mp.pause();
                            // Changing button image to play button
                            audioViewHolder.playPauseButton.setImageResource(R.drawable.ic_music_play);
                        }
                    } else {
                        // Resume song
                        if (audioViewHolder.mp != null) {
                            audioViewHolder.mp.start();
                            // Changing button image to pause button
                            audioViewHolder.playPauseButton.setImageResource(R.drawable.ic_music_pause);
                        }
                    }
                }
            });
            if (!new File(mediaMessage.getMsg()).exists()) {
                String fileName = mediaMessage.getMsg().substring(mediaMessage.getMsg().lastIndexOf("/") + 1);
                File file = new File(folder, fileName);

                if (file.exists()) {

                    mediaMessage.setMsg(file.getPath());
                    mediaMessage.setLoaded(true);
                    mediaMessage.setLoading(false);

                    try {
                        audioViewHolder.mp.reset();
                        audioViewHolder.mp.setDataSource(file.getPath());
                        audioViewHolder.mp.prepare();

                        // set Progress bar values
                        audioViewHolder.seekBarMusic.setProgress(0);
                        audioViewHolder.seekBarMusic.setMax(100);

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
            } else {

                try {
                    audioViewHolder.mp.reset();
                    audioViewHolder.mp.setDataSource(mediaMessage.getMsg());
                    audioViewHolder.mp.prepare();

                    // set Progress bar values
                    audioViewHolder.seekBarMusic.setProgress(0);
                    audioViewHolder.seekBarMusic.setMax(100);

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

            if (!mediaMessage.isLoaded()) {
                audioViewHolder.myProgressBar.setVisibility(View.VISIBLE);
                internetFilesOperations.uploadFileWithProgress(audioViewHolder.myProgressBar, Constants.UPLOAD_URL, mediaMessage.getMsg(), new UploadListener() {
                    @Override
                    public void onUploadFinish(final String result) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.has("error")) {
                                        //show dialog
                                        mediaMessage.setLoaded(true);
                                        showAlerDialog("Audio", Html.fromHtml(jsonObject.getString("error")).toString());
                                    } else {

                                        if (!folder.exists()) folder.mkdirs();
                                        String source = mediaMessage.getMsg();
                                        File destination = new File(folder, jsonObject.getString("file_url").substring(jsonObject.getString("file_url").lastIndexOf("/") + 1));
                                        if (moveFile(new File(source), destination)) {
                                            mediaMessage.setMsg(destination.getPath());
                                        }
                                        mediaMessage.setLoaded(true);
                                        mediaMessage.setLoading(false);
                                        sendMessage(jsonObject.getString("file_url"), Constants.AUDIO);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } else {
                audioViewHolder.myProgressBar.setVisibility(View.INVISIBLE);
            }
            int totalDuration = audioViewHolder.mp.getDuration();
            audioViewHolder.tvMusicDuration.setText("" + audioViewHolder.utils.milliSecondsToTimer(totalDuration));

            audioViewHolder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(context, Document_Update.class);
                    intent.putExtra("data", (Parcelable) mediaMessage);
                    intent.putExtra("type", Constants.AUDIO);
                    context.startActivity(intent);
/*
                showMediaOptions("Audio", position, Constants.AUDIO_MESSAGE, mediaMessage.getMsg());
*/
                    return true;
                }
            });
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

/*
                    showMediaOptions("Image", position, Constants.IMAGE_MESSAGE, message.getMsg());
*/

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
            });

        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    //-----------------------------------------------------------------------------------------------------
    private void setLocationMessage(final LocationViewHolder locationViewHolder, final int position) {
        try {
            final Message locationMessage = mMessages.get(position);
            setImagePrivacy(locationMessage.getPrivacy() , locationViewHolder.privacyImage);

            if (locationMessage.isMine()) {

                locationViewHolder.tvDateMy.setText(locationMessage.getSent_at());
                locationViewHolder.myMessageContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = "{" + locationMessage.getMsg() + "}";
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            Intent intent = new Intent(context, MapsActivity.class);
                            intent.putExtra("lat", jsonObject.getString("lat"));
                            intent.putExtra("long", jsonObject.getString("long"));

                            Toast.makeText(context, jsonObject.getString("lat") + jsonObject.getString("long"), Toast.LENGTH_LONG).show();
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                locationViewHolder.myMessageContainer.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        String msg = "{" + locationMessage.getMsg() + "}";
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            showLocationOptions("Location", position, jsonObject.getString("lat"), jsonObject.getString("longi"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                });

                locationViewHolder.privacyImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage(R.string.change_privacy_msg);
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ja",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        final int privacy ;
                                        if(mMessages.get(position).getPrivacy() == 0)
                                            privacy = 1 ;
                                        else if(mMessages.get(position).getPrivacy() == 1)
                                            privacy = 2 ;
                                        else
                                            privacy = 0 ;
                                        new HttpCall(context,new ApiResponse() {
                                            @Override
                                            public void onSuccess(Object response) {
                                                mMessages.get(position).setPrivacy(privacy);
                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailed(String error) {
                                                Toast.makeText(context, context.getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();

                                            }
                                        }).updatePrivacy(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD) , mMessages.get(position).get_Id() , privacy);
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
                if (locationMessage.getLocationBitmap() == null && !locationMessage.isLoading()) {

                    locationMessage.setLoading(true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String msg = "{" + locationMessage.getMsg() + "}";
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(msg);
                                double lat = jsonObject.getDouble("lat");
                                double lng = jsonObject.getDouble("long");

                                locationMessage.setLocationBitmap(getGoogleMapThumbnail(lat, lng , context));
                                locationMessage.setLoading(false);
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        locationViewHolder.myProgressBar.setVisibility(View.INVISIBLE);
                                        locationViewHolder.myMessage.setImageBitmap(locationMessage.getLocationBitmap());
                                        locationMessage.setLoaded(true);

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }

                if (!locationMessage.isLoaded()) {
                    sendMessage(locationMessage.getMsg(), Constants.LOCATION);
                }

            } else {
                                //**bug number 136 in version 1.2.7 *//
                //***** NULL Pointer Exception hisMessageContainer  because not found in layout *//
                /*locationViewHolder.hisMessageContainer.setVisibility(View.VISIBLE);
                locationViewHolder.myMessageContainer.setVisibility(View.INVISIBLE);
                locationViewHolder.hisMessageContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = "{" + locationMessage.getMsg() + "}";
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            Intent intent = new Intent(context, MapsActivity.class);
                            intent.putExtra("lat", jsonObject.getString("lat"));
                            intent.putExtra("long", jsonObject.getString("long"));
                            Toast.makeText(context, jsonObject.getString("lat") + jsonObject.getString("long"), Toast.LENGTH_LONG).show();
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                locationViewHolder.hisMessageContainer.setOnLongClickListener(new View.OnLongClickListener() {
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

                locationViewHolder.tvDate.setText(locationMessage.getSent_at());


                if (locationMessage.getLocationBitmap() == null && !locationMessage.isLoading()) {

                    locationMessage.setLoading(true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String msg = "{" + locationMessage.getMsg() + "}";
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(msg);
                                double lat = jsonObject.getDouble("lat");
                                double lng = jsonObject.getDouble("long");
                                locationMessage.setLocationBitmap(getGoogleMapThumbnail(lat, lng , context));
                                locationMessage.setLoading(false);
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        locationViewHolder.hisProgressBar.setVisibility(View.INVISIBLE);
                                        locationViewHolder.hisMessage.setImageBitmap(locationMessage.getLocationBitmap());
                                        locationMessage.setLoaded(true);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    locationViewHolder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (!selected) {
                                changeToolbar(true);
                            }
                            if (!list.contains(locationMessage.get_Id()))
                                selectItem(locationViewHolder.messageContainer, locationMessage);
                            else {
                                unselectItem(locationViewHolder.messageContainer, locationMessage);
                            }

/*
                    showMediaOptions("Image", position, Constants.IMAGE_MESSAGE, message.getMsg());
*/

                            return true;
                        }
                    });
                    locationViewHolder.messageContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (selected)
                                if (!list.contains(locationMessage.get_Id()))
                                    selectItem(locationViewHolder.messageContainer, locationMessage);
                                else {
                                    unselectItem(locationViewHolder.messageContainer, locationMessage);
                                }
                            else {

                            }
                        }
                    });
                }

            }
            locationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void showAlerDialog(String title, String message) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
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


    private void sendMessage(String url, String type) {

        JSONObject sendText = new JSONObject();
        try {
            sendText.put("to_id", prefManager.getData(PrefManager.USER_ID));
            sendText.put("type", type);


            if (!type.equals(Constants.LOCATION))
                sendText.put("msg", url);
            else {

                String msg = "{" + url + "}";
                JSONObject msgJson = new JSONObject(msg);
                sendText.put("msg", msgJson);
            }
            if (!type.equals(Constants.LOCATION))
                sendText.put("is_url", 1);

            Log.d("Message ", sendText.toString());
            AppController.getInstance().getSocket().emit("ChatMessageSend", sendText);
            AppController.getInstance().getSocket().on("ChatMessageSendReturn", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    try {

                        try {
                            JSONObject jsonObject = new JSONObject(args[0].toString());
                            int poisition = jsonObject.getInt("position");
                            Message messageInPosition = mMessages.get(poisition);
                            messageInPosition.setStatus(Constants.SENT_STATUS);
                            messageInPosition.setId(jsonObject.getInt("id"));
                            Date parseDate = DateUtil.getFormat().parse(jsonObject.getString("sent_at"));
                            messageInPosition.setSent_at(DateUtil.formatDate(parseDate.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void showLocationOptions(String title, final int position, String lat, String lng) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle(title);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.listitem, R.id.textview);
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

    private void showMediaOptions(String title, final int position, final int type, final String path) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle(title);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.listitem, R.id.textview);
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
                    File file = new File(path);
                    boolean isExists = file.exists();
                    Log.e("file ", isExists + "");
                    Uri uri = Uri.fromFile(file);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("*/*");
                    if (type == Constants.AUDIO_MESSAGE)
                        share.setType("audio/*");
                    else if (type == Constants.IMAGE_MESSAGE)
                        share.setType("image/*");
                    else if (type == Constants.VIDEO_MESSAGE)
                        share.setType("video/*");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(share, "Share File"));
                } else if (which == 1) {
//                    startChatUsersActivity(position);
                } else if (which == 2) {
                    int withId = mMessages.get(position).get_Id();
                }
            }
        });

        try {
            builderSingle.show();
        } catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
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

        } catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    //===========================================================
    public static Bitmap getGoogleMapThumbnail(double lati, double longi , Context context) {
        String URL = "http://maps.google.com/maps/api/staticmap?center=" + lati + "," + longi + "&zoom=15&size=200x200&sensor=false";
        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in = null;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        return bmp;
    }

    //===============================================================

    public static class TextMsgViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public TextView myMessage, hisMessage, tvDate, tvDateMy;
        public ImageView imgMessageStatus , privacyImage;
        public ImageButton firstPrivacyIcon, secondPrivacyIcon, thirdPrivacyIcon;
        RelativeLayout background;


        public TextMsgViewHolder(View itemView) {
            super(itemView);
/*
            firstPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_first_privacy);
            secondPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_second_privacy);
            thirdPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_third_privacy);*/
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            privacyImage = (ImageView)itemView.findViewById(R.id.privacy_image);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myMessage = (TextView) itemView.findViewById(R.id.my_text_message);
            hisMessage = (TextView) itemView.findViewById(R.id.his_text_message);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            imgMessageStatus = (ImageView) itemView.findViewById(R.id.my_message_status);
        }
    }

    public static class ImageViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer ;
        public ImageView myMessage, hisMessage, privacy_image;
        public ProgressBar progressBar, progressViewDownload;
        public TextView tvDate, tvDateMy;
        public RelativeLayout messageContainer;

/*
        public ImageButton firstPrivacyIcon, secondPrivacyIcon, thirdPrivacyIcon;
*/


        public ImageViewHolder(View itemView) {
            super(itemView);
  /*          firstPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_first_privacy);
            secondPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_second_privacy);
            thirdPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_third_privacy);*/
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myMessage = (ImageView) itemView.findViewById(R.id.my_image_message);
            hisMessage = (ImageView) itemView.findViewById(R.id.his_image_message);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_view);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            progressViewDownload = (ProgressBar) itemView.findViewById(progress_view_download);
        }
    }


    public static class LocationViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public ImageView myMessage, hisMessage , privacyImage;
        public ProgressBar myProgressBar, hisProgressBar;
        public RelativeLayout messageContainer;
        public TextView tvDate, tvDateMy;
/*
        public ImageButton firstPrivacyIcon, secondPrivacyIcon, thirdPrivacyIcon;
*/


        public LocationViewHolder(View itemView) {
            super(itemView);
/*            firstPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_first_privacy);
            secondPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_second_privacy);
            thirdPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_third_privacy);*/
            privacyImage = (ImageView)itemView.findViewById(R.id.privacy_image);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myMessage = (ImageView) itemView.findViewById(R.id.my_image_message);
            hisMessage = (ImageView) itemView.findViewById(R.id.his_image_message);
            myProgressBar = (ProgressBar) itemView.findViewById(R.id.my_progress_view);
            hisProgressBar = (ProgressBar) itemView.findViewById(R.id.his_progress_view);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
        }
    }


    // --- audio
    public static class AudioViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public ImageButton hisPlayPauseButton, playPauseButton;
        public ProgressBar myProgressBar, progressViewDownload;
        public RelativeLayout messageContainer;
        public TextView tvDate, tvDateMy, tvHisMusicCurrentLoc, tvMusicCurrentLoc, tvMusicDuration, tvHisMusicDuration;
        public SeekBar seekBarHisMusic, seekBarMusic;
        public ImageView privacy_image;
/*
        public ImageButton firstPrivacyIcon, secondPrivacyIcon, thirdPrivacyIcon;
*/


        private MediaPlayer mp;
        // Handler to update UI timer, progress bar etc,.
        private Handler mHandler = new Handler();

        private MediaUtilities utils;


        public AudioViewHolder(View itemView) {
            super(itemView);
/*            firstPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_first_privacy);
            secondPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_second_privacy);
            thirdPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_third_privacy);*/
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myProgressBar = (ProgressBar) itemView.findViewById(R.id.my_progress_view);
            progressViewDownload = (ProgressBar) itemView.findViewById(R.id.progress_view_download);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container_audio);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);

            hisPlayPauseButton = (ImageButton) itemView.findViewById(R.id.btn_his_play_pause);
            playPauseButton = (ImageButton) itemView.findViewById(R.id.btn_play_pause);

            tvHisMusicCurrentLoc = (TextView) itemView.findViewById(R.id.tv_his_music_current_loc);
            tvMusicCurrentLoc = (TextView) itemView.findViewById(R.id.tv_music_current_loc);

            tvMusicDuration = (TextView) itemView.findViewById(R.id.tv_music_duration);
            tvHisMusicDuration = (TextView) itemView.findViewById(R.id.tv_his_music_duration);

            seekBarHisMusic = (SeekBar) itemView.findViewById(R.id.seek_bar_his_music);
            seekBarMusic = (SeekBar) itemView.findViewById(R.id.seek_bar_music);

        }
    }


    public static class VideoViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer;
        public ImageView myMessage, privacyImage;
        public FrameLayout myFrameVideo;
        public ProgressBar progressBar, progressViewDownload;
        public RelativeLayout messageContainer;
        public TextView tvDate, tvDateMy;
/*
        public ImageButton firstPrivacyIcon, secondPrivacyIcon, thirdPrivacyIcon;
*/

        public VideoViewHolder(View itemView) {
            super(itemView);
/*            firstPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_first_privacy);
            secondPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_second_privacy);
            thirdPrivacyIcon = (ImageButton) itemView.findViewById(R.id.icon_third_privacy);*/
privacyImage = (ImageView)itemView.findViewById(R.id.privacy_image);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            myMessage = (ImageView) itemView.findViewById(R.id.my_video_message);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_view);
            progressViewDownload = (ProgressBar) itemView.findViewById(progress_view_download);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            myFrameVideo = (FrameLayout) itemView.findViewById(R.id.my_frame_video);
        }
    }

    public void setImagePrivacy(int privacy , ImageView image)
    {
        if(privacy == 0)
        {
//            image.setBackgroundResource(R.drawable.red);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.red, context.getTheme()));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.red));
            }

        }
        if(privacy == 1)
        {
//            image.setBackgroundResource(R.drawable.blue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.blue, context.getTheme()));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.blue));
            }

        }
        if(privacy == 2)
        {
//            image.setBackgroundResource(R.drawable.green);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.green, context.getTheme()));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.green));
            }

        }
    }

    public void updatePrivacy(final int position){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(R.string.change_privacy_msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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
    public void changeToolbar(Boolean select) {
        try {
            selected = true;
            Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
            Toolbar toolbar1 = (Toolbar) context.findViewById(R.id.toolbar2);
            if (select) {
//                toolbar.setVisibility(View.GONE);
//                toolbar1.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.VISIBLE);
                toolbar1.setVisibility(View.GONE);
            }
        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(context, context.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    public void selectItem(RelativeLayout messageContainer, Message message) {
        messageContainer.setBackgroundResource(R.color.gray_black);
        list.add(message.get_Id());
    }

    private void unselectItem(RelativeLayout messageContainer, Message message) {
        messageContainer.setBackgroundResource(0);
        list.remove(list.indexOf(message.get_Id()));
        if (list.size() == 0) {
            changeToolbar(false);
            selected = false;
        }
    }




    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
