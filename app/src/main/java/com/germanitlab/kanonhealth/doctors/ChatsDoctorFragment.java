package com.germanitlab.kanonhealth.doctors;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.DoctorListAdapter;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.ormLite.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class ChatsDoctorFragment extends Fragment implements ApiResponse {

    private View view;
    private RecyclerView recyclerView;
    List<User> doctorList;

    private TextView tvLoadingError;
    private LinearLayout linearLayoutContent;
    private LinearLayout chat_layout;
    private PrefManager mPrefManager;
    Gson gson;
    private UserRepository mDoctorRepository;
    LinearLayoutManager llm;


    private EditText edtFilter;
    private Button doctors_list, praxis_list;
    private static ChatsDoctorFragment chatsDoctorFragment;
    Util util;
    PrefManager prefManager;

    public static ChatsDoctorFragment newInstance() {
        if (chatsDoctorFragment == null)
            chatsDoctorFragment = new ChatsDoctorFragment();
        return chatsDoctorFragment;
    }

    public ChatsDoctorFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (getView() != null && isVisibleToUser) {
                if (Helper.isNetworkAvailable(getContext())) {
                    new HttpCall(getActivity(), this).getChatDoctors(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD));
                } else {
                    doctorList = mDoctorRepository.getChat(1);
                    setAdapter(doctorList);
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getActivity(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            prefManager = new PrefManager(getContext());
            if (Helper.isNetworkAvailable(getContext())) {
                doctorList = mDoctorRepository.getChat(1);
                setAdapter(doctorList);
                new HttpCall(getActivity(), this).getChatDoctors(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD));
            } else {
                doctorList = mDoctorRepository.getChat(1);
                setAdapter(doctorList);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
        changeColors(R.color.blue, R.color.white, R.color.gray, R.color.black, doctors_list, praxis_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (view != null) {

                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {

                    parent.removeView(view);
                }
                return view;
            }
            util = Util.getInstance(getActivity());
            gson = new Gson();
            mPrefManager = new PrefManager(getActivity());
            mDoctorRepository = new UserRepository(getContext());
            view = inflater.inflate(R.layout.fragment_chats_doctor, container, false);
            initView();
            handelEvent();
            setHasOptionsMenu(true);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
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
                Helper.ImportQr(mPrefManager, getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    private void handelEvent() {
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
                if (doctorList != null) {

                    if (charSequence.toString().trim().length() > 0) {
                        List<User> fileDoctorResponses = new ArrayList<>();
                        for (int j = 0; j < doctorList.size(); j++) {

                            String name = doctorList.get(j).getLast_name() + ", " + doctorList.get(j).getFirst_name();

                            if (name != null && name.toLowerCase().contains(charSequence.toString().toLowerCase())) {

                                fileDoctorResponses.add(doctorList.get(j));
                            }
                        }
                        setAdapter(fileDoctorResponses);
                    } else setAdapter(doctorList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);

        downloadDialog.setTitle(title);

        downloadDialog.setMessage(message);

        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {

                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                try {

                    act.startActivity(intent);

                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(act, act.getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
                }

            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    private void initView() {
        praxis_list = (Button) view.findViewById(R.id.praxis_list);
        chat_layout = (LinearLayout) view.findViewById(R.id.chat_layout);
        doctors_list = (Button) view.findViewById(R.id.doctor_list);
        doctors_list.setBackgroundResource(R.color.blue);
        doctors_list.setTextColor(getResources().getColor(R.color.white));
        praxis_list.setBackgroundResource(R.color.gray);
        praxis_list.setTextColor(getResources().getColor(R.color.black));
//        toolbar = (Toolbar) view.findViewById(R.id.doctor_list_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        doctors_list = (Button) view.findViewById(R.id.doctor_list);
        praxis_list = (Button) view.findViewById(R.id.praxis_list);
//        myQr = (ImageButton) toolbar.findViewById(R.id.my_Qr);
//        imgbtnScan = (ImageButton) toolbar.findViewById(R.id.scan);
//        Helper.ImportQr(mPrefManager, getActivity(), myQr);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_doctor_list);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        tvLoadingError = (TextView) view.findViewById(R.id.tv_chats_doctor_error);
        linearLayoutContent = (LinearLayout) view.findViewById(R.id.linear_layout_content);


        edtFilter = (EditText) view.findViewById(R.id.img_filter);
        edtFilter.setVisibility(View.GONE);
        praxis_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorList = mDoctorRepository.getChat(2);
                setAdapter(doctorList);
                changeColors(R.color.blue, R.color.white, R.color.gray, R.color.black, praxis_list, doctors_list);
                if (Helper.isNetworkAvailable(getContext())) {
                    new HttpCall(getActivity(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            doctorList = (List<User>) response;
                            int m = getScrolled();
                            setAdapter(doctorList);
                            scrollToPosition(m);
                            updateDataBase(doctorList);
                            chat_layout.setVisibility(View.VISIBLE);
                            tvLoadingError.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailed(String error) {
//                            tvLoadingError.setVisibility(View.VISIBLE);
//                            if (error != null && error.length() > 0)
//                                tvLoadingError.setText(error);
//                            else tvLoadingError.setText("Some thing went wrong");
//                            chat_layout.setVisibility(View.GONE);
                        }
                    }).getChatClinics(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD));
                }
            }
        });
        doctors_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorList = mDoctorRepository.getChat(1);
                setAdapter(doctorList);
                changeColors(R.color.blue, R.color.white, R.color.gray, R.color.black, doctors_list, praxis_list);
                if (Helper.isNetworkAvailable(getContext())) {
                    new HttpCall(getActivity(), new ApiResponse() {
                        @Override
                        public void onSuccess(Object response) {
                            doctorList = (List<User>) response;
                            updateDataBase(doctorList);
                            int m = getScrolled();
                            setAdapter(doctorList);
                            scrollToPosition(m);
                            linearLayoutContent.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailed(String error) {
//                            tvLoadingError.setVisibility(View.VISIBLE);
//                            if (error != null && error.length() > 0)
//                                tvLoadingError.setText(error);
//                            else tvLoadingError.setText("Some thing went wrong");
                        }
                    }).getChatDoctors(prefManager.getData(PrefManager.USER_ID), prefManager.getData(PrefManager.USER_PASSWORD));
                }

            }
        });


    }

    public void changeColors(int backColorSelected, int textSelected, int backColorUnSelected, int textUnSelected, Button btnSelected, Button btnUnSelected) {
        btnUnSelected.setBackgroundResource(backColorUnSelected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btnUnSelected.setTextColor(getResources().getColor(textUnSelected, null));
            btnSelected.setTextColor(getResources().getColor(textSelected, null));
        } else {
            btnUnSelected.setTextColor(getResources().getColor(textUnSelected));
            btnSelected.setTextColor(getResources().getColor(textSelected));
        }
        btnSelected.setBackgroundResource(backColorSelected);
    }

    DoctorListAdapter doctorListAdapter;

    private void setAdapter(List<User> doctorList) {
        if (doctorList != null) {
            doctorListAdapter = new DoctorListAdapter(doctorList, getActivity(), view.VISIBLE, 3);
            recyclerView.setAdapter(doctorListAdapter);
        }
    }


    @Override
    public void onSuccess(Object response) {
        try {
            doctorList = (List<User>) response;
            Gson gson = new Gson();
            updateDataBase(doctorList);
            int m = getScrolled();
            setAdapter(doctorList);
            scrollToPosition(m);
            linearLayoutContent.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getContext(), getContext().getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private int getScrolled() {
        if (llm != null && llm instanceof LinearLayoutManager) {
            try {
                return llm.findFirstVisibleItemPosition();
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }

    }

    private void scrollToPosition(int mScrollPosition) {
        try {
            recyclerView.scrollToPosition(mScrollPosition);
        } catch (Exception e) {
        }

    }

    private void updateDataBase(List<User> doctorList) {
        for (User user : doctorList) {
            user.setIs_chat(1);
            mDoctorRepository.create(user);
        }
    }

    @Override
    public void onFailed(String error) {
//        linearLayoutContent.setVisibility(View.GONE);
//        tvLoadingError.setVisibility(View.VISIBLE);
//        if (error != null && error.length() > 0)
//            tvLoadingError.setText(error);
//        else tvLoadingError.setText("Some thing went wrong");
    }

}
