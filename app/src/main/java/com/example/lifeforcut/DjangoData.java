package com.example.lifecut;

public class DjangoData {

    String comment = "NoComment";
    double anger = 0.0;
    double contempt = 0.0;
    double disgust = 0.0;
    double fear = 0.0;
    double happiness = 0.0;
    double neutral = 0.0;
    double sadness = 0.0;
    double surprise = 0.0;
    String image_url = "url";
    String main_emotion= "main";


    public DjangoData(String comment, double anger, double contempt, double disgust, double fear, double happiness, double neutral, double sadness, double surprise, String image_url, String main_emotion) {
        this.comment = comment;
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.surprise = surprise;
        this.image_url = image_url;
        this.main_emotion = main_emotion;
    }

    public String getComment() {
        return comment;
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

    public String getImage_url() {
        return image_url;
    }

    public String getMain_emotion() {
        return main_emotion;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setMain_emotion(String main_emotion) {
        this.main_emotion = main_emotion;
    }
}
