package com.example.foodcart.shoppingList;

import com.example.foodcart.ingredients.Ingredient;

import java.util.Date;

public class ShoppingItem extends Ingredient {

    /**
     * The constructor of the Ingredient class. Creates a new
     * ingredient with the params passed.
     *
     * @param description
     * @param count
     * @param unit
     * @param category
     */
    public ShoppingItem(String description, int count, String unit, String category) {
        super(description, null, null, count, unit, category);
    }
}
