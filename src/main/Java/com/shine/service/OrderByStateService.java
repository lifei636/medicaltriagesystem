package com.shine.service;

import com.core.base.service.IService;
import com.core.toolbox.Record;
import com.moder.QueryConditionBean;
import com.shine.model.PatientQueue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface OrderByStateService extends IService<PatientQueue> {

    /**
     * 医生叫号器-查询锁定的队列
     * @return
     */
    List<Record> queryLockWait(String queueTypeId);

    /**
     * 医生叫号器-根据状态查询患者队列
     * @param queueTypeId 队列ID
     * @param statePatient 患者状态
     * @return
     */
    List<Record> queryWait(String queueTypeId ,String statePatient);

    /**
     * 科室叫号器-查询锁定患者列表
     * @param queueTypeId
     * @return
     */
    List<Record> queryPageWaitLock(String queueTypeId);

    /**
     * 科室叫号器-根据患者状态查询患者列表
     * @param queueTypeId 队列ID
     * @param statePatient 患者状态
     * @return
     */
    List<Record> queryPageWait(String queueTypeId ,String statePatient);


}
