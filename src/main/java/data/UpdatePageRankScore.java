package data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UpdatePageRankScore {
    public static void main(String[] args) throws IOException {
        // Đọc dữ liệu JSON từ file
        ObjectMapper objectMapper = new ObjectMapper();
        File inputFile = new File("E:\\Users\\nqdhocai\\IntelliJIDEAProjects\\OOPProject\\src\\main\\resources\\data\\Data35Tweet.json");
        Map<String, Object> jsonData = objectMapper.readValue(inputFile, Map.class);

        // Lấy dữ liệu tweets và users
        List<Map<String, Object>> tweets = (List<Map<String, Object>>) jsonData.get("tweets");
        List<Map<String, Object>> users = (List<Map<String, Object>>) jsonData.get("users");

        users = filterExistUserId(users, tweets);

        // Tạo một map để lưu PageRank score của người dùng
        Map<String, Double> pageRankScores;
        pageRankScores = calculatePageRank(users, tweets);

        // Thêm trường "score" vào mỗi tweet và người dùng
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            user.put("score", (Math.round(pageRankScores.getOrDefault(userId, 0.0)*100.0)/100.0));
        }

        for (Map<String, Object> tweet : tweets) {
            tweet.put("score", (Math.round(pageRankScores.getOrDefault(tweet.get("tweetId"), 0.0)*100.0)/100.0));
        }

        // Lưu dữ liệu vào file JSON mới
        File outputFile = new File("E:\\Users\\nqdhocai\\IntelliJIDEAProjects\\OOPProject\\src\\main\\resources\\data\\node_data.json");
        objectMapper.writeValue(outputFile, jsonData);

        System.out.println("PageRank scores added and saved to node_data.json");
    }

    private static List<Map<String, Object>> filterExistUserId(List<Map<String, Object>> users, List<Map<String, Object>> tweets){
        // Tạo danh sách userId hợp lệ
        Set<String> validUserIds = users.stream()
                .map(user -> (String) user.get("userId"))
                .collect(Collectors.toSet());

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
        Map<String, Double> pageRankScores = new HashMap<>();
        Map<String, Set<String>> graph = new HashMap<>();

        // Tạo đồ thị
        // Các mối quan hệ người dùng (following)
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            List<String> following = (List<String>) user.get("following");
            for (String followingUser : following) {
                graph.computeIfAbsent(followingUser, k -> new HashSet<>()).add(userId);
            }
        }

        // Các mối quan hệ giữa tweet và người dùng
        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            String author = (String) tweet.get("author");
            graph.computeIfAbsent(author, k -> new HashSet<>()).add(tweetId);
        }

        // Khởi tạo giá trị ban đầu cho PageRank
        for (Map<String, Object> user : users) {
            String userId = (String) user.get("userId");
            pageRankScores.put(userId, 1.0);
        }

        // Khởi tạo giá trị ban đầu cho tweet
        for (Map<String, Object> tweet : tweets) {
            String tweetId = (String) tweet.get("tweetId");
            pageRankScores.put(tweetId, 1.0);
        }

        // Tính toán PageRank
        double dampingFactor = 0.85;
        int maxIterations = 100;
        double tolerance = 1.0e-6;

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Map<String, Double> newScores = new HashMap<>();
            for (String nodeId : pageRankScores.keySet()) {
                double score = (1 - dampingFactor);
                Set<String> followers = graph.getOrDefault(nodeId, new HashSet<>());
                for (String follower : followers) {
                    score += dampingFactor * (pageRankScores.get(follower) / graph.getOrDefault(follower, new HashSet<>()).size());
                }
                newScores.put(nodeId, score);
            }

            // Kiểm tra độ chính xác
            double maxDiff = 0.0;
            for (String nodeId : pageRankScores.keySet()) {
                maxDiff = Math.max(maxDiff, Math.abs(pageRankScores.get(nodeId) - newScores.get(nodeId)));
            }
            if (maxDiff < tolerance) {
                break;
            }
            pageRankScores = newScores;
        }
        return pageRankScores;
    }
}
