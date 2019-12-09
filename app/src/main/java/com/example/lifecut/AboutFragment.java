package com.example.lifecut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecut.Picasso.RoundTransformation;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class AboutFragment extends Fragment {
    Button nickchange;
    Button picture;
    ImageView imageView;
    ImageView imageViewBot;
    TextView photo;
    TextView record;
    TextView comment;

    TextView name;
    TextView sentence;
    List <DjangoGet> emotion;
    private TextView title;

    int size;

    private String token;

    //DatabaseHelper mDatabaseHelper;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private final int PICK_IMAGE =1;

    //listeners for setting and image change
    private FragmentSettingListener listener;
    //interface to sent String data to MainActivity
    public interface FragmentSettingListener{
        void onInputSetting(String next);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        title = (TextView)getActivity().findViewById(R.id.main_text_title);
        title.setText("Settings");

        //get token from MainActivity
        MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();
        //new MyAsyncTask().execute();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fzfv1bjqai.execute-api.ap-northeast-2.amazonaws.com/dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        imageViewBot = (ImageView)v.findViewById(R.id.image2);
        imageView= (ImageView)v.findViewById(R.id.image);

        imageViewBot.setImageResource(R.drawable.robota);
        //getData();
        GetInfo();
        GetNick();

        nickchange = (Button)v.findViewById(R.id.nickchange);
        nickchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onInputSetting("toNickCom");
            }
        });

        picture = (Button)v.findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(
                        intent, "Select Picture"), PICK_IMAGE);
            }
        });

        //mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        ArrayList<String> nickName = new ArrayList<>();
        ArrayList<String> mainComment = new ArrayList<>();

        name = v.findViewById(R.id.nickname);
        sentence = v.findViewById(R.id.anything);

        //Cursor data = mDatabaseHelper.getDataNickname();

        //while(data.moveToNext()){
        //    nickName.add(data.getString(0));
        //    mainComment.add(data.getString(1));
        //}

        //sentence.setText(nickName.get(nickName.size()));
        //name.setText(mainComment.get(mainComment.size()));

        //two font size for photo, comment, record
        photo = (TextView) v.findViewById(R.id.Photo);
        SpannableString SS = new SpannableString(" 100 \n Photos ");
        SS.setSpan(new RelativeSizeSpan(0.7f), 4, SS.length(), 0);
        photo.setText(SS);

        record = (TextView) v.findViewById(R.id.Records);
        SpannableString SS2 = new SpannableString(" 100 \n Records ");
        SS2.setSpan(new RelativeSizeSpan(0.7f), 4, SS2.length(), 0);
        record.setText(SS2);

        comment = (TextView) v.findViewById(R.id.Comment);
        SpannableString SS3 = new SpannableString(" 100 \n Comments ");
        SS3.setSpan(new RelativeSizeSpan(0.7f), 4, SS3.length(), 0);
        comment.setText(SS3);

        imageViewBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ArchiveFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.category_viewgroup_container, fragment);
                imageViewBot.setVisibility(View.GONE);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

    //send context
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AboutFragment.FragmentSettingListener) {
            listener = (AboutFragment.FragmentSettingListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement fragment listener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener =null;
    }

    //get image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {

            //store image
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = convert_UriToBitmap(selectedImageUri);
                 imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap convert_UriToBitmap(Uri selectedURI) throws IOException {
        return (Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedURI);
    }

    //Get information from Django
    public void GetInfo() {

        String tk = "TOKEN " + token;
        Call<List<DjangoGet>> call = jsonPlaceHolderApi.getPosts(tk);
        call.enqueue(new Callback<List<DjangoGet>>() {
            @Override
            public void onResponse(Call<List<DjangoGet>> call, Response<List<DjangoGet>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                emotion = response.body();
                size = response.body().size();

                SpannableString SS = new SpannableString(" "+size+" \n Photos ");
                SS.setSpan(new RelativeSizeSpan(0.7f), 4, SS.length(), 0);
                photo.setText(SS);

                SpannableString SS2 = new SpannableString(" "+size+" \n Records ");
                SS2.setSpan(new RelativeSizeSpan(0.7f), 4, SS2.length(), 0);
                record.setText(SS2);

                SpannableString SS3 = new SpannableString(" "+size+" \n Comments ");
                SS3.setSpan(new RelativeSizeSpan(0.7f), 4, SS3.length(), 0);
                comment.setText(SS3);

                if(emotion.size() != 0){
                    Picasso.get().load("https"+emotion.get(emotion.size()-1).
                            getImage_url().substring(4, emotion.get(emotion.size()-1).getImage_url().length())).
                            resize(150,150).
                            transform(new RoundTransformation(20, 0)).
                            into(imageViewBot);
                }

            }

            @Override
            public void onFailure(Call<List<DjangoGet>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    //Get information from Django
    public void GetNick() {

        String tk = "TOKEN " + token;
        Call<List<Information>> call = jsonPlaceHolderApi.getNick(tk);
        call.enqueue(new Callback<List<Information>>() {
            @Override
            public void onResponse(Call<List<Information>> call, Response<List<Information>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                List<Information> information = response.body();
                name.setText(information.get(0).nickname);
                sentence.setText(information.get(0).comment);

            }

            @Override
            public void onFailure(Call<List<Information>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }




}
