package com.example.lifecut;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecut.Database.DatabaseHelper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArchiveFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static List<DjangoGet> datas;
    DatabaseHelper mDatabaseHelper;

    private static Context context = null;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    List<DjangoGet> posts;

    GridView gridView;
    ProgressDialog detectionProgressDialog;
    String token;
    TextView title;

    int[] numberImage = {R.drawable.img_men_category, R.drawable.img_woman_category, R.drawable.img_men_category, R.drawable.img_woman_category, R.drawable.img_men_category, R.drawable.img_woman_category, R.drawable.img_men_category};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_archive, container, false);

        title = (TextView)getActivity().findViewById(R.id.main_text_title);
        title.setText("Records");

        detectionProgressDialog = new ProgressDialog(getActivity().getApplicationContext());
        //get token from MainActivity
        MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();
        //new MyAsyncTask().execute();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fzfv1bjqai.execute-api.ap-northeast-2.amazonaws.com/dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //getData();
        GetInfo();
        //create database helper
        // mDatabaseHelper = new DatabaseHelper(getActivity().getApplicationContext());

        context = getActivity();

        return v;

    }

    //When Emotion is clicked
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //Cursor data = mDatabaseHelper.getData();

        //Toast.makeText(getActivity().getApplicationContext(), posts.get(0).getComment(), Toast.LENGTH_SHORT).show();
        ArrayList<PostEmotion> happiness = new ArrayList<>();
        ArrayList<PostEmotion> sadness = new ArrayList<>();
        ArrayList<PostEmotion> contempt = new ArrayList<>();
        ArrayList<PostEmotion> disgust = new ArrayList<>();
        ArrayList<PostEmotion> fear = new ArrayList<>();
        ArrayList<PostEmotion> surprise = new ArrayList<>();
        ArrayList<PostEmotion> anger = new ArrayList<>();
        ArrayList<PostEmotion> neutral = new ArrayList<>();
        ArrayList<PostEmotion> all = new ArrayList<>();
        ArrayList<Double> emt = new ArrayList<>();

            /*while (data.moveToNext()) {
                //get the value from the database in column 1
                //then add it to the ArrayList
                PostEmotion emo = new PostEmotion(data.getString(0), data.getString(1), data.getString(2), data.getDouble(3), data.getDouble(4), data.getDouble(5), data.getDouble(6), data.getDouble(7), data.getDouble(8), data.getDouble(9), data.getDouble(10), data.getString(11));
                switch (emo.mainEmotion) {
                    case "happiness":
                        happiness.add(emo);
                        break;
                    case "sadness":
                        sadness.add(emo);
                        break;
                    case "contempt":
                        contempt.add(emo);
                        break;
                    case "disgust":
                        disgust.add(emo);
                        break;
                    case "fear":
                        fear.add(emo);
                        break;
                    case "surprise":
                        surprise.add(emo);
                        break;
                    case "anger":
                        anger.add(emo);
                        break;
                    case "neutral":
                        neutral.add(emo);
                        break;
                    default:
                        break;

                }
                //emotionData.add(emo);
                //Toast.makeText(getActivity().getApplicationContext(), data.getString(1),Toast.LENGTH_SHORT).show();
            }*/

        for (DjangoGet data : posts) {

            //get the value from the database in column 1
            //then add it to the ArrayList
            ZonedDateTime zdt = ZonedDateTime.parse(data.created_at);
            String newFormat = zdt.format(DateTimeFormatter.ofPattern("yyy-MM-dd"));
            //System.out.println(newFormat);
            PostEmotion emo = new PostEmotion(data.comment, newFormat, data.image_url, data.anger, data.contempt, data.disgust, data.fear, data.happiness, data.neutral, data.sadness, data.surprise, data.main_emotion);
            all.add(emo);
            switch (emo.mainEmotion) {
                case "happiness":
                    happiness.add(emo);
                    emt.add(emo.getHappiness());
                    break;
                case "sadness":
                    sadness.add(emo);
                    emt.add(emo.getSadness());
                    break;
                case "contempt":
                    contempt.add(emo);
                    emt.add(emo.getContempt());
                    break;
                case "disgust":
                    disgust.add(emo);
                    emt.add(emo.getDisgust());
                    break;
                case "fear":
                    fear.add(emo);
                    emt.add(emo.getFear());
                    break;
                case "surprise":
                    surprise.add(emo);
                    emt.add(emo.getSurprise());
                    break;
                case "anger":
                    anger.add(emo);
                    emt.add(emo.getAnger());
                    break;
                case "neutral":
                    neutral.add(emo);
                    emt.add(emo.getNeutral());
                    break;
                default:
                    break;

            }
            //emotionData.add(emo);
            //Toast.makeText(getActivity().getApplicationContext(), data.getString(1),Toast.LENGTH_SHORT).show();
        }

        switch (adapterView.getSelectedItem().toString()) {

            case "All Records":
                if (all != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter = new ArchiveAdapterTest(getActivity().getApplicationContext(), all, numberImage);
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            launchActivity(all.get(+i), emt.get(+i));
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;

            case "Happiness":
                if (happiness != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter = new ArchiveAdapterTest(getActivity().getApplicationContext(), happiness, numberImage);
                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+happiness.get(i+1).getUrl().substring(4,  happiness.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(happiness.get(+i), happiness.get(+i).happiness);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;

            case "Sadness":

                if (sadness != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter1 = new ArchiveAdapterTest(getActivity().getApplicationContext(), sadness, numberImage);
                    gridView.setAdapter(adapter1);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https" + sadness.get(i + 1).getUrl().substring(4, sadness.get(i + 1).getUrl().length()), Toast.LENGTH_SHORT).show();
                            launchActivity(sadness.get(+i), sadness.get(+i).sadness);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break; // break is optional

            case "Contempt":
                if (contempt != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter2 = new ArchiveAdapterTest(getActivity().getApplicationContext(), contempt, numberImage);
                    gridView.setAdapter(adapter2);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+happiness.get(i+1).getUrl().substring(4,  happiness.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(contempt.get(+i), contempt.get(+i).contempt);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;

            case "Fear":
                if (fear != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter4 = new ArchiveAdapterTest(getActivity().getApplicationContext(), fear, numberImage);
                    gridView.setAdapter(adapter4);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+happiness.get(i+1).getUrl().substring(4,  happiness.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(fear.get(+i), fear.get(+i).fear);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break; // break is optional

            case "Disgust":
                if (disgust != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter5 = new ArchiveAdapterTest(getActivity().getApplicationContext(), disgust, numberImage);
                    gridView.setAdapter(adapter5);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+happiness.get(i+1).getUrl().substring(4,  happiness.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(disgust.get(+i), disgust.get(+i).disgust);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;

            case "Surprise":
                if (surprise != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter6 = new ArchiveAdapterTest(getActivity().getApplicationContext(), surprise, numberImage);
                    gridView.setAdapter(adapter6);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+happiness.get(i+1).getUrl().substring(4,  happiness.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(surprise.get(+i), surprise.get(+i).surprise);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;

            case "Anger":
                if (anger != null) {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter7 = new ArchiveAdapterTest(getActivity().getApplicationContext(), anger, numberImage);
                    gridView.setAdapter(adapter7);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+happiness.get(i+1).getUrl().substring(4,  happiness.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(anger.get(+i), anger.get(+i).anger);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;

            case "Neutral":

                if (neutral != null) {

                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                    ArchiveAdapterTest adapter9 = new ArchiveAdapterTest(getActivity().getApplicationContext(), neutral, numberImage);
                    gridView.setAdapter(adapter9);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //Toast.makeText(getActivity().getApplicationContext(), "https"+neutral.get(+i).getUrl().substring(4,  neutral.get(i+1).getUrl().length()),Toast.LENGTH_SHORT).show();
                            launchActivity(neutral.get(+i), neutral.get(+i).neutral);
                        }
                    });
                } else {
                    gridView = (GridView) getActivity().findViewById(R.id.grid_view);
                }

                break;


            default:
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void launchActivity(PostEmotion img, double emo) {

        Intent mIntent = new Intent(getContext(), ShowArchiveActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("IMAGE", img);
        mBundle.putSerializable("EMOTION", emo);
        mIntent.putExtras(mBundle);

        startActivity(mIntent);

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
                posts = response.body();
                Spinner menu = (Spinner) getActivity().findViewById(R.id.spinner);
                menu.getOnItemSelectedListener();

                //Adapter for spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                        R.array.Spinner_items,
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                menu.setAdapter(adapter);
                menu.setOnItemSelectedListener(ArchiveFragment.this);
            }

            @Override
            public void onFailure(Call<List<DjangoGet>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}

