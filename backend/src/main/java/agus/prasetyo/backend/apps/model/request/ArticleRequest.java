package agus.prasetyo.backend.apps.model.request;

public class ArticleRequest {


    private String id;
    private String title;
    private String content;
    private String authorId;
    private String contributorId;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContributorId() {
        return contributorId;
    }
}
