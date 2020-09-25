package com.example.reminderappdesign;
public class Notes {
    int id ;

    String startTime,endTime;
    int notificationRequestCode,notificationId,switchStatus;
    public Notes(int id, String startTime, String endTime, int notificationRequestCode,
                 int notificationId,int switchStatus) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationRequestCode = notificationRequestCode;
        this.notificationId = notificationId;
        this.switchStatus = switchStatus;
    }

    public Notes(String startTime, String endTime, int notificationRequestCode, int notificationId, int switchStatus) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationRequestCode = notificationRequestCode;
        this.notificationId = notificationId;
        this.switchStatus = switchStatus;
    }

    public Notes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getNotificationRequestCode() {
        return notificationRequestCode;
    }

    public void setNotificationRequestCode(int notificationRequestCode) {
        this.notificationRequestCode = notificationRequestCode;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(int switchStatus) {
        this.switchStatus = switchStatus;
    }
}
