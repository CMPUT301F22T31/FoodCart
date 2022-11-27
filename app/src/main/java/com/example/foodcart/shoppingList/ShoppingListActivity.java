package com.example.foodcart.shoppingList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.CustomIngredientArrayAdapter;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.ingredients.IngredientFragment;
import com.example.foodcart.mealplans.Meal;
import com.example.foodcart.mealplans.MealPlanActivity;
import com.example.foodcart.recipes.Recipe;
import com.example.foodcart.recipes.RecipeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ShoppingListActivity extends AppCompatActivity
        implements ShoppingItemFragment.OnFragmentInteractionListener {

        // Declare the variables
        private ListView shoppingList;
        private ArrayAdapter<ShoppingItem> shoppingAdapter;
        private ArrayList<ShoppingItem> dataList;
        private ArrayList<ShoppingItem> tempList;
        private int selected;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        private String[] sortValues = { "description", "category" };

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_shopping_list);

            // initialize lists
            shoppingList = findViewById(R.id.shopping_list);
            dataList = new ArrayList<>();
            tempList = new ArrayList<>();

            // set adapter
            shoppingAdapter = new CustomShoppingItemArrayAdapter(this, dataList);
            shoppingList.setAdapter(shoppingAdapter);

            final ImageButton IngredientTab = findViewById(R.id.ingredients_tab);
            IngredientTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(),
                            IngredientActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

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

            final ImageButton MealPlanTab = findViewById(R.id.mealplans_tab);
            MealPlanTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent switchActivityIntent = new Intent(getApplicationContext(),
                            MealPlanActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });

            // onClick for Add Food Button (floating action + button)
            final FloatingActionButton addItemButton = findViewById(R.id.add_item_button);
            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new ShoppingItemFragment().show(getSupportFragmentManager(), "ADD_SHOPPING_ITEM");
                    for(ShoppingItem i : dataList){
                        if(i.isChecked()) {
                            new ShoppingItemFragment().newInstance(i).show(getSupportFragmentManager(), "EDIT_INGREDIENT");
                        }
                    }
                }
            });

            db = FirebaseFirestore.getInstance();
            // Get a top level reference to the collection
            final CollectionReference MealPlanCollection = db.collection("MealPlan");
            MealPlanCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        tempList.clear();
                        ArrayList<Ingredient> ingredientList = new ArrayList<>();
                        assert task.getResult() != null;
                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            Log.d("Update ShoppingList", doc.getId());
                            String title = doc.getId();
                            if(doc.getData().get("Type").equals("Recipe")) {
                                String scale = (String) doc.getData().get("Scale");
                                CollectionReference ingredients = db.collection("MealPlan")
                                        .document(title).collection("Ingredients");
                                ingredients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                                int scaleInt = Integer.parseInt(scale);
                                                // add ingredient to list
                                                ShoppingItem item_temp = new ShoppingItem(description, countInt*scaleInt,0, unit, category);
                                                additemCheck(item_temp);
                                            }
                                        } else {
                                            Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                                        }
                                    }
                                });
                            } else {
                                String description = (String) doc.getData().get("MealName");
                                String count = (String) doc.getData().get("Count");
                                String unit = (String) doc.getData().get("Unit");
                                String category = (String) doc.getData().get("Category");
                                // Convert date string into Date class
                                int countInt = Integer.parseInt(count);
                                // add ingredient to list
                                ShoppingItem item_temp = new ShoppingItem(description, countInt,0, unit, category);
                                additemCheck(item_temp);
                            }
                        }// Notifying the adapter to render any new data fetches from the cloud
                    }
                }
            });
        }

//        @Override
        public void onOkPressedEdit(ShoppingItem item) {
            for(ShoppingItem i:dataList){
                if(i.getDescription().equals(item.getDescription())){
                    i.setCount(i.getCount()-item.getCount());
                    if(i.getCount() <=0){
                        dataList.remove(i);
                    }
                    break;
                }
            }
            shoppingAdapter.notifyDataSetChanged();
        }

        public void additem(ShoppingItem item){
            if(tempList.contains(item)){
                for(ShoppingItem i:tempList){
                    if(i.getDescription().equals(item.getDescription())){
                        i.setCount(i.getCount()+item.getCount());
                        break;
                    }
                }
            } else {
                tempList.add(item);
            }
            dataList.clear();
            for(ShoppingItem i :tempList){
                if(i.getCount()-i.getOldcount() >0){
                    dataList.add(i);
                }
            }
            shoppingAdapter.notifyDataSetChanged();
        }


        public void additemCheck(ShoppingItem item) {
            DocumentReference docIdRef = db.collection("Ingredients").document(item.getDescription());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(tempList.contains(item)){
                            additem(item);
                            return;
                        }
                        if (document.exists()) {
                            String ing_count = (String)document.getData().get("Count");
                            item.setCount(item.getCount());
                            item.setOldcount(Integer.parseInt(ing_count));
                            additem(item);
                            Log.d("Adjust Item exists", item.getDescription());
                        } else {
                            additem(item);
                        }
                    } else {
                        Log.d("Adjust Item", "Failed with: ", task.getException());
                    }
                }
        });
    }
}
