package com.germanitlab.kanonhealth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.models.ChooseModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 05/06/2017.
 */

public class MultiChoiseListFragment extends DialogFragment implements ApiResponse {
    ProgressBar progressBar ;
    RecyclerView recyclerView ;
    TextView error_message ;
    MultiSelectAdapter mAdapter ;
    Button save ;

    // EDit ahmed 10 - 6 -2017
    private ArrayList<ChooseModel> chosedspecialist;
    private ArrayList<ChooseModel> allspecialist;
    int type;
    public MultiChoiseListFragment() {
        super();
        setStyle(STYLE_NO_TITLE, 0);
    }
    Message message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.mulitchoise_list_layout, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        save = (Button) view.findViewById(R.id.save);
        error_message=(TextView)view.findViewById(R.id.error_message);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type=bundle.getInt("Constants");
            chosedspecialist = (ArrayList<ChooseModel>) bundle.getSerializable(Constants.CHOSED_LIST);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.Response(allspecialist,type);
                getDialog().dismiss();
            }
        });
        getData();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                CheckBox rbtn=(CheckBox)view.findViewById(R.id.rbtn);
                //rbtn.setChecked(!allspecialist.get(position).getIsMyChoise());
                allspecialist.get(position).setIsMyChoise(rbtn.isChecked());
            }

            @Override
            public void onClick(Object object) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        message=(Message)context;
    }

    public void getData(){
        switch (type){
            case Constants.SPECIALITIES:
                new HttpCall(getActivity(),this).getAllSpecilaities();
                break;
            case Constants.LANGUAUGE:
                new HttpCall(getActivity(),this).getAllLanguage();
                break;
            case Constants.MEMBERAT:
                new HttpCall(getActivity(),this).getAllMemberAt();
                break;
            case Constants.DoctorAll:
                new HttpCall(getActivity(),this).getDoctorAll();
                break;
        }
    }
    /*private void setDataChecked(RadioButton rbtn,boolean show) {
        if(show)
            rbtn.setChecked(true);
        else
            r.setVisibility(View.GONE);
    }*/

    @Override
    public void onSuccess(Object response) {
        switch (type){
            case Constants.SPECIALITIES:
                initDataSpecialist(response);
                break;
            case Constants.LANGUAUGE:
                initDataLanguauge(response);
                break;
            case Constants.MEMBERAT:
            case Constants.DoctorAll:
                initDataMemberAt(response);
                break;
        }
        mAdapter = new MultiSelectAdapter(getContext(),allspecialist,type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onFailed(String error) {
        progressBar.setVisibility(View.GONE);
        error_message.setVisibility(View.VISIBLE);

    }
    private void initDataSpecialist(Object response){
        allspecialist=(ArrayList<ChooseModel>)  response ;
        // iteration on data to compare and fill data
        for(ChooseModel choseditem :chosedspecialist){
            for(ChooseModel item :allspecialist){
                if(item.getSpeciality_id()==choseditem.getSpeciality_id()){
                    int index = allspecialist.indexOf(item);
                    item.setIsMyChoise(true);
                    allspecialist.set(index,item);
                }
            }
        }
    }
    private void initDataLanguauge(Object response){
        allspecialist=(ArrayList<ChooseModel>)  response ;
        // iteration on data to compare and fill data
        for(ChooseModel choseditem :chosedspecialist){
            for(ChooseModel item :allspecialist){
                if(item.getLang_id()==choseditem.getLang_id()){
                    int index = allspecialist.indexOf(item);
                    item.setIsMyChoise(true);
                    allspecialist.set(index,item);
                }
            }
        }
    }
    private void initDataMemberAt(Object response){
        allspecialist=(ArrayList<ChooseModel>)  response ;
        // iteration on data to compare and fill data
        for(ChooseModel choseditem :chosedspecialist){
            for(ChooseModel item :allspecialist){
                if(item.getIdMember()==choseditem.getIdMember()){
                    int index = allspecialist.indexOf(item);
                    item.setIsMyChoise(true);
                    allspecialist.set(index,item);
                }
            }
        }
    }
}
