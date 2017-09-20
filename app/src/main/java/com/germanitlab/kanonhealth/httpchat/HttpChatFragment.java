package com.germanitlab.kanonhealth.httpchat;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.germanitlab.kanonhealth.ClinicProfileActivity;
import com.germanitlab.kanonhealth.Comment;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.DoctorProfileActivity;
import com.germanitlab.kanonhealth.FCViewPager;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.api.responses.HaveRateResponse;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ImageHelper;
import com.germanitlab.kanonhealth.helpers.ParentFragment;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.inquiry.InquiryActivity;
import com.germanitlab.kanonhealth.ormLite.HttpDocumentRepositry;
import com.germanitlab.kanonhealth.ormLite.HttpMessageRepositry;
import com.germanitlab.kanonhealth.ormLite.UserInfoRepositry;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.germanitlab.kanonhealth.profile.ImageFilePath;
import com.germanitlab.kanonhealth.settings.CustomerSupportActivity;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.content.Context.LOCATION_SERVICE;

public class HttpChatFragment extends ParentFragment implements Serializable, HoldingButtonLayoutListener, LocationListener {

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
    Toolbar toolbar;
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
    UserInfo userInfo;
    UserInfoRepositry chatModelRepositry;


    ChatAdapter chatAdapter;
    DocumentChatAdapter documentChatAdapter;
    static HttpChatFragment httpChatFragment;

    public static boolean chatRunning = false;

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
    boolean flagLongPress = false;
    long timeDifference = 0;
    boolean showAttachmentDialog = false;
    HoldingButtonLayoutListener fragment = this;
    boolean expand = false;
    boolean onBeforeExpand = false;
    ChatHelper chatHelper;
    ArrayList<Message> messages;
    HttpMessageRepositry messageRepositry;
    ArrayList<Document> documents;
    HttpDocumentRepositry documentRepositry;
    UserInfo userMe;
    int userType;
    HaveRateResponse haveRateResponse;
    Location myLocation = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.new_chat, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        try {
            userMe = new Gson().fromJson(PrefHelper.get(getActivity(), PrefHelper.KEY_USER_KEY, ""), UserInfo.class);
        } catch (Exception e) {
        }

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
//                intent.putExtra("doc_id", String.valueOf(userInfo.getUserID()));
//                intent.putExtra("request_id", String.valueOf(userInfo.getRequestID()));
                intent.putExtra("user_info", userInfo);
                intent.putExtra("request_id",haveRateResponse.getRequestId());
                startActivity(intent);
                getActivity().finish();
            }
        });
//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                view.getWindowVisibleDisplayFrame(r);
//                if (view.getRootView().getHeight() - (r.bottom - r.top) > 200) { // if more than 100 pixels, its probably a keyboard...
//                    img_send_txt.setVisibility(View.VISIBLE);
//                      mHoldingButtonLayout.removeListener(fragment);
//
//                } else {
//                    if (etMessage.getText().toString().trim().length() > 0) {
//                        img_send_txt.setVisibility(View.VISIBLE);
//                        mHoldingButtonLayout.removeListener(fragment);
//
//                    } else {
//                        img_send_txt.setVisibility(View.GONE);
//                        mHoldingButtonLayout.addListener(fragment);
//                    }
//
//                }
//            }
//        });
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
            Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    // declare objects in this fragment
    private void initObjects() {
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        chatHelper = new ChatHelper(getContext());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    // set data in object
    private void initData() {
        userID = PrefHelper.get(getContext(), PrefHelper.KEY_USER_ID, -1);
        userPassword = PrefHelper.get(getContext(), PrefHelper.KEY_USER_PASSWORD, "");
        doctorID = getArguments().getInt("doctorID");
        userType = getArguments().getInt("type");

        userInfo = new UserInfo();
        if (userID != doctorID ||(userID == doctorID&& userType == UserInfo.CLINIC)) {
            if (!(doctorID == CustomerSupportActivity.supportID && userType != UserInfo.CLINIC)) {
                userInfo = (UserInfo) getArguments().getSerializable("userInfo");
                if (userMe.getUserType() == UserInfo.PATIENT) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(userType==UserInfo.CLINIC) {
                            haveRateResponse = ApiHelper.getHaveRate(userID,userInfo.getId(),UserInfo.CLINIC);
                        }else
                        {
                            haveRateResponse = ApiHelper.getHaveRate(userID,userInfo.getUserID(),UserInfo.DOCTOR);
                        }
                            if (haveRateResponse != null) {
                                if (haveRateResponse.getStatus() == 0 && haveRateResponse.getRequestId() != -1) {
                                    HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            canRate.setVisibility(View.VISIBLE);
                                        }
                                    });

                                } else {
                                    HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            canRate.setVisibility(View.GONE);
                                        }
                                    });

                                }
                            }

                    }
                }).start();
            }
        }
        }
        if (userID == doctorID && userType!=UserInfo.CLINIC) {
            documents = new ArrayList<>();
            documentRepositry = new HttpDocumentRepositry(getActivity());
        } else {
            messages = new ArrayList<>();
            messageRepositry = new HttpMessageRepositry(getActivity());
        }
        chatModelRepositry = new UserInfoRepositry(getActivity());

        if (userID == doctorID && userType!=UserInfo.CLINIC) {
            userInfo.setLastName(getResources().getString(R.string.documents));
            userInfo.setFirstName(" ");
            toolbar.setVisibility(View.GONE);
        } else if (doctorID == CustomerSupportActivity.supportID && userType != UserInfo.CLINIC) {
            userInfo.setUserID(doctorID);
            userInfo.setLastName(getResources().getString(R.string.support));
            userInfo.setFirstName(" ");
            userInfo.setIsSessionOpen(1);
            img_chat_user_avatar.setVisibility(View.INVISIBLE);
            tv_chat_user_name.setEnabled(false);
        }
            // get data of doctor i talk with him from database
            //userInfo = chatModelRepositry.getDoctor(userID);
            //----------------- main scenario get user info from database but now i get info from backend (Ahmed 14 - 8 -2017 , 1:30 pm)
            //-------- get user from intent instead of database
            if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty())
                ImageHelper.setImage(img_chat_user_avatar, ApiHelper.SERVER_IMAGE_URL + "/" + userInfo.getAvatar(), R.drawable.placeholder);
            if (userType == UserInfo.CLINIC) {
                if (userInfo.getName() != null) {
                    tv_chat_user_name.setText(userInfo.getName());
                }
            } else {
                if (userInfo.getFullName() != null && userInfo.getLastName() != null) {
                    tv_chat_user_name.setText(userInfo.getLastName() + " " + userInfo.getFirstName());
                }
            }
        }


    private void loadChatOnline() {
        // request to get all messages
        new Thread(new Runnable() {
            @Override
            public void run() {
                messages = ApiHelper.getMessages(userID, doctorID, getContext(), userInfo.getUserType());
                if (messages != null) {
                    if (getActivity() != null)
                        // if arraylist have message set data in adapter and save in database and request message seen
                        HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isStoragePermissionGranted();
                                pbar_loading.setVisibility(View.GONE);
                            }
                        });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (Message message : messages) {
                                // update satabase
                                messageRepositry.createOrUpate(message);
                            }
                        }
                    }).start();
                    // request seen all msg
                    if (messages.size() > 0)
                        messageSeen(messages.get(messages.size() - 1).getMessageID().toString());

                } else {
                    if (getActivity() != null)
                        HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages = new ArrayList<>();
                                setData();
                                pbar_loading.setVisibility(View.GONE);

                            }
                        });
                }
            }
        }).start();
    }

    private void loadDocumentOnline() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                documents = ApiHelper.postGetDocumentList(userID, getContext());
                if (documents != null) {
                    // if arraylist have message set data in adapter and save in database and request message seen
                    HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isStoragePermissionGranted();
                            pbar_loading.setVisibility(View.GONE);

                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (Document document : documents) {
                                // update satabase
                                documentRepositry.createOrUpate(document);
                            }
                        }
                    }).start();
                } else {
                    HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            documents = new ArrayList<>();
                            setData();
                            pbar_loading.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();
    }

    private void loadChatOffline() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                messages = (ArrayList) messageRepositry.getAllMessageChat(doctorID);
                isStoragePermissionGranted();
                pbar_loading.setVisibility(View.GONE);
            }
        });
    }

    private void loadDocumentOffline() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                documents = (ArrayList) documentRepositry.getAllDocumentChat();
                isStoragePermissionGranted();
                pbar_loading.setVisibility(View.GONE);
            }
        });
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
                }
            }
        }
    }

    private void setData() {
        if (getActivity() == null)
            return;
        if (userID == doctorID && userType!=UserInfo.CLINIC ) {
            documentChatAdapter = new DocumentChatAdapter(documents, getActivity(), true);
            recyclerView.setAdapter(documentChatAdapter);
            recyclerView.scrollToPosition(documents.size() - 1);
        } else {
            chatAdapter = new ChatAdapter(messages, getActivity());
            recyclerView.setAdapter(chatAdapter);
            recyclerView.scrollToPosition(messages.size() - 1);
        }
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


    @OnClick(R.id.img_send_txt)
    public void img_send_txt() {

        if (etMessage.getText().toString().trim().isEmpty())
            return;
        // declare object and set attribute
        if (userID == doctorID && userType!=UserInfo.CLINIC) {
            chatHelper.sendTextDocument(etMessage.getText().toString(), documentChatAdapter, documents, recyclerView, userID, getActivity());
        } else {
            chatHelper.sendTextMessage(etMessage.getText().toString(), messages, userID, doctorID, chatAdapter, getActivity(), recyclerView, userInfo.getUserType());
        }
        etMessage.setText("");
        etMessage.setHint(R.string.write_a_message);

        /*    if (userMe.getUserType()==UserInfo.DOCTOR  && userInfo.getUserType()==UserInfo.PATIENT && userInfo.getIsSessionOpen() == 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int requestId = ApiHelper.openSession(getActivity(), String.valueOf(PrefHelper.get(getActivity(),PrefHelper.KEY_USER_ID,-1)), String.valueOf(PrefHelper.get(getActivity(),PrefHelper.KEY_IS_DOCTOR,false)));
                        Log.i("requestID:" , requestId+"");
                        if (requestId != -1) {
                            imgbtn_chat_attach.setEnabled(true);
                            mHoldingButtonLayout.setButtonEnabled(true);
                            // img_requestpermission.setEnabled(true);
                            userInfo.setIsSessionOpen(1);
                            checkSessionOpen(iamDoctor);
                            chatHelper.sendTextMessage(etMessage.getText().toString(), messages, userID, doctorID, chatAdapter, getActivity(), recyclerView);
                        }else{
                            imgbtn_chat_attach.setEnabled(false);
                            mHoldingButtonLayout.setButtonEnabled(false);
                            Toast.makeText(getContext(), R.string.message_not_send, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
*/
        //request
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
                            type = Message.MESSAGE_TYPE_VIDEO;
                        else {
                            Toast.makeText(getActivity(), R.string.this_file_is_not_supported, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (userID == doctorID && userType!=UserInfo.CLINIC) {
                            chatHelper.sendMediaDocument(new File(ImageFilePath.getPath(getActivity(), data.getData())), type, "", documentChatAdapter, documents, recyclerView, getActivity(), userID);
                        } else {
                            chatHelper.sendMediaMessage(new File(ImageFilePath.getPath(getActivity(), data.getData())), type, "", getActivity(), userID, doctorID, messages, chatAdapter, recyclerView, userInfo.getUserType());
                        }
                    } catch (Exception e) {
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
                chatHelper.takeAndSelectImage(PickerBuilder.SELECT_FROM_CAMERA, HttpChatFragment.this, getActivity());
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;
            }
        });
        getActivity().findViewById(R.id.img_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatHelper.takeAndSelectImage(PickerBuilder.SELECT_FROM_GALLERY, HttpChatFragment.this, getActivity());
                attachment.setVisibility(View.INVISIBLE);
                showAttachmentDialog = false;
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
       /* if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForLocationPermission();
            return;
        }*/
        Location location = getLocation();
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            if (userID == doctorID && userType!=UserInfo.CLINIC) {
                chatHelper.sendLocationDocument(longitude, latitude, documentChatAdapter, getActivity(), userID, documents, recyclerView);
            } else {
                chatHelper.sendLocationMessage(longitude, latitude, userID, doctorID, getActivity(), messages, chatAdapter, recyclerView, userInfo.getUserType());
            }
        } else {
            Toast.makeText(getActivity(), R.string.cant_find_location, Toast.LENGTH_SHORT).show();
        }

    }

    /*private Location getLastKnownLocation() {

        LocationManager mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
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
    }*/
    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askForLocationPermission();
        } else {
            try {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isNetworkEnabled) {
                    if (myLocation != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                        return myLocation;
                    } else if (locationManager != null) {
                        myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        return myLocation;
                    }
                } else if (isGPSEnabled) {
                    if (myLocation == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        return myLocation;
                    } else if (locationManager != null && myLocation != null) {
                        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        return myLocation;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myLocation;
    }

    private void askForLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        requestPermissions(permission, Constants.LAST_LOCATION_PERMISSION_CODE);
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
        if (userID != doctorID|| (userID == doctorID&& userType == UserInfo.CLINIC)) {
            chatRunning = true;
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver),
                    new IntentFilter("MyData"));
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (userID != doctorID || (userID == doctorID&& userType == UserInfo.CLINIC)) {
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
                if (message.getFromID() == doctorID) {
                    //messageRepositry.createOrUpate(message);
                    chatHelper.creatRealMessage(message, -1, messages, chatAdapter, recyclerView);

                    messageSeen(message.getMessageID().toString());
                    // seen request for specific msg
                } else {
                    // show notification
                    messagesDeliver(message.getMessageID().toString());
                }

            } else if (notificationType == 4) {
                // doctor.setIsOpen(0);
                // checkSessionOpen(iamDoctor);
                //  Toast.makeText(context, "Session Closed", Toast.LENGTH_SHORT).show();
                Message message = (Message) intent.getSerializableExtra("extra");
                if ((message.getFromID() == userID || message.getFromID() == doctorID) && (message.getToID() == userID || message.getToID() == doctorID)) {
                    userInfo.setIsSessionOpen(0);
                    //chatModelRepositry.createOrUpdate(userInfo);
                    checkSessionOpen(userMe.getUserType() == UserInfo.DOCTOR);
                    getActivity().invalidateOptionsMenu();

                }
            } else if (notificationType == 2) {
                int id = Integer.parseInt(intent.getStringExtra("extra"));
                if (doctorID != id)
                    return;
                //handle Deliverd Message here
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Message message : messages) {
                            if (message.getStatus() == 1) {
                                int index = messages.indexOf(message);
                                message.setStatus(2);
                                messages.set(index, message);
                                chatAdapter.setList(messages);
                                chatAdapter.notifyDataSetChanged();
                                // messageRepositry.createOrUpate(message);
                            }
                        }
                    }
                });
            } else if (notificationType == 3) {
                //handle seen Message here
                int id = Integer.parseInt(intent.getStringExtra("extra"));
                if (doctorID != id)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Message message : messages) {
                            if (message.getStatus() == 1 || message.getStatus() == 2) {
                                int index = messages.indexOf(message);
                                message.setStatus(3);
                                messages.set(index, message);
                                chatAdapter.setList(messages);
                                chatAdapter.notifyDataSetChanged();
                                //messageRepositry.createOrUpate(message);
                            }
                        }
                    }
                });
            }


        }
    };


    private void checkSessionOpen(final boolean iamDoctor) {

        if (doctorID == userID && userInfo.getUserType()!= UserInfo.CLINIC) {
            // i talk with my self in document
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
        } else if (iamDoctor && userInfo.getUserType() == UserInfo.PATIENT) {
            // i chat with client and sssion closed
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
            if (userInfo.getIsSessionOpen() == 0) {
                imgbtn_chat_attach.setEnabled(false);
                mHoldingButtonLayout.setButtonEnabled(false);
                // img_requestpermission.setEnabled(false);
                etMessage.setHint(R.string.write_a_message);
            }
//        } else if (userInfo.getUserType() == UserInfo.CLINIC) {
//            // client chat with clinics
//            mHoldingButtonLayout.setVisibility(View.VISIBLE);
//            open_chat_session.setVisibility(View.GONE);

        } else if (userInfo.getIsSessionOpen() == 0) {
            // client chat with doctor and session closed
            mHoldingButtonLayout.setVisibility(View.INVISIBLE);
            open_chat_session.setVisibility(View.VISIBLE);


        } else {
            // client chat with doctor and session opened
            mHoldingButtonLayout.setVisibility(View.VISIBLE);
            open_chat_session.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        if ((doctorID == 1&&userInfo.getUserType()!=UserInfo.CLINIC) || userInfo.getIsSessionOpen() == 0) {
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
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // i need request ID
                                    final boolean result = ApiHelper.closeSession(getContext(), userInfo.getRequestID(), userInfo.getUserType());
                                    HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (result) {
                                                // success
                                                Toast.makeText(getActivity(), R.string.session_ended, Toast.LENGTH_SHORT).show();
                                                userInfo.setIsSessionOpen(0);
                                                //userRepository.update(doctor);
                                                checkSessionOpen(userMe.getUserType() == UserInfo.DOCTOR);
                                                getActivity().invalidateOptionsMenu();
                                                if (userInfo.getUserType() == UserInfo.CLINIC) {
                                                    Intent intent = new Intent(getActivity(), InquiryActivity.class);
                                                    Gson gson = new Gson();
                                                    intent.putExtra("doctor_data", gson.toJson(userInfo));
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else if (userInfo.getUserType() == UserInfo.DOCTOR) {
                                                    openPayment();
                                                } else {
                                                    Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.cant_start_session), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }).start();
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
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // i need request ID
                                    final boolean result = ApiHelper.closeSession(getContext(), userInfo.getRequestID(), userInfo.getUserType());
                                    HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (result) {
                                                // success
                                                // we must handle this
                                                Toast.makeText(getActivity(), R.string.session_ended, Toast.LENGTH_SHORT).show();
                                                userInfo.setIsSessionOpen(0);
                                                checkSessionOpen(userMe.getUserType() == UserInfo.DOCTOR);
                                                canRate.setVisibility(View.GONE);
                                                getActivity().invalidateOptionsMenu();
                                                if (userInfo.getUserType() == UserInfo.DOCTOR || userInfo.getUserType() == UserInfo.CLINIC) {
                                                    if (userMe.getUserType() == UserInfo.PATIENT) {
                                                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                                                        adb.setTitle(R.string.rate_conversation);
                                                        adb.setCancelable(false);
                                                        adb.setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                Intent intent = new Intent(getActivity(), Comment.class);
//                                                            intent.putExtra("doc_id", String.valueOf(userInfo.getUserID()));
//                                                            intent.putExtra("request_id", String.valueOf(userInfo.getRequestID()));
                                                                intent.putExtra("user_info", userInfo);
                                                                startActivity(intent);
                                                                getActivity().finish();
                                                            }
                                                        });
                                                        adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                canRate.setVisibility(View.VISIBLE);

                                                            }
                                                        });
                                                        adb.show();
                                                    }
                                                }
                                            } else {
                                                // failed
                                                Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.cant_close_session), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }).start();


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
            if (userInfo.getUserType() == UserInfo.CLINIC) {
                Intent intent = new Intent(getActivity(), InquiryActivity.class);
                Gson gson = new Gson();
                intent.putExtra("doctor_data", gson.toJson(userInfo));
                startActivity(intent);
                getActivity().finish();
            } else {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                Gson gson = new Gson();
                PrefHelper.put(getContext(), PrefHelper.KEY_USER_INTENT, gson.toJson(userInfo));
                intent.putExtra("doctor_obj", userInfo);
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
            if (userID == doctorID && userType!=UserInfo.CLINIC) {
                loadDocumentOnline();
            } else {
                loadChatOnline();
            }
            imgbtn_chat_attach.setEnabled(true);
            etMessage.setEnabled(true);
            mHoldingButtonLayout.setButtonEnabled(true);
            img_send_txt.setEnabled(true);
            button2.setEnabled(true);
            button2.setText(R.string.start_new_request);
            etMessage.setHint(R.string.write_message);
            start_record.setEnabled(true);
            if (userInfo != null)
                checkSessionOpen(userMe.getUserType() == UserInfo.DOCTOR);
        } else {
            if (userID == doctorID&& userType!=UserInfo.CLINIC) {
                loadDocumentOffline();
            } else {
                loadChatOffline();
            }
            mHoldingButtonLayout.setButtonEnabled(false);
            imgbtn_chat_attach.setEnabled(false);
            etMessage.setEnabled(false);
            img_send_txt.setEnabled(false);
            button2.setEnabled(false);
            button2.setText(R.string.offline_mode_canot_contact_with_doctor);
            etMessage.setHint(R.string.offline_mode_canot_contact_with_doctor);
            start_record.setEnabled(false);
        }
    }


    private void messageSeen(final String msgID) {
        // i sent request to make my msg seen
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiHelper.seenMessage(getContext(), userID, String.valueOf(doctorID), msgID, userInfo.getUserType());
            }
        }).start();

    }

    private void messagesDeliver(final String msgID) {
        // i sent request to make my msg deliver
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiHelper.deliveredMessgae(getContext(), userID, String.valueOf(doctorID), msgID, userInfo.getUserType());
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatAdapter != null)
            chatAdapter.clearSelected();
        UserInfoRepositry userInfoRepositry = new UserInfoRepositry(getContext());
        userInfoRepositry.getDoctor(doctorID);
    }

    @OnClick(R.id.img_back)
    public void back() {
        getActivity().finish();
    }

    @OnClick({R.id.img_chat_user_avatar, R.id.tv_chat_user_name})
    public void openProfile() {
        if (userInfo != null && (userInfo.getUserType() == UserInfo.DOCTOR)) {
            Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
            intent.putExtra("doctor_data", userInfo);
            getActivity().startActivity(intent);
        } else if (userInfo != null && (userInfo.getUserType() == UserInfo.CLINIC)) {
            // this object is user and should open ProfileActivity
            Intent intent = new Intent(getActivity(), ClinicProfileActivity.class);
            intent.putExtra("clinic_data", userInfo);
            getActivity().startActivity(intent);

        }
    }


    @Override
    public void onBeforeExpand() {
        if (onBeforeExpand == false) {
            onBeforeExpand = true;
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
            invalidateTimer();
        }

    }

    @Override
    public void onExpand() {
        onBeforeExpand = false;
        expand = true;
        // if (Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

        // }
    }

    @Override
    public void onBeforeCollapse() {
        onBeforeExpand = false;
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
        onBeforeExpand = false;
        if (expand) {
            expand = false;
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
                            if (System.currentTimeMillis() - mStartTime > 1000) {
                                stopRecording(true);
//            Toast.makeText(getActivity(), "Recording submitted! Time " + getFormattedTime(), Toast.LENGTH_SHORT).show();

                                if (userID == doctorID && userType!=UserInfo.CLINIC) {
                                    chatHelper.sendMediaDocument(mOutputFile, Message.MESSAGE_TYPE_AUDIO, "", documentChatAdapter, documents, recyclerView, getActivity(), userID);
                                } else {
                                    chatHelper.sendMediaMessage(mOutputFile, Message.MESSAGE_TYPE_AUDIO, "", getActivity(), userID, doctorID, messages, chatAdapter, recyclerView, userInfo.getUserType());
                                }

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
                if (timeDifference > 1000 && flagLongPress == false) {
                    flagLongPress = true;

                    if (Build.VERSION.SDK_INT < 23 || (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

                        mStartTime = System.currentTimeMillis();
                        mTime.setVisibility(View.VISIBLE);
                        mSlideToCancel.setVisibility(View.VISIBLE);
                        mTime.setText(getFormattedTime());
                        startRecording();


                    } else {
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

        try {
            long startTimeForRecording = mStartTime;
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(16);
            mRecorder.setAudioSamplingRate(44100);
            mOutputFile = getOutputFile();
            mOutputFile.getParentFile().mkdirs();
            mRecorder.setOutputFile(mOutputFile.getAbsolutePath());
            mRecorder.prepare();
            mRecorder.start();
            //startTime = SystemClock.elapsedRealtime();
            //mHandler.postDelayed(mTickExecutor, 100);
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

        if (userID == doctorID && userType!=UserInfo.CLINIC) {
            chatHelper.sendMediaDocument(new File(ImageFilePath.getPath(getActivity(), uri)), Message.MESSAGE_TYPE_IMAGE, "", documentChatAdapter, documents, recyclerView, getActivity(), userID);
        } else {
            chatHelper.sendMediaMessage(new File(ImageFilePath.getPath(getActivity(), uri)), Message.MESSAGE_TYPE_IMAGE, "", getActivity(), userID, doctorID, messages, chatAdapter, recyclerView, userInfo.getUserType());
        }
    }

    private void setDatainToolbar() {
        HttpChatFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
