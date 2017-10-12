package ash.wasset.models;

/**
 * Created by ahmed on 12/22/16.
 */

public class WorkHoursModel {

    private String Id;
    private String Day;
    private String DayFrom;
    private String DayTo;
    private String OffDays;

    public String getId() {
        return Integer.toString(Double.valueOf(Id).intValue());
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDay() {
        return Integer.toString(Double.valueOf(Day).intValue());
    }

    public void setDay(String day) {
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

    public String getOffDays() {
        return OffDays;
    }

    public void setOffDays(String offDays) {
        OffDays = offDays;
    }
}
