package ash.wasset.models;

/**
 * Created by ahmed on 1/3/17.
 */

public class ReviewModel {

    private String ClientName;
    private String ClientPic;
    private String SubmissionDate;
    private String Comment;

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

    public String getSubmissionDate() {
        return SubmissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        SubmissionDate = submissionDate;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
