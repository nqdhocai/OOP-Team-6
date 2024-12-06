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
    final private Nodes data = new Nodes();
    final private List<User> users = data.getUsers();
    final private List<Tweet> tweets = data.getTweets();

    public DashboardDataController() {
        users.sort(Comparator.comparingDouble(User::getScore).reversed());
        users.sort(Comparator.comparingDouble(User::getScore).reversed());
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

    public ObservableList<UserRow> getUserRows() {
        List<UserRow> userRows = new ArrayList<UserRow>();
        for (User user : users) {
            userRows.add(new UserRow(user.getUsername(), user.getScore()));
        }
        return FXCollections.observableList(userRows);
    }

    public List<User> getUsers() {return this.users;}
    public List<Tweet> getTweets() {return this.tweets;}
}
