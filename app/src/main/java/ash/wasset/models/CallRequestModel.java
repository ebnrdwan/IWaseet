package ash.wasset.models;

/**
 * Created by ahmed on 1/2/17.
 */

public class CallRequestModel {

    private String ClientName;
    private String ClientPic;
    private String RequestDate;
    private String RequestDetails;
    private String Seen;
    private String RequestState;

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

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getRequestDetails() {
        return RequestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        RequestDetails = requestDetails;
    }

    public String getSeen() {
        return Seen;
    }

    public void setSeen(String seen) {
        Seen = seen;
    }

    public int getRequestState() {
        return Double.valueOf(RequestState).intValue();
    }

    public void setRequestState(String requestState) {
        RequestState = requestState;
    }
}
