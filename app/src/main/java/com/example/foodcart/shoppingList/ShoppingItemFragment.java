package com.example.foodcart.shoppingList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 */
public class ShoppingItemFragment extends DialogFragment {
    private EditText itemDescription;
    private EditText itemCount;
    private EditText itemUnit;
    private EditText itemCategory;
    private ShoppingItemFragment.OnFragmentInteractionListener listener;
    private FirebaseFirestore db;

    public interface OnFragmentInteractionListener {
        void onOkPressed(ShoppingItem newItem);
        void onOkPressedEdit(ShoppingItem newItem);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShoppingItemFragment.OnFragmentInteractionListener){
            listener = (ShoppingItemFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener ");
        }
    }

    public static void addShoppingItemDB(ShoppingItem addItem,
                                       CollectionReference shoppingCollect) {
        // Add new ingredient to DataBase
        HashMap<String, String> data = new HashMap<>();
        data.put("Count", String.valueOf(addItem.getCount()));
        data.put("Unit", addItem.getUnit());
        data.put("Category", addItem.getCategory());
        shoppingCollect
                .document(addItem.getDescription())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("Edit Item", String.valueOf(data.get("Description")));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("ERROR Edit Item", String.valueOf(data.get("Description")));
                    }
                });
    }

    public static void delShoppingItemDB(ShoppingItem delItem,
                                       CollectionReference shoppingCollect) {
        shoppingCollect
                .document(delItem.getDescription())
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
    }

    public static void editShoppingItemDB(ShoppingItem oldItem,
                                        ShoppingItem newItem,
                                        CollectionReference shoppingCollect) {
        delShoppingItemDB(oldItem, shoppingCollect);
        addShoppingItemDB(newItem, shoppingCollect);
    }

    /**
     Create new instance to make it possible to edit some food objects
     */
    public static ShoppingItemFragment newInstance(ShoppingItem item) {
        Bundle args = new Bundle();
        args.putSerializable("item", item);

        ShoppingItemFragment fragment = new ShoppingItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_shopping_item, null);

        itemDescription = view.findViewById(R.id.itemDescriptionET);
        itemCount = view.findViewById(R.id.itemCountET);
        itemUnit = view.findViewById(R.id.itemUnitET);
        itemCategory = view.findViewById(R.id.itemCategoryET);
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference ShoppingListCollection = db.collection("Shopping List");

        Bundle args = getArguments();
        //Edit ShoppingItem functionality
        if(args != null) {
            ShoppingItem item = (ShoppingItem) args.getSerializable("shopping item");
            itemDescription.setText(item.getDescription());
            itemCount.setText((String) item.getCount().toString());
            itemUnit.setText(item.getUnit());
            itemCategory.setText(item.getCategory());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setTitle("View/Edit Ingredient")
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener(){
                        //edit button on Edit food functionality
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = itemDescription.getText().toString();
                            String count = itemCount.getText().toString();
                            String unit = itemUnit.getText().toString();
                            String category = itemCategory.getText().toString();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, count, unit, category);
                            if (!emptyStringsExist) {
                                //try to parse the count
                                int countInt = parseCount(count);
                                if (countInt > 0) {
                                    ShoppingItem newItem = new ShoppingItem(description, countInt, unit, category);
                                    listener.onOkPressedEdit(newItem);
                                    // edit item in database
                                    editShoppingItemDB(item, newItem, ShoppingListCollection);
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create();
        }
        //add ingredient functionality
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Add Item")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = itemDescription.getText().toString();
                            String count = itemCount.getText().toString();
                            String unit = itemUnit.getText().toString();
                            String category = itemCategory.getText().toString();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, count, unit, category);
                            if (!emptyStringsExist) {
                                //try to parse the count
                                int countInt = parseCount(count);
                                if (countInt > 0) {
                                    ShoppingItem newItem = new ShoppingItem(description, countInt, unit, category);
                                    listener.onOkPressed(newItem);
                                    // add item to database
                                    addShoppingItemDB(newItem, ShoppingListCollection);
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).create();
        }
    }

    /**
     * Tests the count if it is an integer otherwise print out an exception
     * @param count
     * @return -1 if parsing failed or the result if it succeeded
     */
    private int parseCount(String count) {
        int result = -1;
        try {
            result = Integer.parseInt(count);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Invalid Count", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    //code is not reused as it also toasts specific errors
    private boolean emptyStringCheck(String description, String count, String unit, String category) {
        boolean emptyStringExist = false;
        if(description.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
        }
        else if(count.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Count", Toast.LENGTH_SHORT).show();
        }
        else if(category.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Category", Toast.LENGTH_SHORT).show();
        }
        return emptyStringExist;
    }

}
