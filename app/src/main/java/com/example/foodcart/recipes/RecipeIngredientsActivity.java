package com.example.foodcart.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.CustomIngredientArrayAdapter;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecipeIngredientsActivity extends AppCompatActivity
    implements IngredientFragment.OnFragmentInteractionListener{

    // Declare the variables
    private ListView ingredientList;
    private ArrayAdapter<Ingredient> ingredientAdapter;
    public ArrayList<Ingredient> dataList;
    private int selected;

    @Override
    protected void onResume() {
        super.onResume();
        dataList = (ArrayList<Ingredient>) getIntent().getSerializableExtra("IngredientList");
        // set adapter
        ingredientAdapter = new CustomIngredientArrayAdapter(this, dataList, false);
        ingredientList.setAdapter(ingredientAdapter);
        // onClick for Add Food Button (floating action + button)
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IngredientFragment().newInstance(null, "addFromRecipe").show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        final Button activityComplete = findViewById(R.id.ingredientsDone);
        activityComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Done();
            }
        });

        // onClick for selecting items from list. When item is selected, Edit Food pops up
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Ingredient selectedIngredient = dataList.get(position);
                selected = position;
                new IngredientFragment().newInstance(selectedIngredient, "editFromRecipe").show(getSupportFragmentManager(), "EDIT_INGREDIENT");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);

        // initialize lists
        ingredientList = findViewById(R.id.ingredients_list);
    }

    @Override
    public void onOkPressed(Ingredient newIngredient) {
        dataList.add(newIngredient);
        ingredientAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOkPressedEdit(Ingredient ingredient) {
        dataList.set(selected, ingredient);
        ingredientAdapter.notifyDataSetChanged();
    }
    public void Done() {
        Intent intent = new Intent();
        intent.putExtra("EditedList", dataList);
        setResult(RESULT_OK, intent);
        finish();
    }
}