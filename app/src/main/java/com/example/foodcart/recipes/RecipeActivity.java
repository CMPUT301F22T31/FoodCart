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

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;


import com.example.foodcart.mealplans.MealPlanActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.ingredients.IngredientFragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecipeActivity extends AppCompatActivity
        implements RecipeFragment.OnFragmentInteractionListener{

    ListView recipeListView;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> recipeList;
    int selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // initialize lists
        recipeListView = findViewById(R.id.recipes_list);
        recipeList = new ArrayList<>();
        try {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference recipeCollection = db.collection("Recipes");


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

                    CollectionReference ingredients = db.collection("Recipes")
                                                        .document(title).collection("Ingredients");
                    ingredients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot ing : task.getResult()) {
                                    String description = ing.getId();
                                    String location = (String) ing.getData().get("Location");
                                    String tempDate = (String) ing.getData().get("Date");
                                    String count = (String) ing.getData().get("Count");
                                    String unit = (String) ing.getData().get("Unit");
                                    String category = (String) ing.getData().get("Category");
                                    // Convert date string into Date class
                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("yyyy-mm-dd").parse(tempDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    int countInt = Integer.parseInt(count);
                                    // add ingredient to list
                                    ingredientList.add(new Ingredient(description, date, location, countInt, unit, category));
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
                        recipe = new Recipe(title, prepInt, servInt, picture,
                                            comments, category, ingredientList);

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