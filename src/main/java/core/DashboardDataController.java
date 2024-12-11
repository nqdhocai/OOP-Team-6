package core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Nodes;
import model.Tweet;
import model.User;

import java.util.Comparator;
import java.util.List;

public class DashboardDataController extends DataController {

    public DashboardDataController() {
        if (users != null) {
        users.sort(Comparator.comparingDouble(User::getScore).reversed());
        }
    }

    // Method to get the total number of users
    public int getTotalUsers() {
        return users != null ? users.size() : 0;
    }

    // Method to get the total number of tweets
    public int getTotalTweets() {
        return tweets != null ? tweets.size() : 0;
    }
}
