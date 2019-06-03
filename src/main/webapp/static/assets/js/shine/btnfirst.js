/**
 * 优先
 */
$("#btnfirst").click(function(){
	var ids ='';
	$(".patientcheck:checked").each(function(ele){
		ids +=  $(this).val()+',';
	});
	alert(ids);
});