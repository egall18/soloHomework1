package edu.csumb.gall3079.hw1solo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText username, password;
    Button login;
    public static String cred;
    public static String user_name, name;
    public int id;
    private String pwd = "capitalswe101";

    //grab the username from the api and set up random passwords and save those to loacl computer
    //check using this map to validate user information in login page.
    //pass in the usernmae, id, name as an intent or pass in to mainactivity to display that informaiton.
    //passwords can be constant and hardcoded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_username = username.getText().toString();
                String str_password = password.getText().toString();
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
                            Log.d(TAG, "Error, response failure" + response.code());
                            return;
                        }

                        List<User> users = response.body();

                        for (User user : users) {
                            Log.d(TAG, "Usernames HERE Test: " + user.getUsername());
                            if (user.getUsername().equals(str_username)) {
                                user_name = user.getUsername();
                                name = user.getName();
                                id = user.getId();
                                break;
                            }
                        }
                        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)){
                            Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                            //set up highlight red when either username or password is wrong

                        } else if (!str_username.equals(user_name)) {
                            Log.d(TAG, "USERNAME HERE FAILED: " + user_name);
                            Toast.makeText(LoginActivity.this, "Wrong username", Toast.LENGTH_SHORT).show();
                        } else if (!str_password.equals(pwd)) {
                            Log.d(TAG, "PASSWORD HERE FAILED: " + pwd);
                            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "LOGGED IN!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_name", user_name);
                            intent.putExtra("name", name);
                            intent.putExtra("id", id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        Log.d(TAG, "REAL USERNAME HERE: " + user_name);
                    }
                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.d(TAG, "Error, failure" + t);
                    }
                });
            }
        });

}}
