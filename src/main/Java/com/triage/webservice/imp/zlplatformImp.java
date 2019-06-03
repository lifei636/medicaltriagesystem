package com.triage.webservice.imp;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;

import org.beetl.core.statement.VarAssignExpression;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.triage.webservice.zlplatform;
import com.shine.service.SyncDataLogService;
import com.shine.service.impl.SyncDataLogServiceImpl;

import com.shine.service.ThirdDataService;
import com.shine.service.impl.ThirdDataServiceImpl;

import java.util.Calendar;
import java.util.Date;

/**
 * 作为测试的WebService实现类
 * 
 * @author Johness
 * 
 */
@WebService(endpointInterface = "com.triage.webservice.imp.zlplatformImp")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class zlplatformImp implements zlplatform {
	ThirdDataService third = new ThirdDataServiceImpl();
	SyncDataLogService log = new SyncDataLogServiceImpl();

	@Override
	public String HIPMessageServer(@WebParam(name = "action") String action,
			@WebParam(name = "message") String message) {
		String xmlString = "";
		switch (action) {
		case "ProviderInfoRegister":
			xmlString = ProviderInfoRegister(message);
			break;
		case "ProviderInfoUpdate":
			xmlString = ProviderInfoUpdate(message);
			break;
		case "OrganizationInfoRegister":
			xmlString = OrganizationInfoRegister(message);
			break;
		case "OrganizationInfoUpdate":
			xmlString = OrganizationInfoUpdate(message);
			break;

		case "SourceAndScheduleInfoAdd":
			xmlString = SourceAndScheduleInfoAdd(message);
			break;
		case "SourceAndScheduleInfoUpdate":
			xmlString = SourceAndScheduleInfoUpdate(message);
			break;
		case "OutPatientInfoAdd":
			xmlString = OutPatientInfoAdd(message);
			break;
		case "OutPatientInfoUpdate":
			xmlString = OutPatientInfoUpdate(message);
			break;
		default:
			String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String uuidString = UUID.randomUUID().toString();
			String idString = DOM4J(message).element("id").attributeValue("extension");
			xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
					+ uuidString + "\"/><creationTime value=\"" + date
					+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\"\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\"\"/></id></device></sender><acknowledgement typeCode=\"AE\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
					+ idString + "\"/></targetMessage><acknowledgementDetail><text value=\"请求方法不正确，方法名：" + action
					+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
			;
			break;
		}
		return xmlString;
	}

	public String strToDateFormat(String date, String formart) {

		DateFormat dd = new SimpleDateFormat(formart);
		Date timeDate = null;
		try {
			timeDate = dd.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(timeDate);
		return str;
	}

	private Element DOM4J(String xml) {
		Element element = null;
		try {
			Document document = DocumentHelper.parseText(xml);
			// 通过document对象获取根节点bookstore
			element = document.getRootElement();
			// 通过element对象的elementIterator方法获取迭代器

		} catch (DocumentException e) {

		}
		return element;
	}

	// 添加科室信息
	private String OrganizationInfoRegister(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String source_id = "";
		String name = "";
		String r_string = "";
		try {
			idString = docElement.element("id").attributeValue("extension");
			Element deptInfo = docElement.element("controlActProcess").element("subject").element("registrationRequest")
					.element("subject1").element("assignedEntity");
			List<Element> l_sidElements=deptInfo.element("id").elements("item");
			for (Element idElement: l_sidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.26"))
					{
					source_id=idElement.attributeValue("extension");
					break;
					}
			}
			//source_id = deptInfo.element("id").element("item").attributeValue("extension");// docInfo.element("id").element("staffCode").attributeValue("extension");
			name = deptInfo.element("assignedPrincipalOrganization").element("name").element("item").element("part").attributeValue("value");
		} catch (Exception e) {
			r_string = "传入数据错误";
		}
		if(source_id.equals(""))
			r_string="科室标识为空,系统拒绝添加";
		else
			r_string = third.dpmtInsertOrUpdate("insert", source_id, name, name);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (!r_string.equals("添加成功")) {
			r_codeString = "AE";
			if (r_string.indexOf("PRIMARY") >= 0)
				r_string = "ID重复";
			log.InsertLog("添加科室队列信息", "添加科室队列信息出错", r_string+"--xml:"+xml, 1);
		}

		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 更新科室信息
	private String OrganizationInfoUpdate(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String source_id = "";
		String name = "";
		String r_string = "";
		try {
			idString = docElement.element("id").attributeValue("extension");
			Element deptInfo = docElement.element("controlActProcess").element("subject").element("registrationRequest")
					.element("subject1").element("assignedEntity");
			List<Element> l_sidElements=deptInfo.element("id").elements("item");
			for (Element idElement: l_sidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.26"))
					{
					source_id=idElement.attributeValue("extension");
					break;
					}
			}
			name = deptInfo.element("assignedPrincipalOrganization").element("name").element("item").element("part").attributeValue("value");

		} catch (Exception e) {
			r_string = "传入数据错误";
		}
		if(source_id.equals(""))
			r_string="科室标识为空,系统拒绝更新";
		else
			r_string = third.dpmtInsertOrUpdate("update", source_id, name, name);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (!r_string.equals("更新成功")) {
			r_codeString = "AE";
			log.InsertLog("更新科室队列信息", "更新科室队列信息出错", r_string+"--xml:"+xml, 1);
		}

		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 添加排班信息
	private String SourceAndScheduleInfoAdd(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String r_string = "";
		Integer ins = 0;
		Integer upsInteger=0;
		Integer allInteger = 0;
		try {
			idString = docElement.element("id").attributeValue("extension");
			List<Element> l_rlt_d2q = docElement.element("controlActProcess").element("subject").elements("schedule");
			allInteger = l_rlt_d2q.size();
			for (int i = 0; i < l_rlt_d2q.size(); i++) {
				Element rlt_d2q = l_rlt_d2q.get(i).element("resourceSlot");
				try {
					String pb_id ="";// rlt_d2q.element("id").element("item").attributeValue("extension");
					List<Element> l_pbidElements=rlt_d2q.element("id").elements("item");
					for (Element idElement: l_pbidElements)
					{
						if(idElement.attributeValue("root").equals("2.16.156.10011.2.5.1.20"))
							{
							pb_id=idElement.attributeValue("extension");
							break;
							}
					}
					String dpmt ="";// rlt_d2q.element("deptId").element("item").attributeValue("extension");
					List<Element> l_dpmtidElements=rlt_d2q.element("deptId").elements("item");
					for (Element idElement: l_dpmtidElements)
					{
						if(idElement.attributeValue("root").equals("2.16.156.10011.1.26"))
							{
							dpmt=idElement.attributeValue("extension");
							break;
							}
					}
					String dpmt2 = rlt_d2q.element("profession").element("displayName").attributeValue("value");
					String title = rlt_d2q.element("code").element("displayName").attributeValue("value");
					String title2 = rlt_d2q.element("directTarget").element("IdentifiedEntity").element("code")
							.element("displayName").attributeValue("value");
					String doctor_id ="";// rlt_d2q.element("directTarget").element("IdentifiedEntity").element("id")
							//.element("item").attributeValue("extension");
					List<Element> l_ididElements=rlt_d2q.element("directTarget").element("IdentifiedEntity").element("id").elements("item");
					for (Element idElement: l_ididElements)
					{
						if(idElement.attributeValue("root").equals("2.16.156.10011.1.4"))
							{
							doctor_id=idElement.attributeValue("extension");
							break;
							}
					}
					
					String begin_time = rlt_d2q.element("timeFrame").element("effectiveTime").element("low")
							.attributeValue("value");
					String end_time = rlt_d2q.element("timeFrame").element("effectiveTime").element("high")
							.attributeValue("value");
					Integer totalNmb = Integer
							.parseInt(rlt_d2q.element("timeFrame").element("totalFrameNumber").attributeValue("value"));
					Integer remainNmb = Integer
							.parseInt(rlt_d2q.element("timeFrame").element("remainNumber").attributeValue("value"));
					String name = rlt_d2q.element("directTarget").element("IdentifiedEntity").element("Person")
							.element("name").element("item").element("part").attributeValue("value");
					begin_time = strToDateFormat(begin_time, "yyyyMMddHHmmss");
					end_time = strToDateFormat(end_time, "yyyyMMddHHmmss");
					Integer is_stop=0;
					String statuscodeString = rlt_d2q.element("statusCode").attributeValue("code");
					if (statuscodeString.equals("inactive"))
						is_stop = 1;
					String rString ="";
					if(pb_id.equals("")||doctor_id.equals(""))
					{
						rString="排班标识或医生ID为空,系统拒绝添加";
					}
					else {
						rString=third.docPBInsertOrUpdate("insert", pb_id, doctor_id, name, title, title2, dpmt,
							dpmt2, begin_time, end_time, totalNmb, remainNmb,is_stop);
					}
					
					if (rString.equals("添加成功"))
						ins += 1;
					else {
						rString=third.docPBInsertOrUpdate("update", pb_id, doctor_id, name, title, title2, dpmt,
								dpmt2, begin_time, end_time, totalNmb, remainNmb,is_stop);
					}
					if (rString.equals("更新成功"))
						upsInteger += 1;
				} catch (Exception ex) {
					log.InsertLog("添加排班信息", "添加排班信息出错", "xml数据错误", 1);
				}
			}
			r_string = "本次共执行" + allInteger + "条数据，其中添加成功" + ins + "条,更新"+upsInteger+"条";
		} catch (Exception e) {
			r_string = "传入数据错误";
		}

		log.InsertLog("添加排班信息", "添加排班信息", r_string, 1);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (ins == 0&&upsInteger==0) {
			r_codeString = "AE";
			if (r_string.indexOf("PRIMARY") >= 0)
				r_string = "ID重复";
			log.InsertLog("添加排班信息", "添加排班信息出错", r_string+"--xml:"+xml, 1);
		}
		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 更新排班信息
	private String SourceAndScheduleInfoUpdate(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String r_string = "";
		Integer ins = 0;
		Integer allInteger = 0;
		try {
			idString = docElement.element("id").attributeValue("extension");
			List<Element> l_rlt_d2q = docElement.element("controlActProcess").element("subject").elements("schedule");
			allInteger = l_rlt_d2q.size();
			for (int i = 0; i < l_rlt_d2q.size(); i++) {
				Element rlt_d2q = l_rlt_d2q.get(i).element("resourceSlot");
				try {
					String pb_id ="";// rlt_d2q.element("id").element("item").attributeValue("extension");
					List<Element> l_pbidElements=rlt_d2q.element("id").elements("item");
					for (Element idElement: l_pbidElements)
					{
						if(idElement.attributeValue("root").equals("2.16.156.10011.2.5.1.20"))
							{
							pb_id=idElement.attributeValue("extension");
							break;
							}
					}
					String dpmt ="";// rlt_d2q.element("deptId").element("item").attributeValue("extension");
					List<Element> l_dpmtidElements=rlt_d2q.element("deptId").elements("item");
					for (Element idElement: l_dpmtidElements)
					{
						if(idElement.attributeValue("root").equals("2.16.156.10011.1.26"))
							{
							dpmt=idElement.attributeValue("extension");
							break;
							}
					}
					String dpmt2 = rlt_d2q.element("profession").element("displayName").attributeValue("value");
					String title = rlt_d2q.element("code").element("displayName").attributeValue("value");
					String title2 = rlt_d2q.element("directTarget").element("IdentifiedEntity").element("code")
							.element("displayName").attributeValue("value");
					String doctor_id ="";// rlt_d2q.element("directTarget").element("IdentifiedEntity").element("id")
							//.element("item").attributeValue("extension");
					List<Element> l_ididElements=rlt_d2q.element("directTarget").element("IdentifiedEntity").element("id").elements("item");
					for (Element idElement: l_ididElements)
					{
						if(idElement.attributeValue("root").equals("2.16.156.10011.1.4"))
							{
							doctor_id=idElement.attributeValue("extension");
							break;
							}
					}
					
					String begin_time = rlt_d2q.element("timeFrame").element("effectiveTime").element("low")
							.attributeValue("value");
					String end_time = rlt_d2q.element("timeFrame").element("effectiveTime").element("high")
							.attributeValue("value");
					Integer totalNmb = Integer
							.parseInt(rlt_d2q.element("timeFrame").element("totalFrameNumber").attributeValue("value"));
					Integer remainNmb = Integer
							.parseInt(rlt_d2q.element("timeFrame").element("remainNumber").attributeValue("value"));
					String name = rlt_d2q.element("directTarget").element("IdentifiedEntity").element("Person")
							.element("name").element("item").element("part").attributeValue("value");
					begin_time = strToDateFormat(begin_time, "yyyyMMddHHmmss");
					end_time = strToDateFormat(end_time, "yyyyMMddHHmmss");
					Integer is_stop=0;
					String statuscodeString = rlt_d2q.element("statusCode").attributeValue("code");
					if (statuscodeString.equals("inactive"))
						is_stop = 1;
					String rString ="";
					if(pb_id.equals("")||doctor_id.equals(""))
					{
						rString="排班标识或医生ID为空,系统拒绝更新";
					}
					else {
						rString=third.docPBInsertOrUpdate("update", pb_id, doctor_id, name, title, title2, dpmt,
							dpmt2, begin_time, end_time, totalNmb, remainNmb,is_stop);
					}
					if (rString.equals("更新成功"))
						ins += 1;
				} catch (Exception e) {
					log.InsertLog("更新排班信息", "更新排班信息出错", "xml数据错误", 1);
				}

			}
			r_string = "本次共执行" + allInteger + "条数据，其中更新成功" + ins + "条";
		} catch (Exception e) {
			r_string = "传入数据错误";
		}

		log.InsertLog("更新排班信息", "更新排班信息", r_string, 1);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (ins == 0) {
			r_codeString = "AE";
			log.InsertLog("更新排班信息", "更新排班信息出错", r_string+"--xml:"+xml, 1);
		}
		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 添加患者信息
	private String OutPatientInfoAdd(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String patient_id = "";
		String patient_name = "";
		String queue_type_source_id = "";
		String source_code = "";
		String register_id = "";
		Integer time_interval = 0;
		String doctor_source_id = "";
		Integer is_deleted = 0;
		String begin_time = "";
		String end_time = "";
		String fre_date = "";
		String r_string = "";
		Integer queue_num = 0;
		
		try {
			idString = docElement.element("id").attributeValue("extension");
			Element patientInfo = docElement.element("controlActProcess").element("subject").element("encounterEvent");

			List<Element> l_ids = patientInfo.element("id").elements("item");// docInfo.element("id").element("staffCode").attributeValue("extension");
			String mzcodeString="",codenoString="",ybnoString="",fpnoString="";
			for (int i = 0; i < l_ids.size(); i++) {
				switch (l_ids.get(i).attributeValue("root")) {
				case "2.16.156.10011.1.11":
					mzcodeString=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.1.99":
					codenoString=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.1.15":
					ybnoString=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.2.5.1.9":
					fpnoString=l_ids.get(i).attributeValue("extension");
					patient_id=fpnoString;
					break;
				case "2.16.156.10011.1.9.9.1":
					register_id=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.2.5.1.20":
					if(l_ids.get(i).attributeValue("extension").indexOf("上午")>=0)
						time_interval=1;
					else if(l_ids.get(i).attributeValue("extension").indexOf("下午")>=0)
						time_interval=2;
					break;
				default:
					break;
				}
			}
			
			source_code=mzcodeString+codenoString+ybnoString+fpnoString;
			String statuscodeString = patientInfo.element("statusCode").attributeValue("code");
			if (statuscodeString.equals("inactive"))
				is_deleted = 1;
			/*List<Element> l_pidElements=patientInfo.element("subject").element("patient").element("id").elements("item");
			for (Element idElement: l_pidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.2.5.1.4"))
					{
					patient_id=idElement.attributeValue("extension");
					break;
					}
			}*/
			List<Element> l_pInidElements=patientInfo.element("subject").element("patient").element("patientPerson").element("id").elements("item");
			for (Element idElement: l_pInidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.3"))
					{
					source_code=source_code+idElement.attributeValue("extension");
					break;
					}
			}
			patient_name = patientInfo.element("subject").element("patient").element("patientPerson").element("name")
					.element("item").element("part").attributeValue("value");
			List<Element> l_docidElements=patientInfo.element("admitter").element("assignedPerson").element("id").elements("item");
			for (Element idElement: l_docidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.4"))
					{
					queue_type_source_id=idElement.attributeValue("extension");
					break;
					}
			}
			
			if(queue_type_source_id!="")
				doctor_source_id = queue_type_source_id;
			else 
			{
				List<Element> l_ks_idElements=patientInfo.element("location").element("serviceDeliveryLocation").element("location").element("id").elements("item");
				for (Element idElement: l_ks_idElements)
				{
					if(idElement.attributeValue("root").equals("2.16.156.10011.1.5"))
						{
						queue_type_source_id=idElement.attributeValue("extension");
						break;
						}
				}
			}
			// time_interval=0;
			begin_time =patientInfo.element("effectiveTime").element("low").attributeValue("value"); //new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			//long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
			end_time = patientInfo.element("effectiveTime").element("high").attributeValue("value");//new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(currentTime));
			fre_date = patientInfo.element("effectiveTime").element("any").attributeValue("value");
			if(patientInfo.element("priorityCode").element("displayName").attributeValue("value").equals("优先"))
				queue_num=5;
			if(queue_type_source_id.equals(""))
			{
				r_string="医生和科室标识为空，系统己拒绝添加";
			}
			else {
				if(is_deleted==0)
				{
					if(!patient_id.equals(""))
						r_string = third.patientInserOrUpdate("insert", patient_id, patient_name, queue_type_source_id, source_code,
								register_id, queue_num, time_interval, doctor_source_id, is_deleted, strToDateFormat(begin_time, "yyyyMMddHHmmss"), strToDateFormat(end_time, "yyyyMMddHHmmss"),
								fre_date);
						else {
							r_string="patientID标识为空，系统己拒绝添加";
						}
				}
				else {
					r_string="新增数据己经标识为退号状态，系统己拒绝添加";
				}
			}
		} catch (Exception ex) {
			r_string = "传入数据错误";
		}
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (!r_string.equals("添加成功")) {
			r_codeString = "AE";
			if (r_string.indexOf("PRIMARY") >= 0)
				r_string = "ID重复";
			log.InsertLog("添加患者信息", "添加患者信息出错", r_string+"--xml:"+xml, 1);
		}
		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 更新患者信息
	private String OutPatientInfoUpdate(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String patient_id = "";
		String patient_name = "";
		String queue_type_source_id = "";
		String source_code = "";
		String register_id = "";
		Integer time_interval = 0;
		String doctor_source_id = "";
		Integer is_deleted = 0;
		String begin_time = "";
		String end_time = "";
		String fre_date = "";
		String r_string = "";
		Integer queue_num = 0;
		
		try {
			idString = docElement.element("id").attributeValue("extension");
			Element patientInfo = docElement.element("controlActProcess").element("subject").element("encounterEvent");

			List<Element> l_ids = patientInfo.element("id").elements("item");// docInfo.element("id").element("staffCode").attributeValue("extension");
			String mzcodeString="",codenoString="",ybnoString="",fpnoString="";
			for (int i = 0; i < l_ids.size(); i++) {
				switch (l_ids.get(i).attributeValue("root")) {
				case "2.16.156.10011.1.11":
					mzcodeString=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.1.99":
					codenoString=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.1.15":
					ybnoString=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.2.5.1.9":
					fpnoString=l_ids.get(i).attributeValue("extension");
					patient_id=fpnoString;
					break;
				case "2.16.156.10011.1.9.9.1":
					register_id=l_ids.get(i).attributeValue("extension");
					break;
				case "2.16.156.10011.2.5.1.20":
					if(l_ids.get(i).attributeValue("extension").indexOf("上午")>=0)
						time_interval=1;
					else if(l_ids.get(i).attributeValue("extension").indexOf("下午")>=0)
						time_interval=2;
					break;
				default:
					break;
				}
			}
			
			source_code=mzcodeString+codenoString+ybnoString+fpnoString;
			String statuscodeString = patientInfo.element("statusCode").attributeValue("code");
			if (statuscodeString.equals("inactive"))
				is_deleted = 1;
			/*List<Element> l_pidElements=patientInfo.element("subject").element("patient").element("id").elements("item");
			for (Element idElement: l_pidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.2.5.1.4"))
					{
					patient_id=idElement.attributeValue("extension");
					break;
					}
			}*/
			List<Element> l_pInidElements=patientInfo.element("subject").element("patient").element("patientPerson").element("id").elements("item");
			for (Element idElement: l_pInidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.3"))
					{
					source_code=source_code+idElement.attributeValue("extension");
					break;
					}
			}
			patient_name = patientInfo.element("subject").element("patient").element("patientPerson").element("name")
					.element("item").element("part").attributeValue("value");
			List<Element> l_docidElements=patientInfo.element("admitter").element("assignedPerson").element("id").elements("item");
			for (Element idElement: l_docidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.4"))
					{
					queue_type_source_id=idElement.attributeValue("extension");
					break;
					}
			}
			
			if(queue_type_source_id!="")
				doctor_source_id = queue_type_source_id;
			else 
			{
				List<Element> l_ks_idElements=patientInfo.element("location").element("serviceDeliveryLocation").element("location").element("id").elements("item");
				for (Element idElement: l_ks_idElements)
				{
					if(idElement.attributeValue("root").equals("2.16.156.10011.1.5"))
						{
						queue_type_source_id=idElement.attributeValue("extension");
						break;
						}
				}
			}
			// time_interval=0;
			begin_time =patientInfo.element("effectiveTime").element("low").attributeValue("value"); //new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			//long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
			end_time = patientInfo.element("effectiveTime").element("high").attributeValue("value");//new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(currentTime));
			fre_date = patientInfo.element("effectiveTime").element("any").attributeValue("value");
			if(patientInfo.element("priorityCode").element("displayName").attributeValue("value").equals("优先"))
				queue_num=5;
			if(queue_type_source_id.equals(""))
			{
				r_string="医生和科室标识为空，系统己拒绝更新";
			}
			else {
				if(!patient_id.equals(""))
					r_string = third.patientInserOrUpdate("update", patient_id, patient_name, queue_type_source_id, source_code,
							register_id, queue_num, time_interval, doctor_source_id, is_deleted, strToDateFormat(begin_time, "yyyyMMddHHmmss"), strToDateFormat(end_time, "yyyyMMddHHmmss"),
							fre_date);
					else {
						r_string="patientID标识为空，系统己拒绝更新";
					}
				
			}
		} catch (Exception e) {
			r_string = "传入数据错误";
		}
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (!r_string.equals("更新成功")) {
			r_codeString = "AE";
			if (r_string.indexOf("PRIMARY") >= 0)
				r_string = "ID重复";
			log.InsertLog("更新患者信息", "更新患者信息出错", r_string+"--xml:"+xml, 1);
		}
		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 添加医生信息
	private String ProviderInfoRegister(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String doctor_id = "";
		String login_id = "";
		String title = "";
		String name = "";
		String department = "";
		String description="";
		String r_string = "";
		try {
			idString = docElement.element("id").attributeValue("extension");
			Element docInfo = docElement.element("controlActProcess").element("subject").element("registrationRequest")
					.element("subject1").element("healthCareProvider");
			//doctor_id = docInfo.element("id").element("item").attributeValue("extension");
			List<Element> l_docidElements=docInfo.element("id").elements("item");
			for (Element idElement: l_docidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.4"))
					{
					doctor_id=idElement.attributeValue("extension");
					break;
					}
			}
			login_id = docInfo.elementText("userName");// docInfo.element("id").element("staffCode").attributeValue("extension");
			title = docInfo.element("code").element("displayName").attributeValue("value");
			name = docInfo.element("healthCarePrincipalPerson").element("name").element("item").element("part")
					.attributeValue("value");
			description=docInfo.element("healthCarePrincipalPerson").element("introduction").element("displayName")
					.attributeValue("value");
			List<Element> l_e = docInfo.element("healthCarePrincipalPerson").elements("asAffiliate");
			for (int i = 0; i < l_e.size(); i++) {
				Element dept = l_e.get(i);
				if (dept.element("affiliatedPrincipalOrganization").elementText("default").equals("1")) {
					department = dept.element("affiliatedPrincipalOrganization").element("name").element("item")
							.element("part").attributeValue("value");
					break;
				}
			}
		} catch (Exception e) {
			r_string = "传入数据错误";
		}
		if(doctor_id.equals(""))
		{
			r_string="医生标识为空,系统拒绝添加";
		}
		else
			r_string = third.docInsertOrUpdate("insert", doctor_id, login_id, name, title, department,description);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (!r_string.equals("添加成功")) {
			r_codeString = "AE";
			if (r_string.indexOf("PRIMARY") >= 0)
				r_string = "ID重复";
			log.InsertLog("添加医生信息", "添加医生信息出错", r_string+"--xml:"+xml, 1);
		}

		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}

	// 更新医生信息
	private String ProviderInfoUpdate(String xml) {
		Element docElement = DOM4J(xml);
		String idString = "";
		String doctor_id = "";
		String login_id = "";
		String title = "";
		String name = "";
		String department = "";
		String description="";
		String r_string = "";
		try {
			idString = docElement.element("id").attributeValue("extension");
			Element docInfo = docElement.element("controlActProcess").element("subject").element("registrationRequest")
					.element("subject1").element("healthCareProvider");
			List<Element> l_docidElements=docInfo.element("id").elements("item");
			for (Element idElement: l_docidElements)
			{
				if(idElement.attributeValue("root").equals("2.16.156.10011.1.4"))
					{
					doctor_id=idElement.attributeValue("extension");
					break;
					}
			}
			login_id = docInfo.elementText("userName");// docInfo.element("id").element("staffCode").attributeValue("extension");
			title = docInfo.element("code").element("displayName").attributeValue("value");
			name = docInfo.element("healthCarePrincipalPerson").element("name").element("item").element("part")
					.attributeValue("value");
			description=docInfo.element("healthCarePrincipalPerson").element("introduction").element("displayName")
					.attributeValue("value");
			List<Element> l_e = docInfo.element("healthCarePrincipalPerson").elements("asAffiliate");
			for (int i = 0; i < l_e.size(); i++) {
				Element dept = l_e.get(i);
				if (dept.element("affiliatedPrincipalOrganization").elementText("default").equals("1")) {
					department = dept.element("affiliatedPrincipalOrganization").element("name").element("item")
							.element("part").attributeValue("value");
					break;
				}
			}
		} catch (Exception e) {
			r_string = "传入数据错误";
		}
		if(doctor_id.equals(""))
		{
			r_string="医生标识为空,系统拒绝更新";
		}
		else
			r_string = third.docInsertOrUpdate("update", doctor_id, login_id, name, title, department,description);
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String uuidString = UUID.randomUUID().toString();
		String r_codeString = "AA";
		if (!r_string.equals("更新成功")) {
			r_codeString = "AE";
			log.InsertLog("更新医生信息", "更新医生信息出错", r_string+"--xml:"+xml, 1);
		}
		String return_xmlString = "<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/MCCI_IN000002UV01.xsd\"><id root=\"2.16.156.10011.2.5.1.1\" extension=\""
				+ uuidString + "\"/><creationTime value=\"" + date
				+ "\"/><interactionId root=\"2.16.156.10011.2.5.1.2\" extension=\"MCCI_IN000002UV01\"/><processingCode code=\"P\"/><processingModeCode/><acceptAckCode code=\"AL\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString
				+ "\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item root=\"2.16.156.10011.2.5.1.3\" extension=\""
				+ uuidString + "\"/></id></device></sender><acknowledgement typeCode=\"" + r_codeString
				+ "\"><targetMessage><id root=\"2.16.156.10011.2.5.1.1\" extension=\"" + idString
				+ "\"/></targetMessage><acknowledgementDetail><text value=\"" + r_string
				+ "\"/></acknowledgementDetail></acknowledgement></MCCI_IN000002UV01>";
		return return_xmlString;
	}
}
