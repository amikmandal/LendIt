package com.example.sabrinapin.lendit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by NiallSchroder on 1/27/18.
 */

public class CalendarDisplayAdapter extends RecyclerView.Adapter<CalendarDisplayAdapter.ViewHolder> {

    final Context mContext;
    ArrayList<String> titles;

    public CalendarDisplayAdapter(final Context context, ArrayList<String> titles)  {
        mContext = context;
        this.titles = titles;
    }

    @Override
    public int getItemCount()  {
        return titles.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = mInflater.inflate(R.layout.calendar_display_holder, parent, false);
        final ViewHolder titleHolder = new ViewHolder(row);
        return titleHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)  {
        holder.mTitle.setText("\u2022" + titles.get(position));

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public ViewHolder(View itemView) {

            super(itemView);
            this.mTitle = itemView.findViewById(R.id.calendar_display_holder_text);
        }


    }

}
