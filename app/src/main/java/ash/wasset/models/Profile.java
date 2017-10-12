package ash.wasset.models;

import java.util.ArrayList;

/**
 * Created by ahmed on 11/9/16.
 */

public class Profile {

    private long Id;
    private String PicturePath;
    private ArrayList<Categories> Categories;
    private String Description;
    private ArrayList<HoursWork> HoursWorks;
    private double MapLat;
    private double MapLng;
    private boolean ShowPhone;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public ArrayList<ash.wasset.models.Categories> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<ash.wasset.models.Categories> categories) {
        Categories = categories;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<HoursWork> getHoursWorks() {
        return HoursWorks;
    }

    public void setHoursWorks(ArrayList<HoursWork> hoursWorks) {
        HoursWorks = hoursWorks;
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

    public boolean isShowPhone() {
        return ShowPhone;
    }

    public void setShowPhone(boolean showPhone) {
        ShowPhone = showPhone;
    }
}
