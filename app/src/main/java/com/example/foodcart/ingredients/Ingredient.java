package com.example.foodcart.ingredients;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

class Ingredient implements Serializable {
    String description;
    Date bestBeforeDate;
    String location;
    int count;
    String unit;
    String category;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description.substring(0, Math.min(30, description.length()));
        this.description = description;
    }

    public Date getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(Date bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public String formattedBestBeforeDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(bestBeforeDate);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;

    }

    public Integer getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category){
        this.category = category;

    }

    public Ingredient(String description, Date bestBeforeDate, String location, int count, String unit, String category) {
        setDescription(description);
        setBestBeforeDate(bestBeforeDate);
        setLocation(location);
        setCount(count);
        setUnit(unit);
        setCategory(category);
    }
}


