/**
 * 手动录入
 */
$("#btninput").on('click',function(){
	//队列名称列表
	$.post("/queuetype/list_queue_type_name", function(data) {
		$.each(data,function(index,ele){
			var lists="<option value='"+ele.queue_type_id+"'>"+ele.NAME+"</option>";
			$("#shoudongluru").append(lists);
		});
		$("#shoudongluru").hide().show();
	}, 'json');
	
	layer.open({
			type:1,
			title:'手动录入患者信息',
			area: ['500px', '450px'],
			time: 50000,
			anim: 0,
			resize:false,
			scrollbar:false,
			skin: 'layui-layer-rim', //加上边框
	        shadeClose: true, //点击遮罩关闭
	        content:$("#patientdiv")
		});
	$("#Addto").on('click',function(){
		
		
	});
});
layui.use('form', function(){
	  var form = layui.form();
	  //监听提交
	  form.on('submit(formDemo)', function(data){
	    layer.msg(JSON.stringify(data.field));
	    return false;
	  });
	});