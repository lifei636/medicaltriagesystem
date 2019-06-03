package com.triage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;
import java.text.SimpleDateFormat;

import org.beetl.core.statement.VarAssignExpression;
import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Doctor2queuetype;
import com.shine.model.Triage;
import com.shine.service.Doctor2queuetypeService;
import com.shine.service.Pager2queuetypeService;
import com.shine.service.TriageService;
import com.shine.service.impl.Doctor2queuetypeServiceImpl;
import com.shine.service.impl.Pager2queuetypeServiceImpl;
import com.shine.service.impl.TriageServiceImpl;

@ControllerBind(controllerKey = "/paiban")
public class ClientPaibanController extends BaseController {

	ShardKit shared = new ShardKit();
	TriageService triage = new TriageServiceImpl();
	Doctor2queuetypeService rdq = new Doctor2queuetypeServiceImpl();
	Pager2queuetypeService rpq = new Pager2queuetypeServiceImpl();

	public void DoctorPaibanWeekByDatabase() {
		String week = getPara("week");
		try {
	        if(week.isEmpty()||week.equals("")) 
	        	week="";
			}
			catch (Exception e) {
				week="";
			}
		if(week.equals(""))
		{
			Date today = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(today);
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			switch (weekday) {
				case 1:
					week = "7";
					break;
				case 2:
					week = "1";
					break;
				case 3:
					week = "2";
					break;
				case 4:
					week = "3";
					break;
				case 5:
					week = "4";
					break;
				case 6:
					week = "5";
					break;
				case 7:
					week = "6";
					break;
			}
		}
		
		String[] ar = week.split(",");
		Object[] arr=ifRepeat(ar);
		Boolean listBoolean=true;
		Map<String, Object> map2 = new HashMap<String, Object>();
		for(int i =0;i<arr.length;i++){
			List<Record> list =new ArrayList<Record>();
			if(Integer.parseInt(arr[i].toString())<8)
			{
				listBoolean=false;
				list=rdq.findeDoctorPaibanByWeek(arr[i].toString());
				map2.put("wlist"+ar[i], list);
				map2.put("wcount"+ar[i], list.size());
			}
		}
		if(listBoolean)
		{
			Date today = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(today);
			int weekday = c.get(Calendar.DAY_OF_WEEK);
			switch (weekday) {
				case 1:
					week = "7";
					break;
				case 2:
					week = "1";
					break;
				case 3:
					week = "2";
					break;
				case 4:
					week = "3";
					break;
				case 5:
					week = "4";
					break;
				case 6:
					week = "5";
					break;
				case 7:
					week = "6";
					break;
			}
			List<Record> list =new ArrayList<Record>();
			list=rdq.findeDoctorPaibanByWeek(week);
			map2.put("wlist"+week, list);
			map2.put("wcount"+week, list.size());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
			map.put("return_code", "success");
			map.put("return_msg", "查询按医生类型排班成功");
			map.put("list", map2);
			renderJson(map);
	}
	public Object[] ifRepeat(Object[] arr){  
        //用来记录去除重复之后的数组长度和给临时数组作为下标索引  
        int t = 0;  
        //临时数组  
        Object[] tempArr = new Object[arr.length];  
        //遍历原数组  
        for(int i = 0; i < arr.length; i++){  
            //声明一个标记，并每次重置  
            boolean isTrue = true;  
            //内层循环将原数组的元素逐个对比  
            for(int j=i+1;j<arr.length;j++){  
                //如果发现有重复元素，改变标记状态并结束当次内层循环  
                if(arr[i]==arr[j]){  
                    isTrue = false;  
                    break;  
                }  
            }  
            //判断标记是否被改变，如果没被改变就是没有重复元素  
            if(isTrue){  
                //没有元素就将原数组的元素赋给临时数组  
                tempArr[t] = arr[i];  
                //走到这里证明当前元素没有重复，那么记录自增  
                t++;  
            }  
        }  
        //声明需要返回的数组，这个才是去重后的数组  
        Object[]  newArr = new Object[t];  
        //用arraycopy方法将刚才去重的数组拷贝到新数组并返回  
        System.arraycopy(tempArr,0,newArr,0,t);  
        return newArr;  
    } 
	public void DoctorOrPagerPaiban() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		try {
	        if(ip.isEmpty()||ip.equals("")) 
	        	ip=shared.getIpAddr(getRequest());
			}
			catch (Exception e) {
				ip=shared.getIpAddr(getRequest());
			}
        //	ip=shared.getIpAddr(getRequest());
		//String ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		System.out.println("ip:" + ip);
		Triage rip = triage.queryTriageIp(ip);
		if (null == rip) {
			map.put("return_code", "fail");
			map.put("return_msg", "当前电脑没有分诊台配置");
			renderJson(map);
			return;
		} else {
			String tip = rip.getIp();
			if (StringKit.isBlank(tip)) {
				renderJson(error(NOT_NULL_MSG));
				return;
			}
			if ("1".equals(rip.getTriage_type().toString())) {
				List<Record> list = rdq.findDoctorPaiban(tip);
				if (null == list) {
					map.put("return_code", "fail");
					map.put("return_msg", "查询按医生类型排班失败或没有排班信息");
					renderJson(map);
					return;
				} else {
					map.put("return_code", "success");
					map.put("return_msg", "查询按医生类型排班成功");
					map.put("list", list);
					map.put("count", list.size());
					renderJson(map);
				}
			} else if ("2".equals(rip.getTriage_type().toString())) {
				List<Record> list = rpq.pager_paiban(tip);
				if (null == list) {
					map.put("return_code", "fail");
					map.put("return_msg", "查询按叫号器类型排班失败或没有排班信息");
					renderJson(map);
					return;
				} else {
					map.put("return_code", "success");
					map.put("return_msg", "查询按叫号器排班信息成功");
					map.put("list", list);
					map.put("count", list.size());
					renderJson(map);
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "系统错误，请联系管理员");
				renderJson(map);
				return;
			}
		}
	}

	public void update_pb() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			map.put("return_code", "fail");
			map.put("return_msg", "ID不能为空");
			renderJson(map);
			return;
		}
		ids = ids.substring(0, ids.length() - 1);
		String removestr = "d_";
		ids = ids.replaceAll(removestr, "");
		String onduty = getPara("onduty");
		if (StringKit.isBlank(onduty)) {
			map.put("return_code", "fail");
			map.put("return_msg", "排班不能为空");
			renderJson(map);
			return;
		}
		boolean bool = rdq.update_rdq(ids, onduty);
		if (bool) {
			map.put("return_code", "success");
			map.put("return_msg", "成功");
			renderJson(map);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "失败");
			renderJson(map);
			return;
		}
	}

	public void delete_pb() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			map.put("return_code", "fail");
			map.put("return_msg", "不能为空");
			renderJson(map);
			return;
		}
		ids = ids.substring(0, ids.length() - 1);
		String removestr = "d_";
		ids = ids.replace(removestr, "");
		int rdqnum = rdq.delete_pb(ids);
		if (rdqnum > 0) {
			map.put("return_code", "success");
			map.put("return_msg", "成功");
			renderJson(map);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "失败");
			renderJson(map);
			return;
		}
	}

	public void DoctorOrPagerPaibanAdd() {
		Map<String, Object> map = new HashMap<String, Object>();
		String doctor_id = getPara("doctor_id");
		if (StringKit.isBlank(doctor_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "医生不能为空");
			renderJson(map);
			return;
		}
		String queue_type_id = getPara("queue_type_id");
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "队列不能为空");
			renderJson(map);
			return;
		}
		String onduty = getPara("onduty");
		if (StringKit.isBlank(onduty)) {
			map.put("return_code", "fail");
			map.put("return_msg", "排班不能为空");
			renderJson(map);
			return;
		}
		Record record = rdq.finddoctorandqueuetypeid(doctor_id, queue_type_id);
		if (null != record) {
			map.put("return_code", "fail");
			map.put("return_msg", "当前医生排班已存在");
			renderJson(map);
			return;
		}
		Doctor2queuetype d = new Doctor2queuetype();
		d.setDoctor_id(Integer.parseInt(doctor_id));
		d.setQueue_type_id(Integer.parseInt(queue_type_id));
		d.setOnduty(onduty);
		boolean bool = rdq.save(d);
		if (bool) {
			map.put("return_code", "success");
			map.put("return_msg", "成功");
			renderJson(map);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "失败");
			renderJson(map);
			return;
		}
	}

	public void list_paiban() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取分诊台ip失败");
			renderJson(map);
			return;
		}
		List<Record> list = triage.queryByQueueType(ip);
		if (null == list || list.size() < 1) {
			map.put("return_code", "fail");
			map.put("return_msg", "当前分诊台下没有队列列表");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", list);
			renderJson(map);
		}
	}

	public void doctorPaibanWeek() throws SQLException {
		int page=1;
		int size=4;
		try {
		page=Integer.parseInt(getPara("page"));
		size=Integer.parseInt(getPara("size"));
		}
		catch (Exception e) {
			
		}
		Map<String, Object> map = new HashMap<String, Object>();

		String sql = "SELECT  login_id FROM rlt_doctor2queue_type WHERE datediff(DAY, getdate(), onduty) <= 6 AND datediff(DAY, getdate(), onduty) >= 0 GROUP BY login_id";
		ResultSet rs =SqlHelper.getResultSet(sql);

		ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据  
		int columnCount = md.getColumnCount(); 
		rs.last();
		int rows=rs.getRow();
		if(rows>0)
		{
			rs.beforeFirst();
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
			while (rs.next())
			{
				Map<String,Object> rowData = new HashMap<String,Object>();  
				for (int i = 1; i <= columnCount; i++) {  
					rowData.put(md.getColumnName(i), rs.getObject(i));  
				}  
				list.add(rowData);  
			}

			String login_ids="";
			for (int i=0;i<list.size(); i++) {
				login_ids+=list.get(i).get("login_id").toString()+",";
			}
			
			login_ids=login_ids.substring(0,login_ids.length()-1);
			int begin= (page-1)*size;
			
			int count=rdq.paiban_week_doctor(login_ids).size();
			List<Record> l_d=rdq.paiban_week_doctor_page(login_ids,begin,size);
			String pb_json="{\"count\":" + count + ",\"list\":[";
			
			for (int i = 0; i <l_d.size(); i++) {
				if(i>0)
					pb_json+=",";
				pb_json+="{\"name\":\""+l_d.get(i).getStr("name").toString()+"\",\"title\":\""+l_d.get(i).getStr("title").toString()+"\",\"department\":\""+l_d.get(i).getStr("department").toString()+"\"";

				for(int j=0;j<7;j++)
				{
					Date today = new Date();
					Calendar c = Calendar.getInstance();
					c.setTime(today);
					c.add(Calendar.DAY_OF_MONTH, j);// 今天+1天
					SimpleDateFormat sdf_target =new SimpleDateFormat("yyyy-MM-dd");
					String wdate = sdf_target.format(c.getTime()) ;


					
					String sql21 = "select sum(time_interval) as time_interval from rlt_doctor2queue_type where login_id='"+l_d.get(i).getStr("login_id")+"' and  onduty='"+wdate+"'";
					String sql22 = "select sum(lastno) as lastno from rlt_doctor2queue_type where login_id='"+l_d.get(i).getStr("login_id")+"' and  onduty='"+wdate+"'";
					Object   time_interval=SqlHelper.ExecScalar(sql21);
					Object lastno=SqlHelper.ExecScalar(sql22);
					if(time_interval!=null)
					{
						pb_json+=",\"wt"+j+"\":\""+time_interval.toString()+"\",\"wn"+j+"\":\""+lastno.toString()+"\"";
					}
					else
					{
						pb_json+=",\"wt"+j+"\":\"0\",\"wn"+j+"\":\"0\"";
					}
				}
				pb_json+="}";
			}

			pb_json+="]}";
			renderJson(pb_json);
		}
		else
		{
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
		}

	}
}
