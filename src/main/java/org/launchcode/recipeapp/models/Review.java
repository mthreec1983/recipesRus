package org.launchcode.recipeapp.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Review extends AbstractEntity{

    @ManyToOne
    private Recipe recipe;

    @NotBlank(message = "name is required")
    @Size(min=2)
    private String name;

    @Size(min=5, max=250)
    private String comment;

    private Integer rating;
    private String timestamp;


    public Review() {
    }

    public Review(Recipe recipe, Integer rating, String comment, String name) {
        this.recipe = recipe;
        this.rating = rating;
        this.comment = comment;
        this.name = name;
    }

    // getters and setters
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        LocalDateTime timestampObj = LocalDateTime.now();
        DateTimeFormatter dateObj = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        DateTimeFormatter timeObj = DateTimeFormatter.ofPattern("h:mm a");
        String dateTime = timestampObj.format(dateObj) + " at " + timestampObj.format(timeObj);
        timestamp = dateTime;
    }


}
