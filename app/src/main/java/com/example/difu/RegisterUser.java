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
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUser extends AppCompatActivity  implements View.OnClickListener {
    private TextView logoText,registerUser, tvBackLogin;
    private EditText ptFullName, ptAge, ptEmailReg, pwReg;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        logoText = (TextView) findViewById(R.id.greetingText);
        logoText.setOnClickListener(this);

        tvBackLogin = (TextView)findViewById(R.id.backToLogin);
        tvBackLogin.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.btnRegister);
        registerUser.setOnClickListener(this);

        ptFullName = (EditText) findViewById(R.id.etEmail);
        ptAge = (EditText) findViewById(R.id.ptAge);
        ptEmailReg = (EditText) findViewById(R.id.ptEmailReg);
        pwReg = (EditText) findViewById(R.id.pwReg);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.greetingText:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.backToLogin:
                startActivity(new Intent(this,MainActivity.class));
            case R.id.btnRegister:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String email= ptEmailReg.getText().toString().trim();
        String password= pwReg.getText().toString().trim();
        String fullname= ptFullName.getText().toString().trim();
        String age= ptAge.getText().toString().trim();

        if (fullname.isEmpty()){
            ptFullName.setError("Full name required !");
            ptFullName.requestFocus();
            return;
        }
        if (age.isEmpty()){
            ptAge.setError("Age required !");
            ptAge.requestFocus();
            return;
        }
        if (password.isEmpty()){
            pwReg.setError("Password required !");
            pwReg.requestFocus();
            return;
        }
        if (email.isEmpty()){
            ptEmailReg.setError("Email required !");
            ptEmailReg.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ptEmailReg.setError("Provide a valid address!");
            ptEmailReg.requestFocus();
            return;
        }
        if (password.length()<6){
            pwReg.setError("Minimum length is 6 characters!");
            pwReg.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(fullname, age,email);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterUser.this, "Registration Successful",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                                //REDIRECT TO LOGIN


                            }else
                                Toast.makeText(RegisterUser.this,"Registration failed! Try again.",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }else
                    Toast.makeText(RegisterUser.this,"Registration failed! Try again.",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}