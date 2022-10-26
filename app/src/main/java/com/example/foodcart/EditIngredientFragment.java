package com.example.foodcart;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditIngredientFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        Ingredient getCurrentIngredient();
        void onOkEditPressed(Ingredient ingredient);

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
        EditText ingredientDescription;
        EditText ingredientLocation;
        EditText ingredientBestBeforeDate;
        EditText ingredientCount;
        EditText ingredientUnitCost;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_ingredient_fragment_layout, null);

        Ingredient currentItem = listener.getCurrentIngredient();
        String currentDescription = currentItem.getDescription();
        String currentLocation = currentItem.getLocation();
        Date currentBestBeforeDate = currentItem.getBestBeforeDate();
        Integer currentCount = currentItem.getCount();
        Integer currentUnitCost = currentItem.getUnitCost();


        ingredientDescription = view.findViewById(R.id.descriptionEditText);
        ingredientLocation = view.findViewById(R.id.locationEditText);
        ingredientBestBeforeDate = view.findViewById(R.id.bestBeforeDateEditText);
        ingredientCount = view.findViewById(R.id.countEditNumber);
        ingredientUnitCost = view.findViewById(R.id.unitCostEditNumber);

        ingredientDescription.setText(currentDescription);
        ingredientLocation.setText(currentLocation);
        ingredientBestBeforeDate.setText(new SimpleDateFormat("yyyy-mm-dd").format(currentBestBeforeDate));
        ingredientCount.setText(currentCount.toString());
        ingredientUnitCost.setText(currentUnitCost.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view)
                .setTitle("Edit Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String description = ingredientDescription.getText().toString();
                    String location = ingredientLocation.getText().toString();
                    Date bestBeforeDate = null;
                    if (ingredientBestBeforeDate.getText() != null) {
                        try {
                            bestBeforeDate = new SimpleDateFormat("yyyy-mm-dd").parse(ingredientBestBeforeDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer count = Integer.parseInt(ingredientCount.getText().toString());
                    Integer unitCost = Integer.parseInt(ingredientUnitCost.getText().toString());

                    try {
                        listener.onOkEditPressed(new Ingredient(description, bestBeforeDate, location, count, unitCost));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).create();
        }



    static EditIngredientFragment newInstance(Ingredient ingredient) {
        Bundle args = new Bundle();
        args.putSerializable("ingredient", ingredient);

        EditIngredientFragment fragment = new EditIngredientFragment();
        fragment.setArguments(args);
        return fragment;
    }

}

