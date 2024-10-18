package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class WelcomeActivity extends BaseActivity {

    ImageSlider imageSlider;
    CardView Accommodation_cardView, Convention_cardView, Sports_cardView, Auditorium_cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Accommodation_cardView = findViewById(R.id.cardview2);
        Convention_cardView = findViewById(R.id.cardview5);
        Sports_cardView = findViewById(R.id.cardview4);
        Auditorium_cardView = findViewById(R.id.cardview3);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                Intent next_page = new Intent(WelcomeActivity.this, offers_details.class);

                // Switch based on image position and pass appropriate venue type
                switch (position) {
                    case 0: // First image -> Accommodation
                        next_page.putExtra("venue_type", "Accommodation");
                        next_page.putExtra("placeKey", "accommodationPlaceKey");  // Pass the key for Accommodation
                        break;
                    case 1: // Second image -> Convention
                        next_page.putExtra("venue_type", "Convention");
                        next_page.putExtra("placeKey", "conventionPlaceKey");  // Pass the key for Convention
                        break;
                    case 2: // Third image -> Sports
                        next_page.putExtra("venue_type", "Sports");
                        next_page.putExtra("placeKey", "sportsPlaceKey");  // Pass the key for Sports
                        break;
                    case 3: // Fourth image -> Auditorium
                        next_page.putExtra("venue_type", "Auditorium");
                        next_page.putExtra("placeKey", "auditoriumPlaceKey");  // Pass the key for Auditorium
                        break;
                    default:
                        return; // Do nothing if the position is invalid
                }

                // Start the details activity with the selected venue type
                startActivity(next_page);
            }
        });

        imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.countryinn, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.joyconvetion, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.greenfield, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.paiaudti, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                // Handle image slider item clicks here if necessary
            }
        });

        // Handle click on Accommodation card
        Accommodation_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, accomodation.class);
                next_page.putExtra("venue_type", "Accommodation");  // Pass venue_type as "Accommodation"
                startActivity(next_page);
            }
        });

        // Handle click on Convention card
        Convention_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, convention.class);
                next_page.putExtra("venue_conv", "Convention");
                startActivity(next_page);
            }
        });

        // Handle click on Sports card
        Sports_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, sports.class);
                next_page.putExtra("venue_type", "Sports");
                startActivity(next_page);
            }
        });

        // Handle click on Auditorium card
        Auditorium_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, auditorium.class);
                next_page.putExtra("venue_type", "Auditorium");
                startActivity(next_page);
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        finishAffinity();  // Close all activities
    }*/
}
