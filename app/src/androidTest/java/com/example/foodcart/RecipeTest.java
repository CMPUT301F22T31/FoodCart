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

import com.example.foodcart.recipes.RecipeActivity;
import com.robotium.solo.Solo;

import org.junit.After;
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
    }

    /**
     * Test the adding of recipes
     */
    @Test
    public void AddRecipe(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertTrue(solo.waitForText("RecipeTitle", 1, 2000));

    }

    /**
     * Test the adding of recipes with no pictures
     */
    @Test
    public void AddRecipeNoPicture(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertFalse(solo.waitForText("RecipeTitle"));
    }
    /**
     * Test the adding of recipes with empty Title
     */
    @Test
    public void AddRecipeEmptyTitle(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertFalse(solo.waitForText("RecipeTitle"));
    }
    /**
     * Test the adding of recipes with bad prepTime
     */
    @Test
    public void AddRecipeBadPrepTime(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "twenty");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertFalse(solo.waitForText("RecipeTitle"));
    }

    /**
     * Test the adding of recipes with bad number of servings
     */
    @Test
    public void AddRecipeBadServes(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "five");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertFalse(solo.waitForText("RecipeTitle"));
    }

    /**
     * Test the adding of recipes with empty Category
     */
    @Test
    public void AddRecipeEmptyCategory(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "five");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertFalse(solo.waitForText("RecipeTitle"));
    }

    /**
     * Test the adding of recipes with empty Comments
     */
    @Test
    public void AddRecipeEmptyComment(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "five");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        assertTrue(solo.waitForText("RecipeTitle", 1, 2000));
    }

    /**
     * Test the edit functionality of recipes
     */
    @Test
    public void EditRecipe(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertTrue(solo.waitForText("RecipeTitle", 1, 2000));

        solo.clickOnText("RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle2");
        assertTrue(solo.waitForText("RecipeTitle2", 1, 2000));
    }

    /**
     * Test the deletion of recipes
     */
    @Test
    public void DeleteRecipe(){
        solo.assertCurrentActivity("Wrong Activity", RecipeActivity.class);

        solo.clickOnView(solo.getView(R.id.add_recipe_button));
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        //REQUIREMENT: A picture is required in gallery and should be manually selected
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle2");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertTrue(solo.waitForText("RecipeTitle2", 1, 2000));

        ListView mylist = (ListView)solo.getView(R.id.recipes_list);
        int pos;
        for(int i =0;i<mylist.getCount();i++){
            View child = mylist.getChildAt(i);
            TextView ing_name = (TextView)child.findViewById(R.id.recipe_name);
            if(ing_name.getText().toString().equals("RecipeTitle2")){
                solo.clickOnImageButton(i);
            }
        }

        assertFalse(solo.searchText("RecipeTitle2"));
    }
    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
