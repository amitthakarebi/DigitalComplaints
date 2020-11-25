package com.amitthakare.digitalcomplaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewComplaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView toolbarBackBtn;
    private Spinner spinner;
    private String[] departmentList = {"Muncipal Council","Ministry of Road Transport & Highways",
            "Department of Water Resources","Ministry Of Power"};

    private Button submitComplaintBtn;
    private EditText fullName, mobileNo, address, description,city;
    private String spinnerOption;
    private TextView uploadTextView;
    private ProgressDialog progressDialogImg;
    private String imageUploadUri;
    private ImageView uploadImgBtn;

    //database
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;

    private ProgressDialog progressDialogNewComplaint;

    private String currentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);
        initialize();
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null)
                        {
                            Toast.makeText(NewComplaint.this, "No null", Toast.LENGTH_SHORT).show();
                            fullName.setText(value.getString("Full_Name"));
                            mobileNo.setText(value.getString("Mobile_No"));

                            //fullName.setEnabled(false);
                            //mobileNo.setEnabled(false);

                            if (Variables.LOCALITY != null)
                            {
                                city.setText(Variables.LOCALITY);
                                city.setEnabled(false);
                            }

                            if (Variables.ADDRESSLINE != null)
                            {
                                address.setText(Variables.ADDRESSLINE);
                            }

                        }else
                        {
                            Toast.makeText(NewComplaint.this, "no data", Toast.LENGTH_SHORT).show();
                            fullName.setEnabled(true);
                            mobileNo.setEnabled(true);
                            city.setEnabled(true);
                        }
                    }
                });
    }

    private void initialize() {

        toolbarBackBtn = findViewById(R.id.toolbar_back);
        spinner = findViewById(R.id.toDepartmentSpinnerNC);
        submitComplaintBtn = findViewById(R.id.submitComplaintBtn);
        fullName = findViewById(R.id.fullnameEditTextNC);
        mobileNo = findViewById(R.id.mobileEditTextNC);
        address = findViewById(R.id.addressEditTextNC);
        description = findViewById(R.id.descriptionEditTextNC);
        city = findViewById(R.id.cityEditTextNC);
        uploadTextView = findViewById(R.id.uploadTextView);
        uploadImgBtn = findViewById(R.id.uploadImgBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();


        spinner.setOnItemSelectedListener(this);
        //spinner setting
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,departmentList);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);;
        spinner.setAdapter(arrayAdapter);

        toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent openGallery = new Intent(Intent.ACTION_CAMERA_BUTTON, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               //startActivityForResult(openGallery,1000);

                if (ActivityCompat.checkSelfPermission(NewComplaint.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    //Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(cameraIntent,2000);


                    try {
                        String fileName = "photo";
                        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File imageFile = File.createTempFile(fileName,".jpg",storageDirectory);
                        currentPhotoPath = imageFile.getAbsolutePath();

                        Uri imageUri = FileProvider.getUriForFile(NewComplaint.this,"com.amitthakare.digitalcomplaint",imageFile);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,2000);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    ActivityCompat.requestPermissions(NewComplaint.this,new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                //Uri imageUri = data.getData();
                //uploadTextView.setText("Image Selected");
                //Toast.makeText(this, data.getExtras().get("data")+"", Toast.LENGTH_SHORT).show();
               // saveImgToFirebase(imageUri);


                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");

                //Toast.makeText(this, data.getData()+"", Toast.LENGTH_SHORT).show();
                //Bitmap bitmap = getBitmapByUri(data.getData());
                //Bitmap imageBitmap = bitmap.copy(bitmap.getConfig(),true);

                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                Bitmap imageBitmap = bitmap.copy(bitmap.getConfig(),true);





                //trying to add text
                Paint myPaint = new Paint();
                myPaint.setTextAlign(Paint.Align.RIGHT);
                myPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                myPaint.setTextSize(30);
                myPaint.setColor(Color.GREEN);

                int h = imageBitmap.getHeight();
                int w = imageBitmap.getWidth();
                if (h>=4000 && w>=2000)
                {
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap,w/2,h/2,false);
                    myPaint.setTextSize(60);

                }else if(h>=3000 && w>=1500)
                {
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap,(int)(w/1.5),(int)(h/1.5),false);
                    myPaint.setTextSize(35);

                }else if (h>=2000 && w>=1200)
                {
                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap,(int)(w/1.2),(int)(h/1.2),false);
                    myPaint.setTextSize(30);
                }

                //Bitmap newBitmap = imageBitmap.copy(Bitmap.Config.RGB_565,true);

                //Bitmap newBitmap = Bitmap.createBitmap(imageBitmap.getWidth(),imageBitmap.getHeight(),imageBitmap.getConfig());

                String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());


                Canvas canvas = new Canvas(imageBitmap);
                canvas.drawText(Variables.LOCALITY,imageBitmap.getWidth()-imageBitmap.getWidth()/8.0f,imageBitmap.getHeight()-imageBitmap.getHeight()/8.0f,myPaint);
                canvas.drawText(timeStamp,imageBitmap.getWidth()-imageBitmap.getWidth()/16.0f,imageBitmap.getHeight()-imageBitmap.getHeight()/16.0f,myPaint);


                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                if (tempUri != null)
                {
                    saveImgToFirebase(tempUri);
                    Log.e("Size:",imageBitmap.getHeight()+"  "+imageBitmap.getWidth());
                }

            }
        }


    }

    private Bitmap getBitmapByUri(Uri uri) {

        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 29) {
            ImageDecoder.Source source = ImageDecoder.createSource(getApplicationContext().getContentResolver(), uri);
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void checkValidation() {
        progressDialogNewComplaint = new ProgressDialog(NewComplaint.this);
        progressDialogNewComplaint.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialogNewComplaint.setTitle("Submitting Complaint...");
        progressDialogNewComplaint.show();

        submitComplaintBtn.setClickable(false);
        submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.faint_blue));

        if (!TextUtils.isEmpty(fullName.getText().toString()))
        {
            if (!TextUtils.isEmpty(mobileNo.getText().toString()) && mobileNo.getText().length() == 10)
            {
                if(!TextUtils.isEmpty(spinnerOption))
                {
                    if (!TextUtils.isEmpty(address.getText().toString()))
                    {
                        if (!TextUtils.isEmpty(city.getText().toString()))
                        {
                            if(!TextUtils.isEmpty(description.getText().toString()))
                            {
                                if (uploadTextView.getText().equals("Image Selected"))
                                {
                                    saveDataToFirebase();
                                }else
                                {
                                    Toast.makeText(this, "Please Upload Image!", Toast.LENGTH_SHORT).show();
                                    progressDialogNewComplaint.dismiss();
                                    submitComplaintBtn.setClickable(true);
                                    submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                                }
                            }else
                            {
                                description.setError("Enter Description!");
                                progressDialogNewComplaint.dismiss();
                                submitComplaintBtn.setClickable(true);
                                submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                            }
                        }else
                        {
                            city.setError("Enter Valid City!");
                            progressDialogNewComplaint.dismiss();
                            submitComplaintBtn.setClickable(true);
                            submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                        }
                    }else
                    {
                        address.setError("Enter Valid Address!");
                        progressDialogNewComplaint.dismiss();
                        submitComplaintBtn.setClickable(true);
                        submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                }else
                {
                    Toast.makeText(this, "Choose Department!", Toast.LENGTH_SHORT).show();
                    progressDialogNewComplaint.dismiss();
                    submitComplaintBtn.setClickable(true);
                    submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }else
            {
                mobileNo.setError("Enter Valid Mobile No!");
                progressDialogNewComplaint.dismiss();
                submitComplaintBtn.setClickable(true);
                submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }else
        {
            fullName.setError("Enter Valid Name!");
            progressDialogNewComplaint.dismiss();
            submitComplaintBtn.setClickable(true);
            submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
        }
    }

    private void saveDataToFirebase() {

        int random_int = (int)(Math.random() * (999999999 - 1000000 + 1) + 1000000);

        Map<String,Object> complaintData = new HashMap<>();
        complaintData.put("Image",imageUploadUri);
        complaintData.put("Full_Name",fullName.getText().toString());
        complaintData.put("Mobile_No",mobileNo.getText().toString());
        complaintData.put("Department",spinnerOption);
        complaintData.put("Address",address.getText().toString());
        complaintData.put("City",city.getText().toString());
        complaintData.put("Description",description.getText().toString());
        complaintData.put("Status","Submitted");


        DatabaseReference databaseReference = firebaseDatabase.getReference("Complaints").child(spinnerOption).child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.setValue(complaintData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(NewComplaint.this, "Complaint Submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                }else
                {
                    Toast.makeText(NewComplaint.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialogNewComplaint.dismiss();
                    submitComplaintBtn.setClickable(true);
                    submitComplaintBtn.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }
        });


    }

    //for upload profile image to firebase
    private void saveImgToFirebase(Uri imageUri) {

        int random_int = (int)(Math.random() * (999999999 - 1000000 + 1) + 1000000);

        //this code runs when all text data is stored in the firebase.
        //here we set the progress bar to show the percentage of file uploading into firebase.
        progressDialogImg = new ProgressDialog(this);
        progressDialogImg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialogImg.setTitle("Uploading Images...");
        progressDialogImg.setProgress(0);
        progressDialogImg.show();

        // upload image to firebase storage
        //below we define StorageReference that will help to store the image into Firebase Storage section
        //storageReference get the root directory.
        //storageReference.child("User/"+currentUser.getUid()+"aadhar.jpg") will create a structure like
        // User folder => Folder name UID  (to differentiate user ) => aadhar.jpg
        final StorageReference fileRef = storageReference.child("User/" + currentUser.getUid() + "/"+random_int+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //when image get uploaded then toast will occurs
                        imageUploadUri = uri.toString();
                        Toast.makeText(NewComplaint.this, "Image Uploaded!", Toast.LENGTH_LONG).show();
                        uploadTextView.setText("Image Selected");
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewComplaint.this, "Failed!", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                //Track the progress of our file from 0 to 100%
                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialogImg.setProgress(currentProgress);
                if (currentProgress == 100) {
                    progressDialogImg.dismiss();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, departmentList[position], Toast.LENGTH_SHORT).show();
        spinnerOption = departmentList[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}