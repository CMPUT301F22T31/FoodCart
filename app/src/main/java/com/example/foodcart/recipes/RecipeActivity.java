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
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.ingredients.IngredientFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener{

    ListView recipeListView;
    ArrayAdapter<Recipe> recipeAdapter;
    ArrayList<Recipe> recipeList;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // set adapter
        recipeAdapter = new CustomRecipeArrayAdapter(this, recipeList);
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
        final ImageButton RecipeTab = findViewById(R.id.ingredients_tab);
        RecipeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), IngredientActivity.class);
                startActivity(switchActivityIntent);
                finish();
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