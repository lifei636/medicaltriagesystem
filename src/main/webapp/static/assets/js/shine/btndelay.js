/**
 * 延迟
 */
$("#btndelay").click(function(){
	var ids='';
	$(".patientcheck:checked").each(function(ele){
		ids+=$(this).val()+',';
	});
	if(null==ids||""==ids){
		layer.msg('请选择需要操作的患者', {icon: 5});
		return false;
	}else{
	layer.prompt({title:"延迟时间,以分钟为单位"},function(val, index){
		  $.ajax({
				type:'post',
				dataType:"json",
				url:"/patient/delay",
				data:{ids:ids,delaytime:val},
				success:function(result){
					layer.msg(result.msg,{icon:1});
				}   
			});
		  layer.close(index);
		});
	}
});