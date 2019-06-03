package com.shine.controller;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.shine.service.PagerService;
import com.shine.service.impl.PagerServiceImpl;

public class SessionHandler implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		// TODO 自动生成的方法存根
		System.out.println(se.getSession().getId());
		try
		{
		String login_id=se.getSession().getAttribute("login_id").toString();
		 String ip=se.getSession().getAttribute("ip").toString();
		 String s=login_id+ip;
		 System.out.println(s);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// TODO 自动生成的方法存根
		try
		{
		String login_id=se.getSession().getAttribute("login_id").toString();
		 String ip=se.getSession().getAttribute("ip").toString();
		 String s=login_id+ip;
		 System.out.println(s);
		 PagerService pagerService=new PagerServiceImpl();
		 pagerService.logout(ip, login_id);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		 
	}

}
