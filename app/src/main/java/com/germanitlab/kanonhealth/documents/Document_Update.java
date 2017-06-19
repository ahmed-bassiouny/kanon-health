package com.germanitlab.kanonhealth.documents;

import android.content.Intent;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.models.messages.Message;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Document_Update extends AppCompatActivity {
    @BindView(R.id.image_layout)
    LinearLayout image_layout ;
    @BindView(R.id.video_layout)
    LinearLayout video_layout ;
    @BindView(R.id.audio_layout)
    LinearLayout audio_layout ;
    @BindView(R.id.image_message)
    ImageView imageView ;
    @BindView(R.id.video_message)
    ImageView videoMessage ;
    @BindView(R.id.date)
    EditText date ;
    @BindView(R.id.category)
    EditText category ;
    @BindView(R.id.doctor)
    EditText doctor ;
    @BindView(R.id.diagnose)
    EditText diagnose ;
    @BindView(R.id.report)
    EditText report ;
    @BindView(R.id.comment)
    EditText comment ;
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_document__update);
            ButterKnife.bind(this);
            Intent intent = getIntent();
            String type = intent.getStringExtra("type");
            message =(Message) intent.getSerializableExtra("data");
            if(type.equals(Constants.IMAGE)) {
                image_layout.setVisibility(View.VISIBLE);
                Uri imageUri = Uri.fromFile(new File(message.getMsg()));
                Glide.with(this).load(imageUri).into(imageView);

            }
            else if (type == Constants.VIDEO) {
                video_layout.setVisibility(View.VISIBLE);
                videoMessage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(message.getMsg(),
                        MediaStore.Video.Thumbnails.MICRO_KIND));
            }
            else if(type == Constants.AUDIO) {
                audio_layout.setVisibility(View.VISIBLE);
            }
            date.setText(message.getDate());
            category.setText(message.getCategory());
            doctor.setText(message.getDoctor());
            diagnose.setText(message.getDiagnose());
            report.setText(message.getReport());
            comment.setText(message.getComment());
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(this, getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }



    }
    @OnClick(R.id.save)
    public void SaveData(View v) {
        message.setDate(date.getText().toString());
        message.setCategory(category.getText().toString());
        message.setDoctor(doctor.getText().toString());
        message.setDiagnose(diagnose.getText().toString());
        message.setReport(report.getText().toString());
        message.setComment(comment.getText().toString());
    }
}
