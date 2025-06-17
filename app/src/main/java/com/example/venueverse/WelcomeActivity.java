package com.example.venueverse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

        Intent intent = getIntent();
        final String[] email = {intent.getStringExtra("email")};

        Accommodation_cardView = findViewById(R.id.cardview2);
        Convention_cardView = findViewById(R.id.cardview5);
        Sports_cardView = findViewById(R.id.cardview4);
        Auditorium_cardView = findViewById(R.id.cardview3);

        imageSlider = findViewById(R.id.imageSlider);

        if (imageSlider != null) {
            ArrayList<SlideModel> slideModels = new ArrayList<>();
            slideModels.add(new SlideModel(R.drawable.countryinn, ScaleTypes.FIT));
            slideModels.add(new SlideModel(R.drawable.joyconvetion, ScaleTypes.FIT));
            slideModels.add(new SlideModel(R.drawable.greenfield, ScaleTypes.FIT));
            slideModels.add(new SlideModel(R.drawable.paiaudti, ScaleTypes.FIT));
            imageSlider.setImageList(slideModels, ScaleTypes.FIT);

            imageSlider.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemSelected(int position) {
                    Intent next_page = new Intent(WelcomeActivity.this, offers_details.class);

                    switch (position) {
                        case 0: // First image -> Accommodation
                            next_page.putExtra("venue_type", "Accommodation");
                            next_page.putExtra("placeKey", "accommodationPlaceKey");
                            break;
                        case 1: // Second image -> Convention
                            next_page.putExtra("venue_type", "Convention");
                            next_page.putExtra("placeKey", "conventionPlaceKey");
                            break;
                        case 2: // Third image -> Sports
                            next_page.putExtra("venue_type", "Sports");
                            next_page.putExtra("placeKey", "sportsPlaceKey");
                            break;
                        case 3: // Fourth image -> Auditorium
                            next_page.putExtra("venue_type", "Auditorium");
                            next_page.putExtra("placeKey", "auditoriumPlaceKey");
                            break;
                        default:
                            return;
                    }
                    next_page.putExtra("Email", email[0]);
                    startActivity(next_page);
                }
            });
        } else {
            Log.e("WelcomeActivity", "ImageSlider is null!");
        }

        setUpCardViewClickListeners(intent, email);
    }

    private void setUpCardViewClickListeners(Intent intent, String[] email) {
        Accommodation_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, accomodation.class);
                next_page.putExtra("venue_type", "Accommodation");
                next_page.putExtra("email", email[0]);
                startActivity(next_page);
            }
        });

        Convention_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, convention.class);
                next_page.putExtra("venue_conv", "Convention");
                next_page.putExtra("email", email[0]);
                startActivity(next_page);
            }
        });

        Sports_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, sports.class);
                next_page.putExtra("venue_type", "Sports");
                next_page.putExtra("email", email[0]);
                startActivity(next_page);
            }
        });

        Auditorium_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next_page = new Intent(WelcomeActivity.this, auditorium.class);
                next_page.putExtra("venue_type", "Auditorium");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String Email = currentUser.getEmail();
                    intent.putExtra("Email", Email);
                }
                startActivity(next_page);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please logout to go back", Toast.LENGTH_SHORT).show();
    }
}
