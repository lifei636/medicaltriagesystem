list
===
select * from rlt_doctor2queue_type

findDoctorPaiban
===
SELECT
	a.id,
	a.doctor_id,
	a.queue_type_id,
	a.onduty,
	a.is_custom,
	b.name AS doctorNmae,
	c.name AS queueTypeName,
	c.displayname as queueTypeDisplayName
FROM
	rlt_doctor2queue_type a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN queue_type c ON a.queue_type_id = c.queue_type_id
INNER JOIN triage d ON d.triage_id = c.triage_id
WHERE
	d.ip = #{ip}
	
findDoctorPaibanWeek
===
SELECT
	a.doctor_id,
	a.login_id,
	a.`name`,
	a.title,
	a.department,
	a.description,
	b.queue_type_id,
	b.onduty,
	SUBSTRING(b.onduty,#{week},1) as onduty2,
	c.displayname,
	b.lastno,
	b.other
FROM
 doctor a
INNER JOIN rlt_doctor2queue_type b ON b.doctor_id = a.doctor_id
INNER JOIN queue_type c on c.queue_type_id =b.queue_type_id
WHERE
SUBSTRING(b.onduty,#{week},1) <> '0' and b.onduty<>'0'
ORDER BY department,login_id

querydoctorpaiban	
===
SELECT
	a.id,
	a.doctor_id,
	a.queue_type_id,
	b.name AS doctorname,
	c.name AS queuetypename,
	c.displayname as queueTypeDisplayName
FROM
	rlt_doctor2queue_type a
INNER JOIN doctor b ON a.doctor_id = b.doctor_id
INNER JOIN queue_type c ON a.queue_type_id = c.queue_type_id
WHERE
	b.login_id =#{login_id}
	
paiban_week_doctor
===
SELECT * from doctor where FIND_IN_SET(login_id ,#{login_ids}) ORDER BY department

paiban_week_doctor_page
===
SELECT * from doctor where FIND_IN_SET(login_id ,#{login_ids}) ORDER BY department  limit #{begin}, #{end}
