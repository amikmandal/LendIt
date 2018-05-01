package com.example.sabrinapin.lendit;


public class EventObject {

    //Creates an Event Object for easy storage
    private int id;
    private String title;
    private String date;

    public EventObject(String message, String date) {
        this.id = -1;
        this.title = message;
        this.date = date;
    }
    public EventObject(int id, String message, String date) {
        this.date = date;
        this.title = message;
        this.id = id;
    }

    public EventObject()  {
        this.title = "";
        this.date = "";
        this.id = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }

    public String toString()  {
        return id + " " + title + " " + date.toString();
    }
}
