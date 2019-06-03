list
===
select * from datasql

 checktab
===
 SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='medicaltriagesystem' AND TABLE_NAME=#{tabname}
 
 checktab
===
CREATE TABLE #{tabname} (
  `id` int(11) NOT NULL AUTO_INCREMENT,
	`login_id` varchar(10) DEFAULT NULL,
	`doctor_name` varchar(20) DEFAULT NULL,
	`queue_type_id` varchar(100) DEFAULT NULL,
	`queue_type_name` varchar(100) DEFAULT NULL,
	`pager_name` varchar(100) DEFAULT NULL,
	`pager_ip` varchar(20) DEFAULT NULL,
	`databasename` varchar(100) DEFAULT NULL,
	`dbType` varchar(10) DEFAULT NULL,
	`patient_id` varchar(100) DEFAULT NULL,
	`patient_name` varchar(20) DEFAULT NULL,
	`call_time` datetime DEFAULT NULL,
	`start_time` datetime DEFAULT NULL,
	`fre_date` datetime DEFAULT NULL,
	`state_patient` int(11) DEFAULT NULL,
	`patient_source_code` varchar(255) DEFAULT NULL,
	`is_deleted` int(11) DEFAULT NULL,
	`is_display` int(11) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

SyncPatientData
===
INSERT INTO  #{tabname} (
	login_id,
	doctor_name,
	queue_type_id,
	queue_type_name,
	pager_name,
	pager_ip,
	databasename,
	dbType,
	patient_id,
	patient_name,
	call_time,
	start_time,
	fre_date,
	state_patient,
	patient_source_code,
	is_deleted,
	is_display
) SELECT
	b.login_id,
	b.`NAME` AS doctor_name,
	c.source_id AS queue_type_id,
	c.`NAME` AS queue_type_name,
	d.`NAME` AS pager_name,
	d.ip AS pager_ip,
	f.description AS databasename,
	f.dbType,
	a.patient_id,
	a.patient_name,
	a.call_time,
	a.start_time,
	a.fre_date,
	a.state_patient,
	a.patient_source_code,
	a.is_deleted,
	a.is_display
FROM
	patient_queue a
LEFT JOIN doctor b ON a.doctor_id = b.doctor_id
LEFT JOIN queue_type c ON a.queue_type_id = c.queue_type_id
LEFT JOIN pager d ON a.callerip = d.ip
LEFT JOIN triage e ON c.triage_id = e.triage_id
LEFT JOIN data_connection f ON a.db_source_id = f.id