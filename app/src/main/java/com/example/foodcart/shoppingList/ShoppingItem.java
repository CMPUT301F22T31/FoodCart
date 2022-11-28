package com.example.foodcart.shoppingList;

import com.example.foodcart.ingredients.Ingredient;

import java.util.Date;

/**
 * shopping item class for shoppingList functionality
 *
 * @author Ashley
 * @version 1.0
 */
public class ShoppingItem extends Ingredient {

    private boolean Checked = false;
    private int oldcount;

    /**
     * The constructor of the Ingredient class. Creates a new
     * ingredient with the params passed.
     *
     * @param description
     * @param count
     * @param unit
     * @param category
     */
    public ShoppingItem(String description, int count, int oldcount, String unit, String category) {
        super(description, null, null, count, unit, category);
        this.oldcount = oldcount;
    }

    /**
     * Returns true if the item is checked
     * @return
     */
    public boolean isChecked() {
        return Checked;
    }

    /**
     * Change the value of checked for the item
     * @return
     */
    public void setChecked(boolean checked) {
        Checked = checked;
    }

    /**
     * Get the old count
     * @return
     */
    public Integer getOldcount() {
        return oldcount;
    }

    /**
     * Sets the old count
     * @return
     */
    public void setOldcount(int oldcount) {
        this.oldcount = oldcount;
    }
}
