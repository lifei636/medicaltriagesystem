list
===
select a.id,b.description as sourcename,a.`name`,a.sqlstring,a.Search_fields,a.description,a.`interval`,a.last_time from item a INNER JOIN souce b ON a.db_source_id=b.id