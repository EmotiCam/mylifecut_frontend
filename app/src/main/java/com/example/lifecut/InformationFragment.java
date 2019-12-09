package com.example.lifecut;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InformationFragment extends Fragment {

    private Button tomain;
    EditText nickname;
    RadioGroup gender;
    RadioButton sex;
    Information info;
    private String token;


    //DatabaseHelper mDatabaseHelper;

    private InformationFragment.FragmentInfoListener listener;

    public interface FragmentInfoListener{
        void onInputInformation(Information information);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_information, container, false);


       // mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        tomain = (Button)v.findViewById(R.id.next);
        tomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = (EditText)v.findViewById(R.id.Nickname);
                gender = (RadioGroup)v.findViewById(R.id.gender);
                // get selected radio button from radioGroup
                int selectedId = gender.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                sex = (RadioButton) v.findViewById(selectedId);


                if(!nickname.getText().toString().matches("") && !sex.getText().toString().matches("")){
                    info = new Information("Welcome to the life4Cut Application",nickname.getText().toString(), sex.getText().toString().toUpperCase());
                    //mDatabaseHelper.addDataNickname(nickname.getText().toString(), sex.getText().toString());
                    listener.onInputInformation(info);
                }
                else{
                    Toast.makeText(v.getContext(),"nickname or gender not specified",Toast.LENGTH_SHORT).show();
                }
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
        if(context instanceof InformationFragment.FragmentInfoListener){
            listener = (InformationFragment.FragmentInfoListener) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement fragment listener");
        }
    }

}