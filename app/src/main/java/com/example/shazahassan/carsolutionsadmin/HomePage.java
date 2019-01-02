package com.example.shazahassan.carsolutionsadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.shazahassan.carsolutionsadmin.Adapter.HomeListViewShowCars;
import com.example.shazahassan.carsolutionsadmin.Model.DataForCar;
import com.example.shazahassan.carsolutionsadmin.Model.GetDataFromDB;
import com.example.shazahassan.carsolutionsadmin.Model.ImagesLinks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navView;
    private DatabaseReference car;
    private DataForCar dataForCar;
    private GetDataFromDB getDataFromDB;
    private ArrayList <GetDataFromDB> dataFromDBS = new ArrayList<>();
    private ArrayList<DataForCar> dataForCars = new ArrayList<>();
    private ListView listView;
    private HomeListViewShowCars showCars;
    private ArrayList<String> links = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        setupDrawer();
        car = FirebaseDatabase.getInstance().getReference().child("cars");
        listView = findViewById(R.id.cars_item);
        getDataFromDB();
    }

    private void setupDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.add_new_car) {
            // Handle the camera action
            startActivity(new Intent(this, AddNewCar.class));
        } else if (id == R.id.home_page) {
            startActivity(new Intent(this,HomePage.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getDataFromDB(){
        car.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataForCars.clear();
                dataFromDBS.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    if(snapshot.hasChild("images")){
                        links.clear();
                        for (DataSnapshot image: snapshot.child("images").getChildren()){
                            String value = (String) image.getValue();
                            Log.v("imagelink",value);
                            links.add(value);
                        }
                        String model,color,chachissNo,contactNo,importDate,moreDetails,carID,imgURL;

                        model = (String) snapshot.child("model").getValue();
                        color = (String) snapshot.child("color").getValue();
                        chachissNo =  (String) snapshot.child("chachissNo").getValue();
                        contactNo = (String) snapshot.child("contactNo").getValue();
                        importDate = (String) snapshot.child("importDate").getValue();
                        moreDetails =(String) snapshot.child("moreDetails").getValue();
                        carID = (String) snapshot.child("carID").getValue();
                        imgURL = links.get(0);
                        dataFromDBS.add(new GetDataFromDB(model,color,
                                chachissNo,contactNo,importDate,moreDetails,carID,imgURL));
                    } else{
                        String model,color,chachissNo,contactNo,importDate,moreDetails,carID,imgURL;

                        model = (String) snapshot.child("model").getValue();
                        color = (String) snapshot.child("color").getValue();
                        chachissNo =  (String) snapshot.child("chachissNo").getValue();
                        contactNo = (String) snapshot.child("contactNo").getValue();
                        importDate = (String) snapshot.child("importDate").getValue();
                        moreDetails =(String) snapshot.child("moreDetails").getValue();
                        carID = (String) snapshot.child("carID").getValue();
                        imgURL = "";
                        dataFromDBS.add(new GetDataFromDB(model,color,
                                chachissNo,contactNo,importDate,moreDetails,carID,imgURL));
                    }
                }
                showCars = new HomeListViewShowCars(HomePage.this,R.layout.car_item_in_list_view,dataFromDBS);
                listView.setAdapter(showCars);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}