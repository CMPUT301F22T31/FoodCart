package com.example.foodcart.recipes;

import com.example.foodcart.ingredients.Ingredient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Recipe {
    String title;
    int prep_time;
    int servings;
    String comments;
    String category;
    ArrayList<Ingredient> ingredientList;
    String picture = "PICTURE";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title.substring(0, Math.min(30, title.length()));
        this.title = title;
    }

    public int getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(int prep_time) throws Exception {
        if (prep_time> 0) {
            this.prep_time = prep_time;
        } else {
            throw new Exception("Invalid prep time.");
        }
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) throws Exception {
        if (servings > 0) {
            this.servings = servings;
        } else {
            throw new Exception("Invalid servings.");
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        comments = comments.substring(0, Math.min(30, comments.length()));
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

    public void addIngredient(Ingredient ingredient) {
        this.ingredientList.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredientList.remove(ingredient);
    }

    public Recipe(String title, int prep_time, int servings, String comments, String category,
                  ArrayList<Ingredient> ingredients) throws Exception {
        setTitle(title);
        setPrep_time(prep_time);
        setServings(servings);
        setComments(comments);
        setCategory(category);
        setIngredientList(ingredients);
    }
}