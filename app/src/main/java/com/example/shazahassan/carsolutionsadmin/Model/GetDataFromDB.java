package com.example.shazahassan.carsolutionsadmin.Model;

/**
 * Created by Shaza Hassan on 16-Oct-18.
 */
public class GetDataFromDB {
    String model,color,chachissNo,contactNo,importDate,moreDetails,carID,images;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChachissNo() {
        return chachissNo;
    }

    public void setChachissNo(String chachissNo) {
        this.chachissNo = chachissNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public GetDataFromDB(String model, String color, String chachissNo, String contactNo, String importDate, String moreDetails, String carID, String images) {

        this.model = model;
        this.color = color;
        this.chachissNo = chachissNo;
        this.contactNo = contactNo;
        this.importDate = importDate;
        this.moreDetails = moreDetails;
        this.carID = carID;
        this.images = images;
    }
}
