package com.example.projet1;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.util.Date;

public class Score implements Comparable {

    String name;
    String score;
    Date date;
    String hunter;

    public Score(String name, String score, Date date, String hunter) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.hunter = hunter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHunter() {
        return hunter;
    }

    public void setHunter(String hunter) {
        this.hunter = hunter;
    }

    @NonNull
    @Override
    public String toString() {
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT);

        return this.name + " - " + this.score + " - " + shortDateFormat.format(this.date) + " - " + this.hunter;
    }

    @Override
    public int compareTo(Object o) {
        Score score = (Score)o;
        int compareage = Integer.parseInt(score.getScore());
        return compareage-Integer.parseInt(this.score);
    }
}
