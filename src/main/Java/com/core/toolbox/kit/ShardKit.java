package com.core.toolbox.kit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class ShardKit {
	// 判断身份证号码是否正确 18位
	public boolean isValidIdCard(String cardid) {

		String ls_id = cardid;
		if (ls_id.length() != 18) {
			return false;
		}
		char[] l_id = ls_id.toCharArray();
		int l_jyw = 0;
		int[] wi = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		char[] ai = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		for (int i = 0; i < 17; i++) {
			if (l_id[i] < '0' || l_id[i] > '9') {
				return false;
			}
			l_jyw += (l_id[i] - '0') * wi[i];
		}
		l_jyw = l_jyw % 11;
		if (ai[l_jyw] != l_id[17]) {
			return false;
		}
		return true;
	}

	// 判断手机号码是否正确 11位
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 判断输入的生日格式(yyyy年mm月dd日)
	public boolean isValidDate(String str) {
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			Date date = formatter.parse(str);
			boolean b = str.equals(formatter.format(date));
			if (b) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// 判断输入的生日格式(yyyy-mm-dd)
	public boolean isValidDates(String str) {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = formatter.parse(str);
			boolean b = str.equals((formatter.format(d)));
			if (b) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	// 金额验证
	public boolean isNumber(String str) {

		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	// 判断ip是否能够ping通
	public boolean isIpReachable(String str) {
		final int timeOut = 3000; // 超时应该在3钞以上
		boolean status = false;
		if (str != null) {
			try {
				status = InetAddress.getByName(str).isReachable(timeOut);
			} catch (UnknownHostException e) {

			} catch (IOException e) {

			}
		}
		return status;

	}

	// 判断是否为合法ip
	public boolean isIP(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
				+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

		Pattern pat = Pattern.compile(rexp);

		Matcher mat = pat.matcher(addr);

		boolean ipAddress = mat.find();

		return ipAddress;
	}

	// 获取客户端ip
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
