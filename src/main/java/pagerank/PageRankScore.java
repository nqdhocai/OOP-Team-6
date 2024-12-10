package pagerank;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PageRankScore {
    public static void main(String[] args) throws IOException {
        // Đọc dữ liệu JSON từ file
        ObjectMapper objectMapper = new ObjectMapper();
        File inputFile = new File("src/main/resources/data/RawData.json");
        Map<String, Object> jsonData = objectMapper.readValue(inputFile, Map.class);

        // Lấy dữ liệu tweets và users
        List<Map<String, Object>> tweets = (List<Map<String, Object>>) jsonData.get("tweets");
        List<Map<String, Object>> users = (List<Map<String, Object>>) jsonData.get("users");


        users = repairUsersId(users);
        users = mergeUsers(users);

        // Tạo một map để lưu PageRank score của người dùng
        Map<String, Double> pageRankScores;
        pageRankScores = calculatePageRank(users, tweets);

        users = filterExistUserId(users, tweets);

        // Thêm trường "score" vào mỗi tweet và người dùng
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            user.put("score", (Math.round(pageRankScores.getOrDefault(userId, 0.0) * 100.0) / 100.0));
        }

        for (Map<String, Object> tweet : tweets) {
            tweet.put("score", (Math.round(pageRankScores.getOrDefault(tweet.get("tweetId"), 0.0) * 100.0) / 100.0));
        }

        Map<String, Object> newJsonData = new HashMap<>();
        newJsonData.put("tweets", tweets);
        newJsonData.put("users", users);

        // Lưu dữ liệu vào file JSON mới
        File outputFile = new File("src/main/resources/data/NodeData.json");
        objectMapper.writeValue(outputFile, newJsonData);

        System.out.println("PageRank scores added and saved to NodeData.json");
    }

    private static List<Map<String, Object>> filterExistUserId(List<Map<String, Object>> users, List<Map<String, Object>> tweets) {

        // Tạo danh sách userId hợp lệ
        Set<String> validUserIds = users.stream()
                .map(user -> (String) user.get("userId"))
                .collect(Collectors.toSet());

        List<Map<String, Object>> uniqueUser = new ArrayList<>();
        Set<String> existUserIds = new HashSet<>();

        // Lọc các followingId của từng user
        for (Map<String, Object> user : users) {
            List<String> following = (List<String>) user.get("following");
            // Giữ lại chỉ các followingId hợp lệ
            following.removeIf(followingId -> !validUserIds.contains(followingId));
            user.put("following", following);  // Cập nhật lại danh sách following
        }

        // Lọc các tweetId của từng user
        for (Map<String, Object> user : users) {
            List<String> userTweets = (List<String>) user.get("tweets");
            // Giữ lại chỉ các tweetId hợp lệ
            userTweets.removeIf(tweetId -> !tweets.stream()
                    .map(tweet -> (String) tweet.get("tweetId"))
                    .collect(Collectors.toSet())
                    .contains(tweetId));
            user.put("tweets", userTweets);  // Cập nhật lại danh sách tweets
        }
        return users;
    }

    private static Map<String, Double> calculatePageRank(List<Map<String, Object>> users, List<Map<String, Object>> tweets) {
        // Tạo đồ thị liên kết
        Map<String, HashSet<String>> graph = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        // Thêm các cạnh từ người dùng
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            List<String> following = (List<String>) user.get("following");
            List<String> userTweets = (List<String>) user.get("tweets");

            nodes.add(userId);
            nodes.addAll(following);
            nodes.addAll(userTweets);
        }

        // Thêm các cạnh từ tweet
        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            String author = (String) tweet.get("author");
            List<String> retweeters = (List<String>) tweet.get("retweeters");

            nodes.add(tweetId);
            nodes.addAll(retweeters);
            nodes.add(author);
        }

        for (String node: nodes) {
            graph.putIfAbsent(node, new HashSet<String>());
        }

        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            List<String> following = (List<String>) user.get("following");
            List<String> userTweets = (List<String>) user.get("tweets");

            HashSet<String> userEdgeOut = graph.get(userId);
            userEdgeOut.addAll(following);
            userEdgeOut.addAll(userTweets);
        }

        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            String author = (String) tweet.get("author");
            List<String> retweeters = (List<String>) tweet.get("retweeters");

            graph.get(tweetId).addAll(retweeters);
        }

        // Tính toán PageRank
        return computePageRank(graph);
    }

    private static Map<String, Double> computePageRank(Map<String, HashSet<String>> graph) {
        final double DAMPING_FACTOR = 0.85;
        final int MAX_ITERATIONS = 100;
        final double TOLERANCE = 1e-6;

        Map<String, Double> ranks = new HashMap<>();
        Map<String, Integer> outDegree = new HashMap<>();

        // Khởi tạo giá trị PageRank và tính outDegree
        double initialRank = 1.0 / graph.size();
        for (String node : graph.keySet()) {
            ranks.put(node, initialRank);
            outDegree.put(node, graph.get(node).size());
        }

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Map<String, Double> newRanks = new HashMap<>();
            double diff = 0.0;

            for (String node : graph.keySet()) {
                double rank = (1 - DAMPING_FACTOR) / graph.size();
                for (String neighbor : graph.keySet()) {
                    if (graph.get(neighbor).contains(node)) {
                        rank += DAMPING_FACTOR * (ranks.get(neighbor) / outDegree.get(neighbor));
                    }
                }
                newRanks.put(node, rank);
                diff += Math.abs(rank - ranks.get(node));
            }

            ranks = newRanks;

            // Dừng khi hội tụ
            if (diff < TOLERANCE) break;
        }
        ranks = normalizeScores(ranks);
        //System.out.println(ranks);
        return ranks;
    }
    private static Map<String, Double> normalizeScores(Map<String, Double> scores) {
        // Tìm giá trị min và max
        double min = Collections.min(scores.values());
        double max = Collections.max(scores.values());

        // Trường hợp đặc biệt: max = min
        if (min == max) {
            return scores.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> 1.0)); // Hoặc 0.0
        }

        // Chuẩn hóa các giá trị về khoảng [0, 1]
        return scores.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ((entry.getValue() - min) / (max - min))*100.0
                ));
    }

    public static List<Map<String, Object>> mergeUsers(List<Map<String, Object>> users) {
        // Tạo một Map để nhóm người dùng theo userId
        Map<String, Map<String, Object>> mergedUsers = new HashMap<>();

        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            if (!mergedUsers.containsKey(userId)) {
                mergedUsers.put(userId, user);
            } else {
                Object mergedUser = mergedUsers.get(userId);

                List<String> tweets = (List<String>) ((Map<?, ?>) mergedUser).get("tweets");

                Set<String> uniqueTweets = new HashSet<>(tweets);
                tweets = new ArrayList<>(uniqueTweets);  // Chuyển lại thành List

                List<String> following = (List<String>) ((Map<?, ?>) mergedUser).get("following");

                Set<String> uniqueFollowing = new HashSet<>(following);
                following = new ArrayList<>(uniqueFollowing);

                tweets.addAll((List<String>) user.get("tweets"));
                following.addAll((List<String>) user.get("following"));

            }
        }
        return new ArrayList<>(mergedUsers.values());
    }

    private static List<Map<String, Object>> repairUsersId(List<Map<String, Object>> users) {
        List<Map<String, Object>> repairedUsers = new ArrayList<>();

        for (Map<String, Object> user : users) {
            String userProfileUrl = (String) user.get("profileUrl");
            List<String> urlSplitList = List.of(userProfileUrl.split("/"));
            String userId = (String) urlSplitList.get(urlSplitList.size() - 1);

            if (!userId.equals(user.get("userId"))) {
                user.replace("userId", userId);
                repairedUsers.add(user);
            } else {
                repairedUsers.add(user);
            }
        }
        return repairedUsers;
    }
}
