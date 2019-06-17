package com.moder;

public class QueryConditionBean {

    public QueryConditionBean() {
    }

    public QueryConditionBean(String queueTypeId, String patientState, int isLock, int isDisplay) {

        this.queueTypeId = queueTypeId;
        this.patientState = patientState;
        this.isLock = isLock;
        this.isDisplay = isDisplay;
    }

    /**
     * 队列ID
     */
    private String queueTypeId;

    /**
     * 患者状态
     */
    private String patientState;

    /**
     * 是否锁住
     */
    private int isLock;

    /**
     * 是否签到: 2已签到
     */
    private int isDisplay;

    public String getQueueTypeId() {
        return queueTypeId;
    }

    public void setQueueTypeId(String queueTypeId) {
        this.queueTypeId = queueTypeId;
    }

    public String getPatientState() {
        return patientState;
    }

    public void setPatientState(String patientState) {
        this.patientState = patientState;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }
}
