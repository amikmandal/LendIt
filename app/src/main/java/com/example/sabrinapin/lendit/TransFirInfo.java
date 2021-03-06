package com.example.sabrinapin.lendit;

/**
 * Created by sramprasad on 4/26/2018.
 */

public class TransFirInfo {

    // Sets an object for the Transacttion Firebase Informaiton
    // Allows each of the fields to be retrieved as an object from firebase
    private String borrower;
    private String date;
    private String item;
    private String owner;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getborrower() {
        return borrower;
    }

    public void setborrower(String borrower) {
        this.borrower = borrower;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getitem() {
        return item;
    }

    public void setitem(String item) {
        this.item = item;
    }

    public String getowner() {
        return owner;
    }

    public void setowner(String owner) {
        this.owner = owner;
    }

    public String toString(){
        return borrower + " " + date + " " + item + " " +  owner + " " + imageUrl;
    }
}
