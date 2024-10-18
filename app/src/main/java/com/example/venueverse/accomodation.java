package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class accomodation extends AppCompatActivity {

    private FirebaseDatabase database;
    private String valueReceived;  // To store the received venue type

    // TextViews for displaying place names and prices
    private TextView place1NameTextView;
    private TextView place1PriceTextView;
    private TextView place2NameTextView;
    private TextView place2PriceTextView;
    private TextView place3NameTextView;
    private TextView place3PriceTextView;
    private TextView place4NameTextView;
    private TextView place4PriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.accomodation);

        // Receiving the venue_type from the Intent
        Intent intent = getIntent();
        valueReceived = intent.getStringExtra("venue_type");
        Log.d("accomodation", "Received venue type: " + valueReceived);  // Log received venue_type

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();

        // Initialize TextViews
        place1NameTextView = findViewById(R.id.place_name_1);
        place1PriceTextView = findViewById(R.id.price1);
        place2NameTextView = findViewById(R.id.place_name_2);
        place2PriceTextView = findViewById(R.id.price2);
        place3NameTextView = findViewById(R.id.place_name_3);
        place3PriceTextView = findViewById(R.id.price3);
        place4NameTextView = findViewById(R.id.place_name_4);
        place4PriceTextView = findViewById(R.id.price4);

        // Fetch data from Firebase
        fetchPlaceNamesAndPrices();

        // Set up click listeners for each RelativeLayout
        RelativeLayout place1Layout = findViewById(R.id.relative1);
        place1Layout.setOnClickListener(v -> openDetailsPage("1"));

        RelativeLayout place2Layout = findViewById(R.id.relative2);
        place2Layout.setOnClickListener(v -> openDetailsPage("2"));

        RelativeLayout place3Layout = findViewById(R.id.relative3);
        place3Layout.setOnClickListener(v -> openDetailsPage("3"));

        RelativeLayout place4Layout = findViewById(R.id.relative4);
        place4Layout.setOnClickListener(v -> openDetailsPage("4"));
    }

    private void fetchPlaceNamesAndPrices() {
        // Fetching from "venue1/accomodation" but can customize based on venue_type
        DatabaseReference placesRef = database.getReference("venue1/accomodation");

        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                        Place place = placeSnapshot.getValue(Place.class);
                        if (place != null) {
                            String placeKey = placeSnapshot.getKey();
                            Log.d("MainActivity", "Fetched place: " + placeKey + ", " + place.getPlaceName() + ", " + place.getPlacePrice());

                            switch (placeKey) {
                                case "1":
                                    //String priceString = place.getPlacePrice();
                                   // int price = Integer.parseInt(priceString);
                                   // place1NameTextView.setText(place.getPlaceName());
                                    //place1PriceTextView.setText(String.valueOf(price));
                                    place1NameTextView.setText(place.getPlaceName());
                                    place1PriceTextView.setText(place.getPlacePrice());
                                    break;
                                case "2":
                                    place2NameTextView.setText(place.getPlaceName());
                                    place2PriceTextView.setText(place.getPlacePrice());
                                    break;
                                case "3":
                                    place3NameTextView.setText(place.getPlaceName());
                                    place3PriceTextView.setText(place.getPlacePrice());
                                    break;
                                case "4":
                                    place4NameTextView.setText(place.getPlaceName());
                                    place4PriceTextView.setText(place.getPlacePrice());
                                    break;
                                default:
                                    Log.d("MainActivity", "Unknown place key: " + placeKey);
                                    break;
                            }
                        }
                    }
                } else {
                    Log.d("MainActivity", "No data found in venue1/accomodation");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Error fetching data", databaseError.toException());
            }
        });
    }

    private void openDetailsPage(String placeKey) {
        // Open details page with the placeKey
        Intent intent = new Intent(this, accomd_details.class);
        intent.putExtra("placeKey", placeKey);  // Pass the place key
        startActivity(intent);
    }

    // Inner class to represent a Place object
    public static class Place {
        private String placeName;
        private String placePrice;

        public Place() {
            // Default constructor required for calls to DataSnapshot.getValue(Place.class)
        }

        public String getPlaceName() {
            return placeName;
        }

        public String getPlacePrice() {
            return placePrice;
        }
    }
}
