package com.shine.controller;

import com.core.jfinal.ext.kit.JsonKit;
import com.shine.meta.intercept.SourceIntercept;
import com.shine.model.Source;
import com.shine.service.SourceService;
import com.shine.service.impl.SourceServiceImpl;
import com.system.controller.base.UrlPermissController;

/**
 * Generated by Blade. 2017-08-17 14:14:06
 */
public class SourceController extends UrlPermissController {
	private static String CODE = "source";
	private static String PERFIX = "souce";
	private static String LIST_SOURCE = "Source.list";
	private static String BASE_PATH = "/platform/source/";

	SourceService service = new SourceServiceImpl();

	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "source.html");
	}

	public void add() {
		setAttr("code", CODE);
		render(BASE_PATH + "source_add.html");
	}

	public void edit() {
		String id = getPara(0);
		Source source = service.findById(id);
		setAttr("model", JsonKit.toJson(source));
		setAttr("id", id);
		setAttr("code", CODE);
		render(BASE_PATH + "source_edit.html");
	}

	public void view() {
		String id = getPara(0);
		Source source = service.findById(id);
		setAttr("model", JsonKit.toJson(source));
		setAttr("id", id);
		setAttr("code", CODE);
		render(BASE_PATH + "source_view.html");
	}

	public void list() {
		Object grid = paginate(LIST_SOURCE, new SourceIntercept());
		renderJson(grid);
	}

	public void save() {
		Source source = mapping(PERFIX, Source.class);
		boolean temp = service.save(source);
		if (temp) {
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

	public void update() {
		Source source = mapping(PERFIX, Source.class);

		boolean temp = service.update(source);
		if (temp) {
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}

	public void remove() {
		String ids = getPara("ids");
		int cnt = service.deleteByIds(ids);
		if (cnt > 0) {
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}
}
