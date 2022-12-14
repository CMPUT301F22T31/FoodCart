package com.example.foodcart.shoppingList;

import androidx.annotation.NonNull;
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
import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.mealplans.MealPlanActivity;
import com.example.foodcart.recipes.Recipe;
import com.example.foodcart.recipes.RecipeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Main activity for all shopping list functionality. Sets UI elements, starts fragments to get user
 * input
 *
 * @author Ahmed, Ashley, Alfred
 * @version 1.0
 * @see ShoppingListActivity
 */

public class ShoppingListActivity extends AppCompatActivity
        implements ShoppingItemFragment.OnFragmentInteractionListener {

        // Declare the variables
        private ListView shoppingList;
        private ArrayAdapter<ShoppingItem> shoppingAdapter;
        private ArrayList<ShoppingItem> dataList;
        private ArrayList<ShoppingItem> tempList;
        private int selected;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        private String[] sortValues = { "description", "category" };

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shopping_list);

            // initialize lists
            shoppingList = findViewById(R.id.shopping_list);
            dataList = new ArrayList<>();
            tempList = new ArrayList<>();

            // set adapter
            shoppingAdapter = new CustomShoppingItemArrayAdapter(this, dataList);
            shoppingList.setAdapter(shoppingAdapter);

            // set spinner adapter for drop down sort list
            Spinner sortDropDown = findViewById(R.id.shopping_list_sort_select);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortDropDown.setAdapter(adapter);
            sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // sort the db by the sortValue
                    String sortValue = sortValues[position];
                    Collections.sort(dataList, new Comparator<ShoppingItem>(){
                        public int compare(ShoppingItem s1, ShoppingItem s2)
                        {
                            switch(sortValue) {
                                case "description":
                                    return s1.getDescription().compareTo(s2.getDescription());
                                case "category":
                                    return s1.getCategory().compareTo(s2.getCategory());
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

            // Change Activity to Ingredients
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

            // Change Activity to Recipe
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

            // Change Activity to MealPlan
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
                    // If item is checked for being picked up then allow user to add to storage
                    for(ShoppingItem i : dataList){
                        if(i.isChecked()) {
                            new ShoppingItemFragment().newInstance(i).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
                        }
                    }
                }
            });

            db = FirebaseFirestore.getInstance();
            // Get a top level reference to the collection
            final CollectionReference MealPlanCollection = db.collection("MealPlan");
            // Get all the mealplan items
            MealPlanCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        tempList.clear();
                        assert task.getResult() != null;
                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            Log.d("Update ShoppingList", doc.getId());
                            String title = doc.getId();

                            // Depending on type do certain things
                            if(doc.getData().get("Type").equals("Recipe")) {
                                String scale = (String) doc.getData().get("Scale");
                                CollectionReference ingredients = db.collection("MealPlan")
                                        .document(title).collection("Ingredients");
                                // get all ingredients from recipe
                                ingredients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot ing : task.getResult()) {
                                                // Get Values
                                                String description = ing.getId();
                                                String count = (String) ing.getData().get("Count");
                                                String unit = (String) ing.getData().get("Unit");
                                                String category = (String) ing.getData().get("Category");
                                                // Convert date string into Date class
                                                int countInt = Integer.parseInt(count);
                                                int scaleInt = Integer.parseInt(scale);
                                                // add ingredient to list
                                                ShoppingItem item_temp = new ShoppingItem(description, countInt*scaleInt,0, unit, category);
                                                // Add the item
                                                additemCheck(item_temp);
                                            }
                                        } else {
                                            Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                                        }
                                    }
                                });
                            } else {
                                // Get Values
                                String description = (String) doc.getData().get("MealName");
                                String count = (String) doc.getData().get("Count");
                                String unit = (String) doc.getData().get("Unit");
                                String category = (String) doc.getData().get("Category");
                                // Convert date string into Date class
                                int countInt = Integer.parseInt(count);
                                // add ingredient to list
                                ShoppingItem item_temp = new ShoppingItem(description, countInt,0, unit, category);

                                // Add the item
                                additemCheck(item_temp);
                            }
                        }// Notifying the adapter to render any new data fetches from the cloud
                    }
                }
            });
        }

    /**
     * called when fragment exits with user clicking confirm/edit, sets new count value, and if new
     * count is not integer then remove shopping item from list
     *
     * @param item      new item that was edited
     */
        public void onOkPressedEdit(ShoppingItem item) {
            for(ShoppingItem i:dataList){
                if(i.getDescription().equals(item.getDescription())){
                    i.setOldcount(item.getCount());
                    if((i.getCount()-i.getOldcount()) <=0){
                        dataList.remove(i);
                    }
                    break;
                }
            }
            shoppingAdapter.notifyDataSetChanged();
        }

    /**
     * add shoppingitem to shoppinglist if difference between new item's and old item's count is greater
     * than zero.
     *
     * @param item  shopping item to check and add
     */
        public void additem(ShoppingItem item){
            // if item already in list then add to the count
            if(tempList.contains(item)){
                for(ShoppingItem i:tempList){
                    if(i.getDescription().equals(item.getDescription())){
                        i.setCount(i.getCount()+item.getCount());
                        break;
                    }
                }
            } else {
                tempList.add(item);
            }
            dataList.clear();
            // If negative don't show item
            for(ShoppingItem i :tempList){
                if(i.getCount()-i.getOldcount() >0){
                    dataList.add(i);
                }
            }
            shoppingAdapter.notifyDataSetChanged();
        }

    /**
     * add shoppingitem to shopppinglist if new item's count doesn't match the corresponding ingredient
     * in Ingredients storage's count.
     *
     * @param item  shopping item to check and add
     */
        public void additemCheck(ShoppingItem item) {
            DocumentReference docIdRef = db.collection("Ingredients").document(item.getDescription());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        // If item already in list then no need for adjustment
                        if(tempList.contains(item)){
                            additem(item);
                            return;
                        }
                        // If it exists in ingredient storage
                        if (document.exists()) {
                            // Set the old count so we know how much we already have
                            String ing_count = (String)document.getData().get("Count");
                            item.setCount(item.getCount());
                            item.setOldcount(Integer.parseInt(ing_count));
                            additem(item);
                            Log.d("Adjust Item exists", item.getDescription());
                        } else {
                            additem(item);
                        }
                    } else {
                        Log.d("Adjust Item", "Failed with: ", task.getException());
                    }
                }
        });
    }
}
