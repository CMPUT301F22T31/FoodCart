package com.example.foodcart;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.example.foodcart.ingredients.Ingredient;
import com.example.foodcart.mealplans.Meal;
import com.example.foodcart.recipes.Recipe;

import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnitTests {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    //As we don't have access to Context in our JUnit test classes, we need to mock it
    @Mock
    Context mMockContext;

    @Test
    public void IngredientUnitTest() throws ParseException {

        // Creates an object of Ingredient
        Ingredient testIngredient = new Ingredient("Banana",
                new Date(2023, 05, 26), "fridge",
                5, "", "fruit");

        assertEquals("Banana", testIngredient.getDescription());
        assertEquals(new Date(2023, 5, 26), testIngredient.getBestBeforeDate());
        assertEquals(formatter.format(formatter.parse("2023-05-26")), testIngredient.getFormattedBestBeforeDate());
        assertEquals("fridge", testIngredient.getLocation());
        assertEquals("", testIngredient.getUnit());
        assertEquals(Integer.valueOf(5), testIngredient.getCount());
        assertEquals("fruit", testIngredient.getCategory());
    }

    @Test
    public void RecipeUnitTest() {

        Ingredient spaghetti = new Ingredient("Spaghetti",
                new Date(2020, 8, 24), "pantry",
                1, "bag", "pasta");
        Ingredient ground_beef = new Ingredient("Ground Beef",
                new Date(2023, 1, 14), "freezer",
                2, "packs", "meat");
        Ingredient sauce = new Ingredient("Spaghetti Sauce",
                new Date(2024, 5, 1), "pantry",
                4, "jars", "sauces");
        Ingredient cheese = new Ingredient("Shredded Cheese",
                new Date(2025, 2, 9), "fridge",
                1, "bag", "cheese");

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(spaghetti);
        ingredients.add(ground_beef);
        ingredients.add(sauce);
        ingredients.add(cheese);

        Recipe testRecipe = new Recipe("Spaghetti with Meatballs",
                10, 4,
                "Grandma's famous recipe", "spaghetti-pic", "pasta", ingredients);

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

    @Test
    public void MealUnitTest() throws ParseException {

        Meal testMeal = new Meal("Spaghetti with Meatballs", "supper", 2, new Date(2022, 12, 24));

        assertEquals("Spaghetti with Meatballs", testMeal.getMealName());
        assertEquals("supper", testMeal.getMealType());
        assertEquals(2, testMeal.getScale());
        assertEquals(formatter.format(formatter.parse("2022-12-24")), testMeal.getDate());

    }
}