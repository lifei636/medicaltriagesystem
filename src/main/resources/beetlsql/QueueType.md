list
===
SELECT
	queue_type_id,
	source_id,
	`name`,
	displayname,
	(CASE is_checkin WHEN '1' THEN '报道' WHEN '2' THEN '不报道' ELSE '错误' END ) AS is_checkin,
	(CASE is_pretriage WHEN '1' THEN '预分诊' WHEN '2' THEN '不预分诊' ELSE '错误' END ) AS is_pretriage,
	(CASE is_reorder WHEN '1' THEN '重新排号' WHEN '2' THEN '不重新排号' ELSE '错误' END ) AS is_reorder,
	(CASE is_ckin_order WHEN '1' THEN '报道重新排号' WHEN '2' THEN '报道不重新排号' ELSE '错误' END ) AS is_ckin_order,
	(CASE is_reserve WHEN '1' THEN '预约' WHEN '2' THEN '不预约' ELSE '错误' END ) AS is_reserve,
	reserve_numlist,floor
FROM
	queue_type

listQueueType
===
SELECT
	b.queue_type_id AS queue_number,
	b.`name` AS queue_name,
	b.displayname as queue_displayname,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (0, 2, 3, 4, 5, 6, 7, 8,50)
		AND z.queue_type_id = b.queue_type_id
		AND z.is_display = 2
		and z.is_deleted=0
	) AS wait,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (54)
		AND z.queue_type_id = b.queue_type_id
	) AS pass_no,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (51, 53)
		AND z.queue_type_id = b.queue_type_id
		and z.is_deleted=0
	) AS have_station,
	(
		SELECT
			z.patient_name
		FROM
			patient_queue z
		WHERE
			state_patient = 51
		AND z.queue_type_id = b.queue_type_id
		and z.is_deleted=0
		GROUP BY
			b.queue_type_id
	) AS current_visit,
	(
		SELECT
			v.`name`
		FROM
			doctor v
		LEFT JOIN patient_queue z ON v.doctor_id = z.doctor_id
		WHERE
			z.state_patient = 51
		AND z.queue_type_id = b.queue_type_id
		and z.is_deleted=0
		GROUP BY
			b.queue_type_id
	) AS current_doctor
FROM
	triage a
INNER JOIN queue_type b ON a.triage_id = b.triage_id
LEFT JOIN patient_queue c ON c.queue_type_id = b.queue_type_id
WHERE
	a.ip = #{ip}
GROUP BY
	b.queue_type_id
ORDER BY wait DESC


listQueueType2
===
SELECT
	b.queue_type_id AS queue_number,
	b.`name` AS queue_name,
	b.displayname AS queue_displayname,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (0, 2, 3, 4, 5, 6, 7, 8, 50)
		AND z.queue_type_id = b.queue_type_id
		AND z.is_display = 2
		AND z.is_deleted = 0
	) AS wait,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (54)
		AND z.queue_type_id = b.queue_type_id
	) AS pass_no,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (51, 53)
		AND z.queue_type_id = b.queue_type_id
		AND z.is_deleted = 0
	) AS have_station,
	(
		SELECT
			z.patient_name
		FROM
			patient_queue z
		WHERE
			state_patient = 51
		AND z.queue_type_id = b.queue_type_id
		AND z.is_deleted = 0
		GROUP BY
			b.queue_type_id
	) AS current_visit,
	(
		SELECT
			v.`name`
		FROM
			doctor v
		LEFT JOIN patient_queue z ON v.doctor_id = z.doctor_id
		WHERE
			z.state_patient = 51
		AND z.queue_type_id = b.queue_type_id
		AND z.is_deleted = 0
		GROUP BY
			b.queue_type_id
	) AS current_doctor
FROM
	triage a
INNER JOIN queue_type b ON a.triage_id = b.triage_id
LEFT JOIN patient_queue c ON c.queue_type_id = b.queue_type_id
INNER JOIN rlt_doctor2queue_type d ON d.queue_type_id = b.queue_type_id
AND SUBSTRING(
	d.onduty,
	CASE
WHEN date_format(now(), '%w') = 0 THEN
	7
ELSE
	date_format(now(), '%w')
END,
 1
) <> '0'
AND (
	(
		SUBSTRING(
			d.onduty,
			CASE
		WHEN date_format(now(), '%w') = 0 THEN
			7
		ELSE
			date_format(now(), '%w')
		END,
		1
		) = '1'
	)
	OR (
		CASE
		WHEN CONVERT (
			DATE_FORMAT(now(), '%H'),
			SIGNED
		) < 12 THEN
			SUBSTRING(
				d.onduty,
				CASE
			WHEN date_format(now(), '%w') = 0 THEN
				7
			ELSE
				date_format(now(), '%w')
			END,
			1
			) = '2'
		ELSE
			SUBSTRING(
				d.onduty,
				CASE
			WHEN date_format(now(), '%w') = 0 THEN
				7
			ELSE
				date_format(now(), '%w')
			END,
			1
			) = '3'
		END
	)
)
INNER JOIN doctor e ON d.doctor_id = e.doctor_id
WHERE
	a.ip = #{ip}
GROUP BY
	b.queue_type_id
ORDER BY
	wait DESC

listQueueType3
===
SELECT
	b.queue_type_id AS queue_number,
	b.`name` AS queue_name,
	b.displayname as queue_displayname,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (0, 2, 3, 4, 5, 6, 7, 8,50)
		AND z.queue_type_id = b.queue_type_id
		AND z.is_display = 2
		and z.is_deleted=0
	) AS wait,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (54)
		AND z.queue_type_id = b.queue_type_id
	) AS pass_no,
	(
		SELECT
			COUNT(*)
		FROM
			patient_queue z
		WHERE
			state_patient IN (51, 53)
		AND z.queue_type_id = b.queue_type_id
		and z.is_deleted=0
	) AS have_station,
	(
		SELECT
			z.patient_name
		FROM
			patient_queue z
		WHERE
			state_patient = 51
		AND z.queue_type_id = b.queue_type_id
		and z.is_deleted=0
		GROUP BY
			b.queue_type_id
	) AS current_visit,
	(
		SELECT
			v.`name`
		FROM
			doctor v
		LEFT JOIN patient_queue z ON v.doctor_id = z.doctor_id
		WHERE
			z.state_patient = 51
		AND z.queue_type_id = b.queue_type_id
		and z.is_deleted=0
		GROUP BY
			b.queue_type_id
	) AS current_doctor
FROM
	triage a
INNER JOIN queue_type b ON a.triage_id = b.triage_id
LEFT JOIN patient_queue c ON c.queue_type_id = b.queue_type_id
INNER JOIN rlt_pager2queue_type  d ON d.queue_type_id=b.queue_type_id
INNER JOIN pager f ON d.pager_id=f.id AND f.doctor_id is not null
WHERE
	a.ip = #{ip}
GROUP BY
	b.queue_type_id
ORDER BY wait DESC

listPager
===
SELECT
	a.id AS queue_number,
	a.`name` AS queue_name,
	a.display_name,
	(
		SELECT
			count(*)
		FROM
			(
				SELECT
					ac.ip
				FROM
					patient_queue aa
				INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
				INNER JOIN pager ac ON ab.pager_id = ac.id
				WHERE
					aa.state_patient IN (0, 2, 3, 4, 5, 6, 7, 8,50)
				AND aa.is_display = '2'
				AND aa.is_deleted = 0
				GROUP BY
					ac.ip,
					aa.patient_source_code
			) ta
		WHERE
			ta.ip = a.ip
	) AS wait,
	(
		SELECT
			count(*)
		FROM
			(
				SELECT
					ac.ip
				FROM
					patient_queue aa
				INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
				INNER JOIN pager ac ON ab.pager_id = ac.id
				WHERE
					aa.state_patient IN (54)
				AND aa.is_display = '2'
				AND aa.is_deleted = 0
				GROUP BY
					ac.ip,
					aa.patient_source_code
			) ta
		WHERE
			ta.ip = a.ip
	) AS pass_no,
	(
		SELECT
			count(*)
		FROM
			(
				SELECT
					ac.ip
				FROM
					patient_queue aa
				INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
				INNER JOIN pager ac ON ab.pager_id = ac.id
				WHERE
					aa.state_patient IN (53)
				AND aa.is_display = '2'
				AND aa.is_deleted = 0
				GROUP BY
					ac.ip,
					aa.patient_source_code
			) ta
		WHERE
			ta.ip = a.ip
	) AS have_station,
	(
		SELECT
			aa.patient_name
		FROM
			patient_queue aa
		INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
		INNER JOIN pager ac ON ab.pager_id = ac.id
		WHERE
			ac.ip = a.ip
		AND aa.state_patient IN (51)
		AND aa.content=a.ip
		AND aa.is_display = '2'
		AND aa.is_deleted = 0
		GROUP BY
			ac.ip,
			aa.patient_source_code
	) AS current_visit,
	c.`name` AS current_doctor
FROM
	pager a
INNER JOIN triage b ON a.triage_id = b.triage_id
LEFT JOIN doctor c ON a.doctor_id = c.doctor_id
WHERE
	b.ip = #{ip}
ORDER BY wait DESC


listPager2
===
SELECT
	a.id AS queue_number,
	a.`name` AS queue_name,
	a.display_name,
	(
		SELECT
			count(*)
		FROM
			(
				SELECT
					ac.ip
				FROM
					patient_queue aa
				INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
				INNER JOIN pager ac ON ab.pager_id = ac.id
				WHERE
					aa.state_patient IN (0, 2, 3, 4, 5, 6, 7, 8,50)
				AND aa.is_display = '2'
				AND aa.is_deleted = 0
				GROUP BY
					ac.ip,
					aa.patient_source_code
			) ta
		WHERE
			ta.ip = a.ip
	) AS wait,
	(
		SELECT
			count(*)
		FROM
			(
				SELECT
					ac.ip
				FROM
					patient_queue aa
				INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
				INNER JOIN pager ac ON ab.pager_id = ac.id
				WHERE
					aa.state_patient IN (54)
				AND aa.is_display = '2'
				AND aa.is_deleted = 0
				GROUP BY
					ac.ip,
					aa.patient_source_code
			) ta
		WHERE
			ta.ip = a.ip
	) AS pass_no,
	(
		SELECT
			count(*)
		FROM
			(
				SELECT
					ac.ip
				FROM
					patient_queue aa
				INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
				INNER JOIN pager ac ON ab.pager_id = ac.id
				WHERE
					aa.state_patient IN (53)
				AND aa.is_display = '2'
				AND aa.is_deleted = 0
				GROUP BY
					ac.ip,
					aa.patient_source_code
			) ta
		WHERE
			ta.ip = a.ip
	) AS have_station,
	(
		SELECT
			aa.patient_name
		FROM
			patient_queue aa
		INNER JOIN rlt_pager2queue_type ab ON aa.queue_type_id = ab.queue_type_id
		INNER JOIN pager ac ON ab.pager_id = ac.id
		WHERE
			ac.ip = a.ip
		AND aa.state_patient IN (51)
		AND aa.content=a.ip
		AND aa.is_display = '2'
		AND aa.is_deleted = 0
		GROUP BY
			ac.ip,
			aa.patient_source_code
	) AS current_visit,
	c.`name` AS current_doctor
FROM
	pager a
INNER JOIN triage b ON a.triage_id = b.triage_id
LEFT JOIN doctor c ON a.doctor_id = c.doctor_id
WHERE
	b.ip = #{ip} AND a.doctor_id is not null
ORDER BY wait DESC



listPagerLogin
===
SELECT
	a.id AS queue_type_id,
	a.`name`,
	a.display_name,
	d.login_id,
	a.ip
FROM
	pager a
LEFT JOIN doctor d ON d.doctor_id = a.doctor_id
WHERE
	a.ip =#{ip}
AND a.doctor_id <> ''

listPagerLogin2
===
SELECT
	a.id AS queue_type_id,
	a.`name`,
	a.display_name,
	a.display_name AS pager_displayname,
	a.`name` AS queue_type_name,
	a.display_name AS queue_type_displayname,
	a.`name` AS pager_name,
	d.login_id,
	a.ip,
	e.patient_name,
	e.register_id,
	e.state_patient
FROM
	pager a
INNER JOIN rlt_pager2terminal b ON a.id = b.pager_id
INNER JOIN terminal c ON c.id = b.id
INNER JOIN doctor d ON d.doctor_id = a.doctor_id
LEFT JOIN patient_queue e ON e.callerip = a.ip
AND e.state_patient = 51
WHERE
	c.ip = #{ip}
AND a.doctor_id <> ''
GROUP BY
	e.patient_source_code
ORDER BY a.id	

listPagerLogin2_all
===
SELECT
	a.id AS queue_type_id,
	a.`name`,
	a.display_name,
	a.display_name AS pager_displayname,
	a.`name` AS queue_type_name,
	a.display_name AS queue_type_displayname,
	a.`name` AS pager_name,
	d.login_id,
	a.ip,
	e.patient_name,
	e.register_id,
	e.state_patient
FROM
	pager a
INNER JOIN rlt_pager2terminal b ON a.id = b.pager_id
INNER JOIN terminal c ON c.id = b.id
INNER JOIN doctor d ON d.doctor_id = a.doctor_id
LEFT JOIN patient_queue e ON e.callerip = a.ip
AND e.state_patient = 51
WHERE
	c.ip = #{ip}
AND a.doctor_id <> ''
GROUP BY
	e.patient_source_code
ORDER BY a.id	



FristPagerType
===
SELECT
	a.queue_type_id
FROM
	rlt_pager2queue_type a
INNER JOIN pager b ON a.pager_id = b.id
WHERE
	b.id = #{id}

listQueueTypeName
===
SELECT
	queue_type.queue_type_id,
	queue_type. NAME
FROM
	queue_type
INNER JOIN triage ON queue_type.triage_id = triage.triage_id
INNER JOIN patient_queue c on c.queue_type_id=queue_type.queue_type_id
WHERE
	triage.ip = #{ip}

getQueueTypeById
===
SELECT * FROM queue_type where FIND_IN_SET(queue_type_id,#{queue_type_id})

list_queue_type_wait_call
===
SELECT
	f.queue_type_id,
	f.`name`,
	f.`name` AS queue_type_name,
	f.displayname AS queue_type_displayname,
	c.display_name,
	c.`name` AS pager_name,
	c.display_name AS pager_displayname,
	c.id,
	c.ip,
	h.patient_name,
	h.register_id,
	h.content,
	h.id as patient_id

FROM
	terminal a
INNER JOIN rlt_pager2terminal b ON a.id = b.id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN doctor d ON c.doctor_id = d.doctor_id
INNER JOIN rlt_doctor2queue_type e ON d.doctor_id = e.doctor_id
INNER JOIN queue_type f ON e.queue_type_id = f.queue_type_id
INNER JOIN patient_queue h ON f.queue_type_id = h.queue_type_id
WHERE
	a.ip = #{ip}
AND h.is_deleted = 0
AND is_display = 2
AND h.state_patient IN (51)
AND h.content is not null AND h.content<>'0'
ORDER BY h.opr_time asc

list_queue_type_wait_call_pager
===
SELECT
	a.queue_type_id,
	a.`name`,
	a.`name` AS queue_type_name,
	a.displayname as queue_type_displayname,
	c.display_name,
	c.`name` AS pager_name,
	c.display_name AS pager_displayname,
	f.login_id,
	c.ip,
	c.id,
	h.id as patient_id
FROM
	queue_type a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN rlt_pager2terminal d ON c.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON c.doctor_id = f.doctor_id
INNER JOIN patient_queue h ON h.queue_type_id=a.queue_type_id
WHERE
	e.ip = #{ip}
AND c.doctor_id <> ""	and
h.state_patient=51
AND h.content is not null AND h.content<>'0'
ORDER BY c.id

listPagerLogin2_pager
===
SELECT
	a.id AS queue_type_id,
	a.`name`,
	a.display_name,
	a.display_name AS pager_displayname,
	a.`name` AS queue_type_name,
	a.display_name AS queue_type_displayname,
	a.`name` AS pager_name,
	d.login_id,
	a.ip,
	e.patient_name,
	e.register_id,
	e.state_patient,
	a.id as patient_id
FROM
	pager a
INNER JOIN rlt_pager2terminal b ON a.id = b.pager_id
INNER JOIN terminal c ON c.id = b.id
INNER JOIN doctor d ON d.doctor_id = a.doctor_id
INNER JOIN patient_queue e ON e.callerip = a.ip
AND e.state_patient = 51 AND e.content is not null and e.content<>'0'
WHERE
	c.ip = #{ip}
AND a.doctor_id <> ''
GROUP BY
	e.patient_source_code
ORDER BY a.id	


list_queue_type
===
SELECT
	*
FROM
	(
		SELECT
			f.queue_type_id,
			f.`name`,
			f.`name` AS queue_type_name,
			f.displayname AS queue_type_displayname,
			c.display_name,
			c.`name` AS pager_name,
			c.display_name AS pager_displayname,
			c.id,
			c.ip,
			(
				SELECT
					COUNT(h.patient_id)
				FROM
					patient_queue h
				WHERE
					h.queue_type_id = f.queue_type_id
				AND h.is_deleted = 0
				AND is_display = 2
				AND h.state_patient IN (0, 2, 3, 4, 6, 7, 8, 50, 51)
			) AS bycount
		FROM
			terminal a
		INNER JOIN rlt_pager2terminal b ON a.id = b.id
		INNER JOIN pager c ON b.pager_id = c.id
		INNER JOIN doctor d ON c.doctor_id = d.doctor_id
		INNER JOIN rlt_doctor2queue_type e ON d.doctor_id = e.doctor_id
		INNER JOIN queue_type f ON e.queue_type_id = f.queue_type_id
		WHERE
			a.ip =#{ip}
	) t
WHERE
	t.bycount > 0
ORDER BY
	t.id ASC,
	t.bycount DESC


list_queue_type_all
===
SELECT
	*
FROM
	(
		SELECT
			f.queue_type_id,
			f.`name`,
			f.`name` AS queue_type_name,
			f.displayname AS queue_type_displayname,
			c.display_name,
			c.`name` AS pager_name,
			c.display_name AS pager_displayname,
			c.id,
			c.ip,
			(
				SELECT
					COUNT(h.patient_id)
				FROM
					patient_queue h
				WHERE
					h.queue_type_id = f.queue_type_id
				AND h.is_deleted = 0
				AND is_display = 2
				AND h.state_patient IN (0, 2, 3, 4, 6, 7, 8, 50, 51)
			) AS bycount
		FROM
			terminal a
		INNER JOIN rlt_pager2terminal b ON a.id = b.id
		INNER JOIN pager c ON b.pager_id = c.id
		INNER JOIN doctor d ON c.doctor_id = d.doctor_id
		INNER JOIN rlt_doctor2queue_type e ON d.doctor_id = e.doctor_id
		INNER JOIN queue_type f ON e.queue_type_id = f.queue_type_id
		WHERE
			a.ip =#{ip}
	) t

ORDER BY
	t.id ASC,
	t.bycount DESC
	
list_queue_type3
===

SELECT
	d.queue_type_id,
	d.`name`,
	c.display_name,
	COUNT(f.queue_type_id) AS bycount
FROM
	doctor a
INNER JOIN pager b ON a.doctor_id = b.doctor_id
INNER JOIN rlt_doctor2queue_type c ON c.doctor_id = a.doctor_id
INNER JOIN queue_type d ON d.queue_type_id = c.queue_type_id
INNER JOIN triage e ON e.triage_id = d.triage_id
INNER JOIN patient_queue f ON f.queue_type_id = d.queue_type_id
WHERE
	e.ip = #{ip}
	and f.state_patient IN (0,2,3,4,6,7,8,50,51)
GROUP BY
	f.queue_type_id
ORDER BY
	bycount DESC

list_queue_type2
===

SELECT
	d.queue_type_id,
	d. NAME,
	d.displayname
FROM
	doctor a
INNER JOIN pager b ON a.doctor_id = b.doctor_id
INNER JOIN rlt_doctor2queue_type c ON c.doctor_id = a.doctor_id
INNER JOIN queue_type d ON d.queue_type_id = c.queue_type_id
INNER JOIN triage e ON e.triage_id = d.triage_id
where e.ip=#{ip}

list_queue_type_pager
===
SELECT
	a.queue_type_id,
	a.`name`,
	a.`name` AS queue_type_name,
	a.displayname as queue_type_displayname,
	c.display_name,
	c.`name` AS pager_name,
	c.display_name AS pager_displayname,
	f.login_id,
	c.ip,
	c.id
FROM
	queue_type a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN rlt_pager2terminal d ON c.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON c.doctor_id = f.doctor_id
WHERE
	e.ip = #{ip}
AND c.doctor_id <> ""
ORDER BY c.id	

list_queue_type_pager_all
===
SELECT
	a.queue_type_id,
	a.`name`,
	a.`name` AS queue_type_name,
	a.displayname as queue_type_displayname,
	c.display_name,
	c.`name` AS pager_name,
	c.display_name AS pager_displayname,
	f.login_id,
	c.ip,
	c.id
FROM
	queue_type a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN rlt_pager2terminal d ON c.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON c.doctor_id = f.doctor_id
WHERE
	e.ip = #{ip}
AND c.doctor_id <> ""
ORDER BY c.id	

list_queue_type_pager_jz
===
SELECT
	a.queue_type_id,
	c.`name`,
	c.display_name AS pager_displayname,
	f.login_id,
	c.ip,
	h.patient_name,
	h.register_id,
	h.state_patient
FROM
	queue_type a
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON b.pager_id = c.id
INNER JOIN rlt_pager2terminal d ON c.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON c.doctor_id = f.doctor_id
LEFT JOIN patient_queue h ON c.ip = h.callerip
AND h.state_patient = 51
WHERE
	e.ip = #{ip}
AND c.doctor_id <> ""
GROUP BY h.patient_source_code
ORDER BY c.display_name
	
list_doctor_queuetype
===	
SELECT
	a.queue_type_id,
	a.displayname as queue_type_displayname,
	CONCAT( a.name,'(',(select count(d.id) from patient_queue d where d.queue_type_id=b.queue_type_id ),')') as name,
	(select count(d.id) from patient_queue d where d.queue_type_id=b.queue_type_id ) as bycount
FROM
	queue_type a
INNER JOIN rlt_doctor2queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN doctor c ON c.doctor_id = b.doctor_id
WHERE
	c.login_id = #{login_id}
ORDER BY bycount DESC
	
list_pager_queuetype
===	
SELECT 
	a.queue_type_id,
	a.name,
	a.displayname as queue_type_displayname FROM queue_type a 
INNER JOIN rlt_pager2queue_type b ON a.queue_type_id=b.queue_type_id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON d.doctor_id=c.doctor_id
where
d.login_id=#{login_id}
AND c.ip=#{ip}

showloginupdate
===
UPDATE pager SET doctor_id=#{doctor_id},show_login_time=NOW() where ip=#{ip}

showloginupdateout
===
UPDATE pager SET doctor_id=null,show_login_time=NOW() where ip=#{ip}


showcallupdate
===
UPDATE pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON c.queue_type_id = b.queue_type_id
SET a.show_visiting = #{show_visiting},
a.show_visiting_nmb=#{show_visiting_nmb},
c.show_wait = #{show_wait},
a.show_call1 = 2,
 a.show_call2 = 2,
 a.show_call3 = 2,
 a.show_call4 = 2,
 a.show_call_time = NOW()
WHERE
	c.source_id = #{source_id}
AND a.doctor_id = #{doctor_id}
	
show_login
===
SELECT
	f.*, a.show_login_time,
	a.show_call_time,
	c.show_wait,
	a.`name` AS pager_name,
	a.display_name AS page_display_name,
	c.`name` AS queue_type_name,
	c.displayname AS queue_type_displayname,
	e.`name` AS terminal_name,
	e.display_name AS terminal_display_name
FROM
	pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN rlt_pager2terminal d ON a.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON a.doctor_id = f.doctor_id
WHERE
	e.ip =#{ip}
	
show_call_door
===
SELECT
	f.`name`,
	a.id,
	a.show_visiting,
	a.show_visiting_nmb,
	a.show_call_time,
	a.show_login_time,
	a.show_call1,
	c.show_wait,
	a.`name` AS pager_name,
	a.display_name AS page_display_name,
	c.`name` AS queue_type_name,
	c.displayname AS queue_type_displayname,
	e.`name` AS terminal_name,
	e.display_name AS terminal_display_name
FROM
	pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN rlt_pager2terminal d ON a.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON a.doctor_id = f.doctor_id
WHERE
	e.ip =#{ip}
	
show_call_hall
===
SELECT
	f.`name`,
	a.id,
	a.show_visiting,
	a.show_visiting_nmb,
	a.show_call_time,
	a.`name` AS pager_name,
	a.display_name AS page_display_name,
	c.`name` AS queue_type_name,
	c.displayname AS queue_type_displayname,
	e.`name` AS terminal_name,
	e.display_name AS terminal_display_name
FROM
	pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN rlt_pager2terminal d ON a.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON a.doctor_id = f.doctor_id
WHERE
	e.ip = #{ip}
AND a.show_call2 = 2
ORDER BY
	a.show_call_time
	
show_call_hall_type
===
SELECT
	f.`name`,
	a.id,
	a.show_visiting,
	a.show_visiting_nmb,
	a.show_call_time,
	a.show_login_time,
	a.show_call2,
	c.show_wait,
	a.`name` AS pager_name,
	a.display_name AS pager_display_name,
	c.`name` AS queue_type_name,
	c.displayname AS queue_type_displayname,
	e.`name` AS terminal_name,
	e.display_name AS terminal_display_name
FROM
	pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON b.queue_type_id = c.queue_type_id
INNER JOIN rlt_pager2terminal d ON a.id = d.pager_id
INNER JOIN terminal e ON e.id = d.id
INNER JOIN doctor f ON a.doctor_id = f.doctor_id
WHERE
	e.ip =#{ip}

showupdaecalled_door
===
UPDATE pager SET show_call1=1 where id=#{id}

showupdaecalled_hall
===
UPDATE pager SET show_call2=1 where FIND_IN_SET(id,#{ids})

showupdatequeuetype_select
===
SELECT source_id,name,displayname from queue_type where source_id=#{source_id}

showupdatequeuetype_insert
===
INSERT INTO queue_type (triage_id, source_id,name,displayname,opr_time) VALUES(1, #{source_id},#{name},#{displayname},now())

showupdatequeuetype_update
===
UPDATE queue_type SET name=#{name},displayname=#{displayname} WHERE source_id=#{source_id}

showupdatepager_select
===
SELECT ip,name,display_name from pager where ip=#{ip}

showupdatepager_insert
===
INSERT INTO pager (triage_id, ip,name,display_name) VALUES(1, #{ip},#{name},#{displayname})

showupdatepager_update
===
UPDATE pager SET name=#{name},display_name=#{displayname} WHERE ip=#{ip}

showupdateterminal_select
===
SELECT ip,name,display_name from terminal where ip=#{ip}

showupdateterminal_insert
===
INSERT INTO terminal (triage_id, ip,name,display_name) VALUES(1, #{ip},#{name},#{displayname})

showupdateterminal_update
===
UPDATE terminal SET name=#{name},display_name=#{displayname} WHERE ip=#{ip}

showupdatepager2queue_type_select
===
SELECT pager_id,queue_type_id from rlt_pager2queue_type where pager_id=#{pager_id} and queue_type_id=#{queue_type_id}

showupdatepager2queue_type_insert
===
INSERT INTO rlt_pager2queue_type (pager_id, queue_type_id,onduty) VALUES(#{pager_id},#{queue_type_id},'1111111')

showupdatepager2queue_type_del
===
DELETE rlt_pager2queue_type WHERE queue_type_id=#{queue_type_id} AND pager_id=#{pager_id}

showupdatepager2terminal_select
===
SELECT pager_id,id from rlt_pager2terminal where pager_id=#{pager_id} and id=#{id}

showupdatepager2terminal_insert
===
INSERT INTO rlt_pager2terminal (pager_id, id) VALUES(#{pager_id},#{id})

showupdatepager2terminal_del
===
DELETE rlt_pager2terminal WHERE id=#{id} AND pager_id=#{pager_id}

showupdatepager2queue_type_list
===
SELECT
	a.ip,
	c.source_id,
	c.`name`,
	c.displayname
FROM
	pager a
INNER JOIN rlt_pager2queue_type b ON a.id = b.pager_id
INNER JOIN queue_type c ON c.queue_type_id = b.queue_type_id

showupdatepager2terminal_list
===
SELECT
	a.ip,
	c.ip AS t_ip,
	c.`name`,
	c.display_name
FROM
	pager a
INNER JOIN rlt_pager2terminal b ON a.id = b.pager_id
INNER JOIN terminal c ON c.id = b.id

ResetCallStatus
===
UPDATE queue_type SET pass_first_flag=0,pass_flag_step=0,agin_first_flag=0,agin_flag_step=0,late_first_flag=0,late_flag_step=0 where queue_type_id=#{queue_type_id}