package com.example.foodcart.mealplans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.ingredients.IngredientFragment;
import com.example.foodcart.recipes.RecipeActivity;
import com.example.foodcart.recipes.RecipeFragment;
import com.example.foodcart.shoppingList.ShoppingListActivity;
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

public class MealPlanActivity extends AppCompatActivity
        implements MealPlanFragment.OnFragmentInteractionListener {

    FloatingActionButton addMealPlan, addmIngredientButton, addmRecipeButton;

    TextView addmIngredientText, addmRecipeText;

    boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealplan);

        addMealPlan = findViewById(R.id.add_mealplan_button);
        addmIngredientButton = findViewById(R.id.add_mealplan_ingredient_button);
        addmRecipeButton = findViewById(R.id.add_mealplan_recipe_button);

        addmIngredientText = findViewById(R.id.add_mealplan_ingredient_text);
        addmRecipeText = findViewById(R.id.add_mealplan_recipe_text);

        addmIngredientButton.hide();
        addmRecipeButton.hide();
        addmIngredientText.setVisibility(View.GONE);
        addmRecipeText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        addMealPlan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {
                            addmIngredientButton.show();
                            addmRecipeButton.show();
                            addmIngredientText.setVisibility(View.VISIBLE);
                            addmRecipeText.setVisibility(View.VISIBLE);

                            isAllFabsVisible = true;
                        } else {
                            addmIngredientButton.hide();
                            addmRecipeButton.hide();
                            addmIngredientText.setVisibility(View.GONE);
                            addmRecipeText.setVisibility(View.GONE);

                            isAllFabsVisible = false;
                        }
                    }
                });

        addmIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanFragment().newInstance("Ingredients").show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        addmRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanFragment().newInstance("Recipes").show(getSupportFragmentManager(), "ADD_RECIPE");
            }
        });

        // Switch to Recipe Activity
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
    }
    @Override
    public void onOkPressed(Ingredient newIngredient) {
    }

    @Override
    public void onOkPressedEdit(Ingredient ingredient) {
    }
}