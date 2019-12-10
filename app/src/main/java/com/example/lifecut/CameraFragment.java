package com.example.lifecut;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


//Fragment for taking picture and extracting emotion from user
public class CameraFragment extends Fragment {

    private static ImageView imageView;
    private static PostEmotion post;
    private static TextView emotion;
    private static TextView time;
    private static FaceInfo faceInfo;
    Button picture;
    Button send;
    EditText text;

    private static Context context = null;

    private FragmentCamListener listener;
    private FragmentCamListenerSave listenerSave;

    //FaceInfo faceInfo;

    private final int TAKE_PICTURE = 1;
    private ProgressDialog detectionProgressDialog;
    TextView title;

    //interface to sent bitmap data to MainActivity
    public interface FragmentCamListener {
        void onInputCamSent(Bitmap input);
    }

    public interface FragmentCamListenerSave {
        void onInputCamSentFinal(PostEmotion postEmotion);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        title = (TextView) getActivity().findViewById(R.id.main_text_title);
        title.setText("Camera");

        picture = (Button) v.findViewById(R.id.picture);
        send = (Button) v.findViewById(R.id.save);
        imageView = (ImageView) v.findViewById(R.id.imageView1);
        emotion = (TextView) v.findViewById(R.id.emotion);
        time = (TextView) v.findViewById(R.id.date);
        text = (EditText) v.findViewById(R.id.comment);
        context = getActivity();

        //Current Date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        time.setText(formattedDate);

        faceInfo = new FaceInfo(formattedDate);
        post = new PostEmotion(formattedDate);

        //Open camera application on phone
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
                    takePicture.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(takePicture, TAKE_PICTURE);
                }

            }
        });

        detectionProgressDialog = new ProgressDialog(getActivity().getApplicationContext());

        //send all data from CameraFragment to MainActivity
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceInfo.setComment(text.getText().toString());
                post.setComment(text.getText().toString());
                //send info to MainActivity to be sent to backend
                listenerSave.onInputCamSentFinal(post);
                faceInfo.setImage(null);
                post.setImage(null);

            }
        });

        return v;
    }

    //take picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView1);

            imageView.setImageBitmap(bitmap);

            //save image
            faceInfo.setFace(bitmap);
            post.setImage(bitmap);

            //listener to MainActivity
            listener.onInputCamSent(bitmap);
            //Toast.makeText(getActivity(), "Hello" + result.image, Toast.LENGTH_SHORT).show();

        }
    }


    //send context
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCamListener) {
            listener = (FragmentCamListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement fragment listener");
        }
        if (context instanceof FragmentCamListenerSave) {
            listenerSave = (FragmentCamListenerSave) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement fragment listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        listenerSave = null;
    }

    //Recieve data bitmap and image data from main activity
    public static void updateCamData(ImageFace result) {
        List<Face> faces;
        faces = Arrays.asList(result.face);
        Bitmap img = result.image;

        if (result.image != null) {
            faceInfo.setImage(result.image);
            post.setImage(result.image);
            imageView.setImageBitmap(
                    drawFaceRectanglesOnBitmap(result.image, result.face));

            emotion.setText(getEmotion(faces.get(0).faceAttributes.emotion));
        }

    }

    // Detect faces by uploading a face image.
    // Frame faces after detection.
    // drawrectangle in face
    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, Face[] faces) {
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

    //send and save emotion
    private static String getEmotion(Emotion emotion) {
        //save information
        faceInfo.setAnger(emotion.anger);
        faceInfo.setContempt(emotion.contempt);
        faceInfo.setDisgust(emotion.disgust);
        faceInfo.setFear(emotion.fear);
        faceInfo.setHappiness(emotion.happiness);
        faceInfo.setNeutral(emotion.neutral);
        faceInfo.setSadness(emotion.sadness);
        faceInfo.setSurprise(emotion.surprise);

        post.setAnger(emotion.anger);
        post.setContempt(emotion.contempt);
        post.setDisgust(emotion.disgust);
        post.setFear(emotion.fear);
        post.setHappiness(emotion.happiness);
        post.setNeutral(emotion.neutral);
        post.setSadness(emotion.sadness);
        post.setSurprise(emotion.surprise);

        String emotionType = "";
        double emotionValue = 0.0;

        if (emotion.anger > emotionValue) {
            emotionValue = emotion.anger;
            emotionType = "Anger";
        }
        if (emotion.contempt > emotionValue) {
            emotionValue = emotion.contempt;
            emotionType = "Contempt";
        }
        if (emotion.disgust > emotionValue) {
            emotionValue = emotion.disgust;
            emotionType = "Disgust";
        }
        if (emotion.fear > emotionValue) {
            emotionValue = emotion.fear;
            emotionType = "Fear";
        }
        if (emotion.happiness > emotionValue) {
            emotionValue = emotion.happiness;
            emotionType = "Happiness";
        }
        if (emotion.neutral > emotionValue) {
            emotionValue = emotion.neutral;
            emotionType = "Neutral";
        }
        if (emotion.sadness > emotionValue) {
            emotionValue = emotion.sadness;
            emotionType = "Sadness";
        }
        if (emotion.surprise > emotionValue) {
            emotionValue = emotion.surprise;
            emotionType = "Surprise";
        }

        post.setMainEmotion(emotionType.toLowerCase());
        return String.format("%s: %f", emotionType, emotionValue);
        //return String.format("%s", emotionType);
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.toString());
                }
            }
        }

        return cam;
    }


}
