package com.example.lifecut;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    private Button tosignup;
    private  Button tomain;

    private EditText username;
    private EditText password;

    private UserPass userpass;

    private FragmentLogListener listener;

    public interface FragmentLogListener{
        void onInputLogin(UserPass userpass);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_login, container, false);

        tosignup = (Button)v.findViewById(R.id.next);
        tomain = (Button)v.findViewById(R.id.next4);
        username = (EditText)v.findViewById(R.id.Username);
        password = (EditText)v.findViewById(R.id.Password);


        tosignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.category_logincontainer,
                        new SignUpFragment()).commit();
            }
        });

        tomain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                userpass = new UserPass(username.getText().toString(), password.getText().toString());
                listener.onInputLogin(userpass);
            }
        });

        return v;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener =null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragment.FragmentLogListener){

            listener = (LoginFragment.FragmentLogListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement fragment listener");
        }

    }


}