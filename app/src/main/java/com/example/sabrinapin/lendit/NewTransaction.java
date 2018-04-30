package com.example.sabrinapin.lendit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.services.calendar.Calendar;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.example.sabrinapin.lendit.LoginActivity.USER_FIRST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.USER_LAST_NAME;
import static com.example.sabrinapin.lendit.LoginActivity.userID;


public class NewTransaction extends AppCompatActivity {

    //all views
    EditText mOwner, mItem, mDate, mBorrower;
    Button mTakePicture, mUploadPicture, mAddToCalendar;
    TextView mReturnDate, mCompleteTransaction, mCancelTransaction;
    ImageView mObjectView;


    private String mPhotoDirectory;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private static final String TAG = NewTransaction.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //Firebase variables
    private FirebaseDatabase mRef;
    //common sharedPref
    private SharedPreferences sharedPref;
    private static final int REQUEST_READ_PHONE_STATE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public StorageReference storageRef;
    public File mphotoFile = null;
    private Uri mImageUri = null;
    Calendar service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        //creates all views
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

        //initializes sharedPref so that app will recognize user is in NewTransaction
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        //takes user's information previously stored in shared preferences
        USER_FIRST_NAME = sharedPref.getString("firstName", "dumb");
        USER_LAST_NAME = sharedPref.getString("lastName", "dumber");
        userID = sharedPref.getString("user", "dumbest");

        storageRef = FirebaseStorage.getInstance().getReference();

        //tells app that user is in NewTransaciton - used when user exits app
        sharedPref.edit().putString("inTransaction","uselessString").commit();

        final Activity thisActivity = this;

        //takes picture once user clicks on button
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(thisActivity);
                dispatchTakePictureIntent();

            }
        });

        mCompleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mRef = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");

                Log.d("wonka", mImageUri.toString());

                //Checks if the borrower already exists
                DatabaseReference myUsernames = mRef.getReference("checkNames");

                myUsernames.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {


                        if ((snapshot.hasChild(mBorrower.getText().toString()))&&(snapshot.hasChild(mOwner.getText().toString()))) {
                            FirebaseDatabase rRefs = FirebaseDatabase.getInstance("https://lendit-af1e0.firebaseio.com/");
                            DatabaseReference tRef = rRefs.getReference("checkNames");



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

                                        Toast.makeText(thisActivity, "dafuq", Toast.LENGTH_SHORT).show();
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

                                        sharedPref.edit().remove("inTransaction").commit();
                                        startActivity(new Intent(NewTransaction.this, MainActivity.class));
                                        finish();
                                    }
                                });


                            }

                        }
                        else{
                            if ((!snapshot.hasChild(mBorrower.getText().toString()))&&(!snapshot.hasChild(mOwner.getText().toString()))){
                                Toast.makeText(NewTransaction.this, "Both the user and borrower are not registered", Toast.LENGTH_LONG).show();
                            }

                            else if(!(snapshot.hasChild(mBorrower.getText().toString()))) {
                                Toast.makeText(NewTransaction.this, "This borrower is not registered", Toast.LENGTH_LONG).show();
                            }

                            else if(!(snapshot.hasChild(mOwner.getText().toString()))) {
                                Toast.makeText(NewTransaction.this, "This owner is not registered", Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        sharedPref.edit().remove("inTransaction").commit();
                        finish();
                    }
                });

                //Creates object storing user's info that will later be used in calendar
                EventObject ev = new EventObject(mBorrower.getText().toString() + " returns " + mItem.getText().toString() + " to " + mOwner.getText().toString(), mDate.getText().toString());
                SQLiteJDBC mDbHelper = new SQLiteJDBC(thisActivity);

                mDbHelper.addEvent(ev);
                mDbHelper.closeDB();

                //Once the transaction is completed, the app will no longer return to NewTransaction when opened
                sharedPref.edit().remove("inTransaction").commit();

            }
        });

        mCancelTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Once the transaction is cancelled, the app will no longer return to NewTransaction when opened
                sharedPref.edit().remove("inTransaction").commit();
                finish();
            }
        });


    }



    private void dispatchTakePictureIntent() {
        System.out.println("HEY");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider",mphotoFile));
                System.out.print("NEED");
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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

    //activity should interact w firebase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("cokeacola", "here");
            mImageUri = Uri.fromFile(mphotoFile);
            Log.d("helloFirebase", mImageUri.toString());
            mObjectView.setImageURI(mImageUri);

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
        //when user presses back arrow, the app will no longer recognize NewTransaction as the current activity
        sharedPref.edit().remove("inTransaction").commit();
        Intent intent = new Intent(NewTransaction.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
