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
    int unitCost;
    String[] locationOptions = {"pantry", "freezer", "fridge"};

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

    public void setLocation(String location) throws Exception {
        System.out.println(location);
        if (Arrays.asList(locationOptions).contains(location)) {
            this.location = location;
            System.out.println("yes");
        } else {
            throw new Exception("Invalid location.");
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(int count) throws Exception {
        if (count > 0) {
            this.count = count;
        } else {
            throw new Exception("Invalid count.");
        }

    }

    public Integer getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double cost) throws Exception {
        if (cost > 0) {
            this.unitCost = (int) Math.ceil(cost);
        } else {
            throw new Exception("Invalid unit cost.");
        }

    }

    public Integer getTotal() {
        return (this.unitCost * this.count);
    }

    public Ingredient(String description, Date bestBeforeDate, String location, int count, int unitCost) throws Exception {
        setDescription(description);
        setBestBeforeDate(bestBeforeDate);
        setLocation(location);
        setCount(count);
        setUnitCost(unitCost);
    }
}


