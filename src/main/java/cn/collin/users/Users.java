package cn.collin.users;

/**
 * Created by collin on 17-5-29.
 */
public class Users {
    long startTime, endTime;
    String contractID;

    public Users() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public Users(long startTime, long endTime, String contractID) {

        this.startTime = startTime;
        this.endTime = endTime;
        this.contractID = contractID;
    }
}
