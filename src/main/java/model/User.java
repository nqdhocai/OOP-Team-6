package model;

import java.util.List;

public class User {
    private String userId;
    private String userName;
    private String bio;
    private String profileUrl;
    private String profileImageUrl;
    private String followersCount;
    private String followingCount;
    private int tweetCount;
    private double score;
    private List<String> tweets;
    private List<String> following;

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String userName, String bio, String profileUrl, String profileImageUrl, String followersCount, String followingCount, int tweetCount, double score, List<String> tweets, List<String> following) {
        this.userId = userId;
        this.userName = userName;
        this.bio = bio;
        this.profileUrl = profileUrl;
        this.profileImageUrl = profileImageUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.tweetCount = tweetCount;
        this.score = score;
        this.tweets = tweets;
        this.following = following;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<String> getTweets() {
        return tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}