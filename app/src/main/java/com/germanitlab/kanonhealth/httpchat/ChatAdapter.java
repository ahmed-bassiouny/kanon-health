package com.germanitlab.kanonhealth.httpchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.chat.MessageAdapterClinic;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.MediaUtilities;
import com.germanitlab.kanonhealth.models.messages.Message;

import java.util.ArrayList;
import java.util.List;

import static com.germanitlab.kanonhealth.R.id.progress_view_download;

/**
 * Created by bassiouny on 28/06/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseViewHolder>{

    private List<Message> mMessages;
    private Context mcontext;
    private int userID;
    public ChatAdapter(List<Message> messages, Context context) {
        this.mMessages = messages;
        this.mcontext=context;
        userID=new PrefManager(context).getInt(PrefManager.USER_ID);
        userID=3;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        ViewGroup chatTextMessage = (ViewGroup) mInflater.inflate(R.layout.item_chat_text_message, parent, false);
        TextMsgViewHolder chatTextMessageViewHolder = new TextMsgViewHolder(chatTextMessage);
        return chatTextMessageViewHolder;
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
                //ImageViewHolder imageViewHolder = (ImageViewHolder) baseViewHolder;
                //setImageMessage(imageViewHolder, position,false);
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

        }catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(mcontext, mcontext.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


        return -1;
    }
    @Override
    public int getItemCount() {
        return mMessages.size();
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
    public  class ImageViewHolder extends BaseViewHolder {
        public LinearLayout myMessageContainer, hisMessageContainer;
        public ImageView myMessage, hisMessage;
        public ProgressBar progressBar, progressViewDownload;
        public RelativeLayout messageContainer;
        public TextView tvDate, tvDateMy,tvMyTextImg,tvHisTextImg;
        public ImageView imgMessageStatus;


        public ImageViewHolder(View itemView) {
            super(itemView);
            myMessageContainer = (LinearLayout) itemView.findViewById(R.id.my_message);
            hisMessageContainer = (LinearLayout) itemView.findViewById(R.id.his_message);
            myMessage = (ImageView) itemView.findViewById(R.id.my_image_message);
            hisMessage = (ImageView) itemView.findViewById(R.id.his_image_message);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_view);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvMyTextImg = (TextView) itemView.findViewById(R.id.tv_text_with_image_my);
            tvHisTextImg= (TextView) itemView.findViewById(R.id.tv_text_with_image_his);
            imgMessageStatus = (ImageView) itemView.findViewById(R.id.my_message_status);
            tvDateMy = (TextView) itemView.findViewById(R.id.tv_date_my);
            progressViewDownload = (ProgressBar) itemView.findViewById(progress_view_download);
            messageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    myMessageContainer.setBackgroundResource(R.color.gray_black);
                    return false;
                }
            });
        }
    }
    // Audio
    public  class AudioViewHolder extends BaseViewHolder {
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
            progressViewDownload = (ProgressBar) itemView.findViewById(progress_view_download);
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
    public  class VideoViewHolder extends BaseViewHolder {
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
            progressViewDownload = (ProgressBar) itemView.findViewById(progress_view_download);
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
        public ImageView status;
        RelativeLayout background;

        public TextMsgViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView.findViewById(R.id.background);
            messageContainer = (LinearLayout) itemView.findViewById(R.id.message_container);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (ImageView) itemView.findViewById(R.id.status);
        }
    }

    /*******processing layout*******/
    //---------------------------------------------------------------------------
    // finished edit
    private void setTextMessage(final TextMsgViewHolder textMsgViewHolder, final int position) {
        try {
             Message textMessage = mMessages.get(position);
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
            }else {
                textMsgViewHolder.background.setGravity(Gravity.LEFT);
                textMsgViewHolder.messageContainer.setBackgroundResource(R.drawable.bubble_out);
                textMsgViewHolder.status.setVisibility(View.GONE);

            }
                textMsgViewHolder.message.setText(textMessage.getMsg());

                try {
                    String[] split = textMessage.getSent_at().split(" ")[1].split(":");
                    textMsgViewHolder.date.setText(split[0] + " " + split[1] );
                } catch (Exception ex) {
                    textMsgViewHolder.date.setText(textMessage.getSent_at());
                }




            /*
            textMsgViewHolder.background.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!selected) {
                        changeToolbar(true);
                    }
                    if (!list.contains(textMessage.get_Id()))
                        selectItem(textMsgViewHolder.background, textMessage);
                    else {
                        unselectItem(textMsgViewHolder.background, textMessage);
                    }
                    return true;
                }
            });
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
            });*/

        }catch (Exception e) {
            //Crashlytics.logException(e);
            Log.e("Chat Adapter", "setTextMessage: ",e );
            Toast.makeText(mcontext, mcontext.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }
}
