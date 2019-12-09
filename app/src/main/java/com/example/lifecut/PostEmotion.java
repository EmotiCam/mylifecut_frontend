package com.example.lifecut;

import android.graphics.Bitmap;

import java.io.Serializable;

public class PostEmotion implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    String comment = "NoComment";
    String date = "NoDate";
    String url = "NoUrl";
    double anger = 0.0;
    double contempt = 0.0;
    double disgust = 0.0;
    double fear = 0.0;
    double happiness = 0.0;
    double neutral = 0.0;
    double sadness = 0.0;
    double surprise = 0.0;
    Bitmap image;
    String mainEmotion = "main";
    double emt;

    public PostEmotion(String date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setMainEmotion(String mainEmotion) {
        this.mainEmotion = mainEmotion;
    }

    public String getMainEmotion() {
        return mainEmotion;
    }

    public PostEmotion() {

    }

    public PostEmotion(String comment, String date, String url, double anger, double contempt, double disgust, double fear, double happiness, double neutral, double sadness, double surprise, String mainEmotion) {
        this.comment = comment;
        this.date = date;
        this.url = url;
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.surprise = surprise;
        this.mainEmotion = mainEmotion;
    }

    public PostEmotion(double emt) {
        this.emt = emt;
    }

    public double getEmt() {
        return emt;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public double getAnger() {
        return anger;
    }

    public double getContempt() {
        return contempt;
    }

    public double getDisgust() {
        return disgust;
    }

    public double getFear() {
        return fear;
    }

    public double getHappiness() {
        return happiness;
    }

    public double getNeutral() {
        return neutral;
    }

    public double getSadness() {
        return sadness;
    }

    public double getSurprise() {
        return surprise;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAnger(double anger) {
        this.anger = anger;
    }

    public void setContempt(double contempt) {
        this.contempt = contempt;
    }

    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    public void setFear(double fear) {
        this.fear = fear;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }
}
