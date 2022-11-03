package com.example.foodcart.recipes;

import com.example.foodcart.ingredients.Ingredient;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Recipe implements Serializable {
    String title;
    int prep_time;
    int servings;
    String comments;
    String category;
    ArrayList<Ingredient> ingredientList;
    String picture;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(int prep_time) {
        this.prep_time = prep_time;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredientList.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredientList.remove(ingredient);
    }

    public Recipe(String title, int prep_time, int servings, String comments, String picture, String category,
                  ArrayList<Ingredient> ingredients)  {
        setTitle(title);
        setPrep_time(prep_time);
        setServings(servings);
        setComments(comments);
        setPicture(picture);
        setCategory(category);
        setIngredientList(ingredients);
    }
}
