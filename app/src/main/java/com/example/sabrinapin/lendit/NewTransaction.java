package com.example.sabrinapin.lendit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.content.FileProvider;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewTransaction extends AppCompatActivity {


    EditText mOwner, mItem, mDate;
    Button mTakePicture, mUploadPicture, mAddToCalendar;
    TextView mReturnDate, mCompleteTransaction, mCancelTransaction;
    ImageView mObjectView;
    private String mPhotoDirectory;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private static final String TAG = NewTransaction.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Firebase mRef;
    private static final int REQUEST_READ_PHONE_STATE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        mOwner = findViewById(R.id.editOwner);
        mItem = findViewById(R.id.editItem);
        mDate = findViewById(R.id.selectDate);
        mTakePicture = findViewById(R.id.takePicture);
        mAddToCalendar = findViewById(R.id.addToCalendar);
        mReturnDate = findViewById(R.id.returnDate);
        mCancelTransaction = findViewById(R.id.cancelTransaction);
        mObjectView = findViewById(R.id.objectImage);
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://lendit-af1e0.firebaseio.com/");
        mCompleteTransaction = findViewById(R.id.completeTransaction);


        final Activity thisActivity = this;
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

                Firebase refTransaction =mRef.push();
                refTransaction.setValue("userId here");
                Firebase dateChild =refTransaction.child("date");
                dateChild.setValue(mDate.getText().toString());
                Firebase ownerChild =refTransaction.child("owner");
                ownerChild.setValue(mOwner.getText().toString());
                Firebase itemChild = refTransaction.child("item");
                itemChild.setValue(mItem.getText().toString());


//                int permissionCheck = ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_PHONE_STATE);
//
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
////                    ActivityCompat.requestPermissions(thisActivity,
////                            new String[]{Manifest.permission.READ_PHONE_STATE},
//                            REQUEST_READ_PHONE_STATE);
//                }
//


//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                mImageBitmap.recycle();
//                //TODO make transaction
//                Transaction tr = new Transaction(mOwner.getText().toString(), mItem.getText().toString(), mDate.getText().toString(), byteArray);
//                Intent receivedIntent = new Intent(NewTransaction.this, MainActivity.class);
//                receivedIntent.putExtra("newTransaction", tr);
//                setResult(Activity.RESULT_OK, receivedIntent);
//                finish();
//                //TODO idk how to handle uploading transactions and refreshing the feed
            }
        });

        mCancelTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i(TAG, "IOException");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                System.out.println("YOU");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MyFileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider",photoFile));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                mObjectView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

}
