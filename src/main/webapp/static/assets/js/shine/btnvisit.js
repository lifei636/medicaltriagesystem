/**
 * 复诊
 */
$("#btnvisit").click(function(){
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
		url:"/patient/Visit",
		data:{ids:ids},
		success:function(result){
			layer.msg(result.msg,{icon:1});
		}   
	});
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
			
			/*if($("#duilie").find("li").length>0){
				$("#duilie").find("li:eq(0)").click();
			}*/
		}, 'json');
	}
});
