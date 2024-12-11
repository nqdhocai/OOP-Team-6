package crawl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
//import graph.UserOld;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Save {
    private final String FILE_NAME;
    private Set<TweetPage> tweets;
    private Set<UserPage> users;
    public Save(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }

    public Save(String FILE_NAME, Set<TweetPage> tweets, Set<UserPage> users) {
        this.FILE_NAME = FILE_NAME;
        this.tweets = tweets;
        this.users = users;
    }
    public void saveToJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), this);
    }

    public void saveToJSON(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_NAME), o);
    }

    public static <T> Set<T> loadJSON(String FILE_NAME, TypeReference<Set<T>> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(FILE_NAME), typeReference);
    }

    public Set<TweetPage> getTweets() {
        return tweets;
    }

    public Set<UserPage> getUsers() {
        return users;
    }



}
