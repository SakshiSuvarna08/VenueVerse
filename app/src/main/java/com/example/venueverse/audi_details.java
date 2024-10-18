package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class audi_details extends AppCompatActivity {

    private TextView placeName;
    private TextView placeDescription;
    private TextView placeAddress;
    private TextView detailPrice;
    private TextView detailAC; // For AC info
    private TextView detailRoom; // For Room info
    private TextView detailTheme; // For Theme info
    private TextView detailStage; // For Stage info
    private ImageView detailImage; // Optional image (can be uncommented if you add this in XML)
    private Button bookNow; // Button for booking

    private FirebaseDatabase database;
    private String placeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.audi_deatils);

        // Receive the venue type from Intent (can be used if needed for logic)
        Intent intent = getIntent();
        String valueReceived = intent.getStringExtra("venue_type");
        Log.d("auditoriums", "Received venue type: " + valueReceived);

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();

        // Initialize views
        placeName = findViewById(R.id.place_name_3);
        placeDescription = findViewById(R.id.place_descript3);
        placeAddress = findViewById(R.id.address3);
        detailPrice = findViewById(R.id.price3);
        detailAC = findViewById(R.id.ac3);
        detailRoom = findViewById(R.id.room3);
        detailTheme = findViewById(R.id.theme3);
        detailStage = findViewById(R.id.Stage3);
        // Uncomment if adding image view in the XML layout
        // detailImage = findViewById(R.id.detail_image);
        bookNow = findViewById(R.id.booknow);

        // Get the place key from the intent
        placeKey = getIntent().getStringExtra("placeKey");

        // Fetch details from Firebase
        fetchPlaceDetails();

        // Handle button click for booking
        bookNow.setOnClickListener(v -> {
            // Add your booking logic here
            Intent formIntent = new Intent(audi_details.this, FormActivity.class);

            // formIntent.putExtra("venuename", String.valueOf(placeName));
            // formIntent.putExtra("price", String.valueOf( detailPrice));
            //  startActivity(formIntent);

        });

    }

    private void fetchPlaceDetails() {
        DatabaseReference placesRef = database.getReference("venue3/auditoriums").child(placeKey);

        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Place place = dataSnapshot.getValue(Place.class);
                    if (place != null) {
                        // Set the details in the UI
                        placeName.setText(place.getPlaceName());
                        placeAddress.setText(place.getPlaceAddress());
                        placeDescription.setText(place.getPlaceDescription());
                        detailPrice.setText("Rs " + place.getPlacePrice());
                        detailAC.setText(place.getAc());
                        detailRoom.setText(place.getPlaceRoom());
                        detailTheme.setText(place.getStageTheme());
                        detailStage.setText(place.getStage());
                    }
                } else {
                    Log.d("audi_details", "No data found for key: " + placeKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("audi_details", "Error fetching data", databaseError.toException());
            }
        });
    }

    // Nested Place class to represent the place details
    public static class Place {
        private String placeName;
        private String placeDescription;
        private String placePrice;
        private String placeRoom;
        private String placeAddress;
        private String AC;
        private String StageTheme;
        private String Stage;

        // No-argument constructor required for Firebase
        public Place() {
        }

        // Getter methods for each property
        public String getPlaceName() {
            return placeName;
        }

        public String getPlaceDescription() {
            return placeDescription;
        }

        public String getPlacePrice() {
            return placePrice;
        }

        public String getPlaceAddress() {
            return placeAddress;
        }

        public String getPlaceRoom() {
            return placeRoom;
        }

        public String getAc() {
            return AC;
        }

        public String getStageTheme() {
            return StageTheme;
        }

        public String getStage() {
            return Stage;
        }
    }
}
