package com.example.difu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private TextView register, forgotPassword;
    private EditText etEmail, etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.backToLogin);
        register.setOnClickListener(this);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backToLogin:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
        }

    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email  required !");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
                 return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password required !");
            etPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            etPassword.setError("Enter a Minimum of Six characters");
            etPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  //authenticating email
                  FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                  if(user.isEmailVerified()){
                      //redirect to user profile
                      startActivity(new Intent(MainActivity.this, Home.class));
                  }else {
                      user.sendEmailVerification();
                      Toast.makeText(MainActivity.this, "Check your email to verify account",
                              Toast.LENGTH_LONG).show();
                  }


              } else {
                  Toast.makeText(MainActivity.this, "Login Unsuccessful! Try Again", Toast.LENGTH_LONG).show();
              }
            }
        });
    }
}