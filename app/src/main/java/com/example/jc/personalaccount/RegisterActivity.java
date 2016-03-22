package com.example.jc.personalaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public AutoCompleteTextView mActvUserName;
    public EditText mEtPassword;
    public EditText mEtRePassword;
    public AutoCompleteTextView mActvEmail;
    public Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mActvUserName = (AutoCompleteTextView)findViewById(R.id.userid_regist);
        this.mEtPassword = (EditText)findViewById(R.id.password_regist);
        this.mEtRePassword = (EditText)findViewById(R.id.repassword_regist);
        this.mActvEmail = (AutoCompleteTextView)findViewById(R.id.email_regist);

        this.mBtnRegister = (Button) findViewById(R.id.regist_button_regist);
        this.mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {

        if (checkDataIsValid()) {
            if (GlobalData.DataStoreHelper.register(this.mActvUserName.getText().toString(),
                    this.mEtPassword.getText().toString(),
                    this.mActvEmail.getText().toString())) {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("注册成功，请登录！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                toLoginActivity();
                            }
                }).show();
            }
            else {
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("注册失败！")
                        .setPositiveButton("确定", null)
                        .show();
            }
        }
    }

    private void toLoginActivity() {

        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.putExtra(GlobalData.EXTRA_USERNAME,this.mActvUserName.getText().toString());
        this.startActivity(intent);
    }

    private Boolean checkDataIsValid() {

        Boolean bIsValid = false;

        int length = this.mActvUserName.getText().toString().length();
        if ((2 > length) || (length > 18)) {
            this.mActvUserName.setError(this.getString(R.string.information_register_invalid_user));
            this.mActvUserName.requestFocus();
            return bIsValid;
        }

        String password = this.mEtPassword.getText().toString();
        String rePassword = this.mEtRePassword.getText().toString();

        length = password.length();
        if ((6 <= length) && (length <= 18)) {
            if (0 != password.compareTo(rePassword)) {
                this.mEtRePassword.setError(this.getString(R.string.information_register_invalid_password));
                this.mEtRePassword.requestFocus();
                return bIsValid;
            }
        } else {
            this.mEtPassword.setError(this.getString(R.string.information_register_invalid_repassword));
            this.mEtPassword.requestFocus();
            return bIsValid;
        }

        String email = this.mActvEmail.getText().toString();
        if (isEmail(email)) {
            bIsValid = true;
        }
        else {
            this.mActvEmail.setError(this.getString(R.string.information_register_invalid_email));
            this.mActvEmail.requestFocus();
            return bIsValid;
        }

        return bIsValid;
    }

    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }


}
