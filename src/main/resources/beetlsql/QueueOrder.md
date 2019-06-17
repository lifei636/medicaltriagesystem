queryWaitLock_docot
===
SELECT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	date_format(a.fre_date,'%Y-%m-%d %H:%I:%s') fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
a.state_patient in (0,2,3, 4,5, 6, 7,8,54)
and b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
AND a.late_lock=1
ORDER BY
	a.opr_time
	
	
queryPatientList_docot
===
SELECT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	date_format(a.fre_date,'%Y-%m-%d %H:%I:%s') fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	FIND_IN_SET(a.state_patient ,#{state_patient})
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.late_lock = 0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
	
	
queryPatientList_page
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	date_format(a.fre_date,'%Y-%m-%d %H:%I:%s') fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,register_id2,state_patient2
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{id}
AND FIND_IN_SET(a.state_patient,#{status})
AND a.is_display = '2'
AND a.is_deleted = 0
and a.late_lock = 0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	(a.register_id + '') ASC
	
	
queryWaitLock_page 
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	date_format(a.fre_date,'%Y-%m-%d %H:%I:%s') fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,register_id2,state_patient2
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{id}
AND a.state_patient in (0,2,3, 4,5, 6, 7,8,54)
AND a.is_display = '2'
AND a.is_deleted = 0
and a.late_lock=1
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.opr_time