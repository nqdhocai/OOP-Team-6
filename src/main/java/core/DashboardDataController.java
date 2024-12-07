package core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Nodes;
import model.Tweet;
import model.User;
import model.UserRow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardDataController {
    private final String dataPath = String.valueOf(getClass().getResource("/data/raw_data.json").getPath());

    final private Nodes data = new Nodes(dataPath);
    final private List<User> users = data.getUsers();
    final private List<Tweet> tweets = data.getTweets();

    public DashboardDataController() {
        if (!users.isEmpty()) {
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

    // Method to get the total number of nodes (users + tweets)
    public int getTotalNodes() {
        return getTotalUsers() + getTotalTweets();
    }

    public ObservableList<User> getUserRows() {
        return FXCollections.observableList(users);
    }

    public List<User> getUsers() {return this.users;}
    public List<Tweet> getTweets() {return this.tweets;}
}
