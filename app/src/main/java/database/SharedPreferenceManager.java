package database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharedPreferenceManager {
     SharedPreferences users_shared_preference;
     SharedPreferences.Editor editor;
     Context context;

    public static final String Preference_Users = "MiniLibraryLoginPreference";
    public static final String Preference_KeepMeLogIn = "KeepMeLogIn";

    private static final String Is_Login = "IsLoggedIn";

    public static final String Key_name = "name";
    public static final String Key_email = "email";
    public static final String Key_mobile = "mobileno";
    public static final String Key_password = "password";
    public static final String Key_favcategory = "favcategory";

    private static final String KeepLoggedIn = "KeepLoggedIn";
    public static final String Key_loggedmobile = "mobileno";
    public static final String Key_loggedpassword = "password";


    public SharedPreferenceManager(Context _context,String preference_name){
        context =_context;
        users_shared_preference = _context.getSharedPreferences(preference_name,Context.MODE_PRIVATE);
        editor=users_shared_preference.edit();
    }
    public void createLoginPreference(String name, String email, String favcategory, String mobileno, String password){
        editor.putBoolean(Is_Login,true);
        editor.putString(Key_name,name);
        editor.putString(Key_email,email);
        editor.putString(Key_favcategory,favcategory);
        editor.putString(Key_mobile,mobileno);
        editor.putString(Key_password,password);
        editor.commit();
    }
    public HashMap<String,String> getUserDataFromPreference(){
        HashMap<String,String> usersData = new HashMap<String, String>();
        usersData.put(Key_name,users_shared_preference.getString(Key_name,null));
        usersData.put(Key_email,users_shared_preference.getString(Key_email,null));
        usersData.put(Key_favcategory,users_shared_preference.getString(Key_favcategory,null));
        usersData.put(Key_mobile,users_shared_preference.getString(Key_mobile,null));
        usersData.put(Key_password,users_shared_preference.getString(Key_password,null));
        return usersData;
    }
    public boolean checkLogIn(){
        return users_shared_preference.getBoolean(Is_Login, false);
    }
    public void logoutUserFromPreference(){
        editor.clear();
        editor.commit();
    }
    public void createKeepmeLoggedInPreference(String mobileno, String password){
        editor.putBoolean(KeepLoggedIn,true);
        editor.putString(Key_loggedmobile,mobileno);
        editor.putString(Key_loggedpassword,password);
        editor.commit();
    }
    public HashMap<String,String> getKeepLoggedInDataFromPreference(){
        HashMap<String,String> usersData = new HashMap<String, String>();
        usersData.put(Key_loggedmobile,users_shared_preference.getString(Key_loggedmobile,null));
        usersData.put(Key_loggedpassword,users_shared_preference.getString(Key_loggedpassword,null));
        return usersData;
    }
    public boolean checkKeepLogIn(){
        return users_shared_preference.getBoolean(KeepLoggedIn, false);
    }

}
