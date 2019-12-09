package com.example.lifecut;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends Fragment {

    private Button tologin;
    private Button toinfo;
    private FragmentSignupListener listener;
    private SignupInfo signupinfo;
    private EditText email;
    private EditText password;
    private EditText confirm;

    private String token;

    public interface FragmentSignupListener{
        void onInputSignup(SignupInfo signup);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        toinfo = (Button)v.findViewById(R.id.next);
        toinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email =(EditText)v.findViewById(R.id.email);
                password =(EditText)v.findViewById(R.id.password);
                confirm =(EditText)v.findViewById(R.id.confirm);
                //send signup information to LoginActivity
                if(!email.getText().toString().matches("") && !confirm.getText().toString().matches("") && !password.getText().toString().matches("")){
                    signupinfo = new SignupInfo(email.getText().toString(), password.getText().toString(), confirm.getText().toString());
                    if(signupinfo.getPassword().equals(signupinfo.getConfirm())){
                        listener.onInputSignup(signupinfo);
                    }
                    else{ Toast.makeText(v.getContext(),"Please check password and confirm password",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(v.getContext(),"Please input information",Toast.LENGTH_SHORT).show();
                }
                /*getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.category_logincontainer,
                        new InformationFragment()).commit();*/
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
        if(context instanceof SignUpFragment.FragmentSignupListener){

            listener = (SignUpFragment.FragmentSignupListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement fragment listener");
        }

    }

}