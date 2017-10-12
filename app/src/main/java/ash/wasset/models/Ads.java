package ash.wasset.models;

import java.util.Date;

/**
 * Created by ahmed on 11/9/16.
 */

public class Ads {

    private long Id;
    private long ServiceProviderId;
    private Date From;
    private Date To;
    private Date SubmissionDate;
    private double Amount;
    private double MapLat;
    private double MapLng;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getServiceProviderId() {
        return ServiceProviderId;
    }

    public void setServiceProviderId(long serviceProviderId) {
        ServiceProviderId = serviceProviderId;
    }

    public Date getFrom() {
        return From;
    }

    public void setFrom(Date from) {
        From = from;
    }

    public Date getTo() {
        return To;
    }

    public void setTo(Date to) {
        To = to;
    }

    public Date getSubmissionDate() {
        return SubmissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        SubmissionDate = submissionDate;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
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
}
