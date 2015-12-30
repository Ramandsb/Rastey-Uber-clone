package tagbin.in.myapplication.UpcomingRides;

/**
 * Created by admin pc on 16-12-2015.
 */
public class DataItems {

    private String cab_no,time,to_loc,pick,timetostart,status,dropAdd,phoneno,clientname;


    public DataItems() {
    }

    public DataItems(String cab_no,String time,String to_loc,String pick,String timetostart,String status,String dropAdd,String phoneno,String clientname) {
        this.cab_no=cab_no;
                this.time=time;
        this.to_loc=to_loc;
        this.pick=pick;
        this.timetostart=timetostart;
        this.status=status;
        this.dropAdd=dropAdd;
        this.phoneno=phoneno;
        this.clientname=clientname;
    }

        public String getCab_no() {
        return  cab_no;
    }

    public void setCab_no(String cab_no) {

        this.cab_no= cab_no;
    }

    public String getTo_loc() {
        return to_loc;
    }

    public void setTo_loc(String to_loc) {
        this.to_loc = to_loc;
    }
    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;

    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimetostart() {
        return timetostart;
    }

    public void setTimetostart(String timetostart) {
        this.timetostart = timetostart;
    }

    public String getStatus() {
        return status;
    }

    public void SetStatus(String status) {
        this.status = status;
    }
    public String getDropadress() {
        return dropAdd;
    }

    public void setDropaddress(String dropAdd) {
        this.dropAdd = dropAdd;
    }
    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
    public String getclientname() {
        return clientname;
    }

    public void setclientname(String clientname) {
        this.clientname = clientname;
    }


}
