package com.triage.webservice;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;


@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING) 
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface zlplatform {
    /**
     * 执行测试的WebService方法(有参)
     * 
     * @param name
     */
    @WebMethod
    
    String HIPMessageServer(@WebParam(name="action") String action,@WebParam(name="message") String message);
}