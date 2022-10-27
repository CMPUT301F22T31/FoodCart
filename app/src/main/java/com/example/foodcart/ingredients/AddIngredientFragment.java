package com.example.foodcart.ingredients;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddIngredientFragment extends DialogFragment {
    private EditText ingredientDescription;
    private EditText ingredientLocation;
    private EditText ingredientBestBeforeDate;
    private EditText ingredientCount;
    private EditText ingredientCategory;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient ingredient);

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_ingredient_fragment_layout, null);

        ingredientDescription = view.findViewById(R.id.descriptionAddText);
        ingredientLocation = view.findViewById(R.id.locationAddText);
        ingredientBestBeforeDate = view.findViewById(R.id.bestBeforeDateAddText);
        ingredientCount = view.findViewById(R.id.countAddNumber);
        ingredientCategory = view.findViewById(R.id.categoryAddText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Food")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String description = ingredientDescription.getText().toString();
                        String location = ingredientLocation.getText().toString();

                        Date bestBeforeDate = null;
                        try {
                            bestBeforeDate = new SimpleDateFormat("yyyy-mm-dd").parse(ingredientBestBeforeDate.getText().toString());
                        } catch (ParseException e) {
                            System.out.println("Invalid format");
                            e.printStackTrace();
                        }
                        int count = Integer.parseInt(ingredientCount.getText().toString());
                        int unitCost = Integer.parseInt(ingredientUnitCost.getText().toString());
                        System.out.println(description + " " + location);
                        try {
                            listener.onOkPressed(new Ingredient(description, bestBeforeDate, location, count, unitCost));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
    }

    static AddIngredientFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        AddIngredientFragment fragment = new AddIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
