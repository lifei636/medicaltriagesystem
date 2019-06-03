package com.shine.controller;

import java.util.Date;
import java.util.List;

import com.core.jfinal.ext.kit.JsonKit;
import com.shine.model.Item;
import com.shine.model.Source;
import com.shine.service.ItemService;
import com.shine.service.SourceService;
import com.shine.service.impl.ItemServiceImpl;
import com.shine.service.impl.SourceServiceImpl;
import com.system.controller.base.UrlPermissController;

/**
 * Generated by Blade. 2017-08-17 14:14:07
 */
public class ItemController extends UrlPermissController {
	private static String CODE = "item";
	private static String PERFIX = "item";
	private static String LIST_SOURCE = "Item.list";
	private static String BASE_PATH = "/platform/item/";

	ItemService service = new ItemServiceImpl();
	SourceService source = new SourceServiceImpl();

	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "item.html");
	}

	public void add() {
		List<Source> list = source.listDesc();
		setAttr("listDesc", list);
		setAttr("code", CODE);
		render(BASE_PATH + "item_add.html");
	}

	public void edit() {
		List<Source> list = source.listDesc();
		setAttr("listDesc", list);
		String id = getPara(0);
		Item item = service.findById(id);
		setAttr("model", JsonKit.toJson(item));
		setAttr("id", id);
		setAttr("code", CODE);
		render(BASE_PATH + "item_edit.html");
	}

	public void view() {
		String id = getPara(0);
		Item item = service.findById(id);
		setAttr("model", JsonKit.toJson(item));
		setAttr("id", id);
		setAttr("code", CODE);
		render(BASE_PATH + "item_view.html");
	}

	public void list() {
		Object grid = paginate(LIST_SOURCE);
		renderJson(grid);
	}

	public void save() {
		Item item = mapping(PERFIX, Item.class);
		switch (item.getType()) {
		case 1:
			item.setName("医生信息");
			break;
		case 2:
			item.setName("队列信息");
			break;
		case 3:
			item.setName("医生与队列关系");
			break;
		case 4:
			item.setName("患者挂号信息");
			break;

		default:
			renderJson(error("系统错误"));
			break;
		}
		item.setLast_time(new Date());
		boolean temp = service.save(item);
		if (temp) {
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

	public void update() {
		Item item = mapping(PERFIX, Item.class);
		switch (item.getType()) {
		case 1:
			item.setName("医生信息");
			break;
		case 2:
			item.setName("队列信息");
			break;
		case 3:
			item.setName("医生与队列关系");
			break;
		case 4:
			item.setName("患者挂号信息");
			break;

		default:
			renderJson(error("系统错误"));
			break;
		}
		item.setLast_time(new Date());
		boolean temp = service.update(item);
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