list
===
select * from patient_queue where is_deleted=0

LISTPATIENT_WAIT
===
SELECT * FROM (
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	a.fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
a.state_patient =5
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
a.opr_time asc  LIMIT 99999
) a
UNION ALL
SELECT * FROM(
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	a.fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
#a.state_patient in (0,2,3,4,6,7,50)
a.state_patient NOT IN (1, 5,51, 52, 53, 54)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
a.time_interval,
a.register_id + '' ASC,
a.opr_time,
a.fre_date  LIMIT 99999
) a 


LISTPATIENT_PASS
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	a.fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient = 54
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.opr_time,
	a.fre_date,
	a.register_id + '' ASC
	
LISTPATIENT_ALREADY	
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	a.fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient = 53
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.opr_time,
	a.fre_date,
	a.register_id + '' ASC
	
LISTPATIENT_PASSNO	
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	a.fre_date,
	a.call_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient IN (1, 52)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.opr_time,
	a.fre_date,
	a.register_id + '' ASC
	
LISTPATIENT_AGIN
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.time_interval,
	a.fre_date,
	a.call_time,
	a.opr_time,
	d. NAME AS doctorName,
	a.state_patient,
	a.state_custom AS state_custom,
	a.patient_source_code,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient IN (2)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.opr_time,
	a.fre_date,
	a.register_id + '' ASC




findByQueueNum
===
SELECT
	CASE
WHEN MAX(register_id + 0) IS NULL THEN
	1
ELSE
	(MAX(register_id + 0) + 1)
END AS register_id
FROM
	patient_queue
WHERE
	queue_type_id = #{QueueNumber}
	and is_deleted=0
	
	
	
findCode
===
SELECT
	b.id,
	b.patient_name,
	b.state_patient,
	b.is_display,
	c.queue_type_id,
	c.source_id,
	c.name AS queue_type_name,
	d.name AS triage_name,
	b.register_id,
	c.is_pretriage,
	c.reserve_numlist,
	d.reorder_type,
	b.time_interval,
	d.print_type
FROM  patient_queue b 
INNER JOIN queue_type c  ON c.queue_type_id = b.queue_type_id
INNER JOIN triage d ON d.triage_id = c.triage_id
WHERE
	LOCATE(#{code},b.patient_source_code)>0
AND d.ip = #{ip}
and b.is_deleted=0

findCodePager
===
SELECT
	a.id,
	a.patient_name,
	a.state_patient,
	a.is_display,
	c.id as queue_type_id,
	e.source_id,
	c.`name` AS queue_type_name,
	d.name AS triage_name,
	a.register_id,
	e.is_pretriage,
	e.reserve_numlist,
	d.reorder_type,
	a.time_interval,
	d.print_type
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER join queue_type e ON e.queue_type_id=b.queue_type_id
INNER JOIN triage d on d.triage_id=c.triage_id
WHERE
	a.patient_source_code= #{code}
and d.ip=#{ip}
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	(a.register_id + '') ASC

updatepatientqueuedislpay
===
UPDATE patient_queue a
INNER JOIN patient_code b ON a.id = b.patient_queue_id
SET is_display = 2
WHERE
	b.code = #{code}

getPatientInfobyscan
===
SELECT
			b.patient_id,b.queue_type_id,b.is_display
		FROM
			patient_queue b
		INNER JOIN queue_type c ON c.queue_type_id = b.queue_type_id
		INNER JOIN triage d ON d.triage_id = c.triage_id
		WHERE
			b.patient_source_code =#{code}
		AND d.ip = #{ip}
		AND b.is_deleted = 0

findmaxregisteridbyisdisplay
===
SELECT
	max(register_id +'')+1 AS register_id
FROM
	patient_queue
WHERE
	 queue_type_id = #{queue_type_id}
and is_deleted=0 and is_display=2

queuetypeisckinorder
===
SELECT
	is_ckin_order
FROM
	queue_type
WHERE
	queue_type_id =#{queue_type_id}

updatedisplaybyscanreorder
===
UPDATE patient_queue SET register_id=#{register_id} ,is_display=2 where id=#{id}

updatedisplaybyscan
===
UPDATE patient_queue SET is_display=2 where id=#{id}

findmaxregisterid	
===
SELECT
	max(register_id +'')+1 AS register_id
FROM
	patient_queue
WHERE
	 queue_type_id = #{queue_type_id}
and is_deleted=0
AND is_display = '2'

findmaxregisterid2
===
SELECT
	max(register_id +'')+1 AS register_id
FROM
	patient_queue
WHERE
	 queue_type_id = #{queue_type_id}
and is_deleted=0

findpagermaxregistreid
===
SELECT MAX(t.register_id+'')+1 as register_id from  (
SELECT
	a.register_id
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{queue_type_id}

AND a.is_display = '2'
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
) t

findpagermaxregistreid2
===
SELECT MAX(t.register_id+'') +1 as register_id from  (
SELECT
	a.register_id
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{queue_type_id}
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
) t
	
findmaxregisterid3	
===
SELECT
	max(register_id) AS register_id
FROM
	patient_queue
WHERE
	register_id REGEXP '[^ -~]' = 1
AND queue_type_id = #{queue_type_id}
and is_deleted=0

doctorid
===
SELECT
	a.doctor_id AS id,
	b.name,
	b.login_id
FROM
	rlt_doctor2queue_type a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
WHERE
	queue_type_id =  #{queue_type_id}
	

pagerid
===
SELECT
	a.pager_id AS id,
	b.name
FROM
	rlt_pager2queue_type a
INNER JOIN pager b ON a.pager_id = b.id
WHERE
	queue_type_id = #{queue_type_id}
	
list_call_patient_doctor2	
===	
SELECT DISTINCT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
	d.login_id = #{login_id}
AND a.queue_type_id = #{queue_type_id}
AND a.state_patient NOT IN (1,50,51,52,53,54)
AND a.is_display = '2'
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
	
list_call_patient_doctor
===
SELECT
	*
FROM
	(
		SELECT
			a.id,
			a.register_id,
			a.patient_name,
			a.state_patient,
			a.time_interval,
			a.is_display
		FROM
			patient_queue a
		LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
		LEFT JOIN triage c ON b.triage_id = c.triage_id
		LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
		WHERE
			a.state_patient = 5
		AND b.queue_type_id = #{queue_type_id}
		AND a.is_display = 2
		and a.is_deleted=0
		ORDER BY
			a.opr_time ASC  LIMIT 99999
	) a
UNION ALL
	SELECT
		*
	FROM
		(
			SELECT
				a.id,
				a.register_id,
				a.patient_name,
				a.state_patient,
				a.time_interval,
				a.is_display
			FROM
				patient_queue a
			LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
			LEFT JOIN triage c ON b.triage_id = c.triage_id
			LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
			WHERE
				#a.state_patient in (0,2,3,4,6,7,50)
				a.state_patient NOT IN (1,2,5,51, 52, 53, 54)
			AND b.queue_type_id =#{queue_type_id}
			AND a.is_display = 2
			and a.is_deleted=0
			and if(date_format(now(),'%k')>12,a.time_interval in (0,1,2),a.time_interval in (0,1))
			ORDER BY
				a.time_interval,
				a.register_id + '' ASC,
				a.opr_time,
				a.fre_date  LIMIT 99999
		) a
	

list_call_patient_doctor_nodisplay
===	
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient IN (0, 2, 3, 4, 6, 7, 50, 51)
AND b.queue_type_id = #{queue_type_id}
AND a.is_display = 1
AND a.is_deleted = 0
AND
IF (
	date_format(now(), '%k') > 12,
	a.time_interval IN (0, 1, 2),
	a.time_interval IN (0, 1)
)
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
			
					
list_call_patient_queuetype2
===
(select 
	a.id,
	a.register_id,
	a.state_patient,
	a.patient_name,
	a.time_interval,
	a.is_display
FROM patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON d.id=c.pager_id
WHERE d.ip=#{ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient =5
AND a.is_display = '2' 
and a.is_deleted=0
GROUP BY a.patient_source_code
LIMIT 99999999
)
UNION All
(
select 
	a.id,
	a.register_id,
	a.state_patient,
	a.patient_name,
	a.time_interval,
	a.is_display FROM patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON d.id=c.pager_id
WHERE d.ip=#{ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient NOT IN (1,5,50,51,52,53,54)
AND a.is_display = '2' 
and a.is_deleted=0
GROUP BY a.patient_source_code
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time
LIMIT 99999999	
)

		
list_call_patient_queuetype
===
select 
	a.id,
	a.register_id,
	a.state_patient,
	a.patient_name,
	a.time_interval,
	a.is_display FROM patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON d.id=c.pager_id
WHERE d.ip=#{ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient NOT IN (1,50,51,52,53,54)
AND a.is_display = '2' 
and a.is_deleted=0
GROUP BY a.patient_source_code
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time,
	a.fre_date  ASC;
	
	

list_patient_doctor_pass
===
SELECT DISTINCT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
	d.login_id = #{login_id}
AND a.queue_type_id = #{queue_type_id}
AND a.state_patient IN (1, 52, 54)
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time,
	a.fre_date  ASC
	
list_patient_pager_pass
===	
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON c.triage_id = b.triage_id
INNER JOIN pager d ON d.triage_id = c.triage_id
WHERE
	d.ip = #{pager_ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient IN (1, 52, 54)
AND a.is_display = 2
and a.is_deleted=0


list_patient_doctor_was
===
SELECT DISTINCT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
	d.login_id = #{login_id}
AND a.queue_type_id = #{queue_type_id}
AND a.state_patient = 54
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.opr_time,
	a.fre_date ASC
	
list_patient_pager_was
===	
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
WHERE
	a.queue_type_id=#{queue_type_id}
AND a.state_patient = 54
AND a.is_display = 2
and a.is_deleted=0


list_patient_pager_was2
===	
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON c.triage_id = b.triage_id
INNER JOIN pager d ON d.triage_id = c.triage_id
WHERE
	d.ip = #{pager_ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient = 54
AND a.is_display = 2
and a.is_deleted=0


list_patient_doctor_over
===
SELECT DISTINCT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
	d.login_id = #{login_id}
AND a.queue_type_id = #{queue_type_id}
AND a.doctor_id = #{doctor_id}
AND a.state_patient IN (53)
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time,
	a.fre_date ASC
	
list_patient_pager_over
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON c.triage_id = b.triage_id
INNER JOIN pager d ON d.triage_id = c.triage_id
WHERE
	d.ip = #{pager_ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient = 53
AND a.is_display = 2
and a.is_deleted=0

list_patient_doctor_agin
===
SELECT DISTINCT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
a.queue_type_id = #{queue_type_id}
AND a.state_patient IN (2)
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.opr_time asc

list_patient_pager_agin
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	DATE_FORMAT(a.opr_time,'%H:%i') as opr_time,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON c.triage_id = b.triage_id
INNER JOIN pager d ON d.triage_id = c.triage_id
WHERE
	d.ip = #{pager_ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient = 2
AND a.is_display = 2
and a.is_deleted=0
order by
	a.time_interval,
	a.opr_time asc

selectTriageRule
===
SELECT
	a.*
FROM
	triage a
INNER JOIN queue_type b ON b.triage_id = a.triage_id
WHERE
	b.queue_type_id =#{queue_type_id}

selectTQrule
===
SELECT
	a.call_buffer,
	a.return_flag_step,
	b.call_pass_first_flag,
	b.call_pass_rule_flag,
	b.call_return_first_flag,
	b.call_return_rule_flag
FROM
	triage a
INNER JOIN pager b ON a.triage_id = b.triage_id
WHERE
	b.ip = #{ip}
	
selectTQrule2
===
SELECT
	a.call_buffer,
	a.return_flag_step,
	b.call_pass_first_flag,
	b.call_pass_rule_flag,
	b.call_return_first_flag,
	b.call_return_rule_flag
FROM
	triage a
INNER JOIN pager b ON a.triage_id = b.triage_id
WHERE
	b.id = #{ip}

selectQueueTime
===
SELECT
	pass_flag_step,
	agin_flag_step,
	agin_first_flag,
	pass_first_flag
FROM
	queue_type
WHERE
	queue_type_id=#{queue_type_id}


updatequeuetime
===
UPDATE queue_type
SET pass_flag_step = #{time_was},
agin_flag_step= #{time_agin},
agin_first_flag=#{f_agin},
pass_first_flag=#{f_was}
WHERE
	queue_type_id =#{queue_type_id}

updatepagertime
===
UPDATE pager
SET call_pass_rule_flag = #{time_was},
call_return_rule_flag= #{time_agin},
call_return_first_flag=#{f_agin},
call_pass_first_flag=#{f_was}
WHERE
	ip =#{ip}

call_patient_doctor_callnext
=== 
SELECT DISTINCT
	a.id,
	a.patient_id,
	a.queue_type_source_id,
	a.register_id,
	a.patient_name,
	a.queue_type_id,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
WHERE
	a.queue_type_id = #{queue_type_id} 
AND d.ip = #{pager_ip}
AND c.login_id = #{login_id}
AND a.state_patient IN (0, 3, 4, 5, 6, 7)
AND a.doctor_id = #{doctor_id}
OR a.doctor_id IS NULL
AND a.is_display = '2'
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time,
	a.fre_date ASC

call_patient_pager_callnext
===
SELECT
	a.id,
	a.patient_id,
	a.queue_type_source_id,
	a.register_id,
	a.patient_name,
	a.queue_type_id,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
WHERE a.queue_type_id=#{queue_type_id} 
AND c.ip=#{pager_ip}
AND d.login_id=#{login_id}
AND a.state_patient IN (0,3, 4, 5, 6, 7)
AND a.is_display = '2'
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time,
	a.fre_date ASC
	
select_patient_doctor_queue_id
===	
SELECT
	a.id
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON b.doctor_id = c.doctor_id
WHERE
	a.state_patient = 51
AND c.ip =#{pager_ip}
AND b.login_id = #{login_id}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	(a.register_id+'') asc


select_patient_pager_queue_id
===
SELECT
	a.id
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON b.doctor_id = c.doctor_id
WHERE
	a.state_patient = 51
AND c.ip =#{pager_ip}
AND b.login_id = #{login_id}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	(a.register_id+'') asc

recall_doctor
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
WHERE
	a.id = #{patient_queue_id} 
AND d.ip = #{pager_ip}
AND c.login_id = #{login_id}
AND a.state_patient = 51
AND a.is_display = 2
and a.is_deleted=0

recall_pager
===
SELECT 
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display from patient_queue a
INNER JOIN rlt_pager2queue_type b ON b.queue_type_id=a.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
WHERE a.id=#{patient_queue_id} 
AND c.ip=#{pager_ip}
AND d.login_id=#{login_id}
AND a.state_patient=51
AND a.is_display=2
and a.is_deleted=0


SetPatientState_doctor_1
===
UPDATE patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
SET a.state_patient = 53,a.opr_time=#{opr_time}
WHERE
	a.id = #{patient_queue_id} 
AND d.ip = #{pager_ip}
AND c.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_doctor_0
===
UPDATE patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
SET a.state_patient = 54,a.opr_time=#{opr_time}
WHERE
	a.id = #{patient_queue_id}
AND d.ip =#{pager_ip}
AND c.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_doctor_2
===
UPDATE patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
SET a.state_patient = 2,a.opr_time=#{opr_time}
WHERE
	a.id = #{patient_queue_id}
AND d.ip =#{pager_ip}
AND c.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_pager_1
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
SET a.state_patient = 53,a.opr_time=#{opr_time}
WHERE
	a.id = #{patient_queue_id} 
AND c.ip = #{pager_ip}
AND d.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_pager_0
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
SET a.state_patient = 54,a.opr_time=#{opr_time}
WHERE
	a.id = #{patient_queue_id} 
AND c.ip = #{pager_ip}
AND d.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_pager_2
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
SET a.state_patient = 2,a.opr_time=#{opr_time}
WHERE
	a.id = #{patient_queue_id} 
AND c.ip = #{pager_ip}
AND d.login_id = #{login_id}
AND a.is_display = 2

call_select_doctor
===
SELECT
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
WHERE
	a.id = #{patient_queue_id}
AND a.queue_type_id = #{queue_type_id} 
AND d.ip = #{pager_ip}
AND c.login_id = #{login_id}
AND a.state_patient IN (0, 2, 3, 4, 5, 6, 7, 53,54)
AND a.is_display = 2
and a.is_deleted=0

call_select_pager
===
SELECT 
	a.id,
	a.register_id,
	a.state_patient,
	a.patient_name,
	a.time_interval,
	a.is_display from patient_queue a 
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
WHERE
	a.id = #{patient_queue_id}
AND a.queue_type_id = #{queue_type_id} 
AND c.ip = #{pager_ip}
AND d.login_id = #{login_id}
AND a.state_patient IN (0, 2, 3, 4, 5, 6, 7, 53,54)
AND a.is_display = 2
and a.is_deleted=0

select_state_patient_doctor
===
SELECT
	a.id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON c.doctor_id = b.doctor_id
WHERE
	a.state_patient = 51
AND a.is_display = 2
AND c.ip = #{pager_ip}
AND b.login_id = #{login_id}
and a.is_deleted=0

select_state_patient_pager
===
SELECT
	a.id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON c.doctor_id = b.doctor_id
WHERE
	a.state_patient = 51
AND a.is_display = 2
AND c.ip = #{pager_ip}
AND b.login_id = #{login_id}
and a.is_deleted=0

list_patient_call4
===
select 
	a.id,
	a.register_id,
	a.patient_name,
	a.content,
	a.caller,
	b. NAME AS zsmc,
	d. NAME AS jhqmc,
	a.time_interval,
	a.is_display FROM patient_queue a 
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN triage c ON c.triage_id=b.triage_id
INNER JOIN pager d ON d.triage_id=c.triage_id and d.doctor_id=a.doctor_id
INNER JOIN rlt_pager2terminal e ON e.pager_id=d.id
INNER JOIN terminal f ON f.id=e.id
INNER JOIN doctor g ON g.doctor_id=d.doctor_id 
WHERE
	a.state_patient = 51
AND a.content = '2'
AND f.ip = #{ip}
AND a.is_display = '2'
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.call_time ASC
	

list_patient_call2
===
select 
	a.id,
	a.register_id,
	a.patient_name,
	a.content,
	a.caller,
	b. NAME AS zsmc,
	d. NAME AS jhqmc,
	a.time_interval,
	a.is_display FROM patient_queue a 
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN pager d ON c.pager_id = d.id and d.ip=a.content
INNER JOIN rlt_pager2terminal e ON e.pager_id = d.id
INNER JOIN terminal f ON f.id = e.id
WHERE
	a.state_patient = 51
AND a.content != '0'
AND f.ip = #{ip}
AND a.is_display = '2'
and a.is_deleted=0
ORDER BY
	a.call_time ASC
	
list_patient_call
===
SELECT
	h.id,
	h.register_id,
	h.patient_name,
	h.content,
	h.caller,
	b. NAME AS zsmc,
	d. NAME AS jhqmc,
	h.time_interval,
	h.is_display
FROM
	doctor a
INNER JOIN pager b ON a.doctor_id = b.doctor_id
INNER JOIN rlt_doctor2queue_type c ON c.doctor_id = a.doctor_id
INNER JOIN queue_type d ON d.queue_type_id = c.queue_type_id
INNER JOIN patient_queue h ON d.queue_type_id = h.queue_type_id
INNER JOIN rlt_pager2terminal e ON b.id = e.pager_id
INNER JOIN terminal f ON f.id = e.id
WHERE
	f.ip = #{ip}
AND h.state_patient = 51
AND h.content = '2'
AND h.is_display = '2'
AND h.is_deleted = 0
ORDER BY
	h.call_time ASC;

list_patient_call_pager
===
SELECT a.id,
	a.register_id,
	a.patient_name,
	a.content,
	a.caller,
	b. NAME AS zsmc,
	d. NAME AS jhqmc,
	a.time_interval,
	a.is_display FROM terminal f
INNER JOIN rlt_pager2terminal e ON e.id=f.id
INNER JOIN pager d on d.id=e.pager_id
INNER JOIN rlt_pager2queue_type c ON c.pager_id=d.id
INNER JOIN queue_type b ON b.queue_type_id=c.queue_type_id
INNER JOIN patient_queue a on a.queue_type_id=b.queue_type_id
WHERE
	a.state_patient = 51
AND a.content= d.ip
AND f.ip = #{ip}
AND a.is_display = '2'
and a.is_deleted=0
GROUP BY
	a.patient_source_code
ORDER BY
	a.call_time ASC

updatecontent2
===
UPDATE terminal f
INNER JOIN rlt_pager2terminal e ON e.id = f.id
INNER JOIN pager d ON d.id = e.pager_id
INNER JOIN rlt_pager2queue_type c ON c.pager_id = d.id
INNER JOIN queue_type b ON b.queue_type_id = c.queue_type_id
INNER JOIN patient_queue a ON a.queue_type_id = b.queue_type_id
SET a.content = '0'
WHERE
	a.state_patient = 51
AND a.content != '0'
AND f.ip = #{ip}
AND a.is_display = '2'
AND a.is_deleted = 0
	
list_patient_call3
===
SELECT
	h.id,
	h.register_id,
	h.patient_name,
	h.content,
	h.caller,
	b. NAME AS zsmc,
	d. NAME AS jhqmc,
	h.time_interval,
	h.is_display
FROM doctor a
INNER JOIN pager b ON a.doctor_id = b.doctor_id
INNER JOIN rlt_doctor2queue_type c ON c.doctor_id = a.doctor_id
INNER JOIN queue_type d ON d.queue_type_id = c.queue_type_id
INNER JOIN patient_queue h ON d.queue_type_id=h.queue_type_id
INNER JOIN triage e ON e.triage_id = d.triage_id
inner JOIN terminal f on f.triage_id=e.triage_id
WHERE
	f.ip = #{ip}
and 
h.state_patient = 51
AND h.content = '2'
AND h.is_display = '2'
and h.is_deleted=0
ORDER BY
	h.call_time ASC
	
list_doctor_room_door_jz
===	
SELECT
	a.id,
	a.state_patient,
	a.register_id,
	a.caller,
	a.patient_name,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
INNER JOIN pager e ON e.doctor_id=d.doctor_id
INNER JOIN rlt_pager2terminal f ON f.pager_id=e.id
INNER JOIN terminal g ON g.id=f.id
WHERE
a.state_patient = 51
AND d.doctor_id=#{doctor_id}
AND g.ip=#{ip}
AND a.is_display = 2
and a.is_deleted=0	
	
list_pager_room_door_jz
===
SELECT
	a.id,
	a.state_patient,
	a.register_id,
	a.caller,
	a.patient_name,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON c.pager_id=d.id
INNER JOIN rlt_pager2terminal e ON e.pager_id=d.id
INNER JOIN terminal f ON f.id=e.id
WHERE a.state_patient = 51
AND f.ip = #{ip}
AND a.is_display = 2
and a.is_deleted=0

updatecall
===
UPDATE patient_queue a
INNER JOIN pager b ON a.doctor_id = b.doctor_id
SET a.caller = '0'
WHERE
	b.doctor_id = #{doctor_id}
AND a.state_patient = 51
AND a.is_display = 2

updatecall2
===
UPDATE patient_queue a
SET a.caller = '0'
WHERE
	a.caller IN (
		SELECT
			b.ip
		FROM
			pager b
		INNER JOIN rlt_pager2terminal c ON b.id = c.pager_id
		INNER JOIN terminal d ON c.id = d.id
		WHERE
			d.ip = #{ip})


list_content_patient
===
SELECT
	a.id,
	a.register_id,
	a.istype,
	a.patient_name,
	b.queue_type_id,
	b.name queuetypename,
	d.name AS doctorname,
	d.doctor_id,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON c.triage_id = b.triage_id
INNER JOIN doctor d ON a.doctor_id = d.doctor_id 
WHERE
	a.state_patient = 51
AND content = '1'
AND c.ip = #{ip}
and a.is_deleted=0

update_content
===
UPDATE patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON c.triage_id = b.triage_id
SET a.content = '2',
 a.caller = '2'
WHERE
	a.id = #{id} 
AND a.queue_type_id = #{queue_type_id}
AND c.ip = #{ip}

count_patient_doctor
===
SELECT DISTINCT
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient IN (0, 2, 3, 4, 5, 6, 7, 50)
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
	and aa.is_deleted=0
	) AS waitingnum,
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient = 54
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		and aa.is_deleted=0
	) AS passnum,
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient IN (51, 53)
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		and aa.is_deleted=0
	) AS callednum
FROM
	rlt_doctor2queue_type a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN queue_type c ON c.queue_type_id = a.queue_type_id
INNER JOIN triage d ON d.triage_id = c.triage_id
INNER JOIN patient_queue e ON e.queue_type_id = c.queue_type_id
INNER JOIN pager f ON f.triage_id = d.triage_id
WHERE
	b.login_id = #{login_id}
AND f.ip = #{ip}


count_patient_pager
===
SELECT DISTINCT
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient IN (0, 2, 3, 4, 5, 6, 7, 50)
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		AND aa.is_deleted = 0
	) AS waitingnum,
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient = 54
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		AND aa.is_deleted = 0
	) AS passnum,
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient IN (51, 53)
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		AND aa.is_deleted = 0
	) AS callednum
FROM
	rlt_pager2queue_type a
INNER JOIN pager b ON a.pager_id = b.id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN triage d ON d.triage_id = b.triage_id
INNER JOIN queue_type e ON e.triage_id = d.triage_id
INNER JOIN patient_queue f ON f.queue_type_id = e.queue_type_id
WHERE
	c.login_id = #{login_id}
AND b.ip = #{ip}


GetStatistic
===
SELECT DISTINCT
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient IN (0, 2, 3, 4, 5, 6, 7, 50)
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		AND aa.is_deleted = 0
	) AS waitingnum,
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient = 54
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		AND aa.is_deleted = 0
	) AS passnum,
	(
		SELECT
			count(*)
		FROM
			patient_queue aa
		WHERE
			aa.state_patient IN (51, 53)
		AND aa.queue_type_id = e.queue_type_id
		AND aa.is_display = 2
		AND aa.is_deleted = 0
	) AS callednum
FROM
	rlt_doctor2queue_type a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN queue_type c ON c.queue_type_id = a.queue_type_id
INNER JOIN triage d ON d.triage_id = c.triage_id
INNER JOIN patient_queue e ON e.queue_type_id = c.queue_type_id
INNER JOIN pager f ON f.triage_id = d.triage_id
WHERE
	b.login_id = #{login_id}
AND f.ip = #{ip}
AND e.queue_type_id = #{queue_type_id}




GetPatientList
===
SELECT
	a.id,
a.patient_id,
a.patient_name,
a.register_id,
b.queue_type_id,
b.name as queue_type_name,
b.source_id as queue_type_source_id,
a.state_patient,
a.sub_queue_order,
f.login_id,
f.name as doctor_name,
a.time_interval,
a.is_display
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON b.triage_id=c.triage_id
INNER JOIN pager d ON d.triage_id=c.triage_id
INNER JOIN rlt_doctor2queue_type e ON e.queue_type_id=b.queue_type_id 
INNER JOIN doctor f ON f.doctor_id=e.doctor_id
WHERE f.login_id=#{login_id}
AND b.source_id=#{Queue_type_source_id}
AND d.ip=#{pager_ip}
and a.is_deleted=0



deletepatientbyqueuetypeid
===
DELETE a,
 b
FROM
	patient_queue a,
	patient_code b
WHERE
	a.id = b.patient_queue_id
AND a.queue_type_id = #{queue_type_id}

deletepatient
===
DELETE  FROM patient_queue where 1=1;


setidentity
===
alter table patient_queue AUTO_INCREMENT=1

autosetpatientdisplay
===
SELECT
	*
FROM
	(
		SELECT
			*, count(t.patient_source_code) AS counts
		FROM
			(
				SELECT
					a.*,c.ip
				FROM
					patient_queue a
				INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
				INNER JOIN pager c ON b.pager_id = c.id
				INNER JOIN triage d ON d.triage_id = c.triage_id
				WHERE
					d.ip = #{ip}
				AND a.state_patient <> '53'
				and a.is_deleted=0
				GROUP BY
					c.ip,
					a.patient_source_code
			) t
		GROUP BY
			t.patient_source_code
		ORDER BY
			t.time_interval,
			t.fre_date,t.id
	) s
WHERE
	s.is_display = '1'
	
updateautosetpatientdisplay
===
UPDATE 
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id

SET a.is_display = '2',
 a.register_id = #{r_id}

WHERE
	a.patient_source_code=#{source_code}
AND c.ip = #{ip}

selectlestpatientqueue
===
SELECT
	count(aa.id) AS counts,
	max(aa.register_id) AS r_id,
	dd.ip
FROM
	patient_queue aa
INNER JOIN rlt_pager2queue_type bb ON aa.queue_type_id = bb.queue_type_id
INNER JOIN pager cc ON bb.pager_id = cc.id
INNER JOIN (
	SELECT
		c.ip
	FROM
		patient_queue a
	INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
	INNER JOIN pager c ON c.id = b.pager_id
	WHERE
		a.patient_source_code = #{source_code}
		and a.is_deleted=0
	GROUP BY
		c.ip
) dd ON cc.ip = dd.ip
GROUP BY
	cc.ip
ORDER BY
	counts
	
getqueuemaxregisterid
===
SELECT
	max(register_id + '') AS register_id
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c on b.pager_id=c.id

WHERE
	a.is_display = 2 and c.ip=#{ip}
	and a.is_deleted=0

vistinglocaltion
===
SELECT
	a.id,
	a.patient_name,
	a.queue_type_id,
	a.register_id,
	a.is_display,
	a.opr_time,
	a.patient_source_code,
	b.displayname,
	b.`name`,
	b.floor,
	(
		SELECT
			count(*)
		FROM
			patient_queue d
		WHERE
			d.queue_type_id = a.queue_type_id
		AND d.register_id+'' < a.register_id+''
		AND d.is_display = 2
		and d.is_deleted=0
	) AS counts,
	a.time_interval
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON b.triage_id = c.triage_id
WHERE
	c.ip = #{ip}
AND a.state_patient NOT IN (53)
AND a.is_deleted = 0
AND a.is_display = 2
and a.is_deleted=0
GROUP BY
	a.patient_source_code
ORDER BY 	a.time_interval,  a.register_id+'' ,a.opr_time 
	


listnextshowdoctor
===
SELECT
	aa.patient_name,
	aa.register_id,
	bb.floor,
	bb.`name`,
	bb.displayname,
	aa.time_interval,
	aa.is_display
FROM
	patient_queue aa
INNER JOIN queue_type bb ON aa.queue_type_id = bb.queue_type_id
WHERE
	patient_source_code IN (
		SELECT
			a.patient_source_code
		FROM
			patient_queue a
		INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
		INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
		INNER JOIN pager d ON c.pager_id = d.id
		INNER JOIN rlt_pager2terminal e ON e.pager_id = d.id
		INNER JOIN terminal f ON f.id = e.id
		WHERE
			a.state_patient = 53
		AND ISNULL(a.nextshow)
		OR a.nextshow = ''
		AND d.doctor_id=#{doctor_id}
		AND f.ip = #{ip}
		AND a.is_display = 2
		and a.is_deleted=0
	)
AND state_patient <> '53'
AND is_display = 2

listnextshowpager
===
SELECT
	aa.patient_name,
	aa.register_id,
	bb.floor,
	bb.`name`,
	bb.displayname,
	aa.queue_type_id,
	aa.time_interval,
	aa.is_display
FROM
	patient_queue aa
INNER JOIN queue_type bb ON aa.queue_type_id = bb.queue_type_id
WHERE
	patient_source_code IN (
		SELECT
			a.patient_source_code
		FROM
			patient_queue a
		INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
		INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
		INNER JOIN pager d ON c.pager_id = d.id
		INNER JOIN rlt_pager2terminal e ON e.pager_id = d.id
		INNER JOIN terminal f ON f.id = e.id
		WHERE
			a.state_patient = 53
		and a.nextshow<>'2'
		AND f.ip = #{ip}
		AND a.is_display = 2
		and a.is_deleted=0
		GROUP BY a.patient_source_code
	)
AND state_patient <> '53'
AND is_display = 2
GROUP BY patient_source_code

updatenextshow
===
UPDATE patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
INNER JOIN pager d ON c.pager_id = d.id
INNER JOIN rlt_pager2terminal e ON e.pager_id = d.id
INNER JOIN terminal f ON f.id = e.id
SET a.nextshow = '2'
WHERE
	a.state_patient = 53
AND a.nextshow<>'2'
AND f.ip = #{ip}
AND a.is_display = 2




selectpatientcallingpager
===
SELECT
	 a.id,
 a.register_id,
 a.patient_name,
 a.state_patient,
 a.patient_source_code,
 a.caller,
 c.`name` as zsmc,
 c.ip,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id and c.ip=a.caller
INNER JOIN rlt_pager2terminal d on d.pager_id=c.id 
INNER JOIN terminal e ON d.id=e.id
WHERE
	e.ip = #{ip}
AND FIND_IN_SET(a.state_patient,#{status})
AND a.is_display='2'
and a.is_deleted=0
GROUP BY
	c.ip,
	c.name,
	a.patient_source_code
ORDER BY 	a.time_interval,	a.time_interval, (a.register_id+'') asc;

selectpatientwaitpager
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	a.fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.ip,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.ip = #{ip}
AND FIND_IN_SET(a.state_patient,#{status})
AND a.is_display = '2'
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	(a.register_id + '') ASC

selectpatientwaitpagerId
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	a.fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.ip,
	a.opr_time,
	a.is_display
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
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	(a.register_id + '') ASC
	
selectpatientwaitpagerId2
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	a.fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.ip,
	a.opr_time,
	a.is_display
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
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	a.opr_time asc
	
selectpatientwaitpagerId3
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	a.fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.ip,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{id}
and a.caller=c.ip
AND FIND_IN_SET(a.state_patient,#{status})
AND a.is_display = '2'
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	a.opr_time asc

selectpatientteminal
===
SELECT
	*
FROM
	(
		SELECT
			a.id,
			a.register_id,
			a.patient_name,
			a.state_patient,
			a.patient_source_code,
			a.caller,
			a.fre_date,
			a.call_time,
			a.time_interval,
			a.state_custom,
			a.opr_time,
			a.is_display
		FROM
			patient_queue a
		INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
		INNER JOIN pager c ON b.pager_id = c.id
		LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
		INNER JOIN rlt_pager2terminal e ON e.pager_id = c.id
		INNER JOIN terminal f ON f.id = e.id
		WHERE
			f.ip = #{ip}
		AND FIND_IN_SET(
			a.state_patient,
			#{status}
		)
		and c.doctor_id<>''
		AND a.is_display = '2'
		AND a.is_deleted = 0
		GROUP BY
			c.ip,
			a.patient_source_code
		ORDER BY
			register_id + '' ASC
	) t
GROUP BY
	t.patient_source_code
ORDER BY 	t.time_interval, t.register_id+'' asc

selectpatientbycode
===
SELECT
	 a.id,
 a.register_id,
 a.patient_name,
 a.state_patient,
 a.patient_source_code,
 a.caller,
 c.`name` as zsmc,
 c.ip,
	a.time_interval,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
WHERE
	c.ip = #{ip}
AND a.patient_source_code=#{code}
AND a.is_display='2'
and a.is_deleted=0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY 	a.time_interval, (a.register_id+'') asc



updatepatientpagercallerstatus
===
UPDATE patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
INNER JOIN pager d ON c.pager_id = d.id
SET caller = '0'
WHERE
	a.patient_source_code = #{code}
AND d.ip =#{ip}

updatepatientpagercontentstatus
===
UPDATE patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
INNER JOIN pager d ON c.pager_id = d.id
SET content = '0'
WHERE
	a.patient_source_code = #{code}
AND d.ip =#{ip}


callupdatestatusbypager
===
UPDATE 
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id

SET a.state_patient = #{status},a.content=#{call},a.caller=#{call}
WHERE
	a.patient_source_code=#{source_code}
AND c.ip = #{ip}

selectpatientbycodeandip
===
SELECT
	a.*, c.ip
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
WHERE
	c.ip = #{ip}
AND a.patient_source_code = #{code}
and a.is_deleted=0
GROUP BY
	a.patient_source_code

updatesetpatient_fz
===
SELECT
	a.*
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN triage c ON b.triage_id = c.triage_id
WHERE
	a.patient_source_code IN (
		SELECT
			d.patient_source_code
		FROM
			patient_queue d
		WHERE
			d.patient_source_code NOT IN (
				SELECT
					e.patient_source_code
				FROM
					patient_queue e
				INNER JOIN rlt_pager2queue_type f ON e.queue_type_id = f.queue_type_id
				INNER JOIN pager g ON e.pager_id = g.id
				INNER JOIN triage h ON g.triage_id = h.triage_id
				WHERE
					h.ip = '192.168.0.99'
				AND e.state_patient <> '53'
				and e.is_deleted=0
				GROUP BY
					e.patient_source_code
			)
		GROUP BY
			d.patient_source_code
	)
AND c.ip <> '192.168.0.99'


selectmaxregisterid_visit
===
SELECT
	max(register_id + '') + 1 as max
FROM
	patient_queue
WHERE
	queue_type_id = (
		SELECT
			queue_type_id
		FROM
			patient_queue
		WHERE
			id = #{id}
		AND state_patient = 53
		and is_deleted=0
	)

selectqueuetypeidbydoctor
===
SELECT
	queue_type_id
FROM
	rlt_doctor2queue_type 
WHERE
	doctor_id =#{doctor_id}

selectPatientBySourceCode
===
SELECT * FROM patient_queue where queue_num=#{source_code2} and patient_source_code=#{source_code1} and queue_type_id=#{queue_type_id}

updateQuhaoIsdisplay
===
UPDATE patient_queue SET is_display=2 where patient_id=#{patient_id}

patientFirstByPagerList
===
SELECT
	a.*
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON c.id = b.pager_id
WHERE
	c.id = #{pager_id}
AND a.patient_source_code IN (
	SELECT
		d.patient_source_code
	FROM
		patient_queue d
	WHERE
		FIND_IN_SET(
			d.id,
			#{ids}
		)
)

patientBDByPager
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
SET a.is_display = 2
WHERE
	a.patient_source_code = #{code}
AND c.id = #{queue_type_id}
AND a.is_deleted = 0

patientBDByPager2
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
SET a.is_display = 2,a.register_id=#{rid}
WHERE
	a.patient_source_code = #{code}
AND c.id = #{queue_type_id}
AND a.is_deleted = 0

getpatientbypagerlist
===
SELECT
	a.patient_name,
	d.`name` as triage_name,
	c.id as queue_type_id,
	a.id,
	a.state_patient,
	a.time_interval,
	c.`name` as queue_type_name,
	d.print_type,
	a.register_id
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN triage d ON d.triage_id=c.triage_id
WHERE
	a.patient_source_code = #{code}
AND c.id = #{queue_type_id}
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
	
	
getPatientByCodeTypeID
 ===
 SELECT
	id
FROM
	patient_queue
WHERE
	patient_source_code = #{code}
AND queue_type_id =#{queue_type_id}
	
getylnmb
===
SELECT
	max(t.register_id + '') + 1 AS register_id
FROM
	(
		SELECT
			a.patient_name,
			d.`name` AS triage_name,
			c.id AS queue_type_id,
			a.id,
			a.register_id,
			a.state_patient,
			a.time_interval,
			a.is_display
		FROM
			patient_queue a
		INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
		INNER JOIN pager c ON b.pager_id = c.id
		INNER JOIN triage d ON d.triage_id = c.triage_id
		WHERE
			c.id = #{queue_type_id}
		AND a.is_deleted = 0
		AND a.state_patient IN (0, 2, 3, 4, 5, 6, 7, 50)
		AND a.register_id + '' <= #{ylh}
		AND a.is_display = 2
		GROUP BY
			c.ip,
			a.patient_source_code
	) t
 
 
 
 selectpatientdoctor
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.patient_source_code,
	a.caller,
	a.fre_date,
	a.call_time,
	a.time_interval,
	a.state_custom,
	c.`name` AS zsmc,
	c.ip,
	a.opr_time,
	a.is_display
FROM
	patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.doctor_id = c.doctor_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.ip = #{ip}
AND FIND_IN_SET( a.state_patient, #{status})
AND a.is_display = '2'
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	(a.register_id + '') ASC
	
selectPatientbycodequeueid
===
SELECT * FROM patient_queue where queue_type_id=#{queue_type_id} and LOCATE(#{code},patient_source_code)>0
 