package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import com.divyakhare.minilibrary.R;

public class ForgotPassword extends AppCompatActivity {
    EditText etUserMobileNo;
    Button btnNext;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etUserMobileNo=findViewById(R.id.etUserMobileNo);
        btnNext = findViewById(R.id.btnNext);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateMobileNo())
                { Toast.makeText(ForgotPassword.this,"Try again",Toast.LENGTH_SHORT).show();
                }
                else{
                    isUser();}
            }
        });

    }

    private void isUser() {
        final String userEnteredMobile = etUserMobileNo.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("New User Data");
        Query checkUser = reference.orderByChild("mobileno").equalTo(userEnteredMobile);
        progressBar.setVisibility(View.VISIBLE);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    etUserMobileNo.setError(null);
                    String mobileFromDB = dataSnapshot.child(userEnteredMobile).child("mobileno").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), PhoneVerification.class);
                    intent.putExtra("mobileno",mobileFromDB);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    etUserMobileNo.setError("No such user exists");
                    etUserMobileNo.requestFocus();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgotPassword.this,"Oops! Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateMobileNo() {
        String check = etUserMobileNo.getText().toString();
        if (check.length() != 10) {
            etUserMobileNo.setError("Enter 10 digit of mobile no.");
            return false;
        } else {
            etUserMobileNo.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

