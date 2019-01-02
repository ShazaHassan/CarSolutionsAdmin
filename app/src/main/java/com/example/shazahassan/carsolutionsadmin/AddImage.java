package com.example.shazahassan.carsolutionsadmin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shazahassan.carsolutionsadmin.Adapter.AddImageAdapter;
import com.example.shazahassan.carsolutionsadmin.Model.DataForCar;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AddImage extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 2;
    private static int RESULT_LOAD_IMG = 1;
    private Uri imageUri;
    private ArrayList<Bitmap> imageViews = new ArrayList<>();
    private ArrayList<Uri> uris = new ArrayList<>();
    private ListView listView;
    private AddImageAdapter imageAdapter;
    //    private Image image;
    private StorageReference imageStorage;
    private DatabaseReference imageDatabase, carDatabase;
    private ArrayList<String> urlsImage = new ArrayList<>();
    private String imageName;
    private UploadTask uploadTask;
    private Context context;
    private ArrayList<Boolean> allFinished = new ArrayList<>();
    private String imageURL, idImg, carID;
    private int i = 0;
    private DataForCar dataForCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        listView = findViewById(R.id.list_view_from_cam);
        imageStorage = FirebaseStorage.getInstance().getReference();
        imageDatabase = FirebaseDatabase.getInstance().getReference();
        carDatabase = FirebaseDatabase.getInstance().getReference().child("cars");
        context = this;
        Intent intent = getIntent();
        dataForCar = intent.getParcelableExtra("car");
        Toast.makeText(context, dataForCar.getModel(), Toast.LENGTH_LONG).show();
    }

    public void uploadImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
                Log.v("imageuri", data.getData().toString() + " uri");

                uris.add(imageUri);
                imageAdapter = new AddImageAdapter(AddImage.this, R.layout.image_item, uris);
                allFinished.add(false);
                listView.setAdapter(imageAdapter);

            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
        if (reqCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Uri imageUri = data.getData();
//            Log.v("imageuri",data.getData().toString()+" uri");
            Log.v("uri", Integer.toString(uris.size()));
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            uris.add(tempUri);
            allFinished.add(false);

            imageViews.add(imageBitmap);
            imageAdapter = new AddImageAdapter(AddImage.this, R.layout.image_item, uris);
            listView.setAdapter(imageAdapter);
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }

    public void takePhoto(View view) {
        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void saveImage(View view) {

        if (uris.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Save data");
            builder.setMessage("Would you like to save data without image?");

            // add the buttons
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveDataDB();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(context,"Please select image to save data",Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            for (Uri imageUri : uris) {
                imageName = UUID.randomUUID().toString();
                final StorageReference ref = imageStorage.child("images/" + imageName);
                uploadTask = ref.putFile(imageUri);
                Task<Uri> urlTask = uploadTask
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploading... ");

                            }
                        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                // Continue with the task to get the download URL
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (i < allFinished.size()) {
                                    allFinished.set(i, true);
                                    i++;
                                    Uri downloadUri = task.getResult();
                                    imageURL = downloadUri.toString();
                                    urlsImage.add(imageURL);
//                                idImg = imageDatabase.push().getKey();
//                                imageDatabase.child(idImg).setValue(imageURL).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(context, "Saved data to database", Toast.LENGTH_LONG).show();
//                                    }
//                                });
                                }
                                if (allFinished.get(allFinished.size() - 1)) {
                                    saveDataDB();
                                    progressDialog.dismiss();
                                }

                            }
                        });
            }
        }
    }

    private void saveDataDB() {
        carID = carDatabase.push().getKey();
        dataForCar.setCarID(carID);
        dataForCar.setStatus("Available");
        carDatabase.child(carID).setValue(dataForCar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        if (urlsImage.size() > 0) {
            for (String downloadUrl : urlsImage) {
                idImg = carDatabase.child(carID).child("images").push().getKey();
                carDatabase.child(carID).child("images").child(idImg).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        }
        startActivity(new Intent(context, HomePage.class));
    }
}
