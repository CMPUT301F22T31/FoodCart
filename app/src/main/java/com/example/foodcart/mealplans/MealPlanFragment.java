package com.example.foodcart.mealplans;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.foodcart.R;
import com.example.foodcart.ingredients.CustomIngredientArrayAdapter;
import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.ingredients.IngredientFragment;
import com.example.foodcart.recipes.CustomRecipeArrayAdapter;
import com.example.foodcart.recipes.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MealPlanFragment extends DialogFragment {
    private FirebaseFirestore db;
    private ArrayAdapter<Ingredient> ingredientAdapter;
    private ArrayList<Ingredient> ingredientdataList;
    private ArrayAdapter<Recipe> recipeAdapter;
    private ArrayList<Recipe> recipedataList;
    private MealPlanFragment.OnFragmentInteractionListener listener;
    private ListView ItemList;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Ingredient newIngredient);
        void onOkPressedEdit(Ingredient newIngredient);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealPlanFragment.OnFragmentInteractionListener){
            listener = (MealPlanFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener ");
        }
    }

    /**
     Create new instance to make it possible to edit some food objects
     */
    public static MealPlanFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putSerializable("type", type);

        MealPlanFragment fragment = new MealPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mealplan, null);

        // initialize lists
        ItemList = view.findViewById(R.id.mealplan_list_frag);
        ingredientdataList = new ArrayList<>();
        recipedataList = new ArrayList<>();

        Bundle args = getArguments();
        String type = args.getString("type");
        assert(args != null);
        db = FirebaseFirestore.getInstance();
        // Get a top level reference to the collection
        final CollectionReference Collection = db.collection(type);
        if(type.equals("Ingredients")){
            TextView type_test = view.findViewById(R.id.addmeal_title);
            type_test.setText("Ingredients");
            ingredientAdapter = new CustomIngredientArrayAdapter(getActivity(), ingredientdataList, false);
            ItemList.setAdapter(ingredientAdapter);
            Collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc: task.getResult()) {
                            Log.d("Get Ingredient", String.valueOf(doc.getData().get("Description")));
                            String description = doc.getId();
                            String location = (String) doc.getData().get("Location");
                            String tempDate = (String) doc.getData().get("Date");
                            String count = (String) doc.getData().get("Count");
                            String unit = (String) doc.getData().get("Unit");
                            String category = (String) doc.getData().get("Category");

                            // Convert date string into Date class
                            Date date = null;
                            try {
                                date = new SimpleDateFormat("yyyy-mm-dd").parse(tempDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                            int countInt = Integer.parseInt(count);
                            // add ingredient to list
                            ingredientdataList.add(new Ingredient(description, date, location, countInt, unit, category));
                        }
                        ingredientAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
                    }
                }
            });
        } else {
            TextView type_test = view.findViewById(R.id.addmeal_title);
            type_test.setText("Recipes");
            recipeAdapter = new CustomRecipeArrayAdapter(getActivity(), recipedataList, false);
            ItemList.setAdapter(recipeAdapter);
            Collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        recipedataList.clear();
                        ArrayList<Ingredient> ingredientList = new ArrayList<>();
                        assert task.getResult() != null;
                        for(QueryDocumentSnapshot doc: task.getResult()) {
                            Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                            String title = doc.getId();
                            String prep_time = (String) doc.getData().get("Prep Time");
                            String servings = (String) doc.getData().get("Servings");
                            String comments = (String) doc.getData().get("Comments");
                            String category = (String) doc.getData().get("Category");
                            String picture =  (String) doc.getData().get("Picture");

                            CollectionReference ingredients = db.collection("Recipes")
                                    .document(title).collection("Ingredients");
                            ingredients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        for(QueryDocumentSnapshot ing : task.getResult()) {
                                            String description = ing.getId();
                                            String location = (String) ing.getData().get("Location");
                                            String tempDate = (String) ing.getData().get("Date");
                                            String count = (String) ing.getData().get("Count");
                                            String unit = (String) ing.getData().get("Unit");
                                            String category = (String) ing.getData().get("Category");
                                            // Convert date string into Date class
                                            Date date = null;
                                            try {
                                                date = new SimpleDateFormat("yyyy-mm-dd").parse(tempDate);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            int countInt = Integer.parseInt(count);
                                            // add ingredient to list
                                            ingredientList.add(new Ingredient(description, date, location, countInt, unit, category));
                                        }
                                    } else {
                                        Log.d("Update RECIPE", String.valueOf(doc.getData().get("Title")));
                                    }
                                }
                            });


                            //no need to parse count as in XML datatype is set to number (no decimals will be allowed)
                            int servInt = Integer.parseInt(servings);
                            int prepInt = Integer.parseInt(prep_time);

                            // add recipe to list
                            Recipe recipe = null;
                            try {
                                recipe = new Recipe(title, prepInt, servInt, picture,
                                        comments, category, ingredientList);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            recipedataList.add(recipe);
                        }
                        recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetches from the cloud
                    }
                }
            });
        }

        // onClick for selecting items from list. When item is selected, Edit Food pops up
        ItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(type.equals("Ingredients")){
                    Ingredient selectedIngredient = ingredientdataList.get(position);
                    Toast.makeText(getContext(), selectedIngredient.getDescription(), Toast.LENGTH_SHORT).show();
                    /*
                    ADD TO DATABASE FOR MEAL_INGREDIENTS HERE
                     */
                    getActivity().getSupportFragmentManager().beginTransaction().remove(MealPlanFragment.this).commit();
                } else {
                    Recipe selectedRecipe = recipedataList.get(position);
                    Toast.makeText(getContext(), selectedRecipe.getTitle(), Toast.LENGTH_SHORT).show();
                    /*
                    IMPLEMENT DATABASE FOR MEAL HERE
                     */
                    getActivity().getSupportFragmentManager().beginTransaction().remove(MealPlanFragment.this).commit();
                }
            }
        });
        return view;
    }
}
