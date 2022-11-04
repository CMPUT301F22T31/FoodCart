package com.example.foodcart;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.media.Image;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.foodcart.recipes.RecipeActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

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
        solo.takeScreenshot();
        solo.clickOnView(solo.getView(R.id.recipeImgUploadButton));
        solo.clickOnImage(0);
        solo.enterText((EditText) solo.getView(R.id.recipeTitleET), "RecipeTitle");
        solo.enterText((EditText) solo.getView(R.id.recipePrepareTimeET), "20");
        solo.enterText((EditText) solo.getView(R.id.recipeServingsET), "5");
        solo.enterText((EditText) solo.getView(R.id.recipeCategoryET), "Breakfast");
        solo.enterText((EditText) solo.getView(R.id.recipeCommentsET), "This is a comment");
        solo.clickOnButton("Add"); //Select CONFIRM Button
        //no data added as photo was missing
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
