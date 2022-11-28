package com.example.foodcart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.mealplans.Meal;
import com.example.foodcart.recipes.Recipe;
import com.example.foodcart.shoppingList.ShoppingItem;

import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnitTests {

    /**
     * Test the Ingredient Class
     */
    @Test
    public void IngredientUnitTest() {

        // Creates an object of Ingredient
        String dateString = "2023-05-26";
        Date date = stringToDate(dateString);
        Ingredient testIngredient = new Ingredient("Banana",
                date, "fridge",
                5, "Units", "fruit");

        //checks if the details are correct
        assertEquals("Banana", testIngredient.getDescription());
        assertEquals(date, testIngredient.getBestBeforeDate());
        assertEquals("fridge", testIngredient.getLocation());
        assertEquals("Units", testIngredient.getUnit());
        assertEquals(Integer.valueOf(5), testIngredient.getCount());
        assertEquals("fruit", testIngredient.getCategory());
    }
    /**
     * Test the Recipe Class
     */
    @Test
    public void RecipeUnitTest() {
        //create ingredients
        String dateString = "2020-08-24";
        Date date = stringToDate(dateString);
        Ingredient spaghetti = new Ingredient("Spaghetti",
                date, "pantry",
                1, "bag", "pasta");
        dateString = "2023-01-14";
        date = stringToDate(dateString);
        Ingredient ground_beef = new Ingredient("Ground Beef",
                date, "freezer",
                2, "packs", "meat");
        dateString = "2024-05-01";
        date = stringToDate(dateString);
        Ingredient sauce = new Ingredient("Spaghetti Sauce",
                date, "pantry",
                4, "jars", "sauces");
        dateString = "2025-02-09";
        date = stringToDate(dateString);
        Ingredient cheese = new Ingredient("Shredded Cheese",
                date, "fridge",
                1, "bag", "cheese");

        //add them to arraylist
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(spaghetti);
        ingredients.add(ground_beef);
        ingredients.add(sauce);
        ingredients.add(cheese);
        //create new Recipe Object
        Recipe testRecipe = new Recipe("Spaghetti with Meatballs",
                10, 4,
                "Grandma's famous recipe", "spaghetti-pic", "pasta", ingredients);

        //check if details are correct
        assertEquals("Spaghetti with Meatballs", testRecipe.getTitle());
        assertEquals(10, testRecipe.getPrep_time());
        assertEquals(4, testRecipe.getServings());
        assertEquals("Grandma's famous recipe", testRecipe.getComments());
        assertEquals("spaghetti-pic", testRecipe.getPicture());
        assertEquals(spaghetti, testRecipe.getIngredientList().get(0));
        assertEquals(ground_beef, testRecipe.getIngredientList().get(1));
        assertEquals(sauce, testRecipe.getIngredientList().get(2));
        assertEquals(cheese, testRecipe.getIngredientList().get(3));
    }

    /**
     * Test the Meal Plan Class
     */
    @Test
    public void MealUnitTest() {
        //create a Meal Object
        String dateString = "2022-12-24";
        Date date = stringToDate(dateString);
        Meal testMeal = new Meal("Spaghetti with Meatballs", "supper", 2, date);
        //check if details are correct
        assertEquals("Spaghetti with Meatballs", testMeal.getMealName());
        assertEquals("supper", testMeal.getMealType());
        assertEquals(2, testMeal.getScale());
        assertEquals(date, testMeal.getDate());

    }

    /**
     * Tests the Shopping List
     */
    @Test
    public void ShoppingItemUnitTest() {

        //creates an object of Shopping Item
        ShoppingItem testItem = new ShoppingItem("Apples", 2, 2,"bags", "fruit");
        //check if details are correct
        assertEquals("Apples", testItem.getDescription());
        assertNull(testItem.getBestBeforeDate());
        assertNull(testItem.getLocation());
        assertEquals("bags", testItem.getUnit());
        assertEquals(Integer.valueOf(2), testItem.getCount());
        assertEquals("fruit", testItem.getCategory());
    }

    /**
     * Converts string value of date to Date Object
     * @param date
     * @return
     */
    private Date stringToDate(String date) {
        Date formattedDate = null;
        try {
            formattedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}