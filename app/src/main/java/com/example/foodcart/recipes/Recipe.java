package com.example.foodcart.recipes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.foodcart.ingredients.Ingredient;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Recipe object with the title, prep time, number of servings,
 * comments, picture, and category of recipe
 */
public class Recipe implements Serializable {
    String title;
    int prep_time;
    int servings;
    String comments;
    String category;
    ArrayList<Ingredient> ingredientList;
    String picture;

    /**
     * Gets the title of the recipe
     * @return the title of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the recipe
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the prep time of the recipe
     * @return the prep time of the recipe
     */
    public int getPrep_time() {
        return prep_time;
    }

    /**
     * Sets the prep time of the recipe
     * @param prep_time
     */
    public void setPrep_time(int prep_time) {
        this.prep_time = prep_time;
    }

    /**
     * Gets the number of servings
     * @return the number of servings for this recipe
     */
    public int getServings() {
        return servings;
    }

    /**
     * Sets the number of servings for this recipe
     * @param servings
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Gets the recipe comments
     * @return recipe comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments for the recipe
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets the category of the recipe
     * @return the category of the recipe
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the recipe
     * @param category
     */
    public void setCategory(String category){
        this.category = category;
    }

    /**
     * Gets the list of ingredients for this recipe
     * @return the list of ingredients for this recipe
     */
    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    /**
     * Sets the list of ingredients for this recipe
     * @param ingredientList
     */
    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    /**
     * Sets the picture for the recipe
     * @param picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Gets the picture for the recipe
     * @return the picture of the recipe
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Gets the picture for the recipe (Bitmap Formatted)
     * @return the picture of the recipe
     */
    public Bitmap getBitmapPicture() {
        return stringToBitmap(picture);
    }

    /**
     * Adds a single ingredient to the
     * @param ingredient
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredientList.add(ingredient);
    }

    /**
     * Removes an ingredient from the list of ingredients for this recipe
     * @param ingredient
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredientList.remove(ingredient);
    }

    /**
     * Converts encoded string value to picture
     * @param picture
     * @return
     * picture Bitmap
     */
    public Bitmap stringToBitmap(String picture) {
        byte[] decodedImage = Base64.decode(picture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
    }

    /**
     * Constructor for recipe
     * @param title
     * @param prep_time
     * @param servings
     * @param comments
     * @param picture
     * @param category
     * @param ingredients
     */
    public Recipe(String title, int prep_time, int servings, String comments,
                  String picture, String category, ArrayList<Ingredient> ingredients)  {
        setTitle(title);
        setPrep_time(prep_time);
        setServings(servings);
        setComments(comments);
        setPicture(picture);
        setCategory(category);
        setIngredientList(ingredients);
    }
}