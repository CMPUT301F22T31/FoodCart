package com.example.foodcart.mealplans;

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.recipes.Recipe;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * TODO Recipe object with the title, prep time, number of servings,
 * comments, picture, and category of recipe
 */
public class Meal implements Serializable {
    String mealName;
    String mealType;
    Date date;
    int scale;

    public Meal(String mealName, String mealType, int scale, Date date) {
        this.mealName = mealName;
        this.mealType = mealType;
        this.scale = scale;
        this.date = date;
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

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    /**
     * Gets the best before date of an ingredient as a formatted string
     * @return the best before date of the ingredient as a formatted string
     */
    public String getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}