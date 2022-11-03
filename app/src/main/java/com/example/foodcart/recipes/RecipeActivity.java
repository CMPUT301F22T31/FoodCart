package com.example.foodcart.recipes;

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

import com.example.foodcart.R;
//import com.example.foodcart.ingredients.AddIngredientFragment;
import com.example.foodcart.ingredients.CustomIngredientArrayAdapter;
//import com.example.foodcart.ingredients.EditIngredientFragment;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecipeActivity extends AppCompatActivity {

    ListView recipeListView;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> recipeList;


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
            ingredients.add(new Ingredient("Marshmello", new Date(10-10-2022),
                    "pantry", 1, "g", "Treat"));
            ingredients.add(new Ingredient("Cracker", new Date(10-10-2022),
                    "pantry", 1,"g", "Treat"));
            recipeList.add(new Recipe("Smore", 5,1,
                    "Marshmello best mello", "Dessert", ingredients));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        recipeListView.setAdapter(recipeAdapter);
        // set adapter
        recipeAdapter = new CustomRecipeArrayAdapter(this, recipeList);


        final CollectionReference recipeCollection = db.collection("Recipes");

        // onClick for Add Food Button (floating action + button)
        final FloatingActionButton addRecipeButton = findViewById(R.id.add_recipe_button);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new AddRecipeFragment().show(getSupportFragmentManager(), "ADD_RECIPE");
            }
        });

        // onClick for selecting items from list. When item is selected, Edit Food pops up
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int selected = position;
                //new EditRecipeFragment().show(getSupportFragmentManager(), "EDIT_RECIPE");
            }
        });

        // Switch to Ingredient Activity
        final ImageButton RecipeTab = findViewById(R.id.ingredients_tab);
        RecipeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), IngredientsActivity.class);
                startActivity(switchActivityIntent);
                finish();
            }
        });

        recipeCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                //recipeList.clear();
                ArrayList<Ingredient> ingredientList = new ArrayList<>();
                assert queryDocumentSnapshots != null;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                    String title = doc.getId();
                    String prep_time = (String) doc.getData().get("Prep Time");
                    String servings = (String) doc.getData().get("Servings");
                    String comments = (String) doc.getData().get("Comments");
                    String category = (String) doc.getData().get("Category");
                    String picture = (String) doc.getData().get("Picture");
                    DocumentReference ingredients = db.collection("Recipe").document(title);
                    ingredients.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot ing) {
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
                    });
                    //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                    int servInt = Integer.parseInt(servings);
                    int prepInt = Integer.parseInt(prep_time);

                    // add recipe to list
                    Recipe recipe = null;
                    try {
                        recipe = new Recipe(title, prepInt, servInt, comments, category, ingredientList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recipeList.add(recipe);
                }
                recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
            }
        });
    }
}