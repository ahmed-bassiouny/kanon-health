package com.germanitlab.kanonhealth.httpchat;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dewarder.holdinglibrary.HoldingButtonLayout;
import com.dewarder.holdinglibrary.HoldingButtonLayoutListener;
import com.germanitlab.kanonhealth.Comment;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.FCViewPager;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentFragment;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.models.user.UserInfoResponse;
import com.germanitlab.kanonhealth.ormLite.MessageRepositry;
import com.germanitlab.kanonhealth.ormLite.UserRepository;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.germanitlab.kanonhealth.settings.CustomerSupportActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class HttpChatFragment extends ParentFragment implements ApiResponse, Serializable, HoldingButtonLayoutListener {

    @BindView(R.id.rv_chat_messages)
    RecyclerView recyclerView;
    @BindView(R.id.et_chat_message)
    EditText etMessage;

    @BindView(R.id.start_record)
    ImageView start_record;

    @BindView(R.id.img_send_txt)
    ImageButton img_send_txt;

    @BindView(R.id.pbar_loading)
    ProgressBar pbar_loading;
    @BindView(R.id.tv_chat_user_name)
    TextView tv_chat_user_name;

    @BindView(R.id.img_chat_user_avatar)
    ImageView img_chat_user_avatar;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.can_rate)
    Button canRate;
    @BindView(R.id.imgbtn_chat_attach)
    ImageView imgbtn_chat_attach;

    @BindView(R.id.open_chat_session)
    LinearLayout open_chat_session;
    @BindView(R.id.attachment)
    CardView attachment;


    private MediaRecorder mRecorder;
    private File mOutputFile;
    private long startTimeForRecording = 0, endTimeForRecording = 0;
    private Handler mHandler = new Handler();
    private long startTime = 0;
    private int[] amplitudes = new int[100];
    private int i = 0;


    // loca variable
    int userID;
    String userPassword;
    int doctorID;

    private static final int SELECT_VIDEO = 2;
    private static final int RECORD_VIDEO = 3;
    private Uri selectedImageUri = null;
    private boolean show_privacy = false;
    User doctor;
    UserRepository userRepository;


    ArrayList<Message> messages;
    PrefManager prefManager;
    ChatAdapter chatAdapter;
    static HttpChatFragment httpChatFragment;
    MessageRepositry messageRepositry;

    boolean iamDoctor = false;
    boolean iamClinic = false;
    public static boolean chatRunning = false;
    LocationManager mLocationManager;

    private static final DateFormat mFormatter = new SimpleDateFormat("mm:ss:SS");
    private static final float SLIDE_TO_CANCEL_ALPHA_MULTIPLIER = 2.5f;
    private static final long TIME_INVALIDATION_FREQUENCY = 50L;

    private HoldingButtonLayout mHoldingButtonLayout;
    private TextView mTime;
    private EditText mInput;
    private View mSlideToCancel;

    private int mAnimationDuration;
    private ViewPropertyAnimator mTimeAnimator;
    private ViewPropertyAnimator mSlideToCancelAnimator;
    private ViewPropertyAnimator mInputAnimator;

    private long mStartTime;
    private Runnable mTimerRunnable;
    FCViewPager vp;
    boolean flagLongPress=false;
    long timeDifference=0;
    boolean showAttachmentDialog = false;
    HoldingButtonLayoutListener fragment= this;
    boolean expand=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.new_chat, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mHoldingButtonLayout = (HoldingButtonLayout) view.findViewById(R.id.chat_bar);
        mHoldingButtonLayout.addListener(this);
        mTime = (TextView) view.findViewById(R.id.time);
        mInput = (EditText) view.findViewById(R.id.et_chat_message);
        mSlideToCancel = view.findViewById(R.id.slide_to_cancel);


        mAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        canRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Comment.class);
                intent.putExtra("doc_id", String.valueOf(doctor.get_Id()));
                intent.putExtra("request_id", String.valueOf(doctor.getRequest_id()));
                startActivity(intent);
                getActivity().finish();
            }
        });
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                if (view.getRootView().getHeight() - (r.bottom - r.top) > 500) { // if more than 100 pixels, its probably a keyboard...
                    img_send_txt.setVisibility(View.VISIBLE);
                  //  mHoldingButtonLayout.removeListener(fragment);

                } else {
                    if (etMessage.getText().toString().trim().length() > 0) {

                        img_send_txt.setVisibility(View.VISIBLE);
                        mHoldingButtonLayout.removeListener(fragment);

                    } else {
                        img_send_txt.setVisibility(View.GONE);
                        mHoldingButtonLayout.addListener(fragment);
                    }

                }
            }
        });


//        mHoldingButtonLayout.getHoldingView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//
//
//                    // Do what you want
//                    return true;
//                }
//                return false;
//            }
//        });
//

        return view;
    }

    public static HttpChatFragment newInstance(int doctorID) {
        Bundle bundle = new Bundle();
        bundle.putInt("doctorID", doctorID);
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

        try {
            initObjects();
            initData();
            showAttachmentDialog();
            checkMode();
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.please_open_chat_again, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    // declare objects in this fragment
    private void initObjects() {
        prefManager = new PrefManager(getContext());
        doctor = new User();
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        messageRepositry = new MessageRepositry(getActivity());
        messages = new ArrayList<>();
        userRepository = new UserRepository(getActivity());
    }

    // set data in object
    private void initData() {
        iamDoctor = new Gson().fromJson(new PrefManager(getContext()).getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser().getIsDoc() == 1;
        iamClinic = new Gson().fromJson(new PrefManager(getContext()).getData(PrefManager.USER_KEY), UserInfoResponse.class).getUser().getIsClinic() == 1;

        userID = prefManager.getInt(PrefManager.USER_ID);
        userPassword = prefManager.getData(prefManager.USER_PASSWORD);

        doctorID = getArguments().getInt("doctorID");
        if (userID == doctorID) {
            doctor.setLast_name(getResources().getString(R.string.documents));
            doctor.setFirst_name(" ");
            show_privacy = true;
            toolbar.setVisibility(View.GONE);
        } else if (doctorID == CustomerSupportActivity.supportID) {
            doctor.setId(doctorID);
            doctor.setLast_name(getResources().getString(R.string.support));
            doctor.setIsOpen(1);
            doctor.setFirst_name(" ");
            img_chat_user_avatar.setVisibility(View.INVISIBLE);
            tv_chat_user_name.setEnabled(false);
        } else {
            // get data of doctor i talk with him from database
            try {
                doctor.setId(doctorID);
                doctor = userRepository.getDoctor(doctor);
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
            }
        }

        if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty())
            ImageHelper.setImage(img_chat_user_avatar, Constants.CHAT_SERVER_URL_IMAGE + "/" + doctor.getAvatar());
        if (doctor.getFullName() != null) {
            tv_chat_user_name.setText(doctor.getFullName());
        }

    }

    private void loadChatOnline() {
        MessageRequest messageRequest = new MessageRequest(userID, doctorID);
        new HttpCall(getActivity(), this).loadChat(messageRequest);
        // request seen all msg
    }

    private void loadChatOffline() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                messages = (ArrayList) messageRepositry.getAll(userID, doctorID);
                isStoragePermissionGranted();
                pbar_loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSuccess(Object response) {
        messages = (ArrayList<Message>) response;
        isStoragePermissionGranted();

        if (getActivity() != null) // I'm almost sure that this is caused when the thread finish its work but the activity is no longer visible.
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Message message : messages) {
                        messageRepositry.createOrUpate(message);
                    }
                }
            });
    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getActivity(), R.string.error_connection, Toast.LENGTH_SHORT).show();
        pbar_loading.setVisibility(View.GONE);
    }

    public boolean isStoragePermissionGranted() {
        if (getContext() != null && Build.VERSION.SDK_INT >= 23) {
            if (getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setData();
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.WRITE_EXTERNAL_STORAGE);
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
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //resume tasks needing this
                switch (requestCode) {
                    case Constants.WRITE_EXTERNAL_STORAGE:
                        setData();
                        break;
                    case Constants.CAMERA_PERMISSION_CODE:
                        //takePhoto();
                        break;
                    case Constants.READ_EXTERNAL_STORARE_PERMISSION_CODE:
                       // pickImage();
                        break;
                    case Constants.VIDEO_PERMISSION_CODE:
                       // recordVideo();
                        break;
                    case Constants.AUDIO_PERMISSION_CODE:
                        setData();
                        break;
                    case Constants.LAST_LOCATION_PERMISSION_CODE:
                        getMyLocation();
                        break;
                    case 13:
//                        startRecording();
                        break;
                }
            } else {
                switch (requestCode) {

                    case 13:
//                        mHoldingButtonLayout.cancel();
                        break;
                }
            }
        }
    }

    private void setData() {
        if (getActivity() == null)
            return;
        chatAdapter = new ChatAdapter(messages, getActivity(), show_privacy);
        recyclerView.setAdapter(chatAdapter);
        pbar_loading.setVisibility(View.GONE);
        recyclerView.scrollToPosition(messages.size() - 1);
    }
//
    @OnTextChanged(R.id.et_chat_message)
    public void changeText() {
        if (etMessage.getText().toString().trim().length() > 0) {
            img_send_txt.setVisibility(View.VISIBLE);
            mHoldingButtonLayout.setButtonEnabled(false);
            mHoldingButtonLayout.removeListener(this);

        } else {
            img_send_txt.setVisibility(View.GONE);
            mHoldingButtonLayout.setButtonEnabled(true);
            mHoldingButtonLayout.addListener(this);

        }
    }

//    @OnClick(R.id.et_chat_message)
//    public void clickOnTextView()
//    {
//        img_send_txt.setVisibility(View.VISIBLE);
//        mHoldingButtonLayout.setButtonEnabled(false);
//        mHoldingButtonLayout.removeListener(this);
//        start_record.setVisibility(View.GONE);
//    }



    @OnClick(R.id.img_send_txt)
    public void img_send_txt() {
        try {
            // declare object and set attribute
            final Message message = new Message();
            message.setUser_id(userID);
            message.setFrom_id(userID);
            message.setTo(doctorID);
            message.setMsg(etMessage.getText().toString());
            message.setType(Constants.TEXT);
            message.setDate(getDateTimeNow());
            message.setSent_at(getDateTimeNow());
            etMessage.setText("");
            message.setIs_send(false);
            etMessage.setHint(R.string.write_a_message);
            messages.add(message);
            chatAdapter.setList(messages);
            chatAdapter.notifyDataSetChanged();
            final int index = messages.size() - 1;
            recyclerView.scrollToPosition(index);

            if (iamDoctor && doctorID != userID && doctor.getIsDoc() == 0 && doctor.isClinic == 0 && doctor.getIsOpen() == 0) {
                new HttpCall(getActivity(), new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        imgbtn_chat_attach.setEnabled(true);
                        mHoldingButtonLayout.setButtonEnabled(true);
                        // img_requestpermission.setEnabled(true);
                        doctor.setIsOpen(1);
                        userRepository.update(doctor);
                        checkSessionOpen(iamDoctor);


                        new HttpCall(getContext(), new ApiResponse() {
                            @Override
                            public void onSuccess(Object response) {
                                Message temp = messages.get(index);
                                temp.setIs_send(true);
                                messages.set(index, temp);
                                chatAdapter.setList(messages);
                                chatAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(messages.size() - 1);
                                messageRepositry.create((Message) response);
                            }

                            @Override
                            public void onFailed(String error) {
                                Toast.makeText(getContext(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                                removeDummyMessage(index);
                            }
                        }).sendMessage(message);


                    }

                    @Override
                    public void onFailed(String error) {
                        imgbtn_chat_attach.setEnabled(false);
                        mHoldingButtonLayout.setButtonEnabled(false);
                        //  img_requestpermission.setEnabled(false);
                        Toast.makeText(getContext(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                        removeDummyMessage(index);
                    }
                }).sendSessionRequest(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD),
                        String.valueOf(doctor.getId()), "2");
            } else {

                new HttpCall(getContext(), new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        Message temp = messages.get(index);
                        temp.setIs_send(true);
                        messages.set(index, temp);
                        chatAdapter.setList(messages);
                        chatAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messages.size() - 1);
                        messageRepositry.create((Message) response);

                    }

                    @Override
                    public void onFailed(String error) {
                        Toast.makeText(getContext(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                        removeDummyMessage(index);
                    }
                }).sendMessage(message);


            }

            //request

        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.msg_not_send, Toast.LENGTH_SHORT).show();
            Crashlytics.logException(e);
            Log.e("send msg", "img_send_txt: ", e);
        }
    }

    @OnClick(R.id.imgbtn_chat_attach)
    public void showDialogMedia() {
        try {

            if (showAttachmentDialog) {
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;
            } else {
                attachment.setVisibility(View.VISIBLE);
                showAttachmentDialog = true;
                YoYo.with(Techniques.SlideInUp)
                        .duration(500)
                        .playOn(attachment);

            }
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.attach_not_show, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_VIDEO:
                case RECORD_VIDEO:
                    try {
                        String filepath = new File(ImageFilePath.getPath(getActivity(), data.getData())).getPath();
                        String ext1 = filepath.substring(filepath.lastIndexOf(".")); // Extension with dot .jpg, .png
                        final String type;
                        if (ext1.equals(".mp4") || ext1.equals(".3gp"))
                            type = Constants.VIDEO;
                        else {
                            Toast.makeText(getActivity(), R.string.this_file_is_not_supported, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final int index2 = creatDummyMessage();
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
                                message.setIs_send(false);
                                message.setSent_at(getDateTimeNow());
                                //request
                                new HttpCall(getActivity(), new ApiResponse() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        creatRealMessage((Message) response, index2);
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        Toast.makeText(getActivity(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                                        removeDummyMessage(index2);
                                    }
                                }).sendMessage(message);
                            }

                            @Override
                            public void onFailed(String error) {
                                Toast.makeText(getActivity(), R.string.cant_upload, Toast.LENGTH_SHORT).show();
                                removeDummyMessage(index2);
                            }
                        }).uploadMedia(new File(ImageFilePath.getPath(getActivity(), data.getData())).getPath());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), R.string.msg_not_send, Toast.LENGTH_SHORT).show();
                        Crashlytics.logException(e);
                        Log.e("send msg", "on Activity result: ", e);
                    }
                    break;
            }
        }
    }


    private void showAttachmentDialog() {

        getActivity().findViewById(R.id.img_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAndSelectImage(PickerBuilder.SELECT_FROM_CAMERA);
            }
        });
        getActivity().findViewById(R.id.img_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAndSelectImage(PickerBuilder.SELECT_FROM_GALLERY);
            }
        });

        getActivity().findViewById(R.id.img_selectvideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        pickVideo();
                    } else
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_EXTERNAL_STORARE_PERMISSION_CODE);
                } else {
                    pickVideo();
                }
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;
            }
        });
        getActivity().findViewById(R.id.img_recordvideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        recordVideo();
                    } else
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.VIDEO_PERMISSION_CODE);
                } else {
                    recordVideo();
                }
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;
            }
        });
        getActivity().findViewById(R.id.img_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                        requestPermissions(permission, Constants.LAST_LOCATION_PERMISSION_CODE);
                    } else {
                        getMyLocation();
                    }
                } else {
                    getMyLocation();
                }

            }
        });
        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;

                return false;
            }
        });

    }
    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForLocationPermission();
            return;
        }
        Location location = getLastKnownLocation();
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            getLocationandSend(longitude, latitude);
        }

    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                askForLocationPermission();
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void askForLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        requestPermissions(permission, Constants.LAST_LOCATION_PERMISSION_CODE);
    }
    private int creatDummyMessage() {
        Message message = new Message();
        message.setSent_at(getDateTimeNow());
        message.setType(Constants.UNDEFINED);
        message.setFrom_id(userID);
        message.setIs_send(false);
        messages.add(message);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        int index = messages.size() - 1;
        recyclerView.scrollToPosition(index);
        return index;
    }

    private String getDateTimeNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()).toString();
    }

    private void pickVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_VIDEO);

    }

    private void recordVideo() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, RECORD_VIDEO);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (userID != doctorID) {
            chatRunning = true;
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                    new IntentFilter("MyData"));
        }
        if (userID == doctorID)
            show_privacy = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (userID != doctorID) {
            chatRunning = false;
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // notificationType ==  1 for new messagee ,2 for login , 3 for deliver message , 4 for close session   Andy
            int notificationType = intent.getIntExtra("notificationtype", 0);
            if (notificationType == 1) {
                Message message = (Message) intent.getSerializableExtra("extra");
                if (message.getFrom_id() == doctorID) {
                    creatRealMessage(message, 0);
                    messageSeen();
                    // seen request for specific msg
                } else {
                    // show notification
                    messagesDeliver();
                }

            } else if (notificationType == 4) {
                // doctor.setIsOpen(0);
                // checkSessionOpen(iamDoctor);
                //  Toast.makeText(context, "Session Closed", Toast.LENGTH_SHORT).show();
                Message message = (Message) intent.getSerializableExtra("extra");
                if ((message.getFrom_id() == userID || message.getFrom_id() == doctorID) && (message.getTo() == userID || message.getTo() == doctorID)) {
                    doctor.setIsOpen(0);
                    userRepository.update(doctor);
                    checkSessionOpen(iamDoctor);
                    getActivity().invalidateOptionsMenu();

                }
            } else if (notificationType == 3) {
                //handle Deliverd Message here /// Andy
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Message message : messages) {
                            if (message.getSeen() == 0) {
                                int index = messages.indexOf(message);
                                message.setSeen(1);
                                messages.set(index, message);
                                chatAdapter.setList(messages);
                                chatAdapter.notifyDataSetChanged();
                                messageRepositry.createOrUpate(message);
                            }
                        }
                    }
                });
            } else if (notificationType == 6) {
                //handle Deliverd Message here /// Andy
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Message message : messages) {
                            if (message.getSeen() == 1 || message.getSeen() == 0) {
                                int index = messages.indexOf(message);
                                message.setSeen(2);
                                messages.set(index, message);
                                chatAdapter.setList(messages);
                                chatAdapter.notifyDataSetChanged();
                                messageRepositry.createOrUpate(message);
                            }
                        }
                    }
                });
            }


        }
    };


    private void getLocationandSend(double longitude, double latitude) {
        //create dummy message
        final int index2 = creatDummyMessage();
        final Message message = new Message();
        message.setUser_id(userID);
        message.setFrom_id(userID);
        message.setTo(doctorID);
        message.setSent_at(getDateTimeNow());
        message.setMsg("{\"long\":" + longitude + ",\"lat\":" + latitude + "}");
        message.setType(Constants.LOCATION);


        //request
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                creatRealMessage((Message) response, index2);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(getActivity(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                removeDummyMessage(index2);
            }
        }).sendMessage(message);
    }


//    private void setButtonToTextMsg(Boolean comingFromVoiceRecording) {
//
//        if (!comingFromVoiceRecording) {
//            start_record.setBackgroundResource(R.drawable.ic_send_black_24dp);
//            start_record.setOnTouchListener(null);
//        }
//
//        linearTextMsg.setVisibility(View.VISIBLE);
//        layout_chat_attach.setVisibility(View.VISIBLE);
//        relativeAudio.setVisibility(View.GONE);
//    }


    private void checkSessionOpen(final boolean iamDoctor) {

        if (doctorID == userID) {
            // i talk with my self in document
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
        } else if (iamDoctor && doctor.isClinic == 0 && doctor.getIsDoc() == 0) {
            // i chat with client and sssion closed
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
            if (doctor.getIsOpen() == 0) {
                imgbtn_chat_attach.setEnabled(false);
                mHoldingButtonLayout.setButtonEnabled(false);
                // img_requestpermission.setEnabled(false);
                etMessage.setHint(R.string.write_a_message);
            }
        } else if (doctor.getIsClinic() == 1) {
            // client chat with clinics
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
        } else if (doctor.getIsOpen() == 0) {
            // client chat with doctor and session closed
            mHoldingButtonLayout.setVisibility(View.INVISIBLE);
            open_chat_session.setVisibility(View.VISIBLE);
            if (iamDoctor == false && iamClinic == false) {
                if (doctor.getHave_rate() != 1) {
                    canRate.setVisibility(View.VISIBLE);
                } else {
                    canRate.setVisibility(View.GONE);
                }
            }
        } else {
            // client chat with doctor and session opened
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        if (doctorID == 1 || doctor.getIsOpen() == 0) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        } else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        }
        if (Helper.isNetworkAvailable(getContext())) {
            menu.getItem(0).setEnabled(true);
            menu.getItem(1).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_session:
                try {
                    AlertDialog.Builder adb_end = new AlertDialog.Builder(getActivity());
                    adb_end.setTitle(R.string.close_conversation);
                    adb_end.setCancelable(false);
                    adb_end.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new HttpCall(getActivity(), new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {


                                    Toast.makeText(getActivity(), R.string.session_ended, Toast.LENGTH_SHORT).show();
                                    doctor.setIsOpen(0);
                                    userRepository.update(doctor);
                                    checkSessionOpen(iamDoctor);
                                    getActivity().invalidateOptionsMenu();
                                    if (doctor.isClinic == 1) {
                                        Intent intent = new Intent(getActivity(), InquiryActivity.class);
                                        UserInfoResponse userInfoResponse = new UserInfoResponse();
                                        userInfoResponse.setUser(doctor);
                                        Gson gson = new Gson();
                                        intent.putExtra("doctor_data", gson.toJson(userInfoResponse));
                                        startActivity(intent);
                                    } else if (doctor.getIsDoc() == 1) {
                                        openPayment();
                                    } else {
                                        Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailed(String error) {
                                    Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_SHORT).show();
                                }
                            }).closeSession(String.valueOf(doctor.getId()));
                        }

                    });
                    adb_end.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    adb_end.show();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.end_session:
                try {
                    AlertDialog.Builder adb_end = new AlertDialog.Builder(getActivity());
                    adb_end.setTitle(R.string.close_conversation);
                    adb_end.setCancelable(false);
                    adb_end.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new HttpCall(getActivity(), new ApiResponse() {
                                @Override
                                public void onSuccess(Object response) {
                                    try {

                                        Toast.makeText(getActivity(), R.string.session_ended, Toast.LENGTH_SHORT).show();
                                        doctor.setIsOpen(0);
                                        userRepository.update(doctor);
                                        checkSessionOpen(iamDoctor);
                                        canRate.setVisibility(View.GONE);
                                        getActivity().invalidateOptionsMenu();
                                        if (doctor.isClinic == 1 || doctor.getIsDoc() == 1) {
                                            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                            adb.setTitle(R.string.rate_conversation);
                                            adb.setCancelable(false);
                                            adb.setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    new HttpCall(getActivity(), new ApiResponse() {
                                                        @Override
                                                        public void onSuccess(Object response) {
                                                            Intent intent = new Intent(getActivity(), Comment.class);
                                                            intent.putExtra("doc_id", String.valueOf(doctor.get_Id()));
                                                            intent.putExtra("request_id", String.valueOf(doctor.getRequest_id()));
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        }

                                                        @Override
                                                        public void onFailed(String error) {
                                                            Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).closeSession(String.valueOf(doctor.getId()));
                                                }
                                            });
                                            adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (iamDoctor == false && iamClinic == false) {
                                                       // doctor.setHave_rate(0);
                                                        //userRepository.update(doctor);
                                                        canRate.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                            adb.show();
                                        }
                                    } catch (Exception e) {
                                        Crashlytics.logException(e);
                                        Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailed(String error) {
                                    Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_SHORT).show();
                                }
                            }).closeSession(String.valueOf(doctor.getId()));
                        }

                    });
                    adb_end.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    adb_end.show();


                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getActivity(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button2)
    public void openPayment() {
        try {
            if (doctor.isClinic == 1) {
                Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
                intent.putExtra("doctor_data", doctor);
                startActivity(intent);
                getActivity().finish();
            } else {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                Gson gson = new Gson();
                prefManager.put(prefManager.USER_INTENT, gson.toJson(doctor));
                intent.putExtra("doctor_obj", doctor);
                startActivity(intent);
                getActivity().finish();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getActivity(), getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void checkMode() {
        if (Helper.isNetworkAvailable(getContext())) {
            loadChatOnline();
            imgbtn_chat_attach.setEnabled(true);
            etMessage.setEnabled(true);
            mHoldingButtonLayout.setButtonEnabled(true);
            img_send_txt.setEnabled(true);
            button2.setEnabled(true);
            button2.setText(R.string.start_new_request);
            etMessage.setHint(R.string.write_message);
            messageSeen();
            if (doctor != null)
                checkSessionOpen(iamDoctor);
        } else {
            loadChatOffline();
            mHoldingButtonLayout.setButtonEnabled(false);
            imgbtn_chat_attach.setEnabled(false);
            etMessage.setEnabled(false);
            img_send_txt.setEnabled(false);
            button2.setEnabled(false);
            button2.setText(R.string.offline_mode_canot_contact_with_doctor);
            etMessage.setHint(R.string.offline_mode_canot_contact_with_doctor);
        }
    }

    private void creatRealMessage(Message message, int index) {
        try {
            if (index >= 0)
                messages.remove(index);
            messages.add(message);
            chatAdapter.setList(messages);
            chatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messages.size() - 1);
            messageRepositry.create(message);
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
            Crashlytics.logException(e);
        }
    }

    private void removeDummyMessage(int index) {
        messages.remove(index);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size() - 1);
    }


    private void messageSeen() {
        // i sent request to make my msg seen
        MessageRequestSeen messageRequest = new MessageRequestSeen(userID, userPassword, doctorID);
        try {
            new HttpCall(getActivity(), new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                }

                @Override
                public void onFailed(String error) {

                }
            }).messagesSeen(messageRequest);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("messageSeen", "messageSeen: ", e);
        }
    }

    private void messagesDeliver() {
        // i sent request to make my msg seen
        try {
            MessageRequestSeen messageRequest = new MessageRequestSeen(userID, doctorID);
            new HttpCall(getActivity(), new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                }

                @Override
                public void onFailed(String error) {

                }
            }).messagesDeliver(messageRequest);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e("messagesDeliver", "messagesDeliver: ", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatAdapter != null)
            chatAdapter.clearSelected();
    }

    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @OnClick({R.id.img_chat_user_avatar, R.id.tv_chat_user_name})
    public void openProfile() {
        if (doctor != null && (doctor.isClinic == 1 || doctor.getIsDoc() == 1)) {
            Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
            intent.putExtra("doctor_data", doctor);
            getActivity().startActivity(intent);
        } else {
            // this object is user and should open ProfileActivity
        }
    }


    @Override
    public void onBeforeExpand() {

        mHoldingButtonLayout.setButtonEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHoldingButtonLayout.setButtonEnabled(true);
            }
        }, 700);

            timeDifference = 0;
            flagLongPress = false;
            expand = true;
            vp = (FCViewPager) getActivity().findViewById(R.id.myviewpager);
            if (vp != null) {
                vp.setEnableSwipe(false);
            }

            cancelAllAnimations();
            mSlideToCancel.setTranslationX(0f);
            mSlideToCancel.setAlpha(0f);
            mSlideToCancelAnimator = mSlideToCancel.animate().alpha(1f).setDuration(mAnimationDuration);
            mSlideToCancelAnimator.start();

            mInputAnimator = mInput.animate().alpha(0f).setDuration(mAnimationDuration);
            mInputAnimator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mInput.setVisibility(View.INVISIBLE);
                    mInputAnimator.setListener(null);
                }
            });
            mInputAnimator.start();

            mTime.setTranslationY(mTime.getHeight());
            mTime.setAlpha(0f);
            // mTime.setVisibility(View.VISIBLE);
            mTimeAnimator = mTime.animate().translationY(0f).alpha(1f).setDuration(mAnimationDuration);
            mTimeAnimator.start();
            mStartTime = System.currentTimeMillis();

    }

    @Override
    public void onExpand() {
        expand = true;
    // if (Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
    invalidateTimer();
    // }
}




    @Override
    public void onBeforeCollapse() {
        cancelAllAnimations();
        mSlideToCancelAnimator = mSlideToCancel.animate().alpha(0f).setDuration(mAnimationDuration);
        mSlideToCancelAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSlideToCancel.setVisibility(View.INVISIBLE);
                mSlideToCancelAnimator.setListener(null);
            }
        });
        mSlideToCancelAnimator.start();
        mInput.setAlpha(0f);
        mInput.setVisibility(View.VISIBLE);
        mInputAnimator = mInput.animate().alpha(1f).setDuration(mAnimationDuration);
        mInputAnimator.start();

        mTimeAnimator = mTime.animate().translationY(mTime.getHeight()).alpha(0f).setDuration(mAnimationDuration);
        mTimeAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mTime.setVisibility(View.INVISIBLE);
                mTimeAnimator.setListener(null);
            }
        });
        mTimeAnimator.start();

    }

    @Override
    public void onCollapse(boolean isCancel) {

        if(expand) {
            expand=false;
            vp = (FCViewPager) getActivity().findViewById(R.id.myviewpager);
            if (vp != null) {
                vp.setEnableSwipe(true);
            }
            if (Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

                try {
                    stopTimer();
                    if (isCancel) {
                        if (flagLongPress == true) {
                            stopRecording(false);
                        }

//            Toast.makeText(getActivity(), "Recording canceled!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (flagLongPress == true) {
                            if (System.currentTimeMillis()-mStartTime > 1000) {
                                stopRecording(true);
//            Toast.makeText(getActivity(), "Recording submitted! Time " + getFormattedTime(), Toast.LENGTH_SHORT).show();

                                final int index2 = creatDummyMessage();
                                new HttpCall(getActivity(), new ApiResponse() {
                                    @Override
                                    public void onSuccess(Object response) {
                                        final Message message = new Message();
                                        message.setUser_id(userID);
                                        message.setFrom_id(userID);
                                        message.setTo(doctorID);
                                        message.setIs_url(1);
                                        message.setMsg((String) response);
                                        message.setType(Constants.AUDIO);
                                        message.setIs_send(false);
                                        message.setSent_at(getDateTimeNow());

                                        //request
                                        new HttpCall(getActivity(), new ApiResponse() {
                                            @Override
                                            public void onSuccess(Object response) {
                                                creatRealMessage((Message) response, index2);
                                            }

                                            @Override
                                            public void onFailed(String error) {
                                                removeDummyMessage(index2);
                                                Toast.makeText(getActivity(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                                            }
                                        }).sendMessage(message);
                                    }

                                    @Override
                                    public void onFailed(String error) {
                                        Toast.makeText(getActivity(), R.string.cant_upload, Toast.LENGTH_SHORT).show();
                                        removeDummyMessage(index2);
                                    }
                                }).uploadMedia(mOutputFile.getPath());
                            } else {
                                stopRecording(false);
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.e("milad", "onCollapse: ", e);
                }
            }

        }

    }

    @Override
    public void onOffsetChanged(float offset, boolean isCancel) {
        mSlideToCancel.setTranslationX(-mHoldingButtonLayout.getWidth() * offset);
        mSlideToCancel.setAlpha(1 - SLIDE_TO_CANCEL_ALPHA_MULTIPLIER * offset);
    }

    private void invalidateTimer() {
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                mTime.setText(getFormattedTime());
                invalidateTimer();
                if(timeDifference>1000&&flagLongPress==false)
                {
                    flagLongPress=true;

                    if(Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {


                                mStartTime = System.currentTimeMillis();
                                mTime.setVisibility(View.VISIBLE);
                                mSlideToCancel.setVisibility(View.VISIBLE);
                                mTime.setText(getFormattedTime());
                                startRecording();



                    }
                    else{
                      onBeforeCollapse();
//                      onCollapse(true);
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.AUDIO_PERMISSION_CODE);
                    }

                }
            }
        };

        mTime.postDelayed(mTimerRunnable, TIME_INVALIDATION_FREQUENCY);
    }

    private void stopTimer() {
        if (mTimerRunnable != null) {
            mTime.getHandler().removeCallbacks(mTimerRunnable);
        }
    }

    private void cancelAllAnimations() {
        if (mInputAnimator != null) {
            mInputAnimator.cancel();
        }

        if (mSlideToCancelAnimator != null) {
            mSlideToCancelAnimator.cancel();
        }

        if (mTimeAnimator != null) {
            mTimeAnimator.cancel();
        }
    }

    private String getFormattedTime() {

        timeDifference = System.currentTimeMillis() - mStartTime;
        return mFormatter.format(new Date(timeDifference));
    }


    private void startRecording() {
        //  Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));خى

        startTimeForRecording = mStartTime;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mRecorder.setAudioEncodingBitRate(16);
        mRecorder.setAudioSamplingRate(44100);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            startTime = SystemClock.elapsedRealtime();
            mHandler.postDelayed(mTickExecutor, 100);
        } catch (Exception e) {
        }

    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/RECORDING_"
                + dateFormat.format(new Date())
                + ".m4a");
    }

    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor, 100);
        }
    };

    private void tick() {
        long time = (startTime < 0) ? 0 : (SystemClock.elapsedRealtime() - startTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
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
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        startTime = 0;
        mHandler.removeCallbacks(mTickExecutor);
        if (!saveFile && mOutputFile != null) {
            mOutputFile.delete();
        }

    }

    @Override
    public void ImagePickerCallBack(Uri uri) {

        try {
            final int index = creatDummyMessage();
            new HttpCall(getContext(), new ApiResponse() {
                @Override
                public void onSuccess(Object response) {
                    final Message message = new Message();
                    message.setUser_id(userID);
                    message.setFrom_id(userID);
                    message.setTo(doctorID);
                    message.setIs_url(1);
                    message.setMsg((String) response);
                    message.setType(Constants.IMAGE);
                    message.setIs_send(false);
                    message.setSent_at(getDateTimeNow());

                    //request
                    new HttpCall(getContext(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            creatRealMessage((Message) response, index);
                        }

                        @Override
                        public void onFailed(String error) {
                            Toast.makeText(getContext(), R.string.picture_not_send, Toast.LENGTH_SHORT).show();
                            removeDummyMessage(index);
                        }
                    }).sendMessage(message);
                }

                @Override
                public void onFailed(String error) {
                    Toast.makeText(getActivity(), R.string.cant_upload, Toast.LENGTH_SHORT).show();
                    removeDummyMessage(index);
                }
            }).uploadMedia(new File(ImageFilePath.getPath(getActivity(), uri)).getPath());
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.msg_not_send, Toast.LENGTH_SHORT).show();
            Crashlytics.logException(e);
            Log.e("send msg", "on Activity result: ", e);
        }
    }
    private void takeAndSelectImage(int type){
        // type = PickerBuilder.SELECT_FROM_GALLERY or PickerBuilder.SELECT_FROM_CAMERA
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Helper.getCroppedImageFromCamera(HttpChatFragment.this,getActivity(), PickerBuilder.SELECT_FROM_GALLERY);
            } else
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_PERMISSION_CODE);
        } else
            Helper.getCroppedImageFromCamera(HttpChatFragment.this,getActivity(), type);

        attachment.setVisibility(View.INVISIBLE);
        showAttachmentDialog = false;
    }



}
