package com.example.foodcart.shoppingList;

import com.example.foodcart.ingredients.Ingredient;

import java.util.Date;

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

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }

    public Integer getOldcount() {
        return oldcount;
    }

    public void setOldcount(int oldcount) {
        this.oldcount = oldcount;
    }
}
