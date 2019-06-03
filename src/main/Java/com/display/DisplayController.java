package com.display;

import com.core.base.BaseController;
import com.core.jfinal.ext.autoroute.ControllerBind;
import com.jfinal.kit.StrKit;

@ControllerBind(controllerKey = "/screen")
public class DisplayController extends BaseController {

	public void index() {
		String filename = getPara("page");
		if (StrKit.isBlank(filename)) {
			render("/web/door/index.html");
		} else {
			render("/web/door/" + filename + ".html");
		}

	}

	public void hall() {
		String filename = getPara("page");
		if (StrKit.isBlank(filename)) {
			render("/web/hall/index.html");
		} else {
			render("/web/hall/" + filename + ".html");
		}
	}
	public void caller_ks() {
		String filename = getPara("page");
		if (StrKit.isBlank(filename)) {
			render("/web/caller/index.html");
		} else {
			render("/web/caller/" + filename + ".html");
		}
	}
}
