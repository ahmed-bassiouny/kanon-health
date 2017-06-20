package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasscodeActivty extends AppCompatActivity {

    boolean checkPassword; // check password if you want login or enter
    boolean finish; // finish this activity or redirect to main activity
    String tempPasscode="";
    String passcode="";
    PrefManager prefManager;
    @BindView(R.id.pass)
    TextView pass;
    @BindView(R.id.pass_text)
    TextView passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_activty);
        ButterKnife.bind(this);
        try {
            prefManager = new PrefManager(this);
            checkPassword = getIntent().getBooleanExtra("checkPassword", true);
            finish = getIntent().getBooleanExtra("finish", true);
            if (!checkPassword) {
                passText.setText("Set your Password");
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero})
    public void enterPass(View view) {
        if (passcode.length() < 6) {
            switch (view.getId()) {
                case R.id.one:
                    passcode += "1";
                    break;
                case R.id.two:
                    passcode += "2";
                    break;
                case R.id.three:
                    passcode += "3";
                    break;
                case R.id.four:
                    passcode += "4";
                    break;
                case R.id.five:
                    passcode += "5";
                    break;
                case R.id.six:
                    passcode += "6";
                    break;
                case R.id.seven:
                    passcode += "7";
                    break;
                case R.id.eight:
                    passcode += "8";
                    break;
                case R.id.nine:
                    passcode += "9";
                    break;
                case R.id.zero:
                    passcode += "0";
                    break;

            }
            pass.setText(pass.getText() + "*");
        }

    }

    @OnClick(R.id.delete)
    public void deleteChar(View view) {
        try {
            if (passcode.length() > 0) {
                String temp = pass.getText().toString();
                pass.setText(temp.substring(0, pass.getText().length() - 1));
                passcode = passcode.substring(0, passcode.length() - 1);
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.submit)
    public void submit(View view) {
        try {
            if (passcode.length() != 6)
                Toast.makeText(this, "wrong passcode", Toast.LENGTH_SHORT).show();
            else if(checkPassword) {
                // check password to login
                checkPasscode();
            }else{
                // enter password to save
                if(tempPasscode.isEmpty()){
                    // enter passcode first time to save it
                    tempPasscode=passcode;
                    passcode="";
                    pass.setText("");
                    passText.setText("Confirm Your Passcode");
                }else if(tempPasscode.equals(passcode)){
                    //enter passcode second time to save it
                    prefManager.put(PrefManager.PASSCODE, passcode);
                    Toast.makeText(this, "Your Password Saved", Toast.LENGTH_SHORT).show();
                    finishActivity();
                }else if(!tempPasscode.equals(passcode)){
                    passText.setText("Set your Password");
                    wrongPassword();
                }
            }
        }catch (Exception e){
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void checkPasscode() {
        if(passcode.equals(prefManager.getData(PrefManager.PASSCODE)))
            finishActivity();
        else{
            wrongPassword();
        }
    }

    private void wrongPassword() {
        Toast.makeText(this, "Invalid Passcode", Toast.LENGTH_SHORT).show();
        pass.setText("");
        passcode = "" ;
        tempPasscode="";
    }

    private void finishActivity(){
        if(finish){
            this.setResult(RESULT_OK);
            finish();
        } else startActivity(new Intent(this,MainActivity.class));
    }
}
