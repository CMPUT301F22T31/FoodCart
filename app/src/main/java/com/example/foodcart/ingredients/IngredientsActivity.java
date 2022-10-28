package com.example.foodcart.ingredients;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcart.R;
import com.example.foodcart.recipes.RecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class IngredientsActivity extends AppCompatActivity implements IngredientFragment.OnFragmentInteractionListener {

    // Declare the variables
    ListView ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayList<Ingredient> dataList;
    int selected;

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

        final ImageButton RecipeTab = findViewById(R.id.recipes_tab);
        RecipeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), RecipeActivity.class);
                startActivity(switchActivityIntent);
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

