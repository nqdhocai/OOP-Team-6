package core;

import model.Nodes;
import model.Tweet;
import model.User;

import java.util.List;
public class DataController {
    Nodes nodesData = new Nodes(getClass().getResource("/data/NodeData.json").getPath());
    List<User> users = nodesData.getUsers();
    List<Tweet> tweets = nodesData.getTweets();

    public Nodes getNodesData() {
        return nodesData;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }
}
