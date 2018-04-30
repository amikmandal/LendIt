package com.example.sabrinapin.lendit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sabrinapin on 4/7/18.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    final Context mContext;
    List<TransFirInfo> list;
//    String[] mPeople;
//    String [] mDates;
//    String[] mObjects;
//    Uri[] mUris;
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row = mInflater.inflate(R.layout.transaction_holder, parent, false);
//
//        final ViewHolder transactionHolder = new ViewHolder(row);
//
//        transactionHolder.mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO info button activity
//            }
//        });
//
//        return transactionHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int pos) {
//        Drawable infoArtwork = mContext.getDrawable(android.R.drawable.ic_dialog_info);
//
//        holder.mImageView.setImageURI(mUris[pos]);
//        holder.mButton.setImageDrawable(infoArtwork);
//        holder.mDate.setText(mDates[pos]);
//        holder.mPerson.setText(mPeople[pos]);
//        holder.mObject.setText(mObjects[pos]);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return (mPeople == null) ? 0 : mPeople.length;
//    }
//
//
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mNamesLinearLayout;
        LinearLayout mInfoLinearLayout;
        ImageView mImageView;
        TextView mDate;
        TextView mBorrower;
        TextView mOwner;
        TextView mObject;


        public ViewHolder(View itemView) {

            //use super so you don't have to rewrite old recycler view
            //aka calling parent constructor
            super(itemView);

            //all taken from album_holder.xml --> this info modifies/updates recycler view
            this.mNamesLinearLayout = itemView.findViewById(R.id.transaction_names_linear_layout);
            this.mInfoLinearLayout = itemView.findViewById(R.id.transaction_info_linear_layout);
            this.mImageView = itemView.findViewById(R.id.transaction_image_view);
            this.mOwner = itemView.findViewById(R.id.mOwner);
            this.mBorrower = itemView.findViewById(R.id.mBorrower);
            this.mDate = itemView.findViewById(R.id.mDate);
            this.mObject = itemView.findViewById(R.id.mObject);


        }

    }
//
//    public TransactionAdapter(Context c, TransFirInfo myInfo) {
//        mContext = c;
//        mPeople = people;
//        mObjects = objects;
//        mDates = dates;
//        mUris = images;
//    }



    public TransactionAdapter(Context context, List<TransFirInfo> List){
        this.mContext = context;
        this.list = List;
    }

    //Ctrl + O


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = mInflater.inflate(R.layout.transaction_holder, parent, false);

        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TransFirInfo data = list.get(position);

        holder.mDate.setText(data.getdate());
        holder.mBorrower.setText(data.getborrower());
        holder.mOwner.setText(data.getowner());
        holder.mObject.setText(data.getitem());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(getApplicationContext()).load(data.getImageUrl()).apply(options).into(holder.mImageView);

//                setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/lendit-af1e0.appspot.com/o/JPEG_20180429_024056_2692024546603169112.jpg?alt=media&token=7332a850-2039-4a87-8a1f-1605aa324f37"));
        //Uri.parse(data.getImageUrl())
    }



    @Override
    public int getItemCount() {
        return list.size();
    }




}