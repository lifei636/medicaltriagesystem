/**
 * 分诊
 */
$("#btntriage").click(function(){
	var ids='';
	$(".patientcheck:checked").each(function(ele){
		ids+=$(this).val()+',';
	});
	alert(ids);
	
	/*
	if(null==ids||""==ids){
		layer.msg('请选择需要操作的患者', {icon: 5});
		return false;
	}else{
		layer.open({
			type:1,
			title:'队列信息',
			area: ['400px', '300px'],
			time: 50000,
			anim: 0,
			resize:false,
			scrollbar:false,
			skin: 'layui-layer-rim', //加上边框
	        shadeClose: true, //点击遮罩关闭
	        content:$('#fenzhen')
		});
		$("#fenzhenduilie").html("");
		 //队列名称列表
		$.ajax({
			
		    url: '/queuetype/list_queue_type_name',
		    type: 'post',
		    dataType: 'json',
		    async: false,
		    success: function (data) {
		        if (data.status == 302) {
		            location.href = data.location;
		        }
		        $.each(data,function(index,ele){
		        	var lists="<option value='"+ele.queue_type_id+"'>"+ele.NAME+"</option>";
					$("#fenzhenduilie").append(lists);
				});  
		        
		        $("#fenzhenduilie").hide().show();
		    }
		}); 
	}*/
	
	/*$.ajax({
		type:'post',
		dataType:"json",
		url:"/patient/Hang",
		data:{ids:ids},
		success:function(result){
			layer.msg(result.msg,{icon:1});
		}   
	});*/
	/*layui.use('form', function(){
		var form = layui.form();
		
		//监听提交
		form.on('submit(formDemo)', function(data){
			layer.msg(JSON.stringify(data.field));
			return false;
			});
		});*/
});