	<script type="text/javascript">
		function getCtxPath(){
			return "${ctxPath}";
		}
	</script>
	<script type="text/javascript" charset="utf-8" src="${ctxPath}/static/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxPath}/static/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxPath}/static/ueditor/ueditor.parse.min.js"></script>
	
	@ var val = x.value!'';
	@ var token = "token_";
	@ if (val != ""){
	@ 	token = "";	
	@}
	
	<!-- 加载编辑器的容器 -->
    <script id="_temp" type="text/plain" style="width:100%">
        ${x.value!}
    </script>
    <script>
			// ueditor
	        var editorImage = UE.getEditor('_temp');
	        editorImage.ready(function () {
     	    	//设置编辑器不可用
     	    	editorImage.setDisabled('insertimage');
	        	//隐藏编辑器，因为不会用到这个编辑器实例，所以要隐藏
	        	editorImage.hide();
	        	//侦听图片上传
	        	editorImage.addListener('beforeInsertImage', function (t, arg) {
	        		var src="";
	        		
	        		if(arg ==null ||arg.length==0){
	        			return;
	        		}
	        		
	        		if("${x.multiple}"=="true"){
	        			$.each(arg,function(index,pic){
	        				src += pic.src+",";
	        			});
	        			
	        			src = src.substring(0, src.length-1);
	        			$("#_${x.index}").val(src);
	        		}else{
	        			//将地址赋值给相应的input,只去第一张图片的路径
		        		$("#_${x.index}").val(arg[0].src);
	        		}
	        		
	        		//图片预览
	        		//$("#preview").attr("src", arg[0].src);
	       		})
	        	
			});
	        
		//弹出图片上传的对话框
	      function upImage() {
	      	var myImage = editorImage.getDialog("insertimage");
	      	myImage.open();
	      }
	</script>	
 <input type="text" id="_${x.index!}" name="${table!x.table}.${x.index!}" value="${x.value!}" intercept="${x.intercept!}" ${x.required!} readonly="readonly" onclick="upImage()" />
