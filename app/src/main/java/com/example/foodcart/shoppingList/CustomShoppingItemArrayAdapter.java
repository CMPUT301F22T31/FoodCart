package com.example.foodcart.shoppingList;

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
import com.example.foodcart.ingredients.Ingredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

/**
 * The custom array adapter for ingredients. Also includes delete
 * and sort functionality for items in the ingredients list
 */
public class CustomShoppingItemArrayAdapter extends ArrayAdapter<ShoppingItem> {
    private ArrayList<ShoppingItem> items;
    private Context context;
    private FirebaseFirestore db;

    /**
     * Constructor for custom array of ingredients adapter
     * @param context
     * @param items
     */
    public CustomShoppingItemArrayAdapter(Context context, ArrayList<ShoppingItem> items) {
        super(context, 0, items);
        this.items = items;
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
            view = LayoutInflater.from(context).inflate(R.layout.content_shopping_list_item, parent, false);
        }

        ShoppingItem item = items.get(position);

        // Get the views of the TextViews
        TextView itemDescription = view.findViewById(R.id.shopping_item_name);
        TextView itemQuantity = view.findViewById(R.id.shopping_item_quantity);
        TextView itemSort = view.findViewById(R.id.shopping_item_sort);
        View parentView = (View) parent.getParent();
        Spinner sortDropDown = parentView.findViewById(R.id.shopping_list_sort_select);

        itemDescription.setText(item.getDescription());
        itemQuantity.setText(item.getCount().toString());

        if (sortDropDown.getSelectedItem() != null) {
            String sortValue = sortDropDown.getSelectedItem().toString();
            System.out.println(sortValue);
            switch (sortValue){
                case "description":
                    itemSort.setText("");
                    break;
                case "category":
                    itemSort.setText(item.getCategory());
                    break;
            }
        } else {
            String sortValue = "description";
            itemSort.setText("");
        }

        // set up delete button on each list item and onClick
        ImageButton boughtButton = (ImageButton) view.findViewById(R.id.shopping_item_boughtButton);
        boughtButton.setFocusable(false);
        boughtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * OnClick for delete button on each item in ingredients list
             * Deletes an ingredient from the Ingredients database
             */
            public void onClick(View view) {
                if (items.size() > 0) {
                    // Access a Cloud Firestore instance from your Activity
                    db = FirebaseFirestore.getInstance();
                    Ingredient ingredient = items.get(position);
                    // Get a top level reference to the collection
                    final CollectionReference IngredientCollection = db.collection("Ingredients");
                    IngredientCollection
                            .document(items.get(position).getDescription())
                            .set(ingredient)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d("Sample", "Data has been added successfully!");
                                    final CollectionReference ShoppingListCollection = db.collection("Shopping List");
                                    ShoppingListCollection
                                            .document(items.get(position).getDescription())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("Sample", "Data has been deleted successfully!");
                                                    // find and remove selection
                                                    // items.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("Sample", "Data could not be deleted.");
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d("Sample", "Data could not be added!" + e.toString());
                                }
                            });
                    notifyDataSetChanged();
                }
            }
        });

        // set up delete button on each list item and onClick
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.shopping_item_deleteButton);
        deleteButton.setFocusable(false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * OnClick for delete button on each item in ingredients list
             * Deletes an ingredient from the Ingredients database
             */
            public void onClick(View view) {
                if (items.size() > 0) {
                    // Access a Cloud Firestore instance from your Activity
                    db = FirebaseFirestore.getInstance();
                    // Get a top level reference to the collection
                    final CollectionReference ShoppingListCollection = db.collection("Shopping List");
                    // delete item from database
                    ShoppingItemFragment.delShoppingItemDB(items.get(position), ShoppingListCollection);
                    // find and remove selection
                    items.remove(Math.min(position, items.size() - 1));
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }


}
