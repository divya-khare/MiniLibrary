package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.divyakhare.minilibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import database.SharedPreferenceManager;
import util.ConnectionManager;

public class Login extends AppCompatActivity {

    EditText etUserMobileNo,etPassword;
    Button btnLogIn;
    TextView txtForgetPassword,txtSignUp;
    ProgressBar progressBar;
    CheckBox checkboxLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserMobileNo=findViewById(R.id.etUserMobileNo);
        etPassword=findViewById(R.id.etPassword);
        btnLogIn=findViewById(R.id.btnLogin);
        txtForgetPassword=findViewById(R.id.txtForgetPassword);
        txtSignUp=findViewById(R.id.txtSignUp);
        checkboxLogin=findViewById(R.id.checkboxLogin);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        SharedPreferenceManager sharedPreference = new SharedPreferenceManager(Login.this,SharedPreferenceManager.Preference_KeepMeLogIn);
        if(sharedPreference.checkKeepLogIn()){
            HashMap<String,String> keepLoggedInData = sharedPreference.getKeepLoggedInDataFromPreference();
            etUserMobileNo.setText(keepLoggedInData.get(SharedPreferenceManager.Key_loggedmobile));
            etPassword.setText(keepLoggedInData.get(SharedPreferenceManager.Key_loggedpassword));
        }

            checkConnection();
            btnLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!validateMobileNo() | !validatePassword()) {
                        Toast.makeText(Login.this, "Fill both the details", Toast.LENGTH_LONG).show();
                    } else {
                        isUser();
                    }
                }
            });


        txtSignUp.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Login.this,NewUserRegister.class);
                startActivity(intent);
            }
        });
        txtForgetPassword.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

    }




    private void isUser() {
        final String userEnteredMobile = etUserMobileNo.getText().toString();
        final String userEnteredPassword = etPassword.getText().toString();
        if(checkboxLogin.isChecked()){
            SharedPreferenceManager sharedPreference = new SharedPreferenceManager(Login.this,SharedPreferenceManager.Preference_KeepMeLogIn);
            sharedPreference.createKeepmeLoggedInPreference(userEnteredMobile,userEnteredPassword);

        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("New User Data");
        Query checkUser = reference.orderByChild("mobileno").equalTo(userEnteredMobile);
        progressBar.setVisibility(View.VISIBLE);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    etUserMobileNo.setError(null);

                    String passwordFromDB = dataSnapshot.child(userEnteredMobile).child("password").getValue(String.class);
                    if(passwordFromDB.equals(userEnteredPassword)){
                        String mobileFromDB = dataSnapshot.child(userEnteredMobile).child("mobileno").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredMobile).child("email").getValue(String.class);
                        String favcategoryFromDB = dataSnapshot.child(userEnteredMobile).child("favcategory").getValue(String.class);
                        String nameFromDB = dataSnapshot.child(userEnteredMobile).child("name").getValue(String.class);


                        SharedPreferenceManager sharedPreference = new SharedPreferenceManager(Login.this,SharedPreferenceManager.Preference_Users);
                        sharedPreference.createLoginPreference(nameFromDB,emailFromDB,favcategoryFromDB,mobileFromDB,passwordFromDB);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);

                    }else{
                        progressBar.setVisibility(View.GONE);
                        etPassword.setError("Wrong password");
                        etPassword.requestFocus();
                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                    etUserMobileNo.setError("No such user exists");
                    etUserMobileNo.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this,"Oops! Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateMobileNo(){
        String check = etUserMobileNo.getText().toString();
        if(check.length()!=10){
            etUserMobileNo.setError("Enter 10 digit of mobile no.");
            return false;
        }else{
            etUserMobileNo.setError(null);
            return true;
        }
    }
    private Boolean validatePassword(){
        String check = etPassword.getText().toString();
        if(check.length()<=3){
            etPassword.setError("Enter 4 or more characters");
            return false;
        }else{
            etPassword.setError(null);
            return true;
        }
    }
    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(null!=activeNetwork){
            if((activeNetwork.getType()==ConnectivityManager.TYPE_WIFI)|(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE)){
                Toast.makeText(Login.this, "Good to proceed", Toast.LENGTH_SHORT).show();
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setTitle("Error")
                    .setMessage("Internet connection not found")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create();
            builder.show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


}
