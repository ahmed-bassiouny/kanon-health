package com.germanitlab.kanonhealth.httpchat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.Crop.PickerBuilder;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.api.ApiHelper;
import com.germanitlab.kanonhealth.api.models.Document;
import com.germanitlab.kanonhealth.api.models.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.helpers.Helper;
import com.germanitlab.kanonhealth.helpers.ParentFragment;
import com.germanitlab.kanonhealth.ormLite.HttpDocumentRepositry;
import com.germanitlab.kanonhealth.ormLite.HttpMessageRepositry;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bassiouny on 06/08/17.
 */

public class ChatHelper {

    Fragment fragment;
    FragmentActivity fragmentActivity;
    ArrayList<Message> messages;
    ArrayList<Document> documents;
    RecyclerView recyclerView;
    int userID;
    int doctorID;
    ChatAdapter chatAdapter;
    HttpMessageRepositry messageRepositry;
    HttpDocumentRepositry documentRepositry;

    public ChatHelper(Fragment fragment, FragmentActivity fragmentActivity, ArrayList<Message> messages, ArrayList<Document> documents,
                      RecyclerView recyclerView,int userID,int doctorID,ChatAdapter chatAdapter,
                      HttpMessageRepositry messageRepositry,HttpDocumentRepositry documentRepositry) {
        this.fragment = fragment;
        this.fragmentActivity = fragmentActivity;
        this.messages=messages;
        this.documents=documents;
        this.recyclerView=recyclerView;
        this.userID=userID;
        this.doctorID=doctorID;
        this.chatAdapter=chatAdapter;
        this.documentRepositry=documentRepositry;
        this.messageRepositry=messageRepositry;
    }

    protected void takeAndSelectImage(int type, ParentFragment parentFragment) {
        // type = PickerBuilder.SELECT_FROM_GALLERY or PickerBuilder.SELECT_FROM_CAMERA
        if (Build.VERSION.SDK_INT >= 23) {
            if (fragmentActivity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Helper.getCroppedImageFromCamera(parentFragment, fragmentActivity, type);
            } else
                fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_PERMISSION_CODE);
        } else
            Helper.getCroppedImageFromCamera(parentFragment, fragmentActivity, type);
        //attachment.setVisibility(View.INVISIBLE);
        //showAttachmentDialog = false;
    }
    protected int creatDummyMessage() {
        Message message = new Message();
        message.setDateTime(getDateTimeNow());
        message.setType(Message.MESSAGE_TYPE_UNDEFINED);
        message.setFromID(userID);
        messages.add(message);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        int index = messages.size() - 1;
        recyclerView.scrollToPosition(index);
        return index;
    }
    protected int creatDummyDocument(DocumentChatAdapter documentChatAdapter) {
        Document document=new Document();
        document.setDateTime(getDateTimeNow());
        document.setType(Message.MESSAGE_TYPE_UNDEFINED);
        documents.add(document);
        documentChatAdapter.setList(documents);
        documentChatAdapter.notifyDataSetChanged();
        int index = documents.size() - 1;
        recyclerView.scrollToPosition(index);
        return index;
    }
    private String getDateTimeNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime()).toString();
    }

    // send Location Message
    protected void sendLocationMessage(double longitude, double latitude) {
        //create dummy message
        final int index = creatDummyMessage();
        final Message message = new Message();
        message.setFromID(userID);
        message.setToID(doctorID);
        message.setDateTime(getDateTimeNow());
        message.setMedia("{\"long\":" + longitude + ",\"lat\":" + latitude + "}");
        message.setType(Message.MESSAGE_TYPE_LOCATION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message result=ApiHelper.sendMessage(message,null,fragmentActivity);
                if(result!=null){
                    // success
                    creatRealMessage(result, index);
                }else{
                    // failed
                    removeDummyMessage(index);
                }
            }
        }).start();

    }

    // send Location Document
    protected void sendLocationDocument(double longitude, double latitude, final DocumentChatAdapter documentChatAdapter) {
        //create dummy message
        final int index = creatDummyDocument(documentChatAdapter);
        final Document document = new Document();
        document.setDateTime(getDateTimeNow());
        document.setMedia("{\"long\":" + longitude + ",\"lat\":" + latitude + "}");
        document.setType(Message.MESSAGE_TYPE_LOCATION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Document result = ApiHelper.postAddDocument(userID, document, null, fragmentActivity);
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            // success
                            creatRealDocument(result, index,documentChatAdapter);
                        } else {
                            // failed
                            removeDummyDocument(index,documentChatAdapter);
                        }
                    }
                });
            }
        }).start();
    }
    private void removeDummyMessage(int index) {
        messages.remove(index);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size() - 1);
    }
    private void removeDummyDocument(int index,DocumentChatAdapter documentChatAdapter) {
        documents.remove(index);
        documentChatAdapter.setList(documents);
        documentChatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(documents.size() - 1);
    }
    protected void creatRealMessage(Message message, int index) {
        try {
            if (index >= 0)
                messages.remove(index);
            messages.add(message);
            chatAdapter.setList(messages);
            chatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messages.size() - 1);
            messageRepositry.createOrUpate(message);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    private void creatRealDocument(Document document, int index,DocumentChatAdapter documentChatAdapter) {
        try {
            if (index >= 0)
                documents.remove(index);
            documents.add(document);
            documentChatAdapter.setList(documents);
            documentChatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(documents.size() - 1);
            documentRepositry.createOrUpate(document);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    protected void sendTextMessage(String textMsg){
        final Message message = new Message();
        message.setFromID(userID);
        message.setToID(doctorID);
        message.setMessage(textMsg);
        message.setType(Message.MESSAGE_TYPE_TEXT);
        message.setDateTime(getDateTimeNow());
        messages.add(message);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        final int index = messages.size() - 1;
        recyclerView.scrollToPosition(index);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message result=ApiHelper.sendMessage(message,null,fragmentActivity);
                if(result!=null){
                    //success
                    Message temp = messages.get(index);
                    temp.setStatus(1);
                    messages.set(index, temp);
                    chatAdapter.setList(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                    messageRepositry.createOrUpate(result);
                }else {
                    // failed
                    removeDummyMessage(index);
                }
            }
        }).start();
    }
    protected void sendTextDocument(String textMsg,DocumentChatAdapter documentChatAdapter){
        final Document document = new Document();
        document.setDocument(textMsg);
        document.setType(Message.MESSAGE_TYPE_TEXT);
        document.setPrivacy(0);
        document.setDateTime(getDateTimeNow());
        documents.add(document);
        documentChatAdapter.setList(documents);
        documentChatAdapter.notifyDataSetChanged();
        final int index = documents.size() - 1;
        recyclerView.scrollToPosition(index);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document result=ApiHelper.postAddDocument(userID,document,null,fragmentActivity);
                if(result!=null){
                    //success
                }else {
                    // failed
                    removeDummyMessage(index);
                }
            }
        }).start();
    }
    protected void sendMediaMessage(final File file, String type, String textMsg){
        final int index = creatDummyMessage();
        final Message message=new Message();
        message.setFromID(userID);
        message.setToID(doctorID);
        message.setMessage(textMsg);
        message.setType(type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message result=ApiHelper.sendMessage(message,file,fragmentActivity);
                removeDummyMessage(index);
                if(result !=null){
                    //success
                    messages.add(result);
                    chatAdapter.setList(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(index);
                }else{
                    // failed
                }
            }
        }).start();
    }
    protected void sendMediaDocument(final File file, String type, String textMsg, final DocumentChatAdapter documentChatAdapter){
        final int index = creatDummyDocument(documentChatAdapter);
        final Document document=new Document();
        document.setDocument(textMsg);
        document.setType(type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Document result=ApiHelper.postAddDocument(userID,document,file,fragmentActivity);
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result !=null){
                            //success
                            documents.add(result);
                            documentChatAdapter.setList(documents);
                            documentChatAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(index);
                        }else{
                            // failed
                            Toast.makeText(fragmentActivity, R.string.msg_not_send, Toast.LENGTH_SHORT).show();
                        }
                        removeDummyDocument(index,documentChatAdapter);
                    }
                });
            }
        }).start();
    }
}
