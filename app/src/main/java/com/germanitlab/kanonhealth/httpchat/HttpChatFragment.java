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
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.interfaces.ApiInterface;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.models.messages.Message;
import com.germanitlab.kanonhealth.models.user.User;

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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HttpChatFragment extends Fragment implements ApiResponse{


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
    // loca variable
    int userID=3;int userPassword=0;int doctorID=3;
    ArrayList<Message> messages;
    PrefManager prefManager;
    ChatAdapter chatAdapter;
    HashMap<UUID,Message> uuidMessageHashMap=new HashMap<>();



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
        loadChat(userID,doctorID);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayListMessageToHashmap();
                isStoragePermissionGranted();
            }
        });


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
        ArrayList<Message> temp =new ArrayList<>();
        temp.addAll(uuidMessageHashMap.values());
        chatAdapter = new ChatAdapter(temp,getActivity(),false);
        recyclerView.setAdapter(chatAdapter);
        pbar_loading.setVisibility(View.GONE);
        recyclerView.scrollToPosition(uuidMessageHashMap.size()-1);
    }

    // init text input
    @OnTextChanged(R.id.et_chat_message)
    public void changeText(){

        if(etMessage.getText().toString().trim().length()>0){
            img_send_audio.setVisibility(View.GONE);
            img_send_txt.setVisibility(View.VISIBLE);
        }else{
            img_send_audio.setVisibility(View.VISIBLE);
            img_send_txt.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.img_send_txt)
    public void img_send_txt(){
        // declare object and set attribute
        Message message = new Message();
        final UUID key = UUID.randomUUID();
        message.setUser_id(userID);
        message.setFrom_id(userID);
        message.setTo(doctorID);
        message.setMsg(etMessage.getText().toString());
        message.setType(Constants.TEXT);
        message.setIs_forward(1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        message.setSent_at(dateFormat.format(cal.getTime()).toString());
        etMessage.setText("");

        addChangeItemChat(key,message);


        //request
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                Message temp=uuidMessageHashMap.get(key);
                temp.setIs_forward(0);
                uuidMessageHashMap.put(key,temp);
                addChangeItemChat(key,temp);
            }

            @Override
            public void onFailed(String error) {

            }
        }).sendMessage(message);
    }
            //----- additional Method
    private void ArrayListMessageToHashmap(){
        for(Message message:messages)
            uuidMessageHashMap.put(UUID.randomUUID(),message);
    }
    private void addChangeItemChat(final UUID key, final Message message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uuidMessageHashMap.put(key,message);
                ArrayList<Message> temp =new ArrayList<>();
                temp.addAll(uuidMessageHashMap.values());
                chatAdapter.setList(temp);
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(uuidMessageHashMap.size()-1);
            }
        });
    }
}
