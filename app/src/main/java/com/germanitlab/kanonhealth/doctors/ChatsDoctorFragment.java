package com.germanitlab.kanonhealth.doctors;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.ChatListAdapter;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.ChatModel;
import com.germanitlab.kanonhealth.api.models.UserInfo;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.intro.StartQrScan;
import com.germanitlab.kanonhealth.ormLite.ChatModelRepository;
import com.germanitlab.kanonhealth.ormLite.UserRepository;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

// Edit By Ahmed 29-5-2017

public class ChatsDoctorFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private EditText edtFilter;
    private int type=0;
    //    private ImageButton imgScan;

    /*
    private TextView filter_to_list;
*/

    ArrayList<ChatModel> chatModel;
    private Button btnLeftList, btnRightList;
    UserInfo user;
    Gson gson;
    private UserRepository mDoctorRepository;
    static ChatsDoctorFragment chatsDoctorFragment;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    LinearLayoutManager llm;

    private static boolean leftTabVisible = true;
    ChatModelRepository chatModelRepository;

    public static ChatsDoctorFragment newInstance() {
        if (chatsDoctorFragment == null)
            chatsDoctorFragment = new ChatsDoctorFragment();
        return chatsDoctorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_chats_doctor, container, false);
            setHasOptionsMenu(true);
            try {
                user = new Gson().fromJson(PrefHelper.get(getContext(), PrefHelper.KEY_USER_KEY, ""), UserInfo.class);
            } catch (Exception e) {
            }
            initView();
            gson = new Gson();
            chatModel = new ArrayList<>();
            chatModelRepository=new ChatModelRepository(getContext());
            //mDoctorRepository = new UserRepository(getContext());

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_search:
                if (edtFilter.getVisibility() == View.GONE)
                    edtFilter.setVisibility(View.VISIBLE);
                else if (edtFilter.getVisibility() == View.VISIBLE)
                    edtFilter.setVisibility(View.GONE);
                break;
            case R.id.mi_scan:
                if (!checkPermissionForCamera()) {
                    requestPermissionForCamera();
                } else {
                    startActivity(new Intent(getActivity(), StartQrScan.class));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (leftTabVisible) {
            btnLeftList.setBackgroundResource(R.color.blue);
            btnLeftList.setTextColor(getResources().getColor(R.color.white));
            btnRightList.setBackgroundResource(R.color.gray);
            btnRightList.setTextColor(getResources().getColor(R.color.black));
        } else {
            btnRightList.setBackgroundResource(R.color.blue);
            btnRightList.setTextColor(getResources().getColor(R.color.white));
            btnLeftList.setBackgroundResource(R.color.gray);
            btnLeftList.setTextColor(getResources().getColor(R.color.black));
        }
        getChatData();
    }

    public void requestPermissionForCamera() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startActivity(new Intent(getActivity(), StartQrScan.class));
        }
    }

    public void getChatData() {
        if (leftTabVisible) {
            if (user.getUserType() == user.PATIENT) {
                type=0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Helper.isNetworkAvailable(getContext())) {
                            chatModel = ApiHelper.getChatDoctor(getContext(), String.valueOf(PrefHelper.get(getActivity(), PrefHelper.KEY_USER_ID, -1)));
                            if (chatModel==null || chatModel.size()==0)
                                return;
                            for(ChatModel item:chatModel) {
                                item.setUserType(UserInfo.DOCTOR);
                                if(item !=null)
                                    chatModelRepository.createDoctorOrUser(item);
                            }
                        }else {
                            chatModel = (ArrayList<ChatModel>) chatModelRepository.getDoctors();
                        }
                        setChatListAdapter(chatModel,type);
                    }
                }).start();

            } else {
                type=1;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Helper.isNetworkAvailable(getContext())) {
                            chatModel = ApiHelper.getChatUser(getContext(), String.valueOf(PrefHelper.get(getActivity(), PrefHelper.KEY_USER_ID, -1)));
                            if (chatModel==null || chatModel.size()==0)
                                return;
                            for(ChatModel item:chatModel) {
                                item.setUserType(UserInfo.PATIENT);
                                if(item !=null)
                                    chatModelRepository.createDoctorOrUser(item);
                            }
                        }else {
                            chatModel= (ArrayList<ChatModel>) chatModelRepository.getUsers();
                        }
                        setChatListAdapter(chatModel,type);
                    }
                }).start();
            }
        } else {
            if (user.getUserType() == user.PATIENT) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Helper.isNetworkAvailable(getContext())) {
                            chatModel = ApiHelper.getChatClinic(getContext(), String.valueOf(PrefHelper.get(getActivity(), PrefHelper.KEY_USER_ID, -1)));
                            if (chatModel==null || chatModel.size()==0)
                                return;
                            for(ChatModel item:chatModel) {
                                item.setUserType(UserInfo.CLINIC);
                                if(item !=null)
                                    chatModelRepository.createClinic(item);
                            }
                        }else {
                            chatModel= (ArrayList<ChatModel>) chatModelRepository.getClinics();
                        }
                        setChatListAdapter(chatModel,0);
                    }
                }).start();
            } else {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Helper.isNetworkAvailable(getContext())) {
                            chatModel = ApiHelper.getChatAnother(getContext(), String.valueOf(PrefHelper.get(getActivity(), PrefHelper.KEY_USER_ID, -1)));
                            if (chatModel==null || chatModel.size()==0)
                                return;
                            for(ChatModel item:chatModel) {
                                if(item.getId()>0) {
                                    item.setUserType(UserInfo.CLINIC);
                                    if(item !=null)
                                        chatModelRepository.createClinic(item);
                                }
                                else if (item.getUserID()>0) {
                                    item.setUserType(UserInfo.DOCTOR);
                                    if(item !=null)
                                        chatModelRepository.createDoctorOrUser(item);
                                }
                                else
                                    return;
                            }
                        }else {
                            chatModel= (ArrayList<ChatModel>) chatModelRepository.getAnother();
                        }
                        setChatListAdapter(chatModel,0);
                    }
                }).start();
            }
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            showDialog(scanResult.getContents());
        } else {
            Toast.makeText(getActivity(), R.string.invalid_qr_please_try_again, Toast.LENGTH_LONG).show();
        }
    }

    public void showDialog(final String qr) {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.custom_dialog);

        Button btnLogin = (Button) dialog.findViewById(R.id.btn_login);

        Button btnShowProfile = (Button) dialog.findViewById(R.id.btn_show_profile);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new HttpCall(getActivity(), new ApiResponse() {
//                    @Override
//                    public void onSuccess(Object response) {
//
//                        Log.d("Response", response.toString());
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailed(String error) {
//                        Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
//                        Log.d("Response", error);
//                        dialog.dismiss();
//                    }
//                }).login(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD), qr);
//            }
//        });


        btnShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

//    private void CheckTabToScrollTo() {
//        if (type == User.DOCTOR_TYPE) {
//            scrollToPosition(firstVisibleItemPositionForLeftTab);
//        } else {
//            scrollToPosition(firstVisibleItemPositionForRightTab);
//        }
//    }

//    private void scrollToPosition(int mScrollPosition) {
//        try {
//            recyclerView.scrollToPosition(mScrollPosition);
//        } catch (Exception e) {
//        }
//
//    }

//    private void updateDatabase(List<User> doctorList) {
//        for (User user : doctorList) {
//            User temp = mDoctorRepository.getDoctor(user);
//            if (temp != null) {
//                user.setIs_chat(temp.getIs_chat());
//            }
//            mDoctorRepository.create(user);
//        }
//    }


    private void setChatListAdapter(List<ChatModel> chatModels,int type) {
        if (chatModels != null) {
            final ChatListAdapter chatListAdapter = new ChatListAdapter(chatModels, getActivity(),type);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(chatListAdapter);
                }
            });
        }
    }


//    @Override
//    public void onPause() {
//        if (type == User.DOCTOR_TYPE) {
//            firstVisibleItemPositionForLeftTab = getScrolled();
//        } else {
//            firstVisibleItemPositionForRightTab = getScrolled();
//        }
//        super.onPause();
//    }

    private void initView() {
        btnLeftList = (Button) view.findViewById(R.id.doctor_list);
        btnRightList = (Button) view.findViewById(R.id.praxis_list);
        edtFilter = (EditText) view.findViewById(R.id.img_filter);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_doctor_list);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        edtFilter.setVisibility(View.GONE);

        if (user.getUserType() == user.PATIENT) {
            btnLeftList.setText(R.string.doctors);
            btnRightList.setText(R.string.practices);
        } else {
            btnLeftList.setText(R.string.clients);
            btnRightList.setText(R.string.others);
        }

        edtFilter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return true;
                }
                return false;
            }
        });

        edtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (chatModel != null) {
                    if (charSequence.toString().trim().length() > 0) {
                        List<ChatModel> fileDoctorResponses = new ArrayList<>();
                        for (int j = 0; j < chatModel.size(); j++) {
                            String name = chatModel.get(j).getFullName();
                            if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                fileDoctorResponses.add(chatModel.get(j));
                            }
                        }
                        if(leftTabVisible) {
                            setChatListAdapter(fileDoctorResponses, type);
                        }else
                        {
                            setChatListAdapter(fileDoctorResponses, 0);
                        }
                    } else
                    {
                        if(leftTabVisible) {
                            setChatListAdapter(chatModel, type);
                        }else {
                            setChatListAdapter(chatModel, 0);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRightList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftTabVisible = false;
                btnRightList.setBackgroundResource(R.color.blue);
                btnRightList.setTextColor(getResources().getColor(R.color.white));
                btnLeftList.setBackgroundResource(R.color.gray);
                btnLeftList.setTextColor(getResources().getColor(R.color.black));
                setChatListAdapter(new ArrayList<ChatModel>(),0);
                getChatData();

            }
        });
        btnLeftList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftTabVisible = true;
                btnRightList.setBackgroundResource(R.color.gray);
                btnRightList.setTextColor(getResources().getColor(R.color.black));
                btnLeftList.setBackgroundResource(R.color.blue);
                btnLeftList.setTextColor(getResources().getColor(R.color.white));
                setChatListAdapter(new ArrayList<ChatModel>(),0);
                getChatData();
            }
        });
/*
        filter_to_list = (TextView) view.findViewById(R.id.filter_to_list);
*/

//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

/*         filter_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("from", false);
                SpecialitiesFragment fragment = new SpecialitiesFragment();
                fragment.setArguments(bundle);
                new Helper(getActivity()).replaceFragments(fragment,
                        R.id.doctor_list_continer, "Specialty");
            }
        });*/


    }


}

