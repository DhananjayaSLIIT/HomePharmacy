package com.example.homepharmacy;

public class cUserHelperClass {
    private String name,password,as;
    private String  phone;

    public cUserHelperClass(){
        // needed
    }

    public cUserHelperClass(String type, String name, String password, String phone) {
        as = "user";
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }
}
