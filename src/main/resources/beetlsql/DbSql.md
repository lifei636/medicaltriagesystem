list
===
SELECT
	a.id,
	b.description,
	(
		CASE a.type
		WHEN '1' THEN
			'医生信息'
		WHEN '2' THEN
			'队列信息'
		WHEN '3' THEN
			'医生与队列关系'
		WHEN '4' THEN
			'患者挂号信息'
		ELSE
			'错误'
		END
	) AS type,
	a.outsql,
	a.interval,
	a.last_time
FROM
	db_sql a
INNER JOIN data_connection b ON a.db_source_id = b.id