package core;

import model.Nodes;
import model.Tweet;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

public class SearchDataController extends DataController {

    public List<Tweet> searchTweetByHashtags(String hashtags) {
        List<String> hashtagArray = List.of(hashtags.split("(?=#)"));
        return tweets.stream()
                .filter(tweet -> hasMatchingHashtags(tweet.getHashtags(), hashtagArray))
                .collect(Collectors.toList());
    }

    private boolean hasMatchingHashtags(List<String> tweetHashtags, List<String> hashtagsToFind) {
        return hashtagsToFind.stream()
                .anyMatch(toFind ->
                        tweetHashtags.stream().anyMatch(hashtag -> hashtag.toLowerCase().equals(toFind.toLowerCase()))
                );
    }

    public List<Tweet> searchTweetByKeyword(String keyword) {
        return tweets.stream()
                .filter(tweet -> tweet.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> searchUserByName(String username) {
        return users.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> searchUserById(String userid) {
        return users.stream()
                .filter(user -> user.getUserId().toLowerCase().contains(userid.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<User> searchUser(String keyword) {
        if (keyword.equals("")){
            return users;
        }
        if (keyword.charAt(0) == '@') {
            keyword = keyword.substring(1);
            return searchUserById(keyword);
        }
        return searchUserByName(keyword);
    }

    public List<Tweet> searchTweet(String keyword) {
        if (keyword.equals("")){
            return tweets;
        }
        keyword = keyword.replaceFirst("^\\s+", "");
        if (keyword.charAt(0) == '#') {
            return searchTweetByHashtags(keyword);
        }
        return searchTweetByKeyword(keyword);
    }
}
