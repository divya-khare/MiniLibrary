package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.divyakhare.minilibrary.R;

import java.util.HashMap;

import database.SharedPreferenceManager;

public class UserProfile extends AppCompatActivity {

    TextView etUserName,etUserMobileNo,etUserEmail,etUserFavCategory,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        etUserName = findViewById(R.id.etUserName);
        etUserMobileNo = findViewById(R.id.etUserMobileNo);
        etUserEmail = findViewById(R.id.etUserEmail);
        etUserFavCategory = findViewById(R.id.etUserFavCategory);
        etPassword = findViewById(R.id.etPassword);

        SharedPreferenceManager sharedPref = new SharedPreferenceManager(this,SharedPreferenceManager.Preference_Users);
        HashMap<String,String> usersDetails = sharedPref.getUserDataFromPreference();
        String _name = usersDetails.get(SharedPreferenceManager.Key_name);
        String _email = usersDetails.get(SharedPreferenceManager.Key_email);
        String _favcategory = usersDetails.get(SharedPreferenceManager.Key_favcategory);
        String _mobile = usersDetails.get(SharedPreferenceManager.Key_mobile);
        String _password = usersDetails.get(SharedPreferenceManager.Key_password);

        etUserName.setText(_name);
        etUserMobileNo.setText(_mobile);
        etUserEmail.setText(_email);
        etUserFavCategory.setText(_favcategory);
        etPassword.setText(_password);
    }


    @Override
    public void onBackPressed() {
        openMainActivity();
    }

    private void openMainActivity() {
        Intent intent = new Intent(UserProfile.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
