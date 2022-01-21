package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LogInActivity extends AppCompatActivity {

    EditText userEdit, passEdit;
    TextView registerView, forgetPass,divert_reg;
    CheckBox checkBox;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        try {
            this.getSupportActionBar().hide();
        }
        catch(NullPointerException e){

        }

        userEdit = (EditText) findViewById(R.id.email);
        passEdit = (EditText) findViewById(R.id.password);
//        registerView = (TextView) findViewById(R.id.register_text);
        Button loginButton = (Button) findViewById(R.id.loginbtn);
        forgetPass = (TextView) findViewById(R.id.forgotpass);
        divert_reg = (TextView) findViewById(R.id.divert_to_reg);


        checkBox = (CheckBox) findViewById(R.id.chck1);
        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEdit.getText().toString().trim();
                String pass = passEdit.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    userEdit.setError("Email id is required");
                }
               else if(TextUtils.isEmpty(pass)){
                    passEdit.setError("Password is required");
                }

              else{
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(LogInActivity.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }


        });

        divert_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterPage();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startForgetPage();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    passEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passEdit.setTransformationMethod((PasswordTransformationMethod.getInstance()));
                }
            }
        });

    }

    private void startForgetPage() {
        Intent forgetIntent = new Intent(LogInActivity.this, ForgotPassword.class);
        startActivity(forgetIntent);
    }

    public void startRegisterPage() {
        Intent regIntent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(regIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startHomePage();
        }
    }

    @Override
    public void onBackPressed()
    {
        finishAffinity();
        finish();
    }

    public void startHomePage() {

        Intent logIntent = new Intent(LogInActivity.this, HomeActivity.class);
        logIntent.putExtra("username", String.valueOf(userEdit));
        logIntent.putExtra("password", String.valueOf(passEdit));
        startActivity(logIntent);

    }
}