/**
 * 批量报道
 */
$("#btnreport").one('click',function(){
	layer.open({
				type:1,
				title:'批量报道',
				area: ['500px', '450px'],
				time: 50000,
				anim: 0,
				resize:false,
				scrollbar:false,
				skin: 'layui-layer-rim', //加上边框
		        shadeClose: true, //点击遮罩关闭
		        content:'<table class="layui-table" id="patientlist" lay-skin="line">'
		        		+'<colgroup>'
		        		+'<col width="150">'
		        		+'<col width="200">'
		        		+'<col>'
		        		+'</colgroup>'
		        		+'<thead>'
		        		+'<tr>'
		        		+'<th>昵称</th>'
		        		+'<th>加入时间</th>'
		        		+'<th>签名</th>'
		        		+'</tr> '
		        		+'</thead>'
		        		+'<tbody>'
		        		+'<tr>'
		        		+'<td>贤心</td>'
		        		+'<td>2016-11-29</td>'
		        		+'<td>人生就像是一场修行</td>'
		        		+'</tr>'
		        		+'<tr>'
		        		+'<td>贤心</td>'
		        		+'<td>2016-11-29</td>'
		        		+'<td>人生就像是一场修行</td>'
		        		+'</tr>'
		        		+'</tbody>'
		        		+'</table>'
			});
});