package com.example.venueverse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference reference;
    EditText eventDate;
    NumberPicker daysPicker;
    RadioGroup packageRadioGroup;
    Button payButton, datePickerButton;
    String venuename;
    double price, totalAmount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));

        // Initialize views
        eventDate = findViewById(R.id.event_date);
        packageRadioGroup = findViewById(R.id.package_radio_group);
        payButton = findViewById(R.id.pay_button);
        datePickerButton = findViewById(R.id.btn_date_picker);
        daysPicker = findViewById(R.id.days_picker);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Bdata");

        // Get venue name and price from intent
        Intent intent = getIntent();
        venuename = intent.getStringExtra("venuename");
        price = intent.getDoubleExtra("price", 50000);

        // Configure NumberPicker for number of days
        daysPicker.setMinValue(1);
        daysPicker.setMaxValue(20);
        daysPicker.setValue(1);

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
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);

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

                if (selectedPackageId == -1) {
                    Toast.makeText(FormActivity.this, "Please select a package", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedPackage = findViewById(selectedPackageId);
                int numberOfDays = daysPicker.getValue();
                String date = eventDate.getText().toString();

                if (date.isEmpty()) {
                    Toast.makeText(FormActivity.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                // Create data object for Firebase
                Bdata bdata = new Bdata(currentUser.getEmail(), venuename, date, numberOfDays, totalAmount);

                // Push data to Firebase
                reference.push().setValue(bdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent cintent = new Intent(FormActivity.this, ConfirmationActivity.class);
                            Toast.makeText(FormActivity.this, "Thank you for choosing VenueVerse", Toast.LENGTH_SHORT).show();
                            startActivity(cintent);
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
            totalAmount = numberOfDays * price;
        } else if (selectedPackage.getText().equals("AC")) {
            totalAmount = numberOfDays * price * 1.5;
        } else {
            totalAmount = numberOfDays * price;
        }

        payButton.setText("PAY ₹" + totalAmount);
    }
}
