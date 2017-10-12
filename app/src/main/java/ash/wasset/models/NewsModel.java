package ash.wasset.models;

/**
 * Created by ahmed on 1/5/17.
 */

public class NewsModel {

    private String EnglishName;
    private String ArabicName;
    private String EnglishDescription;
    private String ArabicDescription;
    private String PicturePath;
    private String DateOfNew;

    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String englishName) {
        EnglishName = englishName;
    }

    public String getArabicName() {
        return ArabicName;
    }

    public void setArabicName(String arabicName) {
        ArabicName = arabicName;
    }

    public String getEnglishDescription() {
        return EnglishDescription;
    }

    public void setEnglishDescription(String englishDescription) {
        EnglishDescription = englishDescription;
    }

    public String getArabicDescription() {
        return ArabicDescription;
    }

    public void setArabicDescription(String arabicDescription) {
        ArabicDescription = arabicDescription;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public String getDateOfNew() {
        return DateOfNew;
    }

    public void setDateOfNew(String dateOfNew) {
        DateOfNew = dateOfNew;
    }
}
