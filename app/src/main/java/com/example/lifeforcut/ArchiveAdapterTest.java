package com.example.lifecut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lifecut.Picasso.RoundTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArchiveAdapterTest extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PostEmotion> word;
    private int[] numberimage;

    public ArchiveAdapterTest(Context c, ArrayList<PostEmotion> word, int[] numberimage){
        context = c;
        this.word = word;
        this.numberimage = numberimage;
    }

    @Override
    public int getCount() {
        return word.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //print out information of the ith data
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null){
            view = inflater.inflate(R.layout.row_item, null);
        }

        ImageView imageView = view.findViewById(R.id.image_view);
        TextView textView = view.findViewById(R.id.text_view);

        Picasso.get().load("https"+word.get(i).
                getUrl().substring(4, word.get(i).getUrl().length())).
                resize(150,150).
                transform(new RoundTransformation(20, 0)).
                into(imageView);

        //imageView.setImageResource(numberimage[i]);
        textView.setText(word.get(i).getDate());

        return view;
    }
}

