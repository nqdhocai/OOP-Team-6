package model;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Nodes {
    private List<User> users;
    private List<Tweet> tweets;

    // Getters and Setters
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
    public List<Tweet> getTweets() { return tweets; }
    public void setTweets(List<Tweet> tweets) { this.tweets = tweets; }

    // Constructor with load functionality
    public Nodes(String filePath) {
        loadDataFromFile(filePath);
    }

    public Nodes(){
        String filePath = "src/main/resources/data/raw_data.json";
        loadDataFromFile(filePath);
    }

    // Method to load data from JSON file
    private void loadDataFromFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load JSON data into the Nodes object
            Nodes nodes = objectMapper.readValue(new File(filePath), Nodes.class);
            this.users = nodes.getUsers();
            this.tweets = nodes.getTweets();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading data from file: " + filePath);
        }
    }
}