package ash.wasset.models;

/**
 * Created by ahmadkholy on 9/10/17.
 */

public class Vendor {
    private String name;
    private String serviceDescription;
    private int rate=0;
    private String location;


    public void setName(String val)
    {
        name = val ;
    }

    public String getName()
    {
        if(name==null)return "";
        return  name;
    }

    public void setServiceDesc(String val)
    {
        serviceDescription=val;
    }

    public String getServiceDescription()
    {
        if(serviceDescription==null) return "";
        return serviceDescription;
    }

    public int getRate() {

        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getLocation() {
        if(location==null)return "";
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
