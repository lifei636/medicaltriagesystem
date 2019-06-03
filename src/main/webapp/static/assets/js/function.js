var type = 0;
//用户登录检测
function CheckLogin()
{
    $.ajax({
        type: "POST",
        url: "/triagecl/getUserSession",
        async: false,
        contentType: "application/text;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.status != "true")
                window.location.href = "login.html";
        },
        error: function(){
            window.location.href = "login.html";
        }
    });
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
var showtime = "";
var ip = "";
var patientList=null;
var queueTypeShowAll="true";
try {
    showtime = getUrlParam("showtime");
} catch (e) {
    showtime = "";
}
try {
    ip = getUrlParam("ip");
} catch (e) {
    ip = "";
}
try {
    queueTypeShowAll = getUrlParam("showall");
} catch (e) {
    queueTypeShowAll = "true";
}


//获取分诊台名称
function TriageName() {
    //CheckLogin();
    $.ajax({
        type: "POST",
        async: false,
        url: "/triagecl/findByTriageName",
        data:{"ip":ip},
        dataType: "json",

        beforeSend: function () {
            $("#span_TriageName").html("数据读取中，请稍后...")
        },
        success: function (data) {
            $("#span_TriageName").html(data.data.name);
            if (data.data.triage_type == 1)
                $("#list_paiban").html("医生排班");
            else
            {
                if (data.data.reorder_type == 1) {
                    $("#list_paiban").html("科室排班");
                    type = 1;
                }
                else {
                    $("#list_paiban").hide();
                    type = 2;
                    $("#btntriage").hide();
                    $("#btnnotriage").hide();
                    $("#btnreferral").hide();
                }
                    
            }
                
        },
        error:function(data)
        {
            layer.msg("系统错误，请联系管理员", { shift: 1, time: 2000 });
        }
    });
}



$("#searchvalue").keypress(function () {
    if (event.keyCode == "13")
    {
        search_show();
    }
});

//获取队列列表
function ListQueueType()
{
    var url_t="/clientqueuetype/listQueueType";
    if(queueTypeShowAll=="false")
        url_t="/clientqueuetype/listQueueType2";
    $.ajax({
        type: "POST",
        async: true,
        url: url_t,
        dataType: "json",
        data: { "ip": ip },
        beforeSend: function () {
            $("#li_listqueue").html("<li style='color:red'>数据读取中，请稍后...</li>")
        },
        success: function (data) {
            $("#li_listqueue").html("");
            var status = data.code;
            if (status ===0) {
                var count = data.count;
                if (count > 0) {
                    patientList = data.list;
                    var p = 0;
                    $("#li_listqueue").width(count * 215);
                    var html = "";
                    $.each(data.list, function (i, li) {
                        if (i == 0) {
                            if (ListNum == null)
                                ListNum ="dl"+li.queue_number;
                        }
                        if (ListNum == "dl"+li.queue_number)
                            p = i+1;
                        html += "<li class='queue_type_id' id='dl" + li.queue_number + "' onclick= 'ListPatient(\"dl"
                            + li.queue_number+"\")"
                            + "'><div class='title'>"
                            + li.queue_name
                            + "</div><div class='left'><ul><li>等候："
                            + li.wait
                            + "</li><li>过号："
                            + li.pass_no
                            + "</li><li>已叫："
                            + li.have_station
                            + "</li></ul></div><div class='right'><ul><li>患者："
                            + li.current_visit
                            + "</li><li> </li><li>医生："
                            + li.current_doctor
                                + "</li></ul></div></li>";
                       
                    });
                    $("#li_listqueue").html(html);
                    var p_l_w = document.documentElement.clientWidth;
                    var longs = p * 215 - p_l_w;
                    if ( longs> 0)
                        $("#fzt_list").scrollLeft(longs);
                    ListPatient(ListNum);
                }
                else
                    $("#li_listqueue").html("<li style='color:red'>没有列表</li>");
            }
            else
            {
                $("#li_listqueue").html("<li style='color:red'>"+data.return_msg+"</li>");
            }
        },
        error: function () {
            $("#li_listqueue").html("<li style='color:red'>数据加载失败，请联系管理员！</li>");
        }
    });
}

//患者列表
function ListPatient(id) {
    id=id.replace("dl","");
    $(".queue_type_id").removeClass("libg");
    ListNum ="dl"+ id;
    ids = "";
    $("#" + ListNum).addClass("libg");
    var div = $("#list_" + list);
    $("#ck_" + list).prop("checked", false);
    $("#ck_" + list)[0].indeterminate = false;
    $.ajax({
        type: "POST",
        async: true,
        url: patientQueue_url,
        dataType: "json",
        data: { "ip":ip,"queue_number": id },
        beforeSend: function () {
            div.html("<li style='color:red'>数据读取中，请稍后...</li>")
        },
        success: function (data) {
            div.html("");
            var status = data.return_code;
            if (status == "success") {
                var count = data.count;
                if (count > 0) {
                    var html = "";
                    var l_c;
                    $.each(data.list, function (i, li) {
                        var s = "未知";
                        switch (li.state_patient) {
                            case 0:
                                s = "初诊";
                                break;
                            case 1:
                                s = "过号";
                                break;
                            case 2:
                                s = "复诊";
                                break;
                            case 3:
                                s = "部分待检";
                                break;
                            case 4:
                                s = "诊室等候";
                                break;
                            case 5:
                                s = "优先";
                                break;
                            case 6:
                                s = "插队";
                                break;
                            case 7:
                                s = "延迟";
                                break;
                            case 8:
                                s = "迟到";
                                break;
                            case 50:
                                s = "挂起";
                                break;
                            case 51:
                                s = "已叫号";
                                break;
                            case 52:
                                s = "绿色通道";
                                break;
                            case 53:
                                s = "已就诊";
                                break;
                            case 54:
                                s = "未到过号";
                                break;
                            default:
                        }
                        var t = "全天";
                        switch (li.time_interval) {
                            case 0:
                                t= '全天';
                                break;
                            case 1:
                                t= '上午';
                                break;
                            case 2:
                                t = '下午';
                                break;
                            default:
                        }
                        html += "<div class=\"rows\"><span class=\"w10\"><input onclick=\"check_status('ck_" + list + "')\" type=\"checkbox\" id=\"" + li.id + "\" value=\"" + li.id + "\" title=\"" + li.id + "\" name=\"ck_" + list + "\" />" + li.register_id + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w15\">" + li.patient_name + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w15\">" + li.doctorName + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w10\">" + s + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w10\">" + li.state_custom + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w10\">" + t + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w15\">" + (showtime=="false"?"":li.fre_date) + "</span><span onclick=\"clickcheck(" + li.id + ")\" class=\"w15 span_last\">" +  (showtime=="false"?"":li.opr_time) + "</span></div><input type=\"hidden\" value=\""+li.patient_source_code+"\" id=\"code"+li.id+"\" />";
                        if (s_id != "") {
                            if (li.id == s_id)
                                l_c = i;
                        }
                    });
                    div.html(html);
                    if (s_id != "") {
                        clickcheck(s_id);
                        s_id = "";
                        if (l_c > 10)
                            div.animate({ 'scrollTop': ''+(l_c - 10) * 40+ 'px' });
                    }
                }
                else
                    div.html("<li style='color:red'>没有患者</li>");
            }
            else {
                div.html("<li style='color:red'>" + data.return_msg + "</li>");
            }
        },
        error: function () {
            div.html("<li style='color:red'>数据加载失败，请联系管理员！</li>");
        }
    });
    
}
layui.use('element', function () {
        element = layui.element();
        element.on('tab(list)', function (data) {
            //list = this.getAttribute('lay-id');
            //$.each(data, function (i, e) {
            //    list = e.val();
            //    list2 = e;
            //});
            list = "wait";
            $("#btnreport").hide();
            switch (data.index) {
                case 1:
                    list = "pass";
                    break;
                case 2:
                    list = "already";
                    break;
                case 3:
                    list = "passno";
                    break;
                case 4:
                    $("#btnreport").show();
                    list ="nodisplay";
                    break;
                default:
            }
            patientQueue_url = "/clientPatientQueue/listPatient_" + list;
            ListPatient(ListNum);
        });
    });
//全选
function check_all(name) {
    var chk = $("#" + name);
    var chks = $("input[name='" + name + "']");
    if (chk.indeterminate || chk.prop('checked')) {
        chks.each(function () {
            this.checked = true;
            
        });
    }
    else {
        chks.each(function () {
            this.checked = false;
            
        });
    }
    check_status(name);
}


//检测选择状态
function check_status(name) {
    var i = 0, j = 0;
    if (name == "ck_doctor")
        d_ids = "";
    else
        ids = "";
    var chks = $("input[name='" + name + "']");
    chks.each(function (obj) {
        i += 1;
        if ($(this).is(':checked')) {
            if (name == "ck_doctor")
                d_ids += $(this).attr("id") + ",";
            else
                ids += $(this).attr("id") + ",";
            j += 1;
            $(this).parent().parent().css("background-color", "#438eb9");
            $(this).parent().parent().css("color", "#fff");
        }
        else {
            $(this).parent().parent().css("background-color", "#fff");
            $(this).parent().parent().css("color", "#666");
        }
    });
    //ids = ids.substr(0, ids.length - 1);
    var chk = $("#" + name);
    if (i == j) {
        chk[0].indeterminate = false;
        chk.prop("checked", true);
    }
    else if (i > j && j > 0) {
        chk[0].indeterminate = true;
    }
    else {
        chk[0].indeterminate = false;
        chk.prop("checked", false);
    }
}
//队列横向滚动

var view = document.getElementById('fzt_list');
horwheel(view);
//点击数据行选择
function clickcheck(id)
{
    $("#" + id).trigger("click");
    s = 60;
}
//患者操作
function patientQueue(url, data) {
    if (ids != "") {
        if (ids.substr(0, ids.length - 1).split(',').length == 1) {
            var json = { "ids": ids, "queue_type_id": ListNum.replace("dl", ""),"ip":ip };
            if (data != null)
                json = data;
            $.ajax({
                type: "POST",
                async: true,
                url: "/clientPatientQueue/" + url,
                dataType: "json",
                data: json,
                success: function (data) {
                    if (data.return_code == "success")
                    {
                        ListQueueType();
                        try {
                            if (data.print_type == 2) {
                                print_no(data.queue_type_name, data.time_interval, data.register_id, data.patient_name, data.state_patient);
                            }
                        } catch (e) {

                        }
                        
                    }

                    layer.msg(data.return_msg, { shift: 1, time: 2000 });
                    
                },
                error: function () {
                    layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
                }
            });
        }
        else
            layer.msg("只能选择一条数据", { shift: 1, time: 2000 });
    }
    else {
        layer.msg("请选择要操作的患者", { shift: 1, time: 2000 });
    }
}
//批量报到
function baodaoselect()
{
    var p_ids=ids.substr(0,ids.length-1).split(",");
    var p_i=0;
    for(var i=0;i<p_ids.length;i++)
    {
        var code=$("#code"+p_ids[i]).val();
        $.ajax({
            type: "POST",
            async: false,
            url: "/clientPatientQueue/ScanBaodao",
            dataType: "json",
            data: { "code": code, 'queue_type_id': ListNum.replace("dl", ""), 'yl': false,"ip":ip },
            success: function (data) {
                if (data.return_code == "success") {
                    p_i++;
                }
                else {
                    
                }
            }
        });
    }
    layer.msg("本次报到"+p_i+"位", { shift: 1, time: 2000 });
    patientQueue_url = "/patientQueue/listPatient_" + list;
    element.tabChange('list', list);
    
}

//延迟
function patientQueue_delay_show()
{
    layer.open(
        {
            type: 1,
            title: '延迟设置',
            skin: 'win',
            area: ['300px', '100px'],
            shade: [0.3, '#fff'],
            content: '<div class="delay">延迟时间（分钟）:<input type="button" class="w30" onclick="cutnmb(\'delay_input_time\')" value="-" /><input id="delay_input_time" type="text" value="1" class="w40" /><input type="button" class="w30" onclick="addnmb(\'delay_input_time\')" value="+"><input type="button" class="w50" id="delay_btn_sumbit" value="提交" onclick="patientQueue_delay()" /></div>',
            success: function (id, index) {
                this.enterEsc = function (event) {
                    if (event.keyCode === 13) {
                        patientQueue_delay();
                        return false;
                    }
                };
                $(document).on('keydown', this.enterEsc);
            },
            end: function () {
                ListQueueType(); 
                $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
            }
        }
    );
    
}
//根据队列获取医生
function getdoclist(id)
{
    $.ajax({
        type: "POST",
        async: true,
        url: "/clientPatientQueue/doctorId",
        dataType: "json",
        data: { "queue_type_id": id, "ip": ip },
        success: function (data) {
            $("#r_s_doc option").remove();
            $("#r_s_doc").append("<option value='0' selected='selected'>请选择</option>");
            if (data.return_code == "success") {
                if (data.count > 0) {
                    $.each(data.list, function (i, item) {
                        if(i==0)
                            $("#r_s_doc").append("<option value='" + item.id + "' selected='selected'>" + item.name + "</option>");
                        else
                            $("#r_s_doc").append("<option value='" + item.id + "' >" + item.name + "</option>");
                    });
                }
                if (data.numlist != undefined && data.numlist != "0") {
                    $("#yl").show();
                }
                else
                    $("#yl").hide();
            }
        }
    });
}
//转诊
function patientQueue_referralQueueType_show() {
    var s_dl = "<div class='left'>选择队列:</div><div class='right'><select id=\'r_s_dl\' class='select2-container' style='width:150px;overflow:hidden'" + ((type == 0 || type == 1) ? "onchange='getdoclist(this.value)'" : "") + "><option value='0' selected='selected'>请选择</option>";
    if (queueTypeShowAll == "false") {
        $.ajax({
            type: "POST",
            async: false,
            url: "/clientqueuetype/listQueueType2",
            data:{"ip":ip},
            dataType: "json",
            success: function (data) {
                var status = data.code;
                if (status === 0) {
                    $.each(data.list, function (i, item) {
                        if (item.queue_number != ListNum.replace("dl", ""))
                            {
                                var py=CC2PY(item.queue_name);
                                s_dl += "<option value='" + item.queue_number + "'>" + item.queue_name + "</option>";
                            }
                            
                    });
                }
            }
        });
    }
    else {
        $.each(patientList, function (i, item) {
            if (item.queue_number != ListNum.replace("dl", ""))
                s_dl += "<option value='" + item.queue_number + "'>" + item.queue_name + "</option>";
        });
        
    }
    s_dl += "</select></div>";
    if (type == 0 || type == 1)
        s_dl+="<div class='left'>叫号器/医生:</div><div class='right'><select id=\'r_s_doc\' style='width:150px;overflow:hidden'></select></div>";
    s_dl+="<div class='left'>&nbsp;</div><div class='right'><input type='button' class='w50'  value='提交' onclick='referralQueueType()' /></div>";
    layer.open(
        {
            type: 1,
            title: '转诊设置',
            skin: 'win',
            area: ['300px', '160px'],
            shade: [0.3, '#fff'],
            content: "<div class='Manual'>"+s_dl+"</div>",
            success: function (id, index) {
                $("#r_s_doc option").empty();
                $("#r_s_doc").append("<option value='0' selected='selected'>请选择</option>");
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/clientPatientQueue/doctorId",
                    dataType: "json",
                    data: { "queue_type_id": $("#r_s_dl").val(), "ip": ip },
                    success: function (data) {
                        if (data.return_code == "success") {
                            if (data.count > 0) {
                                $.each(data.list, function (i, item) {
                                    
                                    $("#r_s_doc").append("<option value='" + item.id + "'>" + item.name + "</option>");
                                });
                            }
                        }
                        
                    }
                });
                $("#r_s_dl").select2(
                    {
                        allowClear: true,
                        formatResult: function (item) {
                            var str = item.text;
                            var idx = str.indexOf('|');
                            return str.substring(0,idx);
                        },
                        formatSelection: function (item) {
                            var str = item.text;
                            var idx = str.indexOf('|');
                            return str.substring(0,idx);
                        }
                    }
                );
                this.enterEsc = function (event) {
                    if (event.keyCode === 13) {
                        referralQueueType();
                        return false;
                    }
                };
                $(document).on('keydown', this.enterEsc);
                
            },
            end: function () {
                $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
            }
        }
    );
    
}
//分诊
function examination_show() {
    $.ajax({
        type: "POST",
        async: true,
        url: "/clientPatientQueue/doctorId",
        dataType: "json",
        data: { "queue_type_id": ListNum.replace("dl", ""), "ip": ip },
        success: function (data) {
            if (data.return_code == "success") {
                var s_html = "<select id=\'examination_select\' style='width:150px;overflow:hidden'><option value='0'>请选择</option>";
                $.each(data.list, function (i, item) {
                    //if (item.queue_number != ListNum)
                    s_html += "<option value='" + item.id + "'>" + item.name + "</option>";
                });
                s_html += "</select>";
                layer.open(
                    {
                        type: 1,
                        title: '分诊设置',
                        skin: 'win',
                        area: ['330px', '100px'],
                        shade: [0.3, '#fff'],
                        content: '<div class="delay">医生/叫号器:' + s_html + '<input type="button" class="w50"  value="提交" onclick="examination()" /></div>',
                        success: function (id, index) {
                            this.enterEsc = function (event) {
                                if (event.keyCode === 13) {
                                    examination();
                                    return false;
                                }
                            };
                            $(document).on('keydown', this.enterEsc);
                        },
                        end: function () {
                            $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
                        }
                    }
                );
            }

        },
        error: function () {
            layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
        }
    });

}
//手动录入
function Manual_show() {

    var s_html = "<select id=\'manual_select\' style='width:150px;overflow:hidden'>";
    $.each(patientList, function (i, item) {
        //if (item.queue_number != ListNum)
        s_html += "<option value='" + item.queue_number + "'>" + item.queue_name + "</option>";
    });
    s_html += "</select>";
    layer.open(
        {
            type: 1,
            title: '手动录入',
            skin: 'win',
            area: ['300px', '160px'],
            shade: [0.3, '#fff'],
            content: '<div class="Manual"><div class="left">患者姓名:</div><div class="right"><input type="text" id="patientName" /></div><div class="left">队列:</div><div class="right">' + s_html + '</div><div class="left">&nbsp;</div><div class="right"><input type="button" class="w50"   value="提交" onclick="Manual()" /></div></div>',
            success: function (id, index) {
                this.enterEsc = function (event) {
                    if (event.keyCode === 13) {
                        Manual();
                        return false;
                    }
                };
                $(document).on('keydown', this.enterEsc);
            },
            end: function () {
                ListQueueType();
                $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
            }
        }
    );
}
function search() {
    var index = layer.index;
    layer.close(index);
    $("#searchvalue").text("");
    }
//搜索
function search_show()
{
    if ($("#searchvalue").val() != "") {
        $("#searchvalue").select();
        
        $.ajax({
            type: "POST",
            async: true,
            url: "/clientPatientQueue/Scan",
            dataType: "json",
            data: { "code": $("#searchvalue").val(), "ip": ip },
            success: function (data) {
                if (data.return_code == "success") {
                    if (data.count >=2) {
                        var html = "<div class='rowsbado'><div class='span1'>姓名</div><div class='span3'>队列</div><div class='span2'>状态</div><div class='span1'>排队号/预留</div><div class='span4'>就诊时间段</div><div class='span1'>操作</div></div>";
                        $.each(data.list, function (i, item) {
                            var s = "", c = "";
                            switch (item.state_patient) {
                                case 0:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 50:
                                    s = "已报到";
                                    c = "<input type='button' value='打印' onclick='print_no(\"" + item.queue_type_name + "\",\"" + item.time_interval + "\",\"" + item.register_id + "\",\"" + item.patient_name + "\",\"" + item.state_patient + "\")' />";
                                    break;
                                case 51:
                                    s = "正在就诊";
                                    c="";
                                    break;
                                case 52:
                                case 53:
                                    s = '已就诊';
                                    c="";
                                    break;
                                default:
                                    s = '过号';
                                    c = "<input type='button' value='打印' onclick='print_no(\"" + item.queue_type_name + "\",\"" + item.time_interval + "\",\"" + item.register_id + "\",\"" + item.patient_name + "\",\"" + item.state_patient + "\")' />";
                            }
                            c = c + "&nbsp;<input type='button' value='选择' onclick='dingwei(\"" + item.id + "\",\"" + item.state_patient + "\",\"" + item.queue_type_id + "\")' />";
                            html += "<div class='rowsbado'><div class='span1'>" + item.patient_name + "</div><div class='span3'>" + item.queue_type_name + "</div><div class='span2'>" + (item.is_display == 2 ? s : "未报到") + "</div><div class='span1'>" + (item.is_display == 2 ? item.register_id : "<input type='checkbox' id='" + item.id + "' />") + "</div><div class='span4'>"+str2date(item.begin_time,item.last_tiem,item.fre_date)+"</div><div class='span1'>" + (item.is_display == 2 ? c : "<input type='button' value='报到' onclick='baodao(\"" + $("#searchvalue").val() + "\",\"" + item.queue_type_id + "\",\"" + item.id + "\",\"" + item.reorder_type + "\",\"" + item.is_pretriage + "\",\"" + item.reserve_numlist + "\",\"" + item.print_type + "\")' />") + "</div></div>";
                        });
                        var h=(data.count*40+120)+"px";
                        layer.open(
                            {
                                type: 1,
                                title: '患者报道',
                                skin: 'win',
                                area: ['690px', h],
                                shade: [0.3, '#fff'],
                                content: '<div class="baodao" id="baodaolist">' + html + '</div>',
                                success: function (id, index) {
                                    this.enterEsc = function (event) {

                                    };
                                    $(document).on('keydown', this.enterEsc);
                                    $("#baodaolist").height(data.count*40+30);
                                },
                                end: function () {
                                    $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
                                }
                            }
                        );
                    }
                    else if (data.count == 1)
                    {
                        //if (data.list[0].reserve_numlist != undefined && data.list[0].reserve_numlist != "0") {
                            var html = "<div class='rowsbado'><div class='span1'>姓名</div><div class='span3'>队列</div><div class='span2'>状态</div><div class='span1'>排队号/预留</div><div class='span4'>就诊时间段</div><div class='span1'>操作</div></div>";
                            $.each(data.list, function (i, item) {
                                var s = "", c = "";
                                switch (item.state_patient) {
                                    case 0:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 50:
                                        s = "已报到";
                                        c = "<input type='button' value='打印' onclick='print_no(\"" + item.queue_type_name + "\",\"" + item.time_interval + "\",\"" + item.register_id + "\",\"" + item.patient_name + "\",\"" + item.state_patient + "\")' />";
                                        break;
                                    case 51:
                                        s = "正在就诊";
                                        c = "";
                                        break;
                                    case 52:
                                    case 53:
                                        s = '已就诊';
                                        c = "";
                                        break;
                                    default:
                                        s = '过号';
                                        c = "<input type='button' value='打印' onclick='print_no(\"" + $("#searchvalue").val() + "\")' />";
                                }
                                c = c + "&nbsp;<input type='button' value='选择' onclick='dingwei(\"" + item.id + "\",\"" + item.state_patient + "\",\"" + item.queue_type_id + "\")' />";
                                html += "<div class='rowsbado'><div class='span1'>" + item.patient_name + "</div><div class='span3'>" + item.queue_type_name + "</div><div class='span2'>" + (item.is_display == 2 ? s : "未报到") + "</div><div class='span1'>" + (item.is_display == 2 ? item.register_id : "<input type='checkbox' id='" + item.id + "' />") + "</div><div class='span4'>"+str2date(item.begin_time,item.last_tiem,item.fre_date)+"</div><div class='span1'>" + (item.is_display == 2 ? c : "<input type='button' value='报到' onclick='baodao(\"" + $("#searchvalue").val() + "\",\"" + item.queue_type_id + "\",\"" + item.id + "\",\"" + item.reorder_type + "\",\"" + item.is_pretriage + "\",\"" + item.reserve_numlist + "\",\"" + item.print_type + "\")' />") + "</div></div>";
                            });

                            layer.open(
                                {
                                    type: 1,
                                    title: '患者报道',
                                    skin: 'win',
                                    area: ['690px', '160px'],
                                    shade: [0.3, '#fff'],
                                    content: '<div class="baodao">' + html + '</div>',
                                    success: function (id, index) {
                                        this.enterEsc = function (event) {

                                        };
                                        $(document).on('keydown', this.enterEsc);
                                    },
                                    end: function () {
                                        $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
                                    }
                                }
                            );
                        //}
                        //else {
                        // baodao($("#searchvalue").val(), data.list[0].queue_type_id, data.list[0].id,data.list[0].reorder_type,data.list[0].is_pretriage,data.list[0].reserve_numlist);
                        //}
                           
                    }
                    else
                        layer.msg("未查询到患者信息", { shift: 1, time: 2000 });
                    
                    //ListQueueType();
                    //ListPatient(ListNum);
                }
            },
            error: function () {
                layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
            }
        });
        
    }
    else
        layer.msg("请输入查询号", { shift: 1, time: 2000 });
}
function str2date(begin,last,fre) {
    var bdate = Date.parse(begin); 
    var ldate= Date.parse(last); 
    var bstr="",lstr="";

    if (isNaN(bdate)) {  
        bdate= Date.parse(fre);
        ldate=bdate+30*60*1000;
        if(isNaN(bdate))
        {
            bdate=new Date();
            ldate=bdate+30*60*1000;
        }
    } 
    else
    {
        if (isNaN(ldate)) {  
            ldate=bdate+30*60*1000;
        }  
    }
    bdate=new Date(bdate);
    ldate=new Date(ldate);
    if (bdate.getHours() < 10) {
        bstr = "0" + bdate.getHours();
    }
    else {
        bstr = bdate.getHours();
    }
    if (bdate.getMinutes() < 10) {
        bstr += ":0" + bdate.getMinutes();
    }
    else
    {
        bstr += ":"+bdate.getMinutes();
    }
    if (ldate.getHours() < 10) {
        lstr = "0" + ldate.getHours();
    }
    else {
        lstr = ldate.getHours();
    }
    if (ldate.getMinutes() < 10) {
        lstr += ":0" + ldate.getMinutes();
    }
    else
    {
        lstr += ":"+ldate.getMinutes();
    }
    return bstr+'-'+lstr;
}
function dingwei(id, p_s, queue_type_id)
{
    var index = layer.index;
    layer.close(index);
    s_id = id;
    ids = id
    var status = "wait";
    if (p_s == 1)
        status = "passno";
    else if (p_s == 53)
        status = "already";
    else if (p_s == 54)
        status = "pass";
    ListNum = "dl" + queue_type_id;
    patientQueue_url = "/patientQueue/listPatient_" + status;
    element.tabChange('list', status);
}
function baodao(code, queue, id, ttype, is_tri, numlist,print_type) {
    var yl = $("#" + id).is(":checked");
    if (ttype == 1 && is_tri == 1) {
        var index = layer.index;
        layer.close(index);
        ids = id + ",";
        var s_dl = "<div class='left'>选择队列:</div><div class='right'><select id=\'r_s_dl\' style='width:150px;overflow:hidden'" + ((type == 0 || type == 1) ? "onchange='getdoclist(this.value)'" : "") + "><option value='0'>请选择</option>";
        $.each(patientList, function (i, item) {
            if (item.queue_number != queue)
                s_dl += "<option value='" + item.queue_number + "'>" + item.queue_name + "</option>";
        });

        s_dl += "</select></div>";
        if (type == 0 || type == 1)
            s_dl += "<div class='left'>诊室:</div><div class='right'><select id=\'r_s_doc\' style='width:150px;overflow:hidden'></select></div>";
        s_dl += "<div id='yl' style='display:none;'><div class='left'>预留:</div><div class='right'><input type='checkbox' id='" + id + "' /></div></div><div class='left'>&nbsp;</div><div class='right'><input type='button' class='w50'  value='提交' onclick='referralQueueType2(\"" + code + "\",\"" + queue + "\",\"" + id + "\")' /></div>";
        layer.open(
            {
                type: 1,
                title: '分诊设置',
                skin: 'win',
                area: ['300px', '200px'],
                shade: [0.3, '#fff'],
                content: "<div class='Manual'>" + s_dl + "</div>",
                success: function (id, index) {
                    $("#r_s_doc option").empty();
                    $("#r_s_doc").append("<option value='0' selected='selected'>请选择</option>");
                    $.ajax({
                        type: "POST",
                        async: true,
                        url: "/clientPatientQueue/doctorId",
                        dataType: "json",
                        data: { "queue_type_id": $("#r_s_dl").val(), "ip": ip },
                        success: function (data) {
                            if (data.return_code == "success") {
                                if (data.count > 0) {
                                    $.each(data.list, function (i, item) {
                                        $("#r_s_doc").append("<option value='" + item.id + "'>" + item.name + "</option>");
                                    });
                                }
                            }
                        }
                    });

                    this.enterEsc = function (event) {
                        if (event.keyCode === 13) {
                            referralQueueType();
                            return false;
                        }
                    };
                    $(document).on('keydown', this.enterEsc);
                },
                end: function () {
                    $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
                }
            }
        );
    }
    else {
        $.ajax({
            type: "POST",
            async: true,
            url: "/clientPatientQueue/ScanBaodao",
            dataType: "json",
            data: { "code": code, 'queue_type_id': queue, 'yl': yl,"ip":ip },
            success: function (data) {
                if (data.return_code == "success") {
                    var index = layer.index;
                    layer.close(index);
                    layer.msg(data.return_msg, { shift: 1, time: 2000 });
                    s_id = data.id;
                    ids = data.id;
                    var p_s = data.state_patient;
                    var status = "wait";
                    if (p_s == 1)
                        status = "passno";
                    else if (p_s == 53)
                        status = "already";
                    else if (p_s == 54)
                        status = "pass";
                    ListNum = "dl" + data.queue_type_id;
                    patientQueue_url = "/patientQueue/listPatient_" + status;
                    element.tabChange('list', status);
                    if (print_type == 2) {
                        print_no(data.queue_type_name, data.time_interval, data.register_id, data.patient_name, data.state_patient);
                    }
                }
                else {
                    layer.msg(data.return_msg, { shift: 1, time: 2000 });
                }
            }
        });
    }
}

function print_no(queue_type_name, time_interval, register_id,patient_name,state_patient)
{
    var html = "<div style=\"width:100%;font-size:24px;text-align:center\">" + $("#span_TriageName").text() + "</div><div style=\"width:100%; font-size:16px;text-align:left\">科室：" + queue_type_name + "<br />姓名：" + patient_name + "<br />就诊号：" + register_id + "<br /></div>";
    $("#print").html(html);
    $("#print").print({
        globalStyles: false,
        mediaPrint: false,
        stylesheet: null,
        iframe: true
    });
}

function cutnmb(id) {
    var num = $("#" + id);
    var n = parseInt(num.val());
    if (n > 1)
        num.val(n - 1);
}
function addnmb(id) {
    var num = $("#" + id);
    var n = parseInt(num.val());
    num.val(n + 1);
}

function patientQueue_delay() {
    var data = { "ids": ids, "queue_type_id": ListNum.replace("dl", ""), "timeInterval": $("#delay_input_time").val(), "ip": ip };
    var index = layer.index;
    layer.close(index);
    patientQueue("delay", data);
    
}
function referralQueueType2(code, queue,id) {
    var yl = $("#" + id).is(":checked");
        if (type == 0 || type == 1) {
            if ($("#r_s_doc").val() != 0 && $("#r_s_dl").val() != 0) {
                var data = { "code": code, "queue": queue, "queue_type_id": $("#r_s_dl").val(), "id": $("#r_s_doc").val(), "yl": yl, "ip": ip };
                ListNum = "dl" + $("#r_s_dl").val();
                var index = layer.index;
                layer.close(index);
                patientQueue("referralQueueType2", data);
            }
            else
                layer.msg("请选择队列或诊室", { shift: 1, time: 2000 });
        }
        else {
            if ($("#r_s_dl").val() != 0) {
                var data = { "ids": ids, "queue_type_id": $("#r_s_dl").val() };
                ListNum = "dl" + $("#r_s_dl").val();
                var index = layer.index;
                layer.close(index);
                patientQueue("referralQueueType2", data);
            }
            else
                layer.msg("请选择队列", { shift: 1, time: 2000 });
        }
}
function referralQueueType() {
    if (ids.split(",").length == 2) {
        if (type == 0 || type == 1) {
            if ($("#r_s_doc").val() != 0 && $("#r_s_dl").val() != 0) {
                var data = { "ids": ids, "queue_type_id": $("#r_s_dl").val(), "id": $("#r_s_doc").val(),"ip":ip };
                ListNum = "dl" + $("#r_s_dl").val();
                var index = layer.index;
                layer.close(index);
                patientQueue("referralQueueType", data);
            }
            else
                layer.msg("请选择队列或医生", { shift: 1, time: 2000 });
        }
        else {
            if ($("#r_s_dl").val() != 0) {
                var data = { "ids": ids, "queue_type_id": $("#r_s_dl").val(), "ip": ip };
                ListNum = "dl" + $("#r_s_dl").val();
                var index = layer.index;
                layer.close(index);
                patientQueue("referralQueueType", data);
            }
            else
                layer.msg("请选择队列", { shift: 1, time: 2000 });
        }
    }
    else
        layer.msg("一次只能转诊一个患者", { shift: 1, time: 2000 });
}

function examination()
{
    if ($("#examination_select").val() != 0) {
        var data = { "ids": ids, "queue_type_id": ListNum.replace("dl", ""), "id": $("#examination_select").val(), "ip": ip };
        var index = layer.index;
        layer.close(index);
        patientQueue("examination", data);
    }
    else
        layer.msg("请选择叫号器或医生", { shift: 1, time: 2000 });
}
function examination_cannel() {
    var data = { "ids": ids, "ip": ip };
    //var index = layer.index;
    //layer.close(index);
    patientQueue("noexamination", data);
}

function Manual() {
    var name = $("#patientName").val();
    var id = $("#manual_select").val();
    var json = { "patientName": name, "QueueNumber": id, "ip": ip };
    
    if (name != "" && id != "") {
        var index = layer.index;
        layer.close(index);
        $.ajax({
            type: "POST",
            async: true,
            url: "/clientPatientQueue/Manual",
            dataType: "json",
            data: json,
            success: function (data) {
                if (data.data.code == 0) {
                    ListNum ="dl"+ id;
                    ListQueueType();
                }
                layer.msg(data.data.message, { shift: 1, time: 2000 });
            },
            error: function () {
                layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
            }
        });
    }
    else
    {
        layer.msg("患者姓名和队列不能为空", { shift: 1, time: 2000 });
    }
}
var doc_pb_list=null;
//医生排班
function getRltDoctor() {
    $.ajax({
        type: "POST",
        async: true,
        url: "/paiban/DoctorOrPagerPaiban",
        dataType: "json",
        data:{"ip":ip},
        success: function (data) {
            if (data.return_code == "success") {
                
                doc_pb_list=data.list;
                show_doc_pb(data.list);
            }
            //layer.msg(data.return_msg, { shift: 1, time: 2000 });
        }
    });
}
//搜索医生
function search_doctor()
{
    var doc_key=$("#searchdoctor").val();
    if(doc_key!=null&&doc_key!=undefined)
    {
        var new_pb_list= new Array();
        $.each(doc_pb_list,function(i,item){
            if(item.doctorNmae.indexOf(doc_key)>=0)
            {
                new_pb_list.push(item);
            }
        });
        show_doc_pb(new_pb_list);
    }
    else
        show_doc_pb(doc_pb_list);
}
function show_doc_pb(data)
{
    $("#list_rltdoc").html();
    var html = "";
    var l_d = 0;
    $.each(data, function (i, item) {
        html += "<div class=\"rows\" ><span class=\"w5\"><input onclick=\"check_status('ck_doctor')\" type=\"checkbox\" id=\"d_" + item.id + "\" value=\"" + item.queue_type_id + "\" title=\"" + item.id + "\" name=\"ck_doctor\" />" + item.id + "</span><span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w10\" title=\"" + item.queueTypeName + "\">" + item.queueTypeName + "</span><span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w10\"  title=\"" + item.doctorNmae + "\">" + item.doctorNmae + "</span>";
        var d = item.onduty.split("");
        $.each(d, function (j, li_d) {
            switch (li_d) {
                case '0':
                    html += "<span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w10\">休息</span>";
                    break;
                case '1':
                    html += "<span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w10\">全天</span>";
                    break;
                case '2':
                    html += "<span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w5\">上午</span><span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w5\" class=\"w5\">&nbsp;</span>";
                    break;
                case '3':
                    html += "<span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w5\">&nbsp;</span><span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w5\" class=\"w5\">下午</span>";
                    break;
                default:
                    html += "<span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w5\">&nbsp;</span><span onclick=\"clickcheck('d_" + item.id + "')\" class=\"w5\" class=\"w5\">&nbsp;</span>";
                    break;
            }
        });
        html += "<span class=\"w5\"><i class=\"layui-icon\" style='cursor:pointer;' onclick=\"doc_edit(" + item.id + ",'" + item.onduty + "','id','" + item.queueTypeName + "-" + item.doctorNmae + "')\">&#xe642;</i>&nbsp;&nbsp;<i class=\"layui-icon\"  style='cursor:pointer;'  onclick=\"doc_del(" + item.id + ")\">&#xe640;</i>&nbsp;</span></div>";
        if (d_ids == item.id)
            l_d = i;
    });
    $("#list_rltdoc").html(html);
    if (d_ids != "") {
        var s_d_id = d_ids.substr(0, d_ids.length - 1);
        var l_d_id = s_d_id.split(",");
        $.each(l_d_id, function (i, id) {
            clickcheck(id);
        });
    }
    if (l_d > 10)
        $("#list_rltdoc").animate({ 'scrollTop': '' + (l_d - 10) * 40 + 'px' });
}
function del_confirm() {
    var rr = false;
    return rr;

    
}
function doc_del(id) {
    if (id != "")
        d_ids = "d_" + id + ",";
    if (d_ids != "") {
        layer.confirm('确实要删除吗？', {
            btn: ['确定', '取消'], //按钮
            shade: [0.5, '#fff'] //显示遮罩
        }, function (index) {

            $.ajax({
                type: "POST",
                async: true,
                url: "/paiban/delete_pb",
                dataType: "json",
                data: { "ids": d_ids, "ip": ip },
                success: function (data) {
                    if (data.return_code == "success") {
                        d_ids = "";
                        getRltDoctor();
                    }
                    layer.msg(data.return_msg, { shift: 1, time: 2000 });
                },
                error: function () {
                    layer.msg("操作失败，请联系管理员 ", { shift: 1, time: 2000 });
                }
            });
            layer.close(index);
        });
    }
    else
        layer.msg("请选择要删除的排班信息！", { shift: 1, time: 2000 });
}
//医生排班信息修改

function doc_edit(id,onduty,ids,doc)
{
    if (id != "")
        d_ids = "d_"+id+",";
    layer.open(
        {
            type: 1,
            title: '医生排班信息修改',
            skin: 'win',
            area: ['400px', '400px'],
            shade: [0.3, '#fff'],
            content: '<div class="Paiban"><div class="left">&nbsp;</div><div class="right">' + doc + '</div><div class="left">周一:</div><div class="right"><input type="radio"name="0"value="0"/>休息<input type="radio"name="0"value="1"/>全天<input type="radio"name="0"value="2"/>上午<input type="radio"name="0"value="3"/>下午</div><div class="left">周二:</div><div class="right"><input type="radio"name="1"value="0"/>休息<input type="radio"name="1"value="1"/>全天<input type="radio"name="1"value="2"/>上午<input type="radio"name="1"value="3"/>下午</div><div class="left">周三:</div><div class="right"><input type="radio"name="2"value="0"/>休息<input type="radio"name="2"value="1"/>全天<input type="radio"name="2"value="2"/>上午<input type="radio"name="2"value="3"/>下午</div><div class="left">周四:</div><div class="right"><input type="radio"name="3"value="0"/>休息<input type="radio"name="3"value="1"/>全天<input type="radio"name="3"value="2"/>上午<input type="radio"name="3"value="3"/>下午</div><div class="left">周五:</div><div class="right"><input type="radio"name="4"value="0"/>休息<input type="radio"name="4"value="1"/>全天<input type="radio"name="4"value="2"/>上午<input type="radio"name="4"value="3"/>下午</div><div class="left">周六:</div><div class="right"><input type="radio"name="5"value="0"/>休息<input type="radio"name="5"value="1"/>全天<input type="radio"name="5"value="2"/>上午<input type="radio"name="5"value="3"/>下午</div><div class="left">周日:</div><div class="right"><input type="radio"name="6"value="0"/>休息<input type="radio"name="6"value="1"/>全天<input type="radio"name="6"value="2"/>上午<input type="radio"name="6"value="3"/>下午</div><div class="left">&nbsp;</div><div class="right"><input type="button"class="w50"value="提交"onclick="doc_edit_add()"/></div></div>',
            success: function (id, index) {
                this.enterEsc = function (event) {
                    if (event.keyCode === 13) {
                        doc_edit_add();
                        return false;
                    }
                };
                $(document).on('keydown', this.enterEsc);
                if (onduty != "") {
                    var d = onduty.split("");
                    $.each(d, function (i, li_d) {
                        $(":radio[name='" + i + "'][value='" + li_d + "']").prop("checked", "checked");
                    });
                }
            },
            end: function () {
                $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
            }
        }
    );
}



function doc_edit_add()
{
    if (d_ids != "") {
        var f_onduty = false;
        var onduty = "";
        for (var i = 0; i < 7; i++) {
            //$("input[name='rd']:checked").val();
            var s = $(":radio[name='" + i + "']:checked").val();
            if (s != null && s != "0") {
                onduty += s;
                f_onduty = true;
            }
            else
                onduty += "0";
        }
        if (f_onduty) {
            $.ajax({
                type: "POST",
                async: true,
                url: "/paiban/update_pb",
                dataType: "json",
                data: { "ids": d_ids, "onduty": onduty, "ip": ip },
                success: function (data) {
                    if (data.return_code == "success") {
                        var index = layer.index;
                        layer.close(index);
                        getRltDoctor();
                    }
                    layer.msg(data.return_msg, { shift: 1, time: 2000 });
                },
                error: function () {
                    layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
                }
            });
        }
        else
            layer.msg("至少选择一个非休息排班信息", { shift: 1, time: 2000 });
    }
    else
        layer.msg("请选择要操作的医生", { shift: 1, time: 2000 });
}


function doc_add() {
    var s_html = "<select id=\'d_manual_select\' style='width:150px;overflow:hidden'><option value='0' selected='selected'>请选择</option>";
    $.each(patientList, function (i, item) {
        //if (item.queue_number != ListNum)
        s_html += "<option value='" + item.queue_number + "'>" + item.queue_name + "</option>";
    });
    s_html += "</select>";
    layer.open(
        {
            type: 1,
            title: '医生排班信息添加',
            skin: 'win',
            area: ['400px', '520px'],
            shade: [0.3, '#fff'],
            content: '<div class="Paiban"><div class="left">选择队列:</div><div class="right">' + s_html + '</div><div class="left">选择医生:</div><div class="right"><select id="pb_doc" style="width: 150px;overflow: hidden"></select></div><div class="left">周一:</div><div class="right"><input type="radio"name="0"value="0"/>休息<input type="radio"name="0"value="1"/>全天<input type="radio"name="0"value="2"/>上午<input type="radio"name="0"value="3"/>下午</div><div class="left">周二:</div><div class="right"><input type="radio"name="1"value="0"/>休息<input type="radio"name="1"value="1"/>全天<input type="radio"name="1"value="2"/>上午<input type="radio"name="1"value="3"/>下午</div><div class="left">周三:</div><div class="right"><input type="radio"name="2"value="0"/>休息<input type="radio"name="2"value="1"/>全天<input type="radio"name="2"value="2"/>上午<input type="radio"name="2"value="3"/>下午</div><div class="left">周四:</div><div class="right"><input type="radio"name="3"value="0"/>休息<input type="radio"name="3"value="1"/>全天<input type="radio"name="3"value="2"/>上午<input type="radio"name="3"value="3"/>下午</div><div class="left">周五:</div><div class="right"><input type="radio"name="4"value="0"/>休息<input type="radio"name="4"value="1"/>全天<input type="radio"name="4"value="2"/>上午<input type="radio"name="4"value="3"/>下午</div><div class="left">周六:</div><div class="right"><input type="radio"name="5"value="0"/>休息<input type="radio"name="5"value="1"/>全天<input type="radio"name="5"value="2"/>上午<input type="radio"name="5"value="3"/>下午</div><div class="left">周日:</div><div class="right"><input type="radio"name="6"value="0"/>休息<input type="radio"name="6"value="1"/>全天<input type="radio"name="6"value="2"/>上午<input type="radio"name="6"value="3"/>下午</div><div class="left">&nbsp;</div><div class="right"><input type="button"class="w50"value="提交"onclick="doc_add_save()"/></div></div>',
            success: function (id, index) {
                this.enterEsc = function (event) {
                    if (event.keyCode === 13) {
                        doc_add_save();
                        return false;
                    }
                };
                var onduty = "1111111";
                var d = onduty.split("");
                $.each(d, function (i, li_d) {
                    $(":radio[name='" + i + "'][value='" + li_d + "']").prop("checked", "checked");
                });
                $(document).on('keydown', this.enterEsc);
                $("#pb_doc option").empty();
                $("#pb_doc").append("<option value='0' selected='selected'>请选择</option>");
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/clientdoctor/list_doctor",
                    dataType: "json",
                    data: { "queue_type_id": ListNum.replace("dl", ""), "ip": ip },
                    success: function (data) {
                        if (data.return_code == "success") {
                            if (data.count > 0) {
                                $.each(data.list, function (i, item) {
                                    $("#pb_doc").append("<option value='" + item.doctor_id + "'>" + item.name + "</option>");
                                });
                            }
                        }
                    }
                });
            },
            end: function () {
                $(document).off('keydown', this.enterEsc); //解除键盘关闭事件
            }
        }
    );
}

function doc_add_save()
{
    var doctor_id = $("#pb_doc").val();
    var queue_type_id = $("#d_manual_select").val();
    var f_onduty = false;
    var onduty = "";
    for (var i = 0; i < 7; i++) {
        //$("input[name='rd']:checked").val();
        var s = $(":radio[name='" + i + "']:checked").val();
        if (s != null && s != "0") {
            onduty += s;
            f_onduty = true;
        }
        else
            onduty += "0";
    }
    if (doctor_id != "0" && queue_type_id != "0") {
        if (f_onduty) {
            var index = layer.index;
            layer.close(index);
            $.ajax({
                type: "POST",
                async: true,
                url: "/paiban/DoctorOrPagerPaibanAdd",
                dataType: "json",
                data: { "doctor_id": doctor_id, "queue_type_id": queue_type_id, "onduty": onduty, "ip": ip },
                success: function (data) {
                    if (data.return_code == "success") {
                        var index = layer.index;
                        layer.close(index);
                        getRltDoctor();
                    }
                    layer.msg(data.return_msg, { shift: 1, time: 2000 });
                },
                error: function () {
                    layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
                }
            });
        }
        else
            layer.msg("至少选择一个非休息排班信息", { shift: 1, time: 2000 });
    }
    else {
        layer.msg("请选择医生和队列", { shift: 1, time: 2000 });
    }
}


//读取医生已叫患者
function getDocCaller() {
    $("#call_doc_count").html("0");
    $.ajax({
        type: "POST",
        async: true,
        url: "/qmj/list_content_patient",
        dataType: "json",
        success: function (data) {
            $("#call_list").html("");
            var html = "<div class=\"rows\"><span class=\"w15\">序号</span><span class=\"w20\">科室</span><span class=\"w20\">医生</span><span class=\"w20\">患者</span><span class=\"w25\">操作</span></div>";
            var html2 = "";
            if (data.return_code == "success") {
                $("#call_doc_count").html(data.count);
                if (data.count > 0)
                {
                    $.each(data.list, function (i, item) {
                        html += "<div class=\"rows\"><span class=\"w15\">" + item.register_id + "</span><span class=\"w20\">" + item.queuetypename + "</span><span class=\"w20\">" + item.doctorname + "</span><span class=\"w20\">" + item.patient_name + "</span><span class=\"w25\"><a href=\"javascript:fztCaller(" + item.queue_type_id + "," + item.id + ")\">呼叫</a>&nbsp;<a href=\"javascript:patientQueue('newlyDiagnosed',{\"ids\":" + item.id + "})\">取消</a></span></div>";
                        if (item.istype==1)
                            html2 += "<div class=\"rows2\">" + item.queuetypename + "诊室" + item.doctorname + "医生呼叫" + item.patient_name + "患者</div>";
                    });
                    $("#call_list").html(html);
                    if (html2 != "") {
                        $("#doc_call_rows").html(html2);
                        $("#show_tips").animate({ bottom: '0px' }, 1000).delay(3000).animate({ bottom: '-160px' }, 1000);
                    }
                }
            }
        },
        error: function () {
            layer.msg("操作失败，请联系管理员", { shift: 1, time: 2000 });
        }
    });
}

function call_doc_close() {

    if ($("#call_list").is(":hidden")) {
        $("#call_doc_count_btn").html("&or;")
        $("#call_list").show();
        getDocCaller();
    } else {
        $("#call_doc_count_btn").html("&and;")
        $("#call_list").hide();
    }
}

