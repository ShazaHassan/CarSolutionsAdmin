package com.example.shazahassan.carsolutionsadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.shazahassan.carsolutionsadmin.Adapter.ListImagesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllImages extends AppCompatActivity {

    private ListView listView;
    private String carID;
    private DatabaseReference singleCarData;
    private ArrayList<String> links = new ArrayList<>();
    private ListImagesAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);
        Intent intent = getIntent();
        carID = intent.getStringExtra("carID");
        listView = findViewById(R.id.imageList);
        singleCarData = FirebaseDatabase.getInstance().getReference().child("cars").child(carID);
        singleCarData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                links.clear();
                for (DataSnapshot image: dataSnapshot.child("images").getChildren()){
                    String value = (String) image.getValue();
                    Log.v("imagelinka",value);
                    links.add(value);
                    Log.v("imagelinka",Integer.toString(links.size()));
                }
                adapter = new ListImagesAdapter(AllImages.this,R.layout.image_item_list,links);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
