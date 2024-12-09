package model;

import java.util.List;

public class Tweet{
    private String tweetId;
    private String author;
    private String createdAt;
    private String content;
    private String tweetUrl;
    private String likesCount;
    private String retweetsCount;
    private String repliesCount;
    private double score;
    private List<String> hashtags;
    private List<String> retweeters;

    public Tweet(){}

    // Getters and Setters
    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTweetUrl() {
        return tweetUrl;
    }

    public void setTweetUrl(String tweetUrl) {
        this.tweetUrl = tweetUrl;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getRetweetsCount() {
        return retweetsCount;
    }

    public void setRetweetsCount(String retweetsCount) {
        this.retweetsCount = retweetsCount;
    }

    public String getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(String repliesCount) {
        this.repliesCount = repliesCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getRetweeters() {
        return retweeters;
    }

    public void setRetweeters(List<String> retweeters) {
        this.retweeters = retweeters;
    }

}
