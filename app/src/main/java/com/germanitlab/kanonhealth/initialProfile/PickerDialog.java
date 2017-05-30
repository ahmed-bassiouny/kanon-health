package com.germanitlab.kanonhealth.initialProfile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.germanitlab.kanonhealth.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mo on 3/30/17.
 */

public class PickerDialog  extends DialogFragment {

    DialogPickerCallBacks callBacks;
    Boolean delete = false ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //Make sure the container activity implemented the callback interface.
        try {
            callBacks = (DialogPickerCallBacks) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement DialogPickerCallBacks");
        }
    }

    public PickerDialog(){}

    @SuppressLint("ValidFragment")
    public PickerDialog(Boolean delete)
    {
        this.delete = delete ;
    }
    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            callBacks = (DialogPickerCallBacks) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement DialogPickerCallBacks");
        }
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.image_picker_dialog,null);

        ButterKnife.bind(this, view);
        if(delete)
            view.findViewById(R.id.delete_image).setVisibility(View.VISIBLE);

        builder.setView(view);

        return builder.create();
    }

    @OnClick(R.id.select_image)
    public void onTakeImageClicked (){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        callBacks.onGalleryClicked(chooserIntent);
    }

    @OnClick(R.id.take_image)
    public void onSelectImageClicked (){
        callBacks.onCameraClicked();
    }
    @OnClick(R.id.delete_image)
    public void deleteImage(){
        callBacks.deleteMyImage();
    }
}
