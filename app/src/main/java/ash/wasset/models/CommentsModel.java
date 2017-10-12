package ash.wasset.models;

/**
 * Created by ahmed on 12/12/16.
 */

public class CommentsModel {

    private String ClientName;
    private String ClientPic;
    private String ReviewDate;
    private String ReviewText;
    private String ReviewRate;
    private String ReviewId;

    public CommentsModel(String clientName, String clientPic, String reviewDate, String reviewText, String reviewRate, String reviewId) {
        ClientName = clientName;
        ClientPic = clientPic;
        ReviewDate = reviewDate;
        ReviewText = reviewText;
        ReviewRate = reviewRate;
        ReviewId = reviewId;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientPic() {
        return ClientPic;
    }

    public void setClientPic(String clientPic) {
        ClientPic = clientPic;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public String getReviewText() {
        return ReviewText;
    }

    public void setReviewText(String reviewText) {
        ReviewText = reviewText;
    }

    public String getReviewRate() {
        return ReviewRate;
    }

    public void setReviewRate(String reviewRate) {
        ReviewRate = reviewRate;
    }

    public String getReviewId() {
        return ReviewId;
    }

    public void setReviewId(String reviewId) {
        ReviewId = reviewId;
    }
}
