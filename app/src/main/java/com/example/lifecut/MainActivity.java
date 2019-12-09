package com.example.lifecut;

// <snippet_imports>

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// </snippet_imports>
// <snippet_face_imports>
// </snippet_face_imports>

public class MainActivity extends AppCompatActivity implements
        CameraFragment.FragmentCamListener, CameraFragment.FragmentCamListenerSave,
        AboutFragment.FragmentSettingListener{

    private TextView txt;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    List<DjangoGet> posts;

    private String url;

    private ImageFace output;

    private DjangoData djangoData;

    //Database
    //DatabaseHelper mDatabaseHelper;

    private TextView title;

    private Toolbar toolbar;
    // face api endpoint
    private final String apiEndpoint = "https://life4cut.cognitiveservices.azure.com/face/v1.0";
    // face api subscription key
    private final String subscriptionKey = "def0ff196a4141a1a6c329891921e4f1";
    private final FaceServiceClient faceServiceClient = new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    String tok;
    //waiting dialogue box
    private ProgressDialog detectionProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try{
            tok = getIntent().getStringExtra("EXTRA_SESSION_ID");
        }
        catch(Exception e){
            Toast.makeText(getApplication(), "nothing", Toast.LENGTH_LONG).show();
        }


        //toolbars
        title = findViewById(R.id.main_text_title);
        toolbar = findViewById(R.id.main_toolbar);
        title.setText(R.string.home_activity_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        txt = findViewById(R.id.textView123);

        //HTTP Calls GET,POST
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://fzfv1bjqai.execute-api.ap-northeast-2.amazonaws.com/dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //Set botton navigator
        BottomNavigationView botnav = findViewById(R.id.main_navigation);
        botnav.setItemIconTintList(null);
        botnav.setOnNavigationItemSelectedListener(nalistener);

        //display first fragment as about
        getSupportFragmentManager().beginTransaction().replace(R.id.category_viewgroup_container,
                new HomeFragment()).commit();
        //dialogue for displaying error message in detectAndFrame method
        detectionProgressDialog = new ProgressDialog(this);

        //Database
        //mDatabaseHelper = new DatabaseHelper(this);

    }

    //send token to other fragments
    public String getMyData() {
        return tok;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mnu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_add) {
            // reveal from top right corner:
            Intent intent = new Intent(this, ShowInformationActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInputSetting(String next) {
        Intent intent = new Intent(this, ActivityChangeNick.class);
        intent.putExtra("TOKEN", tok);
        startActivity(intent);
    }

    //Datas Received from Fragments using interface
    //image data from CameraFragment
    @Override
    public void onInputCamSent(Bitmap input) {
        ImageFace result;
        result = detectAndFrame(input);
    }


    //All the face information data from Camera Fragment
    @Override
    public void onInputCamSentFinal(PostEmotion s) {
        if( s.surprise==0.0 && s.sadness==0.0 && s.neutral==0.0
                && s.happiness==0.0 && s.fear==0.0 && s.disgust==0.0 && s.contempt==0.0 && s.anger==0.0) {

            Toast.makeText(getApplication(), "No Image Taken, Please take Image", Toast.LENGTH_LONG).show();
        }
        else if(s.image==null){
            Toast.makeText(getApplication(), "Image already saved", Toast.LENGTH_LONG).show();
        }
        else{
            //PostInfo(s);
            Toast.makeText(getApplication(), "Image and Comment Saved" , Toast.LENGTH_LONG).show();

            Bitmap takeimg = s.getImage();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            takeimg.compress(Bitmap.CompressFormat.JPEG, 100, stream); // Used for compression rate of the Image : 100 means no compression
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), takeimg, "xyz", null);

            //send image to cloudinary and receive url
            String requestId = MediaManager.get()
                    .upload(Uri.parse(path))
                    .unsigned("vkjup7hs").callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {

                        }
                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                        }
                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            if (resultData != null && resultData.size() > 0) {
                                if (resultData.containsKey("url")) {
                                    //String imageURL = (String) resultData.get("url");
                                    url = (String) resultData.get("url");
                                    s.setUrl(url);
                                    //Toast.makeText(getApplication(), s.getUrl(), Toast.LENGTH_LONG).show();
                                    //AddData(s);
                                    djangoData = new DjangoData(s.comment, s.anger, s.contempt, s.disgust, s.fear, s.happiness, s.neutral, s.sadness, s.surprise, s.url, s.mainEmotion);
                                    //Toast.makeText(getApplication(), tok, Toast.LENGTH_LONG).show();
                                    PostInfo(djangoData);

                                }
                            }
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {

                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }
                    }).dispatch();
            //ArchiveFragment.upDateData(s);

        }


    }

    //Get information from Django
    public void GetInfo(){

        String tk = "TOKEN " + tok;
        Call<List<DjangoGet>> call = jsonPlaceHolderApi.getPosts(tk);
        call.enqueue(new Callback<List<DjangoGet>>() {
            @Override
            public void onResponse(Call<List<DjangoGet>> call, Response<List<DjangoGet>> response) {
                //not successful
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                posts = response.body();
                for (DjangoGet post: posts){
                    String content = "";
                    content += "ID " + post.getComment() + "\n";
                    //Toast.makeText(getApplication(), post.created_at , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DjangoGet>> call, Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //Post infromation in Django
    public void PostInfo(DjangoData djangoData){

        DjangoData post = new DjangoData(djangoData.comment, djangoData.anger, djangoData.contempt, djangoData.disgust, djangoData.fear, djangoData.happiness, djangoData.neutral, djangoData.sadness, djangoData.surprise, djangoData.image_url, djangoData.main_emotion);

        String tokens = "TOKEN "+tok;
        Call<DjangoData> call =jsonPlaceHolderApi.createPost(post, tokens);
        call.enqueue(new Callback<DjangoData>() {
            @Override
            public void onResponse(Call<DjangoData> call, Response<DjangoData> response) {
                //not successful
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), response.code(), Toast.LENGTH_LONG).show();
                    //txt.setText("Code: " + response.code());
                    return;
                }
                DjangoData postresponse = response.body();
            }

            @Override
            public void onFailure(Call<DjangoData> call, Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    // Detect faces by the given bitmap image
    public ImageFace detectAndFrame(final Bitmap imageBitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";
                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    new FaceServiceClient.FaceAttributeType[] {
                                            FaceServiceClient.FaceAttributeType.Age,
                                            FaceServiceClient.FaceAttributeType.Gender,
                                            FaceServiceClient.FaceAttributeType.Smile,
                                            FaceServiceClient.FaceAttributeType.Glasses,
                                            FaceServiceClient.FaceAttributeType.FacialHair,
                                            FaceServiceClient.FaceAttributeType.Emotion,
                                            FaceServiceClient.FaceAttributeType.HeadPose,
                                            FaceServiceClient.FaceAttributeType.Accessories,
                                            FaceServiceClient.FaceAttributeType.Blur,
                                            FaceServiceClient.FaceAttributeType.Exposure,
                                            FaceServiceClient.FaceAttributeType.Hair,
                                            FaceServiceClient.FaceAttributeType.Makeup,
                                            FaceServiceClient.FaceAttributeType.Noise,
                                            FaceServiceClient.FaceAttributeType.Occlusion
                                    }
                            );
                            if (result == null){
                                publishProgress(
                                        "Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(String.format(
                                    "Detection Finished. %d face(s) detected",
                                    result.length));
                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        //TODO: show progress dialog
                        detectionProgressDialog.show();
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //TODO: update progress
                        detectionProgressDialog.setMessage(progress[0]);
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames
                        detectionProgressDialog.dismiss();

                        if(!exceptionMessage.equals("")){
                            showError(exceptionMessage);
                        }
                        if (result == null) return;

                        //send face information back to Camera Fragment
                        output = new ImageFace(imageBitmap, result);
                        CameraFragment.updateCamData(output);


                    }
                };
        detectTask.execute(inputStream);
        return output;
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }
    // </snippet_detection_methods>

    // <snippet_drawrectangles>
    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }

    //Add to to database
    /*public void AddData(PostEmotion data) {
        boolean insertData = mDatabaseHelper.addData(data.comment, data.date, data.url, data.anger, data.contempt, data.disgust, data.fear, data.happiness, data.neutral, data.sadness, data.surprise, data.mainEmotion);

        if (insertData) {
            Toast.makeText(getApplication(), "Data Inserted" , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplication(), "Error" , Toast.LENGTH_LONG).show();
        }
    }*/

    //Switch around navigation on the bottom of the screen
    private BottomNavigationView.OnNavigationItemSelectedListener nalistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_about:
                            selectedFragment = new AboutFragment();
                            break;
                        case R.id.navigation_archive:
                            selectedFragment = new ArchiveFragment();
                            break;
                        case R.id.navigation_camera:
                            selectedFragment = new CameraFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.category_viewgroup_container,
                            selectedFragment).commit();

                    return true;
                }
            };





}
