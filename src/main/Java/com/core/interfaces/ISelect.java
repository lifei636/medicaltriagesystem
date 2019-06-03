package com.core.interfaces;

/**
 * select aop
 */
public interface ISelect {

	IQuery userIntercept();

	IQuery deptIntercept();

	IQuery dictIntercept();

	IQuery roleIntercept();

	IQuery provinceIntercept();

	IQuery CityIntercept();

	IQuery AreaIntercept();

}
