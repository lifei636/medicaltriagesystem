list
===
SELECT
	a.id,
	a.triage_id,
	(
		SELECT
			NAME
		FROM
			triage b
		WHERE
			b.triage_id = a.triage_id
	) AS fztname,
	a. NAME AS tname,
	a.display_name,
	a.ip
FROM
	terminal a

findbytriageip
===
SELECT
	b.ip
FROM
	terminal a
INNER JOIN triage b ON a.triage_id = b.triage_id
WHERE
	a.ip = #{ip}
	
getpageripbyterip
===
SELECT
	a.ip
FROM
	pager a
INNER JOIN rlt_pager2terminal b ON b.pager_id = a.id
INNER JOIN terminal c ON b.id = c.id
WHERE
	c.ip =#{ip}
	
addterminal
===
INSERT terminal(ip,name,display_name) VALUES(#{ip},#{name},#{display_name})

edittriage
===
UPDATE terminal SET triage_id = #{id} WHERE  FIND_IN_SET( id , #{ids})
	