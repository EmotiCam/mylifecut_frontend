package com.example.lifecut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity
        implements LoginFragment.FragmentLogListener, InformationFragment.FragmentInfoListener, SignUpFragment.FragmentSignupListener{

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Map config;

    String id;
    String user;

    String token = "N/A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //cloudinary
        MediaManager.init(this);

        //display first fragment as about
        getSupportFragmentManager().beginTransaction().replace(R.id.category_logincontainer,
                new LoginFragment()).commit();

        //client request for backend server
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://fzfv1bjqai.execute-api.ap-northeast-2.amazonaws.com/dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

    }

    //send login information to back
    @Override
    public void onInputLogin(UserPass userpass) {

        PostInfo(userpass);

    }

    //send signup information to back
    @Override
    public void onInputSignup(SignupInfo signup) {

        PostSignup(signup);

    }

    @Override
    public void onInputInformation(Information s) {

        PostInformation(s);
    }

    private void launchActivityFrom() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", token);
        startActivity(intent);
    }


    //Post Login in Django
    public void PostInfo(UserPass usr){

        UserPass post = new UserPass(usr.username,usr.password);

        Call<User> call =jsonPlaceHolderApi.login(post);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //not successful
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                token = response.body().getKey();
                Toast.makeText(getApplication(), token, Toast.LENGTH_LONG).show();
                launchActivityFrom();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplication(), t.getCause().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    //Post Login in Django
    public void PostSignup(SignupInfo usr){

        UserSign post = new UserSign(usr.email, usr.password, usr.confirm);
        Call<User> call =jsonPlaceHolderApi.registration(post);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //not successful
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                User resp = new User(response.body().getKey());
                token = response.body().getKey();
                Toast.makeText(getApplication(), resp.getKey() , Toast.LENGTH_LONG).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.category_logincontainer, new InformationFragment()).commit();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplication(), t.getCause().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    //Post Login in Django
    public void PostInformation(Information information){

        Information post = new Information(information.comment,information.nickname, information.gender);
        //Toast.makeText(getApplication(), token, Toast.LENGTH_LONG).show();

        String tokens = "TOKEN "+token;
        Call<Information> call =jsonPlaceHolderApi.createProfile(post, tokens);

        call.enqueue(new Callback<Information>() {
            @Override
            public void onResponse(Call<Information> call, Response<Information> response) {
                //not successful
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                Information info = response.body();
                Toast.makeText(getApplication(), info.gender, Toast.LENGTH_LONG).show();
                launchActivityFrom();

            }

            @Override
            public void onFailure(Call<Information> call, Throwable t) {
                Toast.makeText(getApplication(), t.getCause().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }




    public String getToken(){
        return token;
    }

    public String getProfile(){
        return token;
    }

}
