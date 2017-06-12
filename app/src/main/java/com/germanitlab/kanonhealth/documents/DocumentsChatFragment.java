package com.germanitlab.kanonhealth.documents;

/**
 * Created by Mo on 3/19/17.
 */

// Edit by Ahmed 29-5-2017

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.SocketCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.DateUtil;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.PopupHelper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
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

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;
import io.socket.emitter.Emitter;


public class DocumentsChatFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ImageButton imgbtnAttach;
    private CircleImageView imageView;
    private ImageButton imgbtnSend;
    private TextView tvUserName, tvRecordTimer;
    private EditText etMessage;
    private RecyclerView recyclerView;
    private View view;

    private LinearLayout linearTextMsg;
    private RelativeLayout relativeAudio;

    private List<Message> mMessages = new ArrayList<Message>();
    private DocumentsAdapter mAdapter;


    private String filePath;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private SimpleLocation simpleLocation;
    private long startTimeForRecording = 0, endTimeForRecording = 0;


    PrefManager prefManager;

    private Uri selectedImageUri = null;
    private ContentValues contentValues;

    private int mUserId = AppController.getInstance().getClientInfo().getUser_id();

    private static DocumentsChatFragment documentsChatFragment;


    public static DocumentsChatFragment newInstance(){
        if(documentsChatFragment==null)
            documentsChatFragment=new DocumentsChatFragment();
        return documentsChatFragment;
    }
    public DocumentsChatFragment(){
        
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new DocumentsAdapter(mMessages, getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefManager = new PrefManager(getActivity());
        if (view != null) {

            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {

                parent.removeView(view);
            }

            return view;

        }

        view = inflater.inflate(R.layout.fragment_chats_fragments, container, false);
        setHasOptionsMenu(true);

        initView();

        if (Helper.isNetworkAvailable(getContext())) {

            fetchHistory();
        } else {
            handleJsonHistory(prefManager.getData(PrefManager.DOCUMENT_HISTORY), false);
        }

        handelEvent();
//        assignViews();

        buildGoogleApiClient();

        return view;
    }

    private void initView() {

//        imgbtnAttach = (ImageButton) view.findViewById(R.id.imgbtn_chat_attach);
        imgbtnSend = (ImageButton) view.findViewById(R.id.imgbtn_chat_autio_send);
        imageView = (CircleImageView) view.findViewById(R.id.img_chat_user_avatar);
//        tvUserName = (TextView) view.findViewById(R.id.tv_chat_user_name);
        etMessage = (EditText) view.findViewById(R.id.et_chat_message);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_chat_messages);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(mAdapter);

        linearTextMsg = (LinearLayout) view.findViewById(R.id.linear_txt_msg);
        relativeAudio = (RelativeLayout) view.findViewById(R.id.relative_record);

        tvRecordTimer = (TextView) view.findViewById(R.id.timer);
    }


    private void sendMessage() {

        String message = etMessage.getText().toString().trim();

        if (message.length() == 0) return;

        final Message msg = new Message();
        msg.setMsg(message);
        msg.setMine(true);
        msg.setType(Constants.TEXT);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        final String str = sdf.format(new Date());
        msg.setSent_at(str);


        msg.setStatus(Constants.PENDING_STATUS);

        etMessage.setText("");
        int position = addMessage(msg);
        JSONObject sendText = new JSONObject();
        try {
            sendText.put("to_id", mUserId);
            sendText.put("type", Constants.TEXT);
            sendText.put("msg", message);
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
                        Gson gson = new Gson();
//                        Message message = gson.fromJson(args[0].toString() , Message.class);
//                        caching(message, prefManager);
                        JSONObject jsonObject = new JSONObject(args[0].toString());
                        Log.d("my type", jsonObject.getString("type"));
                        Log.d("my type", jsonObject.getString("type"));
                        if (jsonObject.getString("type").equals(Constants.IMAGE))

                        {
                            Log.d("I'm inside the image", jsonObject.getString("msg"));

                        }
                        int poisition = jsonObject.getInt("position");
                        Message messageInPosition = mMessages.get(poisition);
                        messageInPosition.setStatus(Constants.SENT_STATUS);
                        messageInPosition.setId(jsonObject.getInt("id"));
                        try {
                            Date parseDate = DateUtil.getFormat().parse(jsonObject.getString("sent_at"));
                            messageInPosition.setSent_at(DateUtil.formatDate(parseDate.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
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

    public void changeStatus() {
        for (Message message : mMessages) {

            if (message.isMine()) {
                message.setStatus(Constants.SEEN_STATUS);
            }
        }
        if (mAdapter != null) {
            mAdapter.setList(mMessages);

            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            scrollToBottom();

        }
    }


    public int addMessage(Message message) {
        Log.d("100 MESS", message.toString());
        try {
//            Date parseDate = DateUtil.getFormat().parse(message.getSent_at());
//            message.setSent_at(DateUtil.formatDate(parseDate.getTime()));
            Log.d("sent at ", message.getSent_at().toString());
            message.setSent_at(formatDate(message.getSent_at().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMessages.add(message);
        mAdapter = new DocumentsAdapter(mMessages, getActivity(), this);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();

        return mMessages.size() - 1;

    }


    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_document, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //------  get chat history
    private void fetchHistory() {

        new SocketCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {

                Log.e("fetch history", response.toString());
                prefManager.put(PrefManager.DOCUMENT_HISTORY , response.toString());
                handleJsonHistory(response.toString(), true);


            }

            @Override
            public void onFailed(String error) {

                Log.e("fetch history error ", error + " +");
            }
        }).fetchMessage(String.valueOf(AppController.getInstance().getClientInfo().getUser_id()), "1");
    }


    private void handleJsonHistory(String response, Boolean from) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Message>>() {
        }.getType();
        List<Message> historyMessage = gson.fromJson(response.toString(), listType);
/*        if (from)
            prefManager.put("history", response.toString());*/
        Log.d("all history ", prefManager.getData("history"));
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
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
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

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    private void handelEvent() {

        imgbtnSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            linearTextMsg.setVisibility(View.GONE);
                            relativeAudio.setVisibility(View.VISIBLE);

                            Log.e("Start Recording", "start");
                            startRecording();

                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e("stop Recording", "stop");
                            stopRecording(true);
                            long diff = endTimeForRecording - startTimeForRecording;
                            Log.e("Difference", String.valueOf(diff));
                            if (diff > 1000) {
                                Log.e("No file capacity ", String.valueOf(mOutputFile.getUsableSpace()));
                                sendMessage(mOutputFile.getPath(), Constants.AUDIO);
                            }
                            setButtonToTextMsg(true);

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

                } else {

                    imgbtnSend.setImageResource(R.drawable.mic);
                    imgbtnSend.setOnClickListener(null);
                    imgbtnSend.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    linearTextMsg.setVisibility(View.GONE);
                                    relativeAudio.setVisibility(View.VISIBLE);

                                    Log.e("Start Recording", "start");

                                    startRecording();

                                    break;
                                case MotionEvent.ACTION_UP:

                                    Log.e("stop Recording", "stop");
                                    stopRecording(true);


                                    long diff = endTimeForRecording - startTimeForRecording;
                                    Log.e("Difference", String.valueOf(diff));
                                    if (diff > 1000) {

                                        Log.e("file capacity ", String.valueOf(mOutputFile.getUsableSpace()));
                                        sendMessage(mOutputFile.getPath(), Constants.AUDIO);
                                    }

                                    setButtonToTextMsg(true);
                                    imgbtnSend.setImageResource(R.drawable.mic);


                                    break;
                            }
                            return false;
                        }
                    });

                }


            }
        });

//        imgbtnAttach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showPopup(view);
//            }
//        });

    }

    private void setButtonToTextMsg(Boolean comingFromVoiceRecording) {

        if (!comingFromVoiceRecording) {
            imgbtnSend.setImageResource(android.R.drawable.ic_menu_send);
            imgbtnSend.setOnTouchListener(null);
        }

        linearTextMsg.setVisibility(View.VISIBLE);
        relativeAudio.setVisibility(View.GONE);
        imgbtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {

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
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {





    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void showPopup(View view) {
        final PopupWindow showPopup = PopupHelper
                .newBasicPopupWindow(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_navigation_menu, null);


        popupView.findViewById(R.id.img_view_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
                }


                showPopup.dismiss();
            }
        });
        popupView.findViewById(R.id.img_view_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        startActivityForResult(intent, 200);

                    } else
                        askForPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.VIDEO_PERMISSION_CODE);
                } else {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(intent, 200);
                }


                showPopup.dismiss();
            }
        });
        popupView.findViewById(R.id.img_view_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopup.dismiss();

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                        askForPermission(permission,
                                Constants.LAST_LOCATION_PERMISSION_CODE);
                    } else {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                    }
                } else mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

                if (mLastLocation != null && mLastLocation.getLongitude() != 0) {
                    sendMessage("long:" + mLastLocation.getLongitude() + ",lat:" + mLastLocation.getLatitude(), Constants.LOCATION);
                } else {

                    if (simpleLocation == null || simpleLocation.getLatitude() == 0) {
                        Toast.makeText(getActivity(), "Can't get your location", Toast.LENGTH_LONG).show();
                        mGoogleApiClient.reconnect();
                        return;
                    }
                    mLastLocation = new Location("");
                    mLastLocation.setLatitude(simpleLocation.getLatitude());
                    mLastLocation.setLongitude(simpleLocation.getLongitude());
                    sendMessage("long: " + mLastLocation.getLongitude() + ",lat:" + mLastLocation.getLatitude(), Constants.LOCATION);
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
       ActivityCompat.requestPermissions(getActivity(), permission, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ChatFragment", "Result");
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case 100:
                        final Uri selectedUri = data.getData();
                        String mimeType = getMimeType(data.getData().getPath());

                        if (mimeType != null) {

                            if (mimeType.equalsIgnoreCase("image/jpg") || mimeType.equalsIgnoreCase("image/png") || mimeType.equalsIgnoreCase("image/jpeg") || mimeType.equalsIgnoreCase("image/GIF")) {
                                sendMessage(getPathFromURI(selectedUri), Constants.IMAGE);
                            } else {
                                sendMessage(getPath(getActivity(), selectedUri), Constants.VIDEO);
                            }

                        } else {
                            if (isImageFile(selectedUri)) {
                                filePath = getPath(getActivity(), selectedUri);
                                sendMessage(filePath, Constants.IMAGE);
                            } else {
                                sendMessage(getPath(getActivity(), selectedUri), Constants.VIDEO);
                            }
                        }
                        break;
                    case 200://for video
                        Uri uriVideo = data.getData();
                        sendMessage(getPath(getActivity(), uriVideo), Constants.VIDEO);
                        break;
                    case 300:
                        Uri savedUri = data.getData();
                        sendMessage(getPath(getActivity(), savedUri), Constants.AUDIO);
                        break;
                }
            } else {
                if (requestCode == TAKE_PICTURE) {
                    try {
                        File finalFile = new File(getRealPathFromURI(selectedImageUri));
                        sendMessage(finalFile.getPath(), Constants.IMAGE);
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
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
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
        selectedImageUri = getActivity().getContentResolver().insert(
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void sendMessage(String path, String type) {
        Message message = new Message();
        message.setType(type);
        message.setMsg(path);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String str = sdf.format(new Date());
        message.setSent_at(str);
        message.setMine(true);
        message.setTo_id(AppController.getInstance().getClientInfo().getUser_id());
        addMessage(message);
    }

    public String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

    }

    public boolean isImageFile(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
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
        int milliseconds = (int) (time / 100) % 10;
        tvRecordTimer.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + "." + milliseconds);
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
        String[] current_date = date.split(" ")[1].split(":");
        Log.e("Length of the array", String.valueOf(current_date.length));
        if (current_date.length < 3){
            return  current_date[0] + ":" + current_date[1];
        }
        return current_date[0] + ":" + current_date[1]  + ":" + current_date[2];

    }


}
