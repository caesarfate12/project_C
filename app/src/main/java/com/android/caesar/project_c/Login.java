package com.android.caesar.project_c;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail;
    private EditText mPassword;
    private Button mlogin_button;
    private TextView mlogin_textview;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**連接Firebase Authentication*/
        firebaseAuth = FirebaseAuth.getInstance();

        /**getCurrentUser(User 資訊)不為NULL，直接進入主介面*/
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);
        mlogin_button = (Button) findViewById(R.id.login_button);
        mlogin_textview  = (TextView) findViewById(R.id.login_textview);

        progressDialog = new ProgressDialog(this);

        mlogin_button.setOnClickListener(this);
        mlogin_textview.setOnClickListener(this);

    }


    private void userLogin(){

        /**取得TextView字串*/
        final String email = mEmail.getText().toString().trim();
        final String password  = mPassword.getText().toString().trim();

        /**判斷TextView是否為空*/
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("登入中");
        progressDialog.show();

        /**進行登入*/
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(Login.this,"登入失敗，帳號或密碼錯誤",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if(view == mlogin_button){
            userLogin();
        }

        if(view == mlogin_textview){
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
    }
}