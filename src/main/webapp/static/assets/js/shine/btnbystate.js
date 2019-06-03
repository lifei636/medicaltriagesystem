/**
 * 按状态分组
 */
$("#btnbystate").click(function(){
	var ids='';
	$(".patientcheck:checked").each(function(ele){
		ids+=$(this).val()+',';
	});
	/*$.post("patientQueuecl/NewlyDiagnosed",{id:ids})*/
	alert(ids);
});