package com.core.constant;

public interface ConstCurd {
	/**
	 * 定义首页转向key
	 */
	final static String KEY_INDEX = "/index";

	/**
	 * 定义主页转向key
	 */
	final static String KEY_MAIN = "/";

	/**
	 * 定义新增转向key
	 */
	final static String KEY_ADD = "/add";

	/**
	 * 定义修改转向key
	 */
	final static String KEY_EDIT = "/edit";

	/**
	 * 定义查看转向key
	 */
	final static String KEY_VIEW = "/view";
	/**
	 * 定义首页列表key
	 */
	final static String KEY_LIST = "/list";

	/**
	 * 定义新增操作key
	 */
	final static String KEY_SAVE = "/save";

	/**
	 * 定义修改操作key
	 */
	final static String KEY_UPDATE = "/update";

	/**
	 * 定义删除操作key
	 */
	final static String KEY_REMOVE = "/remove";

	/**
	 * 定义删除操作key
	 */
	final static String KEY_DEL = "/del";

	/**
	 * 定义还原操作key
	 */
	final static String KEY_RESTORE = "/restore";
	final static String QUERY_SUCCESS_MSG = "查询成功";
	final static String QUERY_FAIL_MSG = "查询失败";

	final static String SAVE_SUCCESS_MSG = "新增成功！";
	final static String SAVE_FAIL_MSG = "新增失败！";

	final static String SETTING_SUCCESS_MSG = "设置成功";
	final static String SETTING_ERROR_MSG = "设置失败";

	final static String UPDATE_SUCCESS_MSG = "修改成功！";
	final static String UPDATE_FAIL_MSG = "修改失败！";
	final static String DEL_SUCCESS_MSG = "删除成功！";
	final static String DEL_FAIL_MSG = "删除失败！";
	final static String RESTORE_SUCCESS_MSG = "还原成功！";
	final static String RESTORE_FAIL_MSG = "还原失败！";
	final static String TRIAGE_NAME_MSG = "当前分诊台已经存在";
	final static String TRIAGE_IP_MSG = "当前IP已经存在";
	final static String TRIAGE_IP_ERROR_MSG = "当前IP不合法";
	final static String IP_NULL_MSG = "IP为空或获取失败";
	final static String IP_MSG = "当前IP无法ping通，请确认网络正常";
	final static String IP_ERROR_MSG = "获取ip失败";
	final static String QUEUE_TYPE_NAME_MSG = "队列名称已经存在";
	final static String DOCTOR_LOGIN_ID_MSG = "当前医生登陆工号已经存在";
	final static String PAGER_NAME_MSG = "当前叫号器名称已存在";
	final static String SYNC_SUCCESS_MSG = "同步成功！";

	// 分诊台
	final static String LOGIN_ID_NULL_MSG = "医生工号不能为空";
	final static String PASSWORD_NULL_MSG = "密码不能为空";
	final static String PASSWORD_ERROR_MSG = "密码错误,请重新输入";
	final static String TRIAGE_IP_ERR_MSG = "当前电脑没有配置为分诊台";
	final static String LOGIN_ERROR_MSG = "登陆失败,请确认登陆工号或密码是否正确";
	final static String LOGIN_SUCCESS_MSG = "登陆成功";
	final static String TRIAGE_NAME_NULL_MSG = "";

	final static String QUEUE_TYPE_NULL_MSG = "队列信息为空";
	final static String NOT_NULL_MSG = "不能为空值";
}
