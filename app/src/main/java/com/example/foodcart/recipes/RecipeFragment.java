package com.example.foodcart.recipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Fragment for Recipe which allows user to add/edit a recipe
 *
 * @author Arsh, Ahmed, Ashley, Alfred
 * @version 3.0
 * @see RecipeActivity              calling activity
 * @see RecipeIngredientsActivity   allows user to add ingredients to a recipe
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

    /**
     * Recursively delete a recipe from a given database collection reference
     * @param delRecipe     The recipe to delete
     * @param delCollect    The collection to delete from
     */
    public static void delRecipeDB(Recipe delRecipe,
                                   CollectionReference delCollect) {

        ArrayList<Ingredient> delIngredients = delRecipe.getIngredientList();
        Iterator<Ingredient> iter = delIngredients.iterator();

        // get reference to ingredient collection
        CollectionReference delIngredientCollect = delCollect.document(delRecipe.getTitle())
                                                    .collection("Ingredients");
        while(iter.hasNext())
        {
            Ingredient currentIngredient = iter.next();
            IngredientFragment.delIngredientDB(currentIngredient.getDescription(), delIngredientCollect);
        }
        delCollect
                .document(delRecipe.getTitle())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("Delete Recipe", "Data has been deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("Delete Recipe", "Data could not be deleted!" + e.toString());
                    }
                });
    }

    /**
     * Add a recipe to a given database collection reference
     * @param addRecipe     The recipe to add
     * @param addCollect    The collection to add it too
     */
    public static void addRecipeDB(Recipe addRecipe,
                                   CollectionReference addCollect) {
        // Add new edited recipe to database
        HashMap<String, String> data = new HashMap<>();
        data.put("Prep Time", String.valueOf(addRecipe.getPrep_time()));
        data.put("Servings", String.valueOf(addRecipe.getServings()));
        data.put("Category", addRecipe.getCategory());
        data.put("Comments", addRecipe.getComments());
        data.put("Picture", addRecipe.getPicture());
        addCollect
                .document(addRecipe.getTitle())
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
        for (Ingredient ingredient : addRecipe.getIngredientList()) {
            // Erase all previous entries to add Ingredient
            data.clear();
            // Put all ingredient members into hashmap
            data.put("Count", Integer.toString(ingredient.getCount()));
            data.put("Unit", ingredient.getUnit());
            data.put("Category", ingredient.getCategory());

            // get reference to sub-collection
            CollectionReference IngredientCollection = addCollect.document(addRecipe.getTitle()).collection("Ingredients");
            // put ingredient into sub-collection
            IngredientCollection
                    .document(ingredient.getDescription())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d("Edit RecipeI", String.valueOf(ingredient.getDescription()));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d("ERROR Edit RecipeI",
                                    String.valueOf(ingredient.getDescription()) + e.toString());
                        }
                    });
        }
    }

    /**
     * edit a recipe from a given database collection reference
     * @param oldRecipe     The old recipe to replace
     * @param newRecipe     The new recipe to replace old recipe with
     * @param editCollect   The collection containing oldRecipe
     */
    public static void editRecipeDB(Recipe oldRecipe, Recipe newRecipe,
                                    CollectionReference editCollect) {
        // Delete old recipe before adding new one
        delRecipeDB(oldRecipe, editCollect);
        // Delete old recipe before adding new one
        addRecipeDB(newRecipe, editCollect);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recipe, null);
        recipeImage = view.findViewById(R.id.recipeImgView);
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference recipeCollection = db.collection("Recipes");

        recipeTitle = view.findViewById(R.id.recipeTitleET);
        recipePrepareTime = view.findViewById(R.id.recipePrepareTimeET);
        recipeServings = view.findViewById(R.id.recipeServingsET);
        recipeCategory = view.findViewById(R.id.recipeCategoryET);
        recipeComments = view.findViewById(R.id.recipeCommentsET);
        ingredients = new ArrayList<>();
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
                Intent ingredientIntent = new Intent(getActivity(), RecipeIngredientsActivity.class);
                ingredientIntent.putExtra("IngredientList", ingredients);
                ingredientActivity.launch(ingredientIntent);
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
            CollectionReference ingredientData = recipeCollection.document(currentRecipe.getTitle()).collection("Ingredients");
            ingredientData.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot ing : task.getResult()) {
                           String description = ing.getId();
                           String count = (String) ing.getData().get("Count");
                           String unit = (String) ing.getData().get("Unit");
                           String category = (String) ing.getData().get("Category");
                           // Convert date string into Date class
                           int countInt = Integer.parseInt(count);
                           // add ingredient to list
                           ingredients.add(new Ingredient(description, countInt, unit, category));
                       }
                   }
               }
            });

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

                                String picture = bitmapToString(imageBitmap);
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, comments, picture, category, ingredients);
                                listener.onOkPressedEditRecipe(newRecipe);

                                // reflect recipe changes to database
                                editRecipeDB(currentRecipe, newRecipe, recipeCollection);

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
                                String picture  = bitmapToString(imageBitmap);
                                Recipe newRecipe = new Recipe(title, prepTimeInt, servesInt, comments, picture, category, ingredients);
                                listener.onOkPressedRecipe(newRecipe);

                                // Add recipe to database
                                addRecipeDB(newRecipe, recipeCollection);
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
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ingredients = (ArrayList<Ingredient>) result.getData().
                                getSerializableExtra("EditedList");
                    }
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
     * @param picture bitmap image from camera activity
     * @return
     * string encoded picture
     */
    private String bitmapToString(Bitmap picture) {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG,100, byteArrayStream);
        return Base64.encodeToString(byteArrayStream.toByteArray(), Base64.DEFAULT);
    }
}
