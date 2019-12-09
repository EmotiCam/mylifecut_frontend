package com.example.lifecut;

import android.content.Intent;
import android.os.Bundle;
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

public class ActivityChangeNick extends AppCompatActivity {
    Button next;
    EditText nickname;
    EditText comments;
    Information2 information;

    String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private static final String TAG = "MyActivity";
    int id;

    //DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changenick);

        nickname = findViewById(R.id.nickname);
        comments = findViewById(R.id.comments);


        try{
            token = getIntent().getStringExtra("TOKEN");

        }

        catch(Exception e){
            Log.i(TAG, "Failed");
        }

        //Toast.makeText(getApplication(), token, Toast.LENGTH_LONG).show();


        //mDatabaseHelper = new DatabaseHelper(this);

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://fzfv1bjqai.execute-api.ap-northeast-2.amazonaws.com/dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        GetId();

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send changed data to MainActivity
                if(nickname.getText()!=null && comments.getText()!=null){
                    Log.i(TAG, nickname.getText().toString() + "    " +comments.getText().toString());
                    updateNick(comments.getText().toString(),nickname.getText().toString(),"MALE");
                }
                else{
                    Toast.makeText(getApplication(), "No information changed" , Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    /*public void AddDataNickname(String nickName, String mainComment) {
        boolean insertData = mDatabaseHelper.addDataNickname( nickName, mainComment);

        if (insertData) {
            Toast.makeText(getApplication(), "Data Inserted" , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplication(), "Error" , Toast.LENGTH_LONG).show();
        }
    }*/

    //Get information from Django
    public void updateNick(String nickname, String comment, String gender) {

        String tk = "TOKEN " + token;
        Information info = new Information(comment, nickname, gender);

        Call<Information> call = jsonPlaceHolderApi.changeProfile(id,info, tk);
        call.enqueue(new Callback<Information>() {
            @Override
            public void onResponse(Call<Information> call, Response<Information> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Log.i(TAG, "Code: " + response.code());
                    //Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i(TAG, response.body().nickname);
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", token);
                startActivity(intent);

                //get data
                //List<Information> information = response.body();

            }

            @Override
            public void onFailure(Call<Information> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });

    }

    //Get information from Django
    public void GetId() {

        String tk = "TOKEN " + token;
        Call<List<Information2>> call = jsonPlaceHolderApi.getId(tk);
        call.enqueue(new Callback<List<Information2>>() {
            @Override
            public void onResponse(Call<List<Information2>> call, Response<List<Information2>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                List<Information2> information = response.body();
                id= information.get(0).getId();
                Log.i(TAG, "id: "+id);
                //Toast.makeText(getApplication(), id, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<List<Information2>> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });

    }


}
