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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText nemail;
    private EditText npassword;
    private EditText nrealname;
    private Button nsign_up_button;
    private TextView nsign_up_textview;

    private ProgressDialog progressDialog ;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        /**註冊後，用戶資料不為NULL，直接進入介面*/
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        nemail = (EditText) findViewById(R.id.sign_up_email);
        npassword = (EditText)findViewById(R.id.sign_up_password);
        nrealname = (EditText)findViewById(R.id.real_name);
        nsign_up_button = (Button)findViewById(R.id.sign_up_button);
        nsign_up_textview = (TextView)findViewById(R.id.sign_up_textview);


        progressDialog = new ProgressDialog(this);


        nsign_up_button.setOnClickListener(this);

        nsign_up_textview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUp.this, Login.class));
                /*Intent intent = new Intent();
                intent.setClass(SignUp.this, Login.class);
                startActivity(intent);
                finish();*/
            }
        });
    }

    private void signUpUser(){
        String email = nemail.getText().toString().trim();
        String password = npassword.getText().toString().trim();
        final String realname = nrealname.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(realname)){
            Toast.makeText(this,"Please enter chinese name chinese name" , Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(SignUp.this,"注冊成功",Toast.LENGTH_SHORT).show();
                            userProfile();
                        }else {
                            Toast.makeText(SignUp.this,"注冊不成功,請再之嘗試",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }


    /**用戶資料*/
    private void userProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            /**設定顯示名字*/
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nrealname.getText().toString().trim())
                    .build();
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);

            /**更新用戶個人資訊*/
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Log.d("TEST","updates");
                            }
                        }
                    });
        }
    }





    @Override
    public void onClick(View view){
        if (view == nsign_up_button){
            signUpUser();
        }
        if(view == nsign_up_textview){
            finish();
            startActivity(new Intent(this,Login.class));
        }
    }
}
