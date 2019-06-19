package com.triage;

import com.core.base.BaseController;
import com.core.constant.ConstCurd;
import com.core.toolbox.Record;
import com.moder.TriageBean;
import com.shine.model.Triage;
import com.shine.service.OrderByStateService;
import com.shine.service.PatientQueueService;
import com.shine.service.impl.OrderByStateServiceImpl;
import com.shine.service.impl.PatientQueueServiceImpl;
import com.util.BeansUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排序工具类
 * 1、锁定 ：可配置（默认2个）
 * 2、特殊号：【his传标识进行优先处理】
 * 3、优先号【1号 张三 （可配置：“老、军、急”）】
 * 4、过号【1号 张三（过）】：签到重排
 * 5、回诊号（复）【隔几叫几（如 ： 张三（复-1））】（回诊转到其他医生，做选择操作：是同级别）
 * 6、转诊号【一个医生转到另一个医生下面（1号 张三（转））】(初诊)
 * 7、加号【按号序排（排最后面）】
 * 8、迟到【分时段，排在签到时间段的最后面】
 * @since : 2019/6/14
 */
public class DataOrderUtil {
    private PatientQueueService servicepatientqueue = new PatientQueueServiceImpl();
    private OrderByStateService orderByStateService = new OrderByStateServiceImpl();
    

    public Map<String,Object> orderBy(Triage triageBean, String queueNumber, BaseController baseController) throws ParseException {
        Map<String, Object> resultMap = new HashMap<>(16);
        //正常排队的队列，优先的除外
        List<Record> result = new ArrayList<Record>();
        //页面展示的序列
        List<Record> final_result= new ArrayList<Record>();
        //队列信息
        Record rule = null;
        List<Record> list_wait = new ArrayList<>();
        /**
         * 查询等候队列状态码为(0初诊 1过号 2复诊 3部分待检 4诊室等候 5优先 6插队 7延迟 50挂起)的患者
         */
        List<Record> list_wait2 = null;
        /**
         * 优先队列集合
         */
        List<Record> list_first=null;
        /**
         * 回复诊队列
         */
        List<Record> list_agin = null;
        /**
         * 锁定等候的人
         */
        List<Record> list_wait_lockedList = null;
        /**
         * 特殊的人
         */
        List<Record> specialNumber = null;
        /**
         * 转诊号
         */
        List<Record> referralList = null;
        /**
         * 加号
         */
        List<Record> addList = null;
        /**
         *  迟到
         */
        List<Record> lateList = null;

        /**
         * 过号
         */
        List<Record> passList = null;



        if (triageBean.getReorder_type() == 1) {
            //队列
            rule=servicepatientqueue.selectTQrule3(queueNumber,triageBean.getTriage_type()+"");
            list_first = orderByStateService.queryWait(queueNumber,"5");
            list_agin=orderByStateService.queryWait(queueNumber,"2");
            lateList =  orderByStateService.queryWait(queueNumber,"54");
            //加号his暂时没标识，如果按普通患者处理，在list_wait2中一起查询
            addList = orderByStateService.queryWait(queueNumber,"");
            //转诊号his暂时没标识，如果按普通患者处理，在list_wait2中一起查询
            referralList =  orderByStateService.queryWait(queueNumber,"");
            //普通患者
            list_wait2 = orderByStateService.queryWait(queueNumber,"0,3,4,6,7,50");
            list_wait_lockedList=orderByStateService.queryLockWait(queueNumber);
            //特殊号his暂时没标识
            specialNumber = orderByStateService.queryWait(queueNumber,"");
            passList = orderByStateService.queryWait(queueNumber,"1");
        } else {
            rule=servicepatientqueue.selectTQrule4(queueNumber);
            list_first = orderByStateService.queryPageWait(queueNumber,"5");
            list_agin = orderByStateService.queryPageWait(queueNumber, "2");
            lateList = orderByStateService.queryPageWait(queueNumber, "54");
            addList = orderByStateService.queryPageWait(queueNumber, "");
            referralList = orderByStateService.queryPageWait(queueNumber, "");
            list_wait2 = orderByStateService.queryPageWait(queueNumber, "0,3,4,6,7,50");
            list_wait_lockedList=servicepatientqueue.selectpatientpagerId_lock(queueNumber);
            specialNumber = orderByStateService.queryWait(queueNumber,"");
            passList = orderByStateService.queryWait(queueNumber,"1");
        }
        final_result.addAll(list_wait_lockedList);
        final_result.addAll(specialNumber);
        final_result.addAll(list_first);
        list_wait.addAll(list_wait2);

        //回复诊等待数
        int agin = rule.getInt("return_flag_step");
        //过号间隔
        int was = rule.getInt("call_buffer");
        //迟到间隔
        int late=rule.getInt("late_flag_step");
        //锁定数
        int lockNumber = rule.getInt("lock_flag_step");

        //没有锁定的队列
        List<Record> list_unlock_wait = new ArrayList<>();

        //排序迟到队列
        //1是首位间隔，2是每位间隔，迟到排序方式
        if (triageBean.getLate_show()==1) {
            //如果迟到队列有数据添加到普通队列按号序重排
            if (lateList.size() > 0) {
                list_wait.addAll(lateList);
                Collections.sort(list_wait, (Record o1, Record o2) -> {
                    //午别（预约时间）比较，排队编号比较
                    if (o1.getInt("time_interval") <= o2.getInt("time_interval")
                            && BeansUtil.substrNumber(o1.get("register_id")) <= BeansUtil.substrNumber(o2.get("register_id"))) {
                        return 1;
                    } else {
                        return -1;
                    }
                });

            } else {
                if (lateList.size() > 0) {
                    if (list_wait.size() > 0) {
                        int i = late;
                        for (Record record : lateList) {
                            if (list_wait.size() >= i) {
                                list_wait.add(i-1, record);
                            } else {
                                list_wait.add(record);
                            }
                            i += late;
                        }
                    } else {
                        list_wait.addAll(lateList);
                    }
                }
            }
        }
        //如果转诊号不按初诊处理，在此处理


        //回复诊排序
        if (list_agin != null && list_agin.size() > 0){
            int i = agin;
            for (Record record : list_agin) {
                addUtil(list_wait,record, i);
                i += agin;
            }
        }


        //过号
        if(passList !=null && passList.size()> 0){
            int i = agin;
            for (Record record : passList) {
                addUtil(list_wait,record, i);
                i += agin;
            }
        }

        //优先集合放到等待队列前面
        if(list_first !=null && list_first.size() > 0) {
            for (int i = list_first.size() - 1; i > -1; i--) {
                list_wait.add(0, list_first.get(i));
            }
        }

        //特殊集合放到等待队列前面
        if(specialNumber != null && specialNumber.size() > 0) {
            for (int i = specialNumber.size() - 1; i > -1; i--) {
                list_wait.add(0, specialNumber.get(i));
            }
        }

        //final_result.addAll(list_first);
        final_result.addAll(list_wait);


        //查询状态不为0、5、8的患者,修改为锁定状态
        List<Record> calledList = servicepatientqueue.selectIsBegin((byte) 2, baseController.getPara("ip"));
        if (list_wait.size() > 0) {
            if( list_wait_lockedList.size() < lockNumber) {
                for (int i = 0; i < lockNumber; i++) {
                    if (final_result.get(i).getInt("late_lock") == 0) {
                        servicepatientqueue.updatePatientLateLock(final_result.get(i).getStr("patient_source_code"),
                                final_result.get(i).getStr("queue_type_id"),
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
                    }
                }
            }
        }

        if (null == final_result) {
            resultMap.put("return_msg", ConstCurd.QUEUE_TYPE_NULL_MSG);
            resultMap.put("return_code", "success");
            resultMap.put("count", 0);
        } else {
            final_result = util(final_result,baseController);
            resultMap.put("return_msg", ConstCurd.QUERY_SUCCESS_MSG);
            resultMap.put("return_code", "success");
            resultMap.put("count", final_result.size());
        }

        resultMap.put("list", final_result);
        return resultMap;
    }

    /**
     * 条件筛选
     * @param final_result 队列
     * @param baseController jfinal控制层对象
     * @return
     * @throws ParseException
     */
    public List<Record> util(List<Record> final_result,BaseController baseController) throws ParseException {
        String startTime = baseController.getPara("startTime");
        String endTime = baseController.getPara("endTime");
        String inputText = baseController.getPara("inputText");
        String checkType = baseController.getPara("checkType");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Iterator<Record> iterator = final_result.iterator();
        if (startTime == null && endTime == null && inputText == null && checkType == null ){
            return final_result;
        }
        while (iterator.hasNext()){
            Record record = iterator.next();
            if(checkType != null && !"".equals(checkType)){
                if (inputText != null && !"".equals(inputText)){
                    switch (checkType){
                        case "name":
                            if(record.getStr("patient_name").indexOf(inputText) != -1){
                                //final_result.remove(record);
                                iterator.remove();
                            }
                            break;
                        case "case":
                            if (record.getStr("patient_source_code").indexOf(inputText) != -1){
                                //final_result.remove(record);
                                iterator.remove();
                            }
                            break;
                        case "code":
                            if (record.getStr("patient_source_code").indexOf(inputText) != -1){
                                //final_result.remove(record);
                                iterator.remove();
                            }
                        default:
                            break;
                    }

                }
            }
            String date = record.getStr("fre_date");
            if(date !=null && !"".equals(date)) {
                Date parse = format.parse(date);
                if (startTime != null && !"".equals(startTime)) {
                    Date start = format.parse(startTime);
                    if (parse != null && parse.compareTo(start) == -1) {
                        iterator.remove();
                    }
                }
                if (endTime != null && !"".equals(endTime)) {
                    Date start = format.parse(endTime);
                    if (parse != null && parse.compareTo(start) > -1) {
                        iterator.remove();
                    }
                }
            }

        }
        return final_result;
    }

    /**
     * 插队的公用方法
     * @param record
     */
    public static  <T> List<T> addUtil(List<T> list,T record,int i){
        if (i < 0){
            return list;
        }
        if (list.size() >= i){
            list.add(i,record);
        }else {
            list.add(record);
        }
        return list;
    }


    class orderArylist{
        Integer T,Q,F,id;
        String name;
        List<Record> l_r;
        int getT() {
            return T;
        }
        void setT(Integer t) {
            T = t;
        }
        Integer getF() {
            return F;
        }
        void setF(Integer f) {
            F = f;
        }
        Integer getQ() {
            return Q;
        }
        void setQ(Integer q) {
            Q = q;
        }
        Integer getId() {
            return id;
        }
        void setId(Integer id) {
            this.id = id;
        }
        String getName() {
            return name;
        }
        void setName(String name) {
            this.name = name;
        }
        List<Record> getL_r() {
            return l_r;
        }
        void setL_r(List<Record> l_r) {
            this.l_r = l_r;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            orderArylist that = (orderArylist) o;
            return Objects.equals(T, that.T) &&
                    Objects.equals(Q, that.Q) &&
                    Objects.equals(F, that.F) &&
                    Objects.equals(id, that.id) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(l_r, that.l_r);
        }

        @Override
        public int hashCode() {
            return Objects.hash(T, Q, F, id, name, l_r);
        }
    }

    /**
     *
     */
    class MyComprator implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            orderArylist t1=(orderArylist)arg0;
            orderArylist t2=(orderArylist)arg1;
            if(t1.F.equals(t2.F)) {
                return t1.F<t2.F? 1:-1;
            } else {
                return t1.id>t2.id? 1:-1;
            }
        }
    }

    /**
     * 午别排序规则
     */
    class MyComprator2 implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            Record r0=(Record)arg0;
            Record r1=(Record)arg1;
            int t1=r0.getInt("time_interval");
            int t2=r1.getInt("time_interval");
            int t3=r0.getInt("register_id");
            int t4=r1.getInt("register_id");

            if(t1<t2) {
                return -1;
            } else if(t1>t2) {
                return 1;
            } else {
                if (t3<t4) {
                    return -1;
                }else if (t3>t4) {
                    return 1;
                }else {
                    return 0;
                }
            }
        }

    }

    /**
     * 操作时间的排序
     */
    class MyComprator3 implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            Record r0=(Record)arg0;
            Record r1=(Record)arg1;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
            Date t1 = null;
            try {
                t1 = df.parse(r0.getStr("opr_time"));
            } catch (Exception e) {
                t1=null;
            }
            Date t2 = null;
            try {
                t2 = df.parse(r1.getStr("opr_time"));
            } catch (Exception e) {
                t2=null;
            }

            if(t1.after(t2))
                return 1;
            else if(t1.before(t2))
                return -1;
            else
                return 0;
        }
    }
}
