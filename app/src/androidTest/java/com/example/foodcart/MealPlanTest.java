package com.example.foodcart;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MealPlanTest {
    private Solo solo;

    /**
     *  Sets the activity that we will be on
     */
    @Rule
    public ActivityTestRule<MealPlanActivity> rule =
            new ActivityTestRule<>(MealPlanActivity.class, true, true);

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
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
    }

    /**
     * Tests adding of Meal from a Recipe
     */
    @Test
    public void AddMealRecipe() {
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
        removeAllElements();
        solo.clickOnView(solo.getView(R.id.recipes_tab));
        solo.assertCurrentActivity("Did not switch", RecipeActivity.class);
        ListView list = (ListView)solo.getView(R.id.recipes_list);
        for(int i =0;i<list.getCount();i++){
            solo.clickOnImageButton(i);
        }
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Butter Chicken");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Indian");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertTrue(solo.waitForText("Butter Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.mealplans_tab));
        solo.assertCurrentActivity("Did not switch", MealPlanActivity.class);
        solo.clickOnView(solo.getView(R.id.add_mealplan_button));
        solo.clickOnView(solo.getView(R.id.add_mealplan_recipe_button));
        solo.clickOnText("Butter Chicken");
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        assertTrue(solo.waitForText("Butter Chicken", 1, 2000));
    }

    /**
     * Tests adding of Meal from an ingredient
     */
    @Test
    public void AddMealIngredient() {
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
        removeAllElements();
        solo.clickOnView(solo.getView(R.id.ingredients_tab));
        solo.assertCurrentActivity("Did not switch", IngredientActivity.class);
        ListView list = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<list.getCount();i++){
            solo.clickOnImageButton(i);
        }
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Rice");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Rice");
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("Rice", 1, 2000));
        solo.clickOnView(solo.getView(R.id.mealplans_tab));
        solo.assertCurrentActivity("Did not switch", MealPlanActivity.class);
        solo.clickOnView(solo.getView(R.id.add_mealplan_button));
        solo.clickOnView(solo.getView(R.id.add_mealplan_ingredient_button));
        solo.clickOnText("Rice");
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        assertTrue(solo.waitForText("Rice", 1, 2000));
    }

    /**
     * Rescale meal from an Recipe
     */
    @Test
    public void RescaleMealRecipe() {
        AddMealRecipe();
        solo.clickOnText("Butter Chicken");
        solo.clearEditText((EditText) solo.getView(R.id.mealscaleET));
        solo.enterText((EditText) solo.getView(R.id.mealscaleET), "5");
        solo.clickOnButton("Edit");
        solo.clickOnText("Butter Chicken");
        assertTrue(solo.waitForText("5", 1, 2000));
    }

    /**
     * Test the deletion of Meals
     */
    @Test
    public void DeleteMeal(){
        AddMealIngredient();
        ListView mylist = (ListView)solo.getView(R.id.mealplan_list);
        for(int i =0;i<mylist.getCount();i++){
            View child = mylist.getChildAt(i);
            TextView ing_name = (TextView)child.findViewById(R.id.mealplan_name);
            if(ing_name.getText().toString().equals("Rice")){
                solo.clickOnImageButton(i);
            }
        }
        assertFalse(solo.searchText("Rice"));
    }

    /**
     * removes all elements from the listview
     */
    private void removeAllElements() {
        ListView mylist = (ListView)solo.getView(R.id.mealplan_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
    }

}
