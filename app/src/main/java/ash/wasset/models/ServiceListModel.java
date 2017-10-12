package ash.wasset.models;

/**
 * Created by ahmed on 11/30/16.
 */

public class ServiceListModel {

    private String Id;
    private String Name;
    private String MapLat;
    private String MapLng;
    private String Distance;
    private String IsAdds;
    private String TotalRate;
    private String Description;
    private String PicturePath;
    private String ShopName;
    private String PhoneNumber;
    private String ShowPhone;

    public ServiceListModel(String id, String name, String mapLat, String mapLng, String distance, String isAdds, String totalRate, String description, String picturePath, String shopName, String phoneNumber, String showPhone) {
        Id = id;
        Name = name;
        MapLat = mapLat;
        MapLng = mapLng;
        Distance = distance;
        IsAdds = isAdds;
        TotalRate = totalRate;
        Description = description;
        PicturePath = picturePath;
        ShopName = shopName;
        PhoneNumber = phoneNumber;
        ShopName = shopName;
    }

    public ServiceListModel() {
    }

    public String getId() {
        return Integer.toString(Double.valueOf(Id).intValue());
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getMapLat() {
        return Double.parseDouble(MapLat);
    }

    public void setMapLat(String mapLat) {
        MapLat = mapLat;
    }

    public double getMapLng() {
        return Double.parseDouble(MapLng);
    }

    public void setMapLng(String mapLng) {
        MapLng = mapLng;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getIsAdds() {
        return IsAdds;
    }

    public void setIsAdds(String isAdds) {
        IsAdds = isAdds;
    }

    public String getTotalRate() {
        return TotalRate;
    }

    public void setTotalRate(String totalRate) {
        TotalRate = totalRate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getShowPhone() {
        return ShowPhone;
    }

    public void setShowPhone(String showPhone) {
        ShowPhone = showPhone;
    }
}
