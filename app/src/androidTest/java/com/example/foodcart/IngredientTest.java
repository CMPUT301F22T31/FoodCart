package com.example.foodcart;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import com.example.foodcart.ingredients.IngredientActivity;
import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

public class IngredientTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<IngredientActivity> rule =
            new ActivityTestRule<>(IngredientActivity.class, true, true);

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
     */
    @Test
    public void AddCity(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test1");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertTrue(solo.waitForText("Test1", 1, 2000));

        assertTrue(solo.searchText("Test1"));
    }
}
