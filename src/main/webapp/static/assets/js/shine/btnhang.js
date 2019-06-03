/**
 * 挂起
 */
$("#btnhang").click(function(){
	var ids='';
	$(".patientcheck:checked").each(function(ele){
		ids+=$(this).val()+',';
	});
	if(null==ids||""==ids){
		layer.msg('请选择需要操作的患者', {icon: 5});
		return false;
	}else{
		$.ajax({
			type:'post',
			dataType:"json",
			url:"/patient/Hang",
			data:{ids:ids},
			success:function(result){
				layer.msg(result.msg,{icon:1});
			}   
		});
	}
});