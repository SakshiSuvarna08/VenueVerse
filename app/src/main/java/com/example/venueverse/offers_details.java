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

public class offers_details extends AppCompatActivity {

    private TextView placeName;
    private TextView placeDescription;
    private TextView placeAddress;
    private TextView detailPrice;
    private TextView detailAC; // Assuming this is for AC info
    private TextView detailRoom; // Assuming this is for Room info
    private TextView detailTheme; // Assuming this is for Theme info
    private TextView detailStage; // Assuming this is for Stage info
    private ImageView detailImage;
    private Button bookNow; // Button for booking

    private FirebaseDatabase database;
    private String placeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.convention_details);

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();

        // Initialize views
        placeName = findViewById(R.id.place_name_3); // Matches XML ID
        placeDescription = findViewById(R.id.place_descript3); // Updated to match XML ID
        placeAddress=findViewById(R.id.address3);
        detailPrice = findViewById(R.id.price3); // Matches XML ID
        detailAC = findViewById(R.id.ac3); // Matches XML ID
        detailRoom = findViewById(R.id.room3); // Matches XML ID
        detailTheme = findViewById(R.id.theme3); // Matches XML ID
        detailStage = findViewById(R.id.Stage3); // Matches XML ID
        // detailImage = findViewById(R.id.detail_image); // Uncomment if you add an image view in the XML
        bookNow = findViewById(R.id.booknow); // Matches XML ID

        // Get the place key from the intent
        placeKey = getIntent().getStringExtra("placeKey");

        // Fetch details from Firebase
        fetchPlaceDetails();

        // Handle button click
        bookNow.setOnClickListener(v -> {
            Intent formIntent = new Intent(offers_details.this, FormActivity.class);

            // formIntent.putExtra("venuename", String.valueOf(placeName));
            // formIntent.putExtra("price", String.valueOf( detailPrice));
            //  startActivity(formIntent);

        });
        //});
    }

    private void fetchPlaceDetails() {
        DatabaseReference placesRef = database.getReference("venue/convention").child(placeKey);

        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Place place = dataSnapshot.getValue(Place.class);
                    if (place != null) {
                        placeName.setText(place.getPlaceName());
                        placeAddress.setText(""+place.getPlaceAddress());
                        placeDescription.setText(" " + place.getPlaceDescription());
                        detailPrice.setText(" " + place.getPlacePrice());
                        detailAC.setText(" " + place.getAc());
                        detailRoom.setText(" " + place.getPlaceRoom());
                        detailTheme.setText(" " + place.getStageTheme());
                        detailStage.setText("" + place.getStage());
                    }
                } else {
                    Log.d("details_page_1", "No data found for key: " + placeKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("details_page1", "Error fetching data", databaseError.toException());
            }
        });
    }


    // Nested Place class
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

        // Constructor with parameters
        public Place(String placeName, String placeDescription, String placePrice, String placeAddress, String placeRoom, String AC, String StageTheme, String Stage) {
            this.placeName = placeName;
            this.placeDescription = placeDescription;
            this.placePrice = placePrice;
            this.placeRoom = placeRoom;
            this.placeAddress = placeAddress;
            this.AC = AC;
            this.StageTheme = StageTheme;
            this.Stage = Stage;

        }

        // Getter methods
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