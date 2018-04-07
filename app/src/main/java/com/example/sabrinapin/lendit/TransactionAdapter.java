package com.example.sabrinapin.lendit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sabrinapin on 4/7/18.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    final Context mContext;
    String[] mPeople;
    String[] mObjects;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = mInflater.inflate(R.layout.transaction_holder, parent, false);

        final ViewHolder transactionHolder = new ViewHolder(row);

        transactionHolder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this stuff comes from album holder . xml
//                openAlbum(mAlbums[albumHolder.getAdapterPosition()], mArtists[albumHolder.getAdapterPosition()]);
            }
        });

        return transactionHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLinearLayout;
        ImageView mImageView;
        TextView mDate;
        TextView mPerson;
        ImageButton mButton;


        public ViewHolder(View itemView) {

            //use super so you don't have to rewrite old recycler view
            //aka calling parent constructor
            super(itemView);

            //all taken from album_holder.xml --> this info modifies/updates recycler view
            this.mLinearLayout = itemView.findViewById(R.id.transaction_linear_layout);
            this.mImageView = itemView.findViewById(R.id.transaction_image_view);
            this.mPerson = itemView.findViewById(R.id.mPerson);
            this.mDate = itemView.findViewById(R.id.mDate);
            this.mButton = itemView.findViewById(R.id.mButton);


        }

    }

    public TransactionAdapter(Context c, String[] people, String[] objects) {
        mContext = c;
        mPeople = people;
        mObjects = objects;

    }

}