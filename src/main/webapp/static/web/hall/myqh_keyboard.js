/// <reference path="jquery-1.8.2.min.js" />

$(function () {
    var serialNum = $("#serialNum");
    $("#serialOK").bind("vmousedown", function (e) {
        var obj = this;
       // obj.style.background = "#faa";
        setTimeout(function () {
            serialOK();
            //obj.style.background = "white";
            return false;
        }, 200);
    })

    //  $("#keyboard li").mousedown(function () {
    $("#keyboard div").bind("vmousedown", function () {
        var obj = this;
        obj.style.background = "#faa";
        var value = this.getAttribute("data-value");
        if (value == "del") {
            if (serialNum.html().length > 0) {
                serialNum.html(serialNum.html().substr(0, serialNum.html().length - 1))
            }
        } else if (value == "delAll") {
            if (serialNum.html().length > 0) {
                serialNum.html("");
            }
        }
        else {
            serialNum.html(serialNum.html() + value)
        }
        setTimeout(function () {
            obj.style.background = "url(/static/web/hall/myqh_images/btn_back.png) no-repeat";
        }, 200);
    });
})