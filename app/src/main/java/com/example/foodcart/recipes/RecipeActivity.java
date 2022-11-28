package com.example.foodcart.recipes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;


import com.example.foodcart.mealplans.MealPlanActivity;
import com.example.foodcart.shoppingList.ShoppingListActivity;
//import com.example.foodcart.mealplans.MealPlanActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import com.example.foodcart.ingredients.IngredientActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * recipe activity responsible for all recipe functionality. It sets the UI, calls RecipeFragment
 * to allow user input, and facilitates navigation between activities
 *
 * author Arsh, Ahmed, Ashley, Alfred
 * @version 3.0
 * @see RecipeFragment              allows user to add/edit recipes
 * @see CustomRecipeArrayAdapter    sorts recipes and allows ListView to display recipes
 */
public class RecipeActivity extends AppCompatActivity
        implements RecipeFragment.OnFragmentInteractionListener{

    ListView recipeListView;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> recipeList;
    int selected;
    private final String[] sortValues = { "title", "prep time", "# of servings", "category" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            FirebaseFirestore db;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recipe);

            // initialize lists
            recipeListView = findViewById(R.id.recipes_list);
            recipeList = new ArrayList<>();

            // Access a Cloud Firestore instance from your Activity
            db = FirebaseFirestore.getInstance();
            // Get a top level reference to the collection
            final CollectionReference recipeCollection = db.collection("Recipes");

            // set spinner adapter for drop down sort list
            Spinner sortDropDown = findViewById(R.id.recipes_sort_select);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sortDropDown.setAdapter(adapter);
            sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // sort the db by the sortValue
                    String sortValue = sortValues[position];
                    recipeCollection.orderBy(sortValue);
                    Collections.sort(recipeList, new Comparator<Recipe>(){
                        public int compare(Recipe rec1, Recipe rec2)
                        {
                            switch(sortValue) {
                                case "title":
                                    return rec1.getTitle().compareTo(rec2.getTitle());
                                case "prep time":
                                    return rec1.getPrep_time() - rec2.getPrep_time();
                                case "# of servings":
                                    return rec1.getServings() - rec2.getServings();
                                case "category":
                                    return rec1.getCategory().compareTo(rec2.getCategory());
                            }
                            return 0;
                        }
                    });
                    recipeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // set adapter
            recipeAdapter = new CustomRecipeArrayAdapter(this, recipeList, true);
            recipeListView.setAdapter(recipeAdapter);

            // onClick for Add Food Button (floating action + button)
            final FloatingActionButton addRecipeButton = findViewById(R.id.add_recipe_button);
            addRecipeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RecipeFragment().show(getSupportFragmentManager(), "ADD_RECIPE");
                }
            });

            // onClick for selecting items from list. When item is selected, Edit Food pops up
            recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Recipe selectedRecipe = recipeList.get(position);
                    selected = position;
                    new RecipeFragment().newInstance(selectedRecipe).show(getSupportFragmentManager(), "EDIT_RECIPE");
                }
            });

            // Switch to Ingredient Activity
            final ImageButton IngredientTab = findViewById(R.id.ingredients_tab);
            IngredientTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(), IngredientActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

            // Switch to mealplan activity
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

            // Switch to shoppinglist activity
            final ImageButton ShoppingListTab = findViewById(R.id.shoppinglist_tab);
            ShoppingListTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(),
                            ShoppingListActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

        // Notify Adapter whenever the database updates
            recipeCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                        FirebaseFirestoreException error) {
                    recipeList.clear();
                    ArrayList<Ingredient> ingredientList = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                        Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                        String title = doc.getId();
                        String prep_time = (String) doc.getData().get("Prep Time");
                        String servings = (String) doc.getData().get("Servings");
                        String comments = (String) doc.getData().get("Comments");
                        String category = (String) doc.getData().get("Category");
                        String picture =  (String) doc.getData().get("Picture");

                        // Get the ingredients for the recipe
                        CollectionReference ingredients = db.collection("Recipes")
                                                            .document(title).collection("Ingredients");
                        ingredients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for(QueryDocumentSnapshot ing : task.getResult()) {
                                        String description = ing.getId();
                                        String count = (String) ing.getData().get("Count");
                                        String unit = (String) ing.getData().get("Unit");
                                        String category = (String) ing.getData().get("Category");
                                        // Convert date string into Date class
                                        int countInt = Integer.parseInt(count);
                                        // add ingredient to list
                                        ingredientList.add(new Ingredient(description, countInt, unit, category));
                                    }
                                } else {
                                    Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                                }
                            }
                        });


                        //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                        int servInt = Integer.parseInt(servings);
                        int prepInt = Integer.parseInt(prep_time);

                        // add recipe to list
                        Recipe recipe = null;
                        try {
                            recipe = new Recipe(title, prepInt, servInt, comments, picture, category, ingredientList);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        recipeList.add(recipe);
                    }
                    recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
                }
            });
    }

    @Override
    public void onOkPressedRecipe(Recipe newRecipe) {
        recipeAdapter.add(newRecipe);
    }

    @Override
    public void onOkPressedEditRecipe(Recipe newRecipe) {
        recipeList.set(selected, newRecipe);
        recipeAdapter.notifyDataSetChanged();
    }
}