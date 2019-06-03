docInsert
===
insert into third_doctor (doctor_id,login_id,name,department,title,description,opr_time) values(#{doctor_id},#{login_id},#{name},#{department},#{title},#{description},now())

docUpdate
===
UPDATE third_doctor SET login_id=#{login_id},name=#{name}, opr_time=now(),title=#{title}, department=#{department},description=#{description} WHERE doctor_id=#{doctor_id}

dpmtInsert
===
insert into third_department (souce_id,name,displayname,opr_time) values(#{souce_id},#{name},#{displayname},now())

dpmtUpdate
===
UPDATE third_department SET name=#{name}, opr_time=now(),displayname=#{displayname} WHERE souce_id=#{souce_id}

docPBInsert
===
INSERT INTO third_doctor_pd (pb_id, doctor_id, name, title, title2, department, department2, begin_time, end_time, totalNmb, remainNmb, opr_time,is_stop) VALUES (#{pb_id}, #{doctor_id}, #{name}, #{title}, #{title2}, #{department}, #{department2}, #{begin_time}, #{end_time}, #{totalNmb}, #{remainNmb}, now(),#{is_stop});

docPBUpdate
===
update third_doctor_pd set doctor_id=#{doctor_id}, name=#{name}, title=#{title}, title2=#{title2}, department=#{department}, department2=#{department2}, begin_time=#{begin_time}, end_time=#{end_time}, totalNmb=#{totalNmb}, remainNmb=#{remainNmb}, opr_time=now(),is_stop=#{is_stop} where pb_id=#{pb_id}


patientInsert
===
INSERT INTO third_patient_queue (patient_id, patient_name, queue_type_source_id, source_code, register_id, queue_num, time_interval, doctor_source_id, is_deleted,begin_time,end_time, fre_date) VALUES (#{patient_id}, #{patient_name}, #{queue_type_source_id}, #{source_code}, #{register_id}, #{queue_num}, #{time_interval}, #{doctor_source_id}, #{is_deleted},#{begin_time},#{end_time}, #{fre_date});



patientUpdate
===
update third_patient_queue set patient_name=#{patient_name}, queue_type_source_id=#{queue_type_source_id}, source_code=#{source_code}, register_id=#{register_id}, queue_num=#{queue_num}, time_interval=#{time_interval}, doctor_source_id=#{doctor_source_id}, is_deleted=#{is_deleted}, begin_time=#{begin_time},end_time=#{end_time},fre_date=#{fre_date} where patient_id=#{patient_id};
