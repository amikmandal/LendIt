package com.example.sabrinapin.lendit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends AppCompatActivity {
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    private ScrollView mScrollView;
    private Map<String, ArrayList<String>> dates;
    private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private CalendarDisplayAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SharedPreferences sharedPref;
    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        if (caldroidFragment != null) {
            ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));
            for(String s : dates.keySet())  {

                Date d= new Date();

                try {
                     d= formatter.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                caldroidFragment.setBackgroundDrawableForDate(red, d);
                caldroidFragment.setTextColorForDate(R.color.caldroid_white, d);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caldroid_sample);

        mRecyclerView = findViewById(R.id.mRecyclerView);

        SQLiteJDBC sql = new SQLiteJDBC(this);
        List<EventObject> eventObjects = sql.getAllEvents();
        dates = new HashMap<String, ArrayList<String>>();

        for(EventObject eo : eventObjects)  {
            if(dates.get(eo.getDate()) == null)  {
                dates.put(eo.getDate(), new ArrayList<String>());
            }
            dates.get(eo.getDate()).add(eo.getTitle());
            dates.put(eo.getDate(), dates.get(eo.getDate()));
        }


        // Setup caldroid fragment
        caldroidFragment = new CaldroidFragment();

        //takes in common shared preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //used later to determine that the user is in the calendar
        sharedPref.edit().putString("inCalendar","uselessString").commit();


        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }

        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                if(dates.keySet().contains(formatter.format(date))) {
                    mAdapter = new CalendarDisplayAdapter(getApplicationContext(), dates.get(formatter.format(date)));
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
                else {
                    mRecyclerView.setLayoutManager(null);
                }

            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    @Override
    public void onBackPressed() {
        //when user presses back arrow, the app will no longer recognize Calendar as the current activity
        sharedPref.edit().remove("inCalendar").commit();
        Intent intent = new Intent(CaldroidSampleActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}