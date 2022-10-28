package com.example.foodcart.ingredients;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodcart.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                            Date BBD = parseDate(ingredientBestBeforeDate.getText().toString());
                            String count = ingredientCount.getText().toString();
                            String unit = ingredientUnit.getText().toString();
                            String category = ingredientCategory.getText().toString();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, location, count, unit, category);
                            //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                            int countInt = Integer.parseInt(count);

                            if (BBD != null && !emptyStringsExist) {
                                Ingredient newIngredient = new Ingredient(description, BBD, location, countInt, unit, category);
                                listener.onOkPressedEdit(newIngredient);
                            } else {
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
                            Date BBD = parseDate(ingredientBestBeforeDate.getText().toString());
                            String count = ingredientCount.getText().toString();
                            String unit = ingredientUnit.getText().toString();
                            String category = ingredientCategory.getText().toString();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(description, location, count, unit, category);
                            //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                            int countInt = Integer.parseInt(count);

                            if (BBD != null && !emptyStringsExist) {
                                Ingredient newIngredient = new Ingredient(description, BBD, location, countInt, unit, category);
                                listener.onOkPressed(newIngredient);
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

    private boolean emptyStringCheck(String description, String location, String count, String unit, String category) {
        boolean emptyStringExist = true;
        if(description.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Description", Toast.LENGTH_SHORT).show();
        }
        else if(location.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Location", Toast.LENGTH_SHORT).show();
        }
        else if(count.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Count", Toast.LENGTH_SHORT).show();
        }
        else if(unit.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Unit", Toast.LENGTH_SHORT).show();
        }
        else if(category.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Category", Toast.LENGTH_SHORT).show();
        }
        else {
            emptyStringExist = false;
        }

        return emptyStringExist;
    }

}
