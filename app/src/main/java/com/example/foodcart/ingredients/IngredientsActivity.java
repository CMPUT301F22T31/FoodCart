package com.example.foodcart.ingredients;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.AddIngredientFragment;
import com.example.foodcart.ingredients.CustomIngredientArrayAdapter;
import com.example.foodcart.ingredients.EditIngredientFragment;
import com.example.foodcart.ingredients.Ingredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class IngredientsActivity extends AppCompatActivity implements AddIngredientFragment.OnFragmentInteractionListener, EditIngredientFragment.OnFragmentInteractionListener {

    // Declare the variables
    ListView ingredientList;
    ArrayAdapter<Ingredient> ingredientAdapter;
    ArrayList<Ingredient> dataList;
    int selected;
    TextView totalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        // initialize lists
        ingredientList = findViewById(R.id.ingredients_list);
        dataList = new ArrayList<>();
        try {
            dataList.add(new Ingredient("Banana", new Date(10-10-2022), "pantry", 3, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set adapter
        ingredientAdapter = new CustomIngredientArrayAdapter(this, dataList);
        ingredientList.setAdapter(ingredientAdapter);

        // onClick for Add Food Button (floating action + button)
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddIngredientFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        // onClick for selecting items from list. When item is selected, Edit Food pops up
        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                selected = position;
                new EditIngredientFragment().show(getSupportFragmentManager(), "EDIT_INGREDIENT");

            }
        });

        // totalCost = findViewById(R.id.totalCost);
        Integer sum = 0;
        for (Ingredient item: dataList){
            sum += item.getTotal();
        }
        // totalCost.setText("Total Cost: $" + sum.toString() + " USD");


    }

    // for Add Food Fragment submit
    @Override
    public void onOkPressed(Ingredient ingredient) {
        ingredientAdapter.add(ingredient);
        // totalCost = findViewById(R.id.totalCost);
        Integer sum = 0;
        for (Ingredient item: dataList){
            sum += item.getTotal();
        }
        // totalCost.setText("Total Cost: $" + sum.toString() + " USD");
    }

    // get the current food for Edit Food Fragment
    @Override
    public Ingredient getCurrentIngredient() {
        return dataList.get(selected);
    }

    // for Edit Food Fragment submit
    public void onOkEditPressed(Ingredient ingredient) {
        dataList.set(selected, ingredient);
        ingredientAdapter.notifyDataSetChanged();
        // totalCost = findViewById(R.id.totalCost); TODO
        Integer sum = 0;
        for (Ingredient item: dataList){
            sum += item.getTotal();
        }
        // totalCost.setText("Total Cost: $" + sum.toString() + " USD");
    }
}

