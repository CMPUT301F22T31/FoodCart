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
    String mealType;
    int scale;

    public Meal(String mealName, String mealType, int scale) {
        this.mealName = mealName;
        this.mealType = mealType;
        this.scale = scale;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}