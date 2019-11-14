package Main;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MonitoredData {

    public static final String pattern="yyyy-MM-dd HH:mm:ss";

    private Date start_date;
    private Date end_date;
    private String  label;

    public MonitoredData(Date start_date,Date end_date, String label)
    {
        this.start_date=start_date;
        this.end_date=end_date;
        this.label=label;

    }


   public String   display()
    {   String s="";
        s=start_date.toString()+"\t"+end_date.toString()+"\t"+label;
        return s;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getLabel() {
        return label;
    }

    public int getstartDay()
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(start_date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getDuration()
    {
        Long diffInMillies = getEnd_date().getTime() - getStart_date().getTime();

        return  Math.abs(TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MINUTES));



    }
}
