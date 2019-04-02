package com.test.adhavan.touchmenot;

public class contact {
    private String name;
    private String phnumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhnumber() {
        return phnumber;
    }

    public void setPhnumber(String phnumber) {
        this.phnumber = phnumber;
    }

    public contact(String name, String phnumber){
        this.name = name;
        this.phnumber = phnumber;

    }
}
