package com.gmail.foodde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.foodde.model.Key;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Random;

public class UserRegistrationActivity extends AppCompatActivity {

    private LinearLayout firstWindow, secondWindow;
    private ImageView mailErase, firstCont, back, secondCont;
    private EditText mailEditText, keyEditText;
    private TextView firstAgreement, secondAgreement, keyResend, mail;
    private CheckBox firstCheckBox, secondCheckBox;

    private Key key = new Key(null);
    private String email;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_registration);

        final Animation animAlpha = AnimationUtils.loadAnimation(UserRegistrationActivity.this, R.anim.alpha);
        final Animation show = AnimationUtils.loadAnimation(UserRegistrationActivity.this, R.anim.show);
        final Animation hide = AnimationUtils.loadAnimation(UserRegistrationActivity.this, R.anim.hide);

        firstWindow = (LinearLayout) findViewById(R.id.profile_registration_first_window);
        mailErase = (ImageView) findViewById(R.id.profile_registration_mail_erase);
        firstCont = (ImageView) findViewById(R.id.profile_registration_first_next);
        mailEditText = (EditText) findViewById(R.id.profile_registration_mail_et);
        firstAgreement = (TextView) findViewById(R.id.profile_registration_first_agreement);
        secondAgreement = (TextView) findViewById(R.id.profile_registration_second_agreement);
        firstCheckBox = (CheckBox) findViewById(R.id.profile_registration_check_first);
        secondCheckBox = (CheckBox) findViewById(R.id.profile_registration_check_second);
        //..
        secondWindow = (LinearLayout) findViewById(R.id.profile_registration_second_window);
        keyResend = (TextView) findViewById(R.id.profile_registration_key_resend);
        back = (ImageView) findViewById(R.id.profile_registration_back);
        secondCont = (ImageView) findViewById(R.id.profile_registration_second_next);
        keyEditText = (EditText) findViewById(R.id.profile_registration_key_et);
        mail = (TextView) findViewById(R.id.profile_registration_mail_text_view);


                FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Main", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        token = task.getResult();

                    }
                });


        firstCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstCheckBox.isChecked()&&isEmail(mailEditText.getText().toString())){

                    email = mailEditText.getText().toString();

                    new SendMail(UserRegistrationActivity.this, email, key).execute();

                    hideKeyboard();
                    mail.setText("Адрес почты: "+email);

                    Toast.makeText(UserRegistrationActivity.this, "Сообщение с кодом было отправлено на Вашу почту", Toast.LENGTH_LONG).show();

                    firstWindow.setVisibility(View.GONE);
                    firstWindow.setAnimation(hide);
                    firstWindow.getAnimation().start();

                    secondWindow.setVisibility(View.VISIBLE);
                    secondWindow.startAnimation(show);
                    secondWindow.getAnimation().start();
                }else {
                    if(!firstCheckBox.isChecked()){
                        String msg = "Подтвердите пользовательское соглашение";
                        final AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
                        builder.setCancelable(true)
                                .setMessage(msg)
                                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                        dialog.dismiss();
                                    }
                                });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    }else {
                        if(mailEditText.getText().toString().equals("")){
                            mailEditText.setText("");
                            String msg = "Введите адрес электронной почты";
                            final AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
                            builder.setCancelable(true)
                                    .setMessage(msg)
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            dialog.cancel();
                                            dialog.dismiss();
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
                        }else {
                            mailEditText.setText("");
                            String msg = "Вы ввели некорректный адрес электронной почты";
                            final AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
                            builder.setCancelable(true)
                                    .setMessage(msg)
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            dialog.cancel();
                                            dialog.dismiss();
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                }
            }
        });

        secondCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(keyEditText.getText().toString().equals(key.getText())){
                    new UserLogin(UserRegistrationActivity.this, email, String.valueOf(secondCheckBox.isChecked()),UserRegistrationActivity.this, token).execute();
                }else{
                    keyEditText.setText("");
                    Toast.makeText(UserRegistrationActivity.this, "Вы ввели неверный код,\nпопробуйте снова", Toast.LENGTH_LONG).show();
                }
            }
        });

        keyResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);

                new SendMail(UserRegistrationActivity.this, email, key).execute();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                secondWindow.setVisibility(View.GONE);
                secondWindow.setAnimation(hide);
                secondWindow.getAnimation().start();

                firstWindow.setVisibility(View.VISIBLE);
                firstWindow.setAnimation(show);
                firstWindow.getAnimation().start();

                keyEditText.setText("");
            }
        });

        mailErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                mailEditText.setText("");
                //isEmail("sergeygaras2002@gmail.com");
            }
        });

        firstAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstCheckBox.isChecked()){
                    firstCheckBox.setChecked(false);
                }else{
                    firstCheckBox.setChecked(true);
                }
            }
        });

        String textWithLink = "Я ознакомлен и соглашаюсь с" +
                " <a href=\"https://myfirstdbproject.000webhostapp.com\">Пользовательским соглашением</a> и" +
                " <a href=\"https://myfirstdbproject.000webhostapp.com/terms_of_use.html\">Политикой конфиденциальности</a>";

        firstAgreement.setText(Html.fromHtml(textWithLink, null, null));
        firstAgreement.setLinksClickable(true);
        firstAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = firstAgreement.getText();
        if (text instanceof Spannable)
        {
            firstAgreement.setText(MakeLinksClicable.reformatText(text));
        }

        secondAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(secondCheckBox.isChecked()){
                    secondCheckBox.setChecked(false);
                }else{
                    secondCheckBox.setChecked(true);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        final Animation show = AnimationUtils.loadAnimation(UserRegistrationActivity.this, R.anim.show);
        final Animation hide = AnimationUtils.loadAnimation(UserRegistrationActivity.this, R.anim.hide);

        if(secondWindow.getVisibility() == View.VISIBLE){
            secondWindow.setVisibility(View.GONE);
            secondWindow.setAnimation(hide);

            firstWindow.setVisibility(View.VISIBLE);
            firstWindow.setAnimation(show);
        }else {
            finish();
        }
    }

    private void hideKeyboard(){
        View view = UserRegistrationActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isEmail(String em){
        if(em.contains("@")){
            int pos = em.indexOf("@");
            String domainName = em.substring(pos, em.length());
            if(domainName.contains(".")){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }
}