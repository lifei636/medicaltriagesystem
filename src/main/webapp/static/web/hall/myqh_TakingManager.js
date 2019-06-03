
//获取url参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

$.ajaxSetup({
    global: false,
    async: true,
    type: "POST",
    dataType: "json",
    timeout: 2000
});

document.oncontextmenu = "return false";

//--------刷卡开始
var keystring = ""; //记录按键的字符串
var isPrint = 0;
var isSwipeCard = 0;
function keypress(e) {
    if (isSwipeCard == 1) return;

    var currKey = 0, CapsLock = 0, e = e || event;
    currKey = e.keyCode || e.which || e.charCode;
    CapsLock = currKey >= 65 && currKey <= 90;

    if (currKey == 13) {
        //获取对应的条形码后执行数据查询
        if (keystring.length > 0) {
            if ($("#serialNum"))
                $("#serialNum").html(keystring);
            isSwipeCard = 1;
            GetQueueList(keystring);
        }
        else {
            showdiv("操作错误，请重新刷卡！");
        }
    }
    else {
        switch (currKey) {
            //屏蔽了退格、制表、回车、空格、方向键、删除键                  
            case 8: case 9: case 16: case 17: case 18: case 32: case 37: case 38: case 39: case 40: case 46: keyName = ""; break;
            default: keyName = String.fromCharCode(currKey); break;
        }

        keystring += keyName;
    }
}

window.onkeydown = keypress;
//--------刷卡结束

var showPaperState = null;
function LowPaperCheck() {
    $("#bg").css("display", "block");
    $("#divConfirm").css("display", "block");
    $("#divAlter").css("display", "block");
    $("#divPrintTest").css("display", "block");
    $("#content").html("取号机缺纸，暂时无法提供取号打印服务，请联系工作人员！");
    showPaperState = setInterval(paperCheck, 1000);
}

function paperCheck() {
    var checkPaper = checkpaper();
    if (checkPaper == 1) {
        if (document.getElementById("bg") != null && document.getElementById("bg").style.display == "block") {
            $("#bg").css("display", "none");
            $("#divConfirm").css("display", "none");
            $("#divAlter").css("display", "none");
            $("#divPrintTest").css("display", "none");
            $("#content").html("");
        }
        isSwipeCard = 0;
        keystring = "";
        if (showPaperState != null) {
            clearInterval(showPaperState);
        }
    }
    else if (checkPaper == -1) {
        showdiv("取号机忙，请稍候再试！");
    }
}

function printxml(xml) {
    window.Callobject.PrintXml(xml);
}

function PrinterAlert() {
    showdiv("打印机忙，请稍后再试");
}

var isCheck = 0;
function checkpaper() {
    return 1;//window.Callobject.CheckPaper();
}
function HasCheckPaper() {
    showdiv("有纸");
}
function LessCheckPaper() {
    showdiv("缺纸");
}

function printTest(obj) {
    obj.className = "table-tiao3-btn-down";

    setTimeout(function () {
        obj.className = "table-tiao3-btn";
        if (checkpaper() == 1) {
            var printXml = '<?xml version="1.0" encoding="GB18030"?><print_task charset="GB18030/BIG5" line_blank="32" copy="1">';
            printXml += '<line content="text"><text times_height="1" times_width="1" left="0" right="0" underline="0" font="0" align="1">神舟视翰科技</text></line>';
            printXml += '<line content="text"><text times_height="1" times_width="1" left="0" right="0" underline="0" font="0" align="1">神舟视翰科技</text></line>';
            printXml += '<line content="text"><text times_height="1" times_width="1" left="0" right="0" underline="0" font="0" align="1">神舟视翰科技</text></line>';
            printXml += '<line content="text"><text times_height="1" times_width="1" left="0" right="0" underline="0" font="0" align="1">神舟视翰科技</text></line></print_task>';
            var isPaper = printxml(printXml);
            if (isPaper == 0) {
                isSwipeCard = 0;
                keystring = "";
                paperCheck();
            }
        }
    }, 10);
}

function playVoice(content) {
    var callText = "<?xml version=\"1.0\" encoding=\"GB18030\"?>"
        + "<Contents><Content>"
        + "<Voice Num=\"1\" Speed=\"1000\" digitMode=\"2\">"
        + content + " </Voice>"
        + "</Content></Contents>";
    window.Callobject.playVoice(callText);
}