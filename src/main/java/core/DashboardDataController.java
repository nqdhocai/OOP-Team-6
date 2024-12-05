package core;

import model.Nodes;
import model.Tweet;
import model.User;

import java.util.List;

public class DashboardDataController {
    final private Nodes data = new Nodes();
    final private List<User> users = data.getUsers();
    final private List<Tweet> tweets = data.getTweets();

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

    public List<User> getUsers() {return this.users;}
    public List<Tweet> getTweets() {return this.tweets;}
}