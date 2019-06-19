package com.moder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class PatientBean {

    /**
     * id自增
     */
    private int  id;

    /**
     * 患者所属数据源
     */
    private int db_source_id;

    /**
     * 患者名称
     */
    private String patient_name;

    /**
     * 对应Queue_type排队队列视图中queue_type_id字段
     */
    private int queue_type_id;

    /**
     * 显示患者编号用，如有特殊需求可对此项定制。例如“+1号”；
     * 取排队队列的序号或科室排队队列的序号（挂号条上的队列排队序号）
     */
    private String register_id;

    /**
     * 每种队列的排队顺序，默认与register_id(数据为整数的情况下)相同，如需按照挂号时间先后排序，可将此项值设置为0
     */
    private String queue_num;

    /**
     * 如队列有多种优先组合时使用。叫号优先级，默认情况下固定值为0，
     * 如有特殊群体需叫号优先请取值1或更大数字。数字越大优先级越高。
     */
    private int sub_queue_order;

    /**
     * 特殊队列级别的显示名称：军人、预约、初诊等
     */
    private String sub_queue_type;

    /**
     * 叫号器ID（按照叫号器叫号）
     */
    private int pager_id;

    /**
     * 医生id（按医生叫号）
     */
    private int doctor_id;

    /**
     * 呼叫时间
     */
    private String call_time;

    /**
     *
     */
    private int is_priority;

    /**
     *
     */
    private int return_flag;

    /**
     * 是否报道(1未报道，2已报道)
     */
    private int is_display;

    /**
     * 全天 = 34359738367
     * 早晨 = 1108378657
     * 上午 = 2216757314
     * 中午 = 4433514628
     * 下午 = 8867029256
     * 夜间 = 17734058512
     */
    private int time_interval;

    /**
     * 数据同步时间(德阳有中间表，这个是数据同步到中间表时间)
     */
    private String fre_date;

    /**
     * 最后操作时间
     */
    private String opr_time;

    /**
     *
     */
    private String content;

    /**
     *
     */
    private String state_custom;

    /**
     * 患者ID（必须唯一）
     */
    private String patient_id;

    /**
     * 队列ID（德阳这里取得对应医生login_id）
     */
    private String queue_type_source_id;

    /**
     * 该患者叫号时间
     */
    private String start_time;

    /**
     * 患者状态(学习群的接口文档有详细状态标识)
     */
    private int  state_patient;

    /**
     *
     */
    private int  call_count;

    /**
     *
     */
    private int source_id;

    /**
     *
     */
    private String delay_time;

    /**
     *
     */
    private int operator_type;

    /**
     *
     */
    private int parent_id;

    /**
     * 退号，退费等数据对比删除处理；如不存在此项处理则固定0 as is_deleted
     * 1，	需删除；
     * 0，正常
     */
    private int is_deleted;

    /**
     *
     */
    private String caller;

    /**
     *
     */
    private String istype;

    /**
     * 患者检索卡号
     */
    private String patient_source_code;

    /**
     *
     */
    private String nextshow;

    /**
     * 呼叫患者的叫号器IP
     */
    private String callerip;

    /**
     * 第几次呼叫患者的叫号器IP
     */
    private String caller2;

    /**
     * 第几次呼叫患者的叫号器IP
     */
    private String caller3;

    /**
     * 第几次呼叫患者的叫号器IP
     */
    private String caller4;

    /**
     * 分时段就诊开始时间
     */
    private String begin_time;

    /**
     * 分时段就诊结束时间
     */
    private String last_time;

    /**
     * 是否锁定状态：
     * 0 未锁定
     * 1 锁定
     */
    private int late_lock;

    /**
     *
     */
    private  int  register_id2;

    /**
     *
     */
    private  int  state_patient2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDb_source_id() {
        return db_source_id;
    }

    public void setDb_source_id(int db_source_id) {
        this.db_source_id = db_source_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public int getQueue_type_id() {
        return queue_type_id;
    }

    public void setQueue_type_id(int queue_type_id) {
        this.queue_type_id = queue_type_id;
    }

    public String getRegister_id() {
        return register_id;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private SimpleDateFormat simpleDateFormat = null ;

    public SimpleDateFormat getSimpleDateFormat(){
        if(simpleDateFormat == null ){
            simpleDateFormat = new SimpleDateFormat();
        }
        return simpleDateFormat;
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void setRegister_id(String register_id) {
        this.register_id = register_id;
    }

    public String getQueue_num() {
        return queue_num;
    }

    public void setQueue_num(String queue_num) {
        this.queue_num = queue_num;
    }

    public int getSub_queue_order() {
        return sub_queue_order;
    }

    public void setSub_queue_order(int sub_queue_order) {
        this.sub_queue_order = sub_queue_order;
    }

    public String getSub_queue_type() {
        return sub_queue_type;
    }

    public void setSub_queue_type(String sub_queue_type) {
        this.sub_queue_type = sub_queue_type;
    }

    public int getPager_id() {
        return pager_id;
    }

    public void setPager_id(int pager_id) {
        this.pager_id = pager_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public int getIs_priority() {
        return is_priority;
    }

    public void setIs_priority(int is_priority) {
        this.is_priority = is_priority;
    }

    public int getReturn_flag() {
        return return_flag;
    }

    public void setReturn_flag(int return_flag) {
        this.return_flag = return_flag;
    }

    public int getIs_display() {
        return is_display;
    }

    public void setIs_display(int is_display) {
        this.is_display = is_display;
    }

    public int getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(int time_interval) {
        this.time_interval = time_interval;
    }

    public String getFre_date() {
        return fre_date;
    }

    public void setFre_date(String fre_date) {
        this.fre_date = fre_date;
    }

    public String getOpr_time() {
        return opr_time;
    }

    public void setOpr_time(String opr_time) {
        this.opr_time = opr_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState_custom() {
        return state_custom;
    }

    public void setState_custom(String state_custom) {
        this.state_custom = state_custom;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getQueue_type_source_id() {
        return queue_type_source_id;
    }

    public void setQueue_type_source_id(String queue_type_source_id) {
        this.queue_type_source_id = queue_type_source_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getState_patient() {
        return state_patient;
    }

    public void setState_patient(int state_patient) {
        this.state_patient = state_patient;
    }

    public int getCall_count() {
        return call_count;
    }

    public void setCall_count(int call_count) {
        this.call_count = call_count;
    }

    public int getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public String getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(String delay_time) {
        this.delay_time = delay_time;
    }

    public int getOperator_type() {
        return operator_type;
    }

    public void setOperator_type(int operator_type) {
        this.operator_type = operator_type;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getIstype() {
        return istype;
    }

    public void setIstype(String istype) {
        this.istype = istype;
    }

    public String getPatient_source_code() {
        return patient_source_code;
    }

    public void setPatient_source_code(String patient_source_code) {
        this.patient_source_code = patient_source_code;
    }

    public String getNextshow() {
        return nextshow;
    }

    public void setNextshow(String nextshow) {
        this.nextshow = nextshow;
    }

    public String getCallerip() {
        return callerip;
    }

    public void setCallerip(String callerip) {
        this.callerip = callerip;
    }

    public String getCaller2() {
        return caller2;
    }

    public void setCaller2(String caller2) {
        this.caller2 = caller2;
    }

    public String getCaller3() {
        return caller3;
    }

    public void setCaller3(String caller3) {
        this.caller3 = caller3;
    }

    public String getCaller4() {
        return caller4;
    }

    public void setCaller4(String caller4) {
        this.caller4 = caller4;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public int getLate_lock() {
        return late_lock;
    }

    public void setLate_lock(int late_lock) {
        this.late_lock = late_lock;
    }

    public int getRegister_id2() {
        return register_id2;
    }

    public void setRegister_id2(int register_id2) {
        this.register_id2 = register_id2;
    }

    public int getState_patient2() {
        return state_patient2;
    }

    public void setState_patient2(int state_patient2) {
        this.state_patient2 = state_patient2;
    }
}
