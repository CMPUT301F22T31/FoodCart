package com.example.foodcart.mealplans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.ingredients.IngredientFragment;
import com.example.foodcart.recipes.CustomRecipeArrayAdapter;
import com.example.foodcart.recipes.Recipe;
import com.example.foodcart.recipes.RecipeActivity;
import com.example.foodcart.recipes.RecipeFragment;
import com.example.foodcart.shoppingList.ShoppingListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MealPlanActivity extends AppCompatActivity
        implements MealPlanScaleFragment.OnFragmentInteractionListener {

    ListView mealPlanListView;
    ArrayAdapter<Meal> mealplanAdapter;
    ArrayList<Meal> mealplanList;
    FirebaseFirestore db;
    int selected;

    FloatingActionButton addMealPlan, addmIngredientButton, addmRecipeButton;

    TextView addmIngredientText, addmRecipeText;

    boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealplan);

        mealPlanListView = findViewById(R.id.mealplan_list);
        mealplanList = new ArrayList<>();

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference mealPlanCollection = db.collection("MealPlan");

        mealplanAdapter = new CustomMealPlanArrayAdapter(this, mealplanList);
        mealPlanListView.setAdapter(mealplanAdapter);

        addMealPlan = findViewById(R.id.add_mealplan_button);
        addmIngredientButton = findViewById(R.id.add_mealplan_ingredient_button);
        addmRecipeButton = findViewById(R.id.add_mealplan_recipe_button);

        addmIngredientText = findViewById(R.id.add_mealplan_ingredient_text);
        addmRecipeText = findViewById(R.id.add_mealplan_recipe_text);

        addmIngredientButton.hide();
        addmRecipeButton.hide();
        addmIngredientText.setVisibility(View.GONE);
        addmRecipeText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        addMealPlan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {
                            addmIngredientButton.show();
                            addmRecipeButton.show();
                            addmIngredientText.setVisibility(View.VISIBLE);
                            addmRecipeText.setVisibility(View.VISIBLE);

                            isAllFabsVisible = true;
                        } else {
                            addmIngredientButton.hide();
                            addmRecipeButton.hide();
                            addmIngredientText.setVisibility(View.GONE);
                            addmRecipeText.setVisibility(View.GONE);

                            isAllFabsVisible = false;
                        }
                    }
                });

        // onClick for selecting items from list. When item is selected, Scale Recipe is selected
        mealPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Meal selectedmeal = mealplanList.get(position);
                if (selectedmeal.getMealType().equals("Recipe")){
                    selected = position;
                    new MealPlanScaleFragment().newInstance(selectedmeal).show(getSupportFragmentManager(), "EDIT_SCALE");
                }
            }
        });

        addmIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanFragment().newInstance("Ingredients").show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        addmRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanFragment().newInstance("Recipes").show(getSupportFragmentManager(), "ADD_RECIPE");
            }
        });

        // Switch to Recipe Activity
        final ImageButton RecipeTab = findViewById(R.id.recipes_tab);
        RecipeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(),
                        RecipeActivity.class);
                startActivity(switchActivityIntent);
                finish();
            }
        });

        // Switch to Ingredient Activity
        final ImageButton IngredientTab = findViewById(R.id.ingredients_tab);
        IngredientTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), IngredientActivity.class);
                startActivity(switchActivityIntent);
                finish();
            }
        });

        final ImageButton ShoppingListTab = findViewById(R.id.shoppinglist_tab);
        ShoppingListTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(getApplicationContext(),
                        ShoppingListActivity.class);
                startActivity(switchActivityIntent);
                finish();
            }
        });

        mealPlanCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                mealplanList.clear();
                assert queryDocumentSnapshots != null;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d("Update MealPlan", doc.getId());
                    String name = (String) doc.getData().get("MealName");
                    String type = (String) doc.getData().get("Type");
                    String scale = (String) doc.getData().get("Scale");
                    String tempDate = (String) doc.getData().get("Day");

                    // Convert date string into Date class
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(tempDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Meal meal = null;
                    try {
                        assert scale != null;
                        meal = new Meal(name,type,Integer.parseInt(scale),date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mealplanList.add(meal);
                }
                mealplanAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
            }
        });
    }

    @Override
    public void onOkPressed(Meal newMeal) {
        mealplanAdapter.add(newMeal);
    }

    @Override
    public void onOkEditPressed(Meal newMeal) {
        mealplanList.set(selected, newMeal);
        mealplanAdapter.notifyDataSetChanged();
    }
}