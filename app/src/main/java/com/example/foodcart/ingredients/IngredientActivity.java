package com.example.foodcart.ingredients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcart.R;
import com.example.foodcart.recipes.RecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class IngredientActivity extends AppCompatActivity implements IngredientFragment.OnFragmentInteractionListener {

    // Declare the variables
    ListView ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayList<Ingredient> dataList;
    int selected;
    FirebaseFirestore db;
    String[] sortValues = { "description", "best before date", "location", "category" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        // initialize lists
        ingredientList = findViewById(R.id.ingredients_list);
        dataList = new ArrayList<>();

        // set adapter
        ingredientAdapter = new CustomIngredientArrayAdapter(this, dataList);
        ingredientList.setAdapter(ingredientAdapter);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference IngredientCollection = db.collection("Ingredients");

        // set spinner adapter for drop down sort list
        Spinner sortDropDown = (Spinner) findViewById(R.id.ingredients_sort_select);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortDropDown.setAdapter(adapter);
        sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // sort the db by the sortValue
                String sortValue = sortValues[position];
                IngredientCollection.orderBy(sortValue);
                Collections.sort(dataList, new Comparator<Ingredient>(){
                    public int compare(Ingredient ing1, Ingredient ing2)
                    {
                        switch(sortValue) {
                            case "description":
                                return ing1.getDescription().compareTo(ing2.getDescription());
                            case "best before date":
                                return ing1.getBestBeforeDate().compareTo(ing2.getBestBeforeDate());
                            case "location":
                                return ing1.getLocation().compareTo(ing2.getLocation());
                            case "category":
                                return ing1.getCategory().compareTo(ing2.getCategory());
                        }
                        return 0;
                    }
                });
                ingredientAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ImageButton RecipeTab = findViewById(R.id.recipes_tab);
        RecipeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), RecipeActivity.class);
                startActivity(switchActivityIntent);
                finish();
            }
        });

        // onClick for Add Food Button (floating action + button)
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        // onClick for selecting items from list. When item is selected, Edit Food pops up
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Ingredient selectedIngredient = dataList.get(position);
                selected = position;
                new IngredientFragment().newInstance(selectedIngredient).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
        }
        });

        IngredientCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                dataList.clear();
                assert queryDocumentSnapshots != null;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d("Update Ingredient", String.valueOf(doc.getData().get("Description")));
                    String description = doc.getId();
                    String location = (String) doc.getData().get("Location");
                    String tempDate = (String) doc.getData().get("Date");
                    String count = (String) doc.getData().get("Count");
                    String unit = (String) doc.getData().get("Unit");
                    String category = (String) doc.getData().get("Category");

                    // Convert date string into Date class
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-mm-dd").parse(tempDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                    int countInt = Integer.parseInt(count);
                    // add ingredient to list
                    dataList.add(new Ingredient(description, date, location, countInt, unit, category));
                }
                ingredientAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
            }
        });

    }

    @Override
    public void onOkPressed(Ingredient newIngredient) {
        ingredientAdapter.add(newIngredient);
    }

    @Override
    public void onOkPressedEdit(Ingredient ingredient) {
        dataList.set(selected, ingredient);
        ingredientAdapter.notifyDataSetChanged();
    }

}

