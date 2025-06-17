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

public class accomd_details extends BaseActivity {

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
        setContentView(R.layout.accomd_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

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
         // Initialize ViewFlipper

        placeKey = getIntent().getStringExtra("placeKey");

        fetchPlaceDetails();
        viewFlipper = findViewById(R.id.viewFlipper);
        setImagesForViewFlipper(); // Set images for ViewFlipper based on placeKey

        bookNow.setOnClickListener(v -> {
            Intent formIntent = new Intent(accomd_details.this, FormActivity.class);

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

                String venueName = placeName.getText().toString();
                formIntent.putExtra("venuename", venueName);

                startActivity(formIntent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    private void setImagesForViewFlipper() {
        int[] images;

        switch (placeKey) {
            case "1":
                images = new int[]{R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a1};
                break;
            case "2":
                images = new int[]{R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a2};
                break;
            case "3":
                images = new int[]{R.drawable.a31, R.drawable.a32, R.drawable.a33, R.drawable.a3};
                break;
            case "4":
                images = new int[]{R.drawable.a41, R.drawable.a42, R.drawable.a43, R.drawable.a4};
                break;
            default:
                images = new int[]{R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a2};
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
        DatabaseReference placesRef = database.getReference("venue1/accomodation").child(placeKey);

        placesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Place place = dataSnapshot.getValue(Place.class);
                    if (place != null) {
                        placeName.setText(place.getPlaceName());
                        placeAddress.setText("" + place.getPlaceAddress());
                        placeDescription.setText(" " + place.getPlaceDescription());
                        detailPrice.setText(" " + place.getPlacePrice());
                        detailAC.setText(" " + place.getAc());
                        detailRoom.setText(" " + place.getPlaceRoom());
                        detailTheme.setText(" " + place.getFacility());
                        detailStage.setText("" + place.getFood());
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

    public static class Place {
        private String placeName;
        private String placeDescription;
        private String placePrice;
        private String placeRoom;
        private String placeAddress;
        private String AC;
        private String Food;
        private String Facility;

        public Place() {
        }

        public Place(String placeName, String placeDescription, String placePrice, String placeAddress, String placeRoom, String AC, String StageTheme, String Stage) {
            this.placeName = placeName;
            this.placeDescription = placeDescription;
            this.placePrice = placePrice;
            this.placeRoom = placeRoom;
            this.placeAddress = placeAddress;
            this.AC = AC;
            this.Food = Food;
            this.Facility = Facility;
        }

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

        public String getFood() {
            return Food;
        }

        public String getFacility() {
            return Facility;
        }
    }
}
