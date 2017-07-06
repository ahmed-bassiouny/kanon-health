package com.germanitlab.kanonhealth.httpchat;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.PopupHelper;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import im.delight.android.location.SimpleLocation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HttpChatFragment extends Fragment implements ApiResponse ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int ACCESS_FINE_LOCATION =51 ;
    // Declare UI
    @BindView(R.id.rv_chat_messages)
    RecyclerView recyclerView;
    @BindView(R.id.et_chat_message)
    EditText etMessage;
    @BindView(R.id.img_send_audio)
    SeekBar img_send_audio;
    @BindView(R.id.img_send_txt)
    ImageButton img_send_txt;
    @BindView(R.id.pbar_loading)
    ProgressBar pbar_loading;
    @BindView(R.id.tv_chat_user_name)
    TextView tv_chat_user_name;

    @BindView(R.id.img_chat_user_avatar)
    ImageView img_chat_user_avatar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // loca variable
    int userID ;
    int userPassword = 0;
    int doctorID ;
    String doctorName="";
    String doctorUrl="";
    private static final int TAKE_PICTURE = 1;
    private static final int SELECT_PICTURE = 2;
    private static final int RECORD_VIDEO =3 ;
    private Uri selectedImageUri = null;
    private boolean show_privacy = false;

    private final static int WRITE_EXTERNAL_STORAGE=50;

    ArrayList<Message> messages;
    PrefManager prefManager;
    ChatAdapter chatAdapter;
    HashMap<UUID, Message> uuidMessageHashMap = new HashMap<>();
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    static HttpChatFragment httpChatFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_http_chat, container, false);
        ButterKnife.bind(this, view);
        buildGoogleApiClient();
        return view;
    }
    public static HttpChatFragment newInstance(int doctorID,String doctorUrl) {
        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
        bundle.putString("doctorName", "My Documents");
        bundle.putString("doctorUrl", doctorUrl);
        if (httpChatFragment == null) {
            httpChatFragment = new HttpChatFragment();
            httpChatFragment.setArguments(bundle);
        } else {
            httpChatFragment.getArguments().putAll(bundle);
        }
        return httpChatFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initObjects();
        initData();
        loadChat(userID, doctorID);

    }


    // declare objects in this fragment
    private void initObjects() {
        prefManager = new PrefManager(getActivity());

    }

    // set data in object
    private void initData() {
        userID=prefManager.getInt(PrefManager.USER_ID);
        doctorID=getArguments().getInt("doctorID");
        doctorName=getArguments().getString("doctorName");
        doctorUrl=getArguments().getString("doctorUrl");
        if(!doctorUrl.isEmpty())
        ImageHelper.setImage(img_chat_user_avatar,Constants.CHAT_SERVER_URL_IMAGE+"/"+doctorUrl,getActivity());
        tv_chat_user_name.setText(doctorName);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        if(userID==doctorID) {
            show_privacy = true;
            toolbar.setVisibility(View.GONE);
        }
    }

    private void loadChat(int userID, int doctorID) {
        MessageRequest messageRequest = new MessageRequest(userID, doctorID);
        new HttpCall(getActivity(), this).loadChat(messageRequest);
    }

    @Override
    public void onSuccess(Object response) {
        messages = (ArrayList<Message>) response;
        isStoragePermissionGranted();

    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
        pbar_loading.setVisibility(View.GONE);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setData();
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                return false;
            }
        } else {
            setData();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&requestCode==WRITE_EXTERNAL_STORAGE) {
            //resume tasks needing this permission
            setData();
        }
    }

    private void setData() {
        chatAdapter = new ChatAdapter(messages, getActivity(), show_privacy);
        recyclerView.setAdapter(chatAdapter);
        pbar_loading.setVisibility(View.GONE);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    // init text input
    @OnTextChanged(R.id.et_chat_message)
    public void changeText() {

        if (etMessage.getText().toString().trim().length() > 0) {
            img_send_audio.setVisibility(View.GONE);
            img_send_txt.setVisibility(View.VISIBLE);
        } else {
            img_send_audio.setVisibility(View.VISIBLE);
            img_send_txt.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.img_send_txt)
    public void img_send_txt() {
        // declare object and set attribute
        Message message = new Message();
        message.setUser_id(userID);
        message.setFrom_id(userID);
        message.setTo(doctorID);
        message.setMsg(etMessage.getText().toString());
        message.setType(Constants.TEXT);
        message.setIs_forward(1);
        etMessage.setText("");
        messages.add(message);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        final int index=messages.size()-1;
        recyclerView.scrollToPosition(index);


        //request
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Message temp = messages.get(index);
                temp.setIs_forward(0);
                messages.set(index,temp);
                chatAdapter.setList(messages);
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);

            }

            @Override
            public void onFailed(String error) {

            }
        }).sendMessage(message);
    }

    @OnClick(R.id.imgbtn_chat_attach)
    public void showDialogMedia() {
        showPopup(getView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    messages.add(creatDummyMessage());
                    final int index=messages.size()-1;
                    chatAdapter.setList(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size()-1);
                    new HttpCall(getActivity(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {

                            final Message message = new Message();
                            message.setUser_id(userID);
                            message.setFrom_id(userID);
                            message.setTo(doctorID);
                            message.setIs_url(1);
                            message.setMsg((String) response);
                            message.setType(Constants.IMAGE);
                            message.setIs_forward(0);
                            message.setSent_at(getDateTimeNow());


                            //request
                            new HttpCall(getActivity(), new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {
                                    messages.remove(index);
                                    messages.add(message);
                                    chatAdapter.setList(messages);
                                    chatAdapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(messages.size()-1);
                                }

                                @Override
                                public void onFailed(String error) {
                                    messages.remove(index);
                                    Toast.makeText(getActivity(), "Message not send", Toast.LENGTH_SHORT).show();
                                }
                            }).sendMessage(message);
                        }

                        @Override
                        public void onFailed(String error) {
                            Toast.makeText(getActivity(), R.string.cantupload, Toast.LENGTH_SHORT).show();
                        }
                    }).uploadMedia(new File(getRealPathFromURI(selectedImageUri)).getPath());
                    break;
                case SELECT_PICTURE:
                case RECORD_VIDEO:
                    String filepath = new File(getRealPathFromURI(data.getData())).getPath();
                    String ext1 = filepath.substring(filepath.lastIndexOf(".")); // Extension with dot .jpg, .png
                    final String type;
                    if(ext1.equals(".mp4"))
                        type=Constants.VIDEO;
                    else if (ext1.equals(".jpg")||ext1.equals(".png")||ext1.equals(".jpeg"))
                        type=Constants.IMAGE;
                    else {
                        Toast.makeText(getActivity(), "don't support this file", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    messages.add(creatDummyMessage());
                    final int index2=messages.size()-1;
                    chatAdapter.setList(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size()-1);

                    new HttpCall(getActivity(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            final Message message = new Message();
                            message.setUser_id(userID);
                            message.setFrom_id(userID);
                            message.setTo(doctorID);
                            message.setIs_url(1);
                            message.setMsg((String) response);
                            message.setType(type);
                            message.setIs_forward(0);
                            message.setSent_at(getDateTimeNow());

                            //request
                            new HttpCall(getActivity(), new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {
                                    messages.remove(index2);
                                    messages.add(message);
                                    chatAdapter.setList(messages);
                                    chatAdapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(messages.size()-1);
                                }

                                @Override
                                public void onFailed(String error) {
                                    messages.remove(index2);
                                    Toast.makeText(getActivity(), "Message not send", Toast.LENGTH_SHORT).show();
                                }
                            }).sendMessage(message);
                        }

                        @Override
                        public void onFailed(String error) {
                            Toast.makeText(getActivity(), R.string.cantupload, Toast.LENGTH_SHORT).show();
                        }
                    }).uploadMedia(new File(getRealPathFromURI(data.getData())).getPath());
                    break;
            }
        }
    }




    private void addChangeItemChat(final UUID key, final Message message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uuidMessageHashMap.put(key, message);
                ArrayList<Message> temp = new ArrayList<>();
                temp.addAll(uuidMessageHashMap.values());
                chatAdapter.setList(temp);
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(uuidMessageHashMap.size() - 1);
            }
        });
    }

    private void showPopup(View view) {
        final PopupWindow showPopup = PopupHelper.newBasicPopupWindow(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_navigation_menu, null);

        popupView.findViewById(R.id.img_view_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        takePhoto();
                    } else
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.CAMERA_PERMISSION_CODE);
                } else takePhoto();

                showPopup.dismiss();
            }
        });
        popupView.findViewById(R.id.img_view_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        pickImage();
                    } else
                        requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORARE_PERMISSION_CODE);
                } else {
                    pickImage();
                }
                showPopup.dismiss();
            }
        });
        popupView.findViewById(R.id.img_view_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (getActivity().checkSelfPermission( android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            getActivity().checkSelfPermission( android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        recordVideo();

                    } else
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.VIDEO_PERMISSION_CODE);
                } else {
                    recordVideo();
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
                        requestPermissions(permission, Constants.LAST_LOCATION_PERMISSION_CODE);
                    } else {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                    }
                } else mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);

                if (mLastLocation != null && mLastLocation.getLongitude() != 0) {
                    //sendMessage("long:" + mLastLocation.getLongitude() + ",lat:" + mLastLocation.getLatitude(), Constants.LOCATION);
                    //create dummy message
                    messages.add(creatDummyMessage());
                    final int index2=messages.size()-1;
                    chatAdapter.setList(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size()-1);

                    final Message message = new Message();
                    message.setUser_id(userID);
                    message.setFrom_id(userID);
                    message.setTo(doctorID);
                    message.setSent_at(getDateTimeNow());
                    message.setMsg("{\"long\":"+mLastLocation.getLongitude()+",\"lat\":"+mLastLocation.getLatitude()+"}");
                    message.setType(Constants.LOCATION);



                    //request
                    new HttpCall(getActivity(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            messages.remove(index2);
                            messages.add(message);
                            chatAdapter.setList(messages);
                            chatAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }

                        @Override
                        public void onFailed(String error) {
                            messages.remove(index2);
                            Toast.makeText(getActivity(), "Message not send", Toast.LENGTH_SHORT).show();
                        }
                    }).sendMessage(message);


                } else {

                    if (mLastLocation == null || mLastLocation.getLatitude() == 0) {
                        Toast.makeText(getActivity(), "Can't get your location", Toast.LENGTH_LONG).show();
                        mGoogleApiClient.reconnect();
                        return;
                    }
                }
            }
        });


        showPopup.setContentView(popupView);

        showPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        showPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);


        showPopup.showAtLocation(view, Gravity.TOP, 0, 400);


        showPopup.setOutsideTouchable(true);

        showPopup.setFocusable(true);
        // Removes default background.
        showPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void takePhoto() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        selectedImageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Message creatDummyMessage() {
        Message message = new Message();
        message.setSent_at(getDateTimeNow());
        message.setType(Constants.UNDEFINED);
        message.setFrom_id(userID);
        message.setIs_forward(1);
        return message;
    }

    private String getDateTimeNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()).toString();
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/* video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE);

    }
    private void recordVideo(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, RECORD_VIDEO);
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
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permission, ACCESS_FINE_LOCATION);

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData"));
        if(userID==doctorID)
            show_privacy=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message=(Message) intent.getSerializableExtra("extra");
            if(message.getFrom_id()!=doctorID)
                return;
            messages.add(message);
            chatAdapter.setList(messages);
            chatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messages.size() - 1);

        }
    };

}
