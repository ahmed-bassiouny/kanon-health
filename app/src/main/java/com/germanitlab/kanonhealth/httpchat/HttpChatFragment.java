package com.germanitlab.kanonhealth.httpchat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.interfaces.ApiInterface;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HttpChatFragment extends Fragment implements ApiResponse{

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
    ProgressDialog progressDialog;
    @BindView(R.id.chat_bar)
    LinearLayout chat_bar;
    @BindView(R.id.open_chat_session)
    LinearLayout open_chat_session;
    @BindView(R.id.pbar_loading)
    ProgressBar pbar_loading;
    //
    int userID,userPassword,doctorID;
    ArrayList<Message> messages;
    PrefManager prefManager;
    public HttpChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_http_chat, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadChat(3,3);
        initData();

    }

    // declare objects in this fragment
    private void initObjects(){
        prefManager = new PrefManager(getActivity());

    }

    // set data in object
    private void initData(){
        //userID=prefManager.getInt(PrefManager.USER_ID);
        //doctorID=getArguments().getInt("doctorID");

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
    }

    private void loadChat(int userID,int doctorID){
        MessageRequest messageRequest=new MessageRequest(userID,doctorID);
        new HttpCall(getActivity(),this).loadChat(messageRequest);
    }

    @Override
    public void onSuccess(Object response) {
        messages=(ArrayList<Message>)response;
        pbar_loading.setVisibility(View.GONE);
        isStoragePermissionGranted();
    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        pbar_loading.setVisibility(View.GONE);
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                setData();
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            setData();
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            setData();
        }
    }
    private void setData(){
        ChatAdapter chatAdapter = new ChatAdapter(messages,getActivity(),true);
        recyclerView.setAdapter(chatAdapter);
    }
}
