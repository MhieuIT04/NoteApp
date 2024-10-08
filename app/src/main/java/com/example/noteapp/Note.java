package com.example.noteapp;

import com.google.firebase.Timestamp;

public class Note {
    String title;
    String content;
    Timestamp timestamp;
    String tvdate;
    String tvtime;

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTVdate() {
        return tvdate;
    }

    public void setTVdate(String tVdate) {
        this.tvdate = tVdate;
    }

    public String getTVtime() {
        return tvtime;
    }

    public void setTVtime(String tVtime) {
        this.tvtime = tVtime;
    }
}
