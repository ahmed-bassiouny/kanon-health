package com.germanitlab.kanonhealth;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.germanitlab.kanonhealth.application.AppController;
import com.germanitlab.kanonhealth.async.HttpCall;
import com.germanitlab.kanonhealth.helpers.Constants;
import com.germanitlab.kanonhealth.interfaces.ApiResponse;
import com.germanitlab.kanonhealth.interfaces.MyClickListener;
import com.germanitlab.kanonhealth.interfaces.RecyclerTouchListener;
import com.germanitlab.kanonhealth.models.user.User;

import java.util.ArrayList;
import java.util.List;

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
            chosenList = (List<User>) bundle.getSerializable(Constants.CHOSED_LIST);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        getData();
        return view;
    }

    private void saveData() {

    }

    private void addListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                addToList(view, position);
            }

            @Override
            public void onClick(Object object) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addToList(View view, int position) {
        if(chosenList.contains(userList.get(position)))
            chosenList.remove(chosenList.indexOf(userList.get(position)));
        else
            chosenList.add(userList.get(position));
    }

    public void getData(){
        new HttpCall(getActivity(), new ApiResponse() {
            @Override
            public void onSuccess(Object response) {
                userList = (List<User>)  response ;
                mAdapter = new MultiSelectAdapter(userList , chosenList , getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(String error) {
                progressBar.setVisibility(View.GONE);
                error_text.setVisibility(View.VISIBLE);

            }
        }).getlocations(String.valueOf(AppController.getInstance().getClientInfo().getUser_id())
                , AppController.getInstance().getClientInfo().getPassword() , 0 , 2);
    }

}
