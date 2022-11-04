package com.example.foodcart.ingredients;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Ingredient object with the description, best before date, location,
 * count, units (for count), and category an ingredient
 */
public class Ingredient implements Serializable {
    private String description;
    private Date bestBeforeDate;
    private String location;
    private int count;
    private String unit;
    private String category;

    /**
     * Gets the description of an ingredient
     * @return the description of the ingredient
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of an ingredient
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the best before date of an ingredient
     * @return the best before date of the ingredient
     */
    public Date getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * Sets the best before date of an ingredient
     * @param bestBeforeDate
     */
    public void setBestBeforeDate(Date bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * Gets the best before date of an ingredient as a formatted string
     * @return the best before date of the ingredient as a formatted string
     */
    public String getFormattedBestBeforeDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(bestBeforeDate);
    }

    /**
     * Gets the location of an ingredient
     * @return the location of the ingredient
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the ingredient
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;

    }

    /**
     * Gets the count or quantity of the ingredient
     * @return the quantity of the ingredient
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the quantity or count of the ingredient
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets the unit of an ingredient
     * @return the unit of the ingredient
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of the ingredient
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the category of the ingredient
     * @return the category of the ingredient
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the ingredient
     * @param category
     */
    public void setCategory(String category){
        this.category = category;

    }

    /**
     * The constructor of the Ingredient class. Creates a new
     * ingredient with the params passed.
     * @param description
     * @param bestBeforeDate
     * @param location
     * @param count
     * @param unit
     * @param category
     */
    public Ingredient(String description, Date bestBeforeDate, String location, int count, String unit, String category) {
        setDescription(description);
        setBestBeforeDate(bestBeforeDate);
        setLocation(location);
        setCount(count);
        setUnit(unit);
        setCategory(category);
    }
}


