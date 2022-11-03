package com.example.foodcart.recipes;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;

import java.util.ArrayList;


public class RecipeFragment extends DialogFragment {
    private ImageView recipeImage;
    private EditText recipeTitle;
    private EditText recipePrepareTime;
    private EditText recipeServings;
    private EditText recipeCategory;
    private EditText recipeComments;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressedRecipe(Recipe newRecipe);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RecipeFragment.OnFragmentInteractionListener){
            listener = (RecipeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener ");
        }
    }

    public static RecipeFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putSerializable("recipe", recipe);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recipe, null);
        recipeImage = view.findViewById(R.id.recipeImgView);
        recipeTitle = view.findViewById(R.id.recipeTitleET);
        recipePrepareTime = view.findViewById(R.id.recipePrepareTimeET);
        recipeServings = view.findViewById(R.id.recipeServingsET);
        recipeCategory = view.findViewById(R.id.recipeCategoryET);
        recipeComments = view.findViewById(R.id.recipeCommentsET);
        Bundle args = getArguments();
        if (args != null) {
            //edit recipe functionality
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("View/Edit Recipe")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do something
                        }
                    }).create();

        }

        else {
            //add recipe functionality
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("Add Recipe")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String title = recipeTitle.getText().toString();
                            String prepTime = recipePrepareTime.getText().toString();
                            String serves = recipeServings.getText().toString();
                            String category = recipeCategory.getText().toString();
                            String comments = recipeComments.getText().toString();
                            ArrayList<Ingredient> ingredients = new ArrayList<>();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(title, prepTime, serves, category);
                            if (!emptyStringsExist) {
                                //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                                int prepTimeInt = Integer.parseInt(prepTime);
                                int servesInt = Integer.parseInt(serves);
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, category, comments, ingredients);
                                listener.onOkPressedRecipe(newRecipe);
                            }
                        }
                    }).create();
        }
    }

    //code is not reused as it also toasts specific errors
    private boolean emptyStringCheck(String title, String prepTime, String serves, String category) {
        boolean emptyStringExist = false;
        if(title.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Title", Toast.LENGTH_SHORT).show();
        }
        else if(prepTime.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Preparation Time", Toast.LENGTH_SHORT).show();
        }
        else if(serves.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Number of Servings", Toast.LENGTH_SHORT).show();
        }
        else if(category.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Category", Toast.LENGTH_SHORT).show();
        }
        return emptyStringExist;
    }
}
