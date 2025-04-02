package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private TextView detailAC;
    private TextView detailRoom;
    private TextView detailTheme;
    private TextView detailStage;
    private ImageView detailImage;
    private Button bookNow;

    private FirebaseDatabase database;
    private String placeKey;
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.offers_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

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
        bookNow = findViewById(R.id.booknow);
        viewFlipper = findViewById(R.id.viewFlipper);

        // Get the place key from the intent
        placeKey = getIntent().getStringExtra("placeKey");
        if (placeKey == null) {
            Log.e("offers_details", "placeKey is missing from the intent.");
            finish(); // Exit the activity or handle the error appropriately
            return;
        }

        // Fetch details from Firebase
        fetchPlaceDetails();
        setImagesForViewFlipper();  // Call the method to set images for view flipper

        // Handle button click
        bookNow.setOnClickListener(v -> {
            Intent formIntent = new Intent(offers_details.this, FormActivity.class);

            // Extract and convert the price to double
            String priceText = detailPrice.getText().toString().replace("Rs ", "");
            try {
                priceText = priceText.replace(",", "");
                double price = Double.parseDouble(priceText);
                formIntent.putExtra("price", price);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String Email = currentUser.getEmail();
                    formIntent.putExtra("Email", Email);
                }
                // Get the actual text of the venue name
                String venueName = placeName.getText().toString();
                formIntent.putExtra("venuename", venueName);

                // Start the next activity
                startActivity(formIntent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    private void setImagesForViewFlipper() {
        int[] images;

        switch (placeKey) {
            case "accommodationPlaceKey":
                images = new int[]{R.drawable.o11, R.drawable.o12, R.drawable.o13, R.drawable.o14};
                break;
            case "auditoriumPlaceKey":
                images = new int[]{R.drawable.o41, R.drawable.o42, R.drawable.o43, R.drawable.o44};

                break;
            case "conventionPlaceKey":
                images = new int[]{R.drawable.o21, R.drawable.o22, R.drawable.o23, R.drawable.o24};

                break;
            case "sportsPlaceKey":
                images = new int[]{R.drawable.o31, R.drawable.o32, R.drawable.o33, R.drawable.o34};

                break;
            default:
                images = new int[]{R.drawable.o11, R.drawable.o12, R.drawable.o13, R.drawable.o14};
                return;
        }



        viewFlipper.removeAllViews();

        for (int imageRes : images) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageRes);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewFlipper.addView(imageView);
        }
    }

    private void fetchPlaceDetails() {
        // Determine the table based on the placeKey
        String tableName;
        switch (placeKey) {
            case "accommodationPlaceKey":
                tableName = "accommodation";
                break;
            case "auditoriumPlaceKey":
                tableName = "auditorium";
                break;
            case "conventionPlaceKey":
                tableName = "convention";
                break;
            case "sportsPlaceKey":
                tableName = "sports";
                break;
            default:
                Log.e("offers_details", "Invalid place key: " + placeKey);
                return; // Exit if the key is invalid
        }

        // Reference the corresponding table in Firebase
        DatabaseReference placesRef = database.getReference("offers").child(tableName);

        Log.d("offers_details", "Fetching data from path: " + placesRef.toString());

        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("offers_details", "Data snapshot exists: " + dataSnapshot.exists());
                if (dataSnapshot.exists()) {
                    // Get the details into the Place class
                    Place place = dataSnapshot.getValue(Place.class);
                    if (place != null) {
                        // Set the values to the UI elements
                        placeName.setText(place.getPlaceName());
                        placeAddress.setText(place.getPlaceAddress());
                        placeDescription.setText(place.getPlaceDescription());
                        detailPrice.setText(place.getPlacePrice());
                        detailAC.setText(place.getAc());
                        detailRoom.setText(place.getPlaceRoom());
                        detailTheme.setText(place.getStageTheme());
                        detailStage.setText(place.getStage());
                    } else {
                        Log.d("offers_details", "Place object is null for key: " + placeKey);
                    }
                } else {
                    Log.d("offers_details", "No data found for key: " + placeKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("offers_details", "Error fetching data", databaseError.toException());
            }
        });
    }

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
        public Place() {}

        // Getters
        public String getPlaceName() { return placeName; }
        public String getPlaceDescription() { return placeDescription; }
        public String getPlacePrice() { return placePrice; }
        public String getPlaceAddress() { return placeAddress; }
        public String getPlaceRoom() { return placeRoom; }
        public String getAc() { return AC; }
        public String getStageTheme() { return StageTheme; }
        public String getStage() { return Stage; }
    }
}
