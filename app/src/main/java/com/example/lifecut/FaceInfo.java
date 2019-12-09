package com.example.lifecut;

import android.graphics.Bitmap;

//class to save face information to send to backend Django
public class FaceInfo {
    Bitmap image;
    String comment = "NoComment";
    String date="NoDate";
    String url ="NoUrl";
    double anger = 0.0;
    double contempt = 0.0;
    double disgust = 0.0;
    double fear = 0.0;
    double happiness = 0.0;
    double neutral = 0.0;
    double sadness = 0.0;
    double surprise = 0.0;



    FaceInfo(String date){
        this.date = date;
    }
    FaceInfo(Bitmap image, String comment){
        this.comment = comment;
        this.image = image;
    }


    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setFace(Bitmap face){
        this.image = face;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setAnger(double anger){
        this.anger = anger;
    }
    public void setContempt(double contempt){
        this.contempt = contempt;
    }
    public void setDisgust(double disgust){
        this.disgust = disgust;
    }
    public void setFear(double fear){
        this.fear = fear;
    }
    public void setHappiness(double happiness){
        this.happiness = happiness;
    }
    public void setNeutral(double neutral){ this.neutral = neutral; }
    public void setSadness(double sadness){
        this.sadness = sadness;
    }
    public void setSurprise(double surprise){
        this.surprise = surprise;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
