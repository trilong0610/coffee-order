package com.example.coffeeorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeorder.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 10;
    private FirebaseAuth mAuth;

    public static final int PERMISSION_REQUEST_READ_STORAGE = 101;

    private TextInputEditText edtEmailLogin;
    private TextInputEditText edtPassLogin;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;


    private MaterialButton btnLogin;
    private MaterialButton btnGetRegister;
    private MaterialButton btnForgetPass;

    private Button btnLoginGG;

    private ImageView ivLogoLogin;
    private MaterialTextView tvWelcomeLogin;

    Animation bottomAnim;

    ArrayList<TextInputLayout> textInputLayout;



    private GoogleSignInClient mGoogleSignInClient;

    View mainView;



    DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//      Anh xa view
        AnhXa();

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //                            -- Chuyen den HomeActivity neu login thanh cong
            Intent goToHome = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(goToHome);
            finish();
        }
    }

    private void AnhXa(){
        edtEmailLogin = findViewById(R.id.edt_email_login);
        edtPassLogin = findViewById(R.id.edt_pass_login);

        textInputLayoutEmail = findViewById(R.id.textInputLayout_email);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password);


        ivLogoLogin = findViewById(R.id.iv_logo_login);
        tvWelcomeLogin = findViewById(R.id.tv_welcome_login);

        btnLogin = findViewById(R.id.btn_login);
        btnGetRegister = findViewById(R.id.btn_get_register);
        btnForgetPass = findViewById(R.id.btn_forget_pass);

        btnLoginGG = findViewById(R.id.btn_login_gg);

        mainView = findViewById(R.id.activity_login);

        btnLogin.setOnClickListener(this::onClick);
        btnLoginGG.setOnClickListener(this::onClick);
        btnGetRegister.setOnClickListener(this::onClick);
        btnForgetPass.setOnClickListener(this::onClick);

        textInputLayout = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();

        textInputLayout.add(textInputLayoutEmail);
        textInputLayout.add(textInputLayoutPassword);




    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){

            boolean noErrors = true;
            for (TextInputLayout textInputLayout : textInputLayout) {
                String editTextString = textInputLayout.getEditText().getText().toString();
                if (editTextString.isEmpty()) {
                    textInputLayout.setError("Vui lòng điền đầy đủ thông tin");
                    noErrors = false;
                } else {
                    textInputLayout.setError(null);
                }
            }

            if (noErrors) {
                View viewLogin = findViewById(R.id.activity_login);
                Snackbar.make(viewLogin,"Đang đăng nhập...", BaseTransientBottomBar.LENGTH_SHORT).show();
                login();
            }

        }


    }

    public void login(){
        mAuth.signInWithEmailAndPassword(edtEmailLogin.getText().toString().trim(), edtPassLogin.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginResult", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

//                            -- Chuyen den HomeActivity neu login thanh cong
                            Intent goToHome = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(goToHome);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginResult", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng kiểm tra lại!\n" + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                            // ...
                        }

                        // ...
                    }

                });
    }


}