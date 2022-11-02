package com.example.foodcart.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class RecipeActivity extends AppCompatActivity {

    ListView recipeListView;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // set adapter
        recipeAdapter = new CustomRecipeArrayAdapter(this, recipeList);
        recipeListView.setAdapter(recipeAdapter);

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
    }
}