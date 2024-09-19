package com.example.noteapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    EditText textEmailAddress,textPassword,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar processBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        textEmailAddress = findViewById(R.id.email_edit_text);
        textPassword = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.Comfirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        processBar = findViewById(R.id.process_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());

    }
    void createAccount(){
        String email = textEmailAddress.getText().toString();
        String password = textPassword.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isvalidated = validateData(email,password,confirmPassword);

        if(!isvalidated){
            return;
        }

        createAccountInFirebase(email,password);

    }

    void createAccountInFirebase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        Utility.showToast(CreateAccount.this, "Successfully create account, Check email to verify");
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    } else {
                        Utility.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                    }
                }else {
                    Utility.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            processBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);

        }else {
            processBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password, String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmailAddress.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            textPassword.setError("Password length is invalid");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password not matched");
            return false;
        }
        return true;
    }
}