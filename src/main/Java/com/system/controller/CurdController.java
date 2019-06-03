package com.system.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.aop.AopContext;
import com.core.base.BaseController;
import com.core.base.service.CurdService;
import com.core.base.service.impl.CurdServiceImpl;
import com.core.dao.Blade;
import com.core.dao.Db;
import com.core.interfaces.IMeta;
import com.core.jfinal.ext.kit.JsonKit;
import com.core.meta.MetaIntercept;
import com.core.model.AjaxResult;
import com.core.toolbox.Func;
import com.core.toolbox.Record;
import com.core.toolbox.support.Singleton;
import com.system.controller.base.UrlPermissController;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

public abstract class CurdController<M> extends UrlPermissController {
	CurdService service = new CurdServiceImpl();

	private final BaseController ctrl = this;
	/** 自定义拦截器 **/
	private MetaIntercept intercept = null;
	private Class<M> modelClass;
	private IMeta metaFactory;
	private String controllerKey;
	private String paraPerfix;
	private Map<String, Object> switchMap;
	private Map<String, String> renderMap;
	private Map<String, String> sourceMap;
	private Map<String, Object> reverseMap;

	@SuppressWarnings("unchecked")
	private Class<M> getClazz() {
		Type t = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) t).getActualTypeArguments();
		return (Class<M>) params[0];
	}

	private void init() {
		this.modelClass = getClazz();
		this.metaFactory = Singleton.create(metaFactoryClass());
		this.controllerKey = metaFactory.controllerKey();
		this.paraPerfix = metaFactory.paraPerfix();
		this.switchMap = metaFactory.switchMap();
		this.renderMap = metaFactory.renderMap();
		this.sourceMap = metaFactory.sourceMap();
		this.intercept = Singleton.create(metaFactory.intercept());
		this.reverseMap = reverseMap();
	}

	public CurdController() {
		this.init();
	}

	protected abstract Class<? extends IMeta> metaFactoryClass();

	/**
	 * 前端字段混淆map翻转
	 * 
	 * @return Map<String,String>
	 */
	protected Map<String, Object> reverseMap() {
		if (Func.isEmpty(switchMap)) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		for (String key : switchMap.keySet()) {
			map.put((String) switchMap.get(key), key);
		}
		return map;
	}

	public void index() {
		Map<String, Object> map = new HashMap<>();
		map.put("url", renderMap.get(KEY_INDEX));
		map.put("code", controllerKey);
		setAttr("code", controllerKey);
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, map);
			intercept.renderIndexBefore(ac);
		}
		render(map.get("url").toString());
	}

	public void add() {
		Map<String, Object> map = new HashMap<>();
		map.put("url", renderMap.get(KEY_ADD));
		map.put("code", controllerKey);
		setAttr("code", controllerKey);
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, map);
			intercept.renderAddBefore(ac);
		}

		if ("generate".equals(controllerKey)) {
			// Tables_in_osc_jblade
			if ("generate".equals(controllerKey)) {
				List<Record> tables = Db.init().selectList(PropKit.get("master.tablelist"),
						Record.create().set("table_name", PropKit.get("master.database")));
				setAttr("tables", tables);

			}

		}

		render(map.get("url").toString());
	}

	public void edit() {
		String id = getPara(0);
		Map<String, Object> map = new HashMap<>();
		map.put("url", renderMap.get(KEY_EDIT));
		map.put("code", controllerKey);
		Record maps = Record.create();
		if (StrKit.isBlank(sourceMap.get(KEY_EDIT))) {
			M model = Blade.create(modelClass).findById(id);
			maps.parseBean(model);
		} else {
			map.put("id", id);
			Map<String, Object> model = this.find(sourceMap.get(KEY_EDIT), map);
			maps.parseMap(model);
		}
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, maps);
			intercept.renderEditBefore(ac);
		}
		setAttr("code", controllerKey);
		setAttr("_model", autoReverse(maps));
		setAttr("model", JsonKit.toJson(maps));
		render(map.get("url").toString());
	}

	public void view() {
		String id = getPara(0);
		Map<String, Object> map = new HashMap<>();
		map.put("url", renderMap.get(KEY_VIEW));
		Record maps = Record.create();
		if (StrKit.isBlank(sourceMap.get(KEY_VIEW))) {
			M model = Blade.create(modelClass).findById(id);
			maps.parseBean(model);
		} else {
			map.put("id", id);
			Map<String, Object> model = this.find(sourceMap.get(KEY_VIEW), map);
			maps.parseMap(model);
		}
		if (null != intercept) {
			AopContext ac = new AopContext(ctrl, maps);
			intercept.renderViewBefore(ac);
		}
		setAttr("code", controllerKey);
		setAttr("_model", autoReverse(maps));
		setAttr("model", JsonKit.toJson(maps));
		render(map.get("url").toString());
	}

	public void save() {
		M model = autoMapping();
		boolean temp = service.save(ctrl, model, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl, model);
				AjaxResult result = intercept.saveSucceed(ac);
				renderJson(result);
				return;
			}
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

	public void update() {
		M model = autoMapping();
		boolean temp = service.update(ctrl, model, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl, model);
				AjaxResult result = intercept.updateSucceed(ac);
				renderJson(result);
				return;
			}
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}

	public void remove() {
		String ids = getPara("ids");
		boolean temp = service.deleteByIds(ctrl, ids, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl);
				ac.setId(ids);
				AjaxResult result = intercept.removeSucceed(ac);
				renderJson(result);
				return;
			}
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}

	public void del() {
		String ids = getPara("ids");
		boolean temp = service.delByIds(ctrl, ids, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl);
				ac.setId(ids);
				AjaxResult result = intercept.delSucceed(ac);
				renderJson(result);
				return;
			}
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}

	public void restore() {
		String ids = getPara("ids");
		boolean temp = service.restoreByIds(ctrl, ids, modelClass, intercept);
		if (temp) {
			if (null != intercept) {
				AopContext ac = new AopContext(ctrl);
				ac.setId(ids);
				AjaxResult result = intercept.restoreSucceed(ac);
				renderJson(result);
				return;
			}
			renderJson(success(RESTORE_SUCCESS_MSG));
		} else {
			renderJson(error(RESTORE_FAIL_MSG));
		}
	}

	public void list() {
		Integer page = getParaToInt("page");
		Integer rows = getParaToInt("rows");
		String where = getPara("where");
		String sidx = getPara("sidx");
		String sord = getPara("sord");
		String sort = getPara("sort");
		String order = getPara("order");
		if (StrKit.notBlank(sidx)) {
			sort = sidx + " " + sord + (StrKit.notBlank(sort) ? ("," + sort) : "");
		}
		if (StrKit.isBlank(sourceMap.get(KEY_INDEX))) {
			throw new RuntimeException(modelClass.getName() + "没有配置数据源！");
		}
		Object grid = service.paginate(page, rows, sourceMap.get(KEY_INDEX), where, sort, order, intercept, ctrl);
		renderJson(grid);
	}

	/**
	 * 根据子类的paraPerfix,switchMap实现主表的自动映射
	 * 
	 * @return M
	 */
	protected M autoMapping() {
		if (Func.isAllEmpty(paraPerfix, switchMap)) {
			return mapping(modelClass);
		} else if (Func.isEmpty(paraPerfix) && !Func.isEmpty(switchMap)) {
			return mapping(switchMap, modelClass);
		} else if (Func.isEmpty(switchMap) && !Func.isEmpty(paraPerfix)) {
			return mapping(paraPerfix, modelClass);
		} else {
			return mapping(paraPerfix, switchMap, modelClass);
		}
	}

	/**
	 * 将返回的model根据switchMap实现自动翻转
	 * 
	 * @param model
	 * @return Object
	 */
	protected Map<String, Object> autoReverse(Object model) {
		if (Func.isEmpty(reverseMap)) {
			return reverse(model);
		} else {
			return reverse(reverseMap, model);
		}
	}

	/**
	 * 翻转bean
	 * 
	 * @param switchMap
	 *            翻转map
	 * @param model
	 *            bean
	 * @return Map<String,Object>
	 */
	protected Map<String, Object> reverseBean(Map<String, Object> reverseMap, Object model) {
		if (Func.isEmpty(reverseMap)) {
			return reverse(model);
		} else {
			return reverse(reverseMap, model);
		}
	}

	private Map<String, Object> find(String source, Map<String, Object> map) {
		if (source.indexOf("select") == -1) {
			return findOneById(source, map);
		} else {
			return findOneBySql(source, map);
		}
	}

	private Map<String, Object> findOneBySql(String sql, Map<String, Object> map) {
		Map<String, Object> model = Db.init().selectOne(sql, map);
		return Func.caseInsensitiveMap(model);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findOneById(String sqlId, Map<String, Object> map) {
		Map<String, Object> model = Blade.dao().selectSingle(sqlId, map, Map.class); // Db.init().selectOneBySqlId(sqlId,
																						// map);
		return Func.caseInsensitiveMap(model);
	}

}
