package edu.csumb.gall3079.hw1solo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", 0);
        String user_name = intent.getStringExtra("user_name");
        String name = intent.getStringExtra("name");
        Log.d(TAG, "UserName HERE: " + user_name);
        Log.d(TAG, "ID HERE: " + id);
        Log.d(TAG, "Name HERE: " + name);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
        
        //check for json file output
        Log.d(TAG, String.valueOf(retrofit));

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                Log.d(TAG, "WELCOME HOME " + id + " " + name + " also known as " + user_name);
                Toast.makeText(MainActivity.this, "WELCOME HOME " + id + " " + name + " also known as " + user_name, Toast.LENGTH_LONG).show();

                String welcome = "";
                welcome += "WELCOME BACK " + name + " also known as " + user_name + "\n\n";
                textViewResult.append(welcome);

                for (Post post : posts) {
                    String content = "";
                    if (id == post.getId()) {
                        content += "ID: " + post.getId() + "\n";
                        content += "User ID: " + post.getUserId() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n\n";
                        textViewResult.append(content);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}