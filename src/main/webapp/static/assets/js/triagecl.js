/**
 * 
 */
 <script type="text/javascript">
		//调用load方法
		$(function($) {
			load();
		});

		//加载数据
		function load() {
			$
					.post(
							"${basePath}/triagecl/list_triage",//分诊台队列名称，等候，就诊患者，过号，已叫，就诊者医生
							function(data) {
								$
										.each(
												data,
												function(index, ele) {
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

													$("#duilie").append(listr);
												});

							}, 'json');
			//分诊台名称
			$.post("${basePath}/triagecl/list_triage_name", function(data) {
				var list = "<span>" + data.name + "</span>";
				$(".triageName").append(list);
			}, 'json');

			//根据分诊台查询患者
			$("#duilie")
					.on(
							'click',
							".queue_type_id",
							function() {
								var id = $(this).attr('data-id');
								$("#patientList").html("");

								$
										.post(
												"${basePath}/patientQueuecl/list_patient",
												{
													id : id
												},
												function(data) {
													$
															.each(
																	data,
																	function(
																			index,
																			ele) {
																		var list = "<tr><td><input type='checkbox'  name='patientid' value='"+ele.id+"' /></td><td>"
																				+ ele.patient_name
																				+ "</td><td>"
																				+ ele.queue_num
																				+ "</td><td>"
																				+ ele.doctorName
																				+ "</td><td>"
																				+ ele.state_patient
																				+ "</td><td>"
																				+ ele.state_custom
																				+ "</td><td>"
																				+ ele.time_interval
																				+ "</td><td>"
																				+ ele.sub_queue_type
																				+ "</td><td>"
																				+ ele.fre_date
																				+ "</td><td>"
																				+ ele.call_time
																				+ "</td></tr>";
																		$(
																				"#patientList")
																				.append(
																						list);
																	});
												}, 'json');
							});

			//全选中跟全部取消
			$("#checkall").click(function () {
			    if (this.checked) {
			        $("input[name='patientid']").prop("checked", true);
			     } else {
			        $("input[name='patientid']").removeAttr("checked", false);
			    }
			}); 
		};
	</script>