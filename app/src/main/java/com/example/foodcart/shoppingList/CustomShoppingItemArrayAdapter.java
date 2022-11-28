package com.example.foodcart.shoppingList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.recipes.RecipeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * The custom array adapter for ingredients. Also includes delete
 * and sort functionality for items in the ingredients list
 *
 * @author Ashley
 * @version 1.0
 * @see ShoppingListActivity
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
        itemQuantity.setText(Integer.toString(item.getCount()-item.getOldcount()));

        final CheckBox checkBox = view.findViewById(R.id.shopping_item_checkButton);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    item.setChecked(true);
                } else{
                    item.setChecked(false);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }


}
