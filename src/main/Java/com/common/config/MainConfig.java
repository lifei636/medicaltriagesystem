package com.common.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.dreamlu.controller.UeditorApiController;

import org.beetl.core.GroupTemplate;
import org.beetl.sql.ext.jfinal.JFinalBeetlSql;
import org.beetl.sql.ext.jfinal.Trans;

import cn.dreampie.quartz.QuartzPlugin;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.baidu.ueditor.UeditorConfigKit;
import com.baidu.ueditor.manager.QiniuFileManager;
import com.core.constant.Const;
import com.core.intercept.DoLogInterceptor;
import com.core.jfinal.ext.autoroute.AutoBindRoutes;
import com.core.jfinal.ext.shiro.ShiroInterceptor;
import com.core.jfinal.ext.shiro.ShiroPlugin;
import com.core.modules.beetl.BeetlExt;
import com.core.modules.beetl.BeetlRenderFactory;
import com.core.modules.beetl.ShiroExt;
import com.core.modules.beetl.tag.DropDownTag;
import com.core.modules.beetl.tag.FootTag;
import com.core.modules.beetl.tag.SelectTag;
import com.core.modules.beetl.tag.SideBarTag;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

public class MainConfig extends JFinalConfig implements Const {
	public static final Map<String, String> map = new HashMap<String, String>();
	public static final List<String> dbconfigs = new ArrayList<String>();// 保存已经启动好的数据源名称
	/**
	 * 供Shiro插件使用。
	 */
	Routes routes;

	@Override
	public void configConstant(Constants me) {
		PropKit.use("config.properties");
		loadPropertyFile("config.properties");
		map.put("dbType", getProperty("master.dbType", "mysql"));
		map.put("sqldriver", getProperty("sqlserver.driver"));
		map.put("sqlurl", getProperty("sqlserver.url"));
		map.put("sqlusername", getProperty("sqlserver.username"));
		map.put("sqlpassword", getProperty("sqlserver.password"));
		map.put("orcldriver", getProperty("oracle.driver"));
		map.put("orclurl", getProperty("oracle.url"));
		map.put("orclusername", getProperty("oracle.username"));
		map.put("orclpassword", getProperty("oracle.password"));
		map.put("db2driver", getProperty("db2.driver"));
		map.put("db2url", getProperty("db2.url"));
		map.put("db2username", getProperty("db2.username"));
		map.put("db2password", getProperty("db2.password"));
		map.put("bak_database",getProperty("gobal.bak_database"));
		me.setDevMode(getPropertyToBoolean("config.devMode", false));
		me.setRenderFactory(new BeetlRenderFactory());
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate;
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		sharedVars.put("startTime", new Date());
		sharedVars.put("basePath", getProperty("config.basePath", "/"));
		groupTemplate.setSharedVars(sharedVars);
		groupTemplate.registerTag("select", SelectTag.class);
		groupTemplate.registerTag("sidebar", SideBarTag.class);
		groupTemplate.registerTag("dropdown", DropDownTag.class);
		groupTemplate.registerTag("foot", FootTag.class);
		groupTemplate.registerFunctionPackage("func", new BeetlExt());
		groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
		initErrorView(me);
	}

	private void initErrorView(Constants me) {
		me.setError401View(error401Path);
		me.setError403View(error403Path);
		me.setError404View(error404Path);
		me.setError500View(error500Path);
	}

	@Override
	public void configHandler(Handlers me) {
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
		//UrlSkipHandler ulHandler=new UrlSkipHandler(skipedUrlRegx, isCaseSensitive)
		me.add(dvh);
		UrlSkipHandler urlSkipHandler=new UrlSkipHandler("/service/.*",true); 
		me.add(urlSkipHandler);
		
		//me.add(new UrlSkipHandler("*/service/*",true));  
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new Trans());
		me.add(new DoLogInterceptor());
		me.add(new ShiroInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {

		me.add(new EhCachePlugin());

		DruidPlugin druidPlugin = new DruidPlugin(getProperty("master.url"), getProperty("master.username"),
				getProperty("master.password").trim(), getProperty("master.driver"))
						.set(getPropertyToInt("druid.initialSize"), getPropertyToInt("druid.minIdle"),
								getPropertyToInt("druid.maxActive"))
						.setMaxWait(getPropertyToLong("druid.maxWait"));
		druidPlugin.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType(getProperty("master.dbType"));
		druidPlugin.addFilter(wall);
		me.add(druidPlugin);
		druidPlugin.start();
		JFinalBeetlSql.init(druidPlugin.getDataSource(), null);
		me.add(new ShiroPlugin(this.routes));

	}

	@Override
	public void configRoute(Routes me) {
		this.routes = me;
		me.add(new AutoBindRoutes());
		me.add("/ueditor/api", UeditorApiController.class);
	}

	@Override
	public void configEngine(Engine engine) {

	}

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 82, "/");
	}

	@Override
	public void afterJFinalStart() {
		ServletContext sc = JFinal.me().getServletContext();
		map.put("realPath", sc.getRealPath("/").replaceFirst("/", ""));
		map.put("contextPath", sc.getContextPath());
		for (Object name : prop.getProperties().keySet()) {
			map.put(name.toString(), prop.getProperties().get(name).toString());
		}
		if (map.get("config.devMode").toLowerCase().equals("false")) {
			String ak = getProperty("qiniu.ak");
			String sk = getProperty("qiniu.sk");
			String bucket = getProperty("qiniu.bucket");
			UeditorConfigKit.setFileManager(new QiniuFileManager(ak, sk, bucket));
		}

		QuartzPlugin quartzPlugin = new QuartzPlugin();
		quartzPlugin.setJobs("deleteFromPatientQueue-quartz.properties");
		quartzPlugin.start();

	}

}
