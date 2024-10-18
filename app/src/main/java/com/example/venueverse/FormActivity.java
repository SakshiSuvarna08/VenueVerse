package com.example.venueverse;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {


    FirebaseDatabase db;
    DatabaseReference reference;
    EditText eventDate, guestCount;
    NumberPicker daysPicker;
    RadioGroup packageRadioGroup;
    Button payButton, datePickerButton;
    String venuename,username;
    double price,totalAmount;// To hold venue name passed from previous page

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Initialize views
        eventDate = findViewById(R.id.event_date);
        guestCount = findViewById(R.id.guest_count);  // Initialize guestCount EditText
        packageRadioGroup = findViewById(R.id.package_radio_group);
        payButton = findViewById(R.id.pay_button);
        datePickerButton = findViewById(R.id.btn_date_picker);
        daysPicker = findViewById(R.id.days_picker);

        // Initialize Firebase database
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Bdata");

        // Get venue name from intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "nk";  // Use a default value or handle the error
        }
        venuename = intent.getStringExtra("venuename");
        if (venuename == null || venuename.isEmpty()) {
            venuename = "aa";  // Use a default value or handle the error
        }
        price = intent.getDoubleExtra("price",500);


        // Configure NumberPicker for number of days
        daysPicker.setMinValue(1);
        daysPicker.setMaxValue(20);
        daysPicker.setValue(1);  // Set initial value to 1

        // Initialize the pay button with default value
        updateTotalAmount();

        // Set listeners to update total amount dynamically
        packageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateTotalAmount();
            }
        });
        daysPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateTotalAmount();
            }
        });

        // Date picker logic
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(FormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Create a calendar for the selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);

                        // Check if the selected date is less than the current date
                        if (selectedDate.before(calendar)) {
                            Toast.makeText(FormActivity.this, "Selected date is in the past!", Toast.LENGTH_SHORT).show();
                        } else {
                            eventDate.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
                        }
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });


        // Pay button logic
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPackageId = packageRadioGroup.getCheckedRadioButtonId();

                // Check if a package is selected
                if (selectedPackageId == -1) {
                    Toast.makeText(FormActivity.this, "Please select a package", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedPackage = findViewById(selectedPackageId);
                int numberOfDays = daysPicker.getValue();
                String date = eventDate.getText().toString();
                String guestCountText = guestCount.getText().toString();  // Get the guest count

                // Check if all fields are filled
                if (date.isEmpty() || guestCountText.isEmpty()) {
                    Toast.makeText(FormActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int guestCount = Integer.parseInt(guestCountText);  // Convert guest count to integer






                // Create data object for Firebase
                com.example.VenueVerse.Bdata bdata = new com.example.VenueVerse.Bdata(username, venuename, eventDate.getText().toString(), numberOfDays, (int) totalAmount);

                // Push data to Firebase
                reference.push().setValue(bdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FormActivity.this, "Thank you for choosing VenueVerse", Toast.LENGTH_SHORT).show();

                            // Navigate to ConfirmationActivity
                            Intent intent = new Intent(FormActivity.this, com.example.venueverse.ConfirmationActivity.class);
                            intent.putExtra("username", username);

                            startActivity(intent);
                        } else {
                            Toast.makeText(FormActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void updateTotalAmount() {
        int selectedPackageId = packageRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedPackage = findViewById(selectedPackageId);
        int numberOfDays = daysPicker.getValue();

        if (selectedPackage == null) {
            payButton.setText("PAY ₹" + (numberOfDays * price));
            return;
        }
        if (selectedPackage.getText().equals("AC")) {

            totalAmount = numberOfDays * price*1.5;
        } else {
            totalAmount = numberOfDays * price;
        }

        payButton.setText("PAY ₹" + totalAmount);
    }
}
