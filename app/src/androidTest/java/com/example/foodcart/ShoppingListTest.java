package com.example.foodcart;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodcart.ingredients.IngredientActivity;
import com.example.foodcart.mealplans.MealPlanActivity;
import com.example.foodcart.recipes.RecipeActivity;
import com.example.foodcart.shoppingList.ShoppingListActivity;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ShoppingListTest {
    private Solo solo;

    /**
     *  Sets the activity that we will be on
     */
    @Rule
    public ActivityTestRule<ShoppingListActivity> rule =
            new ActivityTestRule<>(ShoppingListActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
    }

    /**
     * Tests adding to shopping list if there is a meal (from Ingredients)
     * and not enough ingredients in storage
     */
    @Test
    public void AddToShoppingIngredient() {
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
        removeAllElements();
        addIngredient();
        addRecipe();
        //add meal
        solo.clickOnView(solo.getView(R.id.mealplans_tab));
        solo.assertCurrentActivity("Did not switch", MealPlanActivity.class);
        solo.clickOnView(solo.getView(R.id.add_mealplan_button));
        solo.clickOnView(solo.getView(R.id.add_mealplan_recipe_button));
        solo.clickOnText("Vegan Burger");
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        assertTrue(solo.waitForText("Vegan Burger", 1, 2000));
        solo.clickOnView(solo.getView(R.id.shoppinglist_tab));
        solo.assertCurrentActivity("Did not switch", ShoppingListActivity.class);
        assertTrue(solo.waitForText("Vegan Patty", 1, 2000));
    }

    @Test
    public void AddToIngredientFromList() {
        AddToShoppingIngredient();
        ListView mylist = (ListView)solo.getView(R.id.shopping_list);
        for(int i =0;i<mylist.getCount();i++){
            View child = mylist.getChildAt(i);
            TextView ing_name = (TextView)child.findViewById(R.id.mealplan_name);
            if(ing_name.getText().toString().equals("Vegan Patty")){
                solo.clickOnCheckBox(i);
            }
        }
        solo.clickOnView(solo.getView(R.id.add_item_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Vegan Patty");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "Freezer");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Packet");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Vegan");
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("Vegan Patty", 1, 2000));
    }

    private void addIngredient() {
        //add ingredient
        solo.clickOnView(solo.getView(R.id.ingredients_tab));
        solo.assertCurrentActivity("Did not switch", IngredientActivity.class);
        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Vegan Patty");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "Freezer");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Packet");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Vegan");
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("Vegan Patty", 1, 2000));
    }

    private void addRecipe() {
        //add recipe
        solo.clickOnView(solo.getView(R.id.recipes_tab));
        solo.assertCurrentActivity("Did not switch", RecipeActivity.class);
        ListView mylist = (ListView)solo.getView(R.id.recipes_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Vegan Burger");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "25");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Vegan");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Vegan Patty");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "2");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Packets");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Vegan");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertTrue(solo.waitForText("Vegan Patty", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertTrue(solo.waitForText("Vegan Burger", 1, 2000));
    }

    /**
     * removes all elements from the listview
     */
    private void removeAllElements() {
        ListView mylist = (ListView)solo.getView(R.id.shopping_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
    }
}