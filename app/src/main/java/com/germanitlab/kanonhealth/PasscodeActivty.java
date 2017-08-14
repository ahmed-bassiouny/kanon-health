package com.germanitlab.kanonhealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.germanitlab.kanonhealth.helpers.PrefHelper;
import com.germanitlab.kanonhealth.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PasscodeActivty extends AppCompatActivity {

    boolean checkPassword; // check password if you want login or enter
    boolean finish;// finish this activity or redirect to main activity
    boolean has_back; // go back to the previuos activity
    String tempPasscode = "";
    String passcode = "";
    int pinCircle=0;

    @BindView(R.id.pass_text)
    TextView passText;

    @BindView(R.id.ind_one)
    Button ind_one;
    @BindView(R.id.ind_two)
    Button ind_two;
    @BindView(R.id.ind_three)
    Button ind_three;
    @BindView(R.id.ind_four)
    Button ind_four;
    @BindView(R.id.ind_five)
    Button ind_five;
    @BindView(R.id.ind_six)
    Button ind_six;
    Button[] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_activty);
        ButterKnife.bind(this);
        views=new Button[]{ind_one,ind_two,ind_three,ind_four,ind_five,ind_six};
        colorCircles();
        try {
            checkPassword = getIntent().getBooleanExtra("checkPassword", true);
            finish = getIntent().getBooleanExtra("finish", true);
            has_back = getIntent().getBooleanExtra("has_back", false);
            if (!checkPassword) {
                passText.setText(R.string.set_your_password);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

//    @OnTouch({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero})
//    public boolean hoverEffect(View view, MotionEvent motionEvent) {
//        if (view != null && view instanceof Button) {
//            Button iv = (Button) view;
//            if (motionEvent != null) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    iv.setBackgroundColor(Color.parseColor("#0099cc"));
//                    iv.setTextColor(Color.WHITE);
//                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    iv.setBackgroundColor(Color.TRANSPARENT);
//                    iv.setTextColor(Color.DKGRAY);
//                }
//            }
//        }
//        return false;
//    }


    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero})
    public void enterPass(View view) {
        pinCircle +=1;
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

        }

        colorCircles();


    }

    private void colorCircles()
    {

        switch (pinCircle) {
            case 0:
                colorView(0);
                break;

            case 1:
                colorView(1);

                break;
            case 2:

                colorView(2);
                break;
            case 3:

                colorView(3);
                break;
            case 4:

                colorView(4);
                break;
            case 5:

                colorView(5);
                break;
            case 6:

                colorView(6);
                submit();

                break;
        }
    }

    @OnClick(R.id.delete)
    public void deleteChar(View view) {
        try {
            if (passcode.length() > 0) {
                ;
                passcode = passcode.substring(0, passcode.length() - 1);
            }

            if(pinCircle>0)
            {
                pinCircle-=1;
            }
            colorCircles();

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
            Log.e("Passcode", "Activity ", e);
        }

    }


    public void submit() {
        try {
            if (passcode.length() != 6)
                Toast.makeText(this, R.string.wrong_passcode, Toast.LENGTH_SHORT).show();
            else if (checkPassword) {
                // check password to login
                checkPasscode();
            } else {
                // enter password to save
                if (tempPasscode.isEmpty()) {
                    // enter passcode first time to save it
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tempPasscode = passcode;
                            passcode = "";
                            pinCircle=0;
                            colorCircles();
                            passText.setText(R.string.confirm_your_passcode);
                        }
                    }, 300);


                } else if (tempPasscode.equals(passcode)) {
                    //enter passcode second time to save it
                    PrefHelper.put(getApplicationContext(),PrefHelper.KEY_PASSCODE, passcode);
                    Toast.makeText(this, R.string.your_password_saved, Toast.LENGTH_SHORT).show();
                    finishActivity();
                } else if (!tempPasscode.equals(passcode)) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passText.setText(R.string.set_your_password);
                            pinCircle=0;
                            colorCircles();
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(0)
                                    .playOn(findViewById(R.id.indicator));
                            wrongPassword();

                        }
                    }, 300);

                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_message), Toast.LENGTH_SHORT).show();
        }

    }

    private void checkPasscode() {
        if (passcode.equals(PrefHelper.get(getApplicationContext(),PrefHelper.KEY_PASSCODE,"")))
            finishActivity();
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pinCircle = 0;
                    colorCircles();
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(0)
                            .playOn(findViewById(R.id.indicator));
                    wrongPassword();

                }
            }, 300);


        }
    }

    private void wrongPassword() {
        Toast.makeText(this, R.string.invalid_passcode, Toast.LENGTH_SHORT).show();
        passcode = "";
        tempPasscode = "";
    }

    @Override
    public void onBackPressed() {
        if (has_back)
            super.onBackPressed();
        else
            return;
    }

    private void finishActivity() {
        if (finish) {
            this.setResult(RESULT_OK);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
    private void colorView (int indicatorCount){
        for (int i=0;i< indicatorCount;i++){
            views[i].setBackgroundResource(R.drawable.indicator_pressed_shape);
        }
        for (int i=indicatorCount;i< views.length;i++){
            views[i].setBackgroundResource(R.drawable.indicator_unpressed_shape);
        }
    }
}
