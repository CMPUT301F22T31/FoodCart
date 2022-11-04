package com.example.foodcart;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

    /**
     *  Sets the activity that we will be on
     */
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
     * Test for adding an ingredient with good fields
     */
    @Test
    public void AddIngredient(){
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

    /**
     * Test for adding with a bad date value that should not add an ingredient
     */
    @Test
    public void AddIngredient_BadDate(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test2");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "00000000");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test2"));
    }

    /**
     * Test for adding with a bad count value that should not add an ingredient
     */
    @Test
    public void AddIngredient_BadCount(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test3");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "five");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test3"));
    }

    /**
     * Test for adding with a blank description
     */
    @Test
    public void AddIngredient_BlankDesc(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("5"));
    }

    /**
     * Test for adding with a blank location
     */
    @Test
    public void AddIngredient_BlankLocation(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test5");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test5"));
    }

    /**
     * Test for adding with a blank date
     */
    @Test
    public void AddIngredient_Blankdate(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test6");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test6"));
    }

    /**
     * Test for adding with a blank count
     */
    @Test
    public void AddIngredient_BlankCount(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test7");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test7"));
    }

    /**
     * Test for adding with a blank unit
     */
    @Test
    public void AddIngredient_BlankUnit(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test8");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test8"));
    }

    /**
     * Test for adding an ingredient with good fields
     */
    @Test
    public void AddIngredient_BlankCategory(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test9");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "");
        solo.clickOnButton("Add"); //Select CONFIRM Button

        assertFalse(solo.searchText("Test9"));
    }

    /**
     * Test for deleting an ingredient from the ingredients list
     */
    @Test
    public void DeleteIngredient(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Test10");
        solo.enterText((EditText) solo.getView(R.id.ingredientBestBeforeDateET), "2022-01-21");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Test");
        solo.clickOnButton("Add");

        assertTrue(solo.waitForText("Test10", 1, 2000));

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            View child = mylist.getChildAt(i);
            TextView ing_name = (TextView)child.findViewById(R.id.ingredient_item_name);
            if(ing_name.getText().toString().equals("Test10")){
                solo.clickOnImageButton(i);
            }
        }

        assertFalse(solo.searchText("Test10"));
    }
}
