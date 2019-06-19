package com.shine.service.impl;

import com.core.base.service.impl.BaseService;
import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.toolbox.Record;
import com.moder.QueryConditionBean;
import com.shine.model.PatientQueue;
import com.shine.service.OrderByStateService;
import org.beetl.sql.ext.jfinal.JFinalBeetlSql;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class OrderByStateServiceImpl extends BaseService<PatientQueue> implements OrderByStateService {

    private Blade blade = Blade.create(PatientQueue.class);

    @Override
    public List<Record> queryLockWait(String queueTypeId){
        String sql = Blade.dao().getScript("QueueOrder.queryWaitLock_docot").getSql();
        return Db.init().selectList(sql, Record.create().set("queueTypeId", queueTypeId));
    }

    @Override
    public List<Record> queryWait(String queueTypeId,String statePatient) {
        String sql = Blade.dao().getScript("QueueOrder.queryPatientList_docot").getSql();
        return Db.init().selectList(sql, Record.create().set("queueTypeId", queueTypeId).set("state_patient",statePatient));
    }

    @Override
    public List<Record> queryPageWaitLock(String queueTypeId) {
        String sql = Blade.dao().getScript("QueueOrder.queryWaitLock_page").getSql();
        return Db.init().selectList(sql, Record.create().set("queueTypeId", queueTypeId));
    }

    @Override
    public List<Record> queryPageWait(String queueTypeId, String statePatient) {
        String sql = Blade.dao().getScript("QueueOrder.queryPatientList_page").getSql();
        return Db.init().selectList(sql, Record.create().set("queueTypeId", queueTypeId).set("state_patient",statePatient));
    }


}
