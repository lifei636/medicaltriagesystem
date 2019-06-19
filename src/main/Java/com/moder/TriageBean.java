package com.moder;

/**
 * 分诊台实体类
 * @author : li.fei
 * @since : 2019/6/14
 */
public class TriageBean {
    /**
     *     分诊台id
     */

    private int triage_id;

    /**
     * 分诊台类型
     */

    private int triage_type;

    /**
     * 叫号器id
     */

    private int pager_type;

    /**
     * 过号间隔
     */
    private int call_buffer;

    /**
     * 分诊台名称
     */
    private String name;

    /**
    *分诊台ip
     */
    private String ip;

    /*
    *描述
     */
    private String description;

    /**
    创建时间
     */
    private String create_time;

    /**
     * 叫号器类型 1 按医生排行，2科室排号
     */

    private int reorder_type;

    /**
     * 回复诊叫号间隔
     */
    private String return_flag_step;

    /**
     *  迟到等候
     **/
    private int late_flag_step;

    /**
     * 优先等候
     **/
    private String first_flag_step;

    /**
     * 分诊台密码
     **/
    private String triage_pwd;

    /*
     * 打印类型
     **/
    private int print_type;

    /**
     * 迟到时间
     **/
    private int pass_time;

    /**
     * 迟到模式
     */
    private int late_type;

    /**
     * 迟到排序方式
     */
    private int late_show;

    /**
     * 锁定数量
     */
    private int lock_flag_step;

    public int getLock_flag_step() {
        return lock_flag_step;
    }

    public void setLock_flag_step(int lock_flag_step) {
        this.lock_flag_step = lock_flag_step;
    }

    public int getTriage_id() {
        return triage_id;
    }

    public void setTriage_id(int triage_id) {
        this.triage_id = triage_id;
    }

    public int getTriage_type() {
        return triage_type;
    }

    public void setTriage_type(int triage_type) {
        this.triage_type = triage_type;
    }

    public int getPager_type() {
        return pager_type;
    }

    public void setPager_type(int pager_type) {
        this.pager_type = pager_type;
    }

    public int getCall_buffer() {
        return call_buffer;
    }

    public void setCall_buffer(int call_buffer) {
        this.call_buffer = call_buffer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getReorder_type() {
        return reorder_type;
    }

    public void setReorder_type(int reorder_type) {
        this.reorder_type = reorder_type;
    }

    public String getReturn_flag_step() {
        return return_flag_step;
    }

    public void setReturn_flag_step(String return_flag_step) {
        this.return_flag_step = return_flag_step;
    }

    public int getLate_flag_step() {
        return late_flag_step;
    }

    public void setLate_flag_step(int late_flag_step) {
        this.late_flag_step = late_flag_step;
    }

    public String getFirst_flag_step() {
        return first_flag_step;
    }

    public void setFirst_flag_step(String first_flag_step) {
        this.first_flag_step = first_flag_step;
    }

    public String getTriage_pwd() {
        return triage_pwd;
    }

    public void setTriage_pwd(String triage_pwd) {
        this.triage_pwd = triage_pwd;
    }

    public int getPrint_type() {
        return print_type;
    }

    public void setPrint_type(int print_type) {
        this.print_type = print_type;
    }

    public int getPass_time() {
        return pass_time;
    }

    public void setPass_time(int pass_time) {
        this.pass_time = pass_time;
    }

    public int getLate_type() {
        return late_type;
    }

    public void setLate_type(int late_type) {
        this.late_type = late_type;
    }

    public int getLate_show() {
        return late_show;
    }

    public void setLate_show(int late_show) {
        this.late_show = late_show;
    }
}
