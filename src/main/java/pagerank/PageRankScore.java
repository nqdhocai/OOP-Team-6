package pagerank;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PageRankScore {

    public static void main(String[] args) {
        try {
            // Đọc dữ liệu JSON từ file
            ObjectMapper objectMapper = new ObjectMapper();
            File inputFile = new File("src/main/resources/data/RawData.json");
            Map<String, Object> jsonData = objectMapper.readValue(inputFile, Map.class);

            // Lấy dữ liệu tweets và users
            List<Map<String, Object>> tweets = (List<Map<String, Object>>) jsonData.get("tweets");
            List<Map<String, Object>> users = (List<Map<String, Object>>) jsonData.get("users");

            // Xử lý dữ liệu người dùng
            users = repairUsersId(users);
            users = mergeUsers(users);
            users = filterInvalidUsers(users, tweets);

            tweets = mergeTweets(tweets);

            // Tính toán PageRank
            Map<String, Double> pageRankScores = calculatePageRank(users, tweets);

            // Gắn điểm số PageRank vào dữ liệu
            annotateScores(users, tweets, pageRankScores);

            // Lưu dữ liệu vào file JSON mới
            saveToFile("src/main/resources/data/NodeData.json", users, tweets);

            System.out.println("PageRank scores added and saved to NodeData.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading or writing file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private static List<Map<String, Object>> repairUsersId(List<Map<String, Object>> users) {
        return users.stream().map(user -> {
            String profileUrl = (String) user.get("profileUrl");
            if (profileUrl != null) {
                String userId = profileUrl.substring(profileUrl.lastIndexOf('/') + 1);
                user.put("userId", userId);
            }
            return user;
        }).collect(Collectors.toList());
    }
    private static List<Map<String, Object>> mergeTweets(List<Map<String, Object>> tweets) {
        Map<String, Map<String, Object>> mergedTweets = new HashMap<>();

        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            mergedTweets.computeIfAbsent(tweetId, k -> new HashMap<>(tweet));

            Map<String, Object> existingTweet = mergedTweets.get(tweetId);
            mergeTweetFields(existingTweet, tweet);
        }

        return new ArrayList<>(mergedTweets.values());
    }
    private static List<Map<String, Object>> mergeUsers(List<Map<String, Object>> users) {
        Map<String, Map<String, Object>> mergedUsers = new HashMap<>();

        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            mergedUsers.computeIfAbsent(userId, k -> new HashMap<>(user));

            Map<String, Object> existingUser = mergedUsers.get(userId);
            mergeUserFields(existingUser, user);
        }

        return new ArrayList<>(mergedUsers.values());
    }

    private static void mergeUserFields(Map<String, Object> target, Map<String, Object> source) {
        mergeLists(target, source, "tweets");
        mergeLists(target, source, "following");
    }

    private static void mergeTweetFields(Map<String, Object> target, Map<String, Object> source) {
        mergeLists(target, source, "retweeters");
        mergeLists(target, source, "hashtags");
    }

    private static void mergeLists(Map<String, Object> target, Map<String, Object> source, String key) {
        List<String> targetList = (List<String>) target.getOrDefault(key, new ArrayList<>());
        List<String> sourceList = (List<String>) source.getOrDefault(key, new ArrayList<>());
        Set<String> mergedSet = new HashSet<>(targetList);
        mergedSet.addAll(sourceList);
        target.put(key, new ArrayList<>(mergedSet));
    }

    private static List<Map<String, Object>> filterInvalidUsers(List<Map<String, Object>> users, List<Map<String, Object>> tweets) {
        Set<String> validUserIds = users.stream()
                .map(user -> (String) user.get("userId"))
                .collect(Collectors.toSet());

        Set<String> validTweetIds = tweets.stream()
                .map(tweet -> (String) tweet.get("tweetId"))
                .collect(Collectors.toSet());

        for (Map<String, Object> user : users) {
            user.put("following", filterValidIds((List<String>) user.get("following"), validUserIds));
            user.put("tweets", filterValidIds((List<String>) user.get("tweets"), validTweetIds));
        }

        return users;
    }

    private static List<String> filterValidIds(List<String> ids, Set<String> validIds) {
        return ids == null ? Collections.emptyList() : ids.stream()
                .filter(validIds::contains)
                .collect(Collectors.toList());
    }

    private static Map<String, Double> calculatePageRank(List<Map<String, Object>> users, List<Map<String, Object>> tweets) {
        Map<String, Set<String>> graph = buildGraph(users, tweets);
        return computePageRank(graph);
    }

    private static Map<String, Set<String>> buildGraph(List<Map<String, Object>> users, List<Map<String, Object>> tweets) {
        Map<String, Set<String>> graph = new HashMap<>();

        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            graph.putIfAbsent(userId, new HashSet<>());
            graph.get(userId).addAll((List<String>) user.get("following"));
        }

        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            graph.putIfAbsent(tweetId, new HashSet<>());
            List<String> retweeters = (List<String>) tweet.get("retweeters");
            if (retweeters != null) {
                graph.get(tweetId).addAll(retweeters);
            }
        }

        return graph;
    }

    private static Map<String, Double> computePageRank(Map<String, Set<String>> graph) {
        final double DAMPING_FACTOR = 0.85;
        final int MAX_ITERATIONS = 50;
        final double TOLERANCE = 1e-6;

        Map<String, Double> ranks = new HashMap<>();
        double initialRank = 1.0 / graph.size();

        Map<String, Double> finalRanks = ranks;
        graph.forEach((node, edges) -> {
            finalRanks.put(node, initialRank);
        });

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Map<String, Double> newRanks = new HashMap<>();
            double diff = 0.0;

            for (String node : graph.keySet()) {
                double rank = (1 - DAMPING_FACTOR) / graph.size();
                for (String neighbor : graph.keySet()) {
                    if (graph.get(neighbor).contains(node)) {
                        rank += DAMPING_FACTOR * (ranks.get(neighbor) / graph.get(neighbor).size());
                    }
                }
                newRanks.put(node, rank);
                diff += Math.abs(rank - ranks.get(node));
            }

            ranks = newRanks;
            if (diff < TOLERANCE) break;
        }

        return normalizeScores(ranks);
    }

    private static Map<String, Double> normalizeScores(Map<String, Double> scores) {
        double min = Collections.min(scores.values());
        double max = Collections.max(scores.values());
        return scores.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> (max == min) ? 1.0 : ((entry.getValue() - min) / (max - min)) * 100.0));
    }

    private static void annotateScores(List<Map<String, Object>> users, List<Map<String, Object>> tweets, Map<String, Double> pageRankScores) {
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            user.put("score", roundScore(pageRankScores.getOrDefault(userId, 0.0)));
        }

        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            tweet.put("score", roundScore(pageRankScores.getOrDefault(tweetId, 0.0)));
        }
    }

    private static double roundScore(double score) {
        return Math.round(score * 100.0) / 100.0;
    }

    private static void saveToFile(String filePath, List<Map<String, Object>> users, List<Map<String, Object>> tweets) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> newJsonData = new HashMap<>();
        newJsonData.put("users", users);
        newJsonData.put("tweets", tweets);
        objectMapper.writeValue(new File(filePath), newJsonData);
    }
}
