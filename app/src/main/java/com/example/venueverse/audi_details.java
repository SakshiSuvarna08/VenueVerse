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

public class audi_details extends BaseActivity {

    private TextView placeName;
    private TextView placeDescription;
    private TextView placeAddress;
    private TextView detailPrice;
    private TextView detailAC; // For AC info
    private TextView detailRoom; // For Room info
    private TextView detailTheme; // For Theme info
    private TextView detailStage; // For Stage info
    private ImageView detailImage; // Optional image (can be uncommented if you add this in XML)
    private Button bookNow;
    private ViewFlipper viewFlipper;// Button for booking

    private FirebaseDatabase database;
    private String placeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.audi_deatils);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Set custom overflow icon
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));


        // Receive the venue type from Intent (can be used if needed for logic)


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
        viewFlipper = findViewById(R.id.viewFlipper);

        // Get the place key from the intent
        placeKey = getIntent().getStringExtra("placeKey");
        setImagesForViewFlipper();
        // Fetch details from Firebase
        fetchPlaceDetails();

        // Handle button click for booking
        bookNow.setOnClickListener(v -> {
            // Add your booking logic here
            Intent formIntent = new Intent(audi_details.this, FormActivity.class);
            Intent intent = getIntent();


            // Extract and convert the price to double
            String priceText = detailPrice.getText().toString().replace("Rs ", "");  // Removing "Rs " prefix if it's included
            try {
                priceText=priceText.replace(",", "");
                double price = Double.parseDouble(priceText);  // Convert the extracted text to a double
                formIntent.putExtra("price", price);           // Pass the double value to the next activity

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String Email = currentUser.getEmail();
                    formIntent.putExtra("Email", Email);
                }
                // Get the actual text of the venue name
                String venueName = placeName.getText().toString();
                formIntent.putExtra("venuename", venueName);   // Pass the venue name as text

                // Start the next activity
                startActivity(formIntent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the exception (optional: show a message or log the error)
            }
        });
    }
    private void setImagesForViewFlipper() {
        int[] images;

        switch (placeKey) {
            case "1":
                images = new int[]{R.drawable.i11, R.drawable.i12, R.drawable.i13, R.drawable.i1};
                break;
            case "2":
                images = new int[]{R.drawable.i21, R.drawable.i2, R.drawable.i21, R.drawable.i2};
                break;
            case "3":
                images = new int[]{R.drawable.i31, R.drawable.i32, R.drawable.i31, R.drawable.i3};
                break;
            case "4":
                images = new int[]{R.drawable.i41, R.drawable.i42, R.drawable.i43, R.drawable.i4};
                break;
            default:
                images = new int[]{R.drawable.i11, R.drawable.i12, R.drawable.i13, R.drawable.i1};
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
