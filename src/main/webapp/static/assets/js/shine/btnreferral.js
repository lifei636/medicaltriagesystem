/**
 * 转诊
 */
$("#btnreferral").click(function(){
	var ids='';
	$(".patientcheck:checked").each(function(ele){
		ids+=$(this).val()+',';
	});
	
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
					//form.render('select');
				});  
		        $("#fenzhenduilie").hide().show();
		       
		    }
		}); 
	}
	$("#Referral").on('click',function(){
		var value=$('#fenzhenduilie option:selected').val();
		$.ajax({
			type:'post',
			dataType:"json",
			url:"/patient/ReferralQueueType",
			data:{ids:ids,queue_type_id:value},
			success:function(result){
				layer.msg(result.msg,{icon:1});
				layer.closeAll();
				
				$("#patientdiv").hide();
				$("#patientlist").hide();
				$("#patientList").html("");//清空数据
				$("#patientPassto").html("");
				$("#patientOver").html("");
				$("#patientPass").html("");
				$("#duilie").html("");
				$.post("/triagecl/list_triage",//分诊台队列名称，等候，就诊患者，过号，已叫，就诊者医生
						function(data) {
							$.each(data,function(index, ele) {
								var listr = "<li class='queue_type_id' data-id='"
									+ ele.duiliebianhao
									+ "'><div class='title'>"
									+ ele.duiliemingcheng
									+ "</div><div class='left'><ul><li>等候："
									+ ele.denghou
									+ "</li><li>过号："
									+ ele.guohao
									+ "</li><li>已叫："
									+ ele.yijiao
									+ "</li></ul></div><div class='right'><ul><li>患者："
									+ ele.dangqianjiuzhen
									+ "</li><li> </li><li>医生："
									+ ele.dangqianyisheng
									+ "</li></ul></div></li>";
							$("#duilie").append(listr);//添加到队列中
						});
						
						if($("#duilie").find("li").length>0){
							$("#duilie").find("li:eq(0)").click();
						}
					},
				'json');
			}   
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
});