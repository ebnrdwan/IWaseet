package ash.wasset.models;

/**
 * Created by ahmed on 12/4/16.
 */

public class ServiceListRequestModel {

    private String CatId;
    private String Lat;
    private String Lnd;
    private String PageNo;
    private String SortBy;
    private String SearchKey;

    public ServiceListRequestModel() {
    }

    public ServiceListRequestModel(String catId, String lat, String lnd, String pageNo, String sortBy, String SearchKey) {
        CatId = catId;
        Lat = lat;
        Lnd = lnd;
        PageNo = pageNo;
        SortBy = sortBy;
        this.SearchKey = SearchKey;
    }

    public String getCatId() {
        return CatId;
    }

    public void setCatId(String catId) {
        CatId = catId;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLnd() {
        return Lnd;
    }

    public void setLnd(String lnd) {
        Lnd = lnd;
    }

    public String getPageNo() {
        return PageNo;
    }

    public void setPageNo(String pageNo) {
        PageNo = pageNo;
    }

    public String getSortBy() {
        return SortBy;
    }

    public void setSortBy(String sortBy) {
        SortBy = sortBy;
    }

    public String getSearchKey() {
        return SearchKey;
    }

    public void setSearchKey(String searchKey) {
        SearchKey = searchKey;
    }
}
