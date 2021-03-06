package com.example.login_api_crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailEt, passwordEt;
    private Button signin;
    private TextView signup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        emailEt= findViewById(R.id.emailSignin);
        passwordEt=findViewById(R.id.passwordSignIn);
        signup=findViewById(R.id.signup);
        progressDialog=new ProgressDialog(this);
        signin=findViewById(R.id.login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Login() {
        String email= emailEt.getText().toString();
        String password=passwordEt.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailEt.setError("Enter your email");
            return;
        }
        else if(TextUtils.isEmpty(password)){
            passwordEt.setError("Enter your password");
            return;
        }
        else if(password.length()<6){
            passwordEt.setError("Password length should be > 6");
            return;
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Login successfully",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Login not successfully",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}