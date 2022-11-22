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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodcart.R;
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
    private static String triggerFlag; //differentiates the fragment called from ingredient class and recipe class
    private FirebaseFirestore db;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient newIngredient);
        void onOkPressedEdit(Ingredient newIngredient);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IngredientFragment.OnFragmentInteractionListener){
            listener = (IngredientFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + "must implement OnFragmentInteractionListener ");
        }
    }

    /**
     * Create new instance to make it possible to edit some food objects
     * @param ingredient
     * @param flag
     * @return
     * object of Ingredient fragment
     */
    public static IngredientFragment newInstance(Ingredient ingredient, String flag) {
        triggerFlag = flag;
        if (flag.equals("edit") || flag.equals("editFromRecipe")) {
            Bundle args = new Bundle();
            args.putSerializable("ingredient", ingredient);
            IngredientFragment fragment = new IngredientFragment();
            fragment.setArguments(args);
            return fragment;
        }
        else {
            return new IngredientFragment();
        }
    }

    public static void addIngredientDB(Ingredient addItem,
                                       CollectionReference addCollect) {
        // Add new ingredient to DataBase
        HashMap<String, String> data = new HashMap<>();
        data.put("Location", addItem.getLocation());
        data.put("Date", addItem.getFormattedBestBeforeDate());
        data.put("Count", String.valueOf(addItem.getCount()));
        data.put("Unit", addItem.getUnit());
        data.put("Category", addItem.getCategory());
        addCollect
                .document(addItem.getDescription())
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

    public static void delIngredientDB(Ingredient delItem,
                                       CollectionReference delCollect) {
        delCollect
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

    public static void editIngredientDB(Ingredient oldItem, Ingredient newitem,
                                       CollectionReference editCollect) {
        // delete old ingredient
        delIngredientDB(oldItem, editCollect);
        // add new ingredient
        addIngredientDB(newitem, editCollect);
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
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference IngredientCollection = db.collection("Ingredients");

        Bundle args = getArguments();
        //Edit Ingredient functionality
        if (triggerFlag.equals("edit") || triggerFlag.equals("editFromRecipe")) {
            Ingredient ingredient = (Ingredient) args.getSerializable("ingredient");
            ingredientDescription.setText(ingredient.getDescription());

            if (triggerFlag.equals("edit")) {
                ingredientLocation.setText(ingredient.getLocation());
                ingredientBestBeforeDate.setText(new SimpleDateFormat("yyyy-mm-dd").format(ingredient.getBestBeforeDate()));
            } else {
                TextView BBDTextView = view.findViewById(R.id.ingredientBestBeforeDateTV);
                TextView locationTextView = view.findViewById(R.id.ingredientLocationTV);
                ingredientLocation.setVisibility(view.INVISIBLE);
                locationTextView.setVisibility(view.INVISIBLE);
                ingredientBestBeforeDate.setVisibility(view.INVISIBLE);
                BBDTextView.setVisibility(view.INVISIBLE);
            }

            ingredientCount.setText(ingredient.getCount().toString());
            ingredientUnit.setText(ingredient.getUnit());
            ingredientCategory.setText(ingredient.getCategory());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setTitle("View/Edit Ingredient")
                    .setView(view)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        //edit button on Edit food functionality
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = ingredientDescription.getText().toString();
                            String location = "";
                            String date = "";
                            String count = ingredientCount.getText().toString();
                            String unit = ingredientUnit.getText().toString();
                            String category = ingredientCategory.getText().toString();
                            if (triggerFlag.equals("edit")) {
                                location = ingredientLocation.getText().toString();
                                date = ingredientBestBeforeDate.getText().toString();
                            } else {
                                //just to make them pass empty string tests
                                location = "defaultLocation";
                                date = "1970-01-01"; //EPOCH date
                            }
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, date, location, count, unit, category);
                            //try to parse the date
                            Date BBD = parseDate(date);
                            //try to parse the count
                            int countInt = parseCount(count);
                            if (!emptyStringsExist && BBD != null && countInt != -1) {
                                if (triggerFlag.equals("edit")) {
                                    Ingredient newIngredient = new Ingredient(description, BBD, location, countInt, unit, category);
                                    listener.onOkPressedEdit(newIngredient);
                                    // edit ingredient in database
                                    editIngredientDB(ingredient, newIngredient, IngredientCollection);
                                } else {
                                    Ingredient newIngredient = new Ingredient(description, countInt, unit, category);
                                    listener.onOkPressedEdit(newIngredient);
                                }
                            } else {
                                Toast.makeText(getContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create();
        }
        //add ingredient functionality
        else {
            if (triggerFlag.equals("addFromRecipe")) {
                TextView BBDTextView = view.findViewById(R.id.ingredientBestBeforeDateTV);
                TextView locationTextView = view.findViewById(R.id.ingredientLocationTV);
                ingredientLocation.setVisibility(view.INVISIBLE);
                locationTextView.setVisibility(view.INVISIBLE);
                ingredientBestBeforeDate.setVisibility(view.INVISIBLE);
                BBDTextView.setVisibility(view.INVISIBLE);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Add Ingredient")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String description = ingredientDescription.getText().toString();
                            String location = "";
                            String date = "";
                            String count = ingredientCount.getText().toString();
                            String unit = ingredientUnit.getText().toString();
                            String category = ingredientCategory.getText().toString();
                            if (triggerFlag.equals("add")) {
                                location = ingredientLocation.getText().toString();
                                date = ingredientBestBeforeDate.getText().toString();
                            } else {
                                //just to make them pass empty string tests
                                location = "defaultLocation";
                                date = "1970-01-01"; //EPOCH date
                            }
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, date, location, count, unit, category);
                            //try to parse the date
                            Date BBD = parseDate(date);
                            //try to parse the count
                            int countInt = parseCount(count);
                            if (!emptyStringsExist && BBD != null && countInt != -1) {
                                if (triggerFlag.equals("add")) {
                                    Ingredient newIngredient = new Ingredient(description, BBD, location, countInt, unit, category);
                                    listener.onOkPressed(newIngredient);
                                    // add new ingredient to database
                                    addIngredientDB(newIngredient, IngredientCollection);
                                } else {
                                    Ingredient newIngredient = new Ingredient(description, countInt, unit, category);
                                    listener.onOkPressed(newIngredient);
                                }
                            } else {
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

    /**
     * Tests the count if it is an integer otherwise print out an exception
     * @param count
     * @return -1 if parsing failed or the result if it succeeded
     */
    public int parseCount(String count) {
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
    public boolean emptyStringCheck(String description, String date, String location, String count, String unit, String category) {
        boolean emptyStringExist = false;
        if(description.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Description", Toast.LENGTH_SHORT).show();
        }
        else if(date.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Ingredient Best Before Date", Toast.LENGTH_SHORT).show();
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

}
