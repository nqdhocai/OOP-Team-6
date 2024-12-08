package core;

import model.Nodes;
import model.Tweet;
import model.User;

import java.util.List;
public class DataController {
    Nodes nodes = new Nodes(String.valueOf(getClass().getResource("/data/raw_data.json").getPath()));
    List<User> users = nodes.getUsers();
    List<Tweet> tweets = nodes.getTweets();

    public Nodes getNodes() {
        return nodes;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }
}
