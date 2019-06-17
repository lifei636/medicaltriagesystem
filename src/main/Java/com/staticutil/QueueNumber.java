package com.staticutil;

/**
 * 队列标识号（含优先等级）
 * @since : 2019/6/15
 */
public class QueueNumber {

    /**
     * 功能描述: 锁定的队列
     * @date : 2019/6/15
     **/
    public static final int WAITLOCK = 1;

    /**
     * 功能描述: 特殊队列
     * @date : 2019/6/15
     **/
    public static final int SPECIAL = 10 ;


    /**
     * 功能描述: 优先队列
     * @date : 2019/6/15
     **/
    public static final int WAIT = 20 ;

    /**
     * 功能描述: 过号队列：签到重排
     * @date : 2019/6/15
     **/
    public static final int PASS = 30;

    /**
     * 功能描述: 回诊号（复）（回诊转到其他医生，做选择操作：是同级别）
     * @date : 2019/6/15
     **/
    public static final int RETURN = 40;

    /**
     * 功能描述: 转诊号：一个医生转到另一个医生下面，按初诊队列排
     * @date : 2019/6/15
     **/
    public static final int TRANSFER = 50;

    /**
     * 功能描述: 加号：按号序排（排最后面）
     * @date : 2019/6/15
     **/
    public static final int ADD = 60;

    /**
     * 功能描述: 迟到：分时段，排在签到时间段的最后面
     * @date : 2019/6/15
     **/
    public static final int BELATE = 70;

}
