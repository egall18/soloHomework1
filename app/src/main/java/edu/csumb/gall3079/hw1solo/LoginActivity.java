package edu.csumb.gall3079.hw1solo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText username, password;
    Button login;
    public String user_name, name;
    public int id;

    //Hardcoded password
    private String pwd = "capitalswe101";
    //Array of users from api
    private List<User> trueUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        //Call on api to grab user information
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<User>> call = jsonPlaceHolderApi.getUser();

        //check for json file output
        Log.d(TAG, String.valueOf(retrofit));

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Error, response failure!", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Error, response failure" + response.code());
                    return;
                }
                //add all users into array
                List<User> users = response.body();
                trueUsers.addAll(users);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error, response Onfailure!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Error, failure" + t);
            }
        });
    }

    public void nextActivity(View view) {
        username.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));
        password.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));

        //grab the true user and check if the user exist
        for (User user : trueUsers) {
            if (user.getUsername().equals(username.getText().toString())) {
                user_name = user.getUsername();
                name = user.getName();
                id = user.getId();
                break;
            }
        }
        //call on bolean functions to check validation of credentials
        if(!emptyCheck(username.getText().toString(), password.getText().toString())) {
            Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_LONG).show();
            //set up highlight red when either username or password is wrong
            username.setBackgroundColor(0x55FF0000);
            password.setBackgroundColor(0x55FF0000);
        } else if (!usernameCheck(trueUsers, username.getText().toString(), name, id) && !passwordCheck(pwd, password.getText().toString())) {
            Log.d(TAG, "USERNAME and PWD HERE FAILED: " + user_name);
            Toast.makeText(LoginActivity.this, "Wrong username and password", Toast.LENGTH_LONG).show();
            username.setBackgroundColor(0x55FF0000);
            password.setBackgroundColor(0x55FF0000);
        } else if (!usernameCheck(trueUsers, username.getText().toString(), name, id)) {
            Log.d(TAG, "USERNAME HERE FAILED: " + user_name);
            Toast.makeText(LoginActivity.this, "Wrong username", Toast.LENGTH_LONG).show();
            username.setBackgroundColor(0x55FF0000);
        } else if (!passwordCheck(pwd, password.getText().toString())) {
            Log.d(TAG, "PASSWORD HERE FAILED: " + pwd);
            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
            password.setBackgroundColor(0x55FF0000);
        } else {
            //pass in the info of user to next activity
            password.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user_name", user_name);
            intent.putExtra("name", name);
            intent.putExtra("id", id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public static boolean usernameCheck(List<User> trueUsers, String user_name, String name, int id){
        for (User user: trueUsers) {
            if (user.getUsername().equals(user_name)) {
                user_name = user.getUsername();
                name = user.getName();
                id = user.getId();
                return true;
            }
        }
        return false;
    }
    public static boolean passwordCheck(String pwd, String password){
        if (pwd.equals(password)) {
            return true;
        }
        return false;
    }
    public static boolean emptyCheck(String str_username, String str_password) {
        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)) {
            return false;
        }
        return true;
    }
}

