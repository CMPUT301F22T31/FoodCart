package com.example.foodcart.ingredients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.foodcart.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Launches the Calender app activity to allow user to select a date and returns the date to the
 * activity that started CalenderActivity. It is called via ActivityResultLauncher in both Ingredient
 * fragment and MealPlanFragment to get the dates for ingredient.bestBeforeDate and meals.
 *
 * @author Arsh
 * @version 1.0
 * @see IngredientFragment
 * @see com.example.foodcart.mealplans.MealPlanFragment
 */
public class CalendarActivity extends AppCompatActivity {
    String date = null;

    /**
     * The first function that is called when created that initializes and sets the UI elements and
     * watches for changes by the user in the selected date and save button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar = findViewById(R.id.calendarView);

        //by default it is the current date
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        date = formatter.format(currentDate);

        // Save Calender date
        final FloatingActionButton saveButton = findViewById(R.id.floatingActionButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });

        // Change date saved from newly selected
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = i + "-" + (i1 + 1) + "-" + i2;
            }
        });
    }

    /**
     * Sends date back to starting activity in an intent and sets result flag to RESULT_OK
     */
    void done() {
        Intent intent = new Intent();
        intent.putExtra("Date", date);
        setResult(RESULT_OK, intent);
        finish();
    }
}