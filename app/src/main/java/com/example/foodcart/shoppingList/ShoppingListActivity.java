package com.example.foodcart.shoppingList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.CustomIngredientArrayAdapter;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.ingredients.IngredientFragment;
import com.example.foodcart.mealplans.MealPlanActivity;
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

public class ShoppingListActivity extends AppCompatActivity
        implements ShoppingItemFragment.OnFragmentInteractionListener {

        // Declare the variables
        private ListView shoppingList;
        private ArrayAdapter<ShoppingItem> shoppingAdapter;
        private ArrayList<ShoppingItem> dataList;
        private int selected;
        private FirebaseFirestore db;
        private String[] sortValues = { "description", "category" };

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shopping_list);

            // initialize lists
            shoppingList = findViewById(R.id.shopping_list);
            dataList = new ArrayList<>();

            // set adapter
            shoppingAdapter = new CustomShoppingItemArrayAdapter(this, dataList);
            shoppingList.setAdapter(shoppingAdapter);

            // Access a Cloud Firestore instance from your Activity
            db = FirebaseFirestore.getInstance();
            // Get a top level reference to the collection
            final CollectionReference ShoppingListCollection = db.collection("Shopping List");

            // set spinner adapter for drop down sort list
            Spinner sortDropDown = (Spinner) findViewById(R.id.shopping_list_sort_select);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortDropDown.setAdapter(adapter);
            sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // sort the db by the sortValue
                    String sortValue = sortValues[position];
                    ShoppingListCollection.orderBy(sortValue);
                    Collections.sort(dataList, new Comparator<Ingredient>(){
                        public int compare(Ingredient ing1, Ingredient ing2)
                        {
                            switch(sortValue) {
                                case "description":
                                    return ing1.getDescription().compareTo(ing2.getDescription());
                                case "category":
                                    return ing1.getCategory().compareTo(ing2.getCategory());
                            }
                            return 0;
                        }
                    });
                    shoppingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final ImageButton IngredientTab = findViewById(R.id.ingredients_tab);
            IngredientTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(),
                            IngredientActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

            final ImageButton RecipeTab = findViewById(R.id.recipes_tab);
            RecipeTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(),
                            RecipeActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

            final ImageButton MealPlanTab = findViewById(R.id.mealplans_tab);
            MealPlanTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(),
                            MealPlanActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

            // onClick for Add Food Button (floating action + button)
            final FloatingActionButton addItemButton = findViewById(R.id.add_item_button);
            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ShoppingItemFragment().show(getSupportFragmentManager(), "ADD_SHOPPING_ITEM");
                }
            });

            // onClick for selecting items from list. When item is selected, Edit Food pops up
            shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    ShoppingItem selectedItem = dataList.get(position);
                    selected = position;
                    new ShoppingItemFragment().newInstance(selectedItem).show(getSupportFragmentManager(), "EDIT_SHOPPING_ITEM");
                }
            });

            ShoppingListCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                        FirebaseFirestoreException error) {
                    dataList.clear();
                    assert queryDocumentSnapshots != null;
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                        Log.d("Update Shopping List", String.valueOf(doc.getData().get("Description")));
                        String description = doc.getId();
                        String count = (String) doc.getData().get("Count");
                        String unit = (String) doc.getData().get("Unit");
                        String category = (String) doc.getData().get("Category");

                        // no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                        int countInt = Integer.parseInt(count);
                        // add ingredient to list
                        dataList.add(new ShoppingItem(description, countInt, unit, category));
                    }
                    shoppingAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
                }
            });

        }

//        @Override
        public void onOkPressed(ShoppingItem newItem) {
            shoppingAdapter.add(newItem);
        }

//        @Override
        public void onOkPressedEdit(ShoppingItem item) {
            dataList.set(selected, item);
            shoppingAdapter.notifyDataSetChanged();
        }

    }
