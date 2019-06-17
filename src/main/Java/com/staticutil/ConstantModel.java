package com.staticutil;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * @author : li.fei
 * @since : 2019/6/11
 */
public class ConstantModel {
    static {
        PropKit.use("config.properties");
    }

    /**
     * 功能描述: 分诊台名称-超声医学科
     **/
    public static final String TRIAGENAME = "超声医学科";

    /**
     * 功能描述: 石棉登陆代码
     **/
    public static final String SM = "sm";

    /**
     * 功能描述: 德阳人民医院登陆代码
     **/
    public static final String DYRY = "dy_ry";

    /**
     * 功能描述: 登陆代码
     **/
    public final static String LOGIN = PropKit.get("login");

    /**
     * 功能描述: 门口显示屏，等候的人数配置
     **/
    public final static byte DOOR_SHOWNUMBER = Byte.parseByte(PropKit.get("door_showNumber"));
}
