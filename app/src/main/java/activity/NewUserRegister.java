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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import model.NewUserHelperClass;

public class NewUserRegister extends AppCompatActivity {

    EditText etName,etEmailId,etMobileNo,etFavCategory,etPassword;
    Button btnSignUp;
    ProgressBar progressBar;
    FirebaseDatabase rootnode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_register);
        etName=findViewById(R.id.etName);
        etEmailId=findViewById(R.id.etEmailId);
        etMobileNo=findViewById(R.id.etMobileNo);
        etFavCategory=findViewById(R.id.etFavCategory);
        etPassword=findViewById(R.id.etPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateName()|!validateEmail()|!validateFavCat()|!validateMobile()|!validatePass()){
                    Toast.makeText(NewUserRegister.this,"Try Again!!",Toast.LENGTH_SHORT).show(); ;}
                else{
                NewUser();}
            }
        });
    }


    private void NewUser() {
        rootnode = FirebaseDatabase.getInstance();
        final DatabaseReference reference = rootnode.getReference("New User Data");

        final String name = etName.getText().toString();
        final String email = etEmailId.getText().toString();
        final String mobileno = etMobileNo.getText().toString();
        final String favcategory = etFavCategory.getText().toString();
        final String password = etPassword.getText().toString();
        Query checkUser = reference.orderByChild("mobileno").equalTo(mobileno);
        progressBar.setVisibility(View.VISIBLE);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(NewUserRegister.this,"User already exists",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(NewUserRegister.this,OtpVerification.class);
                    intent.putExtra("mobileno",mobileno);
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("favcategory",favcategory);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewUserRegister.this,"Oops! Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean validateName(){
        String name = etName.getText().toString();
        if(name.isEmpty()){
            etName.setError("Field cannot be empty");
            return false;
        }
        else{
            etName.setError(null);
            return true;
        }
    }
    private boolean validateMobile(){
        String check = etMobileNo.getText().toString();
        if(check.length()<10){
            etMobileNo.setError("Enter 10 digit mobile No.");
            return false;
        }
        else{
            etMobileNo.setError(null);
            return true;
        }
    }
    private boolean validateEmail(){
        String check = etEmailId.getText().toString();
        if(check.isEmpty()){
            etEmailId.setError("Field cannot be empty");
            return false;
        }
        else{
            etEmailId.setError(null);
            return true;
        }
    }
    private boolean validateFavCat(){
        String check = etFavCategory.getText().toString();
        if(check.isEmpty()){
            etFavCategory.setError("Field cannot be empty");
            return false;
        }
        else{
            etFavCategory.setError(null);
            return true;
        }
    }
    private boolean validatePass(){
        String check = etPassword.getText().toString();
        if(check.isEmpty()){
            etPassword.setError("Field cannot be empty");
            return false;
        }
        else{
            etPassword.setError(null);
            return true;
        }
    }
}
