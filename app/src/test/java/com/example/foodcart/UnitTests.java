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

    //As we don't have access to Context in our JUnit test classes, we need to mock it
    @Mock
    Context mMockContext;

    @Test
    public void IngredientUnitTest() throws ParseException {

        // Creates an object of Ingredient
        Ingredient testIngredient = new Ingredient("Banana",
                new Date(2023, 05, 26), "fridge",
                5, "", "fruit");

        assertEquals(testIngredient.getDescription(), "Banana");
        assertEquals(testIngredient.getBestBeforeDate(), new Date(2023, 5, 26));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(testIngredient.getFormattedBestBeforeDate(), formatter.format(formatter.parse("2023-05-26")));
        assertEquals(testIngredient.getLocation(), "fridge");
        assertEquals(testIngredient.getUnit(), "");
        assertEquals(testIngredient.getCount(), Integer.valueOf(5));
        assertEquals(testIngredient.getCategory(), "fruit");
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

        assertEquals(testRecipe.getTitle(), "Spaghetti with Meatballs");
        assertEquals(testRecipe.getPrep_time(), 10);
        assertEquals(testRecipe.getServings(), 4);
        assertEquals(testRecipe.getComments(), "Grandma's famous recipe");
        assertEquals(testRecipe.getPicture(), "spaghetti-pic");
        assertEquals(testRecipe.getIngredientList().get(0), spaghetti);
        assertEquals(testRecipe.getIngredientList().get(1), ground_beef);
        assertEquals(testRecipe.getIngredientList().get(2), sauce);
        assertEquals(testRecipe.getIngredientList().get(3), cheese);
    }

    @Test
    public void MealUnitTest() {

        Meal testMeal = new Meal("supper", 2);

    }
}