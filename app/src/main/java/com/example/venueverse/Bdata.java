package com.example.venueverse;

public class Bdata {

    String Date;
    int Number_of_Days;
    double Amount_payed;
    String Email,VenueName;

    public Bdata() {
    }

    public Bdata(String email, String venueName, String date, int number_of_Days, double amount_payed) {
        Email = email;
        VenueName = venueName;
        Date = date;
        Number_of_Days = number_of_Days;
        Amount_payed = amount_payed;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getNumber_of_Days() {
        return Number_of_Days;
    }

    public void setNumber_of_Days(int number_of_Days) {
        Number_of_Days = number_of_Days;
    }

    public double getAmount_payed() {
        return Amount_payed;
    }

    public void setAmount_payed(double amount_payed) {
        Amount_payed = amount_payed;
    }

    public String getEmail() {
        return Email;
    }

    public void setUserName(String userName) {
        Email = userName;
    }

    public String getVenueName() {
        return VenueName;
    }

    public void setVenueName(String venueName) {
        VenueName = venueName;
    }
}
