package com.study.android.telephonyex;

import android.graphics.Bitmap;

public class AddressItem {

    private  String name;



    private String telnum;
    private Bitmap photo;

    public AddressItem(String name, String telnum, Bitmap photo) {
        this.name = name;
        this.telnum = telnum;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }


}