list
===
select * from rlt_pager2queue_type

pager_paiban
===
SELECT
	*
FROM
	rlt_pager2queue_type a
LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id
INNER JOIN pager c ON c.id = a.pager_id
INNER JOIN triage d ON d.triage_id = b.triage_id
WHERE
	d.ip = #{ip}
	

querypagerpaiban
===
SELECT
	*
FROM
	rlt_pager2queue_type a
INNER JOIN pager b ON a.pager_id = b.id
INNER JOIN queue_type c ON a.queue_type_id = c.queue_type_id
INNER JOIN doctor d ON b.doctor_id = d.doctor_id
WHERE
	d.login_id =#{login_id}