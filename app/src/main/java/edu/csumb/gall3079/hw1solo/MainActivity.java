package edu.csumb.gall3079.hw1solo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        logout = findViewById(R.id.logout);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", 0);
        String user_name = intent.getStringExtra("user_name");
        String name = intent.getStringExtra("name");

        //call on post api to grab the post of the logged in user
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                Toast.makeText(MainActivity.this, "WELCOME HOME " + id + " " + name + " also known as " + user_name, Toast.LENGTH_LONG).show();

                String welcome = "";
                welcome += "WELCOME BACK " + name + " also known as " + user_name + "\n\n";
                textViewResult.append(welcome);
                for (Post post : posts) {
                    if (id == post.getUserId()) {
                        String content = "";
                        content += "ID: " + post.getId() + "\n";
                        content += "User ID: " + post.getUserId() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n\n";
                        textViewResult.append(content);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
        //logout of user and go back into login page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "LOGGED OUT!!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}