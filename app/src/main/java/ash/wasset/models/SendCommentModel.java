package ash.wasset.models;

/**
 * Created by ahmed on 12/12/16.
 */

public class SendCommentModel {

    private String ClientId;
    private String ServiceProviderId;
    private String SubmissionDate;
    private String Rate;
    private String Comment;

    public SendCommentModel() {
    }

    public SendCommentModel(String clientId, String serviceProviderId, String submissionDate, String rate, String comment) {
        ClientId = clientId;
        ServiceProviderId = serviceProviderId;
        SubmissionDate = submissionDate;
        Rate = rate;
        Comment = comment;
    }

    public String getClientId() {
        return ClientId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }

    public String getServiceProviderId() {
        return ServiceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        ServiceProviderId = serviceProviderId;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getSubmissionDate() {
        return SubmissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        SubmissionDate = submissionDate;
    }
}
