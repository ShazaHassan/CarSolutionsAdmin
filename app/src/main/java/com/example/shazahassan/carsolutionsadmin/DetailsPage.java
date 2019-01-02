package com.example.shazahassan.carsolutionsadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsPage extends AppCompatActivity {

    private String carID ;
    private DatabaseReference singleCar;
    private TextView modelText,colorText,contactText,chachissText,detailsText,moreDetailsText,importDateText,noImage,
            status,seeAllImages;
    private ImageView carImage;
    private ArrayList<String> links = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);
        Intent intent = getIntent();
        carID = intent.getStringExtra("carID");
        Log.v("carID", carID);
        modelText = findViewById(R.id.modelDataDetails);
        colorText = findViewById(R.id.colorDataDetails);
        contactText = findViewById(R.id.contactDataDetails);
        chachissText = findViewById(R.id.chachissDataDetails);
        detailsText = findViewById(R.id.moreDetailsDataDetails);
        moreDetailsText = findViewById(R.id.moreDetailsDetails);
        importDateText = findViewById(R.id.importDateDataDetails);
        noImage = findViewById(R.id.no_img_details);
        seeAllImages = findViewById(R.id.see_all_images_details);
        carImage = findViewById(R.id.car_image_details);
        status = findViewById(R.id.availableDetails);
        singleCar = FirebaseDatabase.getInstance().getReference().child("cars").child(carID);
        getDataFromDB();
    }

    private void getDataFromDB() {
        singleCar.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("images")){
                    noImage.setVisibility(View.GONE);
                    links.clear();
                    for (DataSnapshot image: dataSnapshot.child("images").getChildren()){
                        String value = (String) image.getValue();
                        Log.v("imagelink",value);
                        links.add(value);
                    }
                    Picasso.get().load(links.get(0)).into(carImage);
                }
                else {
                    carImage.setVisibility(View.INVISIBLE);
                    seeAllImages.setVisibility(View.INVISIBLE);
                }
                String model,color,chachissNo,contactNo,importDate,moreDetails,carID,s;

                model = (String) dataSnapshot.child("model").getValue();
                color = (String) dataSnapshot.child("color").getValue();
                chachissNo =  (String) dataSnapshot.child("chachissNo").getValue();
                contactNo = (String) dataSnapshot.child("contactNo").getValue();
                importDate = (String) dataSnapshot.child("importDate").getValue();
                moreDetails =(String) dataSnapshot.child("moreDetails").getValue();
                s = (String) dataSnapshot.child("status").getValue();
                carID = (String) dataSnapshot.child("carID").getValue();

                if(moreDetails.equals("")){
                    detailsText.setVisibility(View.GONE);
                    moreDetailsText.setVisibility(View.GONE);
                }else{
                    modelText.setText(model);
                    colorText.setText(color);
                    chachissText.setText(chachissNo);
                    contactText.setText(contactNo);
                    importDateText.setText(importDate);
                    detailsText.setText(moreDetails);
                    status.setText(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            Intent editPage = new Intent(this,EditPage.class);
            editPage.putExtra("carID",this.carID);
            startActivity(editPage);
        } else if(id == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete car");
            builder.setMessage("Would you like to delete this car?");

            // add the buttons
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    singleCar.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(DetailsPage.this,"delete success",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(DetailsPage.this,HomePage.class));
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    public void seeAllImages(View view) {
        Intent img = new Intent(this,AllImages.class);
        img.putExtra("carID",carID);
        startActivity(img);
    }
}
