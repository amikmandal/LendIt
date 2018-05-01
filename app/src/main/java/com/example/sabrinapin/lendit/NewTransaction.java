package com.example.sabrinapin.lendit;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.sabrinapin.lendit.LoginActivity.USER_FIRST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.USER_LAST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.userID;


public class NewTransaction extends AppCompatActivity {

    //all views
    EditText mOwner, mItem, mBorrower;
    Button mTakePicture, mUploadPicture, mAddToCalendar;
    TextView mReturnDate, mCompleteTransaction, mCancelTransaction, mDate;
    ImageView mObjectView;
    ScrollView mScrollView;
    LinearLayout mLinearLayout;

    private String mPhotoDirectory;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private static final String TAG = NewTransaction.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int year, month, date;
    static final int DIALOG_ID = 0;

    //    private Firebase mRef;
    private FirebaseDatabase mRef;
    private SharedPreferences sharedPref;
    private static final int REQUEST_READ_PHONE_STATE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public StorageReference storageRef;
    public File mphotoFile = null;
    private Uri mImageUri = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        //create all views
        mOwner = findViewById(R.id.editOwner);
        mItem = findViewById(R.id.editItem);
        mBorrower = findViewById(R.id.BorrowerName);
        mDate = findViewById(R.id.selectDate);
        mTakePicture = findViewById(R.id.takePicture);
        mAddToCalendar = findViewById(R.id.addToCalendar);
        mReturnDate = findViewById(R.id.returnDate);
        mCancelTransaction = findViewById(R.id.cancelTransaction);
        mObjectView = findViewById(R.id.objectImage);
        mCompleteTransaction = findViewById(R.id.completeTransaction);
        mScrollView = findViewById(R.id.scrollView);
        mLinearLayout = findViewById(R.id.linearlayoutscrollview);

        //initializes sharedPref so that app will recognize user in NewTransaction
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        //takes in user's information previously stored in shared preferences
        USER_FIRST_NAME = sharedPref.getString("firstName", "dumb");
        USER_LAST_NAME = sharedPref.getString("lastName", "dumber");
        userID = sharedPref.getString("user", "dumbest");
        //adding inTransaction to shared preferences

        storageRef = FirebaseStorage.getInstance().getReference();

        //tells app that user is in NewTransaction - used when user exits app
        sharedPref.edit().putString("inTransaction","uselessString").commit();

        //this was used to test
        if(sharedPref.contains("inTransaction")){
            //Toast.makeText(this, "inTransaction is now in sharedPref", Toast.LENGTH_LONG).show();
        }


        mAddToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });
        final Activity thisActivity = this;

        //takes picture once user clicks on button
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(thisActivity);
                dispatchTakePictureIntent();
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });




        mCompleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            //checks if fields are all filled out
                if (mBorrower.getText().toString().matches("")||mItem.getText().toString().matches("")||mOwner.getText().toString().matches("")||mDate.getText().toString().matches("")) {
                    Toast.makeText(thisActivity, "one or more of your fields is empty", Toast.LENGTH_SHORT).show();

                }

                else{

                    // upload the event
                    Toast.makeText(thisActivity, "Processing Transaction", Toast.LENGTH_LONG).show();

                    UploadFilesTask up = new UploadFilesTask();
                    up.execute();

                    EventObject ev = new EventObject(mBorrower.getText().toString() + " returns " + mItem.getText().toString() + " to " + mOwner.getText().toString(), mDate.getText().toString());
                    SQLiteJDBC mDbHelper = new SQLiteJDBC(getApplicationContext());


                    mDbHelper.addEvent(ev);
                    mDbHelper.closeDB();

                    sharedPref.edit().remove("inTransaction").commit();
                    startActivity(new Intent(NewTransaction.this, MainActivity.class));
                    finish();

                }

            }
        });

        mCancelTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //once the transaction is cancelled, the app will no longer return to NewTransaction
                sharedPref.edit().remove("inTransaction").commit();
                finish();
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id==DIALOG_ID)
        {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth=Calendar.getInstance().get(Calendar.MONTH);
            int currentDate=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);;
            //Toast.makeText(this, formattedDate.substring(5,6), Toast.LENGTH_SHORT).show();
            return new DatePickerDialog(this, dPickerListener, currentYear , currentMonth, currentDate);
        }
        return null;
    }

    // uses a date picker
    private DatePickerDialog.OnDateSetListener dPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
       // sets a datepicker item

        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1;
            date = i2;
            mDate.setText(month+"/"+date+"/"+year);
        }
    };

    private void dispatchTakePictureIntent() {
        System.out.println("HEY");
        Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoPickerIntent.resolveActivity(getPackageManager()) != null) {
            System.out.println("YOU");
            // Create the File where the photo should go

            try {
                mphotoFile = createImageFile();
                Log.d("filepath", mphotoFile.toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "IOException");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (mphotoFile != null) {
                System.out.println("YOU");
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider",mphotoFile));
                System.out.print("NEED");
                startActivityForResult(photoPickerIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        System.out.println("DONT");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        System.out.println("LIKE");
        String imageFileName = "JPEG_" + timeStamp + "_";
        System.out.println("YOUR");
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        System.out.println("GIRLFRIEND");
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",  // suffix
                storageDir     // directory
        );
        System.out.println("I");
        // Save a file: path for use with ACTION_VIEW intents
        mPhotoDirectory = image.getAbsolutePath();
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        System.out.println("THINK");
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("trial", "here");
            mImageUri = Uri.fromFile(mphotoFile);
            Log.d("FirebaseURL", mImageUri.toString());
            mObjectView.setImageURI(mImageUri);


//            try {
//                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
//                mObjectView.setImageBitmap(mImageBitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    //when back is pressed - will go back to main
    @Override
    public void onBackPressed() {
        //when back is pressed, app will no longer recognize NewTransaction as current activity

        //Toast used to test
        //Toast.makeText(this, "you should be going back to main", Toast.LENGTH_LONG).show();
        sharedPref.edit().remove("inTransaction").commit();
        Intent intent = new Intent(NewTransaction.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void addEvent()  {
        java.util.Calendar calendarEvent = java.util.Calendar.getInstance();
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");

        String myDate = mDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        i.putExtra("beginTime", millis);
        i.putExtra("allDay", true);
        i.putExtra("rule", "FREQ=YEARLY");
        i.putExtra("endTime", millis + 60 * 60 * 1000);
        i.putExtra("title", mBorrower.getText().toString() + " returns " + mItem.getText().toString() + " to " + mOwner.getText().toString());
        startActivity(i);
    }


    private class UploadFilesTask extends AsyncTask<EventObject, Void, Void> {
        protected Void doInBackground(EventObject... eo) {
            mRef = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");



            //check if the borrower exists

            DatabaseReference myUsernames = mRef.getReference("checkNames");

            myUsernames.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {


                    if ((snapshot.hasChild(mBorrower.getText().toString())) && (snapshot.hasChild(mOwner.getText().toString()))) {
                        FirebaseDatabase rRefs = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
                        DatabaseReference tRef = rRefs.getReference("checkNames");
                        DatabaseReference pushRef = rRefs.getReference("users");


                        if (mImageUri != null) {

                            StorageReference filepath = storageRef.child(mImageUri.getLastPathSegment());
                            Log.d("firepath", filepath.toString());

                            //Toast.makeText(thisActivity, "hey its working", Toast.LENGTH_SHORT).show();

                            UploadTask uploadTask = filepath.putFile(mImageUri);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    FirebaseDatabase trial = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
                                    DatabaseReference tRef = trial.getReference("users");



                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    TransFirInfo tInfo = new TransFirInfo();
                                    tInfo.setborrower(mBorrower.getText().toString());
                                    tInfo.setdate(mDate.getText().toString());
                                    tInfo.setitem(mItem.getText().toString());
                                    tInfo.setowner(mOwner.getText().toString());
                                    tInfo.setImageUrl(downloadUrl.toString());

                                    Log.d("latte", downloadUrl.toString());
                                    //tRef.child("image").setValue(downloadUrl.toString());

                                    String borrowerUN = mBorrower.getText().toString();
                                    String ownerUN = mOwner.getText().toString();


                                    tRef.child(ownerUN).push().setValue(tInfo);
                                    tRef.child(borrowerUN).push().setValue(tInfo);


                                }
                            });


                        } else {
                            FirebaseDatabase trial = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
                            DatabaseReference pRef = trial.getReference("users");
                            TransFirInfo pInfo = new TransFirInfo();
                            pInfo.setborrower(mBorrower.getText().toString());
                            pInfo.setdate(mDate.getText().toString());
                            pInfo.setitem(mItem.getText().toString());
                            pInfo.setowner(mOwner.getText().toString());
                            pInfo.setImageUrl("https://firebasestorage.googleapis.com/v0/b/lendit-af1e0.appspot.com/o/missing-item-clipart-1.jpg?alt=media&token=c441afb1-824f-4fd6-b871-4e78fb459fdf");
                            String borrowerUN = mBorrower.getText().toString();
                            String ownerUN = mOwner.getText().toString();


                            pRef.child(ownerUN).push().setValue(pInfo);
                            pRef.child(borrowerUN).push().setValue(pInfo);

//                            sharedPref.edit().remove("inTransaction").commit();
//                            startActivity(new Intent(NewTransaction.this, MainActivity.class));
//                            finish();
                        }



                    } else {
                        if ((!snapshot.hasChild(mBorrower.getText().toString())) && (!snapshot.hasChild(mOwner.getText().toString()))) {
                            Toast.makeText(NewTransaction.this, "Both the user and borrower are not registered", Toast.LENGTH_LONG).show();
                        } else if (!(snapshot.hasChild(mBorrower.getText().toString()))) {
                            Toast.makeText(NewTransaction.this, "This borrower is not registered", Toast.LENGTH_LONG).show();
                        } else if (!(snapshot.hasChild(mOwner.getText().toString()))) {
                            Toast.makeText(NewTransaction.this, "This owner is not registered", Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
//                    sharedPref.edit().remove("inTransaction").commit();
//                    finish();
                }
            });




            // add the owner username as a transaction

            return null;

        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }


    }


}
