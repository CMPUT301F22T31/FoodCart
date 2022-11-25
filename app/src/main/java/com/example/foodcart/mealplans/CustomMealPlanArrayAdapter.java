package com.example.foodcart.mealplans;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomMealPlanArrayAdapter extends ArrayAdapter<Meal> {
    private ArrayList<Meal> meals;
    private Context context;
    private FirebaseFirestore db;

    /**
     * Constructor for custom array of mealplan adapter
     * @param context
     * @param meals
     */
    public CustomMealPlanArrayAdapter(Context context, ArrayList<Meal> meals) {
        super(context, 0, meals);
        this.meals = meals;
        this.context = context;
    }



    @NonNull
    @Override
    /**
     * Get the view of the ingredients list and provides delete
     * and sort functionality for the ingredients in the list
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_mealplan_item, parent, false);
        }

        Meal meal = meals.get(position);

        // Get the views of the TextViews
        TextView mealname = view.findViewById(R.id.mealplan_name);
        TextView mealtype = view.findViewById(R.id.mealplan_type);;
        TextView mealdate = view.findViewById(R.id.mealplan_date);;

        mealname.setText(meal.getMealName());
        mealtype.setText(meal.getMealType());
        mealdate.setText(meal.getFormattedDate());

        // set up delete button on each list item and onClick
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.mealplan_item_deleteButton);
        deleteButton.setFocusable(false);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * OnClick for delete button on each item in ingredients list
             * Deletes an ingredient from the Ingredients database
             */
            public void onClick(View view) {
                if (meals.size() > 0) {

                    // Access a Cloud Firestore instance from your Activity
                    db = FirebaseFirestore.getInstance();
                    // Get a top level reference to the collection
                    final CollectionReference MealPlanCollection = db.collection("MealPlan");
                    Meal meal = meals.get(position);
                    if(meal.getMealName().equals("Ingredient")) {
                        MealPlanFragment.delMealIngredientDB(meal.getMealName(), MealPlanCollection);
                    } else {
                        // delete ingredient list
                        MealPlanCollection
                                .document(meal.getMealName())
                                .collection("Ingredients")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("Delete MealIngredient", document.getId() + meal.getMealName());
                                                IngredientFragment.delIngredientDB(document.getId(),
                                                        MealPlanCollection.document(meal.getMealName())
                                                        .collection("Ingredients"));
                                            }
                                        } else {
                                            Log.d("Delete Meal Ingredient", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        // delete recipe
                        MealPlanCollection
                                .document(meals.get(position).getMealName())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        Log.d("Delete Meal Recipe", "Data has been deleted successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        Log.d("Delete Meal Recipe", "Data could not be deleted!" + e.toString());
                                    }
                                });
                    }
                    // find and remove selection
                    meals.remove(Math.min(position, meals.size() - 1));
                    notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
