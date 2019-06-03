list
===
SELECT * FROM doctor

calldoctorname
===
SELECT
	c.queue_type_id,
	c. name,
	c.displayname,
	c.source_id,
	(select count(d.id) from patient_queue d where d.queue_type_id=c.queue_type_id ) as bycount
FROM
	doctor a
INNER JOIN rlt_doctor2queue_type b ON b.doctor_id = a.doctor_id
INNER JOIN queue_type c ON b.queue_type_id = c.queue_type_id
WHERE
	a.login_id = #{login_id}
ORDER BY bycount desc

queryByip
===
SELECT 
	d.doctor_id,
	d.login_id,
	c.display_name AS zsmc,
	c.name as zsmc2,
	c.ip,
	c.id,
	d.name,
	d.title,
	d.department,
	d.description,
	d.call_rule FROM terminal a INNER JOIN rlt_pager2terminal b ON a.id=b.id
INNER JOIN pager c ON c.id=b.pager_id
INNER JOIN doctor d ON c.doctor_id=d.doctor_id
WHERE
	a.ip  = #{ip}
	
