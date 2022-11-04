package com.example.foodcart.recipes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;
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

    public CustomRecipeArrayAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
        this.recipes = recipes;
        this.context = context;
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

        TextView ingredientDescription = view.findViewById(R.id.recipe_name);
        TextView ingredientSort = view.findViewById(R.id.recipe_item_sort);

        ingredientDescription.setText(recipe.getTitle());
        ingredientSort.setText("Sort value");


        // set up delete button on each list item and onClick
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.recipe_item_deleteButton);
        deleteButton.setFocusable(false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipes.size() > 0) {
                    // find selection
                    recipes.remove(Math.min(position, recipes.size() - 1));
                    // Access a Cloud Firestore instance from your Activity
                    db = FirebaseFirestore.getInstance();
                    // Get a top level reference to the collection
                    final CollectionReference recipeCollection = db.collection("Recipes");
                    recipeCollection
                            .document(recipes.get(position).getTitle())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d("Delete Recipe", "Data has been deleted successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d("Delete Recipe", "Data could not be deleted!" + e.toString());
                                }
                            });
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}

