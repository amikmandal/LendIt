package com.example.sabrinapin.lendit;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {

    String mOwner, mItem, mDate;
    byte[] mImage;

    public Transaction(String owner, String item, String date, byte[] image)  {
        mOwner = owner;
        mItem = item;
        mDate = date;
        mImage = image;
    }

    public Transaction(Parcel p)  {
        mOwner = p.readString();
        mItem = p.readString();
        mDate = p.readString();
        p.readByteArray(mImage);
        readFromParcel(p);
    }

    public String getOwner()  { return mOwner; }
    public String getItem()  { return mItem; }
    public String getDate()  { return mDate; }
    public byte[] getImage()  { return mImage; }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
        mOwner = in.readString();
        mItem = in.readString();
        mDate = in.readString();
        mImage = new byte[in.readInt()];
        in.readByteArray(mImage); //LOE - Line Of Exception
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mOwner);
        parcel.writeString(mItem);
        parcel.writeString(mDate);
        parcel.writeInt(mImage.length);
        parcel.writeByteArray(mImage);
    }

    public static final Parcelable.Creator<Transaction> CREATOR
            = new Parcelable.Creator<Transaction>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public String toString()  {
        return mOwner + " " + mItem + " " + mDate + " " + mImage;
    }
}
