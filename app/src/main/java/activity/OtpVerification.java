package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.divyakhare.minilibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import model.NewUserHelperClass;

public class OtpVerification extends AppCompatActivity {

    Button btnVerify;
    EditText etUserOtp;
    String verificationCodeBySystem,_name,_email,mobile_no,_favcategory,_password;
    ProgressBar progressBar;
    FirebaseDatabase rootnode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        btnVerify = findViewById(R.id.btnVerify);
        etUserOtp=findViewById(R.id.etUserCode);
        progressBar=findViewById(R.id.progressBar);
        mobile_no = getIntent().getStringExtra("mobileno");
        _name = getIntent().getStringExtra("name");
        _email = getIntent().getStringExtra("email");
        _favcategory = getIntent().getStringExtra("favcategory");
        _password = getIntent().getStringExtra("password");
        sendVerificationCodeToUser(mobile_no);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etUserOtp.getText().toString();
                if(code.isEmpty()|code.length()<6){
                    etUserOtp.setError("Wrong OTP");
                    etUserOtp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String mobile_no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile_no,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem =s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                etUserOtp.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpVerification.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        final DatabaseReference reference = rootnode.getReference("New User Data");
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            NewUserHelperClass newUserHelperClass = new NewUserHelperClass(_name,_email,mobile_no,_favcategory,_password);
                            reference.child(mobile_no).setValue(newUserHelperClass);
                            Intent intent = new Intent(getApplicationContext(), RegisterConfirmation.class);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.GONE);
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(OtpVerification.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
