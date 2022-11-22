package com.example.foodcart.recipes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Custom array adapter for Recipe class
 */
public class CustomRecipeArrayAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context context;
    private FirebaseFirestore db;
    private boolean sort = true;

    public CustomRecipeArrayAdapter(Context context, ArrayList<Recipe> recipes, boolean sort) {
        super(context, 0, recipes);
        this.recipes = recipes;
        this.context = context;
        this.sort = sort;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_recipes_item, parent, false);
        }

        Recipe recipe = recipes.get(position);

        TextView recipeDescription = view.findViewById(R.id.recipe_name);
        TextView recipeSort = view.findViewById(R.id.recipe_item_sort);

        recipeDescription.setText(recipe.getTitle());
        View parentView = (View) parent.getParent();
        Spinner sortDropDown = parentView.findViewById(R.id.recipes_sort_select);

        if (sortDropDown.getSelectedItem() != null) {
            String sortValue = sortDropDown.getSelectedItem().toString();
            System.out.println(sortValue);
            switch (sortValue){
                case "title":
                    recipeSort.setText("");
                    break;
                case "prep time":
                    recipeSort.setText(String.valueOf(recipe.getPrep_time()));
                    break;
                case "# of servings":
                    recipeSort.setText(String.valueOf(recipe.getServings()));
                    break;
                case "category":
                    recipeSort.setText(recipe.getCategory());
                    break;
            }
        } else {
            recipeSort.setText("");
        }
        notifyDataSetChanged();
        // set up delete button on each list item and onClick
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.recipe_item_deleteButton);
        deleteButton.setFocusable(false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipes.size() > 0) {
                    // Access a Cloud Firestore instance from your Activity
                    db = FirebaseFirestore.getInstance();
                    // Get a top level reference to the collection
                    final CollectionReference recipeCollection = db.collection("Recipes");
                    // Delete recipe from database
                    RecipeFragment.deleteRecipeDB(recipes.get(position), recipeCollection);
                    // find selection
                    recipes.remove(Math.min(position, recipes.size() - 1));
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}

