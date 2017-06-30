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
    private boolean show_privacy = false;
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
                //AudioViewHolder audioViewHolder = (AudioViewHolder) baseViewHolder;
                //setAudioMessage(audioViewHolder, position);
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
        public TextView message, date;
        public ImageView status, privacy_image;
        public RelativeLayout background;
        public LinearLayout messageContainer;
        public ProgressBar progress_view_download;

        public ImageViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView.findViewById(R.id.message_container);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
            privacy_image = (ImageView) itemView.findViewById(R.id.privacy_image);

            image_message = (ImageView) itemView.findViewById(R.id.image_message);
            progress_view_download = (ProgressBar) itemView.findViewById(R.id.progress_view_download);


        }
    }

    // Audio
    public class AudioViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public ImageButton hisPlayPauseButton, playPauseButton;
        public ProgressBar myProgressBar, progressViewDownload;
        public RelativeLayout messageContainer;
        public ImageView imgMessageStatus;
        public TextView tvDate, tvDateMy, tvHisMusicCurrentLoc, tvMusicCurrentLoc, tvMusicDuration, tvHisMusicDuration;
        public SeekBar seekBarHisMusic, seekBarMusic;

        private MediaPlayer mp;
        // Handler to update UI timer, progress bar etc,.
        private Handler mHandler = new Handler();

        private MediaUtilities utils;


        public AudioViewHolder(View itemView) {
            super(itemView);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myProgressBar = (ProgressBar) itemView.findViewById(R.id.my_progress_view);
            progressViewDownload = (ProgressBar) itemView.findViewById(R.id.progress_view_download);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            imgMessageStatus = (ImageView) itemView.findViewById(R.id.my_message_status);

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
        public TextView message, date;
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
        }
    }

                /*--------------processing layout---------------*/
    //---------------------------------------------------------------------------

    private void setTextMessage(final TextMsgViewHolder textMsgViewHolder, final int position) {
        try {
            final Message textMessage = mMessages.get(position);
            if (show_privacy) {
                setImagePrivacy(textMessage.getPrivacy(), textMsgViewHolder.privacy_image);
                changePrivacy(textMsgViewHolder.privacy_image, position);
                textMsgViewHolder.privacy_image.setVisibility(View.VISIBLE);
            } else {
                textMsgViewHolder.privacy_image.setVisibility(View.GONE);
            }
            if (mMessages.get(position).getFrom_id() == userID) {
                textMsgViewHolder.background.setGravity(Gravity.RIGHT);
                textMsgViewHolder.messageContainer.setBackgroundResource(R.drawable.bubble_in);
                textMsgViewHolder.status.setVisibility(View.VISIBLE);

                if (mMessages.get(position).getSeen() == 1) {
                    textMsgViewHolder.status.setImageResource(R.drawable.read);
                } else if (mMessages.get(position).getIs_delivered() == 1) {
                    textMsgViewHolder.status.setImageResource(R.drawable.receive);
                } else if (mMessages.get(position).getIs_forward() == 1) {
                    textMsgViewHolder.status.setImageResource(R.drawable.sent);
                } else
                    textMsgViewHolder.status.setImageResource(R.drawable.pending);
            } else {
                textMsgViewHolder.background.setGravity(Gravity.LEFT);
                textMsgViewHolder.messageContainer.setBackgroundResource(R.drawable.bubble_out);
                textMsgViewHolder.status.setVisibility(View.GONE);

            }
            textMsgViewHolder.message.setText(textMessage.getMsg());

            try {
                String[] split = textMessage.getSent_at().split(" ")[1].split(":");
                textMsgViewHolder.date.setText(split[0] + " " + split[1]);
            } catch (Exception ex) {
                textMsgViewHolder.date.setText(textMessage.getSent_at());
            }


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

            try {
                String[] split = message.getSent_at().split(" ")[1].split(":");
                imageViewHolder.date.setText(split[0] + " " + split[1]);
            } catch (Exception ex) {
                imageViewHolder.date.setText(message.getSent_at());
            }
            imageViewHolder.progress_view_download.setVisibility(View.VISIBLE);
            Uri imageUri = Uri.fromFile(new File(message.getMsg()));
            Glide.with(activity).load(imageUri).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
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



                /*-------------------------- Additional important Methods------------*/

    public void selectItem(RelativeLayout messageContainer, Message message) {
        messageContainer.setBackgroundResource(R.color.gray_black);
        list.add(message.get_Id());
        Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
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

    public void setImagePrivacy(int privacy, ImageView image) {
        if (privacy == 0) {
//            image.setBackgroundResource(R.drawable.red);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.red));
            }

        }
        if (privacy == 1) {
//            image.setBackgroundResource(R.drawable.blue);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.blue));
            }

        }
        if (privacy == 2) {
//            image.setBackgroundResource(R.drawable.green);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green, activity.getTheme()));
            } else {
                image.setImageDrawable(activity.getResources().getDrawable(R.drawable.green));
            }

        }
    }

    private void changePrivacy(ImageView imagePrivacy, final int pos) {
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

}

