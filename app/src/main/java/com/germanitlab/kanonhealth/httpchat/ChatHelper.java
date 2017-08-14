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




    protected void takeAndSelectImage(int type, ParentFragment parentFragment,FragmentActivity fragmentActivity) {
        // type = PickerBuilder.SELECT_FROM_GALLERY or PickerBuilder.SELECT_FROM_CAMERA
        if (Build.VERSION.SDK_INT >= 23) {
            if (fragmentActivity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Helper.getCroppedImageFromCamera(parentFragment, fragmentActivity, type);
            } else
                parentFragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_PERMISSION_CODE);
        } else
            Helper.getCroppedImageFromCamera(parentFragment, fragmentActivity, type);
        //attachment.setVisibility(View.INVISIBLE);
        //showAttachmentDialog = false;
    }
    protected int creatDummyMessage(int userID,ArrayList<Message> messages,ChatAdapter chatAdapter,RecyclerView recyclerView) {
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
    protected int creatDummyDocument(DocumentChatAdapter documentChatAdapter,ArrayList<Document> documents,RecyclerView recyclerView) {
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
    protected void sendLocationMessage(double longitude, double latitude, int userID, int doctorID, final FragmentActivity fragmentActivity, final ArrayList<Message> messages, final ChatAdapter chatAdapter, final RecyclerView recyclerView) {
        //create dummy message
        final int index = creatDummyMessage(userID,messages,chatAdapter,recyclerView);
        final Message message = new Message();
        message.setFromID(userID);
        message.setToID(doctorID);
        message.setDateTime(getDateTimeNow());
        message.setMessage("{\"long\":" + longitude + ",\"lat\":" + latitude + "}");
        message.setType(Message.MESSAGE_TYPE_LOCATION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Message result=ApiHelper.sendMessage(message,null,fragmentActivity);
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result!=null){
                            // success
                            creatRealMessage(result, index,messages,chatAdapter,recyclerView);
                        }else{
                            // failed
                            removeDummyMessage(index,messages,recyclerView,chatAdapter);
                        }
                    }
                });
            }
        }).start();

    }

    // send Location Document
    protected void sendLocationDocument(double longitude, double latitude, final DocumentChatAdapter documentChatAdapter, final FragmentActivity fragmentActivity, final int userID, final ArrayList<Document>documents, final RecyclerView recyclerView) {
        //create dummy message
        final int index = creatDummyDocument(documentChatAdapter,documents,recyclerView);
        final Document document = new Document();
        document.setDateTime(getDateTimeNow());
        document.setDocument("{\"long\":" + longitude + ",\"lat\":" + latitude + "}");
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
                            creatRealDocument(result, index,documentChatAdapter,documents,recyclerView);
                        } else {
                            // failed
                            removeDummyDocument(index,documentChatAdapter,documents,recyclerView);
                        }
                    }
                });
            }
        }).start();
    }
    private void removeDummyMessage(int index,ArrayList<Message>messages, RecyclerView recyclerView,ChatAdapter chatAdapter) {
        messages.remove(index);
        chatAdapter.setList(messages);
        chatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messages.size() - 1);
    }
    private void removeDummyDocument(int index,DocumentChatAdapter documentChatAdapter ,ArrayList<Document>documents , final RecyclerView recyclerView) {
        documents.remove(index);
        documentChatAdapter.setList(documents);
        documentChatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(documents.size() - 1);
    }
    protected void creatRealMessage(Message message, int index, final ArrayList<Message> messages, final ChatAdapter chatAdapter,  final RecyclerView recyclerView) {
        try {
            if (index >= 0)
                messages.remove(index);
            messages.add(message);
            chatAdapter.setList(messages);
            chatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messages.size() - 1);
            //messageRepositry.createOrUpate(message);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    private void creatRealDocument(Document document, int index,DocumentChatAdapter documentChatAdapter, ArrayList<Document> documents, final RecyclerView recyclerView ) {
        try {
            if (index >= 0)
                documents.remove(index);
            documents.add(document);
            documentChatAdapter.setList(documents);
            documentChatAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(documents.size() - 1);
            //documentRepositry.createOrUpate(document);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    protected void sendTextMessage(String textMsg, final ArrayList<Message> messages, int userID, int doctorID, final ChatAdapter chatAdapter, final FragmentActivity fragmentActivity, final RecyclerView recyclerView){
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
                final Message result=ApiHelper.sendMessage(message,null,fragmentActivity);
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result!=null){
                            //success
                            fragmentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Message temp = messages.get(index);
                                    temp.setStatus(1);
                                    messages.set(index, temp);
                                    chatAdapter.setList(messages);
                                    chatAdapter.notifyDataSetChanged();
                                    recyclerView.scrollToPosition(messages.size() - 1);
                                }
                            });
                            //messageRepositry.createOrUpate(result);
                        }else {
                            // failed
                            removeDummyMessage(index,messages,recyclerView,chatAdapter);
                        }
                    }
                });
            }
        }).start();
    }
    protected void sendTextDocument(String textMsg, final DocumentChatAdapter documentChatAdapter, final ArrayList<Document> documents, final RecyclerView recyclerView, final int userID, final FragmentActivity fragmentActivity){
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
                    removeDummyDocument(index,documentChatAdapter,documents,recyclerView);
                }
            }
        }).start();
    }
    protected void sendMediaMessage(final File file, String type, String textMsg, final FragmentActivity fragmentActivity, int userID, int doctorID, final ArrayList<Message> messages, final ChatAdapter chatAdapter, final RecyclerView recyclerView){
        final int index = creatDummyMessage(userID,messages,chatAdapter,recyclerView);
        final Message message=new Message();
        message.setFromID(userID);
        message.setToID(doctorID);
        message.setMessage(textMsg);
        message.setType(type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Message result=ApiHelper.sendMessage(message,file,fragmentActivity);
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        removeDummyMessage(index,messages,recyclerView,chatAdapter);
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
                });
            }
        }).start();
    }
    protected void sendMediaDocument(final File file, String type, String textMsg, final DocumentChatAdapter documentChatAdapter, final ArrayList<Document> documents, final RecyclerView recyclerView, final FragmentActivity fragmentActivity, final int userID){
        final int index = creatDummyDocument(documentChatAdapter,documents,recyclerView);
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
                        removeDummyDocument(index,documentChatAdapter,documents,recyclerView);
                    }
                });
            }
        }).start();
    }
}
