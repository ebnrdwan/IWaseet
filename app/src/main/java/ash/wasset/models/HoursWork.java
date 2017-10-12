package ash.wasset.models;

import ash.wasset.models.enums.Days;

/**
 * Created by ahmed on 11/9/16.
 */

public class HoursWork {

    private int Id;
    private Days Day;
    private String DayFrom;
    private String DayTo;
    private boolean OffDays;
    private int ServiceProviderUsers_Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Days getDay() {
        return Day;
    }

    public void setDay(Days day) {
        Day = day;
    }

    public String getDayFrom() {
        return DayFrom;
    }

    public void setDayFrom(String dayFrom) {
        DayFrom = dayFrom;
    }

    public String getDayTo() {
        return DayTo;
    }

    public void setDayTo(String dayTo) {
        DayTo = dayTo;
    }

    public boolean isOffDays() {
        return OffDays;
    }

    public void setOffDays(boolean offDays) {
        OffDays = offDays;
    }

    public int getServiceProviderUsers_Id() {
        return ServiceProviderUsers_Id;
    }

    public void setServiceProviderUsers_Id(int serviceProviderUsers_Id) {
        ServiceProviderUsers_Id = serviceProviderUsers_Id;
    }
}
