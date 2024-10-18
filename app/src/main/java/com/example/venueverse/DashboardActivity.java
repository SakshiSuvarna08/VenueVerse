package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends BaseActivity {

    LinearLayout bookingsContainer;
    DatabaseReference databaseReference;
    String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize the container layout
        bookingsContainer = findViewById(R.id.bookings_container);

        // Get intent data
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        //use this as no username extraction logic present
        email = intent.getStringExtra("username");

        if (username == null || username.isEmpty()) {
            username = "nk";  // Default username in case it's not passed
        }

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Bdata");

        // Fetch all bookings made by this user
        fetchAllBookingsFromFirebase();
    }

    private void fetchAllBookingsFromFirebase() {
        // Query data from Firebase based on the UserName field
        databaseReference.orderByChild("userName").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if data exists
                        if (dataSnapshot.exists()) {
                            // Loop through all bookings for this user
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Fetch data from each snapshot
                                String venueName = snapshot.child("venueName").getValue(String.class);
                                String eventDate = snapshot.child("date").getValue(String.class);
                                Integer numberOfDays = snapshot.child("number_of_Days").getValue(Integer.class);
                                Integer totalAmount = snapshot.child("amount_payed").getValue(Integer.class);

                                // Inflate the booking_item layout and populate it with data
                                addBookingToLayout(venueName, eventDate, numberOfDays, totalAmount);
                            }
                        } else {
                            // Handle case where no data is found
                            TextView noDataText = new TextView(DashboardActivity.this);
                            noDataText.setText("No bookings found.");
                            bookingsContainer.addView(noDataText);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur
                        TextView errorText = new TextView(DashboardActivity.this);
                        errorText.setText("Error: " + databaseError.getMessage());
                        bookingsContainer.addView(errorText);
                    }
                });
    }

    private void addBookingToLayout(String venueName, String eventDate, Integer numberOfDays, Integer totalAmount) {
        // Inflate booking_item.xml layout
        View bookingView = LayoutInflater.from(this).inflate(R.layout.booking_item, bookingsContainer, false);

        // Set data in the inflated layout
        TextView venueNameText = bookingView.findViewById(R.id.venuename);
        TextView eventDateText = bookingView.findViewById(R.id.event_date);
        TextView numberOfDaysText = bookingView.findViewById(R.id.number_ofdays);
        TextView totalAmountText = bookingView.findViewById(R.id.total_amount);

        venueNameText.setText("Venue: " + venueName);
        eventDateText.setText("Date: " + eventDate);
        numberOfDaysText.setText("Number of Days: " + numberOfDays);
        totalAmountText.setText("Total Amount: ₹" + totalAmount);

        // Add the inflated view to the container
        bookingsContainer.addView(bookingView);
    }

}
