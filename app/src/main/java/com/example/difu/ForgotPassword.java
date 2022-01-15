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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity  implements View.OnClickListener{

    private EditText resetPass;
    private TextView backLogin;
    private Button btnResetPass;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPass = (EditText) findViewById(R.id.resetPass);

        backLogin = (TextView) findViewById(R.id.backToLogin);
        backLogin.setOnClickListener(this);


        btnResetPass= (Button) findViewById(R.id.btnResetPass);
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });

        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        auth= FirebaseAuth.getInstance();
    }

    private void resetPassword() {
        String email= resetPass.getText().toString().trim();
        if(email.isEmpty()){
            resetPass.setError("Email Required!");
            resetPass.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetPass.setError("Provide a valid email");
            resetPass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this,
                            "Check your mail to reset your password",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this,
                            "Try Again!Something went wrong",Toast.LENGTH_LONG).show();
                }
                }
            });
        }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.backToLogin:
                    startActivity(new Intent(this,MainActivity.class));
    }
}
}
