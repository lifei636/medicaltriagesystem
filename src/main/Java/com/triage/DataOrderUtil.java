package com.triage;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.moder.PatientBean;
import com.moder.TriageBean;
import com.shine.service.OrderByStateService;
import com.shine.service.PatientQueueService;
import com.shine.service.impl.OrderByStateServiceImpl;
import com.shine.service.impl.PatientQueueServiceImpl;
import com.util.BeansUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@ControllerBind(controllerKey = "/DataOrderUtil")
public class DataOrderUtil extends BaseController {
    PatientQueueService service = new PatientQueueServiceImpl();
    PatientQueueService servicepatientqueue = new PatientQueueServiceImpl();
    private OrderByStateService orderByStateService = new OrderByStateServiceImpl();
    

    public Map<String,Object> orderBy(TriageBean triageBean,String queueNumber){
        Map<String, Object> resultMap = new HashMap<>(16);
        //正常排队的队列，优先的除外
        List<Record> result = new ArrayList<Record>();
        //页面展示的序列
        List<Record> final_result= new ArrayList<Record>();
        //队列信息
        Record rule = null;
        List<Record> list_wait = null;
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



        if (triageBean.getReorder_type() == 1) {
            //队列
            rule=servicepatientqueue.selectTQrule3(queueNumber,triageBean.getTriage_type()+"");
            list_first = orderByStateService.queryWait(queueNumber,"5");
            list_agin=orderByStateService.queryWait(queueNumber,"2");
            lateList =  orderByStateService.queryWait(queueNumber,"54");
            addList = orderByStateService.queryWait(queueNumber,"");
            referralList =  orderByStateService.queryWait(queueNumber,"");
            list_wait2 = orderByStateService.queryWait(queueNumber,"0,3,4,6,7,50");
            list_wait_lockedList=orderByStateService.queryLockWait(queueNumber);
            specialNumber = orderByStateService.queryWait(queueNumber,"");
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
        }
        final_result.addAll(list_wait_lockedList);
        final_result.addAll(specialNumber);
        final_result.addAll(list_first);
        list_wait = new ArrayList<>();
        list_wait.addAll(list_wait2);
        list_wait.addAll(referralList);
        list_wait.addAll(addList);
        list_wait.addAll(lateList);
        List<PatientBean> plist = new ArrayList<>();
        try {
            for (Record record : list_wait) {
                PatientBean bean = BeansUtil.mapToBean(record, PatientBean.class);
                plist.add(bean);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return resultMap;
    }

    public <T> T mapToBean(Map map,Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Method[] methods = clazz.getMethods();
        Field[] declaredFields = clazz.getDeclaredFields();
        T t = clazz.newInstance();
        for (Field field:declaredFields){
            if(map.containsKey(field.getName())){
                String value = map.get(field.getName()).toString();
                String setName = field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
                Class<?> type = field.getType();
                Method method = clazz.getMethod(setName, type);

                switch (type.getName()){
                    case "String":
                        method.invoke(t,value);
                        break;
                    case "int":
                        method.invoke(t,Integer.parseInt(value));
                        break;
                    case "double":
                        method.invoke(t,Double.parseDouble(value));
                        break;
                    case "float":
                        method.invoke(t,Float.parseFloat(value));
                        break;
                    case "boolean":
                        method.invoke(t,Boolean.parseBoolean(value));
                        break;
                        default:
                            method.invoke(t,value);
                }

            }
        }
        return t;
    }


}
