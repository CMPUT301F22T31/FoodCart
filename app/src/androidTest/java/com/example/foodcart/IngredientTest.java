package com.example.foodcart;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodcart.ingredients.IngredientActivity;
import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


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
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
    }

    /**
     * Test for adding an ingredient with good fields
     */
    @Test
    public void AddIngredient(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "Kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");

        assertTrue(solo.waitForText("Chicken", 1, 2000));
    }


    /**
     * Test for adding with a bad count value that should not add an ingredient
     */
    @Test
    public void AddIngredient_BadCount(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "five");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");

        assertFalse(solo.searchText("Chicken"));
    }

    /**
     * Test for adding with a blank description
     */
    @Test
    public void AddIngredient_BlankDesc(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");

        assertFalse(solo.searchText("EmptyString"));
    }

    /**
     * Test for adding with a blank location
     */
    @Test
    public void AddIngredient_BlankLocation(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");

        assertFalse(solo.searchText("Chicken"));
    }

    /**
     * Test for adding with a blank date
     */
    @Test
    public void AddIngredient_BlankDate(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "Fridge");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");
        assertFalse(solo.searchText("Chicken"));
    }

    /**
     * Test for adding with a blank count
     */
    @Test
    public void AddIngredient_BlankCount(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "g");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");

        assertFalse(solo.searchText("Chicken"));
    }

    /**
     * Test for adding with a blank unit
     */
    @Test
    public void AddIngredient_BlankUnit(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();

        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "Meat");
        solo.clickOnButton("Add");

        assertFalse(solo.searchText("Chicken"));
    }

    /**
     * Test for adding an ingredient with good fields
     */
    @Test
    public void AddIngredient_BlankCategory(){
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        removeAllElements();
        solo.clickOnView(solo.getView(R.id.add_ingredient_button));

        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Chicken");
        solo.clickOnView(solo.getView(R.id.calendarButton));
        solo.clickOnView(solo.getView(R.id.floatingActionButton));
        solo.enterText((EditText) solo.getView(R.id.ingredientLocationET), "pantry");
        solo.enterText((EditText) solo.getView(R.id.ingredientCountET), "5");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitET), "kg");
        solo.enterText((EditText) solo.getView(R.id.ingredientCategoryET), "");
        solo.clickOnButton("Add");

        assertFalse(solo.searchText("Chicken"));
    }

    /**
     * Test for deleting an ingredient from the ingredients list
     */
    @Test
    public void DeleteIngredient(){
        AddIngredient();

        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            View child = mylist.getChildAt(i);
            TextView ing_name = (TextView)child.findViewById(R.id.ingredient_item_name);
            if(ing_name.getText().toString().equals("Chicken")){
                solo.clickOnImageButton(i);
            }
        }

        assertFalse(solo.searchText("Fish"));
    }

    /**
     * Test for editing an ingredient from the ingredients list
     */
    @Test
    public void EditIngredient(){
        AddIngredient();
        solo.clickOnText("Chicken");
        solo.clearEditText((EditText) solo.getView(R.id.ingredientDescriptionET));
        solo.enterText((EditText) solo.getView(R.id.ingredientDescriptionET), "Fish");
        solo.clickOnButton("Edit");
        assertTrue(solo.waitForText("Fish", 1, 2000));
    }

    /**
     * removes all elements from the listview
     */
    private void removeAllElements() {
        ListView mylist = (ListView)solo.getView(R.id.ingredients_list);
        for(int i =0;i<mylist.getCount();i++){
            solo.clickOnImageButton(i);
        }
    }
}