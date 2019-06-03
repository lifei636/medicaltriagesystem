list
===
select * from sync_data_log order by id desc

delete
===
truncate table sync_data_log


InsertLog
===
insert into sync_data_log (log_name,log_msg,log_exception,log_time,log_type) values (#{log_name},#{log_msg},#{log_exception},now(),#{log_type})