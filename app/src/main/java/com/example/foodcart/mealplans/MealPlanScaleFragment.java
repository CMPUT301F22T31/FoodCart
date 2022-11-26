package com.example.foodcart.mealplans;

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
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.shoppingList.ShoppingItem;
import com.example.foodcart.shoppingList.ShoppingItemFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MealPlanScaleFragment extends DialogFragment {
    private MealPlanScaleFragment.OnFragmentInteractionListener listener;
    private FirebaseFirestore db;
    private EditText mealScale_ET;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Meal newMeal);
        void onOkEditPressed(Meal newMeal);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealPlanScaleFragment.OnFragmentInteractionListener){
            listener = (MealPlanScaleFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener ");
        }
    }

    /**
     * Create new instance to make it possible to edit some food objects
     * @param
     * @param
     * @return
     * object of Ingredient fragment
     */
    public static MealPlanScaleFragment newInstance(Meal meal) {
        Bundle args = new Bundle();
        args.putSerializable("meal", meal);

        MealPlanScaleFragment fragment = new MealPlanScaleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mealplan_scale, null);

        mealScale_ET = view.findViewById(R.id.mealscaleET);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference MealPlanCollection = db.collection("MealPlan");

        Bundle args = getArguments();
        Meal meal = (Meal) args.getSerializable("meal");
        mealScale_ET.setText(Integer.toString(meal.getScale()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle("Edit Scale")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener(){
                    //edit button on Edit food functionality
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String scale = mealScale_ET.getText().toString();
                        //validate empty strings
                        if (!scale.isEmpty()) {
                            int scaleInt = -1;
                            //try to parse the count
                            try {
                                scaleInt = Integer.parseInt(scale);
                            }
                            catch (Exception e) {
                                Toast.makeText(getContext(), "Invalid Scale", Toast.LENGTH_SHORT).show();
                            }

                            if (scaleInt > 0) {
                                Meal newMeal= new Meal(meal.getMealName(),meal.getMealType(),scaleInt,meal.getDate());
                                listener.onOkEditPressed(newMeal);
                                // Add new ingredient to DataBase
                                HashMap<String, String> data = new HashMap<>();
                                data.put("Scale", scale);
                                MealPlanCollection
                                        .document(meal.getMealName() + meal.getFormattedDate())
                                        .update("Scale",scale)
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
                        }
                        else {
                            Toast.makeText(getContext(), "Please Enter Scale", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();
        }
    }