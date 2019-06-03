list
===
select * from rlt_pager2terminal

findByIP
===
SELECT
	c.ip
FROM
	rlt_pager2terminal a
INNER JOIN terminal b ON a.id = b.id
INNER JOIN pager c ON a.pager_id = c.id
WHERE
	b.ip = #{ip}