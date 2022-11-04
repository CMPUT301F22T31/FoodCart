package com.example.foodcart.recipes;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private Uri imageURI;
    private OnFragmentInteractionListener listener;


    public interface OnFragmentInteractionListener {
        void onOkPressedRecipe(Recipe newRecipe);
        void onOkPressedEditRecipe(Recipe newRecipe);
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
        final Button recipeTakeImage = view.findViewById(R.id.recipeImgUploadButton);
        recipeTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
                cameraIntent.setType("image/*");
                cameraActivity.launch((Intent.createChooser(cameraIntent, "Select Image")));
            }
        });
        recipeTitle = view.findViewById(R.id.recipeTitleET);
        recipePrepareTime = view.findViewById(R.id.recipePrepareTimeET);
        recipeServings = view.findViewById(R.id.recipeServingsET);
        recipeCategory = view.findViewById(R.id.recipeCategoryET);
        recipeComments = view.findViewById(R.id.recipeCommentsET);
        Bundle args = getArguments();
        if (args != null) {
            //edit recipe functionality
            Recipe currentRecipe = (Recipe) args.getSerializable("recipe");
            recipeImage.setImageURI(Uri.parse(currentRecipe.getPicture()));
            recipeTitle.setText(currentRecipe.getTitle());
            recipePrepareTime.setText(Integer.toString(currentRecipe.getPrep_time()));
            recipeServings.setText(Integer.toString(currentRecipe.getServings()));
            recipeCategory.setText(currentRecipe.getCategory());
            recipeComments.setText(currentRecipe.getComments());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            return builder
                    .setView(view)
                    .setTitle("View/Edit Recipe")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String title = recipeTitle.getText().toString();
                            String prepTime = recipePrepareTime.getText().toString();
                            String serves = recipeServings.getText().toString();
                            String category = recipeCategory.getText().toString();
                            String comments = recipeComments.getText().toString();
                            //replace this with ingredient arraylist
                            ArrayList<Ingredient> ingredients = new ArrayList<>();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(title, prepTime, serves, category);
                            if (!emptyStringsExist && imageURI != null) {
                                //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                                int prepTimeInt = Integer.parseInt(prepTime);
                                int servesInt = Integer.parseInt(serves);
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, comments, imageURI.toString(), category, ingredients);
                                listener.onOkPressedEditRecipe(newRecipe);
                            }
                            else {
                                Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
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
                            //replace this with ingredient arraylist
                            ArrayList<Ingredient> ingredients = new ArrayList<>();
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheck(title, prepTime, serves, category);
                            int prepTimeInt = parsePrepTime(prepTime);
                            int servesInt = parseServing(prepTime);
                            if (!emptyStringsExist && imageURI != null && prepTimeInt != -1 && servesInt != -1) {
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, comments, imageURI.toString(), category, ingredients);
                                listener.onOkPressedRecipe(newRecipe);
                            }
                            else {
                                Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create();
        }
    }

    ActivityResultLauncher<Intent> cameraActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imageURI = result.getData().getData();
                            recipeImage.setImageURI(imageURI);
                        }
                    }
                }
            });

    private int parsePrepTime(String prepTime) {
        int result = -1;
        try {
            result = Integer.parseInt(prepTime);
        }
        catch(Exception e) {
            Toast.makeText(getContext(), "Please Enter a number for Preparation Time", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private int parseServing(String serving) {
        int result = -1;
        try {
            result = Integer.parseInt(serving);
        }
        catch(Exception e) {
            Toast.makeText(getContext(), "Please Enter a number for Servings", Toast.LENGTH_SHORT).show();
        }
        return result;
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
