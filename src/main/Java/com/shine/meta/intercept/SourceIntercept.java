package com.shine.meta.intercept;

import java.util.List;
import java.util.Map;

import com.core.aop.AopContext;
import com.core.toolbox.Func;
import com.core.toolbox.support.BladePage;
import com.system.meta.intercept.ParameterIntercept;

public class SourceIntercept extends ParameterIntercept {

	/**
	 * 查询后附加字典项
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();

		for (Map<String, Object> map : list) {
			map.put("dbType", Func.getDictName(110, map.get("dbType")));
		}
	}

}
