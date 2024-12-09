package model;

public class SearchResult {
    private String type;
    private String id;

    public SearchResult(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

}
