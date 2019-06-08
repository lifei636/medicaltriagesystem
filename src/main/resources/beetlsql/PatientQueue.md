list
===
select * from patient_queue where is_deleted=0

LISTPATIENT_WAIT
===

SELECT
	a.id,
	a.patient_id,
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
a.state_patient in (0,3,4,6,7,50)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
a.time_interval,
a.register_id + '' ASC,
a.opr_time,
a.fre_date 

LISTQUERYTYPE_ALL
===
SELECT qt.queue_type_id FROM triage t INNER join
queue_type qt on t.triage_id=qt.triage_id 
where t.triage_id = #{triage_id}

UPDATE_PATIENT_QUEUE_UNLOCK
===
UPDATE patient_queue SET late_lock=0,opr_time=#{newTime} WHERE patient_id = #{patient_id}

UPDATE_PATIENT_QUEUE_LOCK
===
UPDATE patient_queue SET late_lock=1 ,opr_time=#{newTime} WHERE patient_id = #{patient_id}

FIND_WARIT_PARITE_ALL
===
select * from (SELECT
	a.id,
	a.patient_id,
	a.patient_name,
	a.queue_type_id,
	a.late_lock,
	a.state_patient2
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient = 5
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date) t
union all 
select * from (
SELECT
	a.id,
	a.patient_id,
	a.patient_name,
	a.queue_type_id,
	a.late_lock,
	a.state_patient2
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
a.state_patient in (0,3,4,6,7,50)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
and a.late_lock=0
ORDER BY
a.time_interval,
a.register_id + '' ASC,
a.opr_time,
a.fre_date)b
	

LISTPATIENT_LOCK
===

SELECT
	a.id,
	a.patient_id,
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
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
AND a.late_lock=1
ORDER BY
	a.opr_time

LISTPATIENT_FIRST
===
SELECT
	a.id,
	a.patient_id,
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
	a.state_patient = 5
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
and a.late_lock = 0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date

LISTPATIENT_LATE
===
SELECT
	a.id,
	a.patient_id,
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
	a.state_patient = 8
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.late_lock DESC,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date

LISTPATIENT_PASS
===
SELECT
	a.id,
	a.patient_id,
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
	a.state_patient = 54
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
	
LISTPATIENT_ALREADY	
===
SELECT
	a.id,
	a.patient_id,
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
	a.state_patient = 53
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
	
LISTPATIENT_PASSNO	
===
SELECT
	a.id,
	a.patient_id,
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
	a.state_patient IN (1, 52)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
	
	
LISTPATIENT_NODISPLAY
===
SELECT
	a.id,
	a.patient_id,
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
 b.queue_type_id = #{queueTypeId}
AND a.is_display = 1
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date

LISTPATIENT_AGIN
===
SELECT
	a.id,
	a.patient_id,
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
	a.state_patient IN (2)
AND b.queue_type_id = #{queueTypeId}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.register_id2 ASC,
	a.opr_time,
	a.fre_date




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
	c.displayname as queue_type_displayname,
	d.name AS triage_name,
	b.register_id,
	c.is_pretriage,
	c.reserve_numlist,
	d.reorder_type,
	b.time_interval,
	d.print_type,
	b.patient_source_code,
	c.queue_type_id as q_id,
	b.fre_date,
	b.register_id2,
	b.state_patient2,
	b.doctor_id,
	b.begin_time,
	b.last_time,
	b.doctor_id
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
	f.login_id as source_id,
	c.`name` AS queue_type_name,
	c.display_name as queue_type_displayname,
	d.name AS triage_name,
	a.register_id,
	e.is_pretriage,
	e.reserve_numlist,
	d.reorder_type,
	a.time_interval,
	d.print_type,
	a.patient_source_code,
	e.queue_type_id as q_id,
	a.fre_date,
	a.register_id2,
	a.state_patient2,
	a.doctor_id,
	a.begin_time,
	a.last_time,
	a.doctor_id
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
left JOIN doctor f on c.doctor_id=f.doctor_id
INNER join queue_type e ON e.queue_type_id=b.queue_type_id
INNER JOIN triage d on d.triage_id=c.triage_id
WHERE
	LOCATE(#{code},a.patient_source_code)>0
and d.ip=#{ip}
AND a.is_deleted = 0
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	(a.register_id + '') ASC
	

findCodeThirdSelectPatient
===
SELECT
	b.id,
	b.patient_name,
	b.state_patient,
	b.is_display,
	c.queue_type_id,
	c.name AS queue_type_name,
	c.displayname as queue_type_displayname,
	d.name AS triage_name,
	b.register_id,
	b.patient_source_code,
	b.time_interval,
	b.fre_date
FROM  patient_queue b 
INNER JOIN queue_type c  ON c.queue_type_id = b.queue_type_id
INNER JOIN triage d ON d.triage_id = c.triage_id
WHERE
	LOCATE(#{code},b.patient_source_code)>0
AND d.ip = #{ip}
and b.is_deleted=0

findCodePagerThirdSelectPatient
===
SELECT
	a.id,
	a.patient_name,
	a.state_patient,
	a.is_display,
	c.id as queue_type_id,
	c.`name` AS queue_type_name,
	c.display_name as queue_type_displayname,
	d.name AS triage_name,
	a.register_id,
	a.patient_source_code,
	a.time_interval,
	a.fre_date
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
left JOIN doctor f on c.doctor_id=f.doctor_id
INNER join queue_type e ON e.queue_type_id=b.queue_type_id
INNER JOIN triage d on d.triage_id=c.triage_id
WHERE
	LOCATE(#{code},a.patient_source_code)>0
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
SET a.is_display = 2
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
UPDATE patient_queue SET register_id=#{register_id} ,is_display=2,opr_time=#{opr_time} where id=#{id}

updatedisplaybyscan
===
UPDATE patient_queue SET is_display=2,opr_time=#{opr_time} where id=#{id}

updatedisplaybyscan2
===
UPDATE patient_queue SET state_patient=8,is_display=2,opr_time=#{opr_time} where id=#{id}

updateFZbyscan
===
UPDATE patient_queue SET state_patient=2,call_count=0,opr_time=#{opr_time},register_id2=#{register_id2},	state_patient2=#{state_patient2} where id=#{id}

findmaxregisteridnow
===
SELECT MAX(register_id+'') as register_id FROM patient_queue where state_patient !=0 and queue_type_id=#{queue_type_id}

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
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.patient_source_code,
	a.queue_type_id,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient IN (0, 3, 4, 6, 7)
AND b.queue_type_id = #{queue_type_id}
AND a.is_display = 2
AND a.is_deleted = 0
ORDER BY
	a.time_interval,
	a.register_id + '' ASC,
	a.opr_time,
	a.fre_date
	
list_call_patient_doctor_locked
===
SELECT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.patient_source_code,
	a.queue_type_id,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
LEFT JOIN triage c ON b.triage_id = c.triage_id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	a.state_patient IN (0,2,3, 4,5, 6, 7,8,54)
AND b.queue_type_id = #{queue_type_id}
AND a.is_display = 2
AND a.is_deleted = 0 
AND a.late_lock=1
ORDER BY
	a.opr_time
	
list_call_patient_doctor_interval
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
	a.state_patient IN (0, 2, 3, 4, 6, 7,8, 50, 51)
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

select 
	a.id,
	a.patient_id,
	a.register_id,
	a.state_patient,
	a.patient_name,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2 FROM patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON d.id=c.pager_id
WHERE d.ip=#{ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient NOT IN (1,5,8,50,51,52,53,54)
AND a.is_display = '2' 
and a.is_deleted=0
GROUP BY a.patient_source_code
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time

		
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
	
list_call_patient_queuetype2_locked
===
select 
	a.id,
	a.patient_id,
	a.register_id,
	a.state_patient,
	a.patient_name,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2 FROM patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON d.id=c.pager_id
WHERE d.ip=#{ip} and a.queue_type_id=#{queue_type_id}
AND a.state_patient IN (0,2,3, 4,5, 6, 7,8,54)
AND a.is_display = '2' 
and a.is_deleted=0
and a.late_lock=1
GROUP BY a.patient_source_code
ORDER BY
	a.opr_time
	
	

list_patient_doctor_pass
===
SELECT DISTINCT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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


list_patient_doctor_late
===
SELECT DISTINCT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
	d.login_id = #{login_id}
AND a.queue_type_id = #{queue_type_id}
AND a.state_patient = 8
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.late_lock DESC,
	a.register_id + '',
	a.opr_time,
	a.fre_date ASC
	
list_patient_pager_late
===	
SELECT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
WHERE
	a.queue_type_id=#{queue_type_id}
AND a.state_patient = 8
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.late_lock DESC,
	a.register_id + '',
	
	
list_patient_doctor_first
===
SELECT DISTINCT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON d.doctor_id = c.doctor_id
WHERE
	d.login_id = #{login_id}
AND a.queue_type_id = #{queue_type_id}
AND a.state_patient = 5
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '',
	a.opr_time,
	a.fre_date ASC
	
list_patient_pager_first
===	
SELECT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
WHERE
	a.queue_type_id=#{queue_type_id}
AND a.state_patient = 5
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.time_interval,
	a.register_id + '',


list_patient_doctor_was
===
SELECT DISTINCT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.register_id2,
	a.opr_time asc

list_patient_pager_agin
===
SELECT
	a.id,
	a.patient_id,
	a.register_id,
	a.patient_name,
	a.state_patient,
	DATE_FORMAT(a.opr_time,'%H:%i') as opr_time,
	a.time_interval,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.opr_time,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.register_id2 asc,
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
	a.late_flag_step,
	a.first_flag_step,
	a.late_show,
	b.call_pass_first_flag,
	b.call_pass_rule_flag,
	b.call_return_first_flag,
	b.call_return_rule_flag,
	b.call_late_first_flag,
	b.call_late_rule_flag,
	b.call_first_first_flag,
	b.call_first_rule_flag,
	b.call_first_rule_flag2
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
	a.late_flag_step,
	b.call_pass_first_flag,
	b.call_pass_rule_flag,
	b.call_return_first_flag,
	b.call_return_rule_flag,
	b.call_late_first_flag,
	b.call_late_rule_flag
FROM
	triage a
INNER JOIN pager b ON a.triage_id = b.triage_id
WHERE
	b.id = #{ip}
	
selectTQrule3_1
===
SELECT
	b.call_buffer,
	b.return_flag_step,
	b.late_flag_step,
	b.first_flag_step,
	CASE
WHEN d.call_pass_first_flag IS NULL THEN
	0
ELSE
	d.call_pass_first_flag
END AS call_pass_first_flag,
 CASE
WHEN d.call_pass_rule_flag IS NULL THEN
	0
ELSE
	d.call_pass_rule_flag
END AS call_pass_rule_flag,
 CASE
WHEN d.call_return_first_flag IS NULL THEN
	0
ELSE
	d.call_return_first_flag
END AS call_return_first_flag,
 CASE
WHEN d.call_return_rule_flag IS NULL THEN
	0
ELSE
	d.call_return_rule_flag
END AS call_return_rule_flag,
 CASE
WHEN d.call_late_first_flag IS NULL THEN
	0
ELSE
	d.call_late_first_flag
END AS call_late_first_flag,
 CASE
WHEN d.call_late_rule_flag IS NULL THEN
	0
ELSE
	d.call_late_rule_flag
END AS call_late_rule_flag,
 CASE
WHEN d.call_first_first_flag IS NULL THEN
	0
ELSE
	d.call_first_first_flag
END AS call_first_first_flag,
 CASE
WHEN d.call_first_rule_flag IS NULL THEN
	0
ELSE
	d.call_first_rule_flag
END AS call_first_rule_flag,
 CASE
WHEN d.call_first_rule_flag2 IS NULL THEN
	0
ELSE
	d.call_first_rule_flag2
END AS call_first_rule_flag2
FROM
	queue_type a
INNER JOIN triage b ON a.triage_id = b.triage_id
INNER JOIN rlt_doctor2queue_type c ON a.queue_type_id = c.queue_type_id
LEFT JOIN pager d ON c.doctor_id = d.doctor_id
WHERE a.queue_type_id=#{queue_type_id}

selectTQrule3_2
===
SELECT
	b.call_buffer,
	b.return_flag_step,
	b.late_flag_step,
	b.first_flag_step,
	CASE
WHEN d.call_pass_first_flag IS NULL THEN
	0
ELSE
	d.call_pass_first_flag
END AS call_pass_first_flag,
 CASE
WHEN d.call_pass_rule_flag IS NULL THEN
	0
ELSE
	d.call_pass_rule_flag
END AS call_pass_rule_flag,
 CASE
WHEN d.call_return_first_flag IS NULL THEN
	0
ELSE
	d.call_return_first_flag
END AS call_return_first_flag,
 CASE
WHEN d.call_return_rule_flag IS NULL THEN
	0
ELSE
	d.call_return_rule_flag
END AS call_return_rule_flag,
 CASE
WHEN d.call_late_first_flag IS NULL THEN
	0
ELSE
	d.call_late_first_flag
END AS call_late_first_flag,
 CASE
WHEN d.call_late_rule_flag IS NULL THEN
	0
ELSE
	d.call_late_rule_flag
END AS call_late_rule_flag,
 CASE
WHEN d.call_first_first_flag IS NULL THEN
	0
ELSE
	d.call_first_first_flag
END AS call_first_first_flag,
 CASE
WHEN d.call_first_rule_flag IS NULL THEN
	0
ELSE
	d.call_first_rule_flag
END AS call_first_rule_flag,
 CASE
WHEN d.call_first_rule_flag2 IS NULL THEN
	0
ELSE
	d.call_first_rule_flag2
END AS call_first_rule_flag2
FROM
	queue_type a
INNER JOIN triage b ON a.triage_id = b.triage_id
LEFT JOIN rlt_pager2queue_type c ON a.queue_type_id = c.queue_type_id
LEFT JOIN pager d ON c.pager_id = d.id
WHERE
	a.queue_type_id =#{queue_type_id}
	
selectTQrule4
===
SELECT
	a.call_buffer,
	a.return_flag_step,
	a.late_flag_step,
	a.first_flag_step,
	b.call_pass_first_flag,
	b.call_pass_rule_flag,
	b.call_return_first_flag,
	b.call_return_rule_flag,
	b.call_late_first_flag,
	b.call_late_rule_flag,
	b.call_first_first_flag,
	b.call_first_rule_flag,
	b.call_first_rule_flag2
FROM
	triage a
INNER JOIN pager b ON a.triage_id = b.triage_id
WHERE
	b.id = #{id}
	
selectQueueTime
===
SELECT
	pass_flag_step,
	agin_flag_step,
	agin_first_flag,
	pass_first_flag,
	late_first_flag,
	late_flag_step
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
pass_first_flag=#{f_was},
late_flag_step= #{time_late},
late_first_flag=#{f_late}
WHERE
	queue_type_id =#{queue_type_id}

updatepagertime
===
UPDATE pager SET
call_return_first_flag=#{f_agin},
call_pass_first_flag=#{f_was},
call_late_first_flag=#{f_late},
call_first_first_flag=#{f_first}
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
	a.id,a.call_count,a.queue_type_id
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON b.doctor_id = c.doctor_id
WHERE
	a.state_patient = 51
AND c.ip =#{pager_ip}
and a.callerip=#{pager_ip}
AND b.login_id = #{login_id}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.opr_time asc


select_patient_pager_queue_id
===
SELECT
	a.id,a.call_count,a.queue_type_id
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON b.doctor_id = c.doctor_id
WHERE
	a.state_patient = 51
AND c.ip =#{pager_ip}
and a.callerip=#{pager_ip}
AND b.login_id = #{login_id}
AND a.is_display = 2
and a.is_deleted=0
ORDER BY
	a.opr_time asc

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
and a.callerip= #{pager_ip}
AND c.login_id = #{login_id}
AND a.state_patient = 51
AND a.is_display = 2
and a.is_deleted=0
order by a.opr_time asc

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
and a.callerip= #{pager_ip}
AND d.login_id=#{login_id}
AND a.state_patient=51
AND a.is_display=2
and a.is_deleted=0
order by a.opr_time asc


getMaxRegister2
===
SELECT  max(register_id2) as maxregister FROM patient_queue WHERE queue_type_id=#{queue_type_id} and state_patient2=#{state2}


SetPatientState_doctor_1
===
UPDATE patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
SET a.state_patient = 53,a.opr_time=#{opr_time},a.call_time=NOW(),a.late_lock=0
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
SET a.state_patient = 54,a.opr_time=#{opr_time},a.call_count=a.call_count+1,a.register_id2=#{register_id2},a.state_patient2=#{state_patient2},a.late_lock=0
WHERE
	a.id = #{patient_queue_id}
AND d.ip =#{pager_ip}
AND c.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_doctor_3
===
UPDATE patient_queue a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
INNER JOIN pager d ON d.doctor_id = c.doctor_id
SET a.state_patient = 1,a.opr_time=#{opr_time},a.call_time=NOW(),a.late_lock=0
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
SET a.state_patient = 53,a.opr_time=#{opr_time},a.call_time=NOW(),a.late_lock=0
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
SET a.state_patient = 54,a.opr_time=#{opr_time},a.late_lock=0
WHERE
	a.id = #{patient_queue_id} 
AND c.ip = #{pager_ip}
AND d.login_id = #{login_id}
AND a.is_display = 2

SetPatientState_pager_3
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
SET a.state_patient = 1,a.opr_time=#{opr_time},a.call_time=NOW(),a.late_lock=0
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
AND a.state_patient IN (0, 2, 3, 4, 5, 6, 7,8, 53,54)
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
AND a.state_patient IN (0, 2, 3, 4, 5, 6, 7,8, 53,54)
AND a.is_display = 2
and a.is_deleted=0

select_state_patient_doctor
===
SELECT
	a.id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.patient_id
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON c.doctor_id = b.doctor_id
WHERE
	a.state_patient = 51
AND a.is_display = 2
AND c.ip = #{pager_ip}
and a.callerip=#{pager_ip}
AND b.login_id = #{login_id}
and a.is_deleted=0
order by a.opr_time asc

select_state_patient_pager
===
SELECT
	a.id,
	a.patient_name,
	a.state_patient,
	a.time_interval,
	a.is_display,
	a.patient_id
FROM
	patient_queue a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN pager c ON c.doctor_id = b.doctor_id
WHERE
	a.state_patient = 51
AND a.is_display = 2
AND c.ip = #{pager_ip}
and a.callerip=#{pager_ip}
AND b.login_id = #{login_id}
and a.is_deleted=0
order by a.opr_time asc

list_patient_call4
===
select 
	a.id,
	a.register_id,
	a.patient_name,
	a.content,
	a.caller,
	b. NAME AS zsmc,
	b.displayname as zsmc_display,
	d. NAME AS jhqmc,
	d.display_name as jhqmc_display,
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
	b.displayname as zsmc_display,
	d. NAME AS jhqmc,
	d.display_name as jhqmc_display,
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
GROUP BY
	a.patient_source_code
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
	d. NAME AS zsmc,
	d.displayname as zsmc_display,
	b. NAME AS jhqmc,
	b.display_name as jhqmc_display,
	h.time_interval,
	h.is_display,
	h.register_id2,
	h.state_patient2
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
	b.displayname as zsmc_display,
	d. NAME AS jhqmc,
	d.display_name as jhqmc_display,
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
	b.display_name as zsmc_display,
	d. NAME AS jhqmc,
	d.displayname as jhqmc_display,
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
	a.is_display,
	a.register_id2,
	a.state_patient2
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
	a.is_display,
	a.content
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id
INNER JOIN pager d ON c.pager_id=d.id and a.callerip=d.ip
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
	b.name as queuetypename,
	b.displayname,
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
SET a.content = '2', a.caller = '2',a.call_time=NOW(),a.callerip=#{call},a.caller2=#{call},a.caller3=#{call},a.caller4=#{call}
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
			aa.state_patient IN (0, 2, 3, 4, 5, 6, 7,8, 50)
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
			aa.state_patient IN (0, 2, 3, 4, 5, 6, 7,8, 50)
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
			aa.state_patient IN (0, 2, 3, 4, 5, 6, 7,8, 50)
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
b.displayname as queue_type_displayname,
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
delete from patient_queue where datediff(CURRENT_DATE(),begin_time) >= 7;


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
 c.display_name as zsmc_display,
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
ORDER BY a.time_interval, (a.register_id+'') asc;

selectpatientwaitpager
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.patient_id,
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
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.late_lock DESC,
	(a.register_id + '') ASC
	
selectpatientwaitpagerbyoprtime
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.patient_id,
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
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.register_id2,
	a.opr_time
	
selectpatientwaitpagerbyoprtimelocked
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.patient_id,
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
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.ip = #{ip}
AND a.state_patient IN (0,2,3, 4,5, 6, 7,8,54)
AND a.is_display = '2'
AND a.is_deleted = 0
and a.late_lock=1
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.register_id2,
	a.opr_time

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
	

selectpatientwaitpagerId_lock
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
	
selectpatientpagerId_nodisplay
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
AND a.is_display = '1'
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
	a.patient_id,
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
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.late_lock DESC,
	a.opr_time asc
	
selectpatientwaitpagerId2locked
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.patient_id,
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
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.late_lock,
	a.register_id2,
	a.state_patient2
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{id}
AND a.state_patient IN (0,2,3, 4,5, 6, 7,8,54)
AND a.is_display = '2'
AND a.is_deleted = 0
and a.late_lock=1
GROUP BY
	c.ip,
	a.patient_source_code
ORDER BY
	a.time_interval,
	a.late_lock DESC,
	a.opr_time asc

selectpatientwaitpagerId2first
===
SELECT
	d.`name` AS doctorName,
	a.id,
	a.patient_id,
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
	c.display_name as zsmc_display,
	c.ip,
	a.opr_time,
	a.is_display,
	a.queue_type_id,
	a.patient_source_code,
	a.late_lock,
	a.register_id2,
	a.state_patient2
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
	a.register_id2,
	a.opr_time ASC

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
	c.display_name as zsmc_display,
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
 c.display_name as zsmc_display,
 c.ip,
	a.time_interval,
	a.is_display,
	a.call_count,
	a.queue_type_id
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

updatepatientGHbyDoorscan
===
UPDATE patient_queue
SET state_patient = 8,
 call_count = 0
WHERE
	LOCATE(#{code},patient_source_code)>0
AND queue_type_id =#{queue_type_id}

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

getmaxregisterbypager
===
SELECT
	max(a.register_id2) AS maxregister
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
INNER JOIN pager d ON d.id = c.pager_id
WHERE
	d.ip = #{ip}
AND a.state_patient2 = #{state2}

getmaxregisterbypagerid
===
SELECT
	max(a.register_id2) AS maxregister
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON c.queue_type_id = b.queue_type_id
INNER JOIN pager d ON d.id = c.pager_id
WHERE
	d.id = #{id}
AND a.state_patient2 = #{state2}


callupdatestatusbypager
===
UPDATE 
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id

SET a.state_patient = #{status},a.call_time=NOW(),a.content=#{call},a.caller=#{call},a.callerip=#{call},a.caller2=#{call},a.caller3=#{call},a.caller4=#{call}
WHERE
	a.patient_source_code=#{source_code}
AND c.ip = #{ip}

callupdatestatusbypager2
===
UPDATE 
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
SET a.state_patient = #{status},a.call_time=NOW(),a.content=#{call},a.caller=#{call},a.callerip=#{call},a.caller2=#{call},a.caller3=#{call},a.caller4=#{call},a.call_count=a.call_count+1,a.register_id2=#{register_id2},a.state_patient2=#{state_patient2}
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
	a.queue_type_id,
	(
		select count(b.id) from patient_queue b where b.queue_type_id=a.queue_type_id
	) as bycount
FROM
	rlt_doctor2queue_type a
WHERE
	a.doctor_id =#{doctor_id}
order by bycount desc

selectPatientBySourceCode
===
SELECT * FROM patient_queue where LOCATE(#{source_code}, patient_source_code) > 0 and queue_type_id=#{queue_type_id}

selectPatientBySourceCode2
===
SELECT * FROM patient_queue where LOCATE(#{source_code}, patient_source_code) > 0

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
	LOCATE(#{code}, a.patient_source_code) > 0
AND c.id = #{queue_type_id}
AND a.is_deleted = 0

patientBDByPager3
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
SET a.is_display = 2,state_patient=8
WHERE
	LOCATE(#{code}, a.patient_source_code) > 0
AND c.id = #{queue_type_id}
AND a.is_deleted = 0

patientBDByPager2
===
UPDATE patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
SET a.is_display = 2,a.register_id=#{rid}
WHERE
	LOCATE(#{code}, a.patient_source_code) > 0
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
	c.display_name as queue_type_displayname,
	d.print_type,
	a.register_id
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN triage d ON d.triage_id=c.triage_id
WHERE
	LOCATE(#{code}, a.patient_source_code) > 0
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
		AND a.state_patient IN (0, 2, 3, 4, 5, 6, 7,8, 50)
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
	c.display_name as zsmc_display,
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

selectpatientdoctorbyoprtime
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
	c.display_name as zsmc_display,
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
	a.opr_time

selectPatientbycodequeueid
===
SELECT
	*
FROM
	patient_queue
WHERE
	queue_type_id = #{queue_type_id} 
AND LOCATE(#{code}, patient_source_code) > 0
AND is_deleted = 0

selectPatientbyPagerIDAndCode
===
SELECT
	a.*
FROM
	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
WHERE
	c.id = #{id}
AND LOCATE(
	#{code},a.patient_source_code)>0
	AND a.is_deleted = 0
	GROUP BY
		a.patient_source_code

updatePatientbyPagerIDandCode
===
update 	patient_queue a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
LEFT JOIN doctor d ON a.doctor_id = d.doctor_id
SET a.state_patient=#{state},a.opr_time=#{opr_time},a.call_count=0,a.register_id2=#{register_id2},	a.state_patient2=#{state_patient2}
WHERE
	c.id = #{id}
AND LOCATE(#{code},a.patient_source_code)>0
	AND a.is_deleted = 0
 
 
getWaitQueue
===
 SELECT
	count(*)
FROM
	patient_queue
WHERE
	(register_id + '') < (
		SELECT
			(a.register_id + '')
		FROM
			patient_queue a
		WHERE
			a.queue_type_id = #{queue_type_id}
		AND LOCATE(
			#{code},
			a.patient_source_code
		) > 0
	)
AND queue_type_id = #{queue_type_id}
 
 
getPagerIsLogin
===
 SELECT
	a.*
FROM
	pager a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN rlt_doctor2queue_type c ON c.doctor_id = b.doctor_id
INNER JOIN patient_queue d ON d.queue_type_id = c.queue_type_id
WHERE
	d.id = #{ids}

getPagerIsLogin2
===
SELECT * from pager where id= #{id}
	
getFirstByQueutTypeID
===
SELECT * FROM patient_queue a WHERE a.queue_type_id= #{queue_type_id} and state_patient=5

updateFirstCallStatus
===
UPDATE pager SET call_first_rule_flag=#{v1} ,call_first_rule_flag2=#{v2} WHERE id=#{id}

getLateByQueutTypeID
===
SELECT * FROM patient_queue a WHERE a.queue_type_id= #{queue_type_id} and state_patient=8

updatePatientLateLock
===
UPDATE patient_queue SET late_lock=1,opr_time=#{time} WHERE patient_source_code=#{code} and late_lock=0 and queue_type_id=#{queue_type_id}

selectIsBegin_queue
===
SELECT * FROM patient_queue WHERE queue_type_id=#{id} and state_patient not in(0,5,8)

selectIsBegin_pager
===
SELECT
	a.*
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN pager d ON d.id = c.pager_id
WHERE
	a.state_patient NOT IN (0, 5,8)
AND d.ip =#{id}


checkpagercalling
===
SELECT
	d.id,
	d.register_id,
	d.patient_name,
	d.state_patient,
	d.time_interval,
	d.patient_source_code,
	d.is_display
FROM
	pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN patient_queue d ON c.queue_type_id = d.queue_type_id
WHERE
	a.ip = #{ip}
AND d.callerip = #{ip}
AND d.state_patient = 51
ORDER BY
	d.opr_time ASC

selectPassByTerForPager
===
SELECT
	d.`name`,
	d.display_name,
	a.patient_name,
	a.register_id
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN pager d ON c.pager_id = d.id
INNER JOIN rlt_pager2terminal e ON d.id = e.pager_id
INNER JOIN terminal f ON e.id = f.id
WHERE
	f.ip = #{ip}
AND a.state_patient = 1
AND a.is_display = 2
AND a.is_deleted = 0
GROUP BY
	d.id,
	a.patient_source_code
	
selectPassByTerForDoctor
===
SELECT
	d.`name`,
	d.display_name,
	a.patient_name,
	a.register_id
FROM
	patient_queue a
INNER JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN pager d ON c.doctor_id = d.doctor_id
INNER JOIN rlt_pager2terminal e ON d.id = e.pager_id
INNER JOIN terminal f ON e.id = f.id
WHERE
	f.ip = #{ip}
AND a.state_patient = 1
AND a.is_display = 2
AND a.is_deleted = 0

getReserveMaxNmb
===
SELECT max(reserve_id) as max_id FROM patient_queue_reserve WHERE queue_type_id=#{queue_type_id} and date_format(reserve_time, '%Y-%m-%d')=#{date}

insertReserve
===
insert patient_queue_reserve (queue_type_id,patient_id,patient_name,patient_source_code,reserve_id,reserve_time,opr_time,begin_time,end_time) VALUES(#{queue_type_id},#{patient_id},#{patient_name},#{patient_source_code},#{reserve_id},#{reserve_time},#{begin_time},#{end_time},now())

copyReservePatient
===
INSERT INTO patient_queue (
	queue_type_id,
	patient_id,
	patient_name,
	patient_source_code,
	register_id,
	fre_date,
	begin_time,
	last_time,
	queue_num,
	sub_queue_order,
	is_display
) SELECT
	queue_type_id,
	patient_id,
	patient_name,
	patient_source_code,
	reserve_id,
	reserve_time,
	begin_time,
	end_time,
	0,
	0,
	2
FROM
	patient_queue_reserve
WHERE
	date_format(reserve_time, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')