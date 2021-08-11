package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.divyakhare.minilibrary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {
    Button btnDone;
    EditText etNewPass,etConfirmPass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        btnDone=findViewById(R.id.btnDone);
        etNewPass=findViewById(R.id.etNewPass);
        etConfirmPass=findViewById(R.id.etConfirmPass);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateNewPassword()|!validateConfirmPassword()){
                    return;
                }
                String new_password = etNewPass.getText().toString();
                String confirm_password = etConfirmPass.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(!new_password.equals(confirm_password)){
                    progressBar.setVisibility(View.GONE);
                    etConfirmPass.setError("Password doesn't match");
                }
                else{
                    String mobile_no= getIntent().getStringExtra("mobileno");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("New User Data");
                    assert mobile_no != null;
                    reference.child(mobile_no).child("password").setValue(new_password);
                    startActivity(new Intent(SetNewPassword.this,ForgotPassSuccess.class));
                    finish();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    private boolean validateNewPassword() {

        if(etNewPass!=null){
            etNewPass.setError(null);
            return true;
        }else{
            etNewPass.setError("Field is empty");
            return false;
        }

    }
    private boolean validateConfirmPassword() {

        if(etConfirmPass!=null){
            etConfirmPass.setError(null);
            return true;
        }else{
            etConfirmPass.setError("Field is empty");
            return false;
        }

    }


}
