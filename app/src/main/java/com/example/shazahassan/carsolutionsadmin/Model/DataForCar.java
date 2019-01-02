package com.example.shazahassan.carsolutionsadmin.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shaza Hassan on 16-Oct-18.
 */
public class DataForCar implements Parcelable {
    String model,color,chachissNo,contactNo,importDate,moreDetails,carID, status;

    public DataForCar(String model, String color, String importDate, String chachissNo, String contactNo, String moreDetails,String carID) {
        this.model = model;
        this.color = color;
        this.chachissNo = chachissNo;
        this.importDate = importDate;
        this.contactNo = contactNo;
        this.moreDetails = moreDetails;
        this.carID = carID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataForCar(String model, String color, String chachissNo, String contactNo, String importDate, String moreDetails) {
        this.model = model;
        this.color = color;
        this.chachissNo = chachissNo;
        this.contactNo = contactNo;
        this.importDate = importDate;
        this.moreDetails = moreDetails;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    protected DataForCar(Parcel in) {
        model = in.readString();
        color = in.readString();
        chachissNo = in.readString();
        contactNo = in.readString();
        importDate = in.readString();
        moreDetails = in.readString();
        carID = in.readString();
    }

    public static final Creator<DataForCar> CREATOR = new Creator<DataForCar>() {
        @Override
        public DataForCar createFromParcel(Parcel in) {
            return new DataForCar(in);
        }

        @Override
        public DataForCar[] newArray(int size) {
            return new DataForCar[size];
        }
    };

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getChachissNo() {
        return chachissNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getImportDate() {
        return importDate;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public String getCarID() {
        return carID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(model);
        parcel.writeString(color);
        parcel.writeString(chachissNo);
        parcel.writeString(contactNo);
        parcel.writeString(importDate);
        parcel.writeString(moreDetails);
    }
}
