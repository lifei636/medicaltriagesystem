list
===
select id,dbType,username,`password`,description,dbName,url,(CASE err_type WHEN '1' THEN '出错继续同步'  when '2' then '出错停止同步' ELSE '错误' END ) as err_type,(CASE doc_info WHEN '1' THEN '首次同步'  when '2' then '每次同步' ELSE '错误' END )  AS doc_info from data_connection