package com.triage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.beetl.sql.core.kit.StringKit;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.core.toolbox.support.DateTime;
import com.ibm.db2.jcc.a.a;
import com.shine.model.PatientQueue;
import com.shine.model.QueueType;
import com.shine.model.Triage;
import com.shine.service.DoctorService;
import com.shine.service.PatientQueueService;
import com.shine.service.QueueTypeService;
import com.shine.service.TerminalService;
import com.shine.service.TriageService;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.PatientQueueServiceImpl;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.impl.TerminalServiceImpl;
import com.shine.service.impl.TriageServiceImpl;
import com.jfinal.kit.StrKit;
import com.mysql.fabric.xmlrpc.base.Data;

@ControllerBind(controllerKey = "/clientPatientQueue")
public class ClientPatientQueueController extends BaseController {

	PatientQueueService service = new PatientQueueServiceImpl();
	QueueTypeService queueservice=new QueueTypeServiceImpl();
	ShardKit shardkit = new ShardKit();
	TriageService triage = new TriageServiceImpl();
	DoctorService doctor = new DoctorServiceImpl();
	TerminalService terminal = new TerminalServiceImpl();
	PatientQueueService servicepatientqueue = new PatientQueueServiceImpl();



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
	class MyComprator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			orderArylist t1=(orderArylist)arg0;
			orderArylist t2=(orderArylist)arg1;
			if(t1.F != t2.F)
			{
				return t1.F<t2.F? 1:-1;
			}
			else
			{
				return t1.id>t2.id? 1:-1;
			}
		}
	}

	class MyComprator2 implements Comparator {
	    public int compare(Object arg0, Object arg1) {
	    	Record r0=(Record)arg0;
	    	Record r1=(Record)arg1;
	        int t1=r0.getInt("time_interval");
	        int t2=r1.getInt("time_interval");
	        int t3=r0.getInt("register_id");
	        int t4=r1.getInt("register_id");
	       
	        	if(t1<t2)
	        		return	-1;
	        	else if(t1>t2)
	        		return 1;
	        	else
	        	{
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
	class MyComprator3 implements Comparator {
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
	public void Manual_quhao2() {
		Map<String, Object> map = new HashMap<String, Object>();
		String patientName = getPara("patientName");
		if (StringKit.isBlank(patientName)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String QueueNumber = getPara("QueueNumber");
		if (StringKit.isBlank(QueueNumber)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String type=getPara("type");

		String source_code= getPara("source_code");
		String s = new SimpleDateFormat("yyyyMMddhhmmssSSSS").format(new Date());
		PatientQueue record = service.findByQueueNum(QueueNumber);

		PatientQueue patientqueue = new PatientQueue();
		if(type.equals("1"))
		{
			patientqueue.setPatient_name(patientName);
		}
		else
			patientqueue.setPatient_name(patientName + record.getRegister_id());
		patientqueue.setQueue_type_id(Integer.parseInt(QueueNumber));
		patientqueue.setRegister_id(record.getRegister_id());
		patientqueue.setQueue_num(record.getRegister_id());
		patientqueue.setState_patient(0);
		patientqueue.setSub_queue_order(0);
		patientqueue.setIs_display(2);
		patientqueue.setIs_deleted(0);
		patientqueue.setTime_interval(0);
		patientqueue.setPatient_id(s);
		patientqueue.setOpr_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
		if (!StringKit.isBlank(source_code)) {
			s=source_code;
		}
		patientqueue.setPatient_source_code(s);
		Boolean bool = service.save(patientqueue);
		if (!bool) {
			map.put("return_msg", SAVE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
		} else {
			map.put("return_msg", SAVE_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("number", record.getRegister_id());
			//Date d = new Date();
			map.put("dtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
			renderJson(map);
		}
	}


	public void Manual_quhao_check() {
		Map<String, Object> map = new HashMap<String, Object>();
		String source_code = getPara("source_code");
		if (StringKit.isBlank(source_code)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			map.put("count", 0);
			renderJson(map);
			return;
		}
		String queue_type_id = getPara("queue_type_id");

		Record record =	servicepatientqueue.selectPatientBySourceCode(queue_type_id, source_code);
		if (record!=null) {
			map.put("patient", record);
			map.put("return_msg", "查询成功");
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", "查询失败，没有挂号信息");
			map.put("return_code", "fail");
			renderJson(map);
		}
	}
	public void Manual_quhao2_reserve() {
		Map<String, Object> map = new HashMap<String, Object>();
		String patientName = getPara("patientName");
		if (StringKit.isBlank(patientName)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String QueueNumber = getPara("QueueNumber");
		if (StringKit.isBlank(QueueNumber)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String type=getPara("type");

		String source_code= getPara("source_code");
		String s = new SimpleDateFormat("yyyyMMddhhmmssSSSS").format(new Date());
		String patient_idString=s;
		String dateString=getPara("date");
		if (StringKit.isBlank(dateString)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String begin_timeString=getPara("begin_time");
		String end_timeString=getPara("end_time");
		if (StringKit.isBlank(begin_timeString)) {
			begin_timeString=dateString+" 08:00";
		}
		if (StringKit.isBlank(end_timeString)) {
			end_timeString=dateString+" 18:00";
		}
		String pnameString= "";
		int resever_id=service.getReserveMaxNmb(QueueNumber, dateString);
		if(type.equals("1"))
		{
			pnameString=patientName;
		}
		else
			pnameString=patientName+resever_id;
		
		
		if (!StringKit.isBlank(source_code)) {
			s=source_code;
		}
		
		boolean bool = service.InsertReserve(QueueNumber, patient_idString, pnameString, s, String.valueOf(resever_id), dateString,begin_timeString,end_timeString);
		if (!bool) {
			map.put("return_msg", SAVE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
		} else {
			map.put("return_msg", SAVE_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("resever_id", resever_id);
			map.put("pnameString", pnameString);
			map.put("patient_id", patient_idString);
			map.put("patient_code", s);
			map.put("dtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
			renderJson(map);
		}
	}
	
	public  int  getWait(String queue_type_id,String r_id) {
		List<Record> list = service.listPatient(queue_type_id);
		int w=0;
		for (int i = 0; i < list.size(); i++) {
			if(Integer.parseInt(list.get(i).get("register_id").toString())<Integer.parseInt(r_id)) {
				w+=1;
			}
			else {
				break;
			}
		}
		return w;
	}
	public  void  getWait2() {
		Map<String, Object> map = new HashMap<String, Object>();
		String queue_type_id=getPara("queue_type_id");
		String r_id=getPara("r_id");
		List<Record> list = service.listPatient(queue_type_id);
		int w=0;
		for (int i = 0; i < list.size(); i++) {
			if(Integer.parseInt(list.get(i).get("register_id").toString())<Integer.parseInt(r_id)) {
				w+=1;
			}
			else {
				break;
			}
		}
		map.put("return_msg", "查询成功");
		map.put("return_code", "success");
		map.put("wait", w);
		renderJson(map);
		return;
	}



	public void Manual_quhao() {
		Map<String, Object> map = new HashMap<String, Object>();
		String patient_id = getPara("id");

		if (StringKit.isBlank(patient_id)) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		int r = servicepatientqueue.updateQuhaoIsdisplay(patient_id);
		if (r==1) {
			map.put("return_msg", "取号成功");
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", SAVE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
		}
	}

	public void Manual() {
		Map<String, Object> map = new HashMap<String, Object>();
		String patientName = getPara("patientName");
		if (StringKit.isBlank(patientName)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}

		String QueueNumber = null;


		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		if (null == tri) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊类型失败");
			renderJson(map);
			return;
		}
		if(tri.getReorder_type()==1)
			QueueNumber=getPara("QueueNumber");
		else
			QueueNumber=queueservice.FristPagerType(getPara("QueueNumber"));
		if (StringKit.isBlank(QueueNumber)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String s = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String queue_type_id = getPara("QueueNumber");
		QueueType r_queuetype=queueservice.findById(QueueNumber);
		Record record2 = null;
		if(tri.getReorder_type()==1)
		{
			if(r_queuetype.getIs_ckin_order()==2)
				record2=service.findmaxregisterid2(QueueNumber);
			else
				record2=service.findmaxregisterid(QueueNumber);
		}
		else
		{
			if(r_queuetype.getIs_ckin_order()==2)
				record2=service.findpagermaxregistreid2(queue_type_id);
			else
				record2=service.findpagermaxregistreid(queue_type_id);
		}
		String num="";
		if(!StringKit.isEmpty(r_queuetype.getReserve_numlist()))
		{
			if(Integer.parseInt(r_queuetype.getReserve_numlist())>=record2.getInt("register_id"))
			{
				num=String.valueOf(Integer.parseInt(r_queuetype.getReserve_numlist())+1);
			}
			else
				num=String.valueOf(record2.getInt("register_id"));
		}
		else
			num =String.valueOf(record2.getInt("register_id"));

		//System.out.println(record.getRegister_id());
		PatientQueue patientqueue = new PatientQueue();
		patientqueue.setPatient_name(patientName);
		patientqueue.setQueue_type_id(Integer.parseInt(QueueNumber));

		patientqueue.setRegister_id(num);
		patientqueue.setQueue_num(num);
		patientqueue.setState_patient(0);
		patientqueue.setSub_queue_order(0);
		patientqueue.setIs_display(2);
		patientqueue.setIs_deleted(0);
		patientqueue.setTime_interval(0);
		patientqueue.setPatient_id(s);
		patientqueue.setOpr_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
		patientqueue.setPatient_source_code(s);
		Boolean bool2 = service.save(patientqueue);
		if (!bool2) {
			map.put("return_msg", SAVE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		} else {
			map.put("return_msg", SAVE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		}
	}
	//分诊台等候患者列表
	public void listPatient_wait() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		String QueueNumber = getPara("queue_number");
		if (StringKit.isBlank(QueueNumber)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		//分诊台
		Triage rip=triage.queryTriageIp(ip);
		//正常排队的队列，优先的除外
		List<Record> result = new ArrayList<Record>();
		//页面展示的序列
		List<Record> final_result= new ArrayList<Record>();
		//叫号器
		Record rule = null;
		//
		List<Record> list_wait = null;
		//查询等候队列状态码为(0初诊 1过号 2复诊 3部分待检 4诊室等候 5优先 6插队 7延迟 50挂起)的患者
		List<Record> list_wait2 = null;
		//优先队列集合
		List<Record> list_first=null;
		//复诊队列
		List<Record> list_agin = null;
		//未到过号的人
		List<Record> list_was = null;
		//状态码为8的队列
		List<Record> list_late=null;
		//锁定等候的人
		List<Record> list_wait_lockedList = null;
		if (rip.getReorder_type() == 1) {
			rule=servicepatientqueue.selectTQrule3(QueueNumber,rip.getTriage_type().toString());
			list_first = service.listPatient_first(QueueNumber);
			list_wait2=service.listPatient(QueueNumber);
			list_agin =  service.listPatient_agin(QueueNumber);
			list_was =  service.listPatient_pass(QueueNumber);
			list_late = service.listPatient_late(QueueNumber);
			list_wait_lockedList=servicepatientqueue.listPatient_lock(QueueNumber);
		} else {
			rule=servicepatientqueue.selectTQrule4(QueueNumber);
			list_first = servicepatientqueue.selectpatientpagerId(QueueNumber, "5");
			list_wait2= servicepatientqueue.selectpatientpagerId(QueueNumber, "0,3,4,6,7,50");
			list_agin = servicepatientqueue.selectpatientpagerId(QueueNumber, "2");
			list_was = servicepatientqueue.selectpatientpagerId(QueueNumber, "54");
			list_late = servicepatientqueue.selectpatientpagerId(QueueNumber, "8");
			list_wait_lockedList=servicepatientqueue.selectpatientpagerId_lock(QueueNumber);
		}

		int Tagin = rule.getInt("return_flag_step");
		int Twas = rule.getInt("call_buffer");
		int Tlate=rule.getInt("late_flag_step");
		int Tfirst=rule.getInt("first_flag_step");
		int Qagin = rule.getInt("call_return_rule_flag");
		int Qwas = rule.getInt("call_pass_rule_flag");
		int Qlate=rule.getInt("call_late_rule_flag");
		int Qfirst=rule.getInt("call_first_rule_flag");
		int Qfirst2=rule.getInt("call_first_rule_flag2");

		int Fagin = rule.getInt("call_return_first_flag");
		int Fwas = rule.getInt("call_pass_first_flag");
		int Flate = rule.getInt("call_late_first_flag");
		int Ffirst=rule.getInt("call_first_first_flag");

		//没有锁定的队列
		List<Record> list_unlock_wait = new ArrayList<>();
		ArrayList<orderArylist> list = new ArrayList<>();
		if (rip.getLate_show()==1) {
			if (list_late.size() > 0) {
				if (list_wait2.size() > 0) {
					
					for (int i = 0; i < list_late.size(); i++) {
						if (list_late.get(i).getInt("late_lock") == 0)
							list_unlock_wait.add(list_late.get(i));
					}
					for (int i = 0; i < list_wait2.size(); i++) {
						if (list_wait2.get(i).getInt("late_lock") == 0)
							list_unlock_wait.add(list_wait2.get(i));
					}
					if (list_unlock_wait.size() > 0)
						Collections.sort(list_unlock_wait, new MyComprator2());
					list_wait.addAll(list_unlock_wait);
				} else {
					// Collections.sort(list_late, new MyComprator3());
					for (int i = 0; i < list_late.size(); i++) {
						if (list_late.get(i).getInt("late_lock") == 0)
							list_unlock_wait.add(list_late.get(i));
					}
					list_wait = list_unlock_wait;
				}
			} else
			{
				for (int i = 0; i < list_wait2.size(); i++) {
					//判断病人是否被等候锁定
					if (list_wait2.get(i).getInt("late_lock") == 0)
						list_unlock_wait.add(list_wait2.get(i));
				}
				list_wait =list_unlock_wait ;
			}
		} else {
			for (int i = 0; i < list_wait2.size(); i++) {
				if (list_wait2.get(i).getInt("late_lock") == 0)
					list_unlock_wait.add(list_wait2.get(i));
			}
			list_wait=list_unlock_wait;
			if (list_late.size() > 0) {
				orderArylist oaLate = new orderArylist();
				List<Record> list_late_unlocked=new ArrayList<Record>();
				for (int i = 0; i < list_late.size(); i++) {
					if (list_late.get(i).getInt("late_lock") == 0)
						list_late_unlocked.add(list_late.get(i));
				}
				oaLate.F = Flate;
				oaLate.Q = Qlate;
				oaLate.T = Tlate;
				oaLate.id = 2;
				oaLate.name = "late";
				oaLate.l_r = list_late_unlocked;
				list.add(oaLate);
			}
		}
		if (list_agin.size() > 0) {
			orderArylist oaAgin = new orderArylist();
			List<Record> list_agin_unlocked=new ArrayList<Record>();
			for (int i = 0; i < list_agin.size(); i++) {
				if (list_agin.get(i).getInt("late_lock") == 0)
					list_agin_unlocked.add(list_agin.get(i));
			}
			oaAgin.F = Fagin;
			oaAgin.Q = Qagin;
			oaAgin.T = Tagin;
			oaAgin.id = 1;
			oaAgin.name = "agin";
			oaAgin.l_r = list_agin_unlocked;
			list.add(oaAgin);
		}

		if (list_was.size() > 0) {
			orderArylist oaWas = new orderArylist();
			List<Record> list_was_unlocked=new ArrayList<Record>();
			for (int i = 0; i < list_was.size(); i++) {
				if (list_was.get(i).getInt("late_lock") == 0)
					list_was_unlocked.add(list_was.get(i));
			}
			oaWas.F = Fwas;
			oaWas.Q = Qwas;
			oaWas.T = Twas;
			oaWas.id = 3;
			oaWas.name = "was";
			oaWas.l_r = list_was_unlocked;
			list.add(oaWas);
		}


		if (list.size() > 0) {
			Collections.sort(list, new MyComprator());
			for (int i = 0; i < list_wait.size(); i++) {
				for (int j = 0; j < list.size(); j++) {
					orderArylist ary = list.get(j);
					if (ary.getF() == ary.getT()) {
						if (ary.getL_r().size() > 0) {
							result.add(ary.getL_r().get(0));
							ary.getL_r().remove(0);
						}
						ary.setF(1);
					} else
						ary.setF(ary.getF() + 1);
				}
				result.add(list_wait.get(i));
			}
			for (int i = 0; i < list.size(); i++) {
				orderArylist ary = list.get(i);
				if (ary.getL_r().size() > 0) {
					result.addAll(ary.getL_r());
				}
			}
		} else {
			result = list_wait;
		}
		final_result.addAll(list_wait_lockedList);
		//优先锁定的队列
		List<Record> list_first_unlocked=new ArrayList<Record>();
		for (int i = 0; i < list_first.size(); i++) {
			if (list_first.get(i).getInt("late_lock") == 0) {
				list_first_unlocked.add(list_first.get(i));
			}
		}
		final_result.addAll(list_first_unlocked);
		final_result.addAll(result);
		
		
		List<Record> calledList = servicepatientqueue.selectIsBegin("2", ip);
		if (calledList.size() > 0) {
			if(final_result.size()<rule.getInt("late_flag_step"))
			{
				for (int i = 0; i < final_result.size(); i++) {
					if (final_result.get(i).getInt("late_lock") == 0) {
							servicepatientqueue.updatePatientLateLock(final_result.get(i).getStr("patient_source_code"),
									final_result.get(i).getStr("queue_type_id"),
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
					}
				}
			}
		}
		if (null == final_result) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			final_result = util(final_result);
			map.put("return_msg", QUERY_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("count", final_result.size());
			map.put("list", final_result);
			renderJson(map);
		}
	}

	public List<Record> util(List<Record> final_result) throws ParseException {
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		String inputText = getPara("inputText");
		String checkType = getPara("checkType");
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
	//分诊台未到过号患者列表
	public void listPatient_pass() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String QueueNumber = getPara("queue_number");
		if (StringKit.isBlank(QueueNumber)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		List<Record> list =new ArrayList<Record>();
		if(tri.getReorder_type()==1)
			list = service.listPatient_pass(QueueNumber);
		else
			list = servicepatientqueue.selectpatientpagerId(QueueNumber, "54");
		if (null == list) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			list = this.util(list);
			map.put("return_msg", QUERY_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("count", list.size());
			map.put("list", list);
			renderJson(map);
		}
		renderJson(map);
	}
	//分诊台已就诊患者列表
	public void listPatient_already() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String QueueNumber = getPara("queue_number");
		if (StringKit.isBlank(QueueNumber)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		List<Record> list =new ArrayList<Record>();
		if(tri.getReorder_type()==1)
			list = service.listPatient_already(QueueNumber);
		else
			list = servicepatientqueue.selectpatientpagerId(QueueNumber, "53");

		if (null == list) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			list = this.util(list);
			map.put("return_msg", QUERY_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("count", list.size());
			map.put("list", list);
			renderJson(map);
		}
		renderJson(map);
	}
	//分诊台过号患者列表
	public void listPatient_passno() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String QueueNumber = getPara("queue_number");
		if (StringKit.isBlank(QueueNumber)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		List<Record> list =new ArrayList<Record>();
		if(tri.getReorder_type()==1)
			list = service.listPatient_passno(QueueNumber);
		else
			list = servicepatientqueue.selectpatientpagerId(QueueNumber, "1,52");
		if (null == list) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			list = this.util(list);
			map.put("return_msg", QUERY_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("count", list.size());
			map.put("list", list);
			renderJson(map);
		}
		renderJson(map);
	}
	//未报到列表
	public void listPatient_nodisplay() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		String QueueNumber = getPara("queue_number");
		if (StringKit.isBlank(QueueNumber)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		List<Record> list =new ArrayList<Record>();
		if(tri.getReorder_type()==1)
			list = service.listPatient_nodisplay(QueueNumber);
		else
			list = servicepatientqueue.selectpatientpagerId_nodisplay(QueueNumber);
		if (null == list) {
			renderJson(error(QUERY_FAIL_MSG));
			return;
		} else {
			list = this.util(list);
			map.put("return_msg", QUERY_SUCCESS_MSG);
			map.put("return_code", "success");
			map.put("count", list.size());
			map.put("list", list);
			renderJson(map);
		}
		renderJson(map);
	}
	//分诊台设置患者优先
	public void patientFirst() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");

		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String QueueNumber = getPara("queue_type_id");
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
			{
				ip=shardkit.getIpAddr(getRequest());
			}
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		//String ip = shardkit.getIpAddr(getRequest());
		Triage tri=triage.queryTriageIp(ip);
		boolean bool =false;
		if(tri.getReorder_type()==1)
		{
			//叫号器类型
			Record record=service.getPagerIsLogin(ids);
			if(record!=null)
			{
				//根据队列id获取优先队列数量
				int v1=service.getFirstByQueutTypeID(Integer.parseInt(QueueNumber)).size();
				//优先等待数
				int v2=tri.getFirst_flag_step();
				if(v1<v2&&v1>0)
				{
					int id=record.getInt("id");
					int v3=record.getInt("call_first_first_flag");
					if(v3-v1>0||v2-v1>0) {
						//病人状态码改为5（优先队列最后一个）
						service.updateFirstCallStatus(v3 - v1, v2 - v1, id);
					}
				}
			}

			//病人状态码改为5（优先）
			bool =service.patientFirst(ids);
		}
		else 
		{
			Record record=service.getPagerIsLogin2(Integer.parseInt(QueueNumber));
			if(record!=null)
			{
				int v1=service.getFirstByQueutTypeID(Integer.parseInt(QueueNumber)).size();
				int v2=tri.getFirst_flag_step();
				int id=record.getInt("id");
				if(record.getInt("call_first_rule_flag")==0&&record.getInt("call_first_rule_flag2")==0&&record.getInt("call_first_first_flag")>2&&v1<v2)
				{
					service.updateFirstCallStatus(v1, v2, id);
				}
			}
			bool=service.patientFirstByPagerList(QueueNumber, ids);
		}

		if (bool) {
			map.put("return_msg", SETTING_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", SETTING_ERROR_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}
	
	public void ThirdSelectPatient() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");
		if (StringKit.isBlank(code)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入患者卡号");
			renderJson(map);
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals(""))
			{
				ip=shardkit.getIpAddr(getRequest());
			}
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "未能获取分诊台IP");
			renderJson(map);
			return;
		}
		List<Record> record = new ArrayList<Record>();
		Triage tri=triage.queryTriageIp(ip);
		if(tri.getReorder_type()==1)
			record= service.findCodeThirdSelectPatient(code, ip);
		else
			record=service.findCodePagerThirdSelectPatient(code, ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return;
		}
		else
		{
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", record);
			map.put("count", record.size());
			renderJson(map);
			return;
		}
	}

	
	
	public void Scan() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");
		if (StringKit.isBlank(code)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入患者卡号");
			renderJson(map);
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "未能获取分诊台IP");
			renderJson(map);
			return;
		}
		List<Record> record = new ArrayList<Record>();
		Triage tri=triage.queryTriageIp(ip);
		if(tri.getReorder_type()==1)
			record= service.findCode(code, ip);
		else
			record=service.findCodePager(code, ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return;
		}
		else
		{
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", record);
			map.put("count", record.size());
			renderJson(map);
			return;
		}
	}

	public void Scan2() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");
		if (StringKit.isBlank(code)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入患者卡号");
			renderJson(map);
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "未能获取分诊台IP");
			renderJson(map);
			return;
		}
		List<Record> record = new ArrayList<Record>();
		Triage tri=triage.queryTriageIp(ip);
		if(tri.getReorder_type()==1)
			if(tri.getTriage_type()==1)
				record= service.findCode(code, ip);
			else
				record=service.findCodePager(code, ip);
		else
			record=service.findCodePager(code, ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return;
		}
		else
		{
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", record);
			map.put("count", record.size());
			renderJson(map);
			return;
		}
	}
	public void ScanBaodao()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");
		String patient_name = getPara("patient_name");
		String queue_type_id=getPara("queue_type_id");
		if (StringKit.isBlank(code)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入患者卡号");
			renderJson(map);
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}

		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "未能获取分诊台IP");
			renderJson(map);
			return;
		}
		Triage tri=triage.queryTriageIp(ip);
		if(tri.getReorder_type()==1)
		{
			renderJson(ScanAdd(code , ip,tri.getLate_show().toString(),tri.getLate_flag_step(),queue_type_id,patient_name));
			return;
		}
		else 
		{
			
			String QueueNumber=queueservice.FristPagerType(queue_type_id);
			if (StringKit.isBlank(QueueNumber)) {
				renderJson(error(NOT_NULL_MSG));
				return;
			}
			QueueType r_queuetype=queueservice.findById(QueueNumber);
			String ylh= r_queuetype.getReserve_numlist().trim();
			//Integer.parseInt(s)
			boolean ise=StringKit.isEmpty(ylh);
			boolean isylh=ylh.equals("0");
			if(!isylh&&!ise)
			{
				if(!Boolean.parseBoolean(getPara("yl")))
				{
					Boolean bool1=false;
					if(r_queuetype.getIs_ckin_order()==2)
						bool1=service.baodaopager(code,queue_type_id);
					else
					{
						int r=service.findpagermaxregistreid(QueueNumber).getInt("register_id");
						if (r==0)
							r=1;
						if(!StringKit.isEmpty(r_queuetype.getReserve_numlist()))
						{
							if(r<=Integer.parseInt(r_queuetype.getReserve_numlist()))
								r=Integer.parseInt(r_queuetype.getReserve_numlist())+1;
						}
						bool1=service.baodaopager2(code,queue_type_id,r);
					}
					if(bool1)
					{
						Record record=service.getpatientbypagerlist(code, queue_type_id);
						Record map2 = Record.create();
						map2.put("source_code",code);
						map2.put("patient_name",patient_name);
						map2.put("id",record.getInt("id"));
						service.insertPatient(map2);

						map.put("return_msg",
								"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
						map.put("return_code", "success");
						map.put("queue_type_id", record.getInt("queue_type_id"));
						map.put("id", record.getInt("id"));
						map.put("state_patient", record.get("state_patient"));
						map.put("patient_name", record.getStr("patient_name"));
						map.put("queue_type_name", record.getStr("queue_type_name"));
						map.put("print_type", record.getStr("print_type"));
						map.put("register_id", record.getStr("register_id"));
					}
					else 
					{
						map.put("return_code", "fail");
						map.put("return_msg", "报到失败");
					}
				}
				else
				{
					Record record=service.getpatientbypagerlist(code, queue_type_id);
					Record map2 = Record.create();
					map2.put("source_code",code);
					map2.put("patient_name",record.getStr("patient_name"));
					map2.put("id",record.getInt("id"));
					Integer ylList=service.getylnmb(queue_type_id,Integer.parseInt(r_queuetype.getReserve_numlist()));
					if(ylList<=Integer.parseInt(r_queuetype.getReserve_numlist()))
					{
						service.insertPatient(map2);
						if(ylList==0)
							ylList=1;
						map.put("return_msg",
								"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
						service.baodaopager2(code,queue_type_id, ylList);
						record=service.getpatientbypagerlist(code, queue_type_id);
						map.put("return_code", "success");
						map.put("queue_type_id", record.getInt("queue_type_id"));
						map.put("id", record.getInt("id"));
						map.put("state_patient", record.get("state_patient"));
						map.put("patient_name", record.getStr("patient_name"));
						map.put("queue_type_name", record.getStr("queue_type_name"));
						map.put("print_type", record.getStr("print_type"));
						map.put("register_id", record.getStr("register_id"));
						map.put("register_id2", record.getStr("register_id2"));
						map.put("state_patient2", record.get("state_patient2"));
					}
					else 
					{
						map.put("return_code", "fail");
						map.put("return_msg", "预留号已满");
					}
				}
			}
			else
			{
				Record record=service.getpatientbypagerlist(code, queue_type_id);
				Record map2 = Record.create();
				map2.put("source_code",code);
				map2.put("patient_name",record.getStr("patient_name"));
				map2.put("id",record.getInt("id"));
				if(record.getInt("is_display")==1)
				{
					Boolean bool1=false;
					if(r_queuetype.getIs_ckin_order()==2)
						bool1=service.baodaopager(code,queue_type_id);
					else
					{
						int r =service.findpagermaxregistreid(queue_type_id).getInt("register_id");
						//int r=service.findpagermaxregistreid(QueueNumber).getInt("register_id");
						if(r==0)
							r=1;
						bool1=service.baodaopager2(code,queue_type_id,r);
					}
					if(bool1)
					{
						service.insertPatient(map2);
						map.put("return_msg",
								"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
						map.put("return_code", "success");
						map.put("queue_type_id", record.getInt("queue_type_id"));
						map.put("id", record.getInt("id"));
						map.put("state_patient", record.get("state_patient"));
						map.put("patient_name", record.getStr("patient_name"));
						map.put("queue_type_name", record.getStr("queue_type_name"));
						map.put("print_type", record.getStr("print_type"));
						map.put("register_id", record.getStr("register_id"));
						map.put("register_id2", record.getStr("register_id2"));
						map.put("state_patient2", record.get("state_patient2"));
					}
					else 
					{
						map.put("return_code", "fail");
						map.put("return_msg", "报到失败");
					}
				}
				else {
					int	disp2=0;
					if(record.getInt("state_patient")==53)
					    disp2=service.updatePatientbyPagerIDandCode(queue_type_id,code,2,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
					else if(record.getInt("state_patient")==1) {
						disp2=service.updatePatientbyPagerIDandCode(queue_type_id,code,8,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
					}
					if(disp2>0)
					{
						record=service.getpatientbypagerlist(code, queue_type_id);
						map.put("return_msg",
								"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
						map.put("return_code", "success");
						map.put("queue_type_id", record.getInt("queue_type_id"));
						map.put("id", record.getInt("id"));
						map.put("state_patient", record.get("state_patient"));
						map.put("patient_name", record.getStr("patient_name"));
						map.put("queue_type_name", record.getStr("queue_type_name"));
						map.put("print_type", record.getStr("print_type"));
						map.put("register_id", record.getStr("register_id"));
						map.put("register_id2", record.getStr("register_id2"));
						map.put("state_patient2", record.get("state_patient2"));
					}
					else 
					{
						map.put("return_code", "fail");
						map.put("return_msg", "报到失败");
					}
				}
				
			}
			renderJson(map);
			return;
		}

	}

	public void ScanBaodaoBydoor()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");
		String pagerip=getPara("pagerip");
		if (StringKit.isBlank(code)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请输入患者卡号");
			renderJson(map);
			return;
		}
		List<Record> record = new ArrayList<Record>();
		String ips ="";// getPara("ip");

		//if(ips.isEmpty()||ips.equals("")) 
		ips=shardkit.getIpAddr(getRequest());
		String triageIp =terminal.findbytriageip(ips).getStr("ip");
		Triage tri=triage.queryTriageIp(triageIp);
		String ip=tri.getIp();
		if(tri.getReorder_type()==1)
			if(tri.getTriage_type()==1)
				record= service.findCode(code, ip);
			else
				record=service.findCodePager(code, ip);
		else
			record=service.findCodePager(code, ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return;
		}
		else
		{
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", record);
			map.put("count", record.size());
			renderJson(map);
			return;
		}
	}
	public Map<String, Object> SanAddBydoor() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");
		String patient_name = getPara("patient_name");
		String queue_type_id=getPara("queue_type_id");
		String pagerid=getPara("queue_type_id");
		String ip=shardkit.getIpAddr(getRequest());
		String t_t=triage.findtriageBytemIP(ip).getStr("triage_type");
		Record record =null;
		if(t_t.equals("2"))
		{
			//
			record =servicepatientqueue.selectPatientbyPagerIDAndCode(pagerid,code);

			queue_type_id=queueservice.FristPagerType(pagerid);
		}
		else
		{
			record=service.selectPatientbycode(code, queue_type_id);
		}

		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return map;
		}
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int h=c.get(Calendar.HOUR_OF_DAY);
		int id=record.getInt("id");
		Record map2 = Record.create();
		map2.put("source_code",code);
		map2.put("patient_name",patient_name);
		map2.put("id",id);
		if(h<12)
		{
			if(record.getInt("time_interval")==3)
			{
				map.put("return_code", "fail");
				map.put("return_msg", "不在报到时间内");
				renderJson(map);
				return map;
			}
		}
		if(t_t.equals("1"))
		{
			String is_display = record.getInt("is_display").toString();

			if ("1".equals(is_display)) {
				int is_ckin_order=service.queuetypeisckinorder(queue_type_id);
				int disp=0;

				if(is_ckin_order==1)
				{
					int register=service.findmaxregisteridbyisdisplay(queue_type_id);
					if(register<=0) {
						register = 1;
					}
					disp=service.updatedisplaybyscanreorder(String.valueOf(register),id);

				}
				else
				{
					Record tr=triage.findtriageBytemIP(ip);
					int late= tr.getInt("late_flag_step");
					if(late>0)
					{
						if(tr.getInt("late_type")==1)
						{
							if(h<12&&Integer.parseInt(record.getStr("time_interval"))==2)
							{
								disp = service.updatedisplaybyscan(id);

							}
							else
							{
								int max=service.findmaxregisteridnow(queue_type_id);
								int now= Integer.parseInt(record.getStr("register_id"));
								if(max>now)
								{
									disp=service.updatedisplaybyscan2(id);
								} else {
									disp = service.updatedisplaybyscan(id);
								}
							}
						}
						else
						{
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = null;  
							try {   
								date = df.parse(record.getStr("last_time"));
							} catch (Exception e) {   
								date=null;
							}
							if(date==null)
							{
								try {   
									date = df.parse(record.getStr("fre_date"));
								} catch (Exception e) {   
									date=new Date();
								}
							}
							int late_time=tr.getInt("late_time");
							Calendar rightNow = Calendar.getInstance();
							rightNow.setTime(date);
							rightNow.add(Calendar.MINUTE,late_time);
							Date dt1=rightNow.getTime();
							Date dt2=new Date();
							
							if(dt1.after(dt2))
							{
								disp = service.updatedisplaybyscan(id);
							}
							else
								{
								disp=service.updatedisplaybyscan2(id);
								}
						}
						
						/*int max=service.findmaxregisteridnow(queue_type_id);
						int now= Integer.parseInt(service.findById(id).getRegister_id());
						if(max>now)
						{
							disp=service.updatedisplaybyscan2(id);
						}
						else
							disp = service.updatedisplaybyscan(id);*/
					}
					else
					{
						disp = service.updatedisplaybyscan(id);
					}
					
				}
				
				if (disp > 0) {

					service.insertPatient(map2);

					map.put("return_msg",
							"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
					map.put("return_code", "success");
					map.put("queue_type_id", record.getInt("queue_type_id"));
					map.put("id", record.getInt("id"));
					map.put("state_patient", record.get("state_patient"));
					map.put("patient_name", record.getStr("patient_name"));
					map.put("queue_type_name", record.getStr("queue_type_name"));
					map.put("print_type", record.getStr("print_type"));
					map.put("register_id", record.getStr("register_id"));
					renderJson(map);
					return map;
				} else {
					map.put("return_msg", "患者报到失败");
					map.put("return_code", "fail");
					renderJson(map);
					return map;
				}
			} else if ("2".equals(is_display)) {
				map.put("return_msg",
						"当前患者" + record.getStr("patient_name") + "在队列:" + record.getStr("queue_type_name") + "中");
				map.put("return_code", "success");
				map.put("queue_type_id", record.getInt("queue_type_id"));
				map.put("id", record.getInt("id"));
				map.put("state_patient", record.get("state_patient"));
				map.put("patient_name", record.getStr("patient_name"));
				map.put("queue_type_name", record.getStr("queue_type_name"));
				map.put("print_type", record.getStr("print_type"));
				map.put("register_id", record.getStr("register_id"));
				renderJson(map);
				return map;
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "报到失败");
				renderJson(map);
				return map;
			}
		}
		else {
			String is_display = record.getInt("is_display").toString();

			if ("1".equals(is_display)) {
				int is_ckin_order=service.queuetypeisckinorder(queue_type_id);
				Boolean disp=false;

				if(is_ckin_order==1)
				{
					int register=service.findmaxregisteridbyisdisplay(queue_type_id);
					if(register<=0)
						register=1;
					disp=service.baodaopager2(code,pagerid, Integer.parseInt(String.valueOf(register)));

				}
				else
				{
					Record tr=triage.findtriageBytemIP(ip);
					int late= tr.getInt("late_flag_step");
					if(late>0)
					{
						if(tr.getInt("late_type")==1)
						{
							if(h<12&&Integer.parseInt(record.getStr("time_interval"))==2)
							{
								disp=service.baodaopager(code,pagerid);
							}
							else
							{
								int max=service.findmaxregisteridnow(queue_type_id);
								int now= Integer.parseInt(record.getStr("register_id"));
								if(max>now)
								{
									disp=service.baodaopager3(code,pagerid);
								}
								else
									disp=service.baodaopager(code,pagerid);
							}
						}
						else
						{
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = null;  
							try {   
								date = df.parse(record.getStr("last_time"));
							} catch (Exception e) {   
								date=null;
							}
							if(date==null)
							{
								try {   
									date = df.parse(record.getStr("fre_date"));
								} catch (Exception e) {   
									date=new Date();
								}
							}
							int late_time=tr.getInt("late_time");
							Calendar rightNow = Calendar.getInstance();
							rightNow.setTime(date);
							rightNow.add(Calendar.MINUTE,late_time);
							Date dt1=rightNow.getTime();
							Date dt2=new Date();
							
							if(dt1.after(dt2))
							{
								disp=service.baodaopager(code,pagerid);
							}
							else
								{
								disp=service.baodaopager3(code,pagerid);
								}
						}
						
						/*int max=service.findmaxregisteridnow(queue_type_id);
						int now= Integer.parseInt(service.findById(id).getRegister_id());
						if(max>now)
						{
							disp=service.updatedisplaybyscan2(id);
						}
						else
							disp = service.updatedisplaybyscan(id);*/
					}
					else
					{
						disp=service.baodaopager(code,pagerid);
					}
				}
				if (disp) {

					service.insertPatient(map2);

					map.put("return_msg",
							"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
					map.put("return_code", "success");
					map.put("queue_type_id", record.getInt("queue_type_id"));
					map.put("id", record.getInt("id"));
					map.put("state_patient", record.get("state_patient"));
					map.put("patient_name", record.getStr("patient_name"));
					map.put("queue_type_name", record.getStr("queue_type_name"));
					map.put("print_type", record.getStr("print_type"));
					map.put("register_id", record.getStr("register_id"));
					renderJson(map);
					return map;
				} else {
					map.put("return_msg", "患者报到失败");
					map.put("return_code", "fail");
					renderJson(map);
					return map;
				}
			} else if ("2".equals(is_display)) {
				map.put("return_msg",
						"当前患者" + record.getStr("patient_name") + "在队列:" + record.getStr("queue_type_name") + "中");
				map.put("return_code", "success");
				map.put("queue_type_id", record.getInt("queue_type_id"));
				map.put("id", record.getInt("id"));
				map.put("state_patient", record.get("state_patient"));
				map.put("patient_name", record.getStr("patient_name"));
				map.put("queue_type_name", record.getStr("queue_type_name"));
				map.put("print_type", record.getStr("print_type"));
				map.put("register_id", record.getStr("register_id"));
				renderJson(map);
				return map;
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "报到失败");
				renderJson(map);
				return map;
			}
		}

	}

	public Map<String, Object> SanAddBydoorFZ() {
		Map<String, Object> map = new HashMap<String, Object>();

		String code = getPara("code");
		String queue_type_id=getPara("queue_type_id");
		String ip=shardkit.getIpAddr(getRequest());
		String pagerid=getPara("queue_type_id");
		String t_t=triage.findtriageBytemIP(ip).getStr("triage_type");
		Record record =null;
		if(t_t.equals("2"))
		{
			//
			record =servicepatientqueue.selectPatientbyPagerIDAndCode(pagerid,code);

			queue_type_id=queueservice.FristPagerType(pagerid);
		}
		else
		{
			record=service.selectPatientbycode(code, queue_type_id);
		}

		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return map;
		}
		String is_display = record.getInt("is_display").toString();
		int id=record.getInt("id");
		if ("2".equals(is_display)) {
			int disp=0;
			if(t_t.equals("1"))
			{
				disp= service.updateFZbyscan(id,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()),queue_type_id);
			}
			else
			{
				disp=service.updatePatientbyPagerIDandCode(pagerid,code,2,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
			}

			if(disp>0)
			{
				Record map2 = Record.create();
				map2.put("source_code",code);
				map2.put("patient_name",record.getStr("patient_name"));
				map2.put("id",id);
				service.insertPatient(map2);
				map.put("return_msg",
						"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
				map.put("return_code", "success");
				map.put("queue_type_id", record.getInt("queue_type_id"));
				map.put("id", record.getInt("id"));
				map.put("state_patient", record.get("state_patient"));
				map.put("patient_name", record.getStr("patient_name"));
				map.put("queue_type_name", record.getStr("queue_type_name"));
				map.put("print_type", record.getStr("print_type"));
				map.put("register_id", record.getStr("register_id"));
				renderJson(map);
				return map;
			}
			else
			{
				map.put("return_code", "fail");
				map.put("return_msg", "报到失败");
				renderJson(map);
				return map;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "报到失败");
			renderJson(map);
			return map;
		}


	}

	public Map<String, Object> SanAddBydoorGH() {
		Map<String, Object> map = new HashMap<String, Object>();

		String code = getPara("code");
		String queue_type_id=getPara("queue_type_id");
		String ip=shardkit.getIpAddr(getRequest());

		String pagerid=getPara("queue_type_id");
		String t_t=triage.findtriageBytemIP(ip).getStr("triage_type");
		Record record =null;
		if(t_t.equals("2"))
		{
			//
			record =servicepatientqueue.selectPatientbyPagerIDAndCode(pagerid,code);

			queue_type_id=queueservice.FristPagerType(pagerid);
		}
		else
		{
			record=service.selectPatientbycode(code, queue_type_id);
		}

		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return map;
		}
		else
		{

			int disp=0;
			if(t_t.equals("1"))
			{
				disp= service.updatepatientGHbyDoorscan(code, queue_type_id);
			}
			else
			{
				disp=service.updatePatientbyPagerIDandCode(pagerid,code,8,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
			}

			if(disp>0)
			{map.put("return_code", "success");
			map.put("queue_type_id", record.getInt("queue_type_id"));
			map.put("id", record.getInt("id"));
			map.put("state_patient", record.get("state_patient"));
			map.put("patient_name", record.getStr("patient_name"));
			map.put("queue_type_name", record.getStr("queue_type_name"));
			map.put("print_type", record.getStr("print_type"));
			map.put("register_id", record.getStr("register_id"));
			renderJson(map);
			return map;
			}
			else
			{
				map.put("return_code", "fail");
				map.put("return_msg", "报到失败");
				renderJson(map);
				return map;
			}
		}

	}


	public Map<String, Object> ScanAdd(String code ,String ip,String late_show,int late_flag_step,String QueueNumber,String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> rsList=service.findCode(code, ip);
		Record record = rsList.get(0);
		for (Record r : rsList) {
			if(r.getStr("queue_type_id").equals(QueueNumber)) {
				record=r;
				break;
			}
		}
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有查询到患者");
			renderJson(map);
			return map;
		}
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int h=c.get(Calendar.HOUR_OF_DAY);
		
		if(h<12)
		{
			if(record.getInt("time_interval")==3)
			{
				map.put("return_code", "fail");
				map.put("return_msg", "不在报到时间内");
				renderJson(map);
				return map;
			}
		}
		
		String is_display = record.getInt("is_display").toString();
		String queue_type_id=record.getStr("queue_type_id");
		int id=record.getInt("id");
		int register=record.getInt("register_id");
		Record map2 = Record.create();
		map2.put("source_code",code);
		map2.put("patient_name",name);
		map2.put("id",id);
		if ("1".equals(is_display)) {
			int is_ckin_order=service.queuetypeisckinorder(queue_type_id);
			int disp=0;
			if(is_ckin_order==1)
			{
				register=service.findmaxregisteridbyisdisplay(queue_type_id);
				if(register<=0)
					register=1;
				disp=service.updatedisplaybyscanreorder(String.valueOf(register),id);
			}
			else {
				Record tr=triage.findbytriageip(ip);
				int late= tr.getInt("late_flag_step");
				if(late>0)
				{
					if(tr.getInt("late_type")==1)
					{
						if(h<12&&Integer.parseInt(record.getStr("time_interval"))==2)
						{
							disp = service.updatedisplaybyscan(id);
						}
						else
						{
							int max=service.findmaxregisteridnow(queue_type_id);
							int now= Integer.parseInt(record.getStr("register_id"));
							if(max>now)
							{
								disp=service.updatedisplaybyscan2(id);
								
							}
							else {
								disp = service.updatedisplaybyscan(id);
							}
						}
						
					}
					else
					{
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = null;  
						try {   
							date = df.parse(record.getStr("last_time"));
						} catch (Exception e) {   
							date=null;
						}
						if(date==null)
						{
							
							try {   
								date = df.parse(record.getStr("fre_date"));
							} catch (Exception e) {   
								date=new Date();
							}
							
						}
						int late_time=tr.getInt("late_time");
						Calendar rightNow = Calendar.getInstance();
						rightNow.setTime(date);
						rightNow.add(Calendar.MINUTE,late_time);
						Date dt1=rightNow.getTime();
						Date dt2=new Date();
						
						if(dt1.after(dt2))
						{
							disp = service.updatedisplaybyscan(id);
						}
						else
							{
							disp=service.updatedisplaybyscan2(id);
							
							}
					}
				}
				else {
					disp = service.updatedisplaybyscan(id);
				}
			}
			if (disp > 0) {
				//record = service.findCode(code, ip).get(0);

				service.insertPatient(map2);

				map.put("return_msg",
						"当前患者:" + record.getStr("patient_name") + "已成功报到在当前分诊台" + record.getStr("triage_name") + "下");
				map.put("return_code", "success");
				map.put("queue_type_id", record.getInt("queue_type_id"));
				map.put("id", record.getInt("id"));
				map.put("state_patient", record.get("state_patient"));
				map.put("patient_name", record.getStr("patient_name"));
				map.put("queue_type_name", record.getStr("queue_type_name"));
				map.put("queue_type_displayname", record.getStr("queue_type_displayname"));
				map.put("print_type", record.getStr("print_type"));
				map.put("register_id", register);
				map.put("register_id2", record.getStr("register_id2"));
				map.put("state_patient2", record.get("state_patient2"));
				map.put("begin_time", record.get("begin_time"));
				map.put("last_time", record.get("last_time"));
				map.put("fre_date", record.get("fre_date"));
				map.put("doctor_id", record.get("doctor_id"));
				return map;
			} else {
				map.put("return_msg", "患者报到失败");
				map.put("return_code", "fail");
				return map;
			}
		} else if ("2".equals(is_display)) {
			int	disp2=0;
			if(record.getInt("state_patient")==53||record.getInt("state_patient")==52)
				disp2= service.updateFZbyscan(id,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()),queue_type_id);
			else if(record.getInt("state_patient")==1) {
				disp2= service.updatepatientGHbyDoorscan(code, queue_type_id);
			}
			else 
				disp2=1;
			if(disp2>0)
			{
				//record = service.findCode(code, ip).get(0);
				map.put("return_msg",
					"当前患者" + record.getStr("patient_name") + "在队列:" + record.getStr("queue_type_name") + "中");
				map.put("return_code", "success");
				map.put("queue_type_id", record.getInt("queue_type_id"));
				map.put("id", record.getInt("id"));
				map.put("state_patient", record.get("state_patient"));
				map.put("patient_name", record.getStr("patient_name"));
				map.put("queue_type_name", record.getStr("queue_type_name"));
				map.put("queue_type_displayname", record.getStr("queue_type_displayname"));
				map.put("print_type", record.getStr("print_type"));
				map.put("register_id", record.getStr("register_id"));
				map.put("register_id2", record.getStr("register_id2"));
				map.put("state_patient2", record.get("state_patient2"));
				map.put("begin_time", record.get("begin_time"));
				map.put("last_time", record.get("last_time"));
				map.put("fre_date", record.get("fre_date"));
				map.put("doctor_id", record.get("doctor_id"));
				return map;
			}
			else {
				map.put("return_code", "fail");
				map.put("return_msg", "报到失败");
				return map;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "报到失败");
			return map;
		}
	}

	public void newlyDiagnosed() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getRequest().getParameter("ids");
		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String QueueNumber = getPara("queue_type_id");
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		boolean bool =false;
		if(tri.getReorder_type()==1)
			bool =service.NewlyDiagnosed(ids);
		else 
			bool=service.NewlyDiagnosedByPagerList(QueueNumber, ids);
		if (bool) {
			map.put("return_msg", UPDATE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", UPDATE_FAIL_MSG);
			renderJson(map);
			return;
		}
	}

	public void passed() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getRequest().getParameter("ids");
		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String QueueNumber = getPara("queue_type_id");
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		boolean bool =false;
		if(tri.getReorder_type()==1)
			bool = service.passed(ids);
		else 
			bool=service.passedByPagerList(QueueNumber, ids);
		if (bool) {
			map.put("return_msg", UPDATE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", UPDATE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void visit() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String QueueNumber = getPara("queue_type_id");
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		boolean bool =false;
		if(tri.getReorder_type()==1)
			bool =service.visit(QueueNumber,ids);
		else 
			bool=service.visitByPagerList(QueueNumber, ids);
		if (bool) {
			map.put("return_msg", UPDATE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", UPDATE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void GreenChannel() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getRequest().getParameter("ids");
		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String QueueNumber = getPara("queue_type_id");
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		boolean bool =false;
		if(tri.getReorder_type()==1)
			bool =service.GreenChannel(ids);
		else 
			bool=service.visitByPagerList(QueueNumber, ids);
		if (bool) {
			map.put("return_msg", UPDATE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", UPDATE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void delay() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		ids = ids.substring(0, ids.length() - 1);

		int times = getParaToInt("timeInterval", -1);

		if (times == -1) {
			map.put("return_msg", NOT_NULL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		boolean bool = service.delay(times, ids);
		if (bool) {
			map.put("return_msg", UPDATE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", UPDATE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void Hang() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");

		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String QueueNumber = getPara("queue_type_id");
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage tri=triage.queryTriageIp(ip);
		boolean bool =false;
		if(tri.getReorder_type()==1)
			bool =service.Hang(ids);
		else 
			bool=service.HangByPagerList(QueueNumber, ids);
		if (bool) {
			map.put("return_msg", UPDATE_SUCCESS_MSG);
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", UPDATE_FAIL_MSG);
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void referralQueueType() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		//String triage_ip = shardkit.getIpAddr(getRequest());
		Triage rt = triage.queryTriageIp(ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊类型失败");
			renderJson(map);
			return;
		}
		String type = rt.getTriage_type().toString();

		String queue_type_id = getPara("queue_type_id");
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_msg", "请选择需要转诊的队列");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}

		int tri_reorder_type=rt.getReorder_type();
		QueueType r_queuetype=queueservice.findById(queue_type_id);
		Record record = null;
		String doctor_id ="";
		if(tri_reorder_type==1)
		{
			doctor_id= getPara("id");
			if (StringKit.isBlank(doctor_id)) {
				map.put("return_msg", "请选择医生/诊室");
				map.put("return_code", "fail");
				renderJson(map);
				return;
			}
			if(r_queuetype.getIs_ckin_order()==2)
				record=service.findmaxregisterid2(queue_type_id);
			else
				record=service.findmaxregisterid(queue_type_id);
		}

		else
		{
			if(r_queuetype.getIs_ckin_order()==2)
				record=service.findpagermaxregistreid2(queue_type_id);
			else
				record=service.findpagermaxregistreid(queue_type_id);
		}

		// System.out.println(record + "**********" + record.getStr("register_id"));
		String register_id = null;
		/*if (StringKit.isBlank(record.getStr("register_id"))) {
			register_id = "转诊-1";
		} else {
			register_id = record.getStr("register_id");
			String[] a = register_id.split("转诊-");
			register_id = a[1];
			int maxnum = Integer.parseInt(register_id) + 1;
			register_id = "转诊-" + String.valueOf(maxnum);
		}*/
		if (StringKit.isBlank(record.getStr("register_id"))) {
			register_id = "1";
		} else {
			register_id = record.getInt("register_id").toString();
			//String[] a = register_id.split("转诊-");
			//register_id = a[1];
			//int maxnum = Integer.parseInt(register_id) + 1;
			//register_id = String.valueOf(maxnum);
		}
		if(!StringKit.isEmpty(r_queuetype.getReserve_numlist()))
		{	
			if(Integer.parseInt(register_id)<=Integer.parseInt(r_queuetype.getReserve_numlist()))
				register_id=String.valueOf(Integer.parseInt(r_queuetype.getReserve_numlist())+1);
		}
		boolean bool = false;
		if ("1".equals(type)) {
			bool = service.ReferralQueueType(queue_type_id, ids, register_id, doctor_id);
		} else {
			bool = service.ReferralQueueType2(queue_type_id, ids, register_id);
		}
		if (bool) {
			map.put("return_msg", "转诊患者成功");
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", "转诊患者失败");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void referralQueueType2() {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean yl=getParaToBoolean("yl");
		String code = getPara("code");
		if (StringKit.isBlank(code)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String queue = getPara("queue");
		if (StringKit.isBlank(queue)) {
			renderJson(error(NOT_NULL_MSG));
			return;
		}
		String queue_type_id = getPara("queue_type_id");
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_msg", "请选择需要转诊的队列");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String doctor_id= getPara("id");
		if (StringKit.isBlank(doctor_id)) {
			map.put("return_msg", "请选择诊室");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		Triage rt = triage.queryTriageIp(ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊类型失败");
			renderJson(map);
			return;
		}
		String type = rt.getTriage_type().toString();



		int tri_reorder_type=rt.getReorder_type();
		QueueType r_queuetype=queueservice.findById(queue_type_id);
		String register_id = null;
		if(yl)
		{
			if(r_queuetype.getReserve_numlist()!="0")
			{
				int numlist=Integer.parseInt(r_queuetype.getReserve_numlist());
				int newid=service.getylnmb(queue_type_id,numlist );
				if(newid<=numlist)
				{
					if(newid==0)
						newid=1;
					register_id=String.valueOf(newid);
				}
				else
				{
					map.put("return_msg", "分诊患者失败,预留号已满");
					map.put("return_code", "fail");
					renderJson(map);
					return;
				}
			}
		}
		else
		{
			Record record = null;
			if(tri_reorder_type==1)
			{
				if(r_queuetype.getIs_ckin_order()==2)
					record=service.findmaxregisterid2(queue_type_id);
				else
					record=service.findmaxregisterid(queue_type_id);
			}

			else
			{
				if(r_queuetype.getIs_ckin_order()==2)
					record=service.findpagermaxregistreid2(queue_type_id);
				else
					record=service.findpagermaxregistreid(queue_type_id);
			}
			if (StringKit.isBlank(record.getStr("register_id"))) {
				register_id = "1";
			} else {
				register_id = record.getInt("register_id").toString();
				//String[] a = register_id.split("转诊-");
				//register_id = a[1];
				//int maxnum = Integer.parseInt(register_id) + 1;
				//register_id = String.valueOf(maxnum);
			}
			if(!StringKit.isEmpty(r_queuetype.getReserve_numlist()))
			{	
				if(Integer.parseInt(register_id)<=Integer.parseInt(r_queuetype.getReserve_numlist()))
					register_id=String.valueOf(Integer.parseInt(r_queuetype.getReserve_numlist())+1);
			}
		}
		// System.out.println(record + "**********" + record.getStr("register_id"));

		/*if (StringKit.isBlank(record.getStr("register_id"))) {
			register_id = "转诊-1";
		} else {
			register_id = record.getStr("register_id");
			String[] a = register_id.split("转诊-");
			register_id = a[1];
			int maxnum = Integer.parseInt(register_id) + 1;
			register_id = "转诊-" + String.valueOf(maxnum);
		}*/


		String ids="";
		List<Record> Pids=service.GetPatientByCodeTypeID(queue, code);
		for (Record record2 : Pids) {
			ids+=record2.getStr("id")+",";
		}
		ids = ids.substring(0, ids.length() - 1);
		if(StringKit.isNotBlank(ids))
		{boolean bool = false;
		if ("1".equals(type)) {
			bool = service.ReferralQueueType3(queue_type_id, ids, register_id, doctor_id);
		} else {
			bool = service.ReferralQueueType4(queue_type_id, ids, register_id);
		}
		if (bool) {
			map.put("return_msg", "分诊患者成功");
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", "分诊患者失败");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		}
		else
		{
			map.put("return_msg", "分诊患者失败");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}


	public void examination() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			map.put("return_msg", "请选择需要操作的患者");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		if(ip.isEmpty()||ip.equals("")) 
			ip=shardkit.getIpAddr(getRequest());
		//String triage_ip = shardkit.getIpAddr(getRequest());
		Triage rt = triage.queryTriageIp(ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊类型失败");
			renderJson(map);
			return;
		}
		String type = rt.getTriage_type().toString();

		String queue_type_id = getPara("queue_type_id");
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_msg", "队列id不能为空");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}

		if ("1".equals(type)) {
			String doctor_id = getPara("id").toString();
			if (StringKit.isBlank(doctor_id)) {
				map.put("return_msg", "获取医生失败");
				map.put("return_code", "fail");
				renderJson(map);
				return;
			} else {
				boolean bool = service.examinationdoctor(doctor_id, ids, queue_type_id);
				if (bool) {
					map.put("return_msg", "分诊成功");
					map.put("return_code", "success");
					renderJson(map);
				} else {
					map.put("return_msg", "分诊失败");
					map.put("return_code", "fail");
					renderJson(map);
					return;
				}
			}
		} else if ("2".equals(type)) {
			String pager_id = getPara("id");
			if (StringKit.isBlank(pager_id)) {
				map.put("return_msg", "获取队列获取叫号器失败");
				map.put("return_code", "fail");
				renderJson(map);
				return;
			} else {
				boolean bool = service.examinationpager(pager_id, ids, queue_type_id);
				if (bool) {
					map.put("return_msg", "分诊成功");
					map.put("return_code", "fail");
					renderJson(map);
				} else {
					map.put("return_msg", "分诊失败");
					map.put("return_code", "fail");
					renderJson(map);
					return;
				}
			}
		}
	}

	public void noexamination() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = getPara("ids");
		if (StringKit.isBlank(ids)) {
			map.put("return_msg", "请选择需要操作的患者");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		ids = ids.substring(0, ids.length() - 1);
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		//String triage_ip = shardkit.getIpAddr(getRequest());
		Triage rt = triage.queryTriageIp(ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊类型失败");
			renderJson(map);
			return;
		}
		String type = rt.getTriage_type().toString();

		boolean bool = service.noexamination(ids, type);
		if (bool) {
			map.put("return_msg", "取消分诊成功");
			map.put("return_code", "success");
			renderJson(map);
		} else {
			map.put("return_msg", "取消分诊失败");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
	}

	public void list_queue_type() {
		Map<String, Object> map = new HashMap<String, Object>();
		String triage_ip = shardkit.getIpAddr(getRequest());
		Triage rt = triage.queryTriageIp(triage_ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分针类型失败");
			renderJson(map);
			return;
		}
		List<Record> list = triage.queryByQueueType(triage_ip);
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "医生队列信息成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public void doctorId() {
		Map<String, Object> map = new HashMap<String, Object>();
		String queue_type_id = getPara("queue_type_id");
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_msg", "队列id不能为空");
			map.put("return_code", "fail");
			renderJson(map);
			return;
		}
		String ip = getPara("ip");
		try {
			if(ip.isEmpty()||ip.equals("")) 
				ip=shardkit.getIpAddr(getRequest());
		}
		catch (Exception e) {
			ip=shardkit.getIpAddr(getRequest());
		}
		//String triage_ip = shardkit.getIpAddr(getRequest());
		Triage rt = triage.queryTriageIp(ip);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊类型失败");
			renderJson(map);
			return;
		}
		String type = rt.getTriage_type().toString();

		List<Record> list = service.finddoctorid(queue_type_id, type);
		QueueType queueType=queueservice.findById(queue_type_id);
		if (null != list) {
			map.put("return_code", "success");
			map.put("return_msg", "当前队列排班医生/叫号器查询成功");
			map.put("list", list);
			map.put("numlist", queueType.getReserve_numlist());
			map.put("count", list.size());
			renderJson(map);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "当前队列排班医生/叫号器查询失败");
			renderJson(map);
			return;
		}
	}
	public void list_patient_call() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return _msg", "获取IP失败");
			renderJson(map);
			return;
		}
		Record rcd = terminal.findbytriageip(ip);
		if (null == rcd) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		}
		String triageip = rcd.getStr("ip");
		List<Record> list = null;
		Triage rip = triage.queryTriageIp(triageip);

		if ("1".equals(rip.getTriage_type().toString()))
			list = service.list_patient_call(ip);
		else {
			if(rip.getReorder_type()==1)
				list = service.list_patient_call2(ip);
			else
				list=service.list_patient_call_pager(ip);
		}

		String[] aa = new String[0];
		if (list != null && !list.isEmpty()) {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", list);
			renderJson(map);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			map.put("list", aa);
			renderJson(map);
			return;
		}
		for (Record rd : list) {
			if(rip.getReorder_type()==1)
				service.updatecontent(rd.get("id").toString());
			else
				service.updatecontent2(ip);

		}
		renderJson(map);
	}
	public void list_roomdoor_nodisplay() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			renderJson(map);
			return;
		}
		Record record = doctor.queryByip(ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		}
		String doctor_id = record.getInt("doctor_id").toString();
		String login_id = record.getStr("login_id").toString();
		if (StringKit.isBlank(doctor_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取医生id失败");
			renderJson(map);
			return;
		}
		String queue_type_id = service.selectqueuetypeidbydoctor(Integer.parseInt(doctor_id)).getStr("queue_type_id");
		List<Record> list = servicepatientqueue.list_doctor_nodisplay(login_id, Integer.parseInt(queue_type_id));
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询未报到患者等候列表失败");
			map.put("count", 0);
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}
	public void list_roomdoor() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			renderJson(map);
			return;
		}
		Record record = doctor.queryByip(ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		}
		String doctor_id = record.getInt("doctor_id").toString();
		String login_id = record.getStr("login_id").toString();
		if (StringKit.isBlank(doctor_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取医生id失败");
			renderJson(map);
			return;
		}
		String pager_ip=record.getStr("ip");
		String queue_type_id = service.selectqueuetypeidbydoctor(Integer.parseInt(doctor_id)).getStr("queue_type_id");
		List<Record> list = wait(queue_type_id, login_id,pager_ip);
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询患者等候列表失败");
			map.put("count", 0);
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public List<Record> wait(String queue_type_id, String login_id,String ip) {
		List<Record> result = new ArrayList<Record>();
		List<Record> list_wait =new ArrayList<Record>();
		List<Record> list_wait_lockedList = servicepatientqueue.list_doctor_locked(login_id, Integer.parseInt(queue_type_id));
		List<Record> list_wait2 = servicepatientqueue.list_doctor(login_id, Integer.parseInt(queue_type_id));

		List<Record> list_agin = servicepatientqueue.list_call_patient_agin(Integer.parseInt(queue_type_id), "", "1",
				login_id, 0);

		List<Record> list_was = servicepatientqueue.list_call_patient_was(Integer.parseInt(queue_type_id), "", "1",
				login_id);
		List<Record> list_first = servicepatientqueue.list_call_patient_first(Integer.parseInt(queue_type_id), "", "1",
				login_id);
		List<Record> list_late = servicepatientqueue.list_call_patient_late(Integer.parseInt(queue_type_id), "", "1",
				login_id);

		Record rule = servicepatientqueue.selectTQrule(ip);

		//List<Record> result = new ArrayList<Record>();
		List<Record> final_result= new ArrayList<Record>();


		int Tagin = rule.getInt("return_flag_step");
		int Twas = rule.getInt("call_buffer");
		int Tlate=rule.getInt("late_flag_step");
		int Tfirst=rule.getInt("first_flag_step");
		int Qagin = rule.getInt("call_return_rule_flag");
		int Qwas = rule.getInt("call_pass_rule_flag");
		int Qlate=rule.getInt("call_late_rule_flag");
		int Qfirst=rule.getInt("call_first_rule_flag");
		int Qfirst2=rule.getInt("call_first_rule_flag2");

		int Fagin = rule.getInt("call_return_first_flag");
		int Fwas = rule.getInt("call_pass_first_flag");
		int Flate = rule.getInt("call_late_first_flag");
		int Ffirst=rule.getInt("call_first_first_flag");


		ArrayList<orderArylist> list=new ArrayList<>();
		List<Record> list_unlock_wait = new ArrayList<>();
		if(rule.getInt("late_show")==1){
				if (list_late.size() > 0) {
					if (list_wait2.size() > 0) {
						
						for (int i = 0; i < list_late.size(); i++) {
							if (list_late.get(i).getInt("late_lock") == 0)
								list_unlock_wait.add(list_late.get(i));
						}
						for (int i = 0; i < list_wait2.size(); i++) {
							if (list_wait2.get(i).getInt("late_lock") == 0)
								list_unlock_wait.add(list_wait2.get(i));
						}
						if (list_unlock_wait.size() > 0)
							Collections.sort(list_unlock_wait, new MyComprator2());
						list_wait.addAll(list_unlock_wait);
					} else {
						// Collections.sort(list_late, new MyComprator3());
						for (int i = 0; i < list_late.size(); i++) {
							if (list_late.get(i).getInt("late_lock") == 0)
								list_unlock_wait.add(list_late.get(i));
						}
						list_wait = list_unlock_wait;
					}
				} else
				{
					for (int i = 0; i < list_wait2.size(); i++) {
						if (list_wait2.get(i).getInt("late_lock") == 0)
							list_unlock_wait.add(list_wait2.get(i));
					}
					list_wait =list_unlock_wait ;
				}
			} else {
				for (int i = 0; i < list_wait2.size(); i++) {
					if (list_wait2.get(i).getInt("late_lock") == 0)
						list_unlock_wait.add(list_wait2.get(i));
				}
				list_wait=list_unlock_wait;
				if (list_late.size() > 0) {
					orderArylist oaLate = new orderArylist();
					List<Record> list_late_unlocked=new ArrayList<Record>();
					for (int i = 0; i < list_late.size(); i++) {
						if (list_late.get(i).getInt("late_lock") == 0)
							list_late_unlocked.add(list_late.get(i));
					}
					oaLate.F = Flate;
					oaLate.Q = Qlate;
					oaLate.T = Tlate;
					oaLate.id = 2;
					oaLate.name = "late";
					oaLate.l_r = list_late_unlocked;
					list.add(oaLate);
				}
			}
			if (list_agin.size() > 0) {
				orderArylist oaAgin = new orderArylist();
				List<Record> list_agin_unlocked=new ArrayList<Record>();
				for (int i = 0; i < list_agin.size(); i++) {
					if (list_agin.get(i).getInt("late_lock") == 0)
						list_agin_unlocked.add(list_agin.get(i));
				}
				oaAgin.F = Fagin;
				oaAgin.Q = Qagin;
				oaAgin.T = Tagin;
				oaAgin.id = 1;
				oaAgin.name = "agin";
				oaAgin.l_r = list_agin_unlocked;
				list.add(oaAgin);
			}

			if (list_was.size() > 0) {
				orderArylist oaWas = new orderArylist();
				List<Record> list_was_unlocked=new ArrayList<Record>();
				for (int i = 0; i < list_was.size(); i++) {
					if (list_was.get(i).getInt("late_lock") == 0)
						list_was_unlocked.add(list_was.get(i));
				}
				oaWas.F = Fwas;
				oaWas.Q = Qwas;
				oaWas.T = Twas;
				oaWas.id = 3;
				oaWas.name = "was";
				oaWas.l_r = list_was_unlocked;
				list.add(oaWas);
			}
			Collections.sort(list, new MyComprator());

			if (list.size() > 0) {
				for (int i = 0; i < list_wait.size(); i++) {
					for (int j = 0; j < list.size(); j++) {
						orderArylist ary = list.get(j);
						if (ary.getF() == ary.getT()) {
							if (ary.getL_r().size() > 0) {
								result.add(ary.getL_r().get(0));
								ary.getL_r().remove(0);
							}
							ary.setF(1);
						} else
							ary.setF(ary.getF() + 1);
					}
					result.add(list_wait.get(i));
				}
				for (int i = 0; i < list.size(); i++) {
					orderArylist ary = list.get(i);
					if (ary.getL_r().size() > 0) {
						result.addAll(ary.getL_r());
					}
				}
			} else
				result = list_wait;
			final_result.addAll(list_wait_lockedList);
			List<Record> list_first_unlocked=new ArrayList<Record>();
			for (int i = 0; i < list_first.size(); i++) {
				if (list_first.get(i).getInt("late_lock") == 0)
					list_first_unlocked.add(list_first.get(i));
			}
			final_result.addAll(list_first_unlocked);
			final_result.addAll(result);
			
			return final_result;
	}

	public void list_roomdoor_jz_pager_show() {
		Map<String, Object> map = new HashMap<String, Object>();

		String ip = terminal.getpageripbyterip(shardkit.getIpAddr(getRequest()));
		List<Record> l_r = servicepatientqueue.selectpatientpager(ip, "51");
		if (l_r == null || l_r.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询当前就诊患者失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("dqjz", l_r.get(0));
			renderJson(map);
		}
	}

	public void list_roomdoor_jz_pager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shardkit.getIpAddr(getRequest());
		//String ip2 = terminal.getpageripbyterip(ip);
		List<Record> l_r = servicepatientqueue.selectpatientcallingpager(ip, "51");
		if (l_r == null || l_r.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询当前就诊患者失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", l_r);
			for (Record r : l_r) {
				servicepatientqueue.updatepatientpagercallerstatus("caller", r.getStr("patient_source_code"),
						r.getStr("ip"));
			}
		}
		renderJson(map);
	}

	public void list_roomdoor_jz() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shardkit.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			renderJson(map);
			return;
		}
		Record record = doctor.queryByip(ip);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		}

		String login_id = record.getStr("login_id");
		if (StrKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取医生工号失败");
			renderJson(map);
			return;
		}
		String doctor_id = record.getInt("doctor_id").toString();
		if (StrKit.isBlank(doctor_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取医生id失败");
			renderJson(map);
			return;
		}
		Record rt = triage.findtypelogin_id(login_id);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊台分诊模式失败");
			renderJson(map);
			return;
		}
		String type = rt.get("triage_type").toString();
		if ("1".equals(type)) {

			Record r = service.list_doctor_room_door_jz(doctor_id, ip);
			if (null == r) {
				map.put("return_code", "fail");
				map.put("return_msg", "查询当前就诊患者失败");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "查询成功");
				map.put("dqjz", r);
				// System.out.println(r.getStr("caller"));
				renderJson(map);
				service.updatecall(doctor_id);
			}

		} else {
			Record r = service.list_pager_room_door_jz(ip);
			if (null == r) {
				map.put("return_code", "fail");
				map.put("return_msg", "查询当前就诊患者失败");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "查询成功");
				map.put("dqjz", r);
				// System.out.println(r.getStr("caller"));
				renderJson(map);
				service.updatecall2(ip);
			}

		}
	}

	public void next_show() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = terminal.getpageripbyterip(shardkit.getIpAddr(getRequest()));
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			renderJson(map);
			return;
		}
		List<Record> r = service.listnextshowpager(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询当前就诊患者失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", r);
			map.put("count", r.size());
			if (r.size() > 0)
				service.updatenextshow(ip);
			renderJson(map);

		}

	}
}
