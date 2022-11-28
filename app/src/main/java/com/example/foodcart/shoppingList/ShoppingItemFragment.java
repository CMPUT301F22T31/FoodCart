package com.example.foodcart.shoppingList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.CalendarActivity;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * A fragment that allows user to edit shopping items or add an existing one to Ingredient storage
 * by filling in the required values.
 *
 * @author Ahmed, Ashley, Alfred
 * @version 1.0
 * @see ShoppingListActivity
 */
public class ShoppingItemFragment extends DialogFragment {
    private EditText itemDescription;
    private EditText itemCount;
    private EditText itemUnit;
    private EditText itemLocation;
    private TextView itemBestBeforeDate;
    private Date calendarDate = null;
    private EditText itemCategory;
    private OnFragmentInteractionListener listener;
    private FirebaseFirestore db;

    /**
     * adaptor to interact with ShoppingListActivity
     * @author Ashley
     */
    public interface OnFragmentInteractionListener {
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

    /**
     * edit a shopping item from firebase in specified collection
     * @param oldItem               description of old ingredient to delete
     * @param newItem               new ingredient to add
     * @param IngredientCollection  collection to edit ingredient in
     */
    public static void editShoppingItemDB(String oldItem, Ingredient newItem,
                                        CollectionReference IngredientCollection) {
        // Put the data in the hashmap
        HashMap<String, String> data = new HashMap<>();
        data.put("Category", newItem.getCategory());
        data.put("Count", newItem.getCount().toString());
        data.put("Date", newItem.getFormattedBestBeforeDate());
        data.put("Location", newItem.getLocation());
        data.put("Unit", newItem.getUnit());
        IngredientCollection
                .document(oldItem)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("Edit Scale", String.valueOf(data.get("Description")));
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


    /**
     * method called when fragment is first created. Creates UI elements and checks for valid user
     * input
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient, null);

        // Get our textviews
        itemDescription = view.findViewById(R.id.ingredientDescriptionET);
        itemCount = view.findViewById(R.id.ingredientCountET);
        itemLocation = view.findViewById(R.id.ingredientLocationET);
        itemUnit = view.findViewById(R.id.ingredientUnitET);
        itemCategory = view.findViewById(R.id.ingredientCategoryET);
        itemBestBeforeDate = view.findViewById(R.id.ingredientBestBeforeDateTV2);

        // Setup the calendar button to add date
        final ImageButton calendarButton = view.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calendarIntent = new Intent(getActivity(), CalendarActivity.class);
                calendarActivity.launch(calendarIntent);
            }
        });

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference IngredientCollection = db.collection("Ingredients");

        Bundle args = getArguments();
        //Edit ShoppingItem functionality
        ShoppingItem item = (ShoppingItem) args.getSerializable("item");
        itemDescription.setText(item.getDescription());
        itemLocation = view.findViewById(R.id.ingredientLocationET);
        itemCount.setText(Integer.toString(item.getCount()-item.getOldcount()));
        itemUnit.setText(item.getUnit());
        itemCategory.setText(item.getCategory());

        // Create our alertdialog to add to ingredient storage
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle("View/Edit Ingredient")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    //edit button on Edit food functionality
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String description = itemDescription.getText().toString();
                        String location = itemLocation.getText().toString();
                        String count = itemCount.getText().toString();
                        String unit = itemUnit.getText().toString();
                        String category = itemCategory.getText().toString();
                        //validate empty strings
                        boolean emptyStringsExist = emptyStringCheck(description, location, count, unit, category);
                        //try to parse the count
                        int countInt = parseCount(count)+item.getOldcount();
                        // Ensure data is valid
                        if (!emptyStringsExist && countInt != -1) {
                            if (calendarDate != null) {
                                Ingredient newIngredient = new Ingredient(description, calendarDate, location, countInt, unit, category);
                                ShoppingItem newItem = new ShoppingItem(description, countInt,item.getOldcount(),unit, category);
                                listener.onOkPressedEdit(newItem);
                                // edit ingredient in database
                                editShoppingItemDB(newIngredient.getDescription(), newIngredient, IngredientCollection);
                            }
                            else {
                                Toast.makeText(getContext(), "Please select Expiry Date", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please enter all mandatory fields correctly!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();
    }
    /**
     * Opens the calendar activity to get date
     */
    ActivityResultLauncher<Intent> calendarActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Make sure we have a valid date
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            String date = result.getData().getStringExtra("Date");
                            setDate(date);
                            itemBestBeforeDate.setText(date);
                        }
                    }
                }
            });

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

    /**
     * Checks if none of the input values are empty
     * @param description
     * @param location
     * @param count
     * @param unit
     * @param category
     * @return
     * true: if any value is empty
     * false: all values are filled
     */
    public boolean emptyStringCheck(String description, String location, String count, String unit, String category) {
        boolean emptyStringExist = false;
        if(description.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Description", Toast.LENGTH_SHORT).show();
        }
        else if(location.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Location", Toast.LENGTH_SHORT).show();
        }
        else if(count.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Count", Toast.LENGTH_SHORT).show();
        }
        else if(unit.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Unit", Toast.LENGTH_SHORT).show();
        }
        else if(category.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Category", Toast.LENGTH_SHORT).show();
        }
        return emptyStringExist;
    }

    /**
     * sets the current calendar date
     * @param date
     */
    private void setDate(String date) {
        try {
            calendarDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
