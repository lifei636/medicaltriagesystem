list
===
SELECT
	triage_id,
	(CASE triage_type WHEN '1' THEN '按医生' WHEN '2' THEN '按叫号器' ELSE '错误' END ) AS triage_type,
	(CASE pager_type WHEN '0' THEN '顺呼+选呼模式' ELSE '错误' END ) AS pager_type,
	call_buffer,
	name,
	ip,
	description,
	create_time,
	(CASE reorder_type WHEN '1' THEN '按队列排号' WHEN '2' THEN '按叫号器排号' ELSE '错误' END ) AS reorder_type,
	(CASE print_type WHEN '1' THEN '手动打印'  when '2' then '自动打印' ELSE '错误' END ) AS print_type,
	return_flag_step,
	triage_pwd
FROM
	triage

set_auto_increment
===
alter table triage auto_increment=1

queryByQueueType
===
SELECT b.queue_type_id,b.name,b.displayname from triage a INNER JOIN queue_type b on a.triage_id=b.triage_id where a.ip=#{ip}

findtypelogin_id
===
SELECT
	a.triage_type,a.pass_time
FROM
	triage a
INNER JOIN pager b ON a.triage_id = b.triage_id
INNER JOIN doctor c ON b.doctor_id = c.doctor_id
WHERE
	c.login_id =#{login_id}
	
findbypagerip	
===
SELECT b.* FROM pager a INNER JOIN triage b ON a.triage_id=b.triage_id WHERE a.ip=#{triageIp}

findtriageBytemIP
===
SELECT
	*
FROM
	triage a
INNER JOIN terminal b ON a.triage_id = b.triage_id
WHERE
	b.ip =#{ip}
