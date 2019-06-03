package com.shine.service;


public interface ThirdDataService  {
	public String docInsertOrUpdate(String type,String doctor_id,String login_id,String name,String title,String department,String description);
	public String dpmtInsertOrUpdate(String type,String souce_id,String name,String displayname);
	public String docPBInsertOrUpdate(String type,String pb_id,String doctor_id,String name,String title,String title2,String department,String department2,String begin_time,String end_time,Integer totalNmb,Integer remainNmb,Integer is_stop);
	public String  patientInserOrUpdate(String type,String patient_id,String patient_name,String queue_type_source_id,String source_code,String register_id,Integer queue_num,Integer time_interval,String doctor_source_id,Integer is_deleted,String begin_time,String end_time,String fre_date);
}
