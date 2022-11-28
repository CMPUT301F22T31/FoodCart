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
import com.example.foodcart.recipes.RecipeActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class RecipeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RecipeActivity> rule =
            new ActivityTestRule<>(RecipeActivity.class, true, true);

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
    public void start() throws Exception{
        Activity activity = rule.getActivity();
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
    }

    /**
     * Test the adding of recipes
     */
    @Test
    public void AddRecipe(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
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
        solo.clickOnButton("Add");
        //check if ingredients are added
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add");
        //check if recipe is added
        assertTrue(solo.waitForText("Butter Chicken", 1, 2000));
    }

    /**
     * Test the adding of recipes with no pictures
     */
    @Test
    public void AddRecipeNoPicture(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
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
        solo.clickOnButton("Add");
        //check if ingredient added
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add");
        //recipe should not be added
        assertFalse(solo.waitForText("Butter Chicken"));
    }
    /**
     * Test the adding of recipes with empty Title
     */
    @Test
    public void AddRecipeEmptyTitle(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Indian");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");
        //check if ingredient added
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add");
        //recipe should not be added
        assertFalse(solo.waitForText("EmptyString"));
    }
    /**
     * Test the adding of recipes with bad prepTime
     */
    @Test
    public void AddRecipeBadPrepTime(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Butter Chicken");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "twenty");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Indian");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");
        //check if ingredient added
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add");
        //recipe should not be added
        assertFalse(solo.waitForText("Butter Chicken"));
    }

    /**
     * Test the adding of recipes with bad number of servings
     */
    @Test
    public void AddRecipeBadServes(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Butter Chicken");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "five");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Indian");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");
        //check if ingredient added
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add");
        //recipe should not be added
        assertFalse(solo.waitForText("Butter Chicken"));
    }

    /**
     * Test the adding of recipes with empty Category
     */
    @Test
    public void AddRecipeEmptyCategory(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Butter Chicken");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "1");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");
        //check if ingredient added
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Add");
        //recipe should not be added
        assertFalse(solo.waitForText("Butter Chicken"));
    }

    @Test
    public void NoIngredient() {
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);
        removeAllElements();
        //add recipe with given details
        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT HIT THE CAMERA BUTTON AND CONFIRM THE IMAGE
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Butter Chicken");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Indian");
        solo.clickOnButton("Add");
        //recipe should not be added
        assertFalse(solo.waitForText("Butter Chicken"));
    }


    /**
     * Test the edit functionality of recipes
     */
    @Test
    public void EditRecipe(){
        //add recipe to be edited
        AddRecipe();
        //edit the added recipe details
        solo.clickOnText("Butter Chicken");
        solo.clearEditText((EditText) solo.getView(R.id.recipeTitleET));
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "Chicken Tikka Masala");
        solo.clickOnView(solo.getView(R.id.recipeIngredientButton));
        solo.clickOnText("Chicken");
        solo.clearEditText((EditText) solo.getView(R.id.ingredientCountET));
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "2");
        solo.clickOnButton("Edit");
        //check if the edited ingredient exist
        assertTrue(solo.waitForText("Chicken", 1, 2000));
        solo.clickOnView(solo.getView(R.id.ingredientsDone));
        solo.clickOnButton("Edit");
        //check if recipe edited
        assertTrue(solo.waitForText("Chicken Tikka Masala", 1, 2000));
    }

    /**
     * Test the deletion of recipes
     */
    @Test
    public void DeleteRecipe(){
        //add recipe to be deleted
        AddRecipe();
        //delete this particular recipe
        ListView mylist = (ListView)solo.getView(R.id.recipes_list);
        for(int i =0;i<mylist.getCount();i++){
            View child = mylist.getChildAt(i);
            TextView ing_name = (TextView)child.findViewById(R.id.recipe_name);
            if(ing_name.getText().toString().equals("Butter Chicken")){
                solo.clickOnImageButton(i);
            }
        }
        //recipe should no longer exist
        assertFalse(solo.searchText("Butter Chicken"));
    }

    /**
     * removes all elements from the listview
     */
    private void removeAllElements() {
        ListView mylist = (ListView)solo.getView(R.id.recipes_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
    }
}
