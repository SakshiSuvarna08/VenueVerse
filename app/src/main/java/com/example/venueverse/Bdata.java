package com.example.VenueVerse;

public class Bdata {

    String Date;
    int Number_of_Days;
    double Amount_payed;
    String UserName,VenueName;

    public Bdata() {
    }

    public Bdata(String userName, String venueName, String date, int number_of_Days, double amount_payed) {
        UserName = userName;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getVenueName() {
        return VenueName;
    }

    public void setVenueName(String venueName) {
        VenueName = venueName;
    }
}
