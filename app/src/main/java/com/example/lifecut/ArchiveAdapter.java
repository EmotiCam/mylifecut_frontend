package com.example.lifecut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArchiveAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] word;
    private int[] numberimage;

    public ArchiveAdapter(Context c, String[] word, int[] numberimage){
        context = c;
        this.word = word;
        this.numberimage = numberimage;
    }

    @Override
    public int getCount() {
        return word.length;
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

        imageView.setImageResource(numberimage[i]);
        textView.setText(word[i]);

        return view;
    }
}
