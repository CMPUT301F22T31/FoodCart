package com.example.foodcart.mealplans;

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.recipes.Recipe;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Recipe object with the title, prep time, number of servings,
 * comments, picture, and category of recipe
 */
public class Meal implements Serializable {
    String mealName;
    int scale;

    public Meal(String mealName, int scale) {
        this.mealName = mealName;
        this.scale = scale;
    }
}