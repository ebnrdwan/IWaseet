package ash.wasset.models;

import java.util.Date;

import ash.wasset.models.enums.RequestState;

/**
 * Created by ahmed on 11/9/16.
 */

public class ClientRequests {

    private long Id;
    private long ClientId;
    private long ServiceProviderId;
    private double MapLat;
    private double MapLng;
    private Date RequestDate;
    private String RequestDetails;
    private boolean Seened;
    private Date SeenedDate;
    private RequestState RequestState;
    private ash.wasset.models.enums.RequestType RequestType;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getClientId() {
        return ClientId;
    }

    public void setClientId(long clientId) {
        ClientId = clientId;
    }

    public long getServiceProviderId() {
        return ServiceProviderId;
    }

    public void setServiceProviderId(long serviceProviderId) {
        ServiceProviderId = serviceProviderId;
    }

    public double getMapLat() {
        return MapLat;
    }

    public void setMapLat(double mapLat) {
        MapLat = mapLat;
    }

    public double getMapLng() {
        return MapLng;
    }

    public void setMapLng(double mapLng) {
        MapLng = mapLng;
    }

    public Date getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(Date requestDate) {
        RequestDate = requestDate;
    }

    public String getRequestDetails() {
        return RequestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        RequestDetails = requestDetails;
    }

    public boolean isSeened() {
        return Seened;
    }

    public void setSeened(boolean seened) {
        Seened = seened;
    }

    public Date getSeenedDate() {
        return SeenedDate;
    }

    public void setSeenedDate(Date seenedDate) {
        SeenedDate = seenedDate;
    }

    public ash.wasset.models.enums.RequestState getRequestState() {
        return RequestState;
    }

    public void setRequestState(ash.wasset.models.enums.RequestState requestState) {
        RequestState = requestState;
    }

    public ash.wasset.models.enums.RequestType getRequestType() {
        return RequestType;
    }

    public void setRequestType(ash.wasset.models.enums.RequestType requestType) {
        RequestType = requestType;
    }
}
