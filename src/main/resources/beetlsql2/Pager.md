list
===
SELECT
	a.id,
	a.triage_id,
	b.`name` as triage_name,
	a.`name`,
	a.display_name,
	a.ip,
	(
		CASE a.type
		WHEN '0' THEN
			'虚拟叫号器'
		ELSE
			'错误'
		END
	) AS type,
	a.description,
	b.triage_type
FROM
	pager a
INNER JOIN triage b ON a.triage_id = b.triage_id
	
updatedoctorid
===	
UPDATE pager a
LEFT JOIN doctor b ON a.doctor_id = b.doctor_id
SET a.doctor_id = NULL,
a.call_pass_first_flag=0,
a.call_pass_rule_flag=0,
a.call_return_first_flag=0,
a.call_return_rule_flag=0
WHERE
	a.ip <> #{pager_ip}
AND b.login_id =#{login_id}

call_login
===
SELECT
	*
FROM
	pager a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
WHERE
	b.login_id =#{login_id}
	
	
callpagername	
===
SELECT
	c.queue_type_id,
	c. NAME
FROM
pager b
	
INNER JOIN rlt_pager2queue_type a ON a.pager_id = b.id
INNER JOIN queue_type c ON c.queue_type_id = a.queue_type_id
WHERE
	b.ip = #{ip}
	
logout
===
UPDATE pager a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
SET a.doctor_id = NULL,
a.call_pass_first_flag=0,
a.call_pass_rule_flag=0,
a.call_return_first_flag=0,
a.call_return_rule_flag=0
WHERE
	a.ip =#{pager_ip}
AND b.login_id =#{login_id}

updatepagerinsert
===
UPDATE pager
SET doctor_id = (
	SELECT
		doctor_id
	FROM
		doctor
	WHERE
		login_id = #{login_id}),
login_time=now(),
call_pass_first_flag=0,
call_pass_rule_flag=0,
call_return_first_flag=0,
call_return_rule_flag=0
WHERE
		ip=#{ip}

