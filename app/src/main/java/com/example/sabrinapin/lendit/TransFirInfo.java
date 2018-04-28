package com.example.sabrinapin.lendit;

/**
 * Created by sramprasad on 4/26/2018.
 */

public class TransFirInfo {
    private String borrower;
    private String date;
    private String item;
    private String owner;


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
        return borrower + " " + date + " " + item + " " +  owner;
    }
}
