//调用load方法
//根据分诊台查询有效患者
$("#duilie").on('click',".queue_type_id",function() {
	var id = $(this).attr('data-id');
	$("#patientList").html("");//清空数据
	$("#checkall").removeAttr("checked");
	$.post("/patient/list_patient",{id : id},function(data) {
		$.each(data,function(index,ele) {
			var list = "<tr><td><input class='patientcheck' type='checkbox' name='patientid' lay-skin='line' value='"
				+ ele.id
				+ "'/></td><td>"
				+ ele.queue_num
				+ "</td><td>"
				+ ele.patient_name
				+ "</td><td>"
				+ ele.doctorName
				+ "</td><td>"
				+ ele.state_patient
				+ "</td><td>"
				+ ele.state_custom
				+ "</td><td>"
				+ ele.time_interval
				+ "</td><td>"
				+ ele.sub_queue_type
				+ "</td><td>"
				+ ele.fre_date
				+ "</td><td>"
				+ ele.call_time
				+ "</td></tr>";
		$("#patientList").append(list);
		});
	}, 'json');
});
//根据分诊台查询未到过号患者
$("#duilie").on('click',".queue_type_id",function() {
	var id = $(this).attr('data-id');
	$("#patientPassto").html("");//清空数据
	$("#checkall").removeAttr("checked");
	$.post("/patient/list_patient_passto",{id : id},function(data) {
		$.each(data,function(index,ele) {
			var list = "<tr><td><input class='patientcheck' type='checkbox' name='patientid' lay-skin='line' value='"
				+ ele.id
				+ "'/></td><td>"
				+ ele.queue_num
				+ "</td><td>"
				+ ele.patient_name
				+ "</td><td>"
				+ ele.doctorName
				+ "</td><td>"
				+ ele.state_patient
				+ "</td><td>"
				+ ele.state_custom
				+ "</td><td>"
				+ ele.time_interval
				+ "</td><td>"
				+ ele.sub_queue_type
				+ "</td><td>"
				+ ele.fre_date
				+ "</td><td>"
				+ ele.call_time
				+ "</td></tr>";
		$("#patientPassto").append(list);
		});
	}, 'json');
});
//根据分诊台查询已就诊患者
$("#duilie").on('click',".queue_type_id",function() {
	var id = $(this).attr('data-id');
	$("#patientOver").html("");//清空数据
	$("#checkall").removeAttr("checked");
	$.post("/patient/list_patient_over",{id : id},function(data) {
		$.each(data,function(index,ele) {
			var list = "<tr><td><input class='patientcheck' type='checkbox' name='patientid' lay-skin='line' value='"
				+ ele.id
				+ "'/></td><td>"
				+ ele.queue_num
				+ "</td><td>"
				+ ele.patient_name
				+ "</td><td>"
				+ ele.doctorName
				+ "</td><td>"
				+ ele.state_patient
				+ "</td><td>"
				+ ele.state_custom
				+ "</td><td>"
				+ ele.time_interval
				+ "</td><td>"
				+ ele.sub_queue_type
				+ "</td><td>"
				+ ele.fre_date
				+ "</td><td>"
				+ ele.call_time
				+ "</td></tr>";
		$("#patientOver").append(list);
		});
	}, 'json');
});
//根据分诊台查询过号患者
$("#duilie").on('click',".queue_type_id",function() {
	var id = $(this).attr('data-id');
	$("#patientPass").html("");//清空数据
	$("#checkall").removeAttr("checked");
	$.post("/patient/list_patient_pass",{id : id},function(data) {
		$.each(data,function(index,ele) {
			var list = "<tr><td><input class='patientcheck' type='checkbox' name='patientid' lay-skin='line' value='"
				+ ele.id
				+ "'/></td><td>"
				+ ele.queue_num
				+ "</td><td>"
				+ ele.patient_name
				+ "</td><td>"
				+ ele.doctorName
				+ "</td><td>"
				+ ele.state_patient
				+ "</td><td>"
				+ ele.state_custom
				+ "</td><td>"
				+ ele.time_interval
				+ "</td><td>"
				+ ele.sub_queue_type
				+ "</td><td>"
				+ ele.fre_date
				+ "</td><td>"
				+ ele.call_time
				+ "</td></tr>";
		$("#patientPass").append(list);
		});
	}, 'json');
});

//全选中跟全部取消
$("#checkall").click(function() {
	if (this.checked) {
		$("input[name='patientid']").prop("checked", true);
	} else {
		$("input[name='patientid']").removeAttr("checked");
	}
});
//键盘按下回车事件报道
$("#searchvalue").keyup(function(e) {
	if (e.keyCode == 13) {
		var aa = $("#searchvalue").val();
		if (null == aa || "" == aa) {
			return false;
		} else {
			alert(aa);
		}
	}
});
//选项卡切换
layui.use('element', function() {
	var element = layui.element();
});
	
		
		