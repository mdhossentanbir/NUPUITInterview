package com.mdhossentanbir.nupuitinterview;

/**
 * Created by Tanbir on 31-Mar-17.
 */

public class ContactList {

    String name, number;

    //Empty Constractor
    public ContactList(){

    }
    public ContactList(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
