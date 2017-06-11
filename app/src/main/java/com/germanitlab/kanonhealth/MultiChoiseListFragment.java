package com.germanitlab.kanonhealth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.callback.Message;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Geram IT Lab on 05/06/2017.
 */

public class MultiChoiseListFragment extends DialogFragment {
    ProgressBar progressBar ;
    RecyclerView recyclerView ;
    TextView error_text ;
    List<User> userList  , chosenList;
    MultiSelectAdapter mAdapter ;
    Button save ;

    // EDit ahmed 10 - 6 -2017
    private ArrayList<Specialities> chosedspecialist;
    private ArrayList<Specialities> allspecialist;
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
        userList = new ArrayList<>();
        chosenList = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            chosedspecialist = (ArrayList<Specialities>) bundle.getSerializable(Constants.CHOSED_LIST);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.Response(allspecialist);
                getDialog().dismiss();
            }
        });
        getData();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
//                CircleImageView check=(CircleImageView)view.findViewById(R.id.check);
//                setDataChecked(check,!allspecialist.get(position).is_my_specialities());
//                if(check.getVisibility()==View.VISIBLE)
//                    allspecialist.get(position).setIs_my_specialities(true);
//                else
//                    allspecialist.get(position).setIs_my_specialities(false);
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
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                allspecialist=(ArrayList<Specialities>)  response ;
                // iteration on data to compare and fill data
                for(Specialities choseditem :chosedspecialist){
                    for(Specialities item :allspecialist){
//                        if(item.getId()==choseditem.getId()){
//                            int index = allspecialist.indexOf(item);
//                            item.setIs_my_specialities(true);
//                            allspecialist.set(index,item);
//                        }
                    }
                }
                mAdapter = new MultiSelectAdapter(getContext(),allspecialist);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                /* userList = (List<User>)  response ;
                mAdapter = new MultiSelectAdapter(userList , chosenList , getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);*/
            }

            @Override
            public void onFailed(String error) {
                progressBar.setVisibility(View.GONE);
                error_text.setVisibility(View.VISIBLE);

            }
        }).getAllSpecilaities();
    }
    private void setDataChecked(CircleImageView checked,boolean show) {
        if(show)
            checked.setVisibility(View.VISIBLE);
        else
            checked.setVisibility(View.GONE);
    }

}
