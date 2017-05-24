package com.germanitlab.kanonhealth.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.germanitlab.kanonhealth.R;
import com.germanitlab.kanonhealth.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.list, R.id.document , R.id.chat  , R.id.setting})
    public void click(View view)
    {
        Intent intent = new Intent(this , MainActivity.class);
        switch (view.getId())
        {
            case (R.id.list ):
                intent.putExtra("from" , 0);
                break;
            case (R.id.document):
                intent.putExtra("from" , 1);
                break;
            case (R.id.chat):
                intent.putExtra("from" , 2);
                break;
            case (R.id.setting):
                intent.putExtra("from" , 4);
                break;
        }
        startActivity(intent);
        finish();
    }
}
