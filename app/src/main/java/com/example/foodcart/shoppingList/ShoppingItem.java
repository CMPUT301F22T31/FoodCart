package com.example.foodcart.shoppingList;

import android.widget.Toast;

import com.example.foodcart.ingredients.Ingredient;

import java.util.Date;

public class ShoppingItem extends Ingredient {

    /**
     * The constructor of the ShoppingItem class. Creates a new
     * shopping list item with the params passed.
     * @param description
     * @param count
     * @param unit
     * @param category
     */
    public ShoppingItem(String description, int count, String unit, String category) {
        super(description, null, null, count, unit, category);
    }

    /**
     * Gets the best before date of an ingredient
     * @return the best before date of the ingredient
     */
    public Ingredient convertToIngredient(Date bestbeforedate, String location) {
        if (!emptyStringCheck(getDescription(), location, getCount().toString(), getCategory())) {
            Ingredient ingredient = new Ingredient(getDescription(), bestbeforedate, location, getCount(), getUnit(), getCategory());
            return ingredient;
        } else {
            return null;
        }
    }

    private boolean emptyStringCheck(String description, String location, String count, String category) {
        boolean emptyStringExist = false;
        if(description.isEmpty()) {
            emptyStringExist = true;
        }
        else if(location.isEmpty()) {
            emptyStringExist = true;
        }
        else if(count.isEmpty()) {
            emptyStringExist = true;
        }
        else if(category.isEmpty()) {
            emptyStringExist = true;
        }
        return emptyStringExist;
    }
}
