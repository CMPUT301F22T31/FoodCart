package com.example.foodcart.ingredients;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodcart.R;
import com.example.foodcart.recipes.RecipeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class IngredientFragment extends DialogFragment {
    private EditText ingredientDescription;
    private EditText ingredientLocation;
    private EditText ingredientBestBeforeDate;
    private EditText ingredientCount;
    private EditText ingredientUnit;
    private EditText ingredientCategory;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient newIngredient);
        void onOkPressedEdit(Ingredient newIngredient);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + "must implement OnFragmentInteractionListener ");
        }
    }

    /**
    Create new instance to make it possible to edit some food objects
     */
    public static IngredientFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ingredient, null);

        ingredientDescription = view.findViewById(R.id.ingredientDescriptionET);
        ingredientLocation = view.findViewById(R.id.ingredientLocationET);
        ingredientBestBeforeDate = view.findViewById(R.id.ingredientBestBeforeDateET);
        ingredientCount = view.findViewById(R.id.ingredientCountET);
        ingredientUnit = view.findViewById(R.id.ingredientUnitET);
        ingredientCategory = view.findViewById(R.id.ingredientCategoryET);

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference IngredientCollection = db.collection("Ingredients");

        Bundle args = getArguments();
        //Edit Ingredient functionality
        if(args != null) {
            Ingredient ingredient = (Ingredient) args.getSerializable("ingredient");
            ingredientDescription.setText(ingredient.getDescription());
            ingredientLocation.setText(ingredient.getLocation());
            ingredientBestBeforeDate.setText(new SimpleDateFormat("yyyy-mm-dd").format(ingredient.bestBeforeDate));
            ingredientCount.setText(ingredient.getCount().toString());
            ingredientUnit.setText(ingredient.getUnit());
            ingredientCategory.setText(ingredient.getCategory());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setTitle("View/Edit Ingredient")
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener(){
                        //edit button on Edit food functionality
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = ingredientDescription.getText().toString();
                            String location = ingredientLocation.getText().toString();
                            String date = ingredientBestBeforeDate.getText().toString();
                            String count = ingredientCount.getText().toString();
                            String unit = ingredientUnit.getText().toString();
                            String category = ingredientCategory.getText().toString();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, date, location, count, unit, category);
                            if (!emptyStringsExist) {
                                //try to parse the date
                                Date BBD = parseDate(date);
                                //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                                int countInt = Integer.parseInt(count);
                                if (BBD != null) {
                                    Ingredient newIngredient = new Ingredient(description, BBD, location, countInt, unit, category);
                                    listener.onOkPressedEdit(newIngredient);
                                    // Add new ingredient to DataBase
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("Location", location);
                                    data.put("Date", date);
                                    data.put("Count", count);
                                    data.put("Unit", unit);
                                    data.put("Category", category);
                                    IngredientCollection
                                            .document(description)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("Edit Ingredient", String.valueOf(data.get("Description")));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("ERROR Edit Ingredient", String.valueOf(data.get("Description")));
                                                }
                                            });
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
                    .setTitle("Add Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = ingredientDescription.getText().toString();
                            String location = ingredientLocation.getText().toString();
                            String date = ingredientBestBeforeDate.getText().toString();
                            String count = ingredientCount.getText().toString();
                            String unit = ingredientUnit.getText().toString();
                            String category = ingredientCategory.getText().toString();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, date, location, count, unit, category);
                            if (!emptyStringsExist) {
                                //try to parse the date
                                Date BBD = parseDate(date);
                                //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                                int countInt = parseCount(count);
                                if (BBD != null && countInt != -1) {
                                    Ingredient newIngredient = new Ingredient(description, BBD, location, countInt, unit, category);
                                    listener.onOkPressed(newIngredient);
                                    // Add new ingredient to DataBase
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("Location", location);
                                    data.put("Date", date);
                                    data.put("Count", count);
                                    data.put("Unit", unit);
                                    data.put("Category", category);
                                    IngredientCollection
                                            .document(description)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("Add Ingredient", String.valueOf(data.get("Description")));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("ERROR Add Ingredient", String.valueOf(data.get("Description")));
                                                }
                                            });
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).create();
        }
    }

    private Date parseDate(String bestBeforeDate) {
        Date date = null;
        try {
            if (!bestBeforeDate.equals("0000-00-00")) {
                date = new SimpleDateFormat("yyyy-mm-dd").parse(bestBeforeDate);
            }
        }
        catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
        }
        return date;
    }

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
    private boolean emptyStringCheck(String description, String date, String location, String count, String unit, String category) {
        boolean emptyStringExist = false;
        if(description.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
        }
        else if(date.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Date", Toast.LENGTH_SHORT).show();
        }
        else if(location.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Location", Toast.LENGTH_SHORT).show();
        }
        else if(count.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Count", Toast.LENGTH_SHORT).show();
        }
        else if(unit.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Unit", Toast.LENGTH_SHORT).show();
        }
        else if(category.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Category", Toast.LENGTH_SHORT).show();
        }
        return emptyStringExist;
    }

}
