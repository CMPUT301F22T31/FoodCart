package com.example.foodcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CustomIngredientArrayAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public CustomIngredientArrayAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredients_item, parent, false);
        }

        com.example.foodcart.Ingredient ingredient = ingredients.get(position);

        TextView ingredientDescription = view.findViewById(R.id.ingredient_item_name);
        TextView ingredientQuantity = view.findViewById(R.id.ingredient_item_quantity);
        TextView ingredientSort = view.findViewById(R.id.ingredient_item_sort);

        ingredientDescription.setText(ingredient.getDescription());
        ingredientQuantity.setText(ingredient.getCount().toString());
        ingredientSort.setText("Sort value");


        // set up delete button on each list item and onClick
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.ingredient_item_deleteButton);
        deleteButton.setFocusable(false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredients.size() > 0) {
                    // find selection
                    ingredients.remove(Math.min(position, ingredients.size() - 1));
                    notifyDataSetChanged();

                }
            }
        });

        return view;
    }


}
