package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.germanitlab.kanonhealth.db.PrefManager;
import com.germanitlab.kanonhealth.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasscodeActivty extends AppCompatActivity {
    String passcode;
    @BindView(R.id.pass)
    TextView pass;
    PrefManager prefManager;
    int status;
    Boolean reEnter, save;
    @BindView(R.id.pass_text)
    TextView passText;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_activty);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        reEnter = false;
        status = getIntent().getIntExtra("status", -1);
        if (status == 0) {
            reEnter = true;
            passText.setText("Set your Password");
        }
        passcode = "";
        save = false ;
        temp = "";


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
        if (passcode.length() > 0) {
            String temp = pass.getText().toString();
            pass.setText(temp.substring(0, pass.getText().length() - 1));
            passcode = passcode.substring(0, passcode.length() - 1);
        }
    }

    @OnClick(R.id.submit)
    public void submit(View view) {
        if (passcode.length() != 6)
            Toast.makeText(this, "wrong passcode", Toast.LENGTH_SHORT).show();
        else {

            //reset password
            if (status == 0) {
                //second time password entered
                if(save)
                    save();
                //first time password entered
                else if(reEnter)
                    reEnter();
                else
                    //nothing entered yet
                    newPass();
            }
            //for check  password
            if (status == 1) {
                checkPasscode();
                //update passcode
            } else if (status == 2) {
                if(save)
                    save();
                else if(reEnter)
                    reEnter();
                else {
                    if (passcode.equals(prefManager.getData(PrefManager.PASSCODE))) {
                        newPass();
                    }
                    else {
                        wrongPassword();
                    }
                }

            }
        }
    }

    private void checkPasscode() {
        if(passcode.equals(prefManager.getData(PrefManager.PASSCODE)))
            startActivity(new Intent(this, MainActivity.class));
        else{
            wrongPassword();
        }
    }

    private void wrongPassword() {
        Toast.makeText(this, "Invalid Passcode", Toast.LENGTH_SHORT).show();
        pass.setText("");
        passcode = "" ;
    }

    private void reEnter() {

        passText.setText("Re Enter your Passcode");
        pass.setText("");
        temp = passcode;
        passcode = "";
        save = true ;
        reEnter = false ;
    }
    private void save(){
        if(temp.equals(passcode)) {
            prefManager.put(PrefManager.PASSCODE, passcode);
            Toast.makeText(this, "Your Password Saved", Toast.LENGTH_SHORT).show();
            if(status == 2) {
                Intent intent  = new Intent(this , MainActivity.class);
                if (status == 2)
                    intent.putExtra("index" , 3);
                startActivity(intent);
            }else {
                finish();
            }
        }
        else
        {
            wrongPassword();
            save = false ;
            reEnter = true ;
            passText.setText("Enter your New Passcode");

        }
    }

    private void newPass() {

        passText.setText("Enter your New Passcode");
        passcode = "";
        pass.setText("");
        reEnter = true;
        return;

    }
}
