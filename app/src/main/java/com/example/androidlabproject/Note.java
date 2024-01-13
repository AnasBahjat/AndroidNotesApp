package com.example.androidlabproject;

public class Note {
    private int noteID;
    private String title;
    private String content;
    private String date;
    private String email;
    private String isFavorite;

    public Note(){

    }
    public Note(int noteID,String title,String content,String date,String email,String isFavorite){
        this.noteID=noteID;
        this.title=title;
        this.content=content;
        this.date=date;
        this.email=email;
        this.isFavorite=isFavorite;
    }

    public Note(String title,String content,String date,String email,String isFavorite){
        this.noteID=noteID;
        this.title=title;
        this.content=content;
        this.date=date;
        this.email=email;
        this.isFavorite=isFavorite;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getNoteID() {
        return noteID;
    }
}
