package ash.wasset.models;

/**
 * Created by ahmed on 12/7/16.
 */

public class RequestModel {

    private String ClientId;
    private String ServiceProviderId;
    private String RquestType;
    private String RquestState;
    private String MapLng;
    private String MapLat;
    private String RquestDetails;

    public RequestModel() {
    }

    public RequestModel(String clientId, String serviceProviderId, String rquestType, String rquestState, String mapLng, String mapLat, String rquestDetails) {
        ClientId = clientId;
        ServiceProviderId = serviceProviderId;
        RquestType = rquestType;
        RquestState = rquestState;
        MapLng = mapLng;
        MapLat = mapLat;
        RquestDetails = rquestDetails;
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

    public String getRquestType() {
        return RquestType;
    }

    public void setRquestType(String rquestType) {
        RquestType = rquestType;
    }

    public String getRquestState() {
        return RquestState;
    }

    public void setRquestState(String rquestState) {
        RquestState = rquestState;
    }

    public String getMapLng() {
        return MapLng;
    }

    public void setMapLng(String mapLng) {
        MapLng = mapLng;
    }

    public String getMapLat() {
        return MapLat;
    }

    public void setMapLat(String mapLat) {
        MapLat = mapLat;
    }

    public String getRquestDetails() {
        return RquestDetails;
    }

    public void setRquestDetails(String rquestDetails) {
        RquestDetails = rquestDetails;
    }
}
