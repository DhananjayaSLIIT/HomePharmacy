package com.example.homepharmacy;

import com.google.firebase.database.Exclude;

public class cUpload {
    private String mName;
    private String mPrice;
    private String mQuantity;
    private String mImageUrl;
    private String mKey;

    public cUpload(){
        //Empty constructor needed
    }

    public cUpload(String mName, String mPrice, String mQuantity, String mImageUrl) {
        if (mName.trim().equals("")){
            mName = "Empty Name";
        }
        this.mName = mName;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
        this.mImageUrl = mImageUrl;
        this.mKey = mKey;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmQuantity(String mQuantity) {
        this.mQuantity = mQuantity;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
