package com.triage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.beetl.sql.core.kit.StringKit;

import com.common.vo.LoginLog;
import com.core.base.BaseController;
import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.core.jfinal.ext.kit.JsonKit;
import com.core.jfinal.ext.shiro.ShiroKit;
import com.core.toolbox.Func;
import com.core.toolbox.Record;
import com.core.toolbox.kit.ShardKit;
import com.core.toolbox.log.LogManager;
import com.shine.model.Doctor;
import com.shine.model.Triage;
import com.shine.service.DoctorService;
import com.shine.service.TriageService;
import com.shine.service.impl.DoctorServiceImpl;
import com.shine.service.impl.TriageServiceImpl;

import oracle.jdbc.driver.OracleDriver;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.LogKit;
import com.alibaba.druid.proxy.jdbc.JdbcParameter.TYPE;
import com.common.config.MainConfig;

@Clear
@ControllerBind(controllerKey = "/client")
public class ClientController extends BaseController {

	TriageService triageservice = new TriageServiceImpl();
	DoctorService doctorservice = new DoctorServiceImpl();
	ShardKit shardkit = new ShardKit();

	@Before(GET.class)
	public void login() {
		render(clientLoginRealPath);
	}

	public void index() {
		render(clientIndexRealPath);
	}

	@Before(GET.class)
	public void doLogin() {

		String account = getPara("account");
		if (StringKit.isBlank(account)) {
			renderJson(error(LOGIN_ID_NULL_MSG));
			return;
		}
		String password = getPara("password");
		if (StringKit.isBlank(password)) {
			renderJson(error(PASSWORD_NULL_MSG));
			return;
		}
		String ip = shardkit.getIpAddr(getRequest());
		Triage triage = triageservice.queryTriageIp(ip);
		if (null == triage) {
			renderJson(error(TRIAGE_IP_ERR_MSG));
			return;
		}
		Doctor doctor = doctorservice.findByLogin_id(account);
		Triage tlogin = triageservice.queryLogin(ip, password);
		if (null == doctor || null == tlogin) {
			renderJson(error(LOGIN_ERROR_MSG));
			return;
		} else {
			renderJson(success(LOGIN_SUCCESS_MSG));
			String sql = "insert into tfw_login_log (LOGNAME,USERID,CLASSNAME,METHOD,CREATETIME,SUCCEED,MESSAGE) "
					+ "values ('分诊台登陆','" + doctor.getLogin_id() + "','com.shine.clientController.class',"
					+ "'client/login','" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
					+ "','成功','登陆成功')";
			System.out.println(sql);
			Blade.dao().executeUpdate(sql, null);
			setSessionAttr("login_id", doctor.getLogin_id());
			setSessionAttr("tlogin_pwd", tlogin.getTriage_pwd());
		}
		return;
	}

	public void getUserSession() {
		if (null == getSessionAttr("login_id") || null == getSessionAttr("tlogin_pwd")) {
			renderJson(error(LOGIN_ERROR_MSG));
			render(clientLoginRealPath);
			return;
		} else {
			renderJson(success(LOGIN_SUCCESS_MSG));
			render(clientIndexRealPath);
		}
	}

	public void captcha() {
		renderCaptcha();
	}

	public void logout() {
		doLog(ShiroKit.getSession(), "登出");
		Subject currentUser = ShiroKit.getSubject();
		currentUser.logout();
		redirect("client/login");
	}

	public void unauth() {
		if (ShiroKit.notAuthenticated()) {
			redirect("client/login");
		}
		render(noPermissionPath);
	}

	public void doLog(Session session, String type) {
		if (!LogManager.isDoLog()) {
			return;
		}
		try {
			LoginLog log = new LoginLog();
			String msg = Func.format("[sessionID]: {} [sessionHost]: {} [sessionHost]: {}", session.getId(),
					session.getHost(), session.getTimeout());
			log.setLogname(type);
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed("1");
			log.setUserid(Func.format(ShiroKit.getUser().getId()));
			Blade.create(LoginLog.class).save(log);
		} catch (Exception ex) {
			LogKit.logNothing(ex);
		}
	}
	//使用sql语句访问系统数据
	public void commSelect() {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = getPara("sql");
		String callback=getPara("callback");
		String ip = shardkit.getIpAddr(getRequest());
		
		if (StringKit.isBlank(sql)) {
			map.put("return_code", "fail");
			map.put("return_msg", "SQL语句不能为空");
			renderJson(map);
			return;
		}
		else
		{
			List<Record> list =  Db.init().selectList(sql,Record.create().set("ip", ip));
			if (null == list) {
				map.put("return_code", "fail");
				map.put("return_msg", "没有查询到相关数据");
				renderJson(map);
				return;
			} else {
				map.put("return_code", "success");
				map.put("return_msg", "查询数据成功");
				map.put("list", list);
				map.put("count", list.size());
				if(StringKit.isBlank(callback))
					renderJson(map);
				else
					{
					renderHtml(callback+"("+JsonKit.toJson(map)+")");
					//renderJson(callback);
					}
			}

		}
	}

	public void sqlCommSelect() throws SQLException  {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = getPara("sql");
		String type=getPara("type");

		String ip = shardkit.getIpAddr(getRequest());
		if(StringKit.isBlank(type))
		{
			type="1";
		}
		if (StringKit.isBlank(sql)) {
			map.put("return_code", "fail");
			map.put("return_msg", "SQL语句不能为空");
			renderJson(map);
			return;
		}
		else
		{

			ResultSet rs =null;
			if(type.equals("2"))
				rs=SqlHelper.getResultSet(sql,ip);
			else
				rs=SqlHelper.getResultSet(sql);
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
				map.put("return_code", "success");
				map.put("return_msg", "查询数据成功");
				map.put("list", list);
				map.put("count", list.size());
				renderJson(map);
			}
			else
			{
				map.put("return_code", "fail");
				map.put("return_msg", "查询失败");
				renderJson(map);
			}
		}
	}

	//Oracle数据库直接访问
	public void orclCommSelect() {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = getPara("sql");
		String type=getPara("type");
		String ip = shardkit.getIpAddr(getRequest());
		if(StringKit.isBlank(type))
		{
			type="1";
		}
		if (StringKit.isBlank(sql)) {
			map.put("return_code", "fail");
			map.put("return_msg", "SQL语句不能为空");
			renderJson(map);
			return;
		}
		else
		{
			try {
				Driver driver = new OracleDriver();
				DriverManager.deregisterDriver(driver);

				Properties pro = new Properties();
				pro.put("user", MainConfig.map.get("orclusername"));
				pro.put("password",  MainConfig.map.get("orclpassword"));
				connect = driver.connect(MainConfig.map.get("orclurl"), pro);
				// 第四步：执行sql语句
				if(type.equals("1"))
				{
					statement=connect.createStatement();
					// 第一种方式：
					resultSet = statement.executeQuery(sql);
				}
				else
				{
					// 第二种方式：
					PreparedStatement preState = connect.prepareStatement(sql);
					preState.setString(1, ip);// 1是指sql语句中第一个？, 2是指第一个？的values值
					//preState.setString(2, source_code);
					resultSet = preState.executeQuery(); // 执行查询语句
				}
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
	//DB2数据库直接访问
	public void db2CommSelect() {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = getPara("sql");
		String type=getPara("type");
		String ip = shardkit.getIpAddr(getRequest());
		if(StringKit.isBlank(type))
		{
			type="1";
		}
		if (StringKit.isBlank(sql)) {
			map.put("return_code", "fail");
			map.put("return_msg", "SQL语句不能为空");
			renderJson(map);
			return;
		}
		else
		{
			String jdbcClassName=MainConfig.map.get("db2driver");
			String url=MainConfig.map.get("db2url");
			String user=MainConfig.map.get("db2username");
			String password=MainConfig.map.get("db2password");

			try {
				//Load class into memory
				Class.forName(jdbcClassName);
				//Establish connection
				connect = DriverManager.getConnection(url, user, password);
				if(type.equals("1"))
				{
					statement =connect.createStatement();
					statement.execute(sql);
					resultSet=statement.getResultSet();
				}
				else
				{
					PreparedStatement preState = connect.prepareStatement(sql);
					preState.setString(1, ip);// 1是指sql语句中第一个？, 2是指第一个？的values值
					//preState.setString(2, source_code);
					resultSet = preState.executeQuery(); // 执行查询语句
				}
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
				map.put("return_code", "success");
				map.put("return_msg", "查询成功");
				map.put("count", list.size());
				map.put("list", list);
				renderJson(map);
			} catch (ClassNotFoundException e) {
				map.put("return_code", "fail");
				map.put("return_msg", e.getMessage());
				renderJson(map);
				return;
			} catch (SQLException e) {
				map.put("return_code", "fail");
				map.put("return_msg", e.getMessage());
				renderJson(map);
			}finally{
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
	public void Get(){
		//Map<String, Object> map = new HashMap<String, Object>();
		String url = getPara("url");
		String rsString=HttpClient.doGet(url);
		//map.put("data",rsString);
		renderJson(rsString);
	}
	public void Post() {	
		//Map<String, Object> map = new HashMap<String, Object>();
		String url = getPara("url");
		String param =getPara("param");
		String contentType =getPara("contentType");
		String Authorization= getPara("Authorization");
		String rsString=HttpClient.doPost(url, param, contentType, Authorization);
		//map.put("data",rsString);
		renderJson(rsString);
	}
}
