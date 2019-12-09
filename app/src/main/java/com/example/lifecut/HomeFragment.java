package com.example.lifecut;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifecut.Database.DatabaseHelper;
import com.example.lifecut.JsonPlacement.JsonPlaceHolderApi;
import com.example.lifecut.ObjectFiles.Chat;
import com.example.lifecut.graphitems.BarChartItem;
import com.example.lifecut.graphitems.ChartItem;
import com.example.lifecut.graphitems.LineChartItem;
import com.example.lifecut.graphitems.RadialChartItem;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    //Linear Graph information


    private JsonPlaceHolderApi jsonPlaceHolderApi;
    List<DjangoGet> posts;
    String token;
    List<Chat> chat;
    TextView ai;

    int r;
    private static Context context = null;

    DatabaseHelper mDatabaseHelper;
    ListView list;

    ArrayList<Double> happiness = new ArrayList<>();
    ArrayList<Double> sadness = new ArrayList<>();
    ArrayList<Double> contempt = new ArrayList<>();
    ArrayList<Double> disgust = new ArrayList<>();
    ArrayList<Double> fear = new ArrayList<>();
    ArrayList<Double> surprise = new ArrayList<>();
    ArrayList<Double> anger = new ArrayList<>();
    ArrayList<Double> neutral = new ArrayList<>();

    //count for each emotion
    double happinessc = 0.0;
    double sadnessc = 0.0;
    double contemptc = 0.0;
    double disgustc = 0.0;
    double fearc = 0.0;
    double surprisec = 0.0;
    double angerc = 0.0;
    double neutralc = 0.0;

    double happinessr = 0.0;
    double sadnessr = 0.0;
    double contemptr = 0.0;
    double disgustr = 0.0;
    double fearr = 0.0;
    double surpriser = 0.0;
    double angerr = 0.0;
    double neutralr = 0.0;
    double emotionSum = 0.0;

    int count = 0;
    TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        title = (TextView)getActivity().findViewById(R.id.main_text_title);
        title.setText("Home");

        //get token from MainActivity
        MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();
        ai = v.findViewById(R.id.ai);


        //Django base url call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fzfv1bjqai.execute-api.ap-northeast-2.amazonaws.com/dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        GetInfo();

        //mDatabaseHelper = new DatabaseHelper(getContext().getApplicationContext());
        //Cursor data = mDatabaseHelper.getData();


        /*while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            PostEmotion emo = new PostEmotion(data.getString(0), data.getString(1), data.getString(2), data.getDouble(3), data.getDouble(4), data.getDouble(5), data.getDouble(6), data.getDouble(7), data.getDouble(8), data.getDouble(9), data.getDouble(10), data.getString(11));
            //switch (emo.mainEmotion) {
            //  case "happiness":
            happiness.add(emo.getHappiness() * 100);
            happinessc += emo.getHappiness();
            emotionSum += emo.getHappiness();
            count++;
            //  break;
            //case "sadness":
            sadness.add(emo.getSadness() * 100);
            sadnessc += emo.getSadness();
            emotionSum += emo.getSadness();
            count++;
            //break;
            //case "contempt":
            contempt.add(emo.getContempt() * 100);
            contemptc += emo.getContempt();
            emotionSum += emo.getContempt();
            count++;
            //  break;
            //case "disgust":
            disgust.add(emo.getDisgust() * 100);
            disgustc += emo.getDisgust();
            emotionSum += emo.getDisgust();
            count++;
            //  break;
            //case "fear":
            fear.add(emo.getFear() * 100);
            fearc += emo.getFear();
            emotionSum += emo.getFear();
            count++;
            //  break;
            //case "surprise":
            surprise.add(emo.getSurprise() * 100);
            surprisec += emo.getSurprise();
            emotionSum += emo.getSurprise();
            count++;
            //  break;
            //case "anger":
            anger.add(emo.getAnger() * 100);
            angerc += emo.getAnger();
            emotionSum += emo.getAnger();
            count++;
            //  break;
            //case "neutral":
            neutral.add(emo.getNeutral() * 100);
            neutralc += emo.getNeutral();
            emotionSum += emo.getNeutral();
            count++;
            //  break;
            // default:
            //   break;
            //}
        }*/


        return v;

    }

    public void solve() {

        //clear data to prevent stacking data
        happiness.clear();
        sadness.clear();
        contempt.clear();
        disgust.clear();
        fear.clear();
        anger.clear();
        neutral.clear();
        surprise.clear();

        for (DjangoGet data : posts) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            PostEmotion emo = new PostEmotion(data.comment, data.created_at, data.image_url, data.anger, data.contempt, data.disgust, data.fear, data.happiness, data.neutral, data.sadness, data.surprise, data.main_emotion);

            happiness.add(emo.getHappiness() * 100);
            happinessc += emo.getHappiness();
            emotionSum += emo.getHappiness();
            count++;

            sadness.add(emo.getSadness() * 100);
            sadnessc += emo.getSadness();
            emotionSum += emo.getSadness();
            count++;

            contempt.add(emo.getContempt() * 100);
            contemptc += emo.getContempt();
            emotionSum += emo.getContempt();
            count++;

            disgust.add(emo.getDisgust() * 100);
            disgustc += emo.getDisgust();
            emotionSum += emo.getDisgust();
            count++;

            fear.add(emo.getFear() * 100);
            fearc += emo.getFear();
            emotionSum += emo.getFear();
            count++;

            surprise.add(emo.getSurprise() * 100);
            surprisec += emo.getSurprise();
            emotionSum += emo.getSurprise();
            count++;

            anger.add(emo.getAnger() * 100);
            angerc += emo.getAnger();
            emotionSum += emo.getAnger();
            count++;

            neutral.add(emo.getNeutral() * 100);
            neutralc += emo.getNeutral();
            emotionSum += emo.getNeutral();
            count++;
        }

        //emotion(%) = emotion/totalemotion * 100
        happinessr = happinessc / emotionSum * 100;
        sadnessr = sadnessc / emotionSum * 100;
        contemptr = contemptc / emotionSum * 100;
        disgustr = disgustc / emotionSum * 100;
        fearr = fearc / emotionSum * 100;
        surpriser = surprisec / emotionSum * 100;
        angerr = angerc / emotionSum * 100;
        neutralr = neutralc / emotionSum * 100;

        Random rn = new Random();
        r = rn.nextInt(5 - 1 + 1) + 1;

        //get main emotion for AI
        if(posts.size()!=0)
        {
            String main = posts.get(posts.size() - 1).getMain_emotion();
            Toast.makeText(getContext().getApplicationContext(), "Your latest emotion is: "+main, Toast.LENGTH_LONG).show();
            switch (main) {
                case "happiness":
                    getHappiness();
                    break;

                case "sadness":
                    getSadness();
                    break; // break is optional

                case "contempt":
                    getContempt();
                    break;

                case "fear":
                    getFear();
                    break; // break is optional

                case "disgust":
                    getDisgust();
                    break;

                case "surprise":
                    getSurprise();
                    break;

                case "anger":
                    getAnger();
                    break;

                case "neutral":
                    getNeutral();
                    break;

                default:
                    break;

            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        solve();
        switch (adapterView.getSelectedItem().toString()) {
            case "Happiness":
                ArrayList<ChartItem> list = new ArrayList<>();
                ListView lv = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list.add(new LineChartItem(generateDataLine(k + 1, "Happiness", happiness), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }
                ChartDataAdapter cda = new ChartDataAdapter(getContext().getApplicationContext(), list);
                lv.setAdapter(cda);
                break;

            case "Sadness":
                ArrayList<ChartItem> list2 = new ArrayList<>();
                ListView lv2 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list2.add(new LineChartItem(generateDataLine(k + 1, "Sadness", sadness), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list2.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list2.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }
                ChartDataAdapter cda2 = new ChartDataAdapter(getContext().getApplicationContext(), list2);
                lv2.setAdapter(cda2);
                break; // break is optional

            case "Contempt":
                ArrayList<ChartItem> list3 = new ArrayList<>();
                ListView lv3 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list3.add(new LineChartItem(generateDataLine(k + 1, "Contempt", contempt), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list3.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list3.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }
                ChartDataAdapter cda3 = new ChartDataAdapter(getContext().getApplicationContext(), list3);
                lv3.setAdapter(cda3);
                break;

            case "Fear":
                ArrayList<ChartItem> list4 = new ArrayList<>();
                ListView lv4 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list4.add(new LineChartItem(generateDataLine(k + 1, "Fear", fear), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list4.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list4.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }
                ChartDataAdapter cda4 = new ChartDataAdapter(getContext().getApplicationContext(), list4);
                lv4.setAdapter(cda4);

                break; // break is optional

            case "Disgust":
                ArrayList<ChartItem> list5 = new ArrayList<>();
                ListView lv5 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list5.add(new LineChartItem(generateDataLine(k + 1, "Disgust", disgust), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list5.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list5.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }

                ChartDataAdapter cda5 = new ChartDataAdapter(getContext().getApplicationContext(), list5);
                lv5.setAdapter(cda5);
                break;

            case "Surprise":
                ArrayList<ChartItem> list6 = new ArrayList<>();
                ListView lv6 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list6.add(new LineChartItem(generateDataLine(k + 1, "Surprise", surprise), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list6.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list6.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }

                ChartDataAdapter cda6 = new ChartDataAdapter(getContext().getApplicationContext(), list6);
                lv6.setAdapter(cda6);

                break;

            case "Anger":
                ArrayList<ChartItem> list7 = new ArrayList<>();
                ListView lv7 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list7.add(new LineChartItem(generateDataLine(k + 1, "Anger", anger), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list7.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list7.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }

                ChartDataAdapter cda7 = new ChartDataAdapter(getContext().getApplicationContext(), list7);
                lv7.setAdapter(cda7);

                break;

            case "Neutral":
                ArrayList<ChartItem> list8 = new ArrayList<>();
                ListView lv8 = (ListView) getActivity().findViewById(R.id.listView1);

                // 30 items
                for (int k = 0; k < 3; k++) {

                    if (k % 3 == 0) {
                        list8.add(new LineChartItem(generateDataLine(k + 1, "Neutral", neutral), getContext().getApplicationContext()));
                    } else if (k % 3 == 1) {
                        list8.add(new BarChartItem(generateDataBar(k + 1), getContext().getApplicationContext()));
                    } else if (k % 3 == 2) {
                        list8.add(new RadialChartItem(generateRadarData(), getContext().getApplicationContext()));
                    }
                }

                ChartDataAdapter cda8 = new ChartDataAdapter(getContext().getApplicationContext(), list8);
                lv8.setAdapter(cda8);

                break;


            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     */
    private LineData generateDataLine(int cnt, String name, ArrayList<Double> mainEmotion) {

        ArrayList<Entry> values1 = new ArrayList<>();

        //int[] val = {(int) happinessr, (int) sadnessr, (int) surpriser, (int) fearr, (int) neutralr, (int) contemptr, (int) angerr, (int) disgustr};

        for (int i = 0; i < mainEmotion.size(); i++) {
            values1.add(new Entry(i, mainEmotion.get(i).intValue()));
        }

        LineDataSet d1 = new LineDataSet(values1, name);
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        //Toast.makeText(getContext().getApplicationContext(), name, Toast.LENGTH_LONG).show();

        switch (name) {
            case "Happiness":
                d1.setColor(Color.rgb(189, 237, 255));
                d1.setCircleColors(Color.rgb(189, 237, 255));
                break;
            case "Sadness":
                d1.setColor(Color.rgb(74, 88, 176));
                d1.setCircleColors(Color.rgb(74, 88, 176));
                break;
            case "Contempt":
                d1.setColor(Color.rgb(242, 5, 92));
                d1.setCircleColors(Color.rgb(242, 5, 92));
                break;
            case "Disgust":
                d1.setColor(Color.rgb(79, 139, 62));
                d1.setCircleColors(Color.rgb(79, 139, 62));
                break;
            case "Fear":
                d1.setColor(Color.rgb(89, 72, 122));
                d1.setCircleColors(Color.rgb(89, 72, 122));
                break;
            case "Surprise":
                d1.setColor(Color.rgb(205, 185, 39));
                d1.setCircleColors(Color.rgb(205, 185, 39));
                break;
            case "Anger":
                d1.setColor(Color.rgb(189, 73, 84));
                d1.setCircleColors(Color.rgb(189, 73, 84));
                break;
            case "Neutral":
                d1.setColor(Color.rgb(220, 235, 221));
                d1.setCircleColors(Color.rgb(220, 235, 221));
                break;
            default:
                break;

        }

        d1.setHighLightColor(Color.rgb(255, 0, 0));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < mainEmotion.size(); i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        //sets.add(d2);

        return new LineData(sets);
    }

    private RadarData generateRadarData() {
        float mul = 80;
        float min = 20;
        int cnt = 8;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();

        ArrayList<RadarEntry> entries3 = new ArrayList<>();

        //"Anger", "Happiness", "Sadness", "Contempt", "Normal", "Disgust","Surprise","Fear"
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(val1));

            // float val2 = (float) (Math.random() * mul) + min;
            //entries2.add(new RadarEntry(val2));
        }
        entries3.add(new RadarEntry((float) happinessr));
        entries3.add(new RadarEntry((float) sadnessr));
        entries3.add(new RadarEntry((float) surpriser));
        entries3.add(new RadarEntry((float) fearr));
        entries3.add(new RadarEntry((float) neutralr));
        entries3.add(new RadarEntry((float) contemptr));
        entries3.add(new RadarEntry((float) angerr));
        entries3.add(new RadarEntry((float) disgustr));

        RadarDataSet set1 = new RadarDataSet(entries3, "This Week");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        //RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        //set2.setColor(Color.rgb(121, 162, 175));
        //set2.setFillColor(Color.rgb(121, 162, 175));
        //set2.setDrawFilled(true);
        //set2.setFillAlpha(180);
        //set2.setLineWidth(2f);
        //set2.setDrawHighlightCircleEnabled(true);
        //set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        //sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        return data;
    }


    //bar graph
    private BarData generateDataBar(int cnt) {

        float[] val = {(float) happinessr, (float) sadnessr, (float) surpriser, (float) fearr, (float) neutralr, (float) contemptr, (float) angerr, (float) disgustr};

        String[] emotions = {"Happy", "Sad", "Surprise", "Fear", "Neutral", "Contempt", "Anger", "Disgust"};
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            entries.add(new BarEntry(i, val[i]));
        }

        BarDataSet d = new BarDataSet(entries, "Emotions");
        d.setColors(RAINBOW_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    public static final int[] RAINBOW_COLORS = {
            Color.rgb(189, 237, 255), Color.rgb(74, 88, 176), Color.rgb(205, 185, 39),
            Color.rgb(89, 72, 122), Color.rgb(220, 235, 221), Color.rgb(242, 5, 92),
            Color.rgb(189, 73, 84), Color.rgb(79, 139, 62)
    };

    //check is day is in current week
    public static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
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
                    Log.i(TAG, "Code: " + response.code());
                    return;
                }
                //get data
                posts = response.body();

                //Adapter for spinner
                Spinner menu = (Spinner) getActivity().findViewById(R.id.spinnerhome);
                menu.getOnItemSelectedListener();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                        R.array.Spinner_items_home,
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                menu.setAdapter(adapter);
                menu.setOnItemSelectedListener(HomeFragment.this);
            }

            @Override
            public void onFailure(Call<List<DjangoGet>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //Get information from Django
    public void getNeutral() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getNeutral();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getHappiness() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getHappiness();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getSadness() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getSadness();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getContempt() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getContempt();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getFear() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getFear();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getDisgust() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getDisgust();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getSurprise() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getSurprise();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Get information from Django
    public void getAnger() {
        Call<List<Chat>> call = jsonPlaceHolderApi.getAnger();
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                //not successful
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext().getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //get data
                chat = response.body();
                //Toast.makeText(getContext().getApplicationContext(), chat.get(r).getText(), Toast.LENGTH_LONG).show();
                ai.setText(chat.get(r).getText());

            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




}
