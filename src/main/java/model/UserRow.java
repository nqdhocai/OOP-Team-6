package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserRow {
    private final SimpleStringProperty username;
    private final SimpleDoubleProperty score;

    public UserRow(String username, double score) {
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleDoubleProperty(score);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public double getScore() {
        return score.get();
    }

    public SimpleDoubleProperty scoreProperty() {
        return score;
    }
}
