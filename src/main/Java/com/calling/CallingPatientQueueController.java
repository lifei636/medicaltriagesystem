package com.calling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.staticutil.ConstantModel;
import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Pager;
import com.shine.model.Terminal;
import com.shine.model.Triage;
import com.shine.service.PagerService;
import com.shine.service.PatientQueueService;
import com.shine.service.QueueTypeService;
import com.shine.service.TerminalService;
import com.shine.service.TriageService;
import com.shine.service.impl.PagerServiceImpl;
import com.shine.service.impl.PatientQueueServiceImpl;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.impl.TerminalServiceImpl;
import com.shine.service.impl.TriageServiceImpl;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "/call_patient")
public class CallingPatientQueueController extends BaseController {

	ShardKit shared = new ShardKit();
	PatientQueueService servicepatientqueue = new PatientQueueServiceImpl();
	PagerService pager = new PagerServiceImpl();
	TriageService triage = new TriageServiceImpl();
	QueueTypeService queuetype = new QueueTypeServiceImpl();
	TerminalService terminal = new TerminalServiceImpl();
	class orderArylist {
		Integer T, Q, F, id;
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
	}

	class MyComprator implements Comparator {
		public int compare(Object arg0, Object arg1) {
			orderArylist t1 = (orderArylist) arg0;
			orderArylist t2 = (orderArylist) arg1;
			if (t1.F != t2.F)
				return t1.F < t2.F ? 1 : -1;
			else
				return t1.id > t2.id ? 1 : -1;
		}
	}

	class MyComprator2 implements Comparator {
		public int compare(Object arg0, Object arg1) {
			Record r0 = (Record) arg0;
			Record r1 = (Record) arg1;
			int t1 = r0.getInt("time_interval");
			int t2 = r1.getInt("time_interval");
			int t3 = r0.getInt("register_id");
			int t4 = r1.getInt("register_id");

			if (t1 < t2)
				return -1;
			else if (t1 > t2)
				return 1;
			else {
				if (t3 < t4) {
					return -1;
				} else if (t3 > t4) {
					return 1;
				} else {
					return 0;
				}
			}
		}

	}

	class MyComprator3 implements Comparator {
		public int compare(Object arg0, Object arg1) {
			Record r0 = (Record) arg0;
			Record r1 = (Record) arg1;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
			Date t1 = null;
			try {
				t1 = df.parse(r0.getStr("opr_time"));
			} catch (Exception e) {
				t1 = null;
			}
			Date t2 = null;
			try {
				t2 = df.parse(r1.getStr("opr_time"));
			} catch (Exception e) {
				t2 = null;
			}
			if (t1.after(t2))
				return 1;
			else if (t1.before(t2))
				return -1;
			else
				return 0;
		}
	}

	public void list_patient() {
		Map<String, Object> map = new HashMap<String, Object>();
		String login_id = getSessionAttr("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取医生登陆信息失败，请重新登录");
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
		String ip = shared.getIpAddr(getRequest());
		Pager rpager = pager.findByIP(ip);
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		String type = rt.getStr("triage_type");
		if ("1".equals(type)) {
			String queue_type_id = getPara("queue_type_id");
			if (StrKit.isBlank(queue_type_id)) {
				map.put("return_code", "fail");
				map.put("return_msg", "获取队列ID失败");
				renderJson(map);
				return;
			}

			List<Record> list = wait(queue_type_id, login_id, "1", ip, rt.getStr("late_show"));

			if (null == list) {
				map.put("return_code", "fail");
				map.put("return_msg", "获取患者信息失败");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "获取患者信息成功");
				map.put("list", list);
				map.put("count", list.size());
				renderJson(map);
			}
		} else {
			String queue_type_id = getPara("queue_type_id");
			if (StrKit.isBlank(queue_type_id)) {
				map.put("return_code", "fail");
				map.put("return_msg", "获取队列ID失败");
				renderJson(map);
				return;
			}
			List<Record> list = servicepatientqueue.list_pager(ip, Integer.parseInt(queue_type_id));
			if (null == list) {
				map.put("return_code", "fail");
				map.put("return_msg", "获取患者信息失败");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "获取患者信息成功");
				map.put("list", list);
				map.put("count", list.size());
				renderJson(map);
			}
		}
	}

	/**
	 * 根据规则获取等候患者列表 queue_type_id:队列 login_id：医生登录号 type：分诊台模式 ip：叫号器IP
	 **/
	public List<Record> wait(String queue_type_id, String login_id, String type, String ip, String late_type) {
		List<Record> result = new ArrayList<Record>();
		List<Record> list_wait = new ArrayList<Record>();
		List<Record> list_wait2 = null;
		List<Record> list_wait_lockedList = null;
		if ("1".equals(type)) {
			list_wait2 = servicepatientqueue.list_doctor(login_id, Integer.parseInt(queue_type_id));
			list_wait_lockedList = servicepatientqueue.list_doctor_locked(login_id, Integer.parseInt(queue_type_id));
		} else {
			list_wait2 = servicepatientqueue.list_pager2(ip, Integer.parseInt(queue_type_id));
			list_wait_lockedList = servicepatientqueue.list_pager2_locked(login_id, Integer.parseInt(queue_type_id));
		}
		List<Record> list_agin = servicepatientqueue.list_call_patient_agin(Integer.parseInt(queue_type_id), "", type,
				login_id, 0);

		List<Record> list_was = servicepatientqueue.list_call_patient_was(Integer.parseInt(queue_type_id), "", type,
				login_id);
		List<Record> list_late = servicepatientqueue.list_call_patient_late(Integer.parseInt(queue_type_id), "", type,
				login_id);
		List<Record> list_first = servicepatientqueue.list_call_patient_first(Integer.parseInt(queue_type_id), "", type,
				login_id);
		Record rule = servicepatientqueue.selectTQrule(ip);

		// List<Record> result = new ArrayList<Record>();
		List<Record> final_result = new ArrayList<Record>();

		int Tagin = rule.getInt("return_flag_step");
		int Twas = rule.getInt("call_buffer");
		int Tlate = rule.getInt("late_flag_step");
		int Tfirst = rule.getInt("first_flag_step");
		int Qagin = rule.getInt("call_return_rule_flag");
		int Qwas = rule.getInt("call_pass_rule_flag");
		int Qlate = rule.getInt("call_late_rule_flag");

		int Qfirst = rule.getInt("call_first_rule_flag");
		int Qfirst2 = rule.getInt("call_first_rule_flag2");

		int Fagin = rule.getInt("call_return_first_flag");
		int Fwas = rule.getInt("call_pass_first_flag");
		int Flate = rule.getInt("call_late_first_flag");
		int Ffirst = rule.getInt("call_first_first_flag");
		List<Record> list_unlock_wait = new ArrayList<>();
		ArrayList<orderArylist> list = new ArrayList<>();
		if ("1".equals(late_type)) {
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
		
		List<Record> calledList = servicepatientqueue.selectIsBegin(Byte.parseByte(type), queue_type_id);
		if (calledList.size() > 0) {
			//锁定的数量
			if(final_result.size()< rule.getInt("late_flag_step")){
				for (int i = 0; i < final_result.size(); i++) {
						if (final_result.get(i).getInt("late_lock") ==0)
							servicepatientqueue.updatePatientLateLock(final_result.get(i).getStr("patient_source_code"),
									final_result.get(i).getStr("queue_type_id"),
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
				}
			}
		}
		
		return final_result;
	}

	public void checkpagercalling() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		List<Record> l_r = null;
		l_r = servicepatientqueue.checkpagercalling(ip);
		if (null == l_r) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取患者信息失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取患者信息成功");
			map.put("list", l_r);
			map.put("count", l_r.size());
			renderJson(map);
			if (l_r.size() > 0)
				getSession().setAttribute("code", l_r.get(0).getStr("patient_source_code"));
		}
	}

	public void list_patient_pager2() {
		Map<String, Object> map = new HashMap<String, Object>();
		String s = getPara("status"), status = "0,3,4,6,7,50";
		boolean isvisting = false;
		switch (s) {
		case "wait":
			status = "0,3,4,6,7,50";
			break;
		case "pass":
			status = "54";
			break;
		case "over":
			status = "53";
			break;
		case "visting":
			status = "51";
			isvisting = true;
			break;
		default:
			break;
		}
		String ip = shared.getIpAddr(getRequest());
		try {
			String type = getPara("type");
			if (type.equals("page")) {
				ip = terminal.getpageripbyterip(ip);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		List<Record> l_r = null;
		l_r = servicepatientqueue.selectpatientpager(ip, status);
		if (null == l_r) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取患者信息失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取患者信息成功");
			map.put("list", l_r);
			map.put("count", l_r.size());
			renderJson(map);
			if (isvisting) {
				if (l_r.size() > 0)
					getSession().setAttribute("code", l_r.get(0).getStr("patient_source_code"));
			}
		}
	}

	public void list_patient_pager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String s = getPara("status"), status = "0,3,4,6,7,50";
		boolean isvisting = false;
		switch (s) {
		case "wait":
			status = "0,3,4,6,7,50";
			break;
		case "pass":
			if(ConstantModel.DYRY.equals(ConstantModel.LOGIN)){
				status = "1,51";
			}else{
				status = "54";
			}
			break;
		case "over":
			status = "53";
			break;
		case "visting":
			status = "51";
			isvisting = true;
			break;
		default:
			break;
		}
		String ip = shared.getIpAddr(getRequest());
		try {
			String type = getPara("type");
			if (type.equals("page")) {
				ip = terminal.getpageripbyterip(ip);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		List<Record> l_r = null;
		if (s.equals("wait")) {
			l_r = wait_pager(ip);
		} else
			l_r = servicepatientqueue.selectpatientpager(ip, status);
		if (null == l_r) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取患者信息失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取患者信息成功");
			map.put("list", l_r);
			map.put("count", l_r.size());
			renderJson(map);
			if (isvisting) {
				if (l_r.size() > 0)
					getSession().setAttribute("code", l_r.get(0).getStr("patient_source_code"));
			}
		}
	}

	public List<Record> wait_pager(String ip) {

		Pager rcd = pager.findByIP(ip);

		int triageip = rcd.getTriage_id();
		Triage rip = triage.findById(triageip);
		List<Record> result = new ArrayList<Record>();
		List<Record> final_result = new ArrayList<Record>();
		Record rule = servicepatientqueue.selectTQrule(ip);
		List<Record> list_wait = new ArrayList<>();
		List<Record> list_wait2 = null;
		List<Record> list_first = null;
		List<Record> list_agin = null;
		List<Record> list_was = null;
		List<Record> list_late = null;
		List<Record> list_wait_lockedList = null;
		if (rip.getReorder_type() == 1) {
			list_first = servicepatientqueue.selectpatientpager(ip, "5");
			list_wait2 = servicepatientqueue.selectpatientpager(ip, "0,3,4,6,7");
			list_agin = servicepatientqueue.selectpatientpager(ip, "2");
			list_was = servicepatientqueue.selectpatientpager(ip, "54");
			list_late = servicepatientqueue.selectpatientpager(ip, "8");
			list_wait_lockedList=servicepatientqueue.selectpatientpager_locked(ip);
		} else {
			String queuetypeid = queuetype.listPagerLogin(ip).get(0).getStr("queue_type_id");
			list_first = servicepatientqueue.selectpatientpagerId2(queuetypeid, "5");
			list_wait2 = servicepatientqueue.selectpatientpagerId(queuetypeid, "0,3,4,6,7");
			list_agin = servicepatientqueue.selectpatientpagerId2(queuetypeid, "2");
			list_was = servicepatientqueue.selectpatientpagerId2(queuetypeid, "54");
			list_late = servicepatientqueue.selectpatientpagerId2(queuetypeid, "8");
			list_wait_lockedList=servicepatientqueue.selectpatientpagerId2_locked(queuetypeid);
		}

		int Tagin = rule.getInt("return_flag_step");
		int Twas = rule.getInt("call_buffer");
		int Tlate = rule.getInt("late_flag_step");
		int Tfirst = rule.getInt("first_flag_step");
		int Qagin = rule.getInt("call_return_rule_flag");
		int Qwas = rule.getInt("call_pass_rule_flag");
		int Qlate = rule.getInt("call_late_rule_flag");
		int Qfirst = rule.getInt("call_first_rule_flag");
		int Qfirst2 = rule.getInt("call_first_rule_flag2");

		int Fagin = rule.getInt("call_return_first_flag");
		int Fwas = rule.getInt("call_pass_first_flag");
		int Flate = rule.getInt("call_late_first_flag");
		int Ffirst = rule.getInt("call_first_first_flag");

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
		
		
		List<Record> calledList = servicepatientqueue.selectIsBegin((byte) 2, ip);
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
		return final_result;
	}

	public void list_patient_pass() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		String queue_type_id = getPara("queue_type_id").toString();

		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取队列ID失败");
			renderJson(map);
			return;
		}
		String login_id = getSessionAttr("login_id").toString();
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取session中的login_id失败");
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

		String pager_ip = shared.getIpAddr(getRequest());
		List<Record> list = servicepatientqueue.list_call_patient_pass(Integer.parseInt(queue_type_id), pager_ip, type,
				login_id);
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取患者信息失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取患者信息成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public void list_patient_was() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		String queue_type_id = getPara("queue_type_id").toString();
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取队列ID失败");
			renderJson(map);
			return;
		}
		String login_id = getSessionAttr("login_id").toString();
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取session中的login_id失败");
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

		String pager_ip = shared.getIpAddr(getRequest());
		List<Record> list = servicepatientqueue.list_call_patient_was(Integer.parseInt(queue_type_id), pager_ip, type,
				login_id);
		if (list.size() < 1) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有未到过号患者");
			map.put("count", 0);
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取未到过号患者信息成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public void list_patient_over() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		int doctor_id = rpager.getDoctor_id();
		String queue_type_id = getPara("queue_type_id").toString();
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取队列ID失败");
			renderJson(map);
			return;
		}
		String login_id = getSessionAttr("login_id").toString();
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取session中的login_id失败");
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

		String pager_ip = shared.getIpAddr(getRequest());

		List<Record> list = servicepatientqueue.list_call_patient_over(Integer.parseInt(queue_type_id), pager_ip, type,
				login_id, doctor_id);
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取患者信息失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取患者信息成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public void list_patient_agin() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		int doctor_id = rpager.getDoctor_id();
		String queue_type_id = getPara("queue_type_id").toString();
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取队列ID失败");
			renderJson(map);
			return;
		}
		String login_id = getSessionAttr("login_id").toString();
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取session中的login_id失败");
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

		String pager_ip = shared.getIpAddr(getRequest());

		List<Record> list = servicepatientqueue.list_call_patient_agin(Integer.parseInt(queue_type_id), pager_ip, type,
				login_id, doctor_id);
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取患者信息失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取患者信息成功");
			map.put("list", list);
			map.put("count", list.size());
			renderJson(map);
		}
	}

	public void call_next() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		String queue_type_id = getPara("queue_type_id");
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请传入队列id");
			renderJson(map);
			return;
		}
		String pager_ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请传入叫号器ID");
			renderJson(map);
			return;
		}
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请传入登陆工号");
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
		int doctor_id = rpager.getDoctor_id();
		if (StringKit.isBlank(String.valueOf(doctor_id))) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取医生id失败");
			renderJson(map);
			return;
		}
		String type = rt.get("triage_type").toString();
		String late_show = rt.get("late_show").toString();
		if ("1".equals(type)) {
			Record rd = servicepatientqueue.select_state_patient(pager_ip, login_id, type);
			if (null != rd) {
				map.put("return_code", "fail");
				map.put("return_msg", "\"" + rd.get("patient_name") + "\"患者未进行诊结，过号操作，不能呼叫下一患者");
				map.put("patient_name", rd.get("patient_name"));
				map.put("patient_id", rd.get("patient_id"));
				renderJson(map);
				return;
			} else {
				List<Record> records = wait(queue_type_id, login_id, type, pager_ip, rt.get("late_show").toString());

				if (records.size() == 0) {
					map.put("return_code", "fail");
					map.put("return_msg", "呼叫失败,无新患者");
					renderJson(map);
					return;
				} else {
					Record record = null;
					for (int i = 0; i < records.size(); i++) {
						if (records.get(i).getInt("state_patient") != 50) {
							record = records.get(i);
							break;
						}
					}
					
					if (record == null) {
						map.put("return_code", "fail");
						map.put("return_msg", "呼叫失败,无新患者");
						renderJson(map);
						return;
					} else {
						String id = record.getStr("id");
						getSession().setAttribute("code", record.getStr("patient_source_code"));
						int status = record.getInt("state_patient");
						if (id == null || id == "") {
							map.put("return_code", "fail");
							map.put("return_msg", "呼叫失败");
							renderJson(map);
							return;
						}
						boolean bool = servicepatientqueue.update_patient_state(pager_ip, login_id,
								Integer.parseInt(id), doctor_id);
						if (bool) {
							for (int i = 0; i < records.size(); i++) {
								if (i <= rt.getInt("late_flag_step")) {
									if (records.get(i).getInt("late_lock") ==0)
										servicepatientqueue.updatePatientLateLock(records.get(i).getStr("patient_source_code"),
												records.get(i).getStr("queue_type_id"),
												new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
									status=records.get(i).getInt("state_patient");
								} else
									break;
							}
							Record rule = servicepatientqueue.selectTQrule(pager_ip);
							int Fagin = rule.getInt("call_return_first_flag");
							int Fwas = rule.getInt("call_pass_first_flag");
							int Flate = rule.getInt("call_late_first_flag");
							int Ffirst = rule.getInt("call_first_first_flag");
							int Tfrist = rule.getInt("first_flag_step");
							int Qfirst = rule.getInt("call_first_rule_flag");
							int Tlate = rule.getInt("late_flag_step");
							int Qlate = rule.getInt("call_late_rule_flag");
							Pager rcd = pager.findByIP(pager_ip);
							int triageip = rcd.getTriage_id();
							Triage rip = triage.findById(triageip);

							List<Record> list_first = null;
							List<Record> list_agin = null;
							List<Record> list_was = null;
							List<Record> list_late = null;
							list_first = servicepatientqueue.selectpatientdoctor(pager_ip, "5");
							list_agin = servicepatientqueue.selectpatientdoctor(pager_ip, "2");
							list_was = servicepatientqueue.selectpatientdoctor(pager_ip, "54");
							list_late = servicepatientqueue.selectpatientdoctor(pager_ip, "8");

							if (status == 5) {
								if (list_first.size() > 0)
									Ffirst = Tfrist - Qfirst;
								else
									Ffirst = 0;
							} else {
								if (list_first.size() > 0)
									Ffirst += 1;
								else
									Ffirst = 0;
							}

							if (status == 2)
								Fagin = 0;

							if (status == 54)
								Fwas = 0;
							if (late_show.equals("1")) {
								if (status == 8 || status == 0) {
									if (list_agin.size() > 0)
										Fagin += 1;
									else
										Fagin = 0;
									if (list_was.size() > 0)
										Fwas += 1;
									else
										Fwas = 0;
								}
							} else {
								if (status == 8)
									Flate = 0;
								if (status == 0) {
									if (list_agin.size() > 0)
										Fagin += 1;
									else
										Fagin = 0;
									if (list_late.size() > 0)
										Flate += 1;
									else
										Flate = 0;
									if (list_was.size() > 0)
										Fwas += 1;
									else
										Fwas = 0;
								}
							}
							if(Flate>Tlate)
								Flate=0;
							servicepatientqueue.updatepagertime(pager_ip, Fwas, Fagin, Flate, Ffirst);
							
							map.put("return_code", "success");
							map.put("return_msg", "呼叫成功");
							map.put("list", record);
							renderJson(map);
						}
					}
				}
			}
		} else {
			Record record = servicepatientqueue.call_next_pager(queue_type_id, pager_ip, login_id);
			if (record.size() == 0) {
				map.put("return_code", "fail");
				map.put("return_msg", "呼叫失败,无新患者");
				renderJson(map);
				return;
			} else {
				String id = record.getStr("id");
				if (id == null || id == "") {
					map.put("return_code", "fail");
					map.put("return_msg", "获取患者ID失败");
					renderJson(map);
					return;
				}
				boolean bool = servicepatientqueue.update_patient_state(pager_ip, login_id, Integer.parseInt(id),
						doctor_id);
				if (bool) {
					list_patient();
					map.put("return_code", "success");
					map.put("return_msg", "操作成功");
					map.put("list", record);
					Record r = servicepatientqueue.count_patient_pager(pager_ip, login_id);
					map.put("statistic", r);
					map.put("patient_name", record.getStr("patient_name"));
					getSession().setAttribute("id", id);
					renderJson(map);
				}
			}
		}

	}

	public void call_next_pager() {
		Map<String, Object> map = new HashMap<String, Object>();

		String pager_ip = shared.getIpAddr(getRequest());
		Record rt = triage.findbypagerip(pager_ip);
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请传入叫号器ID");
			renderJson(map);
			return;
		}

		List<Record> records = wait_pager(pager_ip);

		if (records.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "呼叫失败,无新患者");
			renderJson(map);
			return;
		} else {
			Record record = null;
			for (int i = 0; i < records.size(); i++) {
				if (records.get(i).getInt("state_patient") != 50) {
					record = records.get(i);
					break;
				}
			}
			/*for (int i = 0; i < records.size(); i++) {
				if (i <= rt.getInt("late_flag_step")) {
					if (records.get(i).getInt("state_patient") == 8 || records.get(i).getInt("state_patient") == 0)
						servicepatientqueue.updatePatientLateLock(records.get(i).getStr("patient_source_code"),
								records.get(i).getStr("queue_type_id"),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
				} else
					break;
			}*/
			if (record == null) {
				map.put("return_code", "fail");
				map.put("return_msg", "呼叫失败,无新患者");
				renderJson(map);
				return;
			} else {
				String id = record.getStr("id");
				int status = record.getInt("state_patient");
				if (id == null || id == "") {
					map.put("return_code", "fail");
					map.put("return_msg", "呼叫失败");
					renderJson(map);
					return;
				}
				String code = record.getStr("patient_source_code");
				int r = servicepatientqueue.callupdatestatusbypager(pager_ip, code, "51", pager_ip, "2");
				/// boolean bool = servicepatientqueue.callupdatestatusbypager(pager_ip,
				/// login_id, Integer.parseInt(id), doctor_id);
				if (r > 0) {
					Record rule = servicepatientqueue.selectTQrule(pager_ip);
					int Fagin = rule.getInt("call_return_first_flag");
					int Fwas = rule.getInt("call_pass_first_flag");
					int Flate = rule.getInt("call_late_first_flag");
					int Ffirst = rule.getInt("call_first_first_flag");
					int Tfrist = rule.getInt("first_flag_step");
					int Qfirst = rule.getInt("call_first_rule_flag");
					Pager rcd = pager.findByIP(pager_ip);
					int triageip = rcd.getTriage_id();
					Triage rip = triage.findById(triageip);

					for (int i = 0; i < records.size(); i++) {
						if (i <= rt.getInt("late_flag_step")) {
							if (records.get(i).getInt("late_lock") ==0)
								servicepatientqueue.updatePatientLateLock(records.get(i).getStr("patient_source_code"),
										records.get(i).getStr("queue_type_id"),
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
							status=records.get(i).getInt("state_patient");
						} else
							break;
					}
					List<Record> list_first = null;
					List<Record> list_agin = null;
					List<Record> list_was = null;
					List<Record> list_late = null;
					if (rip.getReorder_type() == 1) {
						list_first = servicepatientqueue.selectpatientpager(pager_ip, "5");
						list_agin = servicepatientqueue.selectpatientpager(pager_ip, "2");
						list_was = servicepatientqueue.selectpatientpager(pager_ip, "54");
						list_late = servicepatientqueue.selectpatientpager(pager_ip, "8");
					} else {
						String queuetypeid = queuetype.listPagerLogin(pager_ip).get(0).getStr("queue_type_id");
						list_first = servicepatientqueue.selectpatientpagerId2(queuetypeid, "5");
						list_agin = servicepatientqueue.selectpatientpagerId2(queuetypeid, "2");
						list_was = servicepatientqueue.selectpatientpagerId2(queuetypeid, "54");
						list_late = servicepatientqueue.selectpatientpagerId2(queuetypeid, "8");
					}
					if (status == 5) {
						if (list_first.size() > 0)
							Ffirst = Tfrist - Qfirst;
						else
							Ffirst = 0;
					} else {
						if (list_first.size() > 0)
							Ffirst += 1;
						else
							Ffirst = 0;
					}

					if (status == 2)
						Fagin = 0;

					if (status == 54)
						Fwas = 0;
					if (rip.getLate_show() == 1) {
						if (status == 8 || status == 0) {
							if (list_agin.size() > 0)
								Fagin += 1;
							else
								Fagin = 0;
							if (list_was.size() > 0)
								Fwas += 1;
							else
								Fwas = 0;
						}
					} else {
						if (status == 8)
							Flate = 0;
						if (status == 0) {
							if (list_agin.size() > 0)
								Fagin += 1;
							else
								Fagin = 0;
							if (list_late.size() > 0)
								Flate += 1;
							else
								Flate = 0;
							if (list_was.size() > 0)
								Fwas += 1;
							else
								Fwas = 0;
						}
					}
					servicepatientqueue.updatepagertime(pager_ip, Fwas, Fagin, Flate, Ffirst);
					setSessionAttr("code", code);
					map.put("return_code", "success");
					map.put("return_msg", "呼叫成功");
					map.put("list", record);
					renderJson(map);
				}
			}
		}

	}

	public void CallPatient_pager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = (String) getSession().getAttribute("code");
		String pager_ip = shared.getIpAddr(getRequest());
		int result = servicepatientqueue.callupdatestatusbypager(pager_ip, code, "51", pager_ip, "2");
		if (result > 0) {
			list_patient();
			map.put("return_code", "success");
			map.put("return_msg", "操作成功");
			// map.put("patient_name", l_r.get(0).getStr("patient_name"));
			getSession().setAttribute("code", code);
			// getSession().setAttribute("ip", pager_ip);
			renderJson(map);
		}
	}

	public void CallPatient() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		int doctor_id = rpager.getDoctor_id();
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "请填写登陆工号");
			renderJson(map);
			return;
		}

		String pager_ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "error");
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
		String type = rt.getStr("triage_type");
		Record rd = servicepatientqueue.select_patient_queue_id(pager_ip, login_id, type);

		if (null == rd) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有呼叫患者，无法重新呼叫");
			renderJson(map);
			return;
		}
		int patient_queue_id = rd.getInt("id");
		Record record = servicepatientqueue.recall(patient_queue_id, pager_ip, login_id, type);

		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "重呼患者失败");
			renderJson(map);
			return;
		} else {
			String id = rd.get("id").toString();
			if (id == null) {
				map.put("return_code", "fail");
				map.put("return_msg", "session获取失败");
				renderJson(map);
				return;
			}
			boolean bool = servicepatientqueue.update_patient_state(pager_ip, login_id, Integer.parseInt(id),
					doctor_id);
			if (bool) {
				map.put("return_code", "success");
				map.put("return_msg", "操作成功");
				map.put("list", record);
				getSession().setAttribute("id", patient_queue_id);
				renderJson(map);
			}

		}
	}

	public void SetPatientState_pager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = (String) getSession().getAttribute("code");
		String pager_ip = shared.getIpAddr(getRequest());

		String status = "53";
		if (code != null) {
			String type = getPara("status");
			if (Integer.parseInt(type) == 1)
				status = "53";
			else
				status = "54";
			int result = servicepatientqueue.callupdatestatusbypager(pager_ip, code, status, "0", "0");
			if (status.equals("54")) {
				Record rt = triage.findbypagerip(pager_ip);
				Record rd = servicepatientqueue.selectpatientbycode(pager_ip, code);
				int call_count = rd.getInt("call_count");
				int pass_time = rt.getInt("pass_time");
				if (pass_time > 0) {
					if (call_count >= pass_time) {
						status = "1";
						servicepatientqueue.callupdatestatusbypager(pager_ip, code, status, "0", "0");
					}
				}
			}

			if (result > 0) {
				list_patient();
				map.put("return_code", "success");
				map.put("return_msg", "操作成功");
				// map.put("patient_name", l_r.get(0).getStr("patient_name"));
				getSession().setAttribute("code", null);
				// getSession().setAttribute("ip", pager_ip);
				renderJson(map);
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "操作失败");
				getSession().setAttribute("code", null);
				renderJson(map);
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "操作失败");
			getSession().setAttribute("code", null);
			renderJson(map);
		}

	}

	public void call_select_pager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = getPara("code");

		String pager_ip = shared.getIpAddr(getRequest());
		String status2 = servicepatientqueue.selectpatientbycode(pager_ip, code).getStr("state_patient");
		String status = "51";
		int result = servicepatientqueue.callupdatestatusbypager(pager_ip, code, status, pager_ip, "2");
		if (result > 0) {
			list_patient();
			Record rule = servicepatientqueue.selectTQrule(pager_ip);
			/*
			 * int Tagin = rule.getInt("return_flag_step"); int Twas =
			 * rule.getInt("call_buffer"); int Tlate=rule.getInt("late_flag_step"); int
			 * Qagin = rule.getInt("call_return_rule_flag"); int Qwas =
			 * rule.getInt("call_pass_rule_flag"); int
			 * Qlate=rule.getInt("call_late_rule_flag"); if (Qagin == Tagin) Qagin = 0; else
			 * Qagin++; if (Qwas == Twas) Qwas = 0; else Qwas++; if(Qlate==Tlate) Qlate=0;
			 * else Qlate=0; int Fagin = 0; int Fwas = 0; int Flate=0; if (status2 == "2")
			 * Fwas = 1; else if (status2 == "54") Fagin = 1; else if(status2=="8") Flate=1;
			 * servicepatientqueue.updatepagertime(pager_ip, Qwas, Qagin, Fwas,
			 * Fagin,Qlate,Flate);
			 */
			Record record = servicepatientqueue.selectpatientbycodeandip(pager_ip, code);
			map.put("return_code", "success");
			map.put("return_msg", "操作成功");
			map.put("patient_name", record.getStr("patient_name"));
			setSessionAttr("code", code);
			// getSession().setAttribute("code", code);
			renderJson(map);
		}
	}

	public void SetPatientState() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));
		if (rpager.getDoctor_id() == null) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		String pager_ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "pager_ip不能为空");
			renderJson(map);
			return;
		}
		// System.out.println(pager_ip);
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "login_id不能为空");
			renderJson(map);
			return;
		}
		// System.out.println(login_id);
		int state = getParaToInt("state", -1);
		if (-1 == state) {
			map.put("return_code", "fail");
			map.put("return_msg", "state不能为空");
			renderJson(map);
			return;
		}
		// System.out.println(state);

		Record rt = triage.findtypelogin_id(login_id);
		if (null == rt) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询分诊台分诊模式失败");
			renderJson(map);
			return;
		}
		String type = rt.getStr("triage_type");
		Record rd = servicepatientqueue.select_patient_queue_id(pager_ip, login_id, type);
		if (null == rd) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有患者，无法设置状态");
			renderJson(map);
			return;
		}
		int patient_queue_id = rd.getInt("id");

		if (state == 0) {
			int call_count = rd.getInt("call_count") + 1;
			int pass_time = rt.getInt("pass_time");
			if (pass_time > 0) {
				if (call_count >= pass_time) {
					state = 3;
				}
			}
		}
		int queue_type_id = rd.getInt("queue_type_id");
		int num = servicepatientqueue.SetPatientState(patient_queue_id, pager_ip, login_id, state, type, queue_type_id);
		if (num < 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "更新患者状态失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "更新患者状态成功");
			renderJson(map);
		}
	}

	public void call_select() {
		Map<String, Object> map = new HashMap<String, Object>();
		Pager rpager = pager.findByIP(shared.getIpAddr(getRequest()));

		if (null == rpager.getDoctor_id()) {
			map.put("return_code", "loginout");
			map.put("return_msg", "loginout");
			renderJson(map);
			return;
		}
		int doctor_id = rpager.getDoctor_id();

		String patient_queue_id = getPara("patient_queue_id");
		if (StringKit.isBlank(patient_queue_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "patient_queue_id不能为空");
			renderJson(map);
			return;
		}
		String queue_type_id = getPara("queue_type_id");
		System.out.println(queue_type_id);
		if (StringKit.isBlank(queue_type_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "queue_type_id不能为空");
			renderJson(map);
			return;
		}
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "login_id不能为空");
			renderJson(map);
			return;
		}
		String pager_ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "pager_id不能为空");
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
		// String status=servicepatientqueue.selectpatientbyid(patient_queue_id,
		// queue_type_id, pager_ip, login_id, type);
		Record rd = servicepatientqueue.select_state_patient(pager_ip, login_id, type);
		if (null != rd) {
			map.put("return_code", "fail");
			map.put("return_msg", "\"" + rd.get("patient_name") + "\"患者未进行诊结，过号操作，不能呼叫下一患者");
			map.put("patient_name", rd.get("patient_name"));
			map.put("patient_id", rd.get("patient_id"));
			renderJson(map);
			return;
		}
		Record record = servicepatientqueue.call_select(patient_queue_id, queue_type_id, pager_ip, login_id, type);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "呼叫失败,无此患者");
			renderJson(map);
			return;
		} else {
			String id = record.getStr("id");
			if (id == null || id == "") {
				map.put("return_code", "fail");
				map.put("return_msg", "呼叫失败");
				renderJson(map);
				return;
			}
			String status = record.getStr("state_patient");
			boolean bool = servicepatientqueue.update_patient_state(pager_ip, login_id, Integer.parseInt(id),
					doctor_id);
			if (bool) {
				Record rule = servicepatientqueue.selectTQrule(pager_ip);
				/*
				 * int Tagin = rule.getInt("return_flag_step"); int Twas =
				 * rule.getInt("call_buffer"); int Tlate =rule.getInt("late_flag_step"); int
				 * Qagin = rule.getInt("call_return_rule_flag"); int Qwas =
				 * rule.getInt("call_pass_rule_flag"); int
				 * Qlate=rule.getInt("call_late_rule_flag"); if (Qagin == Tagin) Qagin = 0; else
				 * Qagin++; if (Qwas == Twas) Qwas = 0; else Qwas++; if(Qlate==Tlate) Qlate=0;
				 * else Qlate++; int Fagin = 0; int Fwas = 0; int Flate=0; if (status == "2")
				 * Fwas = 1; else if (status == "54") Fagin = 1; else if(status=="8") Flate=1;
				 * servicepatientqueue.updatepagertime(pager_ip, Qwas, Qagin, Fwas,
				 * Fagin,Qlate,Flate);
				 */
				map.put("return_code", "success");
				map.put("return_msg", "呼叫成功");
				map.put("list", record);
				renderJson(map);
			}
		}
	}

	/**
	 * 根据队列获取患者状态 传入参数status:wait,pass,over,visting queue_type_id:队列ID
	 * login_id：医生登录号
	 **/
	public void list_state_patient() {
		Map<String, Object> map = new HashMap<String, Object>();

		String pager_ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取pager_ip失败");
			renderJson(map);
			return;
		}
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取login_id失败");
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
		Record rd = servicepatientqueue.select_state_patient(pager_ip, login_id, type);
		if (null != rd) {
			map.put("return_code", "fail");
			map.put("return_msg", "\"" + rd.get("patient_name") + "\"患者未进行诊结，过号操作，不能呼叫下一患者");
			map.put("patient_name", rd.get("patient_name"));
			map.put("patient_id", rd.get("patient_id"));
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "可以进行呼叫操作");
			renderJson(map);
		}

		/*
		 * 
		 * Map<String, Object> map = new HashMap<String, Object>(); String type =
		 * getPara("status"), status = "0,3,4,6,7,50", queue_type_id =
		 * getPara("queue_type_id"); boolean isvisting = false; switch (type) { case
		 * "wait": status = "0,3,4,6,7,50"; break; case "pass": status = "54"; break;
		 * case "over": status = "53"; break; case "visting": status = "51"; isvisting =
		 * true; break; default: break; } // 获取叫号器ip String pager_ip =
		 * shared.getIpAddr(getRequest()); if (StringKit.isBlank(pager_ip)) {
		 * map.put("return_code", "fail"); map.put("return_msg", "获取pager_ip失败");
		 * renderJson(map); return; } // 获取医生登录工号 String login_id = getPara("login_id");
		 * if (StringKit.isBlank(login_id)) { map.put("return_code", "fail");
		 * map.put("return_msg", "获取login_id失败"); renderJson(map); return; } Record rt =
		 * triage.findtypelogin_id(login_id); if (null == rt) { map.put("return_code",
		 * "fail"); map.put("return_msg", "查询分诊台分诊模式失败"); renderJson(map); return; }
		 * List<Record> l_r = null; if (type.equals("wait")) l_r = wait(queue_type_id,
		 * login_id, rt.getStr("triage_type"), pager_ip); else {
		 * if(rt.getStr("triage_type").equals("1"))
		 * l_r=servicepatientqueue.selectpatientdoctor(pager_ip, status); else l_r =
		 * servicepatientqueue.selectpatientpager(pager_ip, status); }
		 * 
		 * map.put("return_code", "success"); map.put("return_msg", "获取患者信息成功");
		 * map.put("list", l_r); map.put("count", l_r.size()); if (isvisting) { if
		 * (l_r.size() > 0) { getSession().setAttribute("code",
		 * l_r.get(0).getStr("patient_source_code")); map.put("patient_name",
		 * l_r.get(0).getStr("patient_name")); } } renderJson(map);
		 */
	}

	public void GetStatistic() {
		Map<String, Object> map = new HashMap<String, Object>();
		int queue_type_id = getParaToInt("queue_type_id", -1);
		if (-1 == queue_type_id) {
			map.put("return_code", "fail");
			map.put("return_msg", "队列编号不能为空");
			renderJson(map);
			return;
		}
		String queuetypeid = String.valueOf(queue_type_id);
		String pager_ip = getPara("pager_ip");
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "叫号器IP不能为空");
			renderJson(map);
			return;
		}
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "医生工号不能为空");
			renderJson(map);
			return;
		}
		Record record = servicepatientqueue.GetStatistic(queuetypeid, pager_ip, login_id);
		if (null == record) {
			map.put("return_code", "fail");
			map.put("return_msg", "操作失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "操作成功");
			map.put("statistic", record);
			renderJson(map);
		}
	}

	public void GetPatientList() {
		Map<String, Object> map = new HashMap<String, Object>();
		String Queue_type_source_id = getPara("Queue_type_source_id");
		if (StringKit.isBlank(Queue_type_source_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "队列编号不能为空");
			renderJson(map);
			return;
		}
		String pager_ip = getPara("pager_ip");
		if (StringKit.isBlank(pager_ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "叫号器IP不能为空");
			renderJson(map);
			return;
		}
		String login_id = getPara("login_id");
		if (StringKit.isBlank(login_id)) {
			map.put("return_code", "fail");
			map.put("return_msg", "医生工号不能为空");
			renderJson(map);
			return;
		}
		if (null == getSessionAttr("login")) {
			map.put("return_code", "fail");
			map.put("return_msg", "医生未登录");
			renderJson(map);
			return;
		}
		List<Record> list = servicepatientqueue.GetPatientList(Queue_type_source_id, pager_ip, login_id);
		if (null == list) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("list", list);
			renderJson(map);
		}
	}

	public void getfztname() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			renderJson(map);
			return;
		}

		Terminal r = terminal.findbyip(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			map.put("fztname", "未知");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "获取成功");
			map.put("fztname", r != null ? r.getName() : "");
			renderJson(map);
			return;
		}
	}

	public void list_patient_wait_call() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		String[] aa = new String[0];

		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", aa);
			renderJson(map);
			return;
		}

		Terminal r = terminal.findbyip(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			map.put("fztname", "未知");
			map.put("list", aa);
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
		List<Record> lt = null;
		Triage rip = triage.queryTriageIp(triageip);

		if ("1".equals(rip.getTriage_type().toString())) {
			lt = queuetype.list_queue_type_wait_call(ip);
		} else {
			if (rip.getReorder_type() == 1)
				lt = queuetype.list_queue_type_wait_call_pager(ip);
			else
				lt = queuetype.listPagerLogin2_pager(ip);
		}
		if (lt.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有队列");
			map.put("fztname", r.getName());
			map.put("list", aa);
			renderJson(map);
			return;
		}
		if ("1".equals(rip.getTriage_type().toString())) {
			String json = "{\"count\":" + lt.size() + ",\"fztname\":\"" + r.getName() + "\",\"list\":[";
			for (Record rd : lt) {
				String queuetypeid = rd.getStr("queue_type_id");
				String login_id = servicepatientqueue.finddoctorid(queuetypeid, "1").get(0).getStr("login_id");
				Record dqjzr = servicepatientqueue.list_patient_visit(queuetypeid);
				String pager_ip = rd.getStr("ip");
				/*
				 * try {
				 * 
				 * json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" +
				 * dqjzr.get("register_id") + "\","; } catch (Exception e) { json +=
				 * "{\"jz_name\":\"\",\"jzbh\":\"\",";
				 * 
				 * }
				 */
				try {
					json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" + dqjzr.get("register_id")
							+ "\",\"call_time\":\"" + dqjzr.get("call_time") + "\",\"jzbh2\":\""
							+ dqjzr.get("register_id2") + "\",\"state_patient2\":\"" + dqjzr.get("state_patient2")
							+ "\",";
				} catch (Exception e) {
					json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"jzbh2\":\"\",\"state_patient2\":\"\",\"call_time\":\"\",";

				}
				List<Record> top10 = wait(queuetypeid, login_id, "1", pager_ip, rip.getLate_show().toString());
				for (int i = 0; i < 10; i++) {
					try {

						json += "\"dh_name" + i + "" + "\":\"" + top10.get(i).getStr("patient_name") + "\",\"dh_id" + i
								+ "\":\"" + top10.get(i).getStr("register_id") + "\",\"dh_state" + i + "\":"
								+ top10.get(i).getStr("state_patient") + ",";
					} catch (Exception e) {
						json += "\"dh_name" + i + "" + "\":\"\",\"dh_id" + i + "\":\"\",\"dh_state" + i + "\":-1,";
					}
				}

				json = json.substring(0, json.length() - 1);

				json += ",\"login_id\":\"" + login_id + "\",\"zsmc_display\":\"" + rd.get("queue_type_displayname")
						+ "\",\"zsmc\":\"" + rd.get("name") + "\",\"pagerName\":\"" + rd.get("display_name")
						+ "\",\"pager\":\"" + rd.get("pager_name") + "\"";
				json += "},";

			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			for (Record rd : lt) {
				// if(rip.getReorder_type()==1)
				servicepatientqueue.updatecontent(rd.get("patient_id").toString());
				// else
				// servicepatientqueue.updatecontent2(ip);

			}
			renderJson(json);
		} else {
			String json = "{\"count\":" + lt.size() + ",\"fztname\":\"" + r.getName() + "\",\"list\":[";

			for (Record rd : lt) {
				String queuetypeid = rd.getStr("queue_type_id");
				String login_id = rd.getStr("login_id");
				try {
					Record dqjzr = null;
					if (rip.getReorder_type() == 1)
						dqjzr = servicepatientqueue.list_patient_visit(queuetypeid);
					else
						dqjzr = servicepatientqueue.selectpatientpagerId(queuetypeid, "51").get(0);
					// json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\""
					// + dqjzr.get("register_id") + "\",";
					json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" + dqjzr.get("register_id")
							+ "\",\"call_time\":\"" + dqjzr.get("call_time") + "\",\"jzbh2\":\""
							+ dqjzr.get("register_id2") + "\",\"state_patient2\":\"" + dqjzr.get("state_patient2")
							+ "\",";
				} catch (Exception e) {
					// json += "{\"jz_name\":\"\",\"jzbh\":\"\",";
					json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"jzbh2\":\"\",\"state_patient2\":\"\",\"call_time\":\"\",";
				}
				List<Record> top10 = wait_pager(rd.getStr("ip"));
				for (int i = 0; i < 10; i++) {
					try {

						json += "\"dh_name" + i + "" + "\":\"" + top10.get(i).getStr("patient_name") + "\",\"dh_id" + i
								+ "\":\"" + top10.get(i).getStr("register_id") + "\",\"dh_state" + i + "\":"
								+ top10.get(i).getStr("state_patient") + ",";
					} catch (Exception e) {
						json += "\"dh_name" + i + "" + "\":\"\",\"dh_id" + i + "\":\"\",\"dh_state" + i + "\":-1,";
					}
				}

				json = json.substring(0, json.length() - 1);
				json += ",\"login_id\":\"" + login_id + "\",\"zsmc_display\":\"" + rd.get("queue_type_displayname")
						+ "\",\"zsmc\":\"" + rd.get("name") + "\",\"pagerName\":\"" + rd.get("display_name")
						+ "\",\"pager\":\"" + rd.get("pager_name") + "\"";
				json += "},";
				// json += ",\"login_id\":\"" + login_id + "\",\"zsmc\":\"" + rd.get("name") +
				// "\",\"pagerName\":\""+rd.get("pager_displayname")+"\"";
				// json += "},";

			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			for (Record rd : lt) {
				if (rip.getReorder_type() == 1)
					servicepatientqueue.updatecontent(rd.get("patient_id").toString());
				else
					servicepatientqueue.updatecontent2(ip);

			}

			renderJson(json);
		}
	}
	/**
	 * 功能描述: 获取等候列表
	 * @param
	 * @author : li.fei
	 * @date : 2019/6/3
	 **/
	public void list_patient_wait() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		String[] aa = new String[0];

		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", aa);
			renderJson(map);
			return;
		}

		Terminal r = terminal.findbyip(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			map.put("fztname", "未知");
			map.put("list", aa);
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
		List<Record> lt = null;
		Triage rip = triage.queryTriageIp(triageip);

		if ("1".equals(rip.getTriage_type().toString())) {
			lt = queuetype.list_queue_type(ip);
		} else {
			if (rip.getReorder_type() == 1)
				lt = queuetype.list_queue_type_pager(ip);
			else
				lt = queuetype.listPagerLogin2(ip);
		}
		if (lt.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有队列");
			map.put("fztname", r.getName());
			map.put("list", aa);
			renderJson(map);
			return;
		}
		if ("1".equals(rip.getTriage_type().toString())) {
			String json = "{\"count\":" + lt.size() + ",\"fztname\":\"" + r.getName() + "\",\"list\":[";
			for (Record rd : lt) {
				String queuetypeid = rd.getStr("queue_type_id");
				String login_id = servicepatientqueue.finddoctorid(queuetypeid, "1").get(0).getStr("login_id");
				Record dqjzr = servicepatientqueue.list_patient_visit(queuetypeid);
				String pager_ip = rd.getStr("ip");
				try {
					json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" + dqjzr.get("register_id")
							+ "\",\"call_time\":\"" + dqjzr.get("call_time") + "\",\"jzbh2\":\""
							+ dqjzr.get("register_id2") + "\",\"state_patient2\":\"" + dqjzr.get("state_patient2")
							+ "\",";
				} catch (Exception e) {
					json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"jzbh2\":\"\",\"state_patient2\":\"\",\"call_time\":\"\",";

				}

				List<Record> top10 = wait(queuetypeid, login_id, "1", pager_ip, rip.getLate_show().toString());
				for (int i = 0; i < 10; i++) {
					try {

						json += "\"dh_name" + i + "" + "\":\"" + top10.get(i).getStr("patient_name") + "\",\"dh_id" + i
								+ "\":\"" + top10.get(i).getStr("register_id") + "\",\"dh_state" + i + "\":"
								+ top10.get(i).getStr("state_patient") + ",";
					} catch (Exception e) {
						json += "\"dh_name" + i + "" + "\":\"\",\"dh_id" + i + "\":\"\",\"dh_state" + i + "\":-1,";
					}
				}

				json = json.substring(0, json.length() - 1);

				json += ",\"login_id\":\"" + login_id + "\",\"zsmc_display\":\"" + rd.get("queue_type_displayname")
						+ "\",\"zsmc\":\"" + rd.get("name") + "\",\"pagerName\":\"" + rd.get("display_name")
						+ "\",\"pager\":\"" + rd.get("pager_name") + "\"";
				json += "},";

			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			renderJson(json);
		} else {
			String json = "{\"count\":" + lt.size() + ",\"fztname\":\"" + r.getName() + "\",\"list\":[";

			for (Record rd : lt) {
				String queuetypeid = rd.getStr("queue_type_id");
				String login_id = rd.getStr("login_id");
				try {
					Record dqjzr = null;
					if (rip.getReorder_type() == 1)
						dqjzr = servicepatientqueue.list_patient_visit(queuetypeid);
					else
						dqjzr = servicepatientqueue.selectpatientpagerId(queuetypeid, "51").get(0);
					// json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\""
					// + dqjzr.get("register_id") +
					// "\",\"call_time\":\""+dqjzr.get("call_time")+"\",";
					json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" + dqjzr.get("register_id")
							+ "\",\"call_time\":\"" + dqjzr.get("call_time") + "\",\"jzbh2\":\""
							+ dqjzr.get("register_id2") + "\",\"state_patient2\":\"" + dqjzr.get("state_patient2")
							+ "\",";
				} catch (Exception e) {
					// json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"call_time\":\"\",";
					json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"jzbh2\":\"\",\"state_patient2\":\"\",\"call_time\":\"\",";
				}

				List<Record> top10 = wait_pager(rd.getStr("ip"));
				for (int i = 0; i < 10; i++) {
					try {

						json += "\"dh_name" + i + "" + "\":\"" + top10.get(i).getStr("patient_name") + "\",\"dh_id" + i
								+ "\":\"" + top10.get(i).getStr("register_id") + "\",\"dh_state" + i + "\":"
								+ top10.get(i).getStr("state_patient") + ",";
					} catch (Exception e) {
						json += "\"dh_name" + i + "" + "\":\"\",\"dh_id" + i + "\":\"\",\"dh_state" + i + "\":-1,";
					}
				}

				json = json.substring(0, json.length() - 1);
				json += ",\"login_id\":\"" + login_id + "\",\"zsmc_display\":\"" + rd.get("queue_type_displayname")
						+ "\",\"zsmc\":\"" + rd.get("name") + "\",\"pagerName\":\"" + rd.get("display_name")
						+ "\",\"pager\":\"" + rd.get("pager_name") + "\"";
				json += "},";
				// json += ",\"login_id\":\"" + login_id + "\",\"zsmc\":\"" + rd.get("name") +
				// "\",\"pagerName\":\""+rd.get("pager_displayname")+"\"";
				// json += "},";

			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			renderJson(json);
		}
	}

	public void list_patient_wait_docs() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		String[] aa = new String[0];

		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", aa);
			renderJson(map);
			return;
		}

		Terminal r = terminal.findbyip(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			map.put("fztname", "未知");
			map.put("list", aa);
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
		List<Record> lt = null;
		Triage rip = triage.queryTriageIp(triageip);

		if ("1".equals(rip.getTriage_type().toString())) {
			lt = queuetype.list_queue_type_all(ip);
		} else {
			if (rip.getReorder_type() == 1)
				lt = queuetype.list_queue_type_pager_all(ip);
			else
				lt = queuetype.listPagerLogin2_all(ip);
		}
		if (lt.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有队列");
			map.put("fztname", r.getName());
			map.put("list", aa);
			renderJson(map);
			return;
		}
		if ("1".equals(rip.getTriage_type().toString())) {
			String json = "{\"count\":" + lt.size() + ",\"fztname\":\"" + r.getName() + "\",\"list\":[";
			for (Record rd : lt) {
				String queuetypeid = rd.getStr("queue_type_id");
				String login_id = servicepatientqueue.finddoctorid(queuetypeid, "1").get(0).getStr("login_id");
				Record dqjzr = servicepatientqueue.list_patient_visit(queuetypeid);
				String pager_ip = rd.getStr("ip");
				try {
					json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" + dqjzr.get("register_id")
							+ "\",\"call_time\":\"" + dqjzr.get("call_time") + "\",\"jzbh2\":\""
							+ dqjzr.get("register_id2") + "\",\"state_patient2\":\"" + dqjzr.get("state_patient2")
							+ "\",";
				} catch (Exception e) {
					json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"jzbh2\":\"\",\"state_patient2\":\"\",\"call_time\":\"\",";

				}

				List<Record> top10 = wait(queuetypeid, login_id, "1", pager_ip, rip.getLate_show().toString());
				for (int i = 0; i < 10; i++) {
					try {

						json += "\"dh_name" + i + "" + "\":\"" + top10.get(i).getStr("patient_name") + "\",\"dh_id" + i
								+ "\":\"" + top10.get(i).getStr("register_id") + "\",\"dh_state" + i + "\":"
								+ top10.get(i).getStr("state_patient") + ",";
					} catch (Exception e) {
						json += "\"dh_name" + i + "" + "\":\"\",\"dh_id" + i + "\":\"\",\"dh_state" + i + "\":-1,";
					}
				}

				json = json.substring(0, json.length() - 1);

				json += ",\"login_id\":\"" + login_id + "\",\"zsmc_display\":\"" + rd.get("queue_type_displayname")
						+ "\",\"zsmc\":\"" + rd.get("name") + "\",\"pagerName\":\"" + rd.get("display_name")
						+ "\",\"pager\":\"" + rd.get("pager_name") + "\"";
				json += "},";

			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			renderJson(json);
		} else {
			String json = "{\"count\":" + lt.size() + ",\"fztname\":\"" + r.getName() + "\",\"list\":[";

			for (Record rd : lt) {
				String queuetypeid = rd.getStr("queue_type_id");
				String login_id = rd.getStr("login_id");
				try {
					Record dqjzr = null;
					if (rip.getReorder_type() == 1)
						dqjzr = servicepatientqueue.list_patient_visit(queuetypeid);
					else
						dqjzr = servicepatientqueue.selectpatientpagerId(queuetypeid, "51").get(0);
					// json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\""
					// + dqjzr.get("register_id") +
					// "\",\"call_time\":\""+dqjzr.get("call_time")+"\",";
					json += "{\"jz_name\":\"" + dqjzr.get("patient_name") + "\",\"jzbh\":\"" + dqjzr.get("register_id")
							+ "\",\"call_time\":\"" + dqjzr.get("call_time") + "\",\"jzbh2\":\""
							+ dqjzr.get("register_id2") + "\",\"state_patient2\":\"" + dqjzr.get("state_patient2")
							+ "\",";
				} catch (Exception e) {
					// json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"call_time\":\"\",";
					json += "{\"jz_name\":\"\",\"jzbh\":\"\",\"jzbh2\":\"\",\"state_patient2\":\"\",\"call_time\":\"\",";
				}

				List<Record> top10 = wait_pager(rd.getStr("ip"));
				for (int i = 0; i < 10; i++) {
					try {

						json += "\"dh_name" + i + "" + "\":\"" + top10.get(i).getStr("patient_name") + "\",\"dh_id" + i
								+ "\":\"" + top10.get(i).getStr("register_id") + "\",\"dh_state" + i + "\":"
								+ top10.get(i).getStr("state_patient") + ",";
					} catch (Exception e) {
						json += "\"dh_name" + i + "" + "\":\"\",\"dh_id" + i + "\":\"\",\"dh_state" + i + "\":-1,";
					}
				}

				json = json.substring(0, json.length() - 1);
				json += ",\"login_id\":\"" + login_id + "\",\"zsmc_display\":\"" + rd.get("queue_type_displayname")
						+ "\",\"zsmc\":\"" + rd.get("name") + "\",\"pagerName\":\"" + rd.get("display_name")
						+ "\",\"pager\":\"" + rd.get("pager_name") + "\"";
				json += "},";
				// json += ",\"login_id\":\"" + login_id + "\",\"zsmc\":\"" + rd.get("name") +
				// "\",\"pagerName\":\""+rd.get("pager_displayname")+"\"";
				// json += "},";

			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			renderJson(json);
		}
	}

	public void list_patient_wait2() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		String[] aa = new String[0];

		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", aa);
			renderJson(map);
			return;
		}

		Terminal r = terminal.findbyip(ip);
		if (null == r) {
			map.put("return_code", "fail");
			map.put("return_msg", "查询失败");
			map.put("fztname", "未知");
			map.put("list", aa);
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
		List<Record> lt = null;
		Triage rip = triage.queryTriageIp(triageip);

		if ("1".equals(rip.getTriage_type().toString())) {
			map.put("return_code", "fail");
			map.put("return_msg", "该模式无法使用此方法");
			map.put("list", aa);
			renderJson(map);
			return;
		} else {
			if (rip.getReorder_type() == 1)
				lt = queuetype.list_queue_type_pager_jz(ip);
			else {
				map.put("return_code", "fail");
				map.put("return_msg", "该模式无法使用此方法");
				map.put("list", aa);
				renderJson(map);
			}
		}
		if (lt.size() == 0) {
			map.put("return_code", "fail");
			map.put("return_msg", "没有队列");
			map.put("terminalname", r.getName());
			map.put("list", aa);
			renderJson(map);
			return;
		} else {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("terminalname", r.getName());
			map.put("count", lt.size());
			map.put("list", lt);
			renderJson(map);
		}

	}

	public void list_patient_termainal() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		String[] aa = new String[0];
		List<Record> result = new ArrayList<Record>();
		List<Record> final_result = new ArrayList<Record>();

		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", aa);
			renderJson(map);
			return;
		}
		String trip = terminal.findbytriageip(ip).getStr("ip");
		Triage rip = triage.findById(trip);
		Record rule = terminal.findbytriageip(ip);
		List<Record> list_first = servicepatientqueue.selectpatientteminal(ip, "5");
		List<Record> list_wait2 = servicepatientqueue.selectpatientteminal(ip, "0,3,4,6,7,50");
		List<Record> list_agin = servicepatientqueue.selectpatientteminal(ip, "2");
		List<Record> list_was = servicepatientqueue.selectpatientteminal(ip, "54");
		List<Record> list_late = servicepatientqueue.selectpatientteminal(ip, "8");
		List<Record> list_wait = new ArrayList<Record>();

		int Tagin = rule.getInt("return_flag_step");
		int Twas = rule.getInt("call_buffer");
		int Tlate = rule.getInt("late_flag_step");
		int Tfirst = rule.getInt("first_flag_step");
		int Qagin = rule.getInt("call_return_rule_flag");
		int Qwas = rule.getInt("call_pass_rule_flag");
		int Qlate = rule.getInt("call_late_rule_flag");
		int Qfirst = rule.getInt("call_first_rule_flag");
		int Qfirst2 = rule.getInt("call_first_rule_flag2");

		int Fagin = rule.getInt("call_return_first_flag");
		int Fwas = rule.getInt("call_pass_first_flag");
		int Flate = rule.getInt("call_late_first_flag");
		int Ffirst = rule.getInt("call_first_first_flag");

		ArrayList<orderArylist> list = new ArrayList<>();

		if (rip.getLate_show() == 1) {
			if (list_late.size() > 0) {
				if (list_wait2.size() > 0) {
					List<Record> list_unlock_wait = new ArrayList<>();
					for (int i = 0; i < list_late.size(); i++) {
						if (list_late.get(i).getInt("late_lock") == 1)
							list_wait.add(list_late.get(i));
						else
							list_unlock_wait.add(list_late.get(i));
					}
					for (int i = 0; i < list_wait2.size(); i++) {
						if (list_wait2.get(i).getInt("late_lock") == 1)
							list_wait.add(list_wait2.get(i));
						else
							list_unlock_wait.add(list_wait2.get(i));
					}
					if (list_wait.size() > 0)
						Collections.sort(list_wait, new MyComprator3());
					if (list_unlock_wait.size() > 0)
						Collections.sort(list_unlock_wait, new MyComprator2());
					list_wait.addAll(list_unlock_wait);
				} else {
					// Collections.sort(list_late, new MyComprator3());
					list_wait = list_late;
				}
			} else
				list_wait = list_wait2;

		} else {
			list_wait = list_wait2;
			if (list_late.size() > 0) {
				orderArylist oaLate = new orderArylist();
				oaLate.F = Flate;
				oaLate.Q = Qlate;
				oaLate.T = Tlate;
				oaLate.id = 2;
				oaLate.name = "late";
				oaLate.l_r = list_late;
				list.add(oaLate);
			}
		}

		if (list_agin.size() > 0) {
			orderArylist oaAgin = new orderArylist();
			oaAgin.F = Fagin;
			oaAgin.Q = Qagin;
			oaAgin.T = Tagin;
			oaAgin.id = 1;
			oaAgin.name = "agin";
			oaAgin.l_r = list_agin;
			list.add(oaAgin);
		}

		if (list_was.size() > 0) {
			orderArylist oaWas = new orderArylist();
			oaWas.F = Fwas;
			oaWas.Q = Qwas;
			oaWas.T = Twas;
			oaWas.id = 3;
			oaWas.name = "was";
			oaWas.l_r = list_was;
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
		if (list_first.size() > 0) {
			if (result.size() > 0) {
				final_result.addAll(result.subList(0, Tfirst - Ffirst));
				if (Qfirst == 0) {
					final_result.addAll(list_first);
					final_result.addAll(result.subList(Tfirst - Ffirst, result.size()));
				} else {
					final_result.addAll(list_first.subList(0, Qfirst));
					final_result.addAll(result.subList(Tfirst - Ffirst, Tfirst - Ffirst + Qfirst2));
					final_result.addAll(list_first.subList(Qfirst, list_first.size()));
					final_result.addAll(result.subList(Tfirst - Ffirst + Qfirst2, result.size()));
				}
			} else
				final_result = list_first;
		} else
			final_result = result;
		List<Record> calledList = servicepatientqueue.selectIsBegin((byte) 2, ip);
		if (calledList.size() > 0) {
			for (int i = 0; i < final_result.size(); i++) {

				/*if (i < rule.getInt("late_flag_step")) {
					if (final_result.get(i).getInt("late_lock") == 0) {
						if (final_result.get(i).getInt("state_patient") == 8
								|| final_result.get(i).getInt("state_patient") == 0)
							servicepatientqueue.updatePatientLateLock(final_result.get(i).getStr("patient_source_code"),
									final_result.get(i).getStr("queue_type_id"),
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS").format(new Date()));
					}
				} else
					break;
				*/
			}
		}
		map.put("return_code", "success");
		map.put("return_msg", "获取成功");
		map.put("count", final_result.size());
		map.put("list", final_result);
		renderJson(map);
		return;
	}

	public void list_patient_termainal_pass() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shared.getIpAddr(getRequest());
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", null);
			renderJson(map);
			return;
		}
		String triType = terminal.findtriageBytemIP(ip).getStr("triage_type");
		List<Record> l_rcd = servicepatientqueue.selectPassByTer(triType, ip);
		if (l_rcd.size() > 0) {
			map.put("return_code", "success");
			map.put("return_msg", "获取成功");
			map.put("count", l_rcd.size());
			map.put("list", l_rcd);
			renderJson(map);
			return;
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "获取失败");
			map.put("count", 0);
			renderJson(map);
			return;
		}
	}

	public void vistinglocaltion() {
		Map<String, Object> map = new HashMap<String, Object>();
		// String ip
		// =terminal.findbytriageip(shared.getIpAddr(getRequest())).getStr("ip") ;
		String ip = getPara("ip");
		String[] aa = new String[0];
		if (StringKit.isBlank(ip)) {
			map.put("return_code", "fail");
			map.put("return_msg", "获取ip失败");
			map.put("count", 0);
			map.put("fztname", "未知");
			map.put("list", aa);
			renderJson(map);
			return;
		}
		Terminal r = terminal.findbyip(ip);
		List<Record> l_rcd = servicepatientqueue.vistinglocaltion(ip);
		if (l_rcd.size() > 0) {
			map.put("return_code", "success");
			map.put("return_msg", "获取成功");
			map.put("count", l_rcd.size());
			map.put("list", l_rcd);
			map.put("fztname", r != null ? r.getName() : "");
			renderJson(map);
			return;
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "获取失败");
			map.put("count", 0);
			map.put("fztname", r != null ? r.getName() : "");
			renderJson(map);
			return;
		}
	}

	public void AutoSetPatientDisplay() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("return_code", "success");
		map.put("return_msg", "操作完成");
		renderJson(map);
		/*
		 * // String ss=shared.getIpAddr(getRequest()); String ip = getPara("ip"); if
		 * (ip == null || ip == "") ip =
		 * terminal.findbytriageip(shared.getIpAddr(getRequest())).getStr("ip");
		 * String[] aa = new String[0];
		 * 
		 * if (StringKit.isBlank(ip)) { map.put("return_code", "fail");
		 * map.put("return_msg", "获取ip失败"); map.put("count", 0); map.put("fztname",
		 * "未知"); map.put("list", aa); renderJson(map); return; } Boolean b_r = true;
		 * List<Record> l_rcd = servicepatientqueue.selectPatientDisplay(ip); for
		 * (Record rd : l_rcd) { if (rd.getInt("is_display") == 1) { int sort = 0;
		 * String p_ip = ""; String code = rd.getStr("patient_source_code"); if
		 * (rd.getInt("counts") > 1) { Record lest_rcd =
		 * servicepatientqueue.selectlestpatientqueue(code); p_ip =
		 * lest_rcd.getStr("ip"); String s = lest_rcd.getStr("r_id"); if (s == null ||
		 * s.isEmpty()) sort = 1; else sort = Integer.parseInt(s) + 1; } else { p_ip =
		 * rd.getStr("ip"); String s =
		 * servicepatientqueue.getqueuemaxregisterid(p_ip).getInt("register_id").
		 * toString(); if (s == null || s.isEmpty()) sort = 1; else { sort =
		 * Integer.parseInt(s) + 1; } } int r =
		 * servicepatientqueue.updatesetpatientdisplay(p_ip, String.valueOf(sort),
		 * code); if (r == 0) { b_r = false; break; } } } if (b_r) {
		 * map.put("return_code", "success"); map.put("return_msg", "操作完成");
		 * renderJson(map); } else { map.put("return_code", "fail");
		 * map.put("return_msg", "操作未完成"); renderJson(map); } return;
		 */
	}
}