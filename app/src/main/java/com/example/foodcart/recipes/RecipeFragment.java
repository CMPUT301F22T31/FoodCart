package com.example.foodcart.recipes;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import com.example.foodcart.ingredients.*;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Fragment for Recipe
 * Gives each recipe in the ListView on the recipe page
 */
public class RecipeFragment extends DialogFragment {
    private ImageView recipeImage;
    private EditText recipeTitle;
    private EditText recipePrepareTime;
    private EditText recipeServings;
    private EditText recipeCategory;
    private EditText recipeComments;
    private Bitmap imageBitmap;
    private ArrayList<Ingredient> ingredients;
    private OnFragmentInteractionListener listener;
    private FirebaseFirestore db;


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

        ingredients = new ArrayList<>();
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference recipeCollection = db.collection("Recipes");

        recipeTitle = view.findViewById(R.id.recipeTitleET);
        recipePrepareTime = view.findViewById(R.id.recipePrepareTimeET);
        recipeServings = view.findViewById(R.id.recipeServingsET);
        recipeCategory = view.findViewById(R.id.recipeCategoryET);
        recipeComments = view.findViewById(R.id.recipeCommentsET);
        Bundle args = getArguments();

        //add image function
        final Button recipeTakeImageButton = view.findViewById(R.id.recipeImgUploadButton);
        recipeTakeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivity.launch((Intent.createChooser(cameraIntent, "Select Image")));
            }
        });

        // view/edit ingredients function
        final Button viewIngredients = view.findViewById(R.id.recipeIngredientButton);
        viewIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingredientIntent = new Intent(getContext(), RecipeIngredientsActivity.class);
                ingredientIntent.putExtra("IngredientList", ingredients);
                ingredientActivity.launch((Intent.createChooser(ingredientIntent, "Ingredient")));
            }
        });

        //edit recipe functionality
        if (args != null) {
            Recipe currentRecipe = (Recipe) args.getSerializable("recipe");
            imageBitmap = currentRecipe.getBitmapPicture();
            recipeImage.setImageBitmap(imageBitmap);
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
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheckRecipe(title, prepTime, serves, category);
                            int prepTimeInt = parsePrepTime(prepTime);
                            int servesInt = parseServing(serves);
                            if (!emptyStringsExist && imageBitmap != null && prepTimeInt != -1 && servesInt != -1 && !ingredients.isEmpty()) {
                                Iterator<Ingredient> iter = ingredients.iterator();
                                String picture = bitmapToString(imageBitmap);
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, comments, picture, category, ingredients);
                                listener.onOkPressedEditRecipe(newRecipe);
                                // Add new edited recipe to database
                                HashMap<String, String> data = new HashMap<>();
                                data.put("Prep Time", prepTime);
                                data.put("Servings", serves);
                                data.put("Category", category);
                                data.put("Comments", comments);
                                data.put("Picture", picture);
                                recipeCollection
                                        .document(title)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("Edit Recipe", String.valueOf(data.get("Title")));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("ERROR Edit Recipe",
                                                        String.valueOf(data.get("Title")) + e.toString());
                                            }
                                        });

                                // While ingredients are in ArrayList
                                while(iter.hasNext())
                                {
                                    // Erase all previous entries to add Ingredient
                                    data.clear();
                                    Ingredient currentIngredient = iter.next();
                                    // Put all ingredient members into hashmap
                                    data.put("Count", Integer.toString(currentIngredient.getCount()));
                                    data.put("Unit", currentIngredient.getUnit());
                                    data.put("Category", currentIngredient.getCategory());

                                    // get reference to sub-collection
                                    CollectionReference IngredientCollection = db.collection("Recipes")
                                                                                .document(title).collection("Ingredients");
                                    // put ingredient into sub-collection
                                    IngredientCollection
                                            .document(currentIngredient.getDescription())
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("Edit RecipeI", String.valueOf(iter.next().getDescription()));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("ERROR Edit RecipeI",
                                                            String.valueOf(iter.next().getDescription()) + e.toString());
                                                }
                                            });
                                }

                            }
                            else {
                                Toast.makeText(getContext(), "Make sure all mandatory fields are filled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create();

        }

        //add recipe functionality
        else {
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
                            //validate empty strings
                            boolean emptyStringsExist = emptyStringCheckRecipe(title, prepTime, serves, category);
                            int prepTimeInt = parsePrepTime(prepTime);
                            int servesInt = parseServing(serves);
                            if (!emptyStringsExist && imageBitmap != null && prepTimeInt != -1 && servesInt != -1 && !ingredients.isEmpty()) {
                                Iterator<Ingredient> iter = ingredients.iterator();
                                String picture  = bitmapToString(imageBitmap);
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, comments, picture, category, ingredients);
                                listener.onOkPressedRecipe(newRecipe);

                                // Add new recipe to database
                                HashMap<String, String> data = new HashMap<>();
                                data.put("Prep Time", prepTime);
                                data.put("Servings", serves);
                                data.put("Category", category);
                                data.put("Comments", comments);
                                data.put("Picture", picture);
                                recipeCollection
                                        .document(title)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("Add Recipe", String.valueOf(data.get("Title")));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                Log.d("ERROR Add Recipe", String.valueOf(data.get("Title")) + e.toString());
                                            }
                                        });

                                // While ingredients are in ArrayList
                                while(iter.hasNext())
                                {
                                    // Erase all previous entries to add Ingredient
                                    data.clear();
                                    Ingredient currentIngredient = iter.next();
                                    // Put all ingredient members into hashmap
                                    data.put("Count", Integer.toString(currentIngredient.getCount()));
                                    data.put("Unit", currentIngredient.getUnit());
                                    data.put("Category", currentIngredient.getCategory());
                                    // get reference to sub-collection ingredients in recipe document
                                    CollectionReference IngredientCollection = db.collection("Recipes")
                                            .document(title).collection("Ingredients");
                                    // put ingredient into sub-collection
                                    IngredientCollection
                                            .document(currentIngredient.getDescription())
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("Add RecipeI", String.valueOf(iter.next().getDescription()));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("ERROR Add RecipeI", String.valueOf(iter.next().getDescription()));
                                                }
                                            });
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "Make sure all mandatory fields are filled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).create();
        }
    }

    /**
     * Camera Activity. Opens the Camera app takes the picture and sets the image view with that picture
     */
    ActivityResultLauncher<Intent> cameraActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                            recipeImage.setImageBitmap(imageBitmap);
                        }
                    }
                }
            });

    /**
     * Opens the ingredient activity to add/edit/view ingredients
     */
    ActivityResultLauncher<Intent> ingredientActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Bundle list = new Bundle();
                    ingredients = (ArrayList<Ingredient>) list.getSerializable("EditedList");
                }
            });


    /**
     * Parse prepTime to an integer with error catching
     * @param prepTime
     * @return the prepTime as an integer
     */
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

    /**
     * Parse the num of servings to an integer with error catching
     * @param serving
     * @return the number of servings as an integer
     */
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

    /**
     * Checks if title, prepTime, serves, or category are empty strings
     * @param title
     * @param prepTime
     * @param serves
     * @param category
     * @return boolean if any of these are empty strings
     */
    // code is not reused as it also toasts specific errors
    private boolean emptyStringCheckRecipe(String title, String prepTime, String serves, String category) {
        boolean emptyStringExist = false;
        if(title.isEmpty()) {
            emptyStringExist = true;
            Toast.makeText(getContext(), "Please Enter Recipe Title", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Please Enter Recipe Category", Toast.LENGTH_SHORT).show();
        }
        return emptyStringExist;
    }

    /**
     * Converts bitmap picture to string value
     * @param picture
     * @return
     * string encoded picture
     */
    private String bitmapToString(Bitmap picture) {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG,100, byteArrayStream);
        return Base64.encodeToString(byteArrayStream.toByteArray(), Base64.DEFAULT);
    }
}
