package com.germanitlab.kanonhealth.doctors;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.adapters.DoctorListAdapter;
import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.chat.ChatActivity;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.Util;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.models.user.User;
import com.germanitlab.kanonhealth.payment.PaymentActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class ChatsDoctorFragment extends Fragment implements ApiResponse {

    private View view;
    private RecyclerView recyclerView;
    private ImageButton myQr, imgbtnScan;
    List<User> doctorList;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    private TextView tvLoadingError;
    private LinearLayout linearLayoutContent;
    private LinearLayout chat_layout ;
    private PrefManager mPrefManager;
    Gson gson ;

    private EditText edtFilter;
    private Button doctors_list, praxis_list;
    private static ChatsDoctorFragment chatsDoctorFragment;
    Util util ;

    public static ChatsDoctorFragment newInstance(){
        if(chatsDoctorFragment==null)
            chatsDoctorFragment=new ChatsDoctorFragment();
        return chatsDoctorFragment;
    }

    public ChatsDoctorFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getView()!=null &&isVisibleToUser ){
            if(Helper.isNetworkAvailable(getContext())) {
//                util.showProgressDialog();
                new HttpCall(getActivity(), this).getChatDoctors(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                        , AppController.getInstance().getClientInfo().getPassword());
            }
            else {
                TypeToken<List<User>> token = new TypeToken<List<User>>(){};
                doctorList = gson.fromJson(mPrefManager.getData(PrefManager.CHAT_LIST) ,  token.getType());;
                setAdapter(doctorList);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


        view = inflater.inflate(R.layout.fragment_chats_doctor, container, false);

        initView();
        handelEvent();

        setHasOptionsMenu(true);
        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    private void handelEvent() {


/*        myQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onImgDoctorListMapClick.OnImgDoctorListMapClick();
//                Intent intent = new Intent(getActivity(), DoctorMapActivity.class);
//                startActivity(intent);
            }
        });*/

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {

                PrefManager prefManager = new PrefManager(getActivity());
                Gson gson = new Gson();
                prefManager.put(PrefManager.DOCTOR_KEY, gson.toJson(doctorList.get(position)));
                if (mPrefManager.get(mPrefManager.IS_DOCTOR )|| doctorList.get(position).getIsOpen() == 1) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("doctor_data", gson.toJson(doctorList.get(position)));
                    intent.putExtra("from", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("doctor_data", gson.toJson(doctorList.get(position)));
                    startActivity(intent);
                }
            }

            @Override
            public void onClick(Object object) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        //---------- Camera
//        imgbtnScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), StartQrScan.class));
//            }
//        });

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

                            String name = doctorList.get(j).getName();

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

    @Override
    public void onPause() {
        super.onPause();

    }



    public void scanQrCode() {

        //call scan qr code
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setResultDisplayDuration(0);
        integrator.initiateScan();
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

                } catch (ActivityNotFoundException anfe) {

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
        praxis_list = (Button)view.findViewById(R.id.praxis_list);
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
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        tvLoadingError = (TextView) view.findViewById(R.id.tv_chats_doctor_error);
        linearLayoutContent = (LinearLayout) view.findViewById(R.id.linear_layout_content);


        edtFilter = (EditText) view.findViewById(R.id.img_filter);
        praxis_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.showProgressDialog();
                doctors_list.setBackgroundResource(R.color.gray);
                doctors_list.setTextColor(getResources().getColor(R.color.black));
                praxis_list.setBackgroundResource(R.color.blue);
                praxis_list.setTextColor(getResources().getColor(R.color.white));
                new HttpCall(new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        util.dismissProgressDialog();
                        doctorList = (List<User>) response;
                        setAdapter(doctorList);
                        chat_layout.setVisibility(View.VISIBLE);
                        tvLoadingError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailed(String error) {
                        tvLoadingError.setVisibility(View.VISIBLE);
                        util.dismissProgressDialog();
                        if (error != null && error.length() > 0)
                            tvLoadingError.setText(error);
                        else tvLoadingError.setText("Some thing went wrong");
                        chat_layout.setVisibility(View.GONE);
                    }
                }).getChatClinics(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                        , AppController.getInstance().getClientInfo().getPassword());
            }
        });
        doctors_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.showProgressDialog();
                doctors_list.setBackgroundResource(R.color.blue);
                doctors_list.setTextColor(getResources().getColor(R.color.white));
                praxis_list.setBackgroundResource(R.color.gray);
                praxis_list.setTextColor(getResources().getColor(R.color.black));
                new HttpCall(new ApiResponse() {
                    @Override
                    public void onSuccess(Object response) {
                        util.dismissProgressDialog();
                        doctorList = (List<User>) response;
                        Gson gson = new Gson();
                        String chat_list = gson.toJson(doctorList);
                        setAdapter(doctorList);
                        linearLayoutContent.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailed(String error) {
                        util.dismissProgressDialog();
                        tvLoadingError.setVisibility(View.VISIBLE);
                        if (error != null && error.length() > 0)
                            tvLoadingError.setText(error);
                        else tvLoadingError.setText("Some thing went wrong");
                    }
                }).getChatDoctors(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                        , AppController.getInstance().getClientInfo().getPassword());
            }
        });

    }

    DoctorListAdapter doctorListAdapter;

    private void setAdapter(List<User> doctorList) {
        if (doctorList != null) {
            doctorListAdapter = new DoctorListAdapter(doctorList, getActivity() , view.VISIBLE,3);
            recyclerView.setAdapter(doctorListAdapter);
        }
    }


    @Override
    public void onSuccess(Object response) {
//        util.dismissProgressDialog();
        doctorList = (List<User>) response;
        Gson gson = new Gson();
        String jsonData = gson.toJson(response);
        mPrefManager.put(PrefManager.CHAT_LIST , jsonData);
        setAdapter(doctorList);
        linearLayoutContent.setVisibility(View.VISIBLE);

    }

    @Override
    public void onFailed(String error) {
//        util.dismissProgressDialog();
        tvLoadingError.setVisibility(View.VISIBLE);
        if (error != null && error.length() > 0)
            tvLoadingError.setText(error);
        else tvLoadingError.setText("Some thing went wrong");
    }


}
