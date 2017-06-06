package com.germanitlab.kanonhealth.chat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.germanitlab.kanonhealth.DoctorProfile;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.async.SocketCall;
import com.germanitlab.kanonhealth.db.DataManger;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.PopupHelper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.main.MainActivity;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.ormLite.MessageRepositry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;
import io.socket.emitter.Emitter;

/**
 * Created by Mo on 3/16/17.
 */

public class ChatActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.imgbtn_chat_attach)
    ImageButton imageButtonAttach;
    @BindView(R.id.img_chat_user_avatar)
    CircleImageView imageUser;
    @BindView(R.id.imgbtn_chat_autio_send)
    SeekBar imageButtonSend;
    @BindView(R.id.tv_chat_user_name)
    TextView tvUserName;
    @BindView(R.id.imgbtn_autio_send)
    ImageButton imgbtn_autio_send;
    @BindView(R.id.rv_chat_messages)
    RecyclerView recyclerView;
    @BindView(R.id.et_chat_message)
    EditText etMessage;
    @BindView(R.id.relative_record)
    RelativeLayout relativeAudio;
    @BindView(R.id.toolbar)
    public Toolbar ttoolbar;
    @BindView(R.id.linear_txt_msg)
    LinearLayout linearTextMsg;
    @BindView(R.id.timer)
    TextView tvRecordTimer;
    public static int user_id;
    ProgressDialog progressDialog;

    private List<Message> mMessages = new ArrayList<>();
    private MessageAdapterClinic mAdapter;

    public static User doctor;
    private String filePath;
    private MessageRepositry mMessageRepositry;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private SimpleLocation simpleLocation;
    private long startTimeForRecording = 0, endTimeForRecording = 0;
    public static boolean appStatus = false;

    PrefManager prefManager;

    private Uri selectedImageUri = null;
    private ContentValues contentValues;


    public static int indexFromIntent=0;

    @Inject
    DataManger mDataManger;
    private ChatComponent mChatComponent;
    private Uri selectedUri;
    private boolean selectetImage=false;

    public ChatComponent getmChatComponent() {
        if (mChatComponent == null) {
            mChatComponent = DaggerChatComponent.builder()
                    .appComponent(AppController.get(this).getAppComponent())
                    .chatModule(new ChatModule(this))
                    .build();
        }
        return mChatComponent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        /* data base
         */
        mMessageRepositry = new MessageRepositry(getApplicationContext());
        getmChatComponent().inject(this);


        Gson gson = new Gson();
        prefManager = new PrefManager(this);
        Log.e("Docot in chat data", prefManager.getData("doctor"));
        try {
            Intent intent = getIntent();
            String doctorJson = intent.getStringExtra("doctor_data");
            prefManager.put("doctor", doctorJson);
            Log.d("data from json ", prefManager.getData("doctor"));
            Boolean from = intent.getBooleanExtra("from", false);
            int from_notification = intent.getIntExtra("from_notification", 0);

            if (from_notification == 1) {
                int from_id = intent.getIntExtra("from_id", 0);
                showProgressDialog();
                new HttpCall(this, new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                        doctor = userInfoResponse.getUser();
                        dismissProgressDialog();
                        handleMyData();

                    }

                    @Override
                    public void onFailed(String error) {

                    }
                }).getDoctorId(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                        , AppController.getInstance().getClientInfo().getPassword(), String.valueOf(from_id));
            } else if (from) {
                UserInfoResponse userInfoResponse = gson.fromJson(doctorJson, UserInfoResponse.class);
                doctor = userInfoResponse.getUser();
                handleMyData();
            } else {
                UserInfoResponse userInfoResponse = gson.fromJson(intent.getStringExtra("doctor_data"), UserInfoResponse.class);
                doctor = userInfoResponse.getUser();
                handleMyData();
            }

        } catch (Exception e) {
            doctor = gson.fromJson(prefManager.getData("doctor"), User.class);
            handleMyData();
        }


    }

    private void initDialogImgTextSend(final Uri imgFilePath, String filePath) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_chat_img_text);
        dialog.setTitle("Image Description");

        // set the custom dialog components - text, image and button
        final EditText etText = (EditText) dialog.findViewById(R.id.text);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        Toast.makeText(this, "file:/"+filePath, Toast.LENGTH_SHORT).show();
        Uri imageUri = Uri.fromFile(new File(getPath(getApplicationContext(), selectedUri)));


        image.postInvalidate();

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ChatActivity.this.filePath = getPath(getApplicationContext(), selectedUri);
                sendMessage(ChatActivity.this.filePath, Constants.IMAGE,etText.getText().toString());

            }
        });

        dialog.show();
    }

    public void dismissProgressDialog() {

        progressDialog.dismiss();
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.waiting_text), true);
    }


    public void handleMyData() {
        user_id = doctor.get_Id();


        mAdapter = new MessageAdapterClinic(mMessages, this, doctor);

        if (Helper.isNetworkAvailable(this)) {
            fetchHistory();
        } else {
            List<Message> list = mMessageRepositry.getAll(user_id);
            insertMessages(list);
        }

        handelEvent();
        assignViews();

        buildGoogleApiClient();

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(mAdapter);
        AppController.getInstance().getSocket().on("ChatMessageReceive", handleIncomingMessages);
        AppController.getInstance().getSocket().on("IsSeen", isSeen);
        AppController.getInstance().getSocket().on("IsDeliver", isDeliver);
        sendIsSeen();

    }

    private Emitter.Listener isDeliver = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("Incoming Message SEEN", args[0].toString());
                    Log.d("doctor ID", String.valueOf(doctor.get_Id()));
                    try {
                        int id = data.optInt("id");
                        if (id == (Integer.parseInt(String.valueOf(doctor.get_Id())))) {

                            changeStatusToDeliver();
                            Log.d("my seen JSON", args[0].toString());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void changeStatusToDeliver() {
        for (Message message : mMessages) {

            if (message.isMine() && message.getStatus() == Constants.SENT_STATUS) {
                message.setStatus(Constants.DELIVER_STATUS);
            }
        }
        if (mAdapter != null) {
            mAdapter.setList(mMessages);

            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            scrollToBottom();

        }
    }


    @OnClick({R.id.tv_chat_user_name, R.id.img_chat_user_avatar})
    public void onDoctorNameClicked() {
        Intent intent = new Intent(this, DoctorProfile.class);
        Gson gson = new Gson();
        String doctor_data = gson.toJson(doctor);
        intent.putExtra("doctor_data", doctor_data);
        startActivity(intent);
    }


    private void sendMessage() {

        String message = etMessage.getText().toString().trim();

        if (message.length() == 0) return;

        final Message msg = new Message();
        msg.setMsg(message);
        msg.setMine(true);
        msg.setType(Constants.TEXT);
        msg.setTo_id(doctor.get_Id());
        msg.setFrom_id(AppController.getInstance().getClientInfo().getUser_id());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        final String str = sdf.format(new Date());
        msg.setSent_at(str);

        msg.setStatus(Constants.PENDING_STATUS);

        etMessage.setText("");
        final int position = addMessage(msg);
        JSONObject sendText = new JSONObject();
        try {
            Log.d("User Response", doctor.toString());
            sendText.put("to_id", doctor.get_Id());
            sendText.put("type", Constants.TEXT);
            sendText.put("msg", message);
            sendText.put("text_image", message);
            sendText.put("position", position);
            sendText.put("from_id", AppController.getInstance().getClientInfo().getUser_id());

            Log.d("Message ", sendText.toString());

            AppController.getInstance().getSocket().emit("ChatMessageSend", sendText);
            AppController.getInstance().getSocket().on("ChatMessageSendReturn", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    Log.e("Message Response", args[0].toString());
                    Uri imageUri = Uri.fromFile(new File(msg.getMsg()));
                    Log.e("my uri from here ", imageUri.toString());

                    try {
                        if (prefManager.getData(PrefManager.HISTORY) != "") {
                            String history = prefManager.getData(PrefManager.HISTORY);
                            history = history.substring(0, history.length() - 1);
                            history = history + " , " + args[0].toString() + "]";
                            prefManager.put(PrefManager.HISTORY, history);
                            Log.d("history after ", prefManager.getData(PrefManager.HISTORY));
                        } else {
                            prefManager.put(PrefManager.HISTORY, "[" + args[0] + "]");
                        }

                        JSONObject jsonObject = new JSONObject(args[0].toString());
                        Log.d("my type", jsonObject.getString("type"));
                        Log.d("my type", jsonObject.getString("type"));
                        if (jsonObject.getString("type").equals(Constants.IMAGE)) {
                            Log.d("I'm inside the image", jsonObject.getString("msg"));
                        }
                        int poisition = jsonObject.getInt("position");
                        Message messageInPosition = mMessages.get(poisition);
                        messageInPosition.setStatus(Constants.SENT_STATUS);
                        messageInPosition.setId(jsonObject.getInt("id"));
                        mMessageRepositry.create(messageInPosition);
                        Log.d("count " + mMessageRepositry.count(), "Count ");
                        try {
                            Date parseDate = DateUtil.getFormat().parse(jsonObject.getString("sent_at"));
                            messageInPosition.setSent_at(DateUtil.formatDate(parseDate.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(position);
                                recyclerView.setAdapter(mAdapter);
                                scrollToUp();
                                scrollToBottom();
                            }


                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (JSONException e) {

            Log.e("Ex", e.getLocalizedMessage());

        }

    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("Incoming Message", args[0].toString());

                    try {
                        if (String.valueOf(doctor.get_Id()).equals(String.valueOf(data.get("from_id")))) {
                            if (data.get("type").equals(Constants.LOCATION)) {
                                String msgLoc = data.getString("msg");
                                JSONObject jsonObject = new JSONObject(msgLoc);
                                double lat = jsonObject.getDouble("lat");
                                double lng = jsonObject.getDouble("long");
                                data.put("msg", "long:" + lng + ",lat:" + lat);

                            }
                            handelMessage(data.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handelMessage(data.toString());
                    }
                    JSONObject sendSeen = new JSONObject();
                    try {
                        sendSeen.put("id", doctor.get_Id());
                        sendSeen.put("is_seen", 1);
                        AppController.getInstance().getSocket().emit("IsDeliver", sendSeen);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void scrollToUp() {
        recyclerView.scrollToPosition(0);
    }

    private void handelMessage(String message) {
        Gson gson = new Gson();
        Message incomingMessage = gson.fromJson(message, Message.class);

        try {
            Date parseDate = DateUtil.getFormat().parse(incomingMessage.getSent_at());
            incomingMessage.setSent_at(DateUtil.formatDate(parseDate.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
/*        if (isBackground)
            showNotification(incomingMessage.getMsg(), 1, incomingMessage.getMsg(), incomingMessage.getFrom_id());*/
        addMessage(incomingMessage);
        incomingMessage.setMine(false);
        JSONObject sendSeen = new JSONObject();
        try {
            sendSeen.put("id", doctor.get_Id());
            sendSeen.put("is_seen", 1);
            AppController.getInstance().getSocket().emit("IsSeen", sendSeen);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Emitter.Listener isSeen = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    Log.d("Incoming Message SEEN", args[0].toString());
                    Log.d("doctor ID", String.valueOf(doctor.get_Id()));
                    try {
                        int id = data.optInt("id");
                        if (id == (doctor.get_Id())) {

                            changeStatus();
                            Log.d("my seen JSON", args[0].toString());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public void changeStatus() {
        for (Message message : mMessages) {

            if (message.isMine() && message.getStatus() == Constants.SENT_STATUS) {
                message.setStatus(Constants.SEEN_STATUS);
            }
        }
        if (mAdapter != null) {
            mAdapter.setList(mMessages);

            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
//            scrollToBottom();



        }
    }


    public int addMessage(Message message) {
        Log.d("100 MESS", message.toString());
        try {
            Date parseDate = DateUtil.getFormat().parse(message.getSent_at());
            message.setSent_at(DateUtil.formatDate(parseDate.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMessages.add(message);
        mAdapter = new MessageAdapterClinic(mMessages, this, doctor);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();

        return mMessages.size() - 1;

    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


    @Override
    public void onDestroy() {
        Log.e("Destroyed Called", "User destroyed");
        doctor = null;
        super.onDestroy();
    }

    private void assignViews() {

        tvUserName.setText(doctor.getName());
//            Log.e("returned image :", doctor.getAvatar());
//            Helper.setImage(this , Constants.CHAT_SERVER_URL
//                    + "/" + doctor.getAvatar() , imageUser , R.drawable.profile_place_holder);
//

    }


    //------  get chat history
    private void fetchHistory() {

        new SocketCall(this, new ApiResponse() {
            @Override
            public void onSuccess(Object response) {

                Log.e("fetch history", response.toString());

                handleJsonHistory(response.toString());

            }

            @Override
            public void onFailed(String error) {

                Log.e("fetch history error ", error + " +");
            }
        }).fetchMessage(String.valueOf(doctor.get_Id()), "1");
    }


    private void handleJsonHistory(String response) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Message>>() {
        }.getType();
        List<Message> historyMessage = gson.fromJson(response.toString(), listType);
        insertMessages(historyMessage);
    }

    private void insertMessages(List<Message> historyMessage) {
        for (Message message : historyMessage) {

            if (message.getType().equals(Constants.LOCATION)) {

                String msgLoc = message.getMsg();

                try {
                    JSONObject jsonObject = new JSONObject(msgLoc);
                    double lat = jsonObject.getDouble("lat");
                    double lng = jsonObject.getDouble("long");
                    message.setMsg("long:" + lng + ",lat:" + lat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
/*
                2017-03-15T09:14:35.000Z
*/
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = df.parse(message.getSent_at().toString());
                df.setTimeZone(TimeZone.getDefault());
                String formattedDate = df.format(date);
                Log.e("time in history", formattedDate);
                message.setSent_at(formatDate(formattedDate));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (message.getFrom_id() == AppController.getInstance().getClientInfo().getUser_id()) {
                message.setMine(true);
                message.setLoaded(true);
                if (message.getSeen() == 1) {
                    message.setStatus(Constants.SEEN_STATUS);
                } else message.setStatus(Constants.SENT_STATUS);
            }

        }
        {
            mMessages.addAll(0, historyMessage);

        }
        if (mAdapter != null) {
            mAdapter.setList(mMessages);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (int i=0;i<mMessages.size();i++){
                            if (mMessages.get(i).getSeen()==0) {
                                if(recyclerView!=null) {
                                    recyclerView.scrollToPosition(i);
                                    break;
                                }
                        }

                    }
                    mAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    private void handelEvent() {

        imageButtonSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_MOVE:

                            //handle drag action here   Milad :D
                            break;

                        case MotionEvent.ACTION_DOWN:
                            linearTextMsg.setVisibility(View.GONE);
                            relativeAudio.setVisibility(View.VISIBLE);
                            imageButtonSend.setBackgroundResource(0);
                            Log.e("Start Recording", "start");
                            startRecording();

                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e("stop Recording", "stop");
                            if (imageButtonSend.getProgress() < 30) {
                                Toast.makeText(getApplicationContext(), "You cancel record", Toast.LENGTH_SHORT).show();
                                stopRecording(true);
                                setButtonToTextMsg(true);
                            } else {
                                stopRecording(true);
                                long diff = endTimeForRecording - startTimeForRecording;
                                Log.e("Difference", String.valueOf(diff));
                                if (diff > 1000) {
                                    Log.e("No file capacity ", String.valueOf(mOutputFile.getUsableSpace()));
                                    sendMessage(mOutputFile.getPath(), Constants.AUDIO,"");
                                }
                                setButtonToTextMsg(true);
                            }
                            break;
                    }
                } else {
                    askForPermission(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.AUDIO_PERMISSION_CODE);
                }
                return false;
            }
        });


        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().trim().length() > 0) {

                    setButtonToTextMsg(false);
                    imgbtn_autio_send.setVisibility(View.VISIBLE);
                    imageButtonSend.setVisibility(View.GONE);

                } else {
                    imgbtn_autio_send.setVisibility(View.GONE);
                    imageButtonSend.setVisibility(View.VISIBLE);
                    imageButtonSend.setBackgroundResource(R.drawable.ic_mic_black_24dp);
                    imageButtonSend.setOnClickListener(null);
                    imageButtonSend.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_MOVE:

                                    //handle drag action here   Milad :D
                                    break;

                                case MotionEvent.ACTION_DOWN:
                                    linearTextMsg.setVisibility(View.GONE);
                                    relativeAudio.setVisibility(View.VISIBLE);
                                    imageButtonSend.setBackgroundResource(0);
                                    Log.e("Start Recording", "start");
                                    startRecording();

                                    break;
                                case MotionEvent.ACTION_UP:
                                    Log.e("stop Recording", "stop");
                                    if (imageButtonSend.getProgress() < 30) {
                                        Toast.makeText(getApplicationContext(), "You cancel record", Toast.LENGTH_SHORT).show();
                                        stopRecording(true);
                                        setButtonToTextMsg(true);
                                    } else {
                                        stopRecording(true);
                                        long diff = endTimeForRecording - startTimeForRecording;
                                        Log.e("Difference", String.valueOf(diff));
                                        if (diff > 1000) {
                                            Log.e("No file capacity ", String.valueOf(mOutputFile.getUsableSpace()));
                                            sendMessage(mOutputFile.getPath(), Constants.AUDIO,"");
                                        }
                                        setButtonToTextMsg(true);
                                    }
                                    break;
                            }
                            return false;
                        }
                    });

                }


            }
        });

        imageButtonAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopup(view);
            }
        });

    }

    private void setButtonToTextMsg(Boolean comingFromVoiceRecording) {

        if (!comingFromVoiceRecording) {
            imageButtonSend.setBackgroundResource(R.drawable.ic_send_black_24dp);
            imageButtonSend.setOnTouchListener(null);
        }

        linearTextMsg.setVisibility(View.VISIBLE);
        relativeAudio.setVisibility(View.GONE);
        imgbtn_autio_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == Constants.CAMERA_PERMISSION_CODE) {
                takePhoto();
            } else if (requestCode == Constants.VIDEO_PERMISSION_CODE) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, 200);
            } else if (requestCode == Constants.LAST_LOCATION_PERMISSION_CODE) {
//                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);
            } else if (requestCode == Constants.READ_EXTERNAL_STORARE_PERMISSION_CODE) {
                Intent intent = new Intent();
                intent.setType("image/* video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 100);

            } else if (requestCode == Constants.AUDIO_PERMISSION_CODE) {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, 300);
            } else if (requestCode == Constants.GET_LAST_LOCATION_PERMISSION_CODE) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                askForPermission(permission,
                        Constants.LAST_LOCATION_PERMISSION_CODE);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }
        } else mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void showPopup(View view) {
        final PopupWindow showPopup = PopupHelper
                .newBasicPopupWindow(this);
        LayoutInflater inflater = (LayoutInflater) ChatActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_navigation_menu, null);

        popupView.findViewById(R.id.img_view_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(ChatActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else
                        askForPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.CAMERA_PERMISSION_CODE);
                } else takePhoto();

                showPopup.dismiss();
            }
        });

        popupView.findViewById(R.id.img_view_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setType("image/* video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), 100);
                    } else
                        askForPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORARE_PERMISSION_CODE);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/* video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), 100);
                }
                showPopup.dismiss();
            }
        });
        popupView.findViewById(R.id.img_view_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, MediaRecorder.OutputFormat.MPEG_4);
                        startActivityForResult(intent, 200);

                    } else
                        askForPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.VIDEO_PERMISSION_CODE);
                } else {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, MediaRecorder.OutputFormat.MPEG_4);
                    startActivityForResult(intent, 200);
                }


                showPopup.dismiss();
            }
        });
        popupView.findViewById(R.id.img_view_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopup.dismiss();
                if (mLastLocation != null && mLastLocation.getLongitude() != 0) {
                    sendMessage("long:" + mLastLocation.getLongitude() + ",lat:" + mLastLocation.getLatitude(), Constants.LOCATION,"");
                } else {

                    if (simpleLocation == null || simpleLocation.getLatitude() == 0) {
                        Toast.makeText(getApplicationContext(), "Can't get your location", Toast.LENGTH_LONG).show();
                        mGoogleApiClient.reconnect();
                        return;
                    }
                    mLastLocation = new Location("");
                    mLastLocation.setLatitude(simpleLocation.getLatitude());
                    mLastLocation.setLongitude(simpleLocation.getLongitude());
                    sendMessage("long: " + mLastLocation.getLongitude() + ",lat:" + mLastLocation.getLatitude(), Constants.LOCATION,"");
                }
            }
        });

        showPopup.setContentView(popupView);

        showPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        showPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);

        Log.e("view ", showPopup.getHeight() + " ");

        showPopup.showAtLocation(view, Gravity.TOP, 0,
                400);


        showPopup.setOutsideTouchable(true);

        showPopup.setFocusable(true);
        // Removes default background.
        showPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void askForPermission(String[] permission, Integer requestCode) {
        ActivityCompat.requestPermissions(this, permission, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ChatFragment", "Result");
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case 100:
                        selectedUri = data.getData();
                        String mimeType = getMimeType(data.getData().getPath());

                        if (mimeType != null) {



                            if (mimeType.equalsIgnoreCase("image/jpg") || mimeType.equalsIgnoreCase("image/png") || mimeType.equalsIgnoreCase("image/jpeg") || mimeType.equalsIgnoreCase("image/GIF")) {
                                sendMessage(getPathFromURI(selectedUri), Constants.IMAGE,"");
                            } else {
                                sendMessage(getPath(this, selectedUri), Constants.VIDEO,"");
                            }

                        } else {
                            if (isImageFile(selectedUri)) {

                                filePath = getPath(this, selectedUri);

                                selectetImage=true;


                                    } else {
                                sendMessage(getPath(this, selectedUri), Constants.VIDEO,"");
                            }
                        }
                        break;
                    case 200://for video
                        Uri uriVideo = data.getData();
                        sendMessage(getPath(this, uriVideo), Constants.VIDEO,"");
                        break;
                    case 300:
                        Uri savedUri = data.getData();
                        sendMessage(getPath(this, savedUri), Constants.AUDIO,"");
                        break;
                }
            } else {
                if (requestCode == TAKE_PICTURE) {
                    try {
                        File finalFile = new File(getRealPathFromURI(selectedImageUri));
                        sendMessage(finalFile.getPath(), Constants.IMAGE,"");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String path;
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            path = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }
        return path;
    }


    private final static String FOLDER_NAME = "YourAppName/Image/";


    public void takePhoto() {

        contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        selectedImageUri = this.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }


    private static final int TAKE_PICTURE = 1;

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void sendMessage(String path, String type,String textWithImage) {
        Message message = new Message();
        message.setType(type);
        message.setMsg(path);
        message.setImageText(textWithImage);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String str = sdf.format(new Date());
        message.setSent_at(str);
        message.setMine(true);
        addMessage(message);
    }

    public String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public boolean isImageFile(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        return cR != null && cR.getType(uri).startsWith("image");
    }

    //===================================
    public static String getPath(final Context context, final Uri uri) {


        Log.e("uri", uri == null ? "null" : "not null");

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //==========================

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //-------------------------------------------------------------------------------
    //record audio
    private MediaRecorder mRecorder;
    private File mOutputFile;

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/RECORDING_"
                + dateFormat.format(new Date())
                + ".m4a");
    }

    private void startRecording() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startTimeForRecording = cal.getTimeInMillis();
        mRecorder = new MediaRecorder();
        tvRecordTimer.setText("0:00");
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(48000);
        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(64000);
        }
        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartTime = SystemClock.elapsedRealtime();
            mHandler.postDelayed(mTickExecutor, 100);
            Log.d("Voice Recorder", "started recording to " + mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Voice Recorder", "prepare() failed " + e.getMessage());
        }
    }

    private long mStartTime = 0;
    private int[] amplitudes = new int[100];
    private Handler mHandler = new Handler();
    private int i = 0;
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor, 100);
        }
    };


    private void tick() {
        long time = (mStartTime < 0) ? 0 : (SystemClock.elapsedRealtime() - mStartTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        if (seconds > 0)
            tvRecordTimer.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds));
        if (mRecorder != null) {
            amplitudes[i] = mRecorder.getMaxAmplitude();
            //Log.d("Voice Recorder","amplitude: "+(amplitudes[i] * 100 / 32767));
            if (i >= amplitudes.length - 1) {
                i = 0;
            } else {
                ++i;
            }
        }
    }

    protected void stopRecording(boolean saveFile) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        endTimeForRecording = cal.getTimeInMillis();

        try {
            mRecorder.stop();
        } catch (RuntimeException stopException) {
            //handle cleanup here
        }

        mRecorder.release();
        mRecorder = null;
        mStartTime = 0;
        mHandler.removeCallbacks(mTickExecutor);
        if (!saveFile && mOutputFile != null) {
            mOutputFile.delete();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String formatDate(String date) {
        String[] current_date = date.split("T")[1].split(":");
        return current_date[0] + ":" + current_date[1];

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
//        if (doctor.get_Id() == 1)
//            intent.putExtra("index", 3);
//        else
//            intent.putExtra("index", 2);
        //startActivity(intent);

                if (doctor.get_Id() == 1) {
                    indexFromIntent = 3;
                }
        else {
                    indexFromIntent = 2;
                }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sendIsSeen();
        } catch (Exception e) {

        }

        if(selectetImage){

//            Toast.makeText(this, ""+selectedUri, Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initDialogImgTextSend(selectedUri,filePath);

                }
            });

            selectetImage=false;
        }

//        new SocketCall(activity).chatWithStatus("OpenChatWith", doctor.getDoctor().get_Id());
//        askForPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.LAST_LOCATION_PERMISSION_CODE);
//        simpleLocation.beginUpdates();
    }

    public void sendIsSeen() {
        JSONObject sendSeen = new JSONObject();
        try {
            sendSeen.put("id", doctor.get_Id());
            sendSeen.put("is_seen", 1);
            AppController.getInstance().getSocket().emit("IsSeen", sendSeen);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        appStatus=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        appStatus=false;
    }
}
