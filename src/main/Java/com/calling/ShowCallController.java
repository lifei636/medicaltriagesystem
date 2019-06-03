package com.calling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import org.beetl.sql.core.kit.StringKit;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.shine.model.Doctor;
import com.shine.model.Pager;
import com.shine.model.QueueType;
import com.shine.model.Terminal;
import com.shine.service.DoctorService;
import com.shine.service.PagerService;
import com.shine.service.QueueTypeService;
import com.shine.service.TerminalService;
import com.shine.service.TriageService;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.PagerServiceImpl;
import com.shine.service.impl.QueueTypeServiceImpl;
import com.shine.service.impl.TerminalServiceImpl;
import com.shine.service.impl.TriageServiceImpl;

import oracle.jdbc.driver.OracleDriver;

@ControllerBind(controllerKey = "/showcall")
public class ShowCallController extends BaseController {

	DoctorService doctor = new DoctorServiceImpl();
	QueueTypeService service = new QueueTypeServiceImpl();
	TriageService triage = new TriageServiceImpl();
	ShardKit shard = new ShardKit();
	TerminalService terminal = new TerminalServiceImpl();
	PagerService pager = new PagerServiceImpl();

	// 医生登录接口
	public void login() {
		Map<String, Object> map = new HashMap<String, Object>();
		String login_id = getPara("login_id");
		String ip = getPara("ip");
		if (!StringKit.isBlank(login_id) && !StringKit.isBlank(ip)) {
			if (isIPAddress(ip)) {
				Doctor d = doctor.findByLogin_id(login_id);
				if (d != null) {
					String doctor_id = d.getDoctor_id().toString();
					try {
						int r = service.showloginupdate(ip, doctor_id);
						if (r > 0) {
							map.put("return_code", "success");
							map.put("return_msg", "登录成功");
							renderJson(map);
							return;
						} else {
							map.put("return_code", "fail");
							map.put("return_msg", "未在系统中找到当前诊室队列标识的信息");
							renderJson(map);
							return;
						}
					} catch (Exception e) {
						map.put("return_code", "fail");
						map.put("return_msg", "未在系统中找到当前诊室队列标识的信息");
						renderJson(map);
						return;
					}

				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "未在系统中找到当前工号医生的信息");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP不能为空");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "数据传入不能为空");
			renderJson(map);
			return;
		}
	}

	// 医生退出接口
	public void login_out() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		if (!StringKit.isBlank(ip)) {
			if (isIPAddress(ip)) {
				int r = service.showloginupdateout(ip);
				if (r > 0) {
					map.put("return_code", "success");
					map.put("return_msg", "退出成功");
					renderJson(map);
					return;
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "未在系统中找到当前诊室的IP信息");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP不能为空");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "数据传入不能为空");
			renderJson(map);
			return;
		}
	}

	// 呼叫患者接口
	public void call() {
		Map<String, Object> map = new HashMap<String, Object>();
		String show_visiting = getPara("visiting");
		String source_id = getPara("source_id");
		String show_wait = getPara("wait");
		String login_id = getPara("login_id");
		String show_visiting_nmb = getPara("show_visiting_nmb");
		if (!StringKit.isBlank(source_id)) {
			Doctor d = doctor.findByLogin_id(login_id);
			String doctor_id = d.getDoctor_id().toString();
			int r = service.showcallupdate(doctor_id, source_id, show_visiting, show_wait, show_visiting_nmb);
			if (r > 0) {
				map.put("return_code", "success");
				map.put("return_msg", "呼叫成功");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "未在系统中找到当前诊室队列标识的信息");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "诊室队列标识数据不能为空");
			renderJson(map);
			return;
		}
	}

	// 登录显示
	public void showlogin() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		Record list = service.show_login(ip);
		if (list != null) {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("doctorinfo", list);
		} else {
			Terminal t = terminal.findbyip(ip);
			map.put("return_code", "fail");
			map.put("name", t.getName());
			map.put("displayname", t.getDisplay_name());
			map.put("return_msg", "当前没有医生登录");
		}
		renderJson(map);
		return;
	}

	// 小屏呼叫显示
	public void showcall_door() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		Record list = service.show_call_door(ip);
		if (list != null) {
			String id = list.getStr("id");
			int r = service.showupdaecalled_door(id);
			if (r > 0) {
				map.put("return_code", "success");
				map.put("return_msg", "呼叫成功");
				map.put("list", list);
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "查询成功。但呼叫状态更改失败");
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "没有可呼叫的数据");
		}
		renderJson(map);
		return;
	}

	// 大屏显示队列
	public void showcall_hall_type() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		Terminal t = terminal.findbyip(ip);
		List<Record> list = service.show_call_hall_type(ip);
		if (list.size() > 0) {
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("count", list.size());
			map.put("fztname", t.getDisplay_name());
			map.put("list", list);
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "没有队列数据");
		}
		renderJson(map);
		return;
	}

	// 大屏呼叫队列
	public void showcall_hall() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = shard.getIpAddr(getRequest());
		List<Record> list = service.show_call_hall(ip);
		if (list.size() > 0) {
			String ids = "";
			for (int i = 0; i < list.size(); i++) {
				ids += list.get(i).getStr("id") + ",";
			}
			ids = ids.substring(0, ids.length() - 1);
			int r = service.showupdaecalled_hall(ids);
			if (r > 0) {
				map.put("return_code", "success");
				map.put("return_msg", "呼叫成功");
				map.put("count", list.size());
				map.put("list", list);
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "查询成功。但呼叫状态更改失败");
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "没有可呼叫的数据");
		}
		renderJson(map);
		return;
	}

	// 队列接口
	public void queue_type() {
		Map<String, Object> map = new HashMap<String, Object>();
		String source_id = getPara("source_id");
		String name = getPara("name");
		String displayname = getPara("displayname");
		if (!StringKit.isBlank(source_id) && !StringKit.isBlank(name) && !StringKit.isBlank(displayname)) {
			int r = service.showupdatequeuetype(source_id, name, displayname);
			if (r > 0) {
				map.put("return_code", "success");
				map.put("return_msg", "更新成功");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "更新失败");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "诊室队列标识数据不能为空");
			renderJson(map);
			return;
		}
	}

	// 诊室叫号电脑接口
	public void pager() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		String name = getPara("name");
		String displayname = getPara("displayname");
		if (!StringKit.isBlank(ip) && !StringKit.isBlank(name) && !StringKit.isBlank(displayname)) {
			if (isIPAddress(ip)) {
				int r = service.showupdatepager(ip, name, displayname);
				if (r > 0) {
					map.put("return_code", "success");
					map.put("return_msg", "更新成功");
					renderJson(map);
					return;
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "更新失败");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP地址格式不正确");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "IP不能为空");
			renderJson(map);
			return;
		}
	}

	// 屏幕地址接口
	public void terminal() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		String name = getPara("name");
		String displayname = getPara("displayname");
		if (!StringKit.isBlank(ip) && !StringKit.isBlank(name) && !StringKit.isBlank(displayname)) {
			if (isIPAddress(ip)) {
				int r = service.showupdateterminal(ip, name, displayname);
				if (r > 0) {
					map.put("return_code", "success");
					map.put("return_msg", "更新成功");
					renderJson(map);
					return;
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "更新失败");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP地址格式不正确");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "IP不能为空");
			renderJson(map);
			return;
		}
	}

	// 诊室叫号电脑与队列标识关系接口
	public void pager2queue_type() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		String source_id = getPara("source_id");
		if (!StringKit.isBlank(ip) && !StringKit.isBlank(source_id)) {
			if (isIPAddress(ip)) {
				QueueType qt = service.findBySource_id(source_id);
				if (qt != null) {
					Pager p = pager.findByIP(ip);
					if (p != null) {
						int r = service.showupdatepager2queue_type(p.getId().toString(),
								qt.getQueue_type_id().toString());
						if (r > 0) {
							map.put("return_code", "success");
							map.put("return_msg", "更新成功");
							renderJson(map);
							return;
						} else if (r == 0) {
							map.put("return_code", "fail");
							map.put("return_msg", "更新失败");
							renderJson(map);
							return;
						} else {
							map.put("return_code", "fail");
							map.put("return_msg", "已存在此关系");
							renderJson(map);
							return;
						}
					} else {
						map.put("return_code", "fail");
						map.put("return_msg", "未找到对应医生电脑IP地址，请先添加");
						renderJson(map);
						return;
					}
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "未找到对应诊室队列，请先添加");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP地址格式不正确");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "IP不能为空");
			renderJson(map);
			return;
		}
	}

	// 诊室叫号电脑与屏幕地址关系接口
	public void pager2terminal() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		String t_ip = getPara("t_ip");
		if (!StringKit.isBlank(ip) && !StringKit.isBlank(t_ip)) {
			if (isIPAddress(ip)) {
				Terminal ter = terminal.findbyip(t_ip);
				if (ter != null) {
					Pager p = pager.findByIP(ip);
					if (p != null) {
						int r = service.showupdatepager2terminal(p.getId().toString(), ter.getId().toString());
						if (r > 0) {
							map.put("return_code", "success");
							map.put("return_msg", "更新成功");
							renderJson(map);
							return;
						} else if (r == 0) {
							map.put("return_code", "fail");
							map.put("return_msg", "更新失败");
							renderJson(map);
							return;
						} else {
							map.put("return_code", "fail");
							map.put("return_msg", "已存在此关系");
							renderJson(map);
							return;
						}
					} else {
						map.put("return_code", "fail");
						map.put("return_msg", "未找到对应医生电脑IP地址，请先添加");
						renderJson(map);
						return;
					}
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "未找到对应诊室队列，请先添加");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP地址格式不正确");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "IP不能为空");
			renderJson(map);
			return;
		}
	}

	// 删除诊室叫号电脑与队列标识关系接口
	public void pager2queue_type_del() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		String source_id = getPara("source_id");
		if (!StringKit.isBlank(ip) && !StringKit.isBlank(source_id)) {
			if (isIPAddress(ip)) {
				QueueType qt = service.findBySource_id(source_id);
				if (qt != null) {
					Pager p = pager.findByIP(ip);
					if (p != null) {
						int r = service.showupdatepager2queue_type_del(p.getId().toString(),
								qt.getQueue_type_id().toString());
						if (r > 0) {
							map.put("return_code", "success");
							map.put("return_msg", "删除成功");
							renderJson(map);
							return;
						} else {
							map.put("return_code", "fail");
							map.put("return_msg", "删除失败");
							renderJson(map);
							return;
						}
					} else {
						map.put("return_code", "fail");
						map.put("return_msg", "未找到对应医生电脑IP地址，请先添加");
						renderJson(map);
						return;
					}
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "未找到对应诊室队列，请先添加");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP地址格式不正确");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "IP不能为空");
			renderJson(map);
			return;
		}
	}

	// 删除诊室叫号电脑与屏幕地址关系接口
	public void pager2terminal_del() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = getPara("ip");
		String t_ip = getPara("t_ip");
		if (!StringKit.isBlank(ip) && !StringKit.isBlank(t_ip)) {
			if (isIPAddress(ip)) {
				Terminal ter = terminal.findbyip(t_ip);
				if (ter != null) {
					Pager p = pager.findByIP(ip);
					if (p != null) {
						int r = service.showupdatepager2terminal_del(p.getId().toString(), ter.getId().toString());
						if (r > 0) {
							map.put("return_code", "success");
							map.put("return_msg", "删除成功");
							renderJson(map);
							return;
						} else {
							map.put("return_code", "fail");
							map.put("return_msg", "删除失败");
							renderJson(map);
							return;
						}
					} else {
						map.put("return_code", "fail");
						map.put("return_msg", "未找到对应医生电脑IP地址，请先添加");
						renderJson(map);
						return;
					}
				} else {
					map.put("return_code", "fail");
					map.put("return_msg", "未找到对应诊室队列，请先添加");
					renderJson(map);
					return;
				}
			} else {
				map.put("return_code", "fail");
				map.put("return_msg", "IP地址格式不正确");
				renderJson(map);
				return;
			}
		} else {
			map.put("return_code", "fail");
			map.put("return_msg", "IP不能为空");
			renderJson(map);
			return;
		}
	}

	public void pager2queue_type_list() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> r = service.showupdatepager2queue_type_list();
		map.put("return_code", "success");
		map.put("return_msg", "查询成功");
		map.put("count", r.size());
		map.put("list", r);
		renderJson(map);
		return;
	}

	public void pager2terminal_list() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record> r = service.showupdatepager2terminal_list();
		map.put("return_code", "success");
		map.put("return_msg", "查询成功");
		map.put("count", r.size());
		map.put("list", r);
		renderJson(map);
		return;
	}

	// 验证IP地址
	public boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern.compile(
				"\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}

	// 数据库直接访问
	public void GetDataOrcal() {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Driver driver = new OracleDriver();
			DriverManager.deregisterDriver(driver);

			Properties pro = new Properties();
			pro.put("user", "szhs");
			pro.put("password", "szhs");
			connect = driver.connect("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)\r\n" + 
					"(HOST=10.89.1.6)(PORT=1521)))\r\n" + 
					"(CONNECT_DATA=(SERVICE_NAME=orcl)))", pro);

			String source_code = "%"+getPara("source_code")+"%";
			PreparedStatement preState = connect
					.prepareStatement("select * from view_jyk_patient_data where source_code1 like ? or source_code2 like ?");

			// 第四步：执行sql语句
			// 第一种方式：
			// resultSet = statement.executeQuery("select * from tb1_dept");

			// 第二种方式：
			preState.setString(1, source_code);// 1是指sql语句中第一个？, 2是指第一个？的values值
			preState.setString(2, source_code);
			resultSet = preState.executeQuery(); // 执行查询语句
			// 查询任何语句，如果有结果集，返回true，没有的话返回false,注意如果是插入一条数据的话，虽然是没有结果集，返回false，但是却能成功的插入一条数据
			// boolean execute = preState.execute();
			// System.out.println(execute);
			 ResultSetMetaData md = resultSet.getMetaData(); //获得结果集结构信息,元数据  
		    int columnCount = md.getColumnCount(); 
		    List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
			while (resultSet.next())
            {
				Map<String,Object> rowData = new HashMap<String,Object>();  
	            for (int i = 1; i <= columnCount; i++) {  
	                rowData.put(md.getColumnName(i), resultSet.getObject(i));  
	            }  
	            list.add(rowData);  
            }

			// 第五步：处理结果集
			map.put("return_code", "success");
			map.put("return_msg", "查询成功");
			map.put("count", list.size());
			map.put("list", list);
			//String json="{\"return_code\": \"success\",\"return_msg\":\"查询成功\",\"count\":1,\"list\":[{\"PATIENT_NAME\":\"123\",\"SOURCE_CODE1\":\"123456\",\"SOURCE_CODE2\":\"123456\",\"PROJECT_NAME\":\"test\",\"TYPE\":\"1\"}]}";
			renderJson(map);
		} catch (Exception e) {
			map.put("return_code", "fail");
			map.put("return_msg", e.getMessage());
			renderJson(map);
		} finally {
			// 第六步：关闭资源
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				map.put("return_code", "fail");
				map.put("return_msg", e.getMessage());
				renderJson(map);
			}
		}
	}
}
