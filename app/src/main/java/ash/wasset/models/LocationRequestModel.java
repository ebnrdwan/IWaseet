package ash.wasset.models;

/**
 * Created by ahmed on 12/29/16.
 */

public class LocationRequestModel {

    private String ClientName;
    private String ClientPic;
    private String RequestDate;
    private String RequestDetails;
    private String MapLat;
    private String MapLng;
    private String Seen;

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

    public Double getMapLat() {
        return Double.parseDouble(MapLat);
    }

    public void setMapLat(String mapLat) {
        MapLat = mapLat;
    }

    public Double getMapLng() {
        return Double.parseDouble(MapLng);
    }

    public void setMapLng(String mapLng) {
        MapLng = mapLng;
    }

    public String getSeen() {
        return Seen;
    }

    public void setSeen(String seen) {
        Seen = seen;
    }
}
