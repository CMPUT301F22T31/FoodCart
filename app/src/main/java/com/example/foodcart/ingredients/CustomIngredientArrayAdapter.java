package com.example.foodcart.ingredients;

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

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * The custom array adapter for ingredients. Also includes delete
 * and sort functionality for items in the ingredients list
 */
public class CustomIngredientArrayAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    /**
     * Constructor for custom array of ingredients adapter
     * @param context
     * @param ingredients
     */
    public CustomIngredientArrayAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    /**
     * Get the view of the ingredients list and provides delete
     * and sort functionality for the ingredients in the list
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredients_item, parent, false);
        }

        com.example.foodcart.ingredients.Ingredient ingredient = ingredients.get(position);

        // Get the views of the TextViews
        TextView ingredientDescription = view.findViewById(R.id.ingredient_item_name);
        TextView ingredientQuantity = view.findViewById(R.id.ingredient_item_quantity);
        TextView ingredientSort = view.findViewById(R.id.ingredient_item_sort);
        View parentView = (View) parent.getParent();
        Spinner sortDropDown = parentView.findViewById(R.id.ingredients_sort_select);

        ingredientDescription.setText(ingredient.getDescription());
        ingredientQuantity.setText(ingredient.getCount().toString());

        if (sortDropDown.getSelectedItem() != null) {
            String sortValue = sortDropDown.getSelectedItem().toString();
            System.out.println(sortValue);
            switch (sortValue){
                case "description":
                    System.out.println(ingredientSort.getText());
                    ingredientSort.setText("");
                case "best before date":
                    ingredientSort.setText(ingredient.getFormattedBestBeforeDate());
                case "location":
                    ingredientSort.setText(ingredient.getLocation());
                case "category":
                    ingredientSort.setText(ingredient.getCategory());
            }
        } else {
            String sortValue = "description";
            ingredientSort.setText("");
        }
        notifyDataSetChanged();


        // set up delete button on each list item and onClick
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.ingredient_item_deleteButton);
        deleteButton.setFocusable(false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * OnClick for delete button on each item in ingredients list
             * Deletes an ingredient from the Ingredients database
             */
            public void onClick(View view) {
                if (ingredients.size() > 0) {

                    // find selection

                    // Access a Cloud Firestore instance from your Activity
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    // Get a top level reference to the collection
                    final CollectionReference IngredientCollection = db.collection("Ingredients");
                    IngredientCollection
                            .document(ingredients.get(position).getDescription())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d("Sample", "Data has been deleted successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d("Sample", "Data could not be deleted!" + e.toString());
                                }
                            });


                    // find and remove selection

                    ingredients.remove(Math.min(position, ingredients.size() - 1));
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }


}
