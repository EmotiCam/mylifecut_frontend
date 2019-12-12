package com.example.lifecut;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lifecut.Picasso.RoundTransformation;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class ShowArchiveActivity extends AppCompatActivity {
    ImageView img;
    TextView emotion;
    TextView comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showarchive);

        PostEmotion sessionId = (PostEmotion) getIntent().getSerializableExtra("IMAGE");
        Double em = (double) getIntent().getSerializableExtra("EMOTION");

        emotion = findViewById(R.id.emotion);
        comment = findViewById(R.id.comment);

        comment.setText(sessionId.getComment());
        emotion.setText(em.toString());
        img = findViewById(R.id.img);

        Picasso.get().load("https"+sessionId.
                getUrl().substring(4, sessionId.getUrl().length())).
                resize(150,150).
                transform(new RoundTransformation(20, 0)).
                into(img);

    }

}


