package ash.wasset.models;

import java.util.Date;

/**
 * Created by ahmed on 11/9/16.
 */

public class Users {

    private String Id;
    private String UserName;
    private String Name;
    private String PhoneNo;
    private String MobileNumber;
    private String Email;
    private String Password;
    private String PicturePath;
    private String UserType;
    private String Activated;
    private String Blocked;
    private Date DateAdded;
    private String RegActivationCode;
    private String ActivationCode;
    private Profile Profile;
    private String ShopName;
    private String MapLat;
    private String MapLng;
    private String AccessToken;
    private String Description;
    private String ShowPhone;
    private String MobileType;
    private String PushKey;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public String getUserType() {
        return Integer.toString(Double.valueOf(UserType).intValue());
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public boolean getActivated() {
        return Boolean.parseBoolean(Activated);
    }

    public void setActivated(String activated) {
        Activated = activated;
    }

    public boolean getBlocked() {
        return Boolean.parseBoolean(Activated);
    }

    public void setBlocked(String blocked) {
        Blocked = blocked;
    }

    public Date getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        DateAdded = dateAdded;
    }

    public String getRegActivationCode() {
        return RegActivationCode;
    }

    public void setRegActivationCode(String regActivationCode) {
        RegActivationCode = regActivationCode;
    }

    public String getActivationCode() {
        return ActivationCode;
    }

    public void setActivationCode(String activationCode) {
        ActivationCode = activationCode;
    }

    public ash.wasset.models.Profile getProfile() {
        return Profile;
    }

    public void setProfile(ash.wasset.models.Profile profile) {
        Profile = profile;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getMapLat() {
        return MapLat;
    }

    public void setMapLat(String mapLat) {
        MapLat = mapLat;
    }

    public String getMapLng() {
        return MapLng;
    }

    public void setMapLng(String mapLng) {
        MapLng = mapLng;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getShowPhone() {
        return ShowPhone;
    }

    public void setShowPhone(String showPhone) {
        ShowPhone = showPhone;
    }

    public String getMobileType() {
        return MobileType;
    }

    public void setMobileType(String mobileType) {
        MobileType = mobileType;
    }

    public String getPushKey() {
        return PushKey;
    }

    public void setPushKey(String pushKey) {
        PushKey = pushKey;
    }
}
