package com.example.lifecut;

import android.graphics.Bitmap;

import com.microsoft.projectoxford.face.contract.Face;


//class to save face attributes and image to save analyzed emotions
public class ImageFace {
    Face[] face;
    Bitmap image;

    ImageFace(Bitmap image, Face[] face){
        this.image = image;
        this.face = face;
    }

    public void setFace(Face[] face) {
        this.face = face;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }




}
