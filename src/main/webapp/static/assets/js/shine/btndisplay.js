/**
 * 显示
 */
$("#btndisplay").click(function(){
	var ids='';
	$(".patientcheck:checked").each(function(ele){
		ids+=$(this).val()+',';
	});
	/*$.post("patientQueuecl/NewlyDiagnosed",{id:ids})*/
	alert(ids);
});