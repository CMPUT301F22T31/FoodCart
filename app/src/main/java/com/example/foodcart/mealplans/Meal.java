package com.example.foodcart.mealplans;

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.recipes.Recipe;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * the meal class is used for display and data storage purposes since it's essentially an abstraction
 * of ingredients and recipes. It contains only name, type, date, and scale(only for recipe).
 * @author Ahmed, Alfred
 * @version 1.0
 */
public class Meal implements Serializable {
    String mealName;
    String mealType;
    Date date;
    int scale;

    /**
     * contrutor for Meal class
     * @param mealName  the title/description of meal
     * @param mealType  if the meal is an ingredient or recipe
     * @param scale     the factor to scale a recipe by
     * @param date      the date the meal was selected for
     */
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