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

public class convention_details extends BaseActivity {

    private TextView placeName;
    private TextView placeDescription;
    private TextView placeAddress;
    private TextView detailPrice;
    private TextView detailAC; // Assuming this is for AC info
    private TextView detailRoom; // Assuming this is for Room info
    private TextView detailTheme; // Assuming this is for Theme info
    private TextView detailStage; // Assuming this is for Stage info
    private ImageView detailImage;
    private Button bookNow;
    private ViewFlipper viewFlipper;// Button for booking

    private FirebaseDatabase database;
    private String placeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.convention_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Set custom overflow icon
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));


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
        viewFlipper = findViewById(R.id.viewFlipper);
        setImagesForViewFlipper();

        // Handle button click
        bookNow.setOnClickListener(v -> {
            Intent formIntent = new Intent(convention_details.this, FormActivity.class);
            Intent intent = getIntent();

            // Extract and convert the price to double
            String priceText = detailPrice.getText().toString().replace("Rs ", "");  // Removing "Rs " prefix if it's included
            try {
                priceText=priceText.replace(",", "");
                double price = Double.parseDouble(priceText);  // Convert the extracted text to a double
                formIntent.putExtra("price", price);           // Pass the double value to the next activity

                // Extract the email from the intent
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
        });}

    private void setImagesForViewFlipper() {
        int[] images;

        switch (placeKey) {
            case "1":
                images = new int[]{R.drawable.c11, R.drawable.c12, R.drawable.c13, R.drawable.c14};
                break;
            case "2":
                images = new int[]{R.drawable.c21, R.drawable.c22, R.drawable.c23, R.drawable.c24};
                break;
            case "3":
                images = new int[]{R.drawable.c31, R.drawable.c32, R.drawable.c33, R.drawable.c34};
                break;
            case "4":
                images = new int[]{R.drawable.c41, R.drawable.c42, R.drawable.c43, R.drawable.c4};
                break;
            default:
                images = new int[]{R.drawable.c11, R.drawable.c12, R.drawable.c13, R.drawable.c14};
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